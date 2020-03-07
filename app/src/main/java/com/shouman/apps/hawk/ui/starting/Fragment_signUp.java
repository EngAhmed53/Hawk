package com.shouman.apps.hawk.ui.starting;


import android.content.Context;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.FragmentSignUpBinding;
import com.shouman.apps.hawk.model.BaseUser;
import com.shouman.apps.hawk.model.Company;
import com.shouman.apps.hawk.model.SalesMan;


public class Fragment_signUp extends Fragment implements StartingActivity.OnBackButtonPressed {

    private static final String TAG = "Fragment_signUp";
    public FragmentSignUpBinding mBinding;
    private ArrayAdapter<String> positionsAdapter;
    public static int SELECTED_POSITION = -1;
    public int count = 0;
    private BaseUser user;
    private Company company;
    private SalesMan salesMan;
    //    private String userEmail;
//    private String userPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    public void doBack() {
        onBackPressed();
    }


    public Fragment_signUp() {
        // Required empty public constructor
    }

    public static Fragment_signUp getInstance() {
        return new Fragment_signUp();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSignUpBinding.inflate(inflater);
        user = new BaseUser();
        mBinding.setBaseUser(user);

        firebaseAuth = FirebaseAuth.getInstance();

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
                        break;
                    case 1:
                        SELECTED_POSITION = Common.SALES_POSITION;
                        break;
                }
            }
        });

        mBinding.btnEmailSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInputTextErrors(mBinding.nameTextField)
                        && checkInputTextErrors(mBinding.emailTextField)
                        && checkInputTextErrors(mBinding.passwordTextField)) {

                    switch (SELECTED_POSITION) {
                        case -1:
                            Toast.makeText(getContext(), "Please Select your position", Toast.LENGTH_SHORT).show();
                            break;
                        case 0:

                            firebaseAuth.createUserWithEmailAndPassword(user.getUserEmail(), user.getUserPassword())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                sendVerificationMail();
                                            } else {
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            break;
                        case 1:

                            firebaseAuth.createUserWithEmailAndPassword(user.getUserEmail(), user.getUserPassword())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                sendVerificationMail();
                                            } else {
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            break;
                    }
                } else {
                    if (mBinding.nameTextField.getError() != null ||
                            mBinding.nameTextField.getEditText().getText().toString().isEmpty()) {
                        mBinding.nameTextField.requestFocus();

                    } else if (mBinding.emailTextField.getError() != null ||
                            mBinding.emailTextField.getEditText().getText().toString().isEmpty()) {
                        mBinding.emailTextField.requestFocus();

                    } else if (mBinding.passwordTextField.getError() != null ||
                            mBinding.passwordTextField.getEditText().getText().toString().isEmpty()) {
                        mBinding.passwordTextField.requestFocus();

                    }
                }
            }
        });

        return mBinding.getRoot();
    }

    private void sendVerificationMail() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        switch (SELECTED_POSITION) {
                            case 0:
//                                company = new Company();
//                                company.setBaseUserInfo(Fragment_signUp.this.user);
//                                mBinding.setCompany(company);
                                showEmailVerificationLayout();

                                break;
                            case 1:
//                                salesMan = new SalesMan();
//                                salesMan.setUserBaseInfo(Fragment_signUp.this.user);
//                                mBinding.setSalesMan(salesMan);
                                showEmailVerificationLayout();
                                break;
                        }
                    } else {
                        Toast.makeText(getContext(), "Send Verification Email failed, Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showEmailVerificationLayout() {
        count += 1;
        mBinding.linearSignUpChooseType.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.linearSignUpChooseType.setVisibility(View.INVISIBLE);

        mBinding.verifyEmailLayout.setVisibility(View.VISIBLE);
        mBinding.verifyEmailLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));
    }

    private boolean checkInputTextErrors(TextInputLayout inputLayout) {
        String text = inputLayout.getEditText().getText().toString();
        if (text != null && !text.isEmpty()) {
            return inputLayout.getError() == null;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SELECTED_POSITION != -1) {
            mBinding.filledExposedDropdown.setText(Common.getAllPositions(getContext())[SELECTED_POSITION], false);
        }
    }

    private void showSalesLayout() {
        count += 1;
        mBinding.linearSignUpChooseType.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.linearSignUpChooseType.setVisibility(View.INVISIBLE);

        mBinding.salesFirstTimeLayout.setVisibility(View.VISIBLE);
        mBinding.salesFirstTimeLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));
    }


    private void showManagerLayout() {
        count += 1;
        mBinding.linearSignUpChooseType.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.linearSignUpChooseType.setVisibility(View.INVISIBLE);


        mBinding.companyFirstTimeLayout.setVisibility(View.VISIBLE);
        mBinding.companyFirstTimeLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));

    }

    private void onBackPressed() {
        if (count > 0) {
            switch (SELECTED_POSITION) {
                case 0:
                    hideManagerLayout();
                    break;
                case 1:
                    hideEmployeeLayout();
                    break;
            }
        }
    }

    private void hideManagerLayout() {
        count -= 1;
        mBinding.companyFirstTimeLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.companyFirstTimeLayout.setVisibility(View.INVISIBLE);


        mBinding.linearSignUpChooseType.setVisibility(View.VISIBLE);
        mBinding.linearSignUpChooseType.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));
    }


    private void hideEmployeeLayout() {
        count -= 1;

        mBinding.salesFirstTimeLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.salesFirstTimeLayout.setVisibility(View.INVISIBLE);

        mBinding.linearSignUpChooseType.setVisibility(View.VISIBLE);
        mBinding.linearSignUpChooseType.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));
    }


}
