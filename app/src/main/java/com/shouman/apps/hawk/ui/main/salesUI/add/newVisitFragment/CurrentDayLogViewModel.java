package com.shouman.apps.hawk.ui.main.salesUI.add.newVisitFragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseSalesRepo;
import com.shouman.apps.hawk.data.database.localRepo.LocalSalesRepo;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CurrentDayLogViewModel extends AndroidViewModel {

    private MediatorLiveData<List<String>> currentDayLogMediatorLiveData;

    public CurrentDayLogViewModel(@NonNull Application application) {
        super(application);

        String currentDate = String.valueOf(Common.getCurrentDateWithoutTime().getTime());
        String salesUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseSalesRepo salesRepo = FirebaseSalesRepo.getInstance();
        LocalSalesRepo localSalesRepo = LocalSalesRepo.getInstance();

        final LiveData<List<String>> currentDayLocalLogLiveData = localSalesRepo.getCurrentDayLocalLog(currentDate);

        FirebaseQueryLiveData currentDayLogFirebaseLiveData = new FirebaseQueryLiveData(salesRepo.getCurrentDayLog(application, salesUID, currentDate));

        currentDayLogMediatorLiveData = new MediatorLiveData<>();
        currentDayLogMediatorLiveData.addSource(currentDayLogFirebaseLiveData, dataSnapshot ->
                AppExecutors.getsInstance().getNetworkIO().execute(() -> {

                    List<String> currentDayLogCustomersKey = new ArrayList<>();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        DailyLogEntry dailyLogEntry = d.getValue(DailyLogEntry.class);
                        if(dailyLogEntry != null) currentDayLogCustomersKey.add(dailyLogEntry.getCUID());
                    }
                    currentDayLogMediatorLiveData.postValue(currentDayLogCustomersKey);
                }));

        currentDayLogMediatorLiveData.addSource(currentDayLocalLogLiveData,
                localLogCustomersUIDList -> currentDayLogMediatorLiveData.setValue(localLogCustomersUIDList));
    }

    MediatorLiveData<List<String>> getCurrentDayLogMediatorLiveData() {
        return currentDayLogMediatorLiveData;
    }
}
