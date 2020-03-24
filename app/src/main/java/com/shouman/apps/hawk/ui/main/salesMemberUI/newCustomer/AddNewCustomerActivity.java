package com.shouman.apps.hawk.ui.main.salesMemberUI.newCustomer;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class AddNewCustomerActivity extends AppCompatActivity implements Fragment_add_new_customer.OnContinueBtnClick, Fragment_pick_customer_location.OnAddCustomerClickHandler {

    private static final String TAG = "AddNewCustomerActivity";

    private ActivityAddNewCustomerBinding mBinding;

    public FragmentManager fragmentManager;

    private Fragment_add_new_customer fragment_add_new_customer;

    private Fragment_pick_customer_location fragment_pick_customer_location;

    private Fragment active;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_customer);
        fragmentManager = getSupportFragmentManager();

        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                startTheFragmentsProcess(savedInstanceState);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(AddNewCustomerActivity.this, "Permission needed in order to continue", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
    }

    private void startTheFragmentsProcess(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fragment_add_new_customer = Fragment_add_new_customer.getInstance();
            fragment_pick_customer_location = Fragment_pick_customer_location.getInstance();
            active = fragment_add_new_customer;
            addFragmentToFragmentManager();
            showAddNewCustomerFragment();
        } else {
            fragment_add_new_customer =
                    (Fragment_add_new_customer) fragmentManager.findFragmentByTag("fragment_add_new_customer");

            fragment_pick_customer_location =
                    (Fragment_pick_customer_location) fragmentManager.findFragmentByTag("fragment_pick_location");
            active = fragmentManager.findFragmentByTag(Common.LAST_ACTIVE_FRAGMENT);
        }
    }

    private void addFragmentToFragmentManager() {
        fragmentManager
                .beginTransaction()
                .add(R.id.sales_home_container, fragment_add_new_customer, "fragment_add_new_customer")
                .hide(fragment_add_new_customer)
                .commit();

        fragmentManager
                .beginTransaction()
                .add(R.id.sales_home_container, fragment_pick_customer_location, "fragment_pick_location")
                .hide(fragment_pick_customer_location)
                .commit();
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
        if (active == fragment_pick_customer_location) {
            showAddNewCustomerFragment();
        } else super.onBackPressed();
    }

    @Override
    public void finishHostActivity() {
        finish();
    }
}
