package com.shouman.apps.hawk.ui.main.salesUI.main.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
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

class SalesHomeViewModel extends ViewModel {

    private static final String TAG = "SalesHomeViewModel";
    private MediatorLiveData<Map<String, List<DailyLogEntry>>> mediatorSalesLiveData;

    SalesHomeViewModel(Context context, String salesUID) {

        mediatorSalesLiveData = new MediatorLiveData<>();


        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();
        DatabaseReference salesReference = companyRepo.getSalesMemberCustomersLog(context, salesUID);
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(salesReference);

        mediatorSalesLiveData.addSource(firebaseQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, List<DailyLogEntry>> dates_logEntries_map = new HashMap<>();
                            // to get the dates values;
                            for (DataSnapshot datesDataSnapshot : dataSnapshot.getChildren()) {
                                String date = datesDataSnapshot.getKey();
                                List<DailyLogEntry> logEntries = new ArrayList<>();
                                // to get each date customers list
                                for (DataSnapshot LogsDataSnapshot : datesDataSnapshot.getChildren()) {
                                    DailyLogEntry value = LogsDataSnapshot.getValue(DailyLogEntry.class);
                                    logEntries.add(value);
                                }
                                dates_logEntries_map.put(date, logEntries);
                            }
                            mediatorSalesLiveData.postValue(dates_logEntries_map);
                        }
                    });
                } else {
                    Log.d(TAG, "onChanged: DataSnapShot is null");
                }
            }
        });
    }

    MediatorLiveData<Map<String, List<DailyLogEntry>>> getMediatorSalesLiveData() {
        return mediatorSalesLiveData;
    }
}
