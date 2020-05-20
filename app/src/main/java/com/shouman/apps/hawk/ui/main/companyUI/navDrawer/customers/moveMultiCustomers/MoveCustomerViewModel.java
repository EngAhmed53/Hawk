package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.customers.moveMultiCustomers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.utils.AppExecutors;

public class MoveCustomerViewModel extends AndroidViewModel {

    private final MediatorLiveData<BiMap<String, String>> salesmenMediatorLiveData = new MediatorLiveData<>();


    public MoveCustomerViewModel(@NonNull Application application) {
        super(application);
        FirebaseCompanyRepo firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();

        DatabaseReference allBranches = firebaseCompanyRepo.getCompanyBranchesDetailsReference(application);
        FirebaseQueryLiveData allBranchesLiveData = new FirebaseQueryLiveData(allBranches);
        salesmenMediatorLiveData.addSource(allBranchesLiveData, dataSnapshot -> {

            if (dataSnapshot != null) {
                AppExecutors.getsInstance().getNetworkIO().execute(() -> {

                    BiMap<String, String> salesmen_Names_UIDs = HashBiMap.create();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        DataSnapshot salesList = dataSnapshot1.child("SM");
                        for (DataSnapshot salesman: salesList.getChildren()) {
                            String uid = salesman.getKey();
                            String name = salesman.child("name").getValue(String.class);
                            salesmen_Names_UIDs.put(uid, name);
                        }
                    }
                    salesmenMediatorLiveData.postValue(salesmen_Names_UIDs);
                });

            } else {
                salesmenMediatorLiveData.setValue(null);
            }
        });
    }

    //getter for mapMediatorLiveData
    LiveData<BiMap<String, String>> getSalesmenMediatorLiveData() {
        return salesmenMediatorLiveData;
    }

}
