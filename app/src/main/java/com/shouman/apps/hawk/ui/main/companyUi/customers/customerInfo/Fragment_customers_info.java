package com.shouman.apps.hawk.ui.main.companyUi.customers.customerInfo;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.DialogFragmentVisitLogBinding;
import com.shouman.apps.hawk.databinding.FragmentCustomersInfoBinding;
import com.shouman.apps.hawk.model.Customer;
import com.shouman.apps.hawk.model.Visit;
import com.shouman.apps.hawk.ui.main.companyUi.customers.visitsLog.DialogFragment_Visits_Log;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.TreeMap;

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
        mBinding.setNotDefined(getString(R.string.not_defined));
        String customerUID = getArguments() != null ? getArguments().getString(CUSTOMER_UID) : null;

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startMap(savedInstanceState);

        initViewModel(customerUID);

        mBinding.btnCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallPhoneNumberClick();
            }
        });

        mBinding.showVisitsLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFragment();
            }
        });

        return mBinding.getRoot();
    }

    private void openDialogFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        DialogFragment_Visits_Log dialogFragment_visits_log =
                DialogFragment_Visits_Log.getInstance(new TreeMap<String, Visit>(mainCustomer.getVisitList()), mainCustomer.getN());

        dialogFragment_visits_log.showNow(fragmentManager, "visits_log");
    }

    private void startMap(Bundle savedInstanceState) {
        //start the map;
        mBinding.customerLocationMap.onCreate(savedInstanceState);
        mBinding.customerLocationMap.getMapAsync(this);
    }

    private void initViewModel(String customerUID) {
        CustomersViewModelFactory factory = new CustomersViewModelFactory(getContext(), customerUID);
        CustomersViewModel customersViewModel = new ViewModelProvider(this, factory).get(CustomersViewModel.class);
        customersViewModel.getCustomerMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                if (customer != null) {
                    mainCustomer = customer;
                    mBinding.setCustomer(customer);
                    String date = getDateText(customer);
                    mBinding.setDate(date);
                    if (map != null && !customerLocationSetted) {
                        setCustomerLocationOnMap(customer);
                    }
                }
            }
        });
    }

    @NotNull
    private String getDateText(Customer customer) {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        Calendar calendar = Calendar.getInstance();
        if (customer.getAddedTime() == 0) return getString(R.string.not_defined);
        calendar.setTimeInMillis(customer.getAddedTime());
        return formatter.format(calendar.getTime());
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

    private void finish() {
        FragmentActivity host = getActivity();
        if (host != null) {
            host.getSupportFragmentManager().popBackStack("customer_info", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
