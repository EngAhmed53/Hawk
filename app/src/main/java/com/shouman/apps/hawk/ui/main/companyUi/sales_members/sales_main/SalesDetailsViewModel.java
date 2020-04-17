package com.shouman.apps.hawk.ui.main.companyUi.sales_members.sales_main;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.shouman.apps.hawk.data.FirebaseQueryLiveData;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.CustomersLogDataEntry;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.HashMap;
import java.util.Map;

class SalesDetailsViewModel extends ViewModel {

    private static final String TAG = "SalesHomeViewModel";
    private MediatorLiveData<Map<String, Map<String, CustomersLogDataEntry>>> mediatorSalesLiveData = new MediatorLiveData<>();

     SalesDetailsViewModel(Context context, String salesUID) {

         FirebaseCompanyRepo companyRepo = FirebaseCompanyRepo.getInstance();
        //set the branch uid
        DatabaseReference salesReference = companyRepo.getSalesMemberCustomersLog(context, salesUID);
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
                                String dateKey = dataSnapshot1.getKey();
                                Map<String, CustomersLogDataEntry> customerMap = new HashMap<>();

                                // to get each date customers list
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    String childKey = dataSnapshot2.getKey();
                                    CustomersLogDataEntry log = dataSnapshot2.getValue(CustomersLogDataEntry.class);
                                    customerMap.put(childKey, log);
                                }

                                dates_customers_map.put(dateKey, customerMap);
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
