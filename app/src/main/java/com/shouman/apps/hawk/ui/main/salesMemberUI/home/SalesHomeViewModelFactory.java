package com.shouman.apps.hawk.ui.main.salesMemberUI.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SalesHomeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String salesUID;
    private Context mContext;

    public SalesHomeViewModelFactory(Context context,@NonNull String salesUID) {
        this.salesUID = salesUID;
        this.mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SalesHomeViewModel(mContext, salesUID);
    }
}
