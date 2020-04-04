package com.shouman.apps.hawk.ui.main.companyUi.customers.customerInfo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomersViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private String customerUID;
    private Context mContext;

    public CustomersViewModelFactory(Context context, String customerUID) {
        this.customerUID = customerUID;
        this.mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CustomersViewModel(mContext, customerUID);
    }
}
