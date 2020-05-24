package com.shouman.apps.hawk.data.database.mainRepo;

import android.content.Context;
import android.util.Log;

import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseSalesRepo;
import com.shouman.apps.hawk.data.database.localRepo.LocalSalesRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.data.model.Visit;
import com.shouman.apps.hawk.network.NetworkUtils;
import com.shouman.apps.hawk.utils.AppExecutors;

public class MainRepo {
    private static final String TAG = "MainRepo";
    private static MainRepo mainRepo;
    private final static Object LOCK = new Object();
    // private LocalCompanyRepo localCompanyRepo;
    private LocalSalesRepo localSalesRepo;
    private FirebaseSalesRepo firebaseSalesRepo;
    //private FirebaseCompanyRepo firebaseCompanyRepo;

    public static MainRepo getInstance() {

        if (mainRepo == null) {
            synchronized (LOCK) {
                mainRepo = new MainRepo();
            }
        }
        return mainRepo;
    }

    private MainRepo() {
        //this.localCompanyRepo = LocalCompanyRepo.getInstance();
        this.localSalesRepo = LocalSalesRepo.getInstance();
        //this.firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
        this.firebaseSalesRepo = FirebaseSalesRepo.getInstance();
    }

    synchronized public void addNewCustomer(final Context context, final Customer customer) {
        AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            if (NetworkUtils.isConnectedToInternet(context)) {
                firebaseSalesRepo.addNewCustomerToDatabase(context, customer);
                Log.e(TAG, "addNewCustomer: customer added to firebase database");
            } else {
                localSalesRepo.addNewCustomerToLocalDatabase(context, customer);
                Log.e(TAG, "addNewCustomer: customer added to local database");
            }
        });

    }

    synchronized public void addVisitToCustomer(final Context context, final Visit visit, final String customerUID, final String customerName, final String companyName) {
        AppExecutors.getsInstance().getNetworkIO().execute(() -> {
            if (NetworkUtils.isConnectedToInternet(context)) {
                firebaseSalesRepo.addNewVisitReport(context, visit, customerUID, customerName, companyName);
                Log.e(TAG, "addNewCustomer: visit added to firebase database");
            } else {
                localSalesRepo.addNewVisitReportToLocalDatabase(visit, customerUID, customerName, companyName);
                Log.e(TAG, "addNewCustomer: visit added to local database");
            }
        });

    }
}
