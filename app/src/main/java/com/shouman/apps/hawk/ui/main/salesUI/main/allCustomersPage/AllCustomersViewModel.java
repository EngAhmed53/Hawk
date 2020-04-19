package com.shouman.apps.hawk.ui.main.salesUI.main.allCustomersPage;

import android.content.Context;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;

class AllCustomersViewModel extends ViewModel {

    private MediatorLiveData<Map<String, String>> mapMediatorLiveData;

    AllCustomersViewModel(Context context, String salesUID) {

        FirebaseCompanyRepo firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();

        FirebaseQueryLiveData allCustomersLiveData =
                new FirebaseQueryLiveData(firebaseCompanyRepo.getSalesMemberCustomersList(context, salesUID));

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
