package com.shouman.apps.hawk.data.database.localRepo;

public class LocalCompanyRepo {

    private static LocalCompanyRepo localCompanyRepo;
    private static final Object LOCK = new Object();

    private LocalCompanyRepo() {

    }

    public static LocalCompanyRepo getInstance() {
        if (localCompanyRepo == null) {
            synchronized (LOCK) {
                localCompanyRepo = new LocalCompanyRepo();
            }
        }
        return localCompanyRepo;
    }

//    public MutableLiveData<Customer> getCustomerByUID(final String UID) {
//        final MutableLiveData<Customer> customerMutableLiveData = new MutableLiveData<>();
//        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                Customer customer = Paper.book("New_Customers").read(UID);
//                customerMutableLiveData.postValue(customer);
//            }
//        });
//        return customerMutableLiveData;
//    }

}
