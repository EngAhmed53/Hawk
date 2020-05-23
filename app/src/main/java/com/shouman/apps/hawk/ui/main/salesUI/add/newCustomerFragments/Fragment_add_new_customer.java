package com.shouman.apps.hawk.ui.main.salesUI.add.newCustomerFragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.databinding.FragmentAddNewCustomerBinding;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_add_new_customer extends Fragment {

    private static final String NEW_CUSTOMER = "new_customer";
    private Customer customer;
    public FragmentAddNewCustomerBinding mBinding;
    private NewCustomerSharedViewModel customerViewModel;
    private UserPreference userPreference;
    private final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 212;
    private int LOCATION_REQUEST = 112;


    public static Fragment_add_new_customer getInstance() {
        return new Fragment_add_new_customer();
    }


    public Fragment_add_new_customer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            customer = new Customer();
        } else {
            customer = savedInstanceState.getParcelable(NEW_CUSTOMER);
        }

        userPreference = UserPreference.getInstance();

        initViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddNewCustomerBinding.inflate(inflater);

        mBinding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        //requestCompanyNameFocus();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //on Continue btn clicked
        mBinding.btcContinue.setOnClickListener(v -> {

            //check if the inputs is valid
            if (checkInputTextErrors(mBinding.customerNameTxtInput)
                    && checkInputTextErrors(mBinding.customerCompanyNameTxtInput) && checkInputTextErrors(mBinding.customerPhoneTxtInput)) {

                //set the customer object to the sharedViewModel
                customer.setN(mBinding.edtCustomerName.getEditableText().toString().trim());
                customer.setCn(mBinding.customerCompanyNameEdt.getEditableText().toString().trim());
                customer.setE(mBinding.edtCustomerEmail.getEditableText().toString().trim());
                customer.setP(mBinding.customerPhoneEdt.getEditableText().toString().trim());
                customer.setEi(mBinding.edtCustomerNotes.getEditableText().toString().trim());
                customer.setAddedTime(new Date().getTime());
                customer.setAddedByName(userPreference.getUserName(requireContext()));
                customer.setBelongToName(userPreference.getUserName(requireContext()));
                customer.setBelongToUID(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                customerViewModel.setCustomerMutableLiveData(customer);

                //open the second mapFragment;
                requestLocationPermission();

            } else {
                if (mBinding.customerNameTxtInput.getError() != null ||
                        Objects.requireNonNull(mBinding.customerNameTxtInput.getEditText()).getText().toString().isEmpty()) {

                    //request focus
                    mBinding.customerNameTxtInput.requestFocus();

                } else if (mBinding.customerCompanyNameTxtInput.getError() != null ||
                        Objects.requireNonNull(mBinding.customerCompanyNameTxtInput.getEditText()).getText().toString().isEmpty()) {
                    //request focus
                    mBinding.customerCompanyNameTxtInput.requestFocus();

                } else if (mBinding.customerPhoneTxtInput.getError() != null ||
                        Objects.requireNonNull(mBinding.customerPhoneTxtInput.getEditText()).getText().toString().isEmpty()) {
                    //request focus
                    mBinding.customerPhoneTxtInput.requestFocus();
                }
            }
        });
    }

    private void initViewModel() {
        customerViewModel = new ViewModelProvider(requireActivity()).get(NewCustomerSharedViewModel.class);
    }

    private void requestLocationPermission() {

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            openLocationPickerFragment();
        }
    }

    private void openLocationPickerFragment() {
        if (isLocationEnabled()) {
            hideKeyboard(mBinding.btcContinue.getApplicationWindowToken());
            NavDirections toPickLocation = Fragment_add_new_customerDirections.actionFragmentAddNewCustomerToFragmentPickCustomerLocation();
            Navigation.findNavController(mBinding.getRoot()).navigate(toPickLocation);
        } else {
            //location is not enabled so we need to enable it
            Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null)
                startActivityForResult(intent, LOCATION_REQUEST);
        }
    }

    private void hideKeyboard(IBinder iBinder) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(iBinder, 0);
    }

    private boolean checkInputTextErrors(TextInputLayout inputLayout) {
        String text = Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
        if (!text.isEmpty()) {
            return inputLayout.getError() == null;
        }
        return false;
    }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null)
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
            );
        return false;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == LOCATION_REQUEST) {
//            if (resultCode == RESULT_CANCELED) {
//                if (isLocationEnabled()) {
//                    locationViewModel.requestCurrentLocationUpdate();
//                    locationViewModel.getLatLongMutableLiveData().observe(this, new Observer<LatLng>() {
//                        @Override
//                        public void onChanged(LatLng latLng) {
//                            Log.d(TAG, "onChanged: lat = " + latLng.latitude + " long = " + latLng.longitude);
//                        }
//                    });
//                } else {
//                    Toast.makeText(requireContext(), "Location needed in order to continue !", Toast.LENGTH_SHORT).show();
//                }
//            }
//        } else super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(requireContext(), "Location permission needed to get the customer location", Toast.LENGTH_SHORT).show();

        if (requestCode == MY_PERMISSIONS_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openLocationPickerFragment();
            } else {
                Toast.makeText(requireContext(), "Location permission needed to get the customer location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(NEW_CUSTOMER, customer);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        mBinding.infoFrame.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_down));
        super.onResume();
    }

    //    private void requestCompanyNameFocus() {
//        // handler to open the keyboard and request focus
//        mBinding.customerNameTxtInput.post(() -> {
//            Objects.requireNonNull(mBinding.customerNameTxtInput.getEditText()).requestFocus();
//            InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            assert inputMethodManager != null;
//            inputMethodManager.showSoftInput(mBinding.edtCustomerName, InputMethodManager.SHOW_IMPLICIT);
//        });
//    }

}
