package com.shouman.apps.hawk.ui.main.salesUI.add.newCustomerFragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.shouman.apps.hawk.data.database.mainRepo.MainRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.databinding.FragmentPickLocationBinding;
import com.shouman.apps.hawk.ui.main.salesUI.add.addActivity.AddNewActivity;
import com.shouman.apps.hawk.ui.main.salesUI.add.addActivity.LocationViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pick_customer_location extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Fragment_pick_customer";
    private GoogleMap googleMap;
    private Customer theCustomer;
    private double latitude;
    private double longitude;
    private FragmentPickLocationBinding mBinding;
    private MainRepo mainRepo;
    private Context context;


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
        mainRepo = MainRepo.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentPickLocationBinding.inflate(inflater);

        //set the map view
        setupMapView(savedInstanceState);

        //get the customer from viewModel
        initSharedViewModel();

        //set the getLocation button
        mBinding.myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                theCustomer.setLt(latitude);
                theCustomer.setLn(longitude);

                //push this customer to the database
                mainRepo.addNewCustomer(context, theCustomer);
                //add this new customer to database
                Toast.makeText(context, theCustomer.getCn() + "added to database", Toast.LENGTH_SHORT).show();
                getBaseActivity().finish();
            }
        });
        return mBinding.getRoot();
    }

    private void initSharedViewModel() {
        NewCustomerSharedViewModel customerSharedViewModel = new ViewModelProvider(getBaseActivity()).get(NewCustomerSharedViewModel.class);
        customerSharedViewModel.getCustomerMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                theCustomer = customer;
            }
        });
    }


    //onMap ready we set the map ui
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setMapUI();
        //set salesman location on map
        getSalesMemberLocation();
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

    //get the host activity
    private AddNewActivity getBaseActivity() {
        return (AddNewActivity) getActivity();
    }

    //get the location
    private void getSalesMemberLocation() {
        LocationViewModel locationViewModel = new ViewModelProvider(getBaseActivity()).get(LocationViewModel.class);
        locationViewModel.requestCurrentLocationUpdate();
        locationViewModel.getLatLongMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                //get the longitude and latitude
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                //show the location on mab;
                showLocationOnMap(new LatLng(latitude, longitude));
            }
        });
    }

    //move the camera to location
    private void showLocationOnMap(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18f);
        googleMap.moveCamera(cameraUpdate);
    }

    private void setupMapView(Bundle savedInstanceState) {
        mBinding.map.getMapAsync(this);
        mBinding.map.onCreate(savedInstanceState);
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