package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.customers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class AllCustomersViewModel extends AndroidViewModel {

    private MediatorLiveData<List<Customer>> mapMediatorLiveData;

    public AllCustomersViewModel(@NonNull Application application) {
        super(application);

        FirebaseCompanyRepo firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();

        FirebaseQueryLiveData allCustomersLiveData =
                new FirebaseQueryLiveData(firebaseCompanyRepo.getCompanyCustomersReference(application));

        mapMediatorLiveData = new MediatorLiveData<>();
        mapMediatorLiveData.addSource(allCustomersLiveData, dataSnapshot -> AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            List<Customer> allCustomers = new ArrayList<>();
            for (DataSnapshot d :
                    dataSnapshot.getChildren()) {
                Customer companyCustomer = d.getValue(Customer.class);
                allCustomers.add(companyCustomer);
            }
            mapMediatorLiveData.postValue(allCustomers);
        }));
    }


    MediatorLiveData<List<Customer>> getMapMediatorLiveData() {
        return mapMediatorLiveData;
    }
}
