package com.shouman.apps.hawk.ui.main.salesUI.add.newCustomerFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.shouman.apps.hawk.data.database.mainRepo.MainRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.databinding.FragmentPickLocationBinding;
import com.shouman.apps.hawk.ui.main.salesUI.LocationViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pick_customer_location extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Customer theCustomer;
    private double latitude;
    private double longitude;
    private FragmentPickLocationBinding mBinding;
    private MainRepo mainRepo;
    private NewCustomerSharedViewModel customerViewModel;
    private LocationViewModel locationViewModel;

    public Fragment_pick_customer_location() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainRepo = MainRepo.getInstance();

        initSharedViewModel();

        initLocationViewModel();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentPickLocationBinding.inflate(inflater);

        //set the map view
        setupMapView(savedInstanceState);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.myLocation.hide();

        //get the customer from viewModel
        customerViewModel.getCustomerMutableLiveData().observe(
                getViewLifecycleOwner(), customer -> theCustomer = customer
        );

        //set the pickLocation button
        mBinding.pickLocationBtn.setOnClickListener(v -> {

            theCustomer.setLt(latitude);
            theCustomer.setLn(longitude);

            //push this customer to the database
            mainRepo.addNewCustomer(requireContext(), theCustomer);

            Toast.makeText(requireContext(), theCustomer.getCn() + "added to database", Toast.LENGTH_SHORT).show();
            NavDirections toHome = Fragment_pick_customer_locationDirections.actionFragmentPickCustomerLocationToFragmentSalesHome();
            Navigation.findNavController(mBinding.getRoot()).navigate(toHome);
        });

        mBinding.myLocation.setOnClickListener(v -> locationViewModel.requestCurrentLocationUpdate());
    }

    private void initSharedViewModel() {
        customerViewModel = new ViewModelProvider(requireActivity()).get(NewCustomerSharedViewModel.class);
    }


    //onMap ready we set the map ui
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        setMapUI();

        locationViewModel.getLatLongMutableLiveData().observe(getViewLifecycleOwner(), latLng -> {
            //get the longitude and latitude
            latitude = latLng.latitude;
            longitude = latLng.longitude;

            mBinding.myLocation.show();
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.pickLocationBtn.setEnabled(true);
            //show the location on mab;
            showLocationOnMap(latLng);
        });
    }

    private void setMapUI() {
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setMyLocationEnabled(true);
        mBinding.map.findViewWithTag("GoogleMapMyLocationButton").setVisibility(View.GONE);
        googleMap.setBuildingsEnabled(true);
    }


    //get the location
    private void initLocationViewModel() {
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
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
        locationViewModel.requestCurrentLocationUpdate();
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