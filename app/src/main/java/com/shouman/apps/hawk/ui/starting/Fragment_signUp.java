package com.shouman.apps.hawk.ui.starting;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.FragmentSignUpBinding;
import com.shouman.apps.hawk.model.BaseUser;


public class Fragment_signUp extends Fragment {

    private static final String TAG = "Fragment_signUp";
    public FragmentSignUpBinding mBinding;

    private FirebaseAuth firebaseAuth;

    public Fragment_signUp() {
        // Required empty public constructor
    }

    public static Fragment_signUp getInstance() {
        return new Fragment_signUp();
    }
//فك اعتماد الجرد

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSignUpBinding.inflate(inflater);


        firebaseAuth = FirebaseAuth.getInstance();


        mBinding.btnEmailSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInputTextErrors(mBinding.emailTextField)
                        && checkInputTextErrors(mBinding.passwordTextField)) {

                    final String email = mBinding.emailTextField.getEditText().getText().toString();
                    final String password = mBinding.passwordTextField.getEditText().toString();

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        BaseUser user = Common.baseUserLiveData.getValue();
                                        if (user != null) {
                                            user.setUserEmail(email);
                                            user.setUserPassword(password);
                                            user.setVerified(false);
                                            Common.baseUserLiveData.setValue(user);
                                        } else {
                                            BaseUser newUser = new BaseUser();
                                            newUser.setUserEmail(email);
                                            newUser.setUserPassword(password);
                                            newUser.setVerified(false);
                                            Common.baseUserLiveData.setValue(newUser);
                                        }
                                        sendVerificationMail();
                                    } else {
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    if (mBinding.emailTextField.getError() != null ||
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

    private void openVerifyFragment() {

        StartingActivity host = (StartingActivity) getActivity();
        if (host != null) host.showVerifyEmailFragment();
    }

    private void sendVerificationMail() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        openVerifyFragment();
                    } else {
                        Toast.makeText(getContext(), "Send Verification Email failed, Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
//        if (SELECTED_POSITION != -1) {
//            mBinding.filledExposedDropdown.setText(Common.getAllPositions(getContext())[SELECTED_POSITION], false);
//        }
    }

}
