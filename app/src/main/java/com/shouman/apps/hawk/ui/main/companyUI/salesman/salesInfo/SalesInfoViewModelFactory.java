package com.shouman.apps.hawk.ui.main.companyUI.salesman.salesInfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SalesInfoViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private String salesUID;

    public SalesInfoViewModelFactory(String salesUID) {
        this.salesUID = salesUID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SalesInfoViewModel(salesUID);
    }
}
