package com.shouman.apps.hawk.ui.main.salesUI.home22.allCustomersPage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AllCustomersViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Context mContext;
    private String salesUID;

    public AllCustomersViewModelFactory(Context mContext, String salesUID) {
        this.mContext = mContext;
        this.salesUID = salesUID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AllCustomersViewModel(mContext, salesUID);
    }
}
