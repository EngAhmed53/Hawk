package com.shouman.apps.hawk.ui.main.companyUi.sales_members.sales_info;

import android.content.Context;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.CompanyRepo;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.model.User;
import com.shouman.apps.hawk.utils.AppExecutors;

class SalesInfoViewModel extends ViewModel {

    private MediatorLiveData<User> salesMediatorLiveData;

    SalesInfoViewModel(String salesUID) {
        FirebaseQueryLiveData userLiveData = new FirebaseQueryLiveData(CompanyRepo.getSalesMemberInfo(salesUID));
        salesMediatorLiveData = new MediatorLiveData<>();
        salesMediatorLiveData.addSource(userLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {

                AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        User salesUser = dataSnapshot.getValue(User.class);
                        salesMediatorLiveData.postValue(salesUser);
                    }
                });
            }
        });
    }

    MediatorLiveData<User> getSalesMediatorLiveData() {
        return salesMediatorLiveData;
    }
}
