package com.shouman.apps.hawk.ui.main.companyUi.sales_members;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SalesDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String salesUID;
    private Context mContext;

    public SalesDetailsViewModelFactory(Context context, String salesUID) {
        this.salesUID = salesUID;
        this.mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SalesDetailsViewModel(mContext, salesUID);
    }
}
