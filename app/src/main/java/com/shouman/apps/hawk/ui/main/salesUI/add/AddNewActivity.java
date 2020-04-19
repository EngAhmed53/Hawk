package com.shouman.apps.hawk.ui.main.salesUI.add;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.ActivityAddNewCustomerBinding;
import com.shouman.apps.hawk.ui.main.salesUI.add.newCustomer.Fragment_add_new_customer;
import com.shouman.apps.hawk.ui.main.salesUI.add.newCustomer.Fragment_pick_customer_location;
import com.shouman.apps.hawk.ui.main.salesUI.add.newVisit.Fragment_Add_New_Visit;

import static com.shouman.apps.hawk.ui.main.salesUI.main.home.Fragment_sales_home.FRAGMENT_NEW_CUSTOMER;
import static com.shouman.apps.hawk.ui.main.salesUI.main.home.Fragment_sales_home.FRAGMENT_NEW_VISIT;
import static com.shouman.apps.hawk.ui.main.salesUI.main.home.Fragment_sales_home.OPEN_FRAGMENT;

public class AddNewActivity extends AppCompatActivity implements Fragment_add_new_customer.OnContinueBtnClick, Fragment_pick_customer_location.OnAddCustomerClickHandler {

    private static final String TAG = "AddNewCustomerActivity";

    private ActivityAddNewCustomerBinding mBinding;

    public FragmentManager fragmentManager;

    private Fragment_add_new_customer fragment_add_new_customer;

    private Fragment_pick_customer_location fragment_pick_customer_location;

    Fragment_Add_New_Visit fragment_add_new_visit;

    private Fragment active;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_customer);
        fragmentManager = getSupportFragmentManager();

        final Intent intent = getIntent();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            getPermission(savedInstanceState, intent);
        } else {
            Toast.makeText(this, "permission needed in order to continue", Toast.LENGTH_SHORT).show();
        }

    }

    private void getPermission(final Bundle savedInstanceState, final Intent intent) {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (intent != null && intent.hasExtra(OPEN_FRAGMENT)) {
                    if (intent.getIntExtra(OPEN_FRAGMENT, FRAGMENT_NEW_CUSTOMER) == FRAGMENT_NEW_CUSTOMER) {
                        setupAddNewCustomerFragments(savedInstanceState);
                    } else if (intent.getIntExtra(OPEN_FRAGMENT, FRAGMENT_NEW_CUSTOMER) == FRAGMENT_NEW_VISIT) {
                        showAddVisitFragment();
                    }
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(AddNewActivity.this, "Permission needed in order to continue", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
    }


    private void showAddVisitFragment() {
        fragment_add_new_visit = (Fragment_Add_New_Visit) fragmentManager.findFragmentByTag("fragment_add_new_visit");
        if (fragment_add_new_visit == null) {
            fragment_add_new_visit = Fragment_Add_New_Visit.getInstance();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.sales_home_container, fragment_add_new_visit, "fragment_add_new_visit")
                    .commit();
        } else {
            fragmentManager.beginTransaction().show(fragment_add_new_visit).commit();
        }
    }

    private void setupAddNewCustomerFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fragment_add_new_customer = (Fragment_add_new_customer) fragmentManager.findFragmentByTag("fragment_add_new_customer");
            if (fragment_add_new_customer == null) {
                fragment_add_new_customer = Fragment_add_new_customer.getInstance();
                fragmentManager
                        .beginTransaction()
                        .add(R.id.sales_home_container, fragment_add_new_customer, "fragment_add_new_customer")
                        .hide(fragment_add_new_customer)
                        .commit();
            }

            fragment_pick_customer_location = (Fragment_pick_customer_location) fragmentManager.findFragmentByTag("fragment_pick_location");
            if (fragment_pick_customer_location == null) {
                fragment_pick_customer_location = Fragment_pick_customer_location.getInstance();
                fragmentManager
                        .beginTransaction()
                        .add(R.id.sales_home_container, fragment_pick_customer_location, "fragment_pick_location")
                        .hide(fragment_pick_customer_location)
                        .commit();
            }
            active = fragment_add_new_customer;
            showAddNewCustomerFragment();
        } else {
            fragment_add_new_customer = (Fragment_add_new_customer) fragmentManager.findFragmentByTag("fragment_add_new_customer");
            fragment_pick_customer_location = (Fragment_pick_customer_location) fragmentManager.findFragmentByTag("fragment_pick_location");
            active = fragmentManager.findFragmentByTag(Common.LAST_ACTIVE_FRAGMENT);
            fragmentManager.beginTransaction().show(active).commit();
        }
    }

    private void showAddNewCustomerFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .hide(active)
                .show(fragment_add_new_customer)
                .commit();
        active = fragment_add_new_customer;
        Common.LAST_ACTIVE_FRAGMENT = "fragment_add_new_customer";
    }

    private void showLocationPickerFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .hide(active)
                .show(fragment_pick_customer_location)
                .commit();
        active = fragment_pick_customer_location;
        Common.LAST_ACTIVE_FRAGMENT = "fragment_pick_location";
    }


    @Override
    public void continueToMapFragment(IBinder iBinder) {
        showLocationPickerFragment();
        hideKeyboard(iBinder);
    }

    private void hideKeyboard(IBinder iBinder) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(iBinder, 0);
    }

    @Override
    public void onBackPressed() {
        if (active == fragment_pick_customer_location && active != null) {
            showAddNewCustomerFragment();
        } else super.onBackPressed();
    }

    @Override
    public void finishHostActivity() {
        finish();
    }
}
