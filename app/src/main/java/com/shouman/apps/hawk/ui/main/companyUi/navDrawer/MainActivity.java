package com.shouman.apps.hawk.ui.main.companyUi.navDrawer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupNavigation();
    }

    private void setupNavigation() {

        setSupportActionBar(mBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(mBinding.drawerLayout)
                .build();

        navController = Navigation.findNavController(this, R.id.company_container);

        NavigationUI.setupActionBarWithNavController(this, navController, mBinding.drawerLayout);

        NavigationUI.setupWithNavController(mBinding.navView, navController);

        mBinding.navView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        mBinding.drawerLayout.closeDrawers();

        int id = menuItem.getItemId();

        switch (id) {

            case R.id.nav_home:
                navController.navigate(R.id.fragment_home);
                break;

            case R.id.nav_customers:
                navController.navigate(R.id.fragment_customers);
                break;

            case R.id.nav_notifications:
                navController.navigate(R.id.fragment_notification);
                break;

            case R.id.nav_profile:
                navController.navigate(R.id.fragment_profile);
                break;

            case R.id.nav_reports:
                navController.navigate(R.id.fragment_reports);
                break;

            case R.id.nav_settings:
                navController.navigate(R.id.fragment_settings);
                break;

            case R.id.nav_terms_and_conditions:
                navController.navigate(R.id.fragment_terms_condition);
                break;

            case R.id.nav_sign_out:
                signOut();
                break;

        }
        return true;

    }

    private void signOut() {

    }
}
