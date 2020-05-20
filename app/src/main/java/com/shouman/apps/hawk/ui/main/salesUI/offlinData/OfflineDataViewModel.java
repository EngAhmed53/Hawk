package com.shouman.apps.hawk.ui.main.salesUI.offlinData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.shouman.apps.hawk.data.database.localRepo.LocalSalesRepo;
import com.shouman.apps.hawk.data.model.DailyLogEntry;

import java.util.List;
import java.util.Map;

public class OfflineDataViewModel extends ViewModel {

    private LiveData<Map<String, List<DailyLogEntry>>> localLogLiveData;

    public OfflineDataViewModel() {
        LocalSalesRepo localSalesRepo = LocalSalesRepo.getInstance();
        localLogLiveData = localSalesRepo.getCustomersDailyLogLocalLiveData();
    }

    LiveData<Map<String, List<DailyLogEntry>>> getLocalLogLiveData() {
        return localLogLiveData;
    }
}
