package com.shouman.apps.hawk.ui.main.salesMemberUI.newCustomer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shouman.apps.hawk.model.Customer;

public class NewCustomerSharedViewModel extends ViewModel {

    private MutableLiveData<Customer> customerMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Customer> getCustomerMutableLiveData() {
        return customerMutableLiveData;
    }

    public void setCustomerMutableLiveData(Customer customer) {
        this.customerMutableLiveData.setValue(customer);
    }
}
