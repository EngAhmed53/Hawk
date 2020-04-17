package com.shouman.apps.hawk.ui.main.companyUi.all_branches;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;

public class CompanyHomeViewModel extends AndroidViewModel {

    private static final String TAG = "CompanyHomeViewModel";
    private final MediatorLiveData<Map<String, String>> mapMediatorLiveData = new MediatorLiveData<>();


    public CompanyHomeViewModel(@NonNull Application application) {
        super(application);
        FirebaseCompanyRepo firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
        //company branch liveData
        //company all branches
        DatabaseReference allBranches = firebaseCompanyRepo.getCompanyBranchesReference(application);
        FirebaseQueryLiveData allBranchesLiveData = new FirebaseQueryLiveData(allBranches);
        mapMediatorLiveData.addSource(allBranchesLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "run: " + "i am in the excuter");
                            Map<String, String> values = new HashMap<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                values.put(dataSnapshot1.getKey(), dataSnapshot1.getValue(String.class));
                                Log.e(TAG, "run: i am getting data from data base" + dataSnapshot1.getValue());
                            }
                            mapMediatorLiveData.postValue(values);
                        }
                    });
                } else {
                    mapMediatorLiveData.setValue(null);
                }
            }
        });
    }

    //getter for mapMediatorLiveData
    MediatorLiveData<Map<String, String>> getMapMediatorLiveData() {
        return mapMediatorLiveData;
    }

}
