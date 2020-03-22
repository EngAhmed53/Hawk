package com.shouman.apps.hawk.ui.main.companyUi.customers;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomersViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private String customerUID;

    public CustomersViewModelFactory(String customerUID) {
        this.customerUID = customerUID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CustomersViewModel(customerUID);
    }
}
