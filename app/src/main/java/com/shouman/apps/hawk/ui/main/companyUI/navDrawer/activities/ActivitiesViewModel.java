package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.DayActivity;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitiesViewModel extends AndroidViewModel {

    private MediatorLiveData<Map<Long, List<DayActivity>>> activitiesMediatorLiveData = new MediatorLiveData<>();

    public ActivitiesViewModel(@NonNull Application application) {
        super(application);

        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();
        DatabaseReference activitiesReference = companyRepo.getAllActivities(application);

        FirebaseQueryLiveData activitiesLiveData = new FirebaseQueryLiveData(activitiesReference);


        activitiesMediatorLiveData.addSource(activitiesLiveData, dataSnapshot -> {
            if (dataSnapshot != null) {
                AppExecutors.getsInstance().getNetworkIO().execute(() ->
                {
                    Map<Long, List<DayActivity>> day_activities_map = new HashMap<>();
                    for (DataSnapshot daysDataSnapshot : dataSnapshot.getChildren()) {
                        List<DayActivity> dailyActivities = new ArrayList<>();
                        String dateKey = daysDataSnapshot.getKey();
                        assert dateKey != null;

                        // to get each day log
                        for (DataSnapshot activitiesSnapshot : daysDataSnapshot.getChildren()) {
                            DayActivity activity = activitiesSnapshot.getValue(DayActivity.class);
                            dailyActivities.add(activity);
                        }
                        day_activities_map.put(Long.parseLong(dateKey), dailyActivities);
                    }
                    activitiesMediatorLiveData.postValue(day_activities_map);
                });
            }
        });
    }


    LiveData<Map<Long, List<DayActivity>>> getActivitiesMediatorLiveData() {
        return activitiesMediatorLiveData;
    }
}
