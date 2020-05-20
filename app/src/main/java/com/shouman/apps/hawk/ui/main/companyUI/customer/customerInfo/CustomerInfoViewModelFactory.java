package com.shouman.apps.hawk.ui.main.companyUI.customer.customerInfo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomerInfoViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private String customerUID;
    private Context context;

    public CustomerInfoViewModelFactory(Context context, String customerUID) {
        this.customerUID = customerUID;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CustomerInfoViewModel(context, customerUID);
    }
}
