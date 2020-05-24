package com.shouman.apps.hawk.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseAuthRepo;
import com.shouman.apps.hawk.preferences.UserPreference;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
           String userType = UserPreference.getInstance().getUserType(getApplicationContext());

          if (userType != null && userType.equals("company_account")) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userUID = user.getUid();
                    FirebaseAuthRepo.getInstance().addNewTokenToCompanyUser(userUID, s);
                }
            }
            Log.e("newToken", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
