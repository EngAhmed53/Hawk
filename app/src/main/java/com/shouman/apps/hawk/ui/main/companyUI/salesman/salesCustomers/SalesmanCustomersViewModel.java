package com.shouman.apps.hawk.ui.main.companyUI.salesman.salesCustomers;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

class SalesmanCustomersViewModel extends ViewModel {

    private MediatorLiveData<List<Customer>> salesCustomersMediatorLiveData;

    SalesmanCustomersViewModel(Context application, String salesUID) {
        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();

        FirebaseQueryLiveData salesCustomersQueryLiveData = new FirebaseQueryLiveData(companyRepo.getSalesmanCustomersList(application, salesUID));

        salesCustomersMediatorLiveData = new MediatorLiveData<>();

        salesCustomersMediatorLiveData.addSource(salesCustomersQueryLiveData, dataSnapshot -> AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            List<Customer> customerList = new ArrayList<>();
            if (dataSnapshot != null) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    Customer customer = child.getValue(Customer.class);
                    if (customer != null) {
                        customer.setUid(key);
                        customerList.add(customer);
                    }
                }
                salesCustomersMediatorLiveData.postValue(customerList);
            }
        }));

    }

    LiveData<List<Customer>> getSalesCustomersLiveData() {
        return salesCustomersMediatorLiveData;
    }
}
