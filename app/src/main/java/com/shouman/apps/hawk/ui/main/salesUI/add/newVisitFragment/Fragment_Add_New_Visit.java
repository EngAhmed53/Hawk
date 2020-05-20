package com.shouman.apps.hawk.ui.main.salesUI.add.newVisitFragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.BiMap;
import com.google.firebase.auth.FirebaseAuth;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.CustomersDropDownArrayAdapter;
import com.shouman.apps.hawk.data.database.mainRepo.MainRepo;
import com.shouman.apps.hawk.data.model.Visit;
import com.shouman.apps.hawk.databinding.FragmentAddNewVisitBinding;
import com.shouman.apps.hawk.ui.main.salesUI.LocationViewModel;

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
    private BiMap<String, String> customersMap;
    private List<String> customersData;
    private List<String> customersDayLog;
    private String selectedCustomerUID;
    private double customerLatitude;
    private double customerLongitude;
    private double currentLatitude;
    private double currentLongitude;
    private String companyName;
    private String customerName;
    private MainRepo mainRepo;

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

    public static Fragment_Add_New_Visit getInstance() {
        return new Fragment_Add_New_Visit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mainRepo = MainRepo.getInstance();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddNewVisitBinding.inflate(inflater);

        getSalesMemberLocation();

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

                    if (!isCustomerIsAlreadyExitInThisDayLog(selectedCustomerUID)) {
                        mainRepo.addVisitToCustomer(getContext(), visit, selectedCustomerUID, customerName, companyName);
                        Toast.makeText(getContext(), "Visit added Successfully", Toast.LENGTH_SHORT).show();
                        getBaseActivity().finish();
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
        });
        return mBinding.getRoot();
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
        return (distance >= DistanceLimits.MINIMUM_DISTANCE.distance && distance <= DistanceLimits.MAXIMUM_DISTANCE.distance);
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
                customersAdapter = new CustomersDropDownArrayAdapter(requireContext(), customersData);
                mBinding.filledExposedDropdown.setAdapter(customersAdapter);
            }
        });

        visitsViewModel.getCurrentDayLogMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> customersCurrentDayLog) {
                Log.e("TAG", "onChanged: " + customersCurrentDayLog.toString());
                if (customersDayLog == null) {
                    customersDayLog = new ArrayList<>(customersCurrentDayLog);
                } else {
                    customersDayLog.addAll(customersCurrentDayLog);
                }
            }
        });
    }

    private void getSalesMemberLocation() {
        LocationViewModel locationViewModel = new ViewModelProvider(getBaseActivity()).get(LocationViewModel.class);
        locationViewModel.requestCurrentLocationUpdate();
        locationViewModel.getLatLongMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                currentLatitude = latLng.latitude;
                currentLongitude = latLng.longitude;
            }
        });
    }

    private FragmentActivity getBaseActivity() {
        return getActivity();
    }

}
