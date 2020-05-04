package com.shouman.apps.hawk.ui.main.companyUI.sales_members.sales_info;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.utils.AppExecutors;

class SalesInfoViewModel extends ViewModel {

    private MediatorLiveData<User> salesMediatorLiveData;

    SalesInfoViewModel(String salesUID) {
        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();
        FirebaseQueryLiveData userLiveData = new FirebaseQueryLiveData(companyRepo.getSalesMemberInfo(salesUID));
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
