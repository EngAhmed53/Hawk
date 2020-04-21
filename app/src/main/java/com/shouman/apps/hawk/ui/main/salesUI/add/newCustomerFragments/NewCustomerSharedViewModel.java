package com.shouman.apps.hawk.ui.main.salesUI.add.newCustomerFragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shouman.apps.hawk.data.model.Customer;

public class NewCustomerSharedViewModel extends ViewModel {
    public NewCustomerSharedViewModel() {
    }

    private MutableLiveData<Customer> customerMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Customer> getCustomerMutableLiveData() {
        return customerMutableLiveData;
    }

    void setCustomerMutableLiveData(Customer customer) {
        this.customerMutableLiveData.setValue(customer);
    }
}
