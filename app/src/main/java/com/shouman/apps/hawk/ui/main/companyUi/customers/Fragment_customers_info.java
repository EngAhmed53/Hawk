package com.shouman.apps.hawk.ui.main.companyUi.customers;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shouman.apps.hawk.databinding.FragmentCustomersInfoBinding;
import com.shouman.apps.hawk.model.Customer;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_customers_info extends Fragment implements OnMapReadyCallback {
    private static final String CUSTOMER_NAME = "customer_name";
    private static final String CUSTOMER_UID = "customer_uid";
    public FragmentCustomersInfoBinding mBinding;
    private GoogleMap map;
    private Customer mainCustomer;
    private boolean customerLocationSetted = false;

    public static Fragment_customers_info getInstance() {
        return new Fragment_customers_info();
    }

    public static Fragment_customers_info getInstance(String customerName, String customerUID) {
        Bundle bundle = new Bundle();
        bundle.putString(CUSTOMER_NAME, customerName);
        bundle.putString(CUSTOMER_UID, customerUID);
        Fragment_customers_info customers_info = Fragment_customers_info.getInstance();
        customers_info.setArguments(bundle);
        return customers_info;
    }

    public Fragment_customers_info() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentCustomersInfoBinding.inflate(inflater);
        String customerUID = getArguments() != null ? getArguments().getString(CUSTOMER_UID) : null;

        //start the map;
        mBinding.customerLocationMap.onCreate(savedInstanceState);
        mBinding.customerLocationMap.getMapAsync(this);

        CustomersViewModelFactory factory = new CustomersViewModelFactory(getContext(), customerUID);
        CustomersViewModel customersViewModel = new ViewModelProvider(this, factory).get(CustomersViewModel.class);
        customersViewModel.getCustomerMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                if (customer != null) {
                    mainCustomer = customer;
                    mBinding.setCustomer(customer);

                    if (map != null && !customerLocationSetted) {
                        setCustomerLocationOnMap(customer);
                    }
                }
            }
        });

        mBinding.btnCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallPhoneNumberClick();
            }
        });

        return mBinding.getRoot();
    }

    private void setCustomerLocationOnMap(Customer customer) {
        Marker customerMarker = map.addMarker(new MarkerOptions()
                .position(new LatLng(customer.getLt(), customer.getLn()))
                .title(customer.getN()));

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(customerMarker.getPosition(), 16f));
        customerLocationSetted = true;
    }

    private void onCallPhoneNumberClick() {
        String phoneNumber = Objects.requireNonNull(mBinding.edtCustomerPhone.getText()).toString().trim();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMapStyle(new MapStyleOptions("myMapStyle"));

        if (mainCustomer != null && !customerLocationSetted) {
            setCustomerLocationOnMap(mainCustomer);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.customerLocationMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.customerLocationMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.customerLocationMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBinding.customerLocationMap.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.customerLocationMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.customerLocationMap.onLowMemory();
    }
}
