package com.shouman.apps.hawk.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.model.User;
import com.shouman.apps.hawk.utils.AppExecutors;

public class AuthRepo {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference usersReference = database.getReference().child("users");

    public static DatabaseReference getUserReference(String userUID) {
        return usersReference.child(userUID);
    }

    //create new user and push it to database
    public static User CreateNewUser(String userUID, String userEmail) {
        User user = new User();
        user.setE(userEmail);
        usersReference.child(userUID).setValue(user);
        return user;
    }

    public static void updateTheUserInDatabase(final String userUID, final User mainUser) {
        AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {
                //update the user in the users database
               usersReference.child(userUID).setValue(mainUser);

               //add new company or sales member account
               if (mainUser.getUt().equals("company_account")) {
                   CompanyRepo.addNewCompanyToDatabase(mainUser.getCuid());
               } else if (mainUser.getUt().equals("sales_account")) {
                   SalesRepo.addNewSalesMemberToDatabase(userUID, mainUser);
               }
            }
        });
    }
}
