package com.shouman.apps.hawk.ui.main.companyUi.sales_members;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SalesDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String salesUID;

    public SalesDetailsViewModelFactory(String salesUID) {
        this.salesUID = salesUID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SalesDetailsViewModel(salesUID);
    }
}
