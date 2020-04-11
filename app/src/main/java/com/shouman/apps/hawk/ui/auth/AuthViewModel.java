package com.shouman.apps.hawk.ui.auth;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.AuthRepo;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.model.User;
import com.shouman.apps.hawk.utils.AppExecutors;

public class AuthViewModel extends AndroidViewModel {
    private static final String TAG = "AuthViewModel";

    private FirebaseUser firebaseUser;

    private MediatorLiveData<User> userMediatorLiveData = new MediatorLiveData<>();

    //this is for the barCode captured from the scanner;
    private MutableLiveData<Barcode> barCodesMediatorLiveData = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
    }


    void setupMediatorLiveData() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Log.e(TAG, "setupMediatorLiveData: ");
            final String userUID = firebaseUser.getUid();
            FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(AuthRepo.getUserReference(userUID));
            userMediatorLiveData.addSource(firebaseQueryLiveData, new Observer<DataSnapshot>() {
                @Override
                public void onChanged(final DataSnapshot dataSnapshot) {
                    AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            User user = dataSnapshot.getValue(User.class);
                            if (user == null || user.getE() == null) {
                                user = AuthRepo.CreateNewUser(userUID, firebaseUser.getEmail());
                            } else {
                                Log.e(TAG, "run: " + "user is exist");
                            }
                            userMediatorLiveData.postValue(user);
                        }
                    });
                }
            });
        }
    }

    MediatorLiveData<User> getUserMediatorLiveData() {
        return userMediatorLiveData;
    }

    void updateTheUserInDatabase(User mainUser) {
        AuthRepo.updateTheUserInDatabase(firebaseUser.getUid(), mainUser);
    }

    void setBarCodesArray(Barcode barCode) {
        barCodesMediatorLiveData.postValue(barCode);
    }

    MutableLiveData<Barcode> getBarCodesMediatorLiveData() {
        return barCodesMediatorLiveData;
    }
}
