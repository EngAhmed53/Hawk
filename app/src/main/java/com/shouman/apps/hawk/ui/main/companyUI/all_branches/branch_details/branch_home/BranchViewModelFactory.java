package com.shouman.apps.hawk.ui.main.companyUI.all_branches.branch_details.branch_home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BranchViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String branchUID;
    private Context mContext;

    public BranchViewModelFactory(Context context, String branchUID) {
        this.branchUID = branchUID;
        this.mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BranchViewModel(mContext, branchUID);
    }
}
