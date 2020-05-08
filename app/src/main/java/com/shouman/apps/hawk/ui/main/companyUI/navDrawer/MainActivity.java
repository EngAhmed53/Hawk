package com.shouman.apps.hawk.ui.main.companyUI.navDrawer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.databinding.ActivityMainBinding;
import com.shouman.apps.hawk.network.NetworkUtils;
import com.shouman.apps.hawk.ui.main.companyUI.all_sales_members.moveSales.DialogFragment_Move_Sales;
import com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home.Fragment_homeDirections;
import com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home.OnSalesMemberLongClickActionsHandler;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        OnSalesMemberLongClickActionsHandler,
        FirebaseCompanyRepo.OnSalesMemberDeleteAction {

    private static final String TAG = "MainActivity";
    public ActivityMainBinding mBinding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private FirebaseCompanyRepo firebaseCompanyRepo;
    private TextView enabledNav = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Log.e(TAG, "onCreate: company ");
        enabledNav = mBinding.navHome;
        firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
        setupNavigation();
    }

    private void setupNavigation() {
        setSupportActionBar(mBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navController = Navigation.findNavController(this, R.id.company_container);

        navController.addOnDestinationChangedListener((controller, destination, args) -> {
            if (destination.getId() == R.id.fragment_home) {
                enabledNav.setSelected(false);
                enabledNav = mBinding.navHome;
                enabledNav.setSelected(true);
            }
        });

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(mBinding.drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mBinding.drawerLayout);

        NavigationUI.setupWithNavController(mBinding.navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration);
    }


    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    public void onNavigationItemSelected(View view) {

        view.setSelected(true);

        mBinding.drawerLayout.closeDrawers();

        int id = view.getId();

        switch (id) {

            case R.id.nav_home:
                if (enabledNav != mBinding.navHome) {
                    enabledNav.setSelected(false);
                    navController.navigate(R.id.fragment_home);
                    enabledNav = mBinding.navHome;
                }
                break;

            case R.id.nav_customers:
                if (enabledNav != mBinding.navCustomers) {
                    enabledNav.setSelected(false);
                    navController.navigate(R.id.fragment_customers);
                    enabledNav = mBinding.navCustomers;
                }
                break;

            case R.id.nav_notifications:
                if (enabledNav != mBinding.navCustomers) {
                    enabledNav.setSelected(false);
                    navController.navigate(R.id.fragment_notification);
                    enabledNav = mBinding.navNotifications;
                }
                break;

            case R.id.nav_profile:
                if (enabledNav != mBinding.navProfile) {
                    enabledNav.setSelected(false);
                    navController.navigate(R.id.fragment_profile);
                    enabledNav = mBinding.navProfile;
                }
                break;

            case R.id.nav_reports:
                if (enabledNav != mBinding.navReports) {
                    enabledNav.setSelected(false);
                    navController.navigate(R.id.fragment_reports);
                    enabledNav = mBinding.navReports;
                }
                break;

            case R.id.nav_settings:
                if (enabledNav != mBinding.navSettings) {
                    enabledNav.setSelected(false);
                    navController.navigate(R.id.fragment_settings);
                    enabledNav = mBinding.navSettings;
                }
                break;

            case R.id.nav_terms_and_conditions:
                if (enabledNav != mBinding.navTermsAndConditions) {
                    enabledNav.setSelected(false);
                    navController.navigate(R.id.fragment_terms_condition);
                    enabledNav = mBinding.navTermsAndConditions;
                }
                break;

            case R.id.nav_sign_out:
                signOut();
                break;

        }
    }

    private void signOut() {

    }


    @Override
    public void onActionMove(String salesUID, String salesName, boolean status, String branchDetails) {
        Toast.makeText(this, "" + salesUID + " " + salesName + " " + branchDetails, Toast.LENGTH_SHORT).show();
        String branchUID = branchDetails.substring(0, branchDetails.indexOf(", "));
        openMoveToDialogFragment(salesUID, salesName, status, branchUID);
    }

    @Override
    public void onActionDisable(String salesUID, String branchDetails) {
        String branchUID = branchDetails.substring(0, branchDetails.indexOf(", "));
        showDisableDialog(salesUID, branchUID);
    }

    @Override
    public void onActionEnable(String salesUID, String branchDetails) {
        String branchUID = branchDetails.substring(0, branchDetails.indexOf(", "));
        firebaseCompanyRepo.enableSalesMember(getApplicationContext(), salesUID, branchUID);
        Toast.makeText(this, R.string.enabled, Toast.LENGTH_SHORT).show();
    }

    private void showDisableDialog(final String salesUID, final String branchUID) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(R.string.disable_sales_title)
                .setMessage(R.string.disable_sales_msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.continue_btn), (dialog, which) -> {
                    firebaseCompanyRepo.disableSalesMember(getApplicationContext(), salesUID, branchUID);
                    Toast.makeText(MainActivity.this, R.string.sales_disabled, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    public void onActionDelete(final String salesUID, final String branchDetails) {
        final String branchUID = branchDetails.substring(0, branchDetails.indexOf(", "));

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(R.string.delete_sales_member)
                .setMessage(R.string.delete_sales_confirm)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {

                    if (NetworkUtils.isConnectedToInternet(getApplicationContext())) {
                        performDelete(salesUID, branchUID);
                        dialog.dismiss();
                    } else {
                     noInternetConnectionDialog();
                     dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void noInternetConnectionDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(R.string.no_internet_connection)
                .setMessage(R.string.check_your_internet_connection)
                .setCancelable(true)
                .setPositiveButton(R.string.ok_dialog_btn, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void performDelete(String salesUID, String branchUID) {
        firebaseCompanyRepo.deleteSalesMember(this, salesUID, branchUID);
    }

    @Override
    public void onDeleteSuccess() {
        Toast.makeText(this, R.string.sales_deleted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteFailed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(R.string.delete_sales_member)
                .setMessage(R.string.sales_member_delete_failed_msg)
                .setCancelable(true)
                .setPositiveButton(R.string.ok_dialog_btn, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void openMoveToDialogFragment(String salesUID, String salesName, boolean status, String oldBranchUID) {
        NavDirections toMoveSalesDialog =
                Fragment_homeDirections.actionFragmentHomeToDialogFragmentMoveSales(salesName, salesUID, oldBranchUID, status);
        navController.navigate(toMoveSalesDialog);
    }
}
