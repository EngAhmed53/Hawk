package com.shouman.apps.hawk.ui.main.companyUI.all_branches.branch_details.branch_home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;

class BranchViewModel extends ViewModel {
    private static final String TAG = "BranchDetailsViewModel";
    private MediatorLiveData<Map<String, String>> mediatorBranchLiveData = new MediatorLiveData<>();


    BranchViewModel(Context context, String branchUID) {
        FirebaseCompanyRepo firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
        //set the branch uid
        DatabaseReference branchReference = firebaseCompanyRepo.getBranchSalesMembersList(context, branchUID);
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(branchReference);
        mediatorBranchLiveData.addSource(firebaseQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> allSalesMap = new HashMap<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String key = dataSnapshot1.getKey();
                                String value = dataSnapshot1.getValue(String.class);
                                allSalesMap.put(key, value);
                                mediatorBranchLiveData.postValue(allSalesMap);
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "onChanged: DataSnapShot is null");
                }
            }
        });
    }

    MediatorLiveData<Map<String, String>> getMediatorBranchLiveData() {
        return mediatorBranchLiveData;
    }
}
