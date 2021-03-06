package com.shouman.apps.hawk.ui.main.salesUI.add.newVisitFragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class VisitViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private String salesUID;
    private Context mContext;

    VisitViewModelFactory(Context context, @NonNull String salesUID) {
        this.salesUID = salesUID;
        this.mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new VisitsViewModel(mContext, salesUID);
    }
}
