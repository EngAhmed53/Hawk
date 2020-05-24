package com.shouman.apps.hawk.data.database.firebaseRepo;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.data.model.DayActivity;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.data.model.Visit;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseSalesRepo {
    private static FirebaseSalesRepo firebaseSalesRepo;
    private UserPreference userPreference;
    private FirebaseDatabase database;
    private DatabaseReference companiesReference;
    //private DatabaseReference usersReference;
    private static final Object LOCK = new Object();

    private FirebaseSalesRepo() {
        userPreference = UserPreference.getInstance();
        database = FirebaseDatabase.getInstance();
        companiesReference = database.getReference().child("data");
        //usersReference = database.getReference().child("users");
    }

    public static FirebaseSalesRepo getInstance() {
        if (firebaseSalesRepo == null) {
            synchronized (LOCK) {
                firebaseSalesRepo = new FirebaseSalesRepo();
            }
        }
        return firebaseSalesRepo;
    }

    synchronized public void uploadCustomers(Context context, Customer customer, String customerKey) {
        String companyUID = userPreference.getCompanyUID(context);

        DatabaseReference customersReference =
                companiesReference.child(companyUID).child("C");
        DatabaseReference activityReference =
                companiesReference.child(companyUID).child("Activities");

        customersReference.child(customerKey).setValue(customer);

        DayActivity activity = new DayActivity(
                Common.ACTIVITY_NEW_CUSTOMER,
                customer.getAddedByName(),
                customerKey,
                customer.getN(),
                new Date().getTime());

        activityReference.child(String.valueOf(Common.getCurrentDateWithoutTime().getTime())).push().setValue(activity);
    }

    synchronized public void uploadVisits(Context context, Visit visit, String customerUID, String customerName) {
        String companyUID = userPreference.getCompanyUID(context);
        //add this visit to customer visits list
        DatabaseReference customerReference = companiesReference.child(companyUID).child("C").child(customerUID);
        customerReference.child("visitList").push().setValue(visit);


        Date date = new Date();
        String salesName = userPreference.getUserName(context);

        //activity reference
        DatabaseReference activityReference = companiesReference.child(companyUID).child("Activities");

        //make an activity object
        DayActivity activity = new DayActivity(
                Common.ACTIVITY_NEW_VISIT,
                salesName,
                customerUID,
                customerName,
                date.getTime());

        Date currentDate = Common.getCurrentDateWithoutTime();

        //push the new activity
        activityReference.child(String.valueOf(currentDate.getTime())).push().setValue(activity);
    }


    synchronized public void uploadLocalLog(Context context, String date, DailyLogEntry dailyLogEntry) {
        String companyUID = userPreference.getCompanyUID(context);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference salesMemberCustomersLogReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cLog");
        salesMemberCustomersLogReference.child(date).push().setValue(dailyLogEntry);
    }

    //add new customer to database
    synchronized public void addNewCustomerToDatabase(Context context, Customer customer) {
        String companyUID = userPreference.getCompanyUID(context);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference customersReference = companiesReference.child(companyUID).child("C");
        DatabaseReference salesMemberCustomersLogReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cLog");
        DatabaseReference activityReference = companiesReference.child(companyUID).child("Activities");

        String newCustomerKey = customersReference.push().getKey();

        //add customer to the node
        assert newCustomerKey != null;
        customersReference.child(newCustomerKey).setValue(customer);

        //add this new customer to salesMember customersLog
        //get the date
        Date date = new Date();
        Date dateOnly = Common.getCurrentDateWithoutTime();

        DailyLogEntry dailyLogEntry =
                new DailyLogEntry(customer.getN(),
                        customer.getCn(),
                        true,
                        date.getTime(),
                        newCustomerKey);

        salesMemberCustomersLogReference
                .child(String.valueOf(dateOnly.getTime()))
                .push()
                .setValue(dailyLogEntry);

        DayActivity activity = new DayActivity(
                Common.ACTIVITY_NEW_CUSTOMER,
                customer.getAddedByName(),
                newCustomerKey,
                customer.getN(),
                date.getTime());

        activityReference.child(String.valueOf(dateOnly.getTime())).push().setValue(activity);
    }


    synchronized void addNewSalesMemberToDatabase(Context context, String userUID, User mainUser) {
        String companyUID = userPreference.getCompanyUID(context);
        DatabaseReference companyReference = database.getReference().child("data").child(mainUser.getCuid());
        DatabaseReference branchSalesMembersListReference = companyReference.child("B").child(mainUser.getBuid()).child("SM");
        DatabaseReference companySalesTeamReference = companyReference.child("ST");
        DatabaseReference activityReference = companiesReference.child(companyUID).child("Activities");

        //add this new member to its branch
        Map<String, Object> newSalesman = new HashMap<>();
        newSalesman.put("name", mainUser.getUn());
        newSalesman.put("status", mainUser.isStatus());
        branchSalesMembersListReference.child(userUID).updateChildren(newSalesman);

        //add this new member to the company sales team in the root
        Map<String, Object> newCompanySalesMember = new HashMap<>();
        newCompanySalesMember.put(userUID, null);
        companySalesTeamReference.updateChildren(newCompanySalesMember);

        Date date = new Date();
        Date currentDateWithoutTime = Common.getCurrentDateWithoutTime();

        //make an activity object
        DayActivity activity = new DayActivity(
                Common.ACTIVITY_NEW_SALESMAN,
                mainUser.getUn(),
                userUID,
                null,
                date.getTime());

        //push the new activity
        activityReference.child(String.valueOf(currentDateWithoutTime.getTime())).push().setValue(activity);
    }

    synchronized public void addNewVisitReport(Context context, Visit visit, String customerUID, String customerName, String companyName) {
        String companyUID = userPreference.getCompanyUID(context);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


        //add this visit to customer visits list
        DatabaseReference customerReference = companiesReference.child(companyUID).child("C").child(customerUID);
        customerReference.child("visitList").push().setValue(visit);

        Date date = new Date();

        //add this visit as a logDataEntry to the sales member day log
        DailyLogEntry dailyLogEntry =
                new DailyLogEntry(customerName,
                        companyName,
                        false,
                        date.getTime(),
                        customerUID);

        Date currentDate = Common.getCurrentDateWithoutTime();
        DatabaseReference salesMemberCustomersLogReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cLog");

        salesMemberCustomersLogReference
                .child(String.valueOf(currentDate.getTime()))
                .push()
                .setValue(dailyLogEntry);

        //activity reference
        DatabaseReference activityReference = companiesReference.child(companyUID).child("Activities");
        String salesName = userPreference.getUserName(context);

        //make an activity object
        DayActivity activity = new DayActivity(
                Common.ACTIVITY_NEW_VISIT,
                salesName,
                customerUID,
                customerName,
                date.getTime());

        //push the new activity
        activityReference.child(String.valueOf(currentDate.getTime())).push().setValue(activity);
    }

    synchronized public DatabaseReference getCurrentDayLog(Context context, String salesUID, String date) {
        String companyUID = userPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("ST").child(salesUID).child("cLog").child(date);
    }
}
