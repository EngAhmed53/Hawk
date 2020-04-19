package com.shouman.apps.hawk.data.database.firebaseRepo;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.data.model.Visit;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseSalesRepo {
    private static FirebaseSalesRepo firebaseSalesRepo;
    private FirebaseDatabase database;
    private DatabaseReference companiesReference;
    private DatabaseReference usersReference;
    private static final Object LOCK = new Object();

    private FirebaseSalesRepo() {
        database = FirebaseDatabase.getInstance();
        companiesReference = database.getReference().child("data");
        usersReference = database.getReference().child("users");
    }

    public static FirebaseSalesRepo getInstance() {
        if (firebaseSalesRepo == null) {
            synchronized (LOCK) {
                firebaseSalesRepo = new FirebaseSalesRepo();
            }
        }
        return firebaseSalesRepo;
    }

    synchronized public void uploadCustomers(Context context, Customer customer, String key) {
        String companyUID = UserPreference.getCompanyUID(context);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference salesMemberCustomersListReference =
                companiesReference
                        .child(companyUID)
                        .child("ST")
                        .child(userUID)
                        .child("cList");

        DatabaseReference customersReference =
                companiesReference.child(companyUID).child("C");

        customersReference.child(key).setValue(customer);


        Map<String, Object> newValue = new HashMap<>();
        String value = customer.getN() + ", " + customer.getCn() + ", " + customer.getLt() + ", " + customer.getLn();
        newValue.put(key, value);

        salesMemberCustomersListReference
                .updateChildren(newValue);
    }

    synchronized public void uploadVisits(Context context, Visit visit, String customerUID) {
        String companyUID = UserPreference.getCompanyUID(context);
        //add this visit to customer visits list
        DatabaseReference customerReference = companiesReference.child(companyUID).child("C").child(customerUID);
        customerReference.child("visitList").push().setValue(visit);
    }


    synchronized public void uploadLocalLog(Context context, String date, DailyLogEntry dailyLogEntry) {
        String companyUID = UserPreference.getCompanyUID(context);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference salesMemberCustomersLogReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cLog");
        salesMemberCustomersLogReference.child(date).push().setValue(dailyLogEntry);
    }

    //add new customer to database
    synchronized public void addNewCustomerToDatabase(Context context, Customer customer) {
        String companyUID = UserPreference.getCompanyUID(context);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference customersReference = companiesReference.child(companyUID).child("C");
        DatabaseReference salesMemberCustomersLogReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cLog");
        DatabaseReference salesMemberCustomersListReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cList");

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

        //add this new customer to sales member customersList
        Map<String, Object> newValue = new HashMap<>();
        String value = customer.getN() + ", " + customer.getCn() + ", " + customer.getLt() + ", " + customer.getLn();
        newValue.put(newCustomerKey, value);

        salesMemberCustomersListReference
                .updateChildren(newValue);
    }


    synchronized void addNewSalesMemberToDatabase(String userUID, User mainUser) {
        DatabaseReference companyReference = database.getReference().child("data").child(mainUser.getCuid());
        DatabaseReference branchSalesMembersListReference = companyReference.child("B").child(mainUser.getBuid()).child("SM");
        DatabaseReference companySalesTeamReference = companyReference.child("ST");

        //add this new member to its branch
        Map<String, Object> newBranchSalesMember = new HashMap<>();
        newBranchSalesMember.put(userUID, mainUser.getUn());
        branchSalesMembersListReference.updateChildren(newBranchSalesMember);

        //add this new member to the company sales team in the root
        Map<String, Object> newCompanySalesMember = new HashMap<>();
        newBranchSalesMember.put(userUID, null);
        companySalesTeamReference.updateChildren(newCompanySalesMember);
    }

    synchronized public DatabaseReference getCustomerInfoReference() {
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        return usersReference.child(userUID);
    }

    synchronized public void addNewVisitReport(Context context, Visit visit, String customerUID, String customerName, String companyName) {
        String companyUID = UserPreference.getCompanyUID(context);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


        //add this visit to customer visits list
        DatabaseReference customerReference = companiesReference.child(companyUID).child("C").child(customerUID);
        customerReference.child("visitList").push().setValue(visit);

        //add this visit as a logDataEntry to the sales member day log
        DailyLogEntry dailyLogEntry =
                new DailyLogEntry(customerName,
                        companyName,
                        false,
                        new Date().getTime(),
                        customerUID);

        Date currentDate = Common.getCurrentDateWithoutTime();
        DatabaseReference salesMemberCustomersLogReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cLog");

        salesMemberCustomersLogReference
                .child(String.valueOf(currentDate.getTime()))
                .push()
                .setValue(dailyLogEntry);
    }

    synchronized public DatabaseReference getCurrentDayLog(Context context, String salesUID, String date) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("ST").child(salesUID).child("cLog").child(date);
    }
}
