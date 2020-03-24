package com.shouman.apps.hawk.ui.main.companyUi.branches;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BranchDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String branchUID;
    private Context mContext;

    public BranchDetailsViewModelFactory(Context context, String branchUID) {
        this.branchUID = branchUID;
        this.mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BranchDetailsViewModel(mContext, branchUID);
    }
}
