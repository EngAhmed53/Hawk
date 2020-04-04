package com.shouman.apps.hawk.data;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.model.Customer;
import com.shouman.apps.hawk.model.CustomersLogDataEntry;
import com.shouman.apps.hawk.model.User;
import com.shouman.apps.hawk.model.Visit;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SalesRepo {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference companiesReference = database.getReference().child("data");
    private static DatabaseReference usersReference = database.getReference().child("users");


    //add new customer to database
    public static void addNewCustomerToDatabase(Context context, Customer customer) {
        String companyUID = UserPreference.getCompanyUID(context);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference customersReference = companiesReference.child(companyUID).child("C");
        DatabaseReference salesMemberCustomersLogReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cLog");
        DatabaseReference salesMemberCustomersListReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cList");

        String newCustomerKey = customersReference.push().getKey();

        //add customer to the  node
        assert newCustomerKey != null;
        customersReference.child(newCustomerKey).setValue(customer);

        //add this new customer to salesMember customersLog
        //get the date
        String date = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH).format(new Date());
        Log.e("date", "addNewCustomerToDatabase: " + date );
        CustomersLogDataEntry customersLogDataEntry = new CustomersLogDataEntry(customer.getN(),
                customer.getCn(),
                true,
                new Date().getTime());
        salesMemberCustomersLogReference.child(date).child(newCustomerKey).setValue(customersLogDataEntry);


        //add this new customer to sales member customersList
        Map<String, Object> newValue = new HashMap<>();
        String value = customer.getN() + ", " + customer.getCn() + ", " + customer.getLt() + ", " + customer.getLn();
        newValue.put(newCustomerKey, value);

        salesMemberCustomersListReference
                .updateChildren(newValue);
    }


    static void addNewSalesMemberToDatabase(String userUID, User mainUser) {
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

    public static DatabaseReference getCustomerInfoReference() {
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        return usersReference.child(userUID);
    }

    public static boolean addNewVisitReport(Context context, Visit visit, String customerUID, String customerName, String companyName) {
        String companyUID = UserPreference.getCompanyUID(context);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


        //add this visit to customer visits list
        DatabaseReference customerReference = companiesReference.child(companyUID).child("C").child(customerUID);
        customerReference.child("visitList").push().setValue(visit);

        //add this visit as a logDataEntry to the sales member day log
        CustomersLogDataEntry customersLogDataEntry = new CustomersLogDataEntry();
        customersLogDataEntry.setCustomerCompanyName(companyName);
        customersLogDataEntry.setCustomerName(customerName);
        customersLogDataEntry.setNewCustomer(false);
        customersLogDataEntry.setTimeMillieSeconds(new Date().getTime());

        DatabaseReference salesMemberCustomersLogReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cLog");
        String date = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(new Date());
        salesMemberCustomersLogReference.child(date).child(customerUID).setValue(customersLogDataEntry);
        return true;
    }

    public static DatabaseReference getCurrentDayLog(Context context, String salesUID, String date) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("ST").child(salesUID).child("cLog").child(date);
    }
}

