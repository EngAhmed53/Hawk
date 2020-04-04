package com.shouman.apps.hawk.ui.main.salesMemberUI.home.homeFragment;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.CompanyRepo;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.model.CustomersLogDataEntry;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;

class SalesHomeViewModel extends ViewModel {

    private static final String TAG = "SalesHomeViewModel";
    private MediatorLiveData<Map<String, Map<String, CustomersLogDataEntry>>> mediatorSalesLiveData = new MediatorLiveData<>();

    SalesHomeViewModel(Context context, String salesUID) {

        //set the branch uid
        DatabaseReference salesReference = CompanyRepo.getSalesMemberCustomersLog(context, salesUID);
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(salesReference);

        mediatorSalesLiveData.addSource(firebaseQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, Map<String, CustomersLogDataEntry>> dates_customers_map = new HashMap<>();

                            // to get the dates values;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String parentKey = dataSnapshot1.getKey();
                                Map<String, CustomersLogDataEntry> customerMap = new HashMap<>();

                                // to get each date customers list
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    String childKey = dataSnapshot2.getKey();
                                    CustomersLogDataEntry value = dataSnapshot2.getValue(CustomersLogDataEntry.class);
                                    customerMap.put(childKey, value);
                                }

                                dates_customers_map.put(parentKey, customerMap);
                            }
                            mediatorSalesLiveData.postValue(dates_customers_map);
                        }
                    });
                } else {
                    Log.e(TAG, "onChanged: DataSnapShot is null");
                }
            }
        });
    }

    MediatorLiveData<Map<String, Map<String, CustomersLogDataEntry>>> getMediatorSalesLiveData() {
        return mediatorSalesLiveData;
    }
}
