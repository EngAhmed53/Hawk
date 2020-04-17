package com.shouman.apps.hawk.data.database.localRepo;

public class LocalSalesRepo {
//    private static final String TAG = "LocalSalesRepo";
//    private static LocalSalesRepo localSalesRepo;
//    private static final Object LOCK = new Object();
//    private MutableLiveData<Map<String, Customer>> customersLocalLiveData;
//    private MutableLiveData<Map<String, Map<String, CustomersLogDataEntry>>> customersDailyLogLocalLiveData;
//    private DatabaseReference companiesReference;
//
//
//    private LocalSalesRepo() {
//        Log.e(TAG, "LocalSalesRepo: start setting live data");
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        companiesReference = database.getReference().child("data");
//        customersLocalLiveData = new MutableLiveData<>();
//        customersDailyLogLocalLiveData = new MutableLiveData<>();
//
//        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                Map<String, Customer> customersList = new HashMap<>();
//                Book newCustomersBook = Paper.book("New_Customers");
//                for (String key : newCustomersBook.getAllKeys()) {
//                    Log.e(TAG, "key: " + key );
//                }
//                customersLocalLiveData.postValue(customersList);
//                Map<String, Map<String, CustomersLogDataEntry>> allDaysLog = new HashMap<>();
//
//                Book dailyLocalBook = Paper.book("Daily_Log");
//                for (String day : dailyLocalBook.getAllKeys()) {
//                    Map<String, CustomersLogDataEntry> dayLog = dailyLocalBook.read(day);
//                    allDaysLog.put(day, dayLog);
//                }
//                customersDailyLogLocalLiveData.postValue(allDaysLog);
//                Log.e(TAG, "run: finished setting the liveData");
//            }
//        });
//    }
//
//    public static LocalSalesRepo getInstance() {
//
//        if (localSalesRepo == null) {
//            synchronized (LOCK) {
//                localSalesRepo = new LocalSalesRepo();
//            }
//        }
//        return localSalesRepo;
//    }
//
//    synchronized public void addNewCustomerToLocalDatabase(Context context, Customer customer) {
//        Log.d(TAG, "addNewCustomerToLocalDatabase: in method");
//        Map<String, Customer> pendingCustomersList = customersLocalLiveData.getValue();
//        if (pendingCustomersList == null) {
//            pendingCustomersList = new HashMap<>();
//        }
//
//        String companyUID = UserPreference.getCompanyUID(context);
//        DatabaseReference customersReference = companiesReference.child(companyUID).child("C");
//        String newCustomerKey = customersReference.push().getKey();
//
//        //add customer to the the local database
//        assert newCustomerKey != null;
//        pendingCustomersList.put(newCustomerKey, customer);
//        Paper.book("New_Customers").write(newCustomerKey, customer);
//        customersLocalLiveData.postValue(pendingCustomersList);
//        Log.d(TAG, "addNewCustomerToLocalDatabase: pending customers list setted in paper");
//
//
//        // days log
//        Date date = new Date();
//        String dateText = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH).format(date);
//        CustomersLogDataEntry customersLogDataEntry = new CustomersLogDataEntry(customer.getN(),
//                customer.getCn(),
//                true,
//                date.getTime());
//
//        Map<String, Map<String, CustomersLogDataEntry>> allDaysLog = customersDailyLogLocalLiveData.getValue();
//        if (allDaysLog == null) {
//            Log.d(TAG, "addNewCustomerToLocalDatabase: all days log is null");
//            allDaysLog = new HashMap<>();
//        }
//
//        Map<String, CustomersLogDataEntry> currentDayLog = allDaysLog.get(dateText);
//        if (currentDayLog == null) {
//            Log.d(TAG, "addNewCustomerToLocalDatabase: current day log is null");
//            currentDayLog = new HashMap<>();
//        } else {
//            Log.d(TAG, "addNewCustomerToLocalDatabase: current day log is not null size = " + currentDayLog.size());
//        }
//
//        currentDayLog.put(newCustomerKey, customersLogDataEntry);
//        allDaysLog.put(dateText, currentDayLog);
//        Paper.book("Daily_Log").write(dateText, currentDayLog);
//        customersDailyLogLocalLiveData.postValue(allDaysLog);
//    }
//
//    public MutableLiveData<Map<String, Customer>> getCustomersLocalLiveData() {
//        return customersLocalLiveData;
//    }
//
//    public MutableLiveData<Map<String, Map<String, CustomersLogDataEntry>>> getCustomersDailyLogLocalLiveData() {
//        return customersDailyLogLocalLiveData;
//    }
}
