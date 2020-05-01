package com.shouman.apps.hawk.ui.auth;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseAuthRepo;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.utils.AppExecutors;

public class AuthViewModel extends AndroidViewModel {
    private static final String TAG = "AuthViewModel";

    private FirebaseUser firebaseUser;

    private MediatorLiveData<User> userMediatorLiveData = new MediatorLiveData<>();

    private FirebaseAuthRepo firebaseAuthRepo;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        firebaseAuthRepo = FirebaseAuthRepo.getInstance();
    }


    void setupMediatorLiveData() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            final String userUID = firebaseUser.getUid();
            Log.e(TAG, "setupMediatorLiveData: " + userUID);
            FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(firebaseAuthRepo.getUserReference(userUID));
            userMediatorLiveData.addSource(firebaseQueryLiveData, dataSnapshot -> AppExecutors.getsInstance().getNetworkIO().execute(() -> {
                User user = dataSnapshot.getValue(User.class);
                if (user == null || user.getE() == null) {
                    user = firebaseAuthRepo.createNewUser(userUID, firebaseUser.getEmail());
                } else {
                    Log.e(TAG, "run: " + "user is exist");
                }
                userMediatorLiveData.postValue(user);
            }));
        }
    }

    MediatorLiveData<User> getUserMediatorLiveData() {
        return userMediatorLiveData;
    }

    void updateTheUserInDatabase(User mainUser) {
        firebaseAuthRepo.updateTheUserInDatabase(firebaseUser.getUid(), mainUser);
    }

}
