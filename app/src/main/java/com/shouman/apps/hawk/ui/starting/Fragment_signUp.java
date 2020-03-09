package com.shouman.apps.hawk.ui.starting;


import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.FragmentSignUpBinding;
import com.shouman.apps.hawk.model.UserMap;
import com.shouman.apps.hawk.preferences.UserPreference;


public class Fragment_signUp extends Fragment {

    private static final String TAG = "Fragment_signUp";
    public FragmentSignUpBinding mBinding;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase database;
    private DatabaseReference usersMapReference;

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
        database = FirebaseDatabase.getInstance();
        usersMapReference = database.getReference().child("usersMap");


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

                                        //make a new userMap object
                                        Common.userMap = new UserMap();

                                        //getThe mapUID from the email address
                                        String mapUID = Common.EmailToUID(email);

                                        //save email to userPreference
                                        UserPreference.setUserEmail(getContext(), email);
                                        Log.e(TAG, "onComplete: " + UserPreference.getUserEmail(getContext()) );

                                        //push userMap to database
                                        usersMapReference.child(mapUID).setValue(Common.userMap);

                                        //send verification email
                                        sendVerificationMail();
                                    } else {
                                        //there is an error
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
        //try to send verification email
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // send is success open the verification fragment
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        openVerifyFragment();
                    } else {
                        //send email error
                        Toast.makeText(getContext(), "Send Verification Email failed, Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //check if the InputText is not empty and did not has an error
    private boolean checkInputTextErrors(TextInputLayout inputLayout) {
        String text = inputLayout.getEditText().getText().toString();
        if (!text.isEmpty()) {
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
