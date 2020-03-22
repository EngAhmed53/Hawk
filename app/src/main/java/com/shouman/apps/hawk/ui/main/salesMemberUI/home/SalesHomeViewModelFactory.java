package com.shouman.apps.hawk.ui.main.salesMemberUI.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SalesHomeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String salesUID;

    public SalesHomeViewModelFactory(String salesUID) {
        this.salesUID = salesUID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SalesHomeViewModel(salesUID);
    }
}
