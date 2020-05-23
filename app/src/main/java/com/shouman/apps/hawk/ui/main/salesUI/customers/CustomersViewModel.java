package com.shouman.apps.hawk.ui.main.salesUI.customers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomersViewModel extends AndroidViewModel {

    private MediatorLiveData<Map<String, Customer>> salesCustomersMediatorLiveData;

    public CustomersViewModel(@NonNull Application application) {
        super(application);
        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();

        String salesUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseQueryLiveData salesCustomersQueryLiveData = new FirebaseQueryLiveData(companyRepo.getSalesmanCustomersList(application, salesUID));

        salesCustomersMediatorLiveData = new MediatorLiveData<>();

        salesCustomersMediatorLiveData.addSource(salesCustomersQueryLiveData, dataSnapshot -> AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            Map<String, Customer> customerList = new HashMap<>();
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

    LiveData<Map<String, Customer>> getSalesCustomersMediatorLiveData() {
        return salesCustomersMediatorLiveData;
    }
}
