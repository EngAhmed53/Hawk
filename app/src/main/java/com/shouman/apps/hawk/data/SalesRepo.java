package com.shouman.apps.hawk.data;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.model.Customer;
import com.shouman.apps.hawk.model.User;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SalesRepo {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference companiesReference = database.getReference().child("data");



    //add new customer to database
    public static void addNewCustomerToDatabase(Context context, Customer customer) {
        String companyUID = UserPreference.getCompanyUID(context);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference customersReference = companiesReference.child(companyUID).child("C");
        DatabaseReference salesMemberReference = companiesReference.child(companyUID).child("ST").child(userUID).child("cList");

        String newCustomerKey = customersReference.push().getKey();

        //add customer to the  node
        assert newCustomerKey != null;
        customersReference.child(newCustomerKey).setValue(customer);

        //add this new customer to salesMember customersList
        //get the date
        String date = SimpleDateFormat.getDateInstance().format(new Date());
        //update the values in database
        Map<String, Object> newValue = new HashMap<>();
        newValue.put(newCustomerKey, customer.getCn());

        salesMemberReference
                .child(date)
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

        //add this new member to the company sales teem in the root
        Map<String, Object> newCompanySalesMember = new HashMap<>();
        newBranchSalesMember.put(userUID, null);
        companySalesTeamReference.updateChildren(newCompanySalesMember);
    }
}
