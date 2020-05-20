package com.shouman.apps.hawk.data.database.localRepo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.data.model.Visit;
import com.shouman.apps.hawk.preferences.UserPreference;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Book;
import io.paperdb.Paper;

public class LocalSalesRepo {
    private static final String TAG = "LocalSalesRepo";
    private static LocalSalesRepo localSalesRepo;
    private static final Object LOCK = new Object();
    private MutableLiveData<Map<String, List<DailyLogEntry>>> customersDailyLogLocalLiveData;
    private DatabaseReference companiesReference;


    private LocalSalesRepo() {
        Log.e(TAG, "LocalSalesRepo: start setting live data");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        companiesReference = database.getReference().child("data");
        customersDailyLogLocalLiveData = new MutableLiveData<>();

        AppExecutors.getsInstance().getDiskIO().execute(() -> {
            Map<String, List<DailyLogEntry>> allDaysLog = new HashMap<>();

            Book dailyLocalBook = Paper.book("Daily_Log");
            for (String day : dailyLocalBook.getAllKeys()) {
                List<DailyLogEntry> dayLog = dailyLocalBook.read(day);
                allDaysLog.put(day, dayLog);
            }
            customersDailyLogLocalLiveData.postValue(allDaysLog);
        });
    }

    public static LocalSalesRepo getInstance() {

        if (localSalesRepo == null) {
            synchronized (LOCK) {
                localSalesRepo = new LocalSalesRepo();
            }
        }
        return localSalesRepo;
    }

    synchronized public void addNewCustomerToLocalDatabase(Context context, Customer customer) {
        String companyUID = UserPreference.getInstance().getCompanyUID(context);
        DatabaseReference customersReference = companiesReference.child(companyUID).child("C");
        String newCustomerKey = customersReference.push().getKey();

        //add customer to the the local database
        assert newCustomerKey != null;
        Paper.book("New_Customers").write(newCustomerKey, customer);

        // days log
        Date date = new Date();
        Date currentDateOnly = Common.getCurrentDateWithoutTime();
        String currentDateMillSecond = String.valueOf(currentDateOnly.getTime());
        DailyLogEntry dailyLogEntry = new DailyLogEntry(customer.getN(),
                customer.getCn(),
                true,
                date.getTime(),
                newCustomerKey);

        Map<String, List<DailyLogEntry>> allDaysLog = customersDailyLogLocalLiveData.getValue();
        if (allDaysLog == null) {
            allDaysLog = new HashMap<>();
        }

        List<DailyLogEntry> currentDayLog = allDaysLog.get(currentDateMillSecond);
        if (currentDayLog == null) currentDayLog = new ArrayList<>();

        currentDayLog.add(dailyLogEntry);
        allDaysLog.put(currentDateMillSecond, currentDayLog);
        Paper.book("Daily_Log").write(currentDateMillSecond, currentDayLog);
        customersDailyLogLocalLiveData.postValue(allDaysLog);
    }


    synchronized public void addNewVisitReportToLocalDatabase(Visit visit, String customerUID, String customerName, String companyName) {
        //add this visit to customer visits local data
        Date date = new Date();
        Date currentDate = Common.getCurrentDateWithoutTime();
        String currentDateMillisecond = String.valueOf(currentDate.getTime());

        String visitKey = customerUID + ", " + date.getTime();
        Paper.book("visits_list").write(visitKey, visit);

        //add this visit as a logDataEntry to the sales member day log
        DailyLogEntry dailyLogEntry =
                new DailyLogEntry(customerName,
                        companyName,
                        false,
                        date.getTime(),
                        customerUID);

        Map<String, List<DailyLogEntry>> allDaysLog = customersDailyLogLocalLiveData.getValue();
        if (allDaysLog == null) {
            allDaysLog = new HashMap<>();
        }

        List<DailyLogEntry> currentDayLog = allDaysLog.get(currentDateMillisecond);
        if (currentDayLog == null) currentDayLog = new ArrayList<>();

        currentDayLog.add(dailyLogEntry);
        allDaysLog.put(currentDateMillisecond, currentDayLog);
        Paper.book("Daily_Log").write(currentDateMillisecond, currentDayLog);
        customersDailyLogLocalLiveData.postValue(allDaysLog);
    }

    synchronized public LiveData<List<String>> getCurrentDayLocalLog(final String date) {
        final MutableLiveData<List<String>> currentDayLocalLogMutableLiveData = new MutableLiveData<>();
        final List<String> currentDayLogCustomersKey = new ArrayList<>();
        final Book allLocalLogBook = Paper.book("Daily_Log");
        if (allLocalLogBook.contains(date)) {
            AppExecutors.getsInstance().getDiskIO().execute(() -> {
                List<DailyLogEntry> currentDayLog = allLocalLogBook.read(date);
                for (DailyLogEntry logEntry : currentDayLog) {
                    if (logEntry != null) currentDayLogCustomersKey.add(logEntry.getCUID());
                }
                currentDayLocalLogMutableLiveData.postValue(currentDayLogCustomersKey);
            });
        }
        return currentDayLocalLogMutableLiveData;
    }

    public LiveData<Map<String, List<DailyLogEntry>>> getCustomersDailyLogLocalLiveData() {
        return customersDailyLogLocalLiveData;
    }

    public void notifyAllLogDataUploaded() {
        customersDailyLogLocalLiveData.setValue(null);
    }
}
