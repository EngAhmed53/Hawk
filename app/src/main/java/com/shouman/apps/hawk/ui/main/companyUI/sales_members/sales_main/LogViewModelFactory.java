package com.shouman.apps.hawk.ui.main.companyUI.sales_members.sales_main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LogViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String salesUID;
    private Context mContext;

    public LogViewModelFactory(Context context, String salesUID) {
        this.salesUID = salesUID;
        this.mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LogViewModel(mContext, salesUID);
    }
}
