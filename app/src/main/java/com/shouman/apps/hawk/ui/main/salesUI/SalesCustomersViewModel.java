package com.shouman.apps.hawk.ui.main.salesUI;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.Objects;

public class SalesCustomersViewModel extends AndroidViewModel {

    private MediatorLiveData<BiMap<String, Customer>> customersMapMediatorLiveData;

    public SalesCustomersViewModel(@NonNull Application application) {
        super(application);

        String salesUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseCompanyRepo firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();


        FirebaseQueryLiveData customersMapLiveData =
                new FirebaseQueryLiveData(firebaseCompanyRepo.getSalesmanCustomersList(application, salesUID));


        customersMapMediatorLiveData = new MediatorLiveData<>();
        customersMapMediatorLiveData.addSource(customersMapLiveData, dataSnapshot ->

                AppExecutors.getsInstance().getNetworkIO().execute(() -> {
                    HashBiMap<String, Customer> customersBiMap = HashBiMap.create();
                    for (DataSnapshot d :
                            dataSnapshot.getChildren()) {
                        Customer customer = d.getValue(Customer.class);
                        String customerUID = d.getKey();
                        customersBiMap.put(customerUID, customer);
                    }
                    customersMapMediatorLiveData.postValue(customersBiMap);
                }));
    }

    public LiveData<BiMap<String, Customer>> getCustomersMediatorLiveData() {
        return customersMapMediatorLiveData;
    }
}
