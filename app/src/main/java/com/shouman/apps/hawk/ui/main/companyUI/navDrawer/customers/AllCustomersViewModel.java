package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.customers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.utils.AppExecutors;

public class AllCustomersViewModel extends AndroidViewModel {

    private MediatorLiveData<BiMap<String, Customer>> mapMediatorLiveData;

    public AllCustomersViewModel(@NonNull Application application) {
        super(application);

        FirebaseCompanyRepo firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();

        FirebaseQueryLiveData allCustomersLiveData =
                new FirebaseQueryLiveData(firebaseCompanyRepo.getCompanyCustomersReference(application));

        mapMediatorLiveData = new MediatorLiveData<>();
        mapMediatorLiveData.addSource(allCustomersLiveData, dataSnapshot -> AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            BiMap<String, Customer> allCustomers = HashBiMap.create();
            for (DataSnapshot d :
                    dataSnapshot.getChildren()) {
                String key = d.getKey();
                Customer companyCustomer = d.getValue(Customer.class);
                allCustomers.put(key, companyCustomer);
            }
            mapMediatorLiveData.postValue(allCustomers);
        }));
    }


    LiveData<BiMap<String, Customer>> getMapMediatorLiveData() {
        return mapMediatorLiveData;
    }
}
