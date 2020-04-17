package com.shouman.apps.hawk.ui.main.salesMemberUI.newAdd.newCustomer;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseSalesRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.databinding.FragmentPickLocationBinding;
import com.shouman.apps.hawk.ui.main.salesMemberUI.newAdd.AddNewActivity;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pick_customer_location extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Fragment_pick_customer";
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocation;
    private Customer theCustomer;
    private double latitude;
    private double longitude;
    private OnAddCustomerClickHandler onAddCustomerClickHandler;
    private FragmentPickLocationBinding mBinding;
    private FirebaseSalesRepo salesRepo;
    private Context context;

    public interface OnAddCustomerClickHandler {
        void finishHostActivity();
    }

    public Fragment_pick_customer_location() {
        // Required empty public constructor
    }

    public static Fragment_pick_customer_location getInstance() {
        return new Fragment_pick_customer_location();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        //set the location
        fusedLocation = LocationServices.getFusedLocationProviderClient(context);
        try {
            onAddCustomerClickHandler = (OnAddCustomerClickHandler) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        salesRepo = FirebaseSalesRepo.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentPickLocationBinding.inflate(inflater);

        //set the map view
        setupMapView(savedInstanceState);

        //get the customer from viewModel
        NewCustomerSharedViewModel customerSharedViewModel = new ViewModelProvider(getBaseActivity()).get(NewCustomerSharedViewModel.class);
        customerSharedViewModel.getCustomerMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                theCustomer = customer;
            }
        });

        //set the getLocation button
        mBinding.myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSalesMemberLocation();
            }
        });

        //on add new customer btn click
        mBinding.btcContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theCustomer.setLt(latitude);
                theCustomer.setLn(longitude);

                //push this customer to the database
                AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        //add this new customer to database
                        salesRepo.addNewCustomerToDatabase(context, theCustomer);
                        getBaseActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, theCustomer.getCn() + "added to database", Toast.LENGTH_SHORT).show();
                                onAddCustomerClickHandler.finishHostActivity();
                            }
                        });
                    }
                });
            }
        });

        return mBinding.getRoot();
    }

    //get the host activity
    private AddNewActivity getBaseActivity() {
        return (AddNewActivity) getActivity();
    }

    //get the location
    private void getSalesMemberLocation() {

        if (isLocationEnabled()) {
            //if the location is not enabled
            fusedLocation.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        //get the current location
                        Location location = task.getResult();
                        if (location != null) {
                            //get the longitude and latitude
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            //show the location on mab;
                            showLocationOnMap(new LatLng(latitude, longitude));
                        }
                    } else {

                        //location is not enabled so we have to ask user to enable it
                        requestCurrentLocationUpdate();
                        showLocationOnMap(new LatLng(latitude, longitude));
                    }
                }
            });

        } else {
            //location is not enabled so we need to enable it
            Toast.makeText(getContext(), "Turn on location", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    //onMap ready we set the map ui
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setMapUI();
    }

    //map ui
    private void setMapUI() {
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setMyLocationEnabled(true);
        mBinding.map.findViewWithTag("GoogleMapMyLocationButton").setVisibility(View.GONE);
        googleMap.setBuildingsEnabled(true);
    }

    //move the camera to location
    private void showLocationOnMap(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18f);
        googleMap.animateCamera(cameraUpdate);
    }

    //if the location is not available we Request it
    private void requestCurrentLocationUpdate() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location mLastLocation = locationResult.getLastLocation();
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }
        };
        fusedLocation.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }


    private void setupMapView(Bundle savedInstanceState) {
        mBinding.map.getMapAsync(this);
        mBinding.map.onCreate(savedInstanceState);
    }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }


    @Override
    public void onStart() {
        super.onStart();
        mBinding.map.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.map.onResume();
        //get the sales member location
        getSalesMemberLocation();

    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.map.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBinding.map.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.map.onLowMemory();
    }
}
