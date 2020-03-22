package com.shouman.apps.hawk.ui.main.companyUi.company_profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.Repo;
import com.shouman.apps.hawk.model.Company;
import com.shouman.apps.hawk.utils.AppExecutors;

public class ProfileViewModel extends AndroidViewModel {

    private MediatorLiveData<Company> companyMediatorLiveData = new MediatorLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        DatabaseReference companyReference = Repo.getCompanyReference(application);
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(companyReference);
        companyMediatorLiveData.addSource(firebaseQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Company company = new Company();
                        company.setU(dataSnapshot.child("u").getValue(String.class));
                        company.setE(dataSnapshot.child("e").getValue(String.class));
                        company.setC(dataSnapshot.child("c").getValue(String.class));
                        companyMediatorLiveData.postValue(company);
                    }
                });
            }
        });
    }

    public MediatorLiveData<Company> getCompanyMediatorLiveData() {
        return companyMediatorLiveData;
    }
}
