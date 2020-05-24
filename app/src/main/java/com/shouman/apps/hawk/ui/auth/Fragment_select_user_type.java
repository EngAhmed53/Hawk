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
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.databinding.FragmentSelectUserTypeBinding;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_select_user_type extends Fragment {

    private static int SELECTED_POSITION = -1;

    private FragmentSelectUserTypeBinding mBinding;

    private AuthViewModel authViewModel;

    private User mainUser;

    private UserPreference userPreference;

    private boolean isUserNameSetted = false;

    private boolean isCompanyNameSetted = false;

    private TextWatcher companyNameTextWatcher;

    private TextWatcher userNameTextWatcher;

    public Fragment_select_user_type() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreference = UserPreference.getInstance();
        initViewModel();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentSelectUserTypeBinding.inflate(inflater);

        getTheUserObjectFromViewModel();

        initDropdown();

        initConfirmButton();

        return mBinding.getRoot();
    }

    private void getDynamicLinkDataIfExist() {
        if (getHostActivity().dynamicLinkData != null) {
            mainUser.setCuid(getHostActivity().dynamicLinkData[1]);
            mainUser.setBuid(getHostActivity().dynamicLinkData[2]);
            mainUser.setUn(getHostActivity().dynamicLinkData[3]);
            mainUser.setStatus(true);
            mainUser.setUt("sales_account");

            //update the user object in the viewModel
            authViewModel.updateTheUserInDatabase(requireContext(), mainUser);

            //navigate to the sales main activity
            navigateToSalesHome();
        }
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(getHostActivity()).get(AuthViewModel.class);
    }

    private void getTheUserObjectFromViewModel() {
        authViewModel.getUserMediatorLiveData().observe(getViewLifecycleOwner(), user -> {
            mainUser = user;
            getDynamicLinkDataIfExist();
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
                    SELECTED_POSITION = Common.COMPANY_ACCOUNT;
                    companyNameTextWatcher = getCompanyAccountTextWatcher();
                    mBinding.edtCompanyName.addTextChangedListener(companyNameTextWatcher);
                    initNameEditTextChangeListener();
                    showCompanyLayout();
                    break;
                case 1:
                    SELECTED_POSITION = Common.SALES_ACCOUNT;
                    //remove company name textWatcher
                    if (companyNameTextWatcher != null) {
                        mBinding.edtCompanyName.removeTextChangedListener(companyNameTextWatcher);
                    }
                    if (userNameTextWatcher != null) {
                        mBinding.edtName.removeTextChangedListener(userNameTextWatcher);
                    }
                    showSalesLayout();
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
                    if (SELECTED_POSITION == Common.COMPANY_ACCOUNT) {
                        if (isUserNameSetted && isCompanyNameSetted) {
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
        mBinding.btnConfirmInfo.setOnClickListener(v -> {
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
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) mainUser.setCuid(user.getUid());

            //update the user object in the viewModel
            authViewModel.updateTheUserInDatabase(requireContext(), mainUser);

            //navigate to home activity
            navigateToCompanyHome();
        });
    }

    private void navigateToSalesHome() {
        Navigation.findNavController(mBinding.btnConfirmInfo).navigate(R.id.action_fragment_select_user_type_to_main2Activity);
        userPreference.setBranchUID(requireContext(), mainUser.getBuid());
        userPreference.setCompanyUID(requireContext(), mainUser.getCuid());
        userPreference.setCompanyName(requireContext(), mainUser.getCn());
        userPreference.setSalesmanStatus(requireContext(), mainUser.isStatus());
        userPreference.setUserName(requireContext(), mainUser.getUn());
        userPreference.setUserType(requireContext(), mainUser.getUt());
    }

    private void navigateToCompanyHome() {
        Navigation.findNavController(mBinding.btnConfirmInfo).navigate(R.id.action_fragment_select_user_type_to_mainActivity);
        userPreference.setCompanyUID(requireContext(), mainUser.getCuid());
        userPreference.setCompanyName(requireContext(), mainUser.getCn());
        userPreference.setUserName(requireContext(), mainUser.getUn());
        userPreference.setUserType(requireContext(), mainUser.getUt());
        requireActivity().finish();
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

    private void showSalesLayout() {
        mBinding.companyInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.companyInfoLayout.setVisibility(View.GONE);

        mBinding.salesInfoLayout.setVisibility(View.VISIBLE);
        mBinding.salesInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));
    }

    private void showCompanyLayout() {
        mBinding.salesInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.salesInfoLayout.setVisibility(View.GONE);

        mBinding.companyInfoLayout.setVisibility(View.VISIBLE);
        mBinding.companyInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));

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
