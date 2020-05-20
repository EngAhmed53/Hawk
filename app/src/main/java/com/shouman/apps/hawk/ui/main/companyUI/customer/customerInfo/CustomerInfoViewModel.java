package com.shouman.apps.hawk.ui.main.companyUI.customer.customerInfo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.utils.AppExecutors;

class CustomerInfoViewModel extends ViewModel {
    private MediatorLiveData<Customer> customerMediatorLiveData;

    CustomerInfoViewModel(Context context, String customerUID) {
        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();
        FirebaseQueryLiveData customerLiveData = new FirebaseQueryLiveData(companyRepo.getCustomerReference(context, customerUID));
        customerMediatorLiveData = new MediatorLiveData<>();
        customerMediatorLiveData.addSource(customerLiveData, dataSnapshot -> AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            Customer customer = dataSnapshot.getValue(Customer.class);
            customerMediatorLiveData.postValue(customer);
        }));
    }

    LiveData<Customer> getCustomerMediatorLiveData() {
        return customerMediatorLiveData;
    }
}
