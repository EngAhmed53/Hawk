package com.shouman.apps.hawk.ui.main.salesUI.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.Objects;

public class SalesProfileViewModel extends AndroidViewModel {

    private MediatorLiveData<User> salesMediatorLiveData;

    public SalesProfileViewModel(@NonNull Application application) {
        super(application);
        String salesUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();
        FirebaseQueryLiveData userLiveData = new FirebaseQueryLiveData(companyRepo.getSalesMemberInfo(salesUID));
        salesMediatorLiveData = new MediatorLiveData<>();
        salesMediatorLiveData.addSource(userLiveData, dataSnapshot -> AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            User salesUser = dataSnapshot.getValue(User.class);
            if (salesUser != null) {
                salesMediatorLiveData.postValue(salesUser);
            }
        }));
    }

    MediatorLiveData<User> getSalesMediatorLiveData() {
        return salesMediatorLiveData;
    }
}
