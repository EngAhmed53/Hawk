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

    //get company branches list
    public static DatabaseReference getCompanyBranchesReference(Context context) {
        String userUID = UserPreference.getUserUID(context);
        String userPath = UserPreference.getUserType(context);
        Log.e(TAG, "getCompanyBranchesReference : " + userPath + " " + userUID );
        return database.getReference().child(userPath).child(userUID).child("branchesList");
    }

    //return branch reference
    public static DatabaseReference getBranchDetailsReference(String branchUID) {
        return branchesReference.child(branchUID);
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
        database.getReference().child(userPath).child(userUID).child("branchesList").updateChildren(newValue);
    }
}
