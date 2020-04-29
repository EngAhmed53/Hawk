package com.shouman.apps.hawk.ui.main.companyUi.all_sales_members;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.SalesListItem;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;

public class AllSalesMembersViewModel extends AndroidViewModel {

    private MediatorLiveData<Map<String, Map<String, SalesListItem>>> allSalesMembersMediatorLiveData;

    public AllSalesMembersViewModel(@NonNull Application application) {
        super(application);
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(
                FirebaseCompanyRepo
                        .getInstance()
                        .getCompanyBranchesDetailsReference(application)
        );

        allSalesMembersMediatorLiveData = new MediatorLiveData<>();
        allSalesMembersMediatorLiveData.addSource(firebaseQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {

                AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final Map<String, Map<String, SalesListItem>> branch_salesMembers = new HashMap<>();
                        for (DataSnapshot branchDataSnapShot : dataSnapshot.getChildren()) {
                            String branchUID = branchDataSnapShot.getKey();
                            String branchName = branchDataSnapShot.child("n").getValue(String.class);
                            DataSnapshot salesMembersSnapshot = branchDataSnapShot.child("SM");
                            Map<String, SalesListItem> salesMembers = new HashMap<>();
                            for (DataSnapshot salesMemberSnapshot : salesMembersSnapshot.getChildren()) {
                                salesMembers.put(salesMemberSnapshot.getKey(), salesMemberSnapshot.getValue(SalesListItem.class));
                            }
                            String key = branchUID + ", " + branchName;
                            branch_salesMembers.put(key, salesMembers);
                        }
                        allSalesMembersMediatorLiveData.postValue(branch_salesMembers);
                    }
                });
            }
        });
    }

    MediatorLiveData<Map<String, Map<String, SalesListItem>>> getAllSalesMembersMediatorLiveData() {
        return allSalesMembersMediatorLiveData;
    }
}
