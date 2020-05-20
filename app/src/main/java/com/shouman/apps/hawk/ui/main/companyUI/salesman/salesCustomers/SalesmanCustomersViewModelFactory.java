package com.shouman.apps.hawk.ui.main.companyUI.salesman.salesCustomers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SalesmanCustomersViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application application;
    private String salesUID;

    public SalesmanCustomersViewModelFactory(Application application, String salesUID) {
        this.salesUID = salesUID;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SalesmanCustomersViewModel(application, salesUID);
    }
}
