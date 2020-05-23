package com.shouman.apps.hawk.ui.main.salesUI.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.shouman.apps.hawk.data.database.localRepo.LocalSalesRepo;

public class LocalDataViewModel extends ViewModel {

    private LiveData<Integer> totalLocalLogLiveData;

    public LocalDataViewModel() {
        LocalSalesRepo localSalesRepo = LocalSalesRepo.getInstance();
        totalLocalLogLiveData = localSalesRepo.getTotalLocalLogLiveData();
    }

    LiveData<Integer> getLocalLogLiveData() {
        return totalLocalLogLiveData;
    }
}
