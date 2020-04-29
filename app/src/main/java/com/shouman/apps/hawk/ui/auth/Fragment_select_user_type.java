package com.shouman.apps.hawk.ui.auth;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.vision.barcode.Barcode;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.FragmentSelectUserTypeBinding;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_select_user_type extends Fragment {

    private static final String TAG = "Fragment_select_user_ty";
    private static final String USER_NAME = "user_name";

    private static int SELECTED_POSITION = -1;

    private FragmentSelectUserTypeBinding mBinding;

    private AuthViewModel authViewModel;

    private User mainUser;

    private boolean isUIDsSetted = false;
    private boolean isUserNameSetted = false;
    private boolean isCompanyNameSetted = false;
    private TextWatcher companyNameTextWatcher;
    private TextWatcher userNameTextWatcher;

    public static Fragment_select_user_type getInstance() {
        return new Fragment_select_user_type();
    }

    public static Fragment_select_user_type getInstance(String userName) {
        Fragment_select_user_type fragment_select_user_type = new Fragment_select_user_type();
        Bundle bundle = new Bundle();
        bundle.putString(USER_NAME, userName);
        fragment_select_user_type.setArguments(bundle);
        return fragment_select_user_type;
    }

    public Fragment_select_user_type() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentSelectUserTypeBinding.inflate(inflater);

        initViewModel();

        Bundle arg = getArguments();
        if (arg != null) {
            String name = arg.getString(USER_NAME);
            if (name != null) {
                mBinding.edtName.setText(name);
            }
        }

        initDropdown();

        getTheUserObjectFromViewModel();

        getBarCodeFromViewModelIfExist();

        initScanBtn();

        initConfirmButton();

        return mBinding.getRoot();
    }

    private void initScanBtn() {
        mBinding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanFragment();
            }
        });
    }

    private void initDropdown() {
        ArrayAdapter<String> positionsAdapter = new ArrayAdapter<>(requireContext(),
                R.layout.select_type_dropdown_list_item,
                Common.getAllPositions(requireContext()));
        mBinding.filledExposedDropdown.setAdapter(positionsAdapter);

        mBinding.filledExposedDropdown.setOnItemClickListener((parent, view, position, id) -> {

            InputMethodManager in = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert in != null;
            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

            switch (position) {
                case 0:
                    SELECTED_POSITION = Common.MANAGER_POSITION;
                    companyNameTextWatcher = getCompanyAccountTextWatcher();
                    mBinding.edtCompanyName.addTextChangedListener(companyNameTextWatcher);
                    showCompanyFields();
                    break;
                case 1:
                    SELECTED_POSITION = Common.SALES_POSITION;
                    //remove company name textWatcher
                    if (companyNameTextWatcher != null) {
                        mBinding.edtCompanyName.removeTextChangedListener(companyNameTextWatcher);
                    }
                    showSalesFields();
                    break;
            }
        });
    }

    private void initNameEditTextChangeListener() {
        userNameTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    isUserNameSetted = !s.toString().isEmpty();
                    if (SELECTED_POSITION == Common.MANAGER_POSITION) {
                        if (isUserNameSetted && isCompanyNameSetted) {
                            mBinding.btnConfirmInfo.setEnabled(true);
                        } else {
                            mBinding.btnConfirmInfo.setEnabled(false);
                        }
                    } else if (SELECTED_POSITION == Common.SALES_POSITION) {
                        if (isUserNameSetted && isUIDsSetted) {
                            mBinding.btnConfirmInfo.setEnabled(true);
                        } else {
                            mBinding.btnConfirmInfo.setEnabled(false);
                        }
                    }
                }
            }
        };
        mBinding.edtName.addTextChangedListener(userNameTextWatcher);
    }

    private void initConfirmButton() {
        mBinding.btnConfirmInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SELECTED_POSITION == Common.MANAGER_POSITION) {

                    //clean the user object to avoid any pre-entered data
                    cleanUserObject();

                    //set company account info
                    //company user name
                    mainUser.setUn(Objects.requireNonNull(mBinding.edtName.getText()).toString().trim());
                    //company name
                    mainUser.setCn(Objects.requireNonNull(mBinding.edtCompanyName.getText()).toString().trim());
                    //branch uid is null because it is company not sales member
                    mainUser.setBuid(null);
                    // set the user_type
                    mainUser.setUt("company_account");
                    //set the company uid
                    mainUser.setCuid(Common.EmailToUID(mainUser.getE()));

                    //update the user object in the viewModel
                    authViewModel.updateTheUserInDatabase(mainUser);

                } else if (SELECTED_POSITION == Common.SALES_POSITION) {
                    if (isUIDsSetted) {
                        //set sales account info
                        //sales user name
                        mainUser.setUn(Objects.requireNonNull(mBinding.edtName.getText()).toString().trim());
                        //no company name here
                        mainUser.setCn(null);
                        //branch uid
                        mainUser.setBuid(Objects.requireNonNull(mBinding.edtBranchId.getText()).toString().trim());
                        // set the user_type
                        mainUser.setUt("sales_account");
                        //set the company uid
                        mainUser.setCuid(Objects.requireNonNull(mBinding.edtCompanyUid.getText()).toString().trim());

                        authViewModel.updateTheUserInDatabase(mainUser);

                    } else {
                        openScanFragment();
                    }
                }
            }
        });
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(getHostActivity()).get(AuthViewModel.class);
    }

    private void getTheUserObjectFromViewModel() {
        authViewModel.getUserMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mainUser = user;
            }
        });
    }

    private void getBarCodeFromViewModelIfExist() {
        authViewModel.getBarCodesMediatorLiveData().observe(getHostActivity(), new Observer<Barcode>() {
            @Override
            public void onChanged(Barcode barcode) {
                if (barcode != null && barcode.displayValue != null) {
                    String[] parameters = barcode.displayValue.split(", ");
                    if (!parameters[0].equals(getString(R.string.qr_code_key))) {
                        Toast.makeText(getHostActivity(), "This QR Code is not valid", Toast.LENGTH_SHORT).show();
                    } else {
                        mBinding.imgScanCode.setImageResource(R.drawable.ic_checkmark);
                        mBinding.txtScan.setText(getString(R.string.done));
                        mBinding.btnScan.setEnabled(false);
                        mBinding.edtCompanyUid.setText(parameters[4]);
                        mBinding.edtBranchId.setText(parameters[2]);
                        mBinding.btnScan.setBackgroundColor(getResources().getColor(R.color.gray2));
                        mBinding.btnScan.setElevation(0);

                        //there is an error here i cant get the company and branch name
                        UserPreference.setCompanyName(getHostActivity(), parameters[3]);
                        UserPreference.setBranchName(getHostActivity(), parameters[1]);

                        isUIDsSetted = true;
                    }

                    if (isUIDsSetted && isUserNameSetted) {
                        mBinding.btnConfirmInfo.setEnabled(true);
                    } else {
                        mBinding.btnConfirmInfo.setEnabled(false);
                    }
                }
            }
        });
    }

    private StartingActivity getHostActivity() {
        return (StartingActivity) getActivity();
    }

    private TextWatcher getCompanyAccountTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    isCompanyNameSetted = !s.toString().isEmpty();

                    if (isCompanyNameSetted && isUserNameSetted) {
                        mBinding.btnConfirmInfo.setEnabled(true);

                    } else {
                        mBinding.btnConfirmInfo.setEnabled(false);
                    }
                }
            }
        };
    }

    private void cleanUserObject() {
        this.mainUser.setUt(null);
        this.mainUser.setBuid(null);
        this.mainUser.setCuid(null);
        this.mainUser.setCn(null);
    }

    private void showSalesFields() {
        mBinding.companyInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.companyInfoLayout.setVisibility(View.GONE);

        mBinding.salesInfoLayout.setVisibility(View.VISIBLE);
        mBinding.salesInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));
    }

    private void showCompanyFields() {
        mBinding.salesInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.salesInfoLayout.setVisibility(View.GONE);

        mBinding.companyInfoLayout.setVisibility(View.VISIBLE);
        mBinding.companyInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));

    }

    private void openScanFragment() {
       // getHostActivity().requestCameraPermission();
    }

    @Override
    public void onResume() {
        initNameEditTextChangeListener();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (userNameTextWatcher != null) {
            mBinding.edtName.removeTextChangedListener(userNameTextWatcher);
        }
        if (companyNameTextWatcher != null) {
            mBinding.edtCompanyName.removeTextChangedListener(companyNameTextWatcher);
        }
        super.onPause();
    }
}
