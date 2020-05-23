package com.shouman.apps.hawk.ui.main.salesUI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.database.localRepo.LocalSalesRepo;
import com.shouman.apps.hawk.sync.workManager.SyncWorker;

import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_main2);

        Paper.init(getApplicationContext());

        initWorkManagerSyc();

    }

    synchronized private void initWorkManagerSyc() {
        final Constraints constraints =
                new Constraints
                        .Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

        final PeriodicWorkRequest syncLocalData =
                new PeriodicWorkRequest.Builder(SyncWorker.class, 1, TimeUnit.HOURS)
                        .addTag("TagSyncLocalData")
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork("syncLocalData", ExistingPeriodicWorkPolicy.REPLACE, syncLocalData);
        WorkManager
                .getInstance(this)
                .getWorkInfosByTagLiveData("TagSyncLocalData").observe(this, workInfos -> {
            if (workInfos != null && !workInfos.isEmpty()) {
                if (workInfos.get(0).getState() == WorkInfo.State.RUNNING) {
                    LocalSalesRepo.getInstance().clearAllLocalLogData();
                }
            }
        });

    }


//    private void signOut() {
//        new MaterialAlertDialogBuilder(contextWeakReference.get())
//                .setPositiveButton(getString(R.string.sign_out), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        signOutTheUser();
//                    }
//                })
//                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //cancel
//                    }
//                })
//                .setCancelable(true)
//                .setMessage("Are you sure to sign out ?")
//                .setTitle("Sign out")
//                .setIcon(R.drawable.ic_logout)
//                .create()
//                .show();
//    }

//    private void signOutTheUser() {
//        FirebaseAuth.getInstance().signOut();
//        clearUserPreference();
//        showStartingActivity();
//    }

//    private void showStartingActivity() {
//        Intent intent = new Intent(contextWeakReference.get(), StartingActivity.class);
//        startActivity(intent);
//        finish();
//    }

//    private void clearUserPreference() {
//        UserPreference.clearAllPreference(contextWeakReference.get());
//    }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        new MenuInflater(this).inflate(R.menu.sales_ui_home_main_menu, menu);
//        final MenuItem offlineDataMenuItem = menu.findItem(R.id.action_offline_data);
//        localSalesRepo.getCustomersDailyLogLocalLiveData().observe(this, new Observer<Map<String, List<DailyLogEntry>>>() {
//            @Override
//            public void onChanged(Map<String, List<DailyLogEntry>> localLog) {
//                if (localLog != null) {
//                    if (localLog.isEmpty()) {
//                        offlineDataMenuItem.setVisible(false);
//                    } else {
//                        offlineDataMenuItem.setVisible(true);
//                    }
//                } else {
//                    offlineDataMenuItem.setVisible(false);
//                }
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.action_offline_data) {
//            showOfflineLogFragment();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
