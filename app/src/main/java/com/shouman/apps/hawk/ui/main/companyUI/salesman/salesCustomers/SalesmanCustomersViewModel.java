package com.shouman.apps.hawk.ui.main.companyUI.salesman.salesCustomers;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.utils.AppExecutors;

class SalesmanCustomersViewModel extends ViewModel {

    private MediatorLiveData<BiMap<String, Customer>> salesCustomersMediatorLiveData;

    SalesmanCustomersViewModel(Context application, String salesUID) {
        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();

        FirebaseQueryLiveData salesCustomersQueryLiveData = new FirebaseQueryLiveData(companyRepo.getSalesmanCustomersList(application, salesUID));

        salesCustomersMediatorLiveData = new MediatorLiveData<>();

        salesCustomersMediatorLiveData.addSource(salesCustomersQueryLiveData, dataSnapshot -> AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            BiMap<String, Customer> customerList = HashBiMap.create();
            if (dataSnapshot != null) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    Customer customer = child.getValue(Customer.class);
                    if (customer != null) customerList.put(key, customer);
                }
                salesCustomersMediatorLiveData.postValue(customerList);
            }
        }));

    }

    LiveData<BiMap<String, Customer>> getSalesCustomersMediatorLiveData() {
        return salesCustomersMediatorLiveData;
    }
}
