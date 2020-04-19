package com.shouman.apps.hawk.ui.main.salesUI.add.newVisit;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseSalesRepo;
import com.shouman.apps.hawk.data.database.localRepo.LocalSalesRepo;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

class VisitsViewModel extends ViewModel {

    private MediatorLiveData<BiMap<String, String>> customersMapMediatorLiveData;
    private MediatorLiveData<List<String>> currentDayLogMediatorLiveData;


    VisitsViewModel(Context mContext, String salesUID) {

        //String currentDate = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(new Date());
        String currentDate = String.valueOf(Common.getCurrentDateWithoutTime().getTime());

        FirebaseSalesRepo salesRepo = FirebaseSalesRepo.getInstance();
        FirebaseCompanyRepo firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
        LocalSalesRepo localSalesRepo = LocalSalesRepo.getInstance();

        final LiveData<List<String>> currentDayLocalLogLiveData = localSalesRepo.getCurrentDayLocalLog(currentDate);

        FirebaseQueryLiveData customersMapLiveData =
                new FirebaseQueryLiveData(firebaseCompanyRepo.getSalesMemberCustomersList(mContext, salesUID));

        FirebaseQueryLiveData currentDayLogFirebaseLiveData = new FirebaseQueryLiveData(salesRepo.getCurrentDayLog(mContext, salesUID, currentDate));

        currentDayLogMediatorLiveData = new MediatorLiveData<>();
        currentDayLogMediatorLiveData.addSource(currentDayLogFirebaseLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<String> allDayLog = new ArrayList<>();
                        for (DataSnapshot d :
                                dataSnapshot.getChildren()) {
                            allDayLog.add(d.getKey());
                        }
                        currentDayLogMediatorLiveData.postValue(allDayLog);
                    }
                });
            }
        });
        currentDayLogMediatorLiveData.addSource(currentDayLocalLogLiveData, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> localLogCustomersUIDList) {
                currentDayLogMediatorLiveData.setValue(localLogCustomersUIDList);
            }
        });

        customersMapMediatorLiveData = new MediatorLiveData<>();
        customersMapMediatorLiveData.addSource(customersMapLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        HashBiMap<String, String> customersKeyValueBiMap = HashBiMap.create();
                        for (DataSnapshot d :
                                dataSnapshot.getChildren()) {
                            String customerData = d.getValue(String.class);
                            String customerUID = d.getKey();
                            customersKeyValueBiMap.put(customerUID, customerData);
                        }
                        customersMapMediatorLiveData.postValue(customersKeyValueBiMap);
                    }
                });
            }
        });
    }

    MediatorLiveData<BiMap<String, String>> getCustomersMapMediatorLiveData() {
        return customersMapMediatorLiveData;
    }

    MediatorLiveData<List<String>> getCurrentDayLogMediatorLiveData() {
        return currentDayLogMediatorLiveData;
    }
}
