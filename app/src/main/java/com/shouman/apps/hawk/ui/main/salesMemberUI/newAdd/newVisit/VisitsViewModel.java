package com.shouman.apps.hawk.ui.main.salesMemberUI.newAdd.newVisit;

import android.content.Context;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.CompanyRepo;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.SalesRepo;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class VisitsViewModel extends ViewModel {

    private MediatorLiveData<BiMap<String, String>> customersMapMediatorLiveData;
    private MediatorLiveData<List<String>> currentDayLogMediatorLiveData;


    VisitsViewModel(Context mContext, String salesUID) {
        FirebaseQueryLiveData customersMapLiveData = new FirebaseQueryLiveData(CompanyRepo.getSalesMemberCustomersList(mContext, salesUID));

        String date = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(new Date());
        FirebaseQueryLiveData allCurrentDayLogLiveData = new FirebaseQueryLiveData(SalesRepo.getCurrentDayLog(mContext, salesUID, date));
        currentDayLogMediatorLiveData = new MediatorLiveData<>();
        currentDayLogMediatorLiveData.addSource(allCurrentDayLogLiveData, new Observer<DataSnapshot>() {
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
