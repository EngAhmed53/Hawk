package com.shouman.apps.hawk.data;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Objects;

public class CompanyRepo {
    private static final String TAG = "CompanyRepo";

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference companiesReference = database.getReference().child("data");
    private static DatabaseReference usersReference = database.getReference().child("users");

    //get company branches list
    public static DatabaseReference getCompanyBranchesReference(Context context) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("BList");
    }


    //return branch sales member list reference
    public static DatabaseReference getBranchSalesMembersList(Context context, String branchUID) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("B").child(branchUID).child("SM");
    }

    //return sales member customers list reference
    public static DatabaseReference getSalesMemberCustomersList(Context context, String salesUID) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("ST").child(salesUID).child("cList");
    }

    //return customer reference
    public static DatabaseReference getCustomerReference(Context context, String customerUID) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("C").child(customerUID);
    }

    //add new branch to company
    public static void addNewBranchToMyCompany(Context context, String branchName) {
        String cUID = UserPreference.getCompanyUID(context);
        DatabaseReference branchesListReference = companiesReference.child(cUID).child("BList");
        DatabaseReference branchesReference = companiesReference.child(cUID).child("B");

        String newBranchKey = branchesListReference.push().getKey();

        branchesListReference.child(newBranchKey).setValue(branchName);
        branchesReference.child(newBranchKey).child("n").setValue(branchName);

    }

    // return company reference;
    public static DatabaseReference getCompanyInfo() {
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        return usersReference.child(userUID);
    }

    public static void addNewCompanyToDatabase(String cUID) {
        companiesReference.child(cUID).setValue(null);
    }
}
