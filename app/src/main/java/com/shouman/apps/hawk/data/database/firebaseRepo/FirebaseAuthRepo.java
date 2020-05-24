package com.shouman.apps.hawk.data.database.firebaseRepo;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.utils.AppExecutors;

public class FirebaseAuthRepo {

    private DatabaseReference usersReference;
    private FirebaseCompanyRepo companyRepo;
    private FirebaseSalesRepo salesRepo;
    private static final Object LOCK = new Object();
    private static FirebaseAuthRepo firebaseAuthRepo;

    public static FirebaseAuthRepo getInstance() {
        if (firebaseAuthRepo == null) {
            synchronized (LOCK) {
                firebaseAuthRepo = new FirebaseAuthRepo();
            }
        }
        return firebaseAuthRepo;
    }


    private FirebaseAuthRepo() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersReference = database.getReference().child("users");
        companyRepo = FirebaseCompanyRepo.getInstance();
        salesRepo = FirebaseSalesRepo.getInstance();
    }

    synchronized public DatabaseReference getUserReference(String userUID) {
        return usersReference.child(userUID);
    }

    //create new user and push it to database
    synchronized public User createNewUser(String userUID, String userEmail) {
        User user = new User();
        user.setE(userEmail);
        usersReference.child(userUID).setValue(user);
        return user;
    }

    synchronized public void updateTheUserInDatabase(Context context, final String userUID, final User mainUser) {
        AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            //update the user in the users database
            usersReference.child(userUID).setValue(mainUser);

            //add new company or sales member account
            if (mainUser.getUt().equals("company_account")) {
                companyRepo.addNewCompanyToDatabase(mainUser.getCuid());
            } else if (mainUser.getUt().equals("sales_account")) {
                salesRepo.addNewSalesMemberToDatabase(context, userUID, mainUser);
            }
        });
    }

    public void addNewTokenToCompanyUser(String userUID, String newToken) {
        usersReference.child(userUID).child("NT").child(newToken).setValue(true);
    }
}
