package com.shouman.apps.hawk.data;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.model.Branch;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.HashMap;
import java.util.Map;

public class Repo {
    private static final String TAG = "Repo";

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference branchesReference = database.getReference().child("branches");
    private static DatabaseReference salesMemberReference = database.getReference().child("sales_members");
    private static DatabaseReference customersReference = database.getReference().child("customers");

    //get company branches list
    public static DatabaseReference getCompanyBranchesReference(Context context) {
        String userUID = UserPreference.getUserUID(context);
        String userPath = UserPreference.getUserType(context);
        return database.getReference().child(userPath).child(userUID).child("b");
    }

    //return branch reference
    public static DatabaseReference getBranchDetailsReference(String branchUID) {
        return branchesReference.child(branchUID);
    }

    //return branch sales member list reference
    public static DatabaseReference getBranchSalesMembersList(String branchUID) {
        return branchesReference.child(branchUID).child("salesMemberList");
    }

    //return sales member customers list reference
    public static DatabaseReference getSalesMemberCustomersList(String salesUID) {
        return salesMemberReference.child(salesUID).child("customersLog");
    }

    //return customer reference
    public static DatabaseReference getCustomerReference(String customerUID) {
        return customersReference.child(customerUID);
    }

    //add new branch to company
    public static void addNewBranchToMyCompany(Context context, String branchName) {
        String userUID = UserPreference.getUserUID(context);
        String userPath = UserPreference.getUserType(context);

        //get the new branch added UID;
        String newBranchKey = branchesReference.push().getKey();

        //add branch to the branches node
        branchesReference.child(newBranchKey).setValue(new Branch(branchName, new HashMap<String, String>()));

        //add this new branch to company branchesList
        Map<String, Object> newValue = new HashMap<>();
        newValue.put(newBranchKey, branchName);
        database.getReference().child(userPath).child(userUID).child("b").updateChildren(newValue);
    }

    // return company reference;
    public static DatabaseReference getCompanyReference(Context context) {
        String userUID = UserPreference.getUserUID(context);
        Log.e(TAG, "getCompanyReference: " + userUID);
        return database.getReference().child("companies").child(userUID);
    }
}
