package com.shouman.apps.hawk.data.database.firebaseRepo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shouman.apps.hawk.preferences.UserPreference;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseCompanyRepo {

    public interface OnSalesMemberDeleteAction {
        void onDeleteSuccess();

        void onDeleteFailed();
    }

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


    synchronized public DatabaseReference getCompanyCustomersReference(Context context) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("C");
    }

    synchronized public DatabaseReference getCompanyBranchesDetailsReference(Context context) {
        String companyUID = UserPreference.getCompanyUID(context);
        return companiesReference.child(companyUID).child("B");
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

        assert newBranchKey != null;
        branchesListReference.child(newBranchKey).setValue(branchName);
        branchesReference.child(newBranchKey).child("n").setValue(branchName);

    }

    // return company reference;
    synchronized public DatabaseReference getCompanyInfo() {
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        return usersReference.child(userUID);
    }

    synchronized void addNewCompanyToDatabase(String cUID) {
        companiesReference.child(cUID).setValue(null);
    }

    synchronized public DatabaseReference getSalesMemberInfo(String salesUID) {
        return usersReference.child(salesUID);
    }

    synchronized public void moveSalesMemberToAnotherBranch(Context context, final String salesName, final String salesUID, final boolean salesStatus, String oldBranchUID, final String newBranchUID) {
        String companyUID = UserPreference.getCompanyUID(context);

        final DatabaseReference oldBranchSalesMember = companiesReference.child(companyUID).child("B").child(oldBranchUID).child("SM");


        final DatabaseReference newBranchSalesMember = companiesReference.child(companyUID).child("B").child(newBranchUID).child("SM");

        AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            //remove sales from the old branch
            oldBranchSalesMember.child(salesUID).removeValue();

            //add sales to the new branch
            Map<String, Object> updatedValue = new HashMap<>();
            updatedValue.put("name", salesName);
            updatedValue.put("status", salesStatus);
            newBranchSalesMember.child(salesUID).updateChildren(updatedValue);

            //change the bUID in the sales user reference
            usersReference.child(salesUID).child("buid").setValue(newBranchUID);
        });
    }

    synchronized public void disableSalesMember(Context context, final String salesUID, String branchUID) {
        String companyUID = UserPreference.getCompanyUID(context);

        final DatabaseReference salesMemberReference = companiesReference.child(companyUID).child("B").child(branchUID).child("SM").child(salesUID);


        AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            salesMemberReference.child("status").setValue(false);
            usersReference.child(salesUID).child("status").setValue(false);
        });
    }

    public void enableSalesMember(Context context, final String salesUID, String branchUID) {
        String companyUID = UserPreference.getCompanyUID(context);

        final DatabaseReference salesMemberReference = companiesReference.child(companyUID).child("B").child(branchUID).child("SM").child(salesUID);


        AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            salesMemberReference.child("status").setValue(true);
            usersReference.child(salesUID).child("status").setValue(true);
        });
    }


    public void deleteSalesMember(Context context, final String salesUID, String branchUID) {
        final String companyUID = UserPreference.getCompanyUID(context);
        OnSalesMemberDeleteAction onSalesMemberDeleteAction = (OnSalesMemberDeleteAction) context;
        final DatabaseReference branchSalesMembersList = companiesReference.child(companyUID).child("B").child(branchUID).child("SM");
        final DatabaseReference companySalesTeam = companiesReference.child(companyUID).child("ST").child(salesUID);


        AppExecutors.getsInstance().getNetworkIO().execute(() ->
                companiesReference.child(companyUID).child("ST").child(salesUID).child("cList").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long childCounts = dataSnapshot.getChildrenCount();
                        if (childCounts > 0) {
                            AppExecutors.getsInstance().getMainThread().execute(onSalesMemberDeleteAction::onDeleteFailed);
                        } else {
                            //remove from branch list
                            ////branchSalesMembersList.child(salesUID).removeValue();

                            //remove from company salesTeam"ST"
                            ////companySalesTeam.child(salesUID).removeValue();

                            //remove from users database
                            ////usersReference.child(salesUID).removeValue();

                            //notify the delete is success
                            AppExecutors.getsInstance().getMainThread().execute(onSalesMemberDeleteAction::onDeleteSuccess);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }));
    }
}
