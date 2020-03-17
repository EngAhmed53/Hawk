package com.shouman.apps.hawk.ui.main.companyUi;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.ActivityMainBinding;
import com.shouman.apps.hawk.ui.main.companyUi.add_new_branch.Fragment_add_new_branch;
import com.shouman.apps.hawk.ui.main.companyUi.company_home.Fragment_company_home;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding mainBinding;
    public FragmentManager fragmentManager;

    private Fragment_company_home fragment_company_home;
    private Fragment_company_notification fragment_company_notification;
    private Fragment_company_profile fragment_company_profile;
    private Fragment_add_new_branch fragment_add_new_branch;
    private Fragment active;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragment_company_home = Fragment_company_home.getInstance();
            fragment_company_notification = Fragment_company_notification.getInstance();
            fragment_company_profile = Fragment_company_profile.getInstance();
            fragment_add_new_branch = Fragment_add_new_branch.getInstance();
            active = fragment_company_home;
            addAllFragmentsToFragmentManager();
            showHomeFragment();
        } else {
            fragment_company_home = (Fragment_company_home) fragmentManager.findFragmentByTag("fragment_home");
            fragment_company_notification = (Fragment_company_notification) fragmentManager.findFragmentByTag("fragment_notification");
            fragment_company_profile = (Fragment_company_profile) fragmentManager.findFragmentByTag("fragment_profile");
            fragment_add_new_branch = Fragment_add_new_branch.getInstance();
            active = fragmentManager.findFragmentByTag(Common.LAST_ACTIVE_FRAGMENT);
        }

        mainBinding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notification:
                        showNotificationFragment();
                        return true;
                    case R.id.profile:
                        showCompanyProfileHome();
                        return true;
                    case R.id.company_home:
                    default:
                        showHomeFragment();
                        return true;
                }
            }
        });
    }

    private void addAllFragmentsToFragmentManager() {
        fragmentManager
                .beginTransaction()
                .add(R.id.home_container, fragment_company_home, "fragment_home")
                .commit();

        fragmentManager
                .beginTransaction()
                .add(R.id.home_container, fragment_company_notification, "fragment_notification")
                .hide(fragment_company_notification)
                .commit();

        fragmentManager
                .beginTransaction()
                .add(R.id.home_container, fragment_company_profile, "fragment_profile")
                .hide(fragment_company_profile)
                .commit();
    }

    private void showHomeFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .hide(active)
                .show(fragment_company_home)
                .commit();

        active = fragment_company_home;
        Common.LAST_ACTIVE_FRAGMENT = "fragment_home";
    }

    private void showCompanyProfileHome() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .hide(active)
                .show(fragment_company_profile)
                .commit();

        active = fragment_company_profile;
        Common.LAST_ACTIVE_FRAGMENT = "fragment_profile";
    }

    private void showNotificationFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .hide(active)
                .show(fragment_company_notification)
                .commit();

        active = fragment_company_notification;
        Common.LAST_ACTIVE_FRAGMENT = "fragment_notification";
    }

    public void showAddNewBranchFragment() {
        if (!fragment_add_new_branch.isAdded()) {
            fragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .add(R.id.home_container, fragment_add_new_branch, "fragment_add_new_branch")
                    .commit();
        } else {
            fragmentManager.beginTransaction().show(fragment_add_new_branch).commit();
        }
    }
}
