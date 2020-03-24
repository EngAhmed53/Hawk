package com.shouman.apps.hawk.ui.auth;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.FragmentSelectUserTypeBinding;
import com.shouman.apps.hawk.model.User;

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

        authViewModel.getUserMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mainUser = user;
            }
        });


        mBinding.btnConfirmInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cleanUserObject();

                if (SELECTED_POSITION == Common.MANAGER_POSITION) {
                    if (checkInputTextErrors(mBinding.nameInputLayout)
                            && checkInputTextErrors(mBinding.companyNameInputLayout)) {

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

                        authViewModel.updateTheUserInDatabase(mainUser);

                    } else {
                        if (mBinding.nameInputLayout.getError() != null ||
                                Objects.requireNonNull(mBinding.nameInputLayout.getEditText()).getText().toString().isEmpty()) {
                            mBinding.nameInputLayout.requestFocus();

                        } else if (mBinding.companyNameInputLayout.getError() != null ||
                                Objects.requireNonNull(mBinding.companyNameInputLayout.getEditText()).getText().toString().isEmpty()) {
                            mBinding.companyNameInputLayout.requestFocus();

                        }
                    }
                } else if (SELECTED_POSITION == Common.SALES_POSITION) {
                    if (checkInputTextErrors(mBinding.nameInputLayout)
                            && checkInputTextErrors(mBinding.comIdTextField)) {


                        cleanUserObject();

                        //set company account info
                        //company user name
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
                        if (mBinding.nameInputLayout.getError() != null ||
                                Objects.requireNonNull(mBinding.nameInputLayout.getEditText()).getText().toString().isEmpty()) {
                            mBinding.nameInputLayout.requestFocus();

                        } else if (Objects.requireNonNull(mBinding.comIdTextField.getEditText()).getText().toString().isEmpty() ||
                                mBinding.comIdTextField.getError() != null) {
                            mBinding.comIdTextField.requestFocus();
                        }
                    }
                }
            }


        });

        return mBinding.getRoot();
    }

    private void cleanUserObject() {
        this.mainUser.setUt(null);
        this.mainUser.setBuid(null);
        this.mainUser.setCuid(null);
        this.mainUser.setCn(null);
    }

    private void initDropdown() {
        ArrayAdapter<String> positionsAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.drop_down_list_item,
                Common.getAllPositions(getContext()));
        mBinding.filledExposedDropdown.setAdapter(positionsAdapter);

        mBinding.filledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InputMethodManager in = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                assert in != null;
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
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(getHostActivity()).get(AuthViewModel.class);
    }

    private StartingActivity getHostActivity() {
        return (StartingActivity) getActivity();
    }

    private boolean checkInputTextErrors(TextInputLayout inputLayout) {
        String text = Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
        if (!text.isEmpty()) {
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
