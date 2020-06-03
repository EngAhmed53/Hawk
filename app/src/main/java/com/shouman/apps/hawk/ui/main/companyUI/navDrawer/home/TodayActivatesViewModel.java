package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.DayActivity;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class TodayActivatesViewModel extends AndroidViewModel {

    private MediatorLiveData<List<DayActivity>> todayActivitiesMediatorLiveData = new MediatorLiveData<>();

    public TodayActivatesViewModel(@NonNull Application application) {
        super(application);

        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();
        String currentDate = String.valueOf(Common.getCurrentDateWithoutTime().getTime());
        DatabaseReference currentDayActivitiesRef = companyRepo.getCurrentDayActivities(application, currentDate);

        FirebaseQueryLiveData activitiesLiveData = new FirebaseQueryLiveData(currentDayActivitiesRef);


        todayActivitiesMediatorLiveData.addSource(activitiesLiveData, dataSnapshot -> {
            if (dataSnapshot != null) {
                AppExecutors.getsInstance().getNetworkIO().execute(() ->
                {
                    List<DayActivity> todayActivities = new ArrayList<>();
                    // to get each day log
                    for (DataSnapshot activitiesSnapshot : dataSnapshot.getChildren()) {
                        DayActivity activity = activitiesSnapshot.getValue(DayActivity.class);
                        todayActivities.add(activity);
                    }
                    todayActivitiesMediatorLiveData.postValue(todayActivities);
                });
            }
        });
    }


    LiveData<List<DayActivity>> getTodayActivitiesLiveData() {
        return todayActivitiesMediatorLiveData;
    }
}
