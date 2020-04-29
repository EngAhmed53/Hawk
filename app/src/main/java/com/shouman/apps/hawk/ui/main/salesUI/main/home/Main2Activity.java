package com.shouman.apps.hawk.ui.main.salesUI.main.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.database.localRepo.LocalSalesRepo;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.databinding.ActivityMain2Binding;
import com.shouman.apps.hawk.preferences.UserPreference;
import com.shouman.apps.hawk.sync.workManager.SyncWorker;
import com.shouman.apps.hawk.ui.auth.StartingActivity;
import com.shouman.apps.hawk.ui.main.companyUi.customers.customerInfo.Fragment_customers_info;
import com.shouman.apps.hawk.ui.main.salesUI.main.allCustomersPage.AllCustomersActivity;
import com.shouman.apps.hawk.ui.main.salesUI.main.home.offlinData.FragmentOfflineData;
import com.shouman.apps.hawk.ui.main.salesUI.main.personalPage.PersonalPageActivity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

import static com.shouman.apps.hawk.ui.main.salesUI.main.allCustomersPage.AllCustomersActivity.SALES_UID;

public class Main2Activity extends AppCompatActivity implements IMain2ClickHandler {

    private ActivityMain2Binding mainBinding;
    private Fragment_customers_info fragment_customers_info;
    private FragmentOfflineData fragmentOfflineData;
    private FragmentManager fragmentManager;
    private WeakReference<Main2Activity> contextWeakReference;
    private LocalSalesRepo localSalesRepo;
    private ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextWeakReference = new WeakReference<>(this);
        mainBinding = DataBindingUtil.setContentView(contextWeakReference.get(), R.layout.activity_main2);

        Paper.init(getApplicationContext());

        localSalesRepo = LocalSalesRepo.getInstance();

        initWorkManagerSyc();

        fragmentManager = getSupportFragmentManager();

        //set the actionBar
        initToolbar();

        //check user is enabled or not
        if (!UserPreference.getSalesmanStatus(this)) {
            mainBinding.disabledHint.setVisibility(View.VISIBLE);
            mainBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
        }

        setDrawerInfo();
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
                .getWorkInfosByTagLiveData("TagSyncLocalData").observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                if (workInfos != null && !workInfos.isEmpty()) {
                    if (workInfos.get(0).getState() == WorkInfo.State.RUNNING) {
                        LocalSalesRepo.getInstance().notifyAllLogDataUploaded();
                    }
                }
            }
        });

    }

    private void initToolbar() {
        setSupportActionBar(mainBinding.toolbar);
        //set the actionBarToggle
        toggle = new ActionBarDrawerToggle(contextWeakReference.get(),
                mainBinding.drawerLayout,
                mainBinding.toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_closed);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        mainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setDrawerInfo() {
        String userName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        String companyName = UserPreference.getUserCompanyName(contextWeakReference.get());
        String branchName = UserPreference.getBranchName(contextWeakReference.get());

        mainBinding.headerLayout.txtBranchName.setText(branchName);
        mainBinding.headerLayout.txtCompanyName.setText(companyName);
        mainBinding.headerLayout.txtUserName.setText(userName);
    }


    public void onDrawerItemClick(View view) {
        mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        switch (view.getId()) {
            case R.id.nav_personal_page:
                showPersonalPage();
                break;
            case R.id.nav_settings:
                showSettingsPage();
                break;
            case R.id.nav_terms_and_conditions:
                showTermsAndConditionPage();
                break;
            case R.id.nav_chang_branch:
                showBranchPage();
                break;
            case R.id.nav_allCustomers_list:
                showAllCustomersActivity();
                break;
            case R.id.nav_sign_out:
                signOut();
                break;
            case R.id.nav_exit:
                exitApp();
                break;
        }
    }

    private void showAllCustomersActivity() {
        Intent intent = new Intent(contextWeakReference.get(), AllCustomersActivity.class);
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        intent.putExtra(SALES_UID, userUID);
        startActivity(intent);
    }

    private void exitApp() {
        finish();
    }

    private void signOut() {
        new MaterialAlertDialogBuilder(contextWeakReference.get())
                .setPositiveButton(getString(R.string.sign_out), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOutTheUser();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel
                    }
                })
                .setCancelable(true)
                .setMessage("Are you sure to sign out ?")
                .setTitle("Sign out")
                .setIcon(R.drawable.ic_logout)
                .create()
                .show();
    }

    private void signOutTheUser() {
        FirebaseAuth.getInstance().signOut();
        clearUserPreference();
        showStartingActivity();
    }

    private void showStartingActivity() {
        Intent intent = new Intent(contextWeakReference.get(), StartingActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearUserPreference() {
        UserPreference.clearAllPreference(contextWeakReference.get());
    }

    private void showBranchPage() {

    }

    private void showTermsAndConditionPage() {

    }

    private void showSettingsPage() {

    }

    private void showPersonalPage() {
        Intent intent = new Intent(contextWeakReference.get(), PersonalPageActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        }

        if (fragment_customers_info != null && fragment_customers_info.isVisible()) {
            fragment_customers_info = null;
            fragmentManager.popBackStack("customer_info", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        } else if (fragmentOfflineData != null && fragmentOfflineData.isVisible()) {
            fragmentManager.beginTransaction().hide(fragmentOfflineData).commit();
        } else super.onBackPressed();
    }

    @Override
    public void onCustomerItemClickHandler(String customerUID) {
        fragment_customers_info = Fragment_customers_info.getInstance(customerUID);
        fragmentManager
                .beginTransaction()
                .addToBackStack("customer_info")
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.full_customer_info_container, fragment_customers_info, "fragment_customer_info")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.sales_ui_home_menu, menu);
        final MenuItem offlineDataMenuItem = menu.findItem(R.id.action_offline_data);
        localSalesRepo.getCustomersDailyLogLocalLiveData().observe(this, new Observer<Map<String, List<DailyLogEntry>>>() {
            @Override
            public void onChanged(Map<String, List<DailyLogEntry>> localLog) {
                if (localLog != null) {
                    if (localLog.isEmpty()) {
                        offlineDataMenuItem.setVisible(false);
                    } else {
                        offlineDataMenuItem.setVisible(true);
                    }
                } else {
                    offlineDataMenuItem.setVisible(false);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void showOfflineLogFragment() {
        fragmentOfflineData = (FragmentOfflineData) fragmentManager.findFragmentByTag("fragment_offline_data");
        if (fragmentOfflineData != null) {
            fragmentManager.beginTransaction().show(fragmentOfflineData).commit();
            return;
        }
        fragmentOfflineData = FragmentOfflineData.getInstance();
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.full_customer_info_container, fragmentOfflineData, "fragment_offline_data")
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_offline_data) {
            showOfflineLogFragment();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        mainBinding = null;
        super.onDestroy();
    }
}
