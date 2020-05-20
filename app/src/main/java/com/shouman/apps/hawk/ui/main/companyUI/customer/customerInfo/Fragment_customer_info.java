package com.shouman.apps.hawk.ui.main.companyUI.customer.customerInfo;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.data.model.Visit;
import com.shouman.apps.hawk.databinding.FragmentCustomerInfoBinding;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_customer_info extends Fragment {

    private FragmentCustomerInfoBinding mBinding;
    private Customer customer;
    private String customerUID;
    private CustomerInfoViewModel mViewModel;

    public Fragment_customer_info() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        customer = Fragment_customer_infoArgs.fromBundle(getArguments()).getCustomer();
        customerUID = Fragment_customer_infoArgs.fromBundle(getArguments()).getCustomerUID();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentCustomerInfoBinding.inflate(inflater);

        if (customer == null) {
            if (customerUID != null) {
                getCustomerFromViewModel();
            }
        }
        return mBinding.getRoot();
    }

    private void getCustomerFromViewModel() {
        CustomerInfoViewModelFactory factory = new CustomerInfoViewModelFactory(requireContext(), customerUID);
        mViewModel = new ViewModelProvider(this, factory).get(CustomerInfoViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (customer != null) {
            setUI();
        } else {
            mViewModel.getCustomerMediatorLiveData().observe(getViewLifecycleOwner(), customer1 -> {
                if (customer1 != null) {
                    customer = customer1;
                    setUI();
                }
            });
        }


        mBinding.btnCallCustomer.setOnClickListener(v -> onCallPhoneNumberClick(customer));

        mBinding.visitsLogBtn.setOnClickListener(v -> openDialogFragment(v, customer));

        mBinding.locationBtn.setOnClickListener(v -> openLocationOnMap(customer));

        mBinding.moveBtn.setOnClickListener(v -> moveCustomerTo(v, customer));

        mBinding.deleteBtn.setOnClickListener(v -> deleteCustomer(customerUID));
    }

    private void setUI() {
        if (customer != null) {

            mBinding.progressBar.setVisibility(View.INVISIBLE);
            mBinding.editFieldsLayout.setVisibility(View.VISIBLE);

            mBinding.customerTitleBigTxt.setText(customer.getN());
            mBinding.customerTitleSmallTxt.setText(customer.getN());

            mBinding.customerCompanyNameTxt.setText(customer.getCn());

            mBinding.customerPhoneTxt.setText(customer.getP());

            mBinding.customerAddedTimeTxt.setText(getDateText(customer));

            mBinding.customerAddedByTxt.setText(customer.getAddedByName());

            mBinding.customerBelongToTxt.setText(customer.getBelongToName());

            if (customer.getE() == null || customer.getE().isEmpty()) {
                mBinding.emailField.setVisibility(View.GONE);
                mBinding.view7.setVisibility(View.GONE);
            } else {
                mBinding.customerEmailTxt.setText(customer.getE());
            }

            if (customer.getEi() == null || customer.getEi().isEmpty()) {
                mBinding.noteField.setVisibility(View.GONE);
                mBinding.view12.setVisibility(View.GONE);
            } else {
                mBinding.customerNote.setText(customer.getEi());
            }
        }
    }

    private void openDialogFragment(View view, Customer customer) {
        Visit[] visits = new Visit[customer.getVisitList().values().size()];
        NavDirections toVisitsList =
                Fragment_customer_infoDirections.actionFragmentCustomersInfoToDialogFragmentVisitsLog(customer.getVisitList().values().toArray(visits));
        Navigation.findNavController(view).navigate(toVisitsList);
    }

    private void openLocationOnMap(Customer customer) {
        Uri intentUri = Uri.parse("geo:0,0?q=" + customer.getLt() + "," + customer.getLn() + "(" + customer.getN() + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void moveCustomerTo(View v, Customer customer) {
        NavDirections toMoveCustomer =
                Fragment_customer_infoDirections.actionFragmentCustomersInfoToDialogFragmentMoveCustomer(customerUID, customer.getN(), customer.getBelongToUID());

        Navigation.findNavController(v).navigate(toMoveCustomer);
    }

    private void deleteCustomer(String customerUID) {
        FirebaseCompanyRepo firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.delete_customer)
                .setMessage(R.string.delete_customer_msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.delete_customer_forever), (dialog, which) -> {
                    firebaseCompanyRepo.deleteCustomer(requireContext(), customerUID);
                    Toast.makeText(requireContext(), getString(R.string.customer_deleted), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Navigation.findNavController(mBinding.getRoot()).popBackStack();

                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private String getDateText(Customer customer) {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        Calendar calendar = Calendar.getInstance();
        if (customer.getAddedTime() == 0) return getString(R.string.not_defined);
        calendar.setTimeInMillis(customer.getAddedTime());
        return formatter.format(calendar.getTime());
    }


    private void onCallPhoneNumberClick(Customer customer) {
        String phoneNumber = customer.getP();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        mBinding.infoFrame.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_down));
        super.onResume();
    }
}
