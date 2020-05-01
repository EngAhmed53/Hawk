package com.shouman.apps.hawk.ui.main.companyUi.navDrawer.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.utils.AppExecutors;

public class ProfileViewModel extends AndroidViewModel {

    private MediatorLiveData<User> companyMediatorLiveData = new MediatorLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        FirebaseCompanyRepo firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
        DatabaseReference companyReference = firebaseCompanyRepo.getCompanyInfo();
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(companyReference);
        companyMediatorLiveData.addSource(firebaseQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        User company = new User();
                        company.setUn(dataSnapshot.child("un").getValue(String.class));
                        company.setCn(dataSnapshot.child("cn").getValue(String.class));
                        company.setCuid(dataSnapshot.child("cUID").getValue(String.class));
                        company.setBuid(dataSnapshot.child("bUID").getValue(String.class));
                        company.setE(dataSnapshot.child("e").getValue(String.class));
                        company.setUt(dataSnapshot.child("ut").getValue(String.class));
                        companyMediatorLiveData.postValue(company);
                    }
                });
            }
        });
    }

    MediatorLiveData<User> getCompanyMediatorLiveData() {
        return companyMediatorLiveData;
    }
}
