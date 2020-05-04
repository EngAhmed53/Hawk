package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.SalesListItem;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;

public class HomeViewModel extends AndroidViewModel {

    private MediatorLiveData<Map<String, Map<String, SalesListItem>>> allSalesMembersMediatorLiveData;
    private MutableLiveData<BiMap<String, String>> allBranchesDetails;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(
                FirebaseCompanyRepo
                        .getInstance()
                        .getCompanyBranchesDetailsReference(application)
        );

        Map<String, Map<String, SalesListItem>> branch_salesMembers = new HashMap<>();

        BiMap<String, String> branchesDetails = HashBiMap.create();

        allBranchesDetails = new MutableLiveData<>();

        allSalesMembersMediatorLiveData = new MediatorLiveData<>();

        allSalesMembersMediatorLiveData.addSource(firebaseQueryLiveData, dataSnapshot ->
                AppExecutors.getsInstance().getNetworkIO().execute(() -> {

                    for (DataSnapshot branchDataSnapShot : dataSnapshot.getChildren()) {

                        String branchUID = branchDataSnapShot.getKey();

                        String branchName = branchDataSnapShot.child("n").getValue(String.class);

                        DataSnapshot salesMembersSnapshot = branchDataSnapShot.child("SM");

                        Map<String, SalesListItem> salesMembers = new HashMap<>();

                        for (DataSnapshot salesMemberSnapshot : salesMembersSnapshot.getChildren()) {
                            salesMembers.put(salesMemberSnapshot.getKey(), salesMemberSnapshot.getValue(SalesListItem.class));
                        }

                        String key = branchUID + ", " + branchName;

                        branchesDetails.put(branchUID, branchName);
                        branch_salesMembers.put(key, salesMembers);
                    }

                    allBranchesDetails.postValue(branchesDetails);
                    allSalesMembersMediatorLiveData.postValue(branch_salesMembers);
                }));
    }

    MediatorLiveData<Map<String, Map<String, SalesListItem>>> getAllSalesMembersMediatorLiveData() {
        return allSalesMembersMediatorLiveData;
    }

    public LiveData<BiMap<String, String>> getAllBranchesDetails() {
        return allBranchesDetails;
    }
}
