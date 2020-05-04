package com.shouman.apps.hawk.ui.main.companyUI.all_sales_members.moveSales;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.utils.AppExecutors;

public class AllBranchesViewModel extends AndroidViewModel {

    private static final String TAG = "CompanyHomeViewModel";
    private final MediatorLiveData<BiMap<String, String>> mapMediatorLiveData = new MediatorLiveData<>();


    public AllBranchesViewModel(@NonNull Application application) {
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
                            BiMap<String, String> values = HashBiMap.create();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                values.put(dataSnapshot1.getKey(), dataSnapshot1.getValue(String.class));
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
    MediatorLiveData<BiMap<String, String>> getMapMediatorLiveData() {
        return mapMediatorLiveData;
    }

}
