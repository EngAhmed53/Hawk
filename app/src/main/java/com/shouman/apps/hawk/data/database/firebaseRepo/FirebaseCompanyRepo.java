package com.shouman.apps.hawk.data.database.firebaseRepo;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Objects;

public class FirebaseCompanyRepo {

    private DatabaseReference companiesReference;
    private DatabaseReference usersReference;
    private static FirebaseCompanyRepo firebaseCompanyRepo;
    private final static Object LOCK = new Object();

    public static FirebaseCompanyRepo getInstance() {
        if (firebaseCompanyRepo == null) {
            synchronized (LOCK) {
                firebaseCompanyRepo = new FirebaseCompanyRepo();
            }
        }
        return firebaseCompanyRepo;
    }

    private FirebaseCompanyRepo() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        companiesReference = database.getReference().child("data");
        usersReference = database.getReference().child("users");
    }

    //get company branches list
    synchronized public DatabaseReference getCompanyBranchesReference(Context context) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("BList");
    }


    //return branch sales member list reference
    synchronized public DatabaseReference getBranchSalesMembersList(Context context, String branchUID) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("B").child(branchUID).child("SM");
    }

    //return sales member customers list reference
    synchronized public DatabaseReference getSalesMemberCustomersList(Context context, String salesUID) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("ST").child(salesUID).child("cList");
    }

    //return sales member customers log reference
    synchronized public DatabaseReference getSalesMemberCustomersLog(Context context, String salesUID) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("ST").child(salesUID).child("cLog");
    }

    //return customer reference
    synchronized public DatabaseReference getCustomerReference(Context context, String customerUID) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("C").child(customerUID);
    }

    //add new branch to company
    synchronized public void addNewBranchToMyCompany(Context context, String branchName) {
        String cUID = UserPreference.getCompanyUID(context);
        DatabaseReference branchesListReference = companiesReference.child(cUID).child("BList");
        DatabaseReference branchesReference = companiesReference.child(cUID).child("B");

        String newBranchKey = branchesListReference.push().getKey();

        branchesListReference.child(newBranchKey).setValue(branchName);
        branchesReference.child(newBranchKey).child("n").setValue(branchName);

    }

    // return company reference;
    synchronized public DatabaseReference getCompanyInfo() {
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        return usersReference.child(userUID);
    }

    synchronized public void addNewCompanyToDatabase(String cUID) {
        companiesReference.child(cUID).setValue(null);
    }

    synchronized public DatabaseReference getSalesMemberInfo(String salesUID) {
        return usersReference.child(salesUID);
    }
}
