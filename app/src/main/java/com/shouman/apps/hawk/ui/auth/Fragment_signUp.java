package com.shouman.apps.hawk.ui.auth;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentSignUpBinding;

import java.util.Collections;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class Fragment_signUp extends Fragment {

    private static final String TAG = "Fragment_signUp";

    private static final int RC_SIGN_IN = 122;
    private static final String USER_EMAIL = "user_email";

    public FragmentSignUpBinding mBinding;

    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;

    private AuthViewModel authViewModel;

    private FirebaseAuth firebaseAuth;

    public Fragment_signUp() {
        // Required empty public constructor
    }

    public static Fragment_signUp getInstance() {
        return new Fragment_signUp();
    }

    public static Fragment_signUp getInstance(String email) {
        Fragment_signUp fragmentSignUp = new Fragment_signUp();
        Bundle bundle = new Bundle();
        bundle.putString(USER_EMAIL, email);
        fragmentSignUp.setArguments(bundle);
        return fragmentSignUp;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSignUpBinding.inflate(inflater);
        firebaseAuth = FirebaseAuth.getInstance();

        initViewModel();
        initGoogleSignIn();
        initFacebookSignIn();
        initEmailPasswordSignIn();

        return mBinding.getRoot();
    }


    private void initViewModel() {
        authViewModel = new ViewModelProvider(getHostActivity()).get(AuthViewModel.class);
    }

    private void initEmailPasswordSignIn() {
        //email and password sign up button
        mBinding.btnEmailSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInputTextErrors(mBinding.emailTextField)
                        && checkInputTextErrors(mBinding.passwordTextField)) {

                    showTheSigningUPProgressBarLayout();

                    final String email =
                            Objects.requireNonNull(mBinding.emailTextField.getEditText()).getText().toString().trim();
                    final String password =
                            Objects.requireNonNull(mBinding.passwordTextField.getEditText()).getText().toString().trim();

                    handelEmailPasswordSignIn(email, password);

                } else {
                    if (mBinding.emailTextField.getError() != null ||
                            Objects.requireNonNull(mBinding.emailTextField.getEditText()).getText().toString().isEmpty()) {
                        mBinding.emailTextField.requestFocus();

                    } else if (mBinding.passwordTextField.getError() != null ||
                            Objects.requireNonNull(mBinding.passwordTextField.getEditText()).getText().toString().isEmpty()) {
                        mBinding.passwordTextField.requestFocus();

                    }
                }
            }
        });
    }

    private void initFacebookSignIn() {
        //facebook callbackManager
        callbackManager = CallbackManager.Factory.create();

        //setting the facebook login button
        mBinding.facebookLoginButton.setReadPermissions("email", "public_profile");
        mBinding.facebookLoginButton.setFragment(this);
        mBinding.facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handelFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "Log in cancelled", Toast.LENGTH_SHORT).show();
                hideTheSigningUPProgressBarLayout();
            }

            @Override
            public void onError(FacebookException error) {
                hideTheSigningUPProgressBarLayout();
                showErrorDialog(error.getMessage());

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

        //perform the facebook login button click _ i did this to keep the custom layout and the ripple effect
        mBinding.facebookSignUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.facebookLoginButton.performClick();
                showTheSigningUPProgressBarLayout();
            }
        });
    }

    private void initGoogleSignIn() {
        //define the google sign up button
        final AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.fragment_sign_up)
                .setGoogleButtonId(R.id.google_sign_up_layout)
                .setFacebookButtonId(R.id.facebook_sign_up_layout)
                .build();

        //the intent to open the google sign up
        final Intent googleIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAuthMethodPickerLayout(customLayout)
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .build();


        //open the google sign up
        mBinding.googleSignUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        googleIntent,
                        RC_SIGN_IN);
                showTheSigningUPProgressBarLayout();
            }
        });
    }

    private void handelFacebookToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    authViewModel.setupMediatorLiveData();

                } else {

                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (Exception e) {
                        e.printStackTrace();
                        showErrorDialog(e.getMessage());
                    }
                    hideTheSigningUPProgressBarLayout();
                    //sign out from facebook account because there was an erroe
                    LoginManager.getInstance().logOut();
                }
            }
        });
    }

    private void handelEmailPasswordSignIn(final String email, String password) {
        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            authViewModel.setupMediatorLiveData();
                            //hide progressBar
                            hideTheSigningUPProgressBarLayout();

                        } else {
                            //there is an error
                            //show error dialog
                            //hide progressBar
                            hideTheSigningUPProgressBarLayout();
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (Exception e) {
                                e.printStackTrace();
                                showErrorDialog(e.getMessage());
                            }
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "onActivityResult: " + "result ok" );
                authViewModel.setupMediatorLiveData();
                mBinding.signingUpText.setText(R.string.create_new_account_progress_bar_text);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Sign up cancelled !", Toast.LENGTH_SHORT).show();
                hideTheSigningUPProgressBarLayout();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //check if the InputText is not empty and did not has an error
    private boolean checkInputTextErrors(TextInputLayout inputLayout) {
        String text = Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
        if (!text.isEmpty()) {
            return inputLayout.getError() == null;
        }
        return false;
    }

    private StartingActivity getHostActivity() {
        return (StartingActivity) getActivity();
    }

    private void showTheSigningUPProgressBarLayout() {
        mBinding.signingUpProgressBar.setVisibility(View.VISIBLE);
        mBinding.signingUpProgressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));
        mBinding.mainLayout.setAlpha(0.5f);
    }

    private void hideTheSigningUPProgressBarLayout() {
        mBinding.signingUpProgressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        mBinding.signingUpProgressBar.setVisibility(View.GONE);
        mBinding.mainLayout.setAlpha(1.0f);
    }

//    private void openSignInFragment(String email) {
//        getHostActivity().showSignInFragment(email);
//    }

    private void showErrorDialog(String message) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getHostActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Error")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //no action to do just dismiss
                    }
                })
                .setIcon(R.drawable.ic_report_problem);
        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        accessTokenTracker.startTracking();
//        if (SELECTED_POSITION != -1) {
//            mBinding.filledExposedDropdown.setText(Common.getAllPositions(getContext())[SELECTED_POSITION], false);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        accessTokenTracker.stopTracking();

    }

    @Override
    public void onDestroyView() {
        hideTheSigningUPProgressBarLayout();
        super.onDestroyView();
    }
}
