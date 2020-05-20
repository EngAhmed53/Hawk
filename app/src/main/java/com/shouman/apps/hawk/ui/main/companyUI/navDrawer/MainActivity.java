package com.shouman.apps.hawk.ui.main.companyUI.navDrawer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding mBinding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private TextView enabledNav = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        enabledNav = mBinding.navHome;
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
}
