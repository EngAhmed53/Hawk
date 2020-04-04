package com.shouman.apps.hawk.ui.main.salesMemberUI.home.allCustomersPage;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.CompanyRepo;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AllCustomersViewModel extends AndroidViewModel {

    private MediatorLiveData<Map<String, String>> mapMediatorLiveData;

    public AllCustomersViewModel(@NonNull Application application) {
        super(application);
        String salesUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseQueryLiveData allCustomersLiveData = new FirebaseQueryLiveData(CompanyRepo.getSalesMemberCustomersList(application, salesUID));
        mapMediatorLiveData = new MediatorLiveData<>();
        mapMediatorLiveData.addSource(allCustomersLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> allCustomers = new HashMap<>();
                        for (DataSnapshot d :
                                dataSnapshot.getChildren()) {

                            String key = d.getKey();
                            String customersData = d.getValue(String.class);
                            allCustomers.put(key, customersData);
                        }
                        mapMediatorLiveData.postValue(allCustomers);
                    }
                });
            }
        });
    }


    MediatorLiveData<Map<String, String>> getMapMediatorLiveData() {
        return mapMediatorLiveData;
    }
}
