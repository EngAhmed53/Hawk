package com.shouman.apps.hawk.ui.main.companyUi.company_home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.CompanyRepo;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;

public class CompanyHomeViewModel extends AndroidViewModel {
    private static final String TAG = "CompanyHomeViewModel";
    private final MediatorLiveData<Map<String, String>> mapMediatorLiveData = new MediatorLiveData<>();


    public CompanyHomeViewModel(@NonNull Application application) {
        super(application);
        //company branch liveData
        //company all branches
        DatabaseReference allBranches = CompanyRepo.getCompanyBranchesReference(getApplication());
        FirebaseQueryLiveData allBranchesLiveData = new FirebaseQueryLiveData(allBranches);
        mapMediatorLiveData.addSource(allBranchesLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {

                    AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> values = new HashMap<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                values.put(dataSnapshot1.getKey(), dataSnapshot1.getValue(String.class));
                                Log.e(TAG, "onChanged: " + dataSnapshot1.getValue(String.class));
                            }
                            mapMediatorLiveData.postValue(values);
                        }
                    });
                } else {
                    mapMediatorLiveData.setValue(null);
                    Log.e(TAG, "onChanged: " + "dataSnapshot is null");
                }
            }
        });
    }

    //getter for mapMediatorLiveData
    public MediatorLiveData<Map<String, String>> getMapMediatorLiveData() {
        return mapMediatorLiveData;
    }

}