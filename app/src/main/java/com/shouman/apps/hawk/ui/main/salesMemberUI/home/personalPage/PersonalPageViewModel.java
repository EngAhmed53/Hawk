package com.shouman.apps.hawk.ui.main.salesMemberUI.home.personalPage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseSalesRepo;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.utils.AppExecutors;

public class PersonalPageViewModel extends AndroidViewModel {

    private MediatorLiveData<User> userMediatorLiveData = new MediatorLiveData<>();

    public PersonalPageViewModel(@NonNull Application application) {
        super(application);
        FirebaseSalesRepo salesRepo = FirebaseSalesRepo.getInstance();
        DatabaseReference salesUserReference = salesRepo.getCustomerInfoReference();
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(salesUserReference);
        userMediatorLiveData.addSource(firebaseQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        User sales = new User();
                        sales.setUn(dataSnapshot.child("un").getValue(String.class));
                        sales.setCn(dataSnapshot.child("cn").getValue(String.class));
                        sales.setCuid(dataSnapshot.child("cUID").getValue(String.class));
                        sales.setBuid(dataSnapshot.child("bUID").getValue(String.class));
                        sales.setE(dataSnapshot.child("e").getValue(String.class));
                        sales.setUt(dataSnapshot.child("ut").getValue(String.class));
                        userMediatorLiveData.postValue(sales);
                    }
                });
            }
        });
    }

    MediatorLiveData<User> getUserMediatorLiveData() {
        return userMediatorLiveData;
    }
}

