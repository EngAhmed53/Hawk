package com.shouman.apps.hawk.ui.main.salesMemberUI.newAdd.newVisit;

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
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.firebase.auth.FirebaseAuth;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.CustomersDropDownArrayAdapter;
import com.shouman.apps.hawk.data.SalesRepo;
import com.shouman.apps.hawk.databinding.FragmentAddNewVisitBinding;
import com.shouman.apps.hawk.model.Visit;
import com.shouman.apps.hawk.ui.main.salesMemberUI.newAdd.AddNewActivity;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Add_New_Visit extends Fragment {

    private FragmentAddNewVisitBinding mBinding;
    private CustomersDropDownArrayAdapter customersAdapter;
    private BiMap<String, String> customersMap;
    private List<String> customersData;
    private List<String> customersDayLog;
    private String selectedCustomerUID;
    private double customerLatitude;
    private double customerLongitude;
    private double currentLatitude;
    private double currentLongitude;
    private FusedLocationProviderClient fusedLocation;
    private final float MINIMUM_DISTANCE = 0f;
    private final float MAXIMUM_DISTANCE = 50f;
    private String companyName;
    private String customerName;


    public Fragment_Add_New_Visit() {
        // Required empty public constructor
    }

    public static Fragment_Add_New_Visit getInstance() {
        return new Fragment_Add_New_Visit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        fusedLocation = LocationServices.getFusedLocationProviderClient(context);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddNewVisitBinding.inflate(inflater);

        initViewModel();


        mBinding.filledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCustomerUID = customersMap.inverse().get(customersData.get(position));
                String[] customerData = customersData.get(position).split(", ");
                StringBuilder builder = new StringBuilder().append(customerData[0]).append(", ").append(customerData[1]);
                mBinding.filledExposedDropdown.setText(builder.toString(), false);

                customerName = customerData[0];
                companyName = customerData[1];

                customerLatitude = Double.parseDouble(customerData[2]);
                customerLongitude = Double.parseDouble(customerData[3]);
            }
        });

        mBinding.btcContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSalesMemberInTheCustomerPlace()) {
                    final Visit visit = new Visit();
                    visit.setVisitTime(new Date().getTime());
                    visit.setVisitNote(Objects.requireNonNull(mBinding.edtVisitNotes.getText()).toString());

                    AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (!isCustomerIsAlreadyExitInThisDayLog(selectedCustomerUID)) {
                                SalesRepo.addNewVisitReport(getContext(), visit, selectedCustomerUID, customerName, companyName);
                                showToastInMainThread("Visit added Successfully");
                                closeHostActivity();
                            } else {
                                showToastInMainThread("This user is already exist in this day log");
                            }
                        }
                    });

                } else {
                    Toast.makeText(getContext(),
                            "You should be in the customer place to add a visit report" ,
                            Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        return mBinding.getRoot();
    }

    private void closeHostActivity() {
        AddNewActivity hostActivity = (AddNewActivity) getActivity();
                if(hostActivity != null) {
                    hostActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
                    hostActivity.finish();
                }
    }

    private void showToastInMainThread(final String toastMessage) {

        AppExecutors.getsInstance().getMainThread().execute(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(),
                        toastMessage,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
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

        Log.e("dis", "isSalesMemberInTheCustomerPlace: " + "currentLatitude = " + currentLatitude + "\n" + "currentLongitude = " + currentLongitude + "\n" + "customerLati = " + customerLatitude + "\n" + "customerLong = " + currentLongitude + "\n" + "dis = " + distance);
        return (distance >= MINIMUM_DISTANCE && distance <= MAXIMUM_DISTANCE);
    }

    private void initViewModel() {
        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        VisitViewModelFactory visitViewModelFactory = new VisitViewModelFactory(getContext(), userUID);
        VisitsViewModel visitsViewModel = new ViewModelProvider(this, visitViewModelFactory).get(VisitsViewModel.class);

        visitsViewModel.getCustomersMapMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<BiMap<String, String>>() {
            @Override
            public void onChanged(BiMap<String, String> customerKeyValueMap) {
                customersMap = customerKeyValueMap;
                customersData = new ArrayList<>(customerKeyValueMap.values());
                customersAdapter = new CustomersDropDownArrayAdapter(Objects.requireNonNull(getContext()), customersData);
                mBinding.filledExposedDropdown.setAdapter(customersAdapter);
            }
        });

        visitsViewModel.getCurrentDayLogMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> customersCurrentDayLog) {
                customersDayLog = customersCurrentDayLog;
            }
        });
    }

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
                            currentLatitude = location.getLatitude();
                            currentLongitude = location.getLongitude();
                            //show the location on mab;
                        }
                    } else {

                        //location is not enabled so we have to ask user to enable it
                        requestCurrentLocationUpdate();
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

    private void requestCurrentLocationUpdate() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location mLastLocation = locationResult.getLastLocation();
                currentLatitude = mLastLocation.getLatitude();
                currentLongitude = mLastLocation.getLongitude();
            }
        };
        fusedLocation.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        getSalesMemberLocation();
    }
}
