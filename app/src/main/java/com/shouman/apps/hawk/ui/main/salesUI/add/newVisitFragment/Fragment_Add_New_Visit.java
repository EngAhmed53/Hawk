package com.shouman.apps.hawk.ui.main.salesUI.add.newVisitFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.common.collect.BiMap;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.CustomersDropDownArrayAdapter;
import com.shouman.apps.hawk.data.database.mainRepo.MainRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.data.model.Visit;
import com.shouman.apps.hawk.databinding.FragmentAddNewVisitBinding;
import com.shouman.apps.hawk.ui.main.salesUI.LocationViewModel;
import com.shouman.apps.hawk.ui.main.salesUI.SalesCustomersViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Add_New_Visit extends Fragment {

    private FragmentAddNewVisitBinding mBinding;
    private CustomersDropDownArrayAdapter customersAdapter;

    private String selectedCustomerUID;

    private double customerLatitude;
    private double customerLongitude;
    private double currentLatitude;
    private double currentLongitude;

    private MainRepo mainRepo;

    private LocationViewModel locationViewModel;

    private final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 212;

    private ArrayList<String> customersDayLog;
    private BiMap<String, Customer> customersMap;
    private List<Customer> customersList;
    private String customerName;
    private String companyName;

    private enum DistanceLimits {
        MINIMUM_DISTANCE(0f),
        MAXIMUM_DISTANCE(1000f);

        public final float distance;

        DistanceLimits(float distance) {
            this.distance = distance;
        }
    }


    public Fragment_Add_New_Visit() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainRepo = MainRepo.getInstance();
        initLocationViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddNewVisitBinding.inflate(inflater);
        mBinding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initCustomersViewModel();
        initCurrentDayLogLiveData();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mBinding.filledExposedDropdown.setOnItemClickListener((parent, view1, position, id) -> {
            Customer customer = customersList.get(position);

            customerLatitude = customer.getLt();
            customerLongitude = customer.getLn();
            customerName = customer.getN();
            companyName = customer.getCn();

            Log.e("TAG", "onViewCreated: lat " + customerLatitude + " long " + customerLongitude);

            String selectedName = customer.getN() + ", " + customer.getCn();
            mBinding.filledExposedDropdown.setText(selectedName);

            selectedCustomerUID = customersMap.inverse().get(customer);
            mBinding.btcContinue.setEnabled(true);
        });

        mBinding.btcContinue.setOnClickListener(v -> requestLocationPermission());
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isCustomerIsAlreadyExitInThisDayLog(String selectedCustomerUID) {
        return (customersDayLog.contains(selectedCustomerUID));
    }

    private boolean isSalesMemberInTheCustomerPlace() {
        //get the difference between the current sales location and the customer location
        Location salesCurrentLocation = new Location(getString(R.string.app_name));
        Location customerSavedLocation = new Location(getString(R.string.app_name));

        salesCurrentLocation.setLatitude(currentLatitude);
        salesCurrentLocation.setLongitude(currentLongitude);

        customerSavedLocation.setLatitude(customerLatitude);
        customerSavedLocation.setLongitude(customerLongitude);

        //the difference distance in meters
        float distance = salesCurrentLocation.distanceTo(customerSavedLocation);

        Log.e("dis", "isSalesMemberInTheCustomerPlace: " + "currentLatitude = " + currentLatitude + "\n" + "currentLongitude = " + currentLongitude + "\n" + "customerLati = " + customerLatitude + "\n" + "customerLong = " + customerLongitude + "\n" + "dis = " + distance);
        return (distance >= DistanceLimits.MINIMUM_DISTANCE.distance && distance <= DistanceLimits.MAXIMUM_DISTANCE.distance);
    }

    private void initCustomersViewModel() {

        SalesCustomersViewModel salesCustomersViewModel = new ViewModelProvider(requireActivity()).get(SalesCustomersViewModel.class);

        salesCustomersViewModel.getCustomersMediatorLiveData().observe(getViewLifecycleOwner(), customerMap -> {
            this.customersMap = customerMap;
            customersList = new ArrayList<>(customerMap.values());
            customersAdapter = new CustomersDropDownArrayAdapter(requireContext(), customersList);
            mBinding.filledExposedDropdown.setAdapter(customersAdapter);
        });
    }

    private void initCurrentDayLogLiveData() {
        CurrentDayLogViewModel currentDayLogViewModel = new ViewModelProvider(requireActivity()).get(CurrentDayLogViewModel.class);

        currentDayLogViewModel.getCurrentDayLogMediatorLiveData().observe(getViewLifecycleOwner(), currentDayLog -> {
            if (customersDayLog == null) {
                customersDayLog = new ArrayList<>(currentDayLog);
            } else {
                customersDayLog.addAll(currentDayLog);
            }
        });
    }

    private void initLocationViewModel() {
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
    }


    private void requestLocationPermission() {

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            addTheNawVisit();
        }
    }


    private void addTheNawVisit() {
        if (isLocationEnabled()) {
            //add the new visit
            mBinding.progressBar.setVisibility(View.VISIBLE);
            locationViewModel.requestCurrentLocationUpdate();
            locationViewModel.getLatLongMutableLiveData().observe(getViewLifecycleOwner(), latLng -> {
                if (latLng != null) {
                    currentLatitude = latLng.latitude;
                    currentLongitude = latLng.longitude;
                    if (isSalesMemberInTheCustomerPlace()) {
                        if (!isCustomerIsAlreadyExitInThisDayLog(selectedCustomerUID)) {
                            AddNewVisitToDatabase();
                            Toast.makeText(getContext(), "Visit added Successfully", Toast.LENGTH_SHORT).show();
                            navigateToHome();
                        } else {
                            Toast.makeText(getContext(), "This user is already exist in this day log", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(),
                                "You should be in the customer place to add a visit report",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                mBinding.progressBar.setVisibility(View.GONE);
            });

        } else {
            //location is not enabled so we need to enable it
            Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            int LOCATION_REQUEST = 112;
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null)
                startActivityForResult(intent, LOCATION_REQUEST);
            mBinding.progressBar.setVisibility(View.GONE);
        }
    }

    private void navigateToHome() {
        NavDirections toHome = Fragment_Add_New_VisitDirections.actionFragmentAddNewVisitToFragmentSalesHome();
        Navigation.findNavController(mBinding.getRoot()).navigate(toHome);
    }

    private void AddNewVisitToDatabase() {
        Visit visit = new Visit();
        visit.setVisitTime(new Date().getTime());
        visit.setVisitNote(Objects.requireNonNull(mBinding.edtVisitNotes.getText()).toString());
        mainRepo.addVisitToCustomer(getContext(), visit, selectedCustomerUID, customerName, companyName);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null)
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
            );
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(requireContext(), "Location permission needed to get the customer location", Toast.LENGTH_SHORT).show();

        if (requestCode == MY_PERMISSIONS_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addTheNawVisit();
            } else {
                Toast.makeText(requireContext(), "Location permission needed to get the customer location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        mBinding.infoFrame.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_down));
        super.onResume();
    }
}
