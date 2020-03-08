package com.shouman.apps.hawk.ui.starting;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.FragmentSelectUserTypeBinding;
import com.shouman.apps.hawk.model.BaseUser;
import com.shouman.apps.hawk.model.Company;
import com.shouman.apps.hawk.model.SalesMan;
import com.shouman.apps.hawk.preferences.UserPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_select_user_type extends Fragment {

    private static final String TAG = "Fragment_select_user_ty";

    private static int SELECTED_POSITION = -1;

    private FragmentSelectUserTypeBinding mBinding;

    private ArrayAdapter<String> positionsAdapter;


    public static Fragment_select_user_type getInstance() {
        return new Fragment_select_user_type();
    }

    public Fragment_select_user_type() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentSelectUserTypeBinding.inflate(inflater);

        positionsAdapter = new ArrayAdapter<>(getContext(),
                R.layout.drop_down_list_item,
                Common.getAllPositions(getContext()));
        mBinding.filledExposedDropdown.setAdapter(positionsAdapter);

        mBinding.filledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                switch (position) {
                    case 0:
                        SELECTED_POSITION = Common.MANAGER_POSITION;
                        showCompanyFields();
                        mBinding.btnConfirmInfo.setEnabled(true);
                        break;
                    case 1:
                        SELECTED_POSITION = Common.SALES_POSITION;
                        showSalesFields();
                        mBinding.btnConfirmInfo.setEnabled(true);
                        break;
                }
            }
        });


        mBinding.btnConfirmInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SELECTED_POSITION == Common.MANAGER_POSITION) {
                    if (checkInputTextErrors(mBinding.nameInputLayout)
                            && checkInputTextErrors(mBinding.companyNameInputLayout)) {

                        //add company profile
                        Company company = Common.companyMutableLiveData.getValue();
                        if (company == null) {
                            BaseUser user = Common.baseUserLiveData.getValue();
                            if (user == null) {
                                user = new BaseUser();
                                user.setUserType(SELECTED_POSITION);
                                UserPreference.setUserInfoSettedTrue(getContext());
                                UserPreference.setUserTypeTrue(getContext());
                                UserPreference.setUserType(getContext(), SELECTED_POSITION);
                                Log.e(TAG, "preference changed" );
                                Company newCompany = new Company("id",
                                        user.getUserEmail(),
                                        mBinding.edtCompanyName.getText().toString(),
                                        mBinding.edtName.getText().toString(),
                                        user.getUserPassword()
                                );
                                Common.companyMutableLiveData.setValue(newCompany);

                            }
                        }

                    } else {
                        if (mBinding.nameInputLayout.getError() != null ||
                                mBinding.nameInputLayout.getEditText().getText().toString().isEmpty()) {
                            mBinding.nameInputLayout.requestFocus();

                        } else if (mBinding.companyNameInputLayout.getError() != null ||
                                mBinding.companyNameInputLayout.getEditText().getText().toString().isEmpty()) {
                            mBinding.companyNameInputLayout.requestFocus();

                        }
                    }
                } else if (SELECTED_POSITION == Common.SALES_POSITION) {
                    if (checkInputTextErrors(mBinding.nameInputLayout)
                            && checkInputTextErrors(mBinding.comIdTextField)
                            && checkInputTextErrors(mBinding.branchPinTextField)) {

                        //add sales member profile
                        SalesMan salesMan = Common.salesManMutableLiveData.getValue();
                        if (salesMan == null) {
                            BaseUser user = Common.baseUserLiveData.getValue();
                            if (user != null) {
                                user.setUserType(SELECTED_POSITION);
                                UserPreference.setUserInfoSettedTrue(getContext());
                                UserPreference.setUserTypeTrue(getContext());
                                UserPreference.setUserType(getContext(), SELECTED_POSITION);
                                SalesMan newSalesMan = new SalesMan(mBinding.edtName.getText().toString(),
                                        user.getUserEmail(),
                                        user.getUserPassword(),
                                        22232,
                                        mBinding.edtComId.getText().toString(),
                                        mBinding.edtBranchNum.getText().toString()
                                );
                                Common.salesManMutableLiveData.setValue(newSalesMan);

                            }
                        }

                    } else {
                        if (mBinding.nameInputLayout.getError() != null ||
                                mBinding.nameInputLayout.getEditText().getText().toString().isEmpty()) {
                            mBinding.nameInputLayout.requestFocus();

                        } else if (mBinding.comIdTextField.getEditText().getText().toString().isEmpty() ||
                                mBinding.comIdTextField.getError() != null) {
                            mBinding.comIdTextField.requestFocus();

                        } else if (mBinding.branchPinTextField.getEditText().getText().toString().isEmpty() ||
                                mBinding.branchPinTextField.getError() != null) {
                            mBinding.branchPinTextField.requestFocus();
                        }
                    }
                }
            }


        });

        return mBinding.getRoot();
    }

    private boolean checkInputTextErrors(TextInputLayout inputLayout) {
        String text = inputLayout.getEditText().getText().toString();
        if (text != null && !text.isEmpty()) {
            return inputLayout.getError() == null;
        }
        return false;
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


}
