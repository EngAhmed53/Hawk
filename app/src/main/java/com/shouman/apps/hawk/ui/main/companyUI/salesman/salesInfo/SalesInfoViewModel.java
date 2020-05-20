package com.shouman.apps.hawk.ui.main.companyUI.salesman.salesInfo;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

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
        salesMediatorLiveData.addSource(userLiveData, dataSnapshot -> AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            User salesUser = dataSnapshot.getValue(User.class);
            salesMediatorLiveData.postValue(salesUser);
        }));
    }
    MediatorLiveData<User> getSalesMediatorLiveData() {
        return salesMediatorLiveData;
    }
}
