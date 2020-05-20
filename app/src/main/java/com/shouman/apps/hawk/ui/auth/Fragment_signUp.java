package com.shouman.apps.hawk.ui.auth;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentSignUpBinding;

import java.util.Collections;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class Fragment_signUp extends Fragment {

    private static final int RC_SIGN_IN = 122;

    public FragmentSignUpBinding mBinding;

    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;

    private AuthViewModel authViewModel;

    private FirebaseAuth firebaseAuth;

    public Fragment_signUp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transition customTransition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.logo_curve_transition);
        setSharedElementEnterTransition(customTransition);
        setSharedElementReturnTransition(customTransition);

        firebaseAuth = FirebaseAuth.getInstance();

        initViewModel();


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSignUpBinding.inflate(inflater);

        initGoogleSignUp();
        initFacebookSignIn();
        initEmailPasswordSignIn();

        return mBinding.getRoot();
    }


    private void initViewModel() {
        authViewModel = new ViewModelProvider(getHostActivity()).get(AuthViewModel.class);
    }

    private void initEmailPasswordSignIn() {
        //email and password sign up button
        mBinding.btnEmailSignUp.setOnClickListener(v -> {

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
        mBinding.facebookSignUpLayout.setOnClickListener(v -> {
            mBinding.facebookLoginButton.performClick();
            showTheSigningUPProgressBarLayout();
        });
    }

    private void initGoogleSignUp() {
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

        mBinding.googleSignUpLayout.setOnClickListener(v -> {
            startActivityForResult(
                    googleIntent,
                    RC_SIGN_IN);
            showTheSigningUPProgressBarLayout();
        });

    }

    private void handelFacebookToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                authViewModel.setupMediatorLiveData();

                navigateToSelectUserType();

            } else {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorDialog(e.getMessage());
                }
                hideTheSigningUPProgressBarLayout();
                //sign out from facebook account because there was an error
                LoginManager.getInstance().logOut();
            }
        });
    }

    private void handelEmailPasswordSignIn(final String email, String password) {
        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        authViewModel.setupMediatorLiveData();
                        //hide progressBar
                        hideTheSigningUPProgressBarLayout();
                        navigateToSelectUserType();

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
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {

                authViewModel.setupMediatorLiveData();

                mBinding.signingUpText.setText(R.string.create_new_account_progress_bar_text);

                navigateToSelectUserType();

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

    private void showErrorDialog(String message) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getHostActivity(), R.style.AlertDialogTheme)
                .setTitle("Error")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                .setIcon(R.drawable.ic_report_problem);
        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        accessTokenTracker.startTracking();
    }

    private void navigateToSelectUserType() {
        Navigation.findNavController(mBinding.btnEmailSignUp).navigate(R.id.action_fragment_signUp_to_fragment_select_user_type2);
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
