package com.shouman.apps.hawk.ui.main.salesMemberUI.newAdd.newCustomer;


import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.shouman.apps.hawk.databinding.FragmentAddNewCustomerBinding;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.ui.main.salesMemberUI.newAdd.AddNewActivity;

import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_add_new_customer extends Fragment {
    private static final String TAG = "Fragment_add_new_custom";
    private static final String NEW_CUSTOMER = "new_customer";

    public interface OnContinueBtnClick {
        void continueToMapFragment(IBinder iBinder);
    }

    private OnContinueBtnClick onContinueBtnClick;
    private Customer customer;
    private FragmentAddNewCustomerBinding mBinding;

    public static Fragment_add_new_customer getInstance() {
        return new Fragment_add_new_customer();
    }


    public Fragment_add_new_customer() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onContinueBtnClick = (OnContinueBtnClick) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            Log.e(TAG, "onAttach: " + "The Host activity must implement OnContinueBtnClick interface");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddNewCustomerBinding.inflate(inflater);
        if (savedInstanceState == null) {
            customer = new Customer();
        } else {
            customer = savedInstanceState.getParcelable(NEW_CUSTOMER);
        }

        //viewModel
        final NewCustomerSharedViewModel customerViewModel = new ViewModelProvider(getBaseActivity()).get(NewCustomerSharedViewModel.class);

        //on Continue btn clicked
        mBinding.btcContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    customerViewModel.setCustomerMutableLiveData(customer);

                    //open the second mapFragment;
                    openTheMapFragment(v.getApplicationWindowToken());

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
            }
        });

        return mBinding.getRoot();
    }

    //get the host activity
    private AddNewActivity getBaseActivity() {
        return (AddNewActivity) getActivity();
    }


    private void openTheMapFragment(IBinder iBinder) {
        onContinueBtnClick.continueToMapFragment(iBinder);
    }

    private boolean checkInputTextErrors(TextInputLayout inputLayout) {
        String text = Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
        if (!text.isEmpty()) {
            return inputLayout.getError() == null;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCompanyNameFocus();

    }

    private void requestCompanyNameFocus() {
        // handler to open the keyboard and request focus
        mBinding.customerNameTxtInput.post(new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(mBinding.customerNameTxtInput.getEditText()).requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.showSoftInput(mBinding.edtCustomerName, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(NEW_CUSTOMER, customer);
        super.onSaveInstanceState(outState);
    }
}
