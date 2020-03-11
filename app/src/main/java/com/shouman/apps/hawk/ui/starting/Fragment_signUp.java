package com.shouman.apps.hawk.ui.starting;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.FragmentSignUpBinding;
import com.shouman.apps.hawk.model.UserMap;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Arrays;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class Fragment_signUp extends Fragment {

    private static final String TAG = "Fragment_signUp";
    private static final int RC_SIGN_IN = 122;
    public FragmentSignUpBinding mBinding;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase database;
    private DatabaseReference usersMapReference;

    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;

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
        database.setPersistenceEnabled(true);

        usersMapReference = database.getReference().child("usersMap");
        callbackManager = CallbackManager.Factory.create();

        mBinding.facebookLoginButton.setReadPermissions("email", "public_profile");
        mBinding.facebookLoginButton.setFragment(this);
        mBinding.facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onFacebook Success: " + loginResult);
                handelFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "Log in cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "onError: " + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    firebaseAuth.signOut();
                }
            }
        };

        //define the google sign up button
        final AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.fragment_sign_up)
                .setGoogleButtonId(R.id.google_sign_up_layout)
                .setFacebookButtonId(R.id.facebook_sign_up_layout)
                .build();

        //the intent to open the google sign up
        final Intent googleIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAuthMethodPickerLayout(customLayout)
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .build();


        //open the google sign up
        mBinding.googleSignUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        googleIntent,
                        RC_SIGN_IN);
            }
        });

        //perform the facebook login button click _ i did this to keep the custom layout and the ripple effect
        mBinding.facebookSignUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.facebookLoginButton.performClick();
            }
        });


        //email and password sign up button
        mBinding.btnEmailSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInputTextErrors(mBinding.emailTextField)
                        && checkInputTextErrors(mBinding.passwordTextField)) {

                    final String email = mBinding.emailTextField.getEditText().getText().toString().trim();
                    final String password = mBinding.passwordTextField.getEditText().toString().trim();

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

    private void handelFacebookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "handelFacebook " + task.isSuccessful());

                    String email = null;

                    //get the user email
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        email = firebaseUser.getEmail();
                        UserPreference.setUserEmail(getContext(), email);
                    }
                    //transform email to emailUID
                    final String userMapUID = Common.EmailToUID(email);
                    if (userMapUID != null) {
                        usersMapReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(userMapUID)) {
                                    //user is already exist
                                    performUserExistCheck(dataSnapshot, userMapUID);

                                } else {
                                    //user is not exist create new user account
                                    createNewAccount();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                } else {
                    Log.e(TAG, "handelFacebook " + task.isSuccessful());
                }
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                //first we need to check if the user is exist in the firebase users database
                //if the user was exist we show a toast and send the user to sign in fragment
                //if the user is not exist we do a complete sign up process

                String email = null;

                //get the user email
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    email = firebaseUser.getEmail();
                    UserPreference.setUserEmail(getContext(), email);
                }
                //transform email to emailUID
                final String userMapUID = Common.EmailToUID(email);
                if (userMapUID != null) {
                    usersMapReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(userMapUID)) {
                                //user is already exist
                                performUserExistCheck(dataSnapshot, userMapUID);

                            } else {
                                //user is not exist create new user account
                                createNewAccount();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Sign up cancelled !", Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void performUserExistCheck(@NonNull DataSnapshot dataSnapshot, String userMapUID) {
        Log.e(TAG, "performUserExistCheck: " + "user is exist");
        Common.userMap = new UserMap();
        //get the user path
        DataSnapshot userSnapshot = dataSnapshot.child(userMapUID);
        //check is user path is defined
        if (userSnapshot.hasChild("path")) {
            //user path is found
            //check if it is not null
            String userType = (String) userSnapshot.child("path").getValue();
            if (userType != null && !userType.isEmpty()) {
                Log.e(TAG, "performUserExistCheck: " + "user path is defined");
                //user path is not null
                //get the user type
                if (userType.equals("companies")) {
                    Log.e(TAG, "performUserExistCheck: " + "user is company");
                    //user is company type
                    //so we need to get the company uid
                    //we add this type to user preferences
                    UserPreference.setUserInfoSettedTrue(getContext());
                    UserPreference.setUserTypeTrue(getContext());
                    UserPreference.setUserType(getContext(), 0);
                    Common.userMap.setPath("companies");

                    if (userSnapshot.hasChild("userUID")) {
                        String userUID = (String) userSnapshot.child("userUID").getValue();
                        Log.e(TAG, "performUserExistCheck: " + "user uid founded " + userUID);

                        if (userUID != null && !userUID.isEmpty()) {
                            // we found the user and all is set
                            //we add this uid to user Preferences
                            UserPreference.setUserUID(getContext(), userUID);
                            Common.userMap.setUserUID(userUID);
                            Common.userMap.setVerified(true);
                            Common.userMap.setBranchUID(null);
                        }
                    }

                } else if (userType.equals("sales_member")) {
                    // user is sales_member type
                    //so we need to get the user uid and branch uid
                    //we add this type to user preferences
                    UserPreference.setUserInfoSettedTrue(getContext());
                    UserPreference.setUserTypeTrue(getContext());
                    UserPreference.setUserType(getContext(), 1);

                    //here we get the userUID
                    if (userSnapshot.hasChild("userUID")) {
                        String userUID = (String) userSnapshot.child("userUID").getValue();
                        if (userUID != null && !userUID.isEmpty()) {
                            // we found the userUID
                            UserPreference.setUserUID(getContext(), userUID);
                            Common.userMap.setUserUID(userUID);
                        }
                    }

                    //and here we get the branchUID
                    if (userSnapshot.hasChild("branchUID")) {
                        String branchUID = (String) userSnapshot.child("branchUID").getValue();
                        if (branchUID != null && !branchUID.isEmpty()) {
                            // we found the user branchUID
                            UserPreference.setBranchUID(getContext(), branchUID);
                            Common.userMap.setVerified(true);
                            Common.userMap.setBranchUID(branchUID);
                        }
                    }
                }
            }
        } else {
            //user path is not found we open the verify fragment
            openVerifyFragment();
        }
    }

    private void createNewAccount() {
        //make a new userMap object
        String email = null;
        Common.userMap = new UserMap();

        //get the user email
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
            UserPreference.setUserEmail(getContext(), email);
        }
        //transform email to emailUID
        final String userMapUID = Common.EmailToUID(email);
        if (userMapUID != null) {
            //push userMap to database
            Common.userMap.setUserUID(userMapUID);
            usersMapReference.child(userMapUID).setValue(Common.userMap);
            openVerifyFragment();
        }
    }
}
