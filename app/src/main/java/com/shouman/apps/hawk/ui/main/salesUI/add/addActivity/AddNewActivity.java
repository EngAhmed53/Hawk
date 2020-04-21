package com.shouman.apps.hawk.ui.main.salesUI.add.addActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.ui.main.salesUI.add.newCustomerFragments.Fragment_add_new_customer;
import com.shouman.apps.hawk.ui.main.salesUI.add.newVisitFragment.Fragment_Add_New_Visit;

import static com.shouman.apps.hawk.ui.main.salesUI.main.home.Fragment_sales_home.FRAGMENT_NEW_CUSTOMER;
import static com.shouman.apps.hawk.ui.main.salesUI.main.home.Fragment_sales_home.FRAGMENT_NEW_VISIT;
import static com.shouman.apps.hawk.ui.main.salesUI.main.home.Fragment_sales_home.OPEN_FRAGMENT;

public class AddNewActivity extends AppCompatActivity {

    private static final String TAG = "AddNewCustomerActivity";
    private static final int LOCATION_REQUEST = 100;
    private LocationViewModel locationViewModel;
    public FragmentManager fragmentManager;
    private Fragment_add_new_customer fragment_add_new_customer;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_add_new_customer);
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
                //we have permission, update location
                initLocationViewModel();
                if (intent != null && intent.hasExtra(OPEN_FRAGMENT)) {
                    if (intent.getIntExtra(OPEN_FRAGMENT, FRAGMENT_NEW_CUSTOMER) == FRAGMENT_NEW_CUSTOMER) {
                        showAddNewCustomerFragment(savedInstanceState);
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

    private void initLocationViewModel() {
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        if (isLocationEnabled()) {
            locationViewModel.getLatLongMutableLiveData().observe(this, new Observer<LatLng>() {
                @Override
                public void onChanged(LatLng latLng) {
                    Log.d(TAG, "onChanged: lat = " + latLng.latitude + " long = " + latLng.longitude);
                }
            });
        } else {
            //location is not enabled so we need to enable it
            Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(intent, LOCATION_REQUEST);
        }
    }

    private void showAddVisitFragment() {
        Fragment_Add_New_Visit fragment_add_new_visit = (Fragment_Add_New_Visit) fragmentManager.findFragmentByTag("fragment_add_new_visit");
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

    private void showAddNewCustomerFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            fragment_add_new_customer = (Fragment_add_new_customer) fragmentManager.findFragmentByTag("fragment_add_new_customer");
            assert fragment_add_new_customer != null;
            fragmentManager
                    .beginTransaction()
                    .show(fragment_add_new_customer)
                    .commit();
        } else {
            fragment_add_new_customer = Fragment_add_new_customer.getInstance();
            fragmentManager.
                    beginTransaction().
                    add(R.id.sales_home_container, fragment_add_new_customer, "fragment_add_new_customer")
                    .commit();
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null)
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
            );
        else return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == LOCATION_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                if (isLocationEnabled()) {
                    locationViewModel.requestCurrentLocationUpdate();
                    locationViewModel.getLatLongMutableLiveData().observe(this, new Observer<LatLng>() {
                        @Override
                        public void onChanged(LatLng latLng) {
                            Log.d(TAG, "onChanged: lat = " + latLng.latitude + " long = " + latLng.longitude);
                        }
                    });
                } else {
                    Toast.makeText(this, "Location needed in order to continue !", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (fragment_add_new_customer != null) {
            if (fragment_add_new_customer.getChildFragmentManager().findFragmentById(R.id.map_fragment_container) == null) {
                finish();
            } else {
                fragment_add_new_customer.getChildFragmentManager().popBackStackImmediate();
                fragment_add_new_customer.mBinding.scrollView.setVisibility(View.VISIBLE);
            }
        } else {
            super.onBackPressed();
        }
    }
}
