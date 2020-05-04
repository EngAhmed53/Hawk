package com.shouman.apps.hawk.ui.main.companyUI.sales_members.sales_main;

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

class SalesViewModel extends ViewModel {

    private static final String TAG = "SalesHomeViewModel";
    private MediatorLiveData<Map<String, List<DailyLogEntry>>> mediatorSalesLiveData = new MediatorLiveData<>();

    SalesViewModel(Context context, String salesUID) {

        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();
        //set the branch uid
        DatabaseReference salesReference = companyRepo.getSalesMemberCustomersLog(context, salesUID);
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(salesReference);


        mediatorSalesLiveData.addSource(firebaseQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, List<DailyLogEntry>> date_logEntries_map = new HashMap<>();

                            // to get the dates values;
                            for (DataSnapshot daysDataSnapshot : dataSnapshot.getChildren()) {
                                String dateKey = daysDataSnapshot.getKey();
                                List<DailyLogEntry> logEntries = new ArrayList<>();

                                // to get each date customers list
                                for (DataSnapshot logsDataSnapShot : daysDataSnapshot.getChildren()) {
                                    DailyLogEntry log = logsDataSnapShot.getValue(DailyLogEntry.class);
                                    logEntries.add(log);
                                }
                                date_logEntries_map.put(dateKey, logEntries);
                            }
                            mediatorSalesLiveData.postValue(date_logEntries_map);
                        }
                    });
                } else {
                    Log.e(TAG, "onChanged: DataSnapShot is null");
                }
            }
        });
    }

    MediatorLiveData<Map<String, List<DailyLogEntry>>> getMediatorSalesLiveData() {
        return mediatorSalesLiveData;
    }
}
