package com.shouman.apps.hawk.ui.main.companyUi.branches;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BranchDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String branchUID;

    public BranchDetailsViewModelFactory(String branchUID) {
        this.branchUID = branchUID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BranchDetailsViewModel(branchUID);
    }
}
