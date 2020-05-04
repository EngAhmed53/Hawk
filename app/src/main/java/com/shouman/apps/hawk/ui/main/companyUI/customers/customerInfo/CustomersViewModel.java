package com.shouman.apps.hawk.ui.main.companyUI.customers.customerInfo;

import android.content.Context;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.utils.AppExecutors;

class CustomersViewModel extends ViewModel {

    private static final String TAG = "CustomersViewModel";
    private MediatorLiveData<Customer> customerMediatorLiveData = new MediatorLiveData<>();

    CustomersViewModel(Context context, final String customerUID) {
        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();
        DatabaseReference customerReference = companyRepo.getCustomerReference(context, customerUID);
        FirebaseQueryLiveData customerQueryLiveData = new FirebaseQueryLiveData(customerReference);

        customerMediatorLiveData.addSource(customerQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Customer customer;
                        customer = dataSnapshot.getValue(Customer.class);
                        customerMediatorLiveData.postValue(customer);
                    }
                });
            }
        });
    }

    MediatorLiveData<Customer> getCustomerMediatorLiveData() {
        return customerMediatorLiveData;
    }
}
