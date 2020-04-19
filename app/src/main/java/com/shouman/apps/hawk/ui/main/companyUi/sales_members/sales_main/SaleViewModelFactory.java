package com.shouman.apps.hawk.ui.main.companyUi.sales_members.sales_main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SaleViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String salesUID;
    private Context mContext;

    public SaleViewModelFactory(Context context, String salesUID) {
        this.salesUID = salesUID;
        this.mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SalesViewModel(mContext, salesUID);
    }
}
