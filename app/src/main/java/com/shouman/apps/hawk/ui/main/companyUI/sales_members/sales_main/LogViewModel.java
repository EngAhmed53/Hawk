package com.shouman.apps.hawk.ui.main.companyUI.sales_members.sales_main;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LogViewModel extends ViewModel {

    private MediatorLiveData<Map<Long, List<DailyLogEntry>>> daysLogLiveData = new MediatorLiveData<>();

    LogViewModel(Context context, String salesUID) {

        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();
        DatabaseReference salesReference = companyRepo.getSalesMemberCustomersLog(context, salesUID);

        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(salesReference);


        daysLogLiveData.addSource(firebaseQueryLiveData, dataSnapshot -> {
            if (dataSnapshot != null) {
                AppExecutors.getsInstance().getNetworkIO().execute(() ->
                {
                    Map<Long, List<DailyLogEntry>> day_logEntries_map = new HashMap<>();
                    for (DataSnapshot daysDataSnapshot : dataSnapshot.getChildren()) {
                        List<DailyLogEntry> dailyLogEntries = new ArrayList<>();
                        String dateKey = daysDataSnapshot.getKey();
                        assert dateKey != null;

                        // to get each day log
                        for (DataSnapshot logsDataSnapShot : daysDataSnapshot.getChildren()) {
                            DailyLogEntry entry = logsDataSnapShot.getValue(DailyLogEntry.class);
                            dailyLogEntries.add(entry);
                        }
                        day_logEntries_map.put(Long.parseLong(dateKey), dailyLogEntries);
                        //Log.e(TAG, "LogViewModel: " + day_logEntries_map.keySet().size() );
                    }
                    daysLogLiveData.postValue(day_logEntries_map);

                });
            }
        });
    }

    LiveData<Map<Long, List<DailyLogEntry>>> getDaysLogLiveData() {
        return daysLogLiveData;
    }
}
