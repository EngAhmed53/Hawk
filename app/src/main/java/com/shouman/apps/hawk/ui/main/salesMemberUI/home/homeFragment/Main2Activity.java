package com.shouman.apps.hawk.ui.main.salesMemberUI.home.homeFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.ActivityMain2Binding;
import com.shouman.apps.hawk.preferences.UserPreference;
import com.shouman.apps.hawk.ui.auth.StartingActivity;
import com.shouman.apps.hawk.ui.main.companyUi.customers.customerInfo.Fragment_customers_info;
import com.shouman.apps.hawk.ui.main.salesMemberUI.home.allCustomersPage.AllCustomersActivity;
import com.shouman.apps.hawk.ui.main.salesMemberUI.home.personalPage.PersonalPageActivity;

import java.lang.ref.WeakReference;
import java.util.Objects;

import static com.shouman.apps.hawk.ui.main.salesMemberUI.home.allCustomersPage.AllCustomersActivity.SALES_UID;

public class Main2Activity extends AppCompatActivity implements IMain2ClickHandler {

    private ActivityMain2Binding mainBinding;
    private Fragment_sales_home fragment_sales_home;
    Fragment_customers_info fragment_customers_info;
    private FragmentManager fragmentManager;
    private String userUID;
    private WeakReference<Main2Activity> contextWeakReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextWeakReference = new WeakReference<>(this);
        mainBinding = DataBindingUtil.setContentView(contextWeakReference.get(), R.layout.activity_main2);
        fragmentManager = getSupportFragmentManager();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userUID = firebaseUser.getUid();
        }

        if (savedInstanceState == null) {
            fragment_sales_home = Fragment_sales_home.getInstance(userUID);
        } else {
            fragment_sales_home = (Fragment_sales_home) fragmentManager.findFragmentByTag("fragment_sales_home");
        }

        //set the actionBar
        setSupportActionBar(mainBinding.toolbar);

        //set the actionBarToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(contextWeakReference.get(),
                mainBinding.drawerLayout,
                mainBinding.toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_closed);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.old_rose_light));
        mainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setDrawerInfo();

        //show the home fragment
        showHomeFragment();
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
            case R.id.nav_home:
                showHomeFragment();
                break;
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

    private void showHomeFragment() {
        mainBinding.navHome.setPressed(true);
        if (fragment_sales_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_sales_home);
            return;
        }
        fragmentManager.
                beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.fragment_container_drw, fragment_sales_home, "fragment_sales_home")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        }

        if (fragment_customers_info != null && fragment_customers_info.isVisible()) {

            fragmentManager.popBackStack("customer_info", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        } else super.onBackPressed();
    }

    @Override
    public void onCustomerItemClickHandler(String customerUID, String customerName) {
        fragment_customers_info = (Fragment_customers_info) fragmentManager.findFragmentByTag("fragment_customer_info");
        if (fragment_customers_info != null && fragment_customers_info.isAdded()) {
            fragment_customers_info = null;
            fragment_customers_info = Fragment_customers_info.getInstance(customerName, customerUID);
            fragmentManager.beginTransaction().replace(R.id.full_customer_info_container, fragment_customers_info).commit();
        } else {
            Log.e("TAG", "onCustomerItemClickHandler: " + "customer fragment is null");
            fragment_customers_info = Fragment_customers_info.getInstance(customerName, customerUID);
            fragmentManager
                    .beginTransaction()
                    .addToBackStack("customer_info")
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .add(R.id.full_customer_info_container, fragment_customers_info, "fragment_customer_info")
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        mainBinding = null;
        super.onDestroy();
    }
}
