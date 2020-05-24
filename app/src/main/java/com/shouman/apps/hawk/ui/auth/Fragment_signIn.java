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
import androidx.navigation.NavDirections;
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
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.databinding.FragmentSignInBinding;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Collections;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Fragment_signIn extends Fragment {
    private static final int RC_SIGN_IN = 2525;

    private FirebaseAuth firebaseAuth;

    private UserPreference userPreference;

    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;

    public FragmentSignInBinding mBinding;

    private AuthViewModel authViewModel;

    private FirebaseAuth.AuthStateListener firebaseAuthListener = firebaseAuth -> {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {

            //reload firebase user

            firebaseUser.reload();

            authViewModel.getUserMediatorLiveData().observe(getViewLifecycleOwner(), user -> {
                if (user.getUt() != null) {
                    showMainActivity(user);
                } else {
                    navigateToSelectUserType();
                }
            });
        }
    };

    private void navigateToSelectUserType() {
        Navigation.findNavController(mBinding.btnEmailSignIn).navigate(R.id.action_fragment_signIn_to_fragment_select_user_type2);
    }

    public Fragment_signIn() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transition customTransition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.logo_curve_transition);

        setSharedElementEnterTransition(customTransition);

        setSharedElementReturnTransition(customTransition);

        firebaseAuth = FirebaseAuth.getInstance();

        userPreference = UserPreference.getInstance();

        initViewModel();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentSignInBinding.inflate(inflater);

        initGoogleSignIn();

        initFacebookSignIn();

        initEmailPasswordSignIn();

        //open forget password fragment
        mBinding.forgetPassword.setOnClickListener(v -> {
            String email = Objects.requireNonNull(mBinding.edtEmailSignIn.getText()).toString();
            openForgetPasswordFragment(email);
        });

        return mBinding.getRoot();
    }


    private void initViewModel() {
        authViewModel = new ViewModelProvider(getHostActivity()).get(AuthViewModel.class);
    }

    private void initEmailPasswordSignIn() {
        //email and password sign up button
        mBinding.btnEmailSignIn.setOnClickListener(v -> {

            if (checkInputTextErrors(mBinding.emailInputLayout)
                    && checkInputTextErrors(mBinding.passwordInputLayout)) {

                showTheSigningINProgressBarLayout();

                final String email =
                        Objects.requireNonNull(mBinding.emailInputLayout.getEditText()).getText().toString().trim();
                final String password =
                        Objects.requireNonNull(mBinding.passwordInputLayout.getEditText()).getText().toString().trim();

                handelEmailPasswordSignIn(email, password);

            } else {
                if (mBinding.emailInputLayout.getError() != null ||
                        Objects.requireNonNull(mBinding.emailInputLayout.getEditText()).getText().toString().isEmpty()) {
                    mBinding.emailInputLayout.requestFocus();

                } else if (mBinding.passwordInputLayout.getError() != null ||
                        Objects.requireNonNull(mBinding.passwordInputLayout.getEditText()).getText().toString().isEmpty()) {
                    mBinding.passwordInputLayout.requestFocus();

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
                Toast.makeText(requireContext(), "Log in cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                hideTheSigningINProgressBarLayout();
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
        mBinding.facebookSignInLayout.setOnClickListener(v -> {
            mBinding.facebookLoginButton.performClick();
            showTheSigningINProgressBarLayout();
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
                .setAuthMethodPickerLayout(customLayout)
                .setAvailableProviders(
                        Collections.singletonList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()
                        )
                )
                .build();

        //open the google sign in
        mBinding.googleSignInLayout.setOnClickListener(v -> {
            startActivityForResult(
                    googleIntent,
                    RC_SIGN_IN);
            showTheSigningINProgressBarLayout();
        });
    }

    private void handelFacebookToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                authViewModel.setupMediatorLiveData();

                firebaseAuth.addAuthStateListener(firebaseAuthListener);

            } else {

                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorDialog(e.getMessage());
                }
                hideTheSigningINProgressBarLayout();
                //sign out from facebook account because there was an erroe
                LoginManager.getInstance().logOut();
            }
        });
    }

    private void handelEmailPasswordSignIn(final String email, String password) {
        firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        authViewModel.setupMediatorLiveData();
                        firebaseAuth.addAuthStateListener(firebaseAuthListener);
                        //hide progressBar
                        hideTheSigningINProgressBarLayout();

                    } else {
                        //there is an error
                        //show error dialog
                        //hide progressBar
                        hideTheSigningINProgressBarLayout();
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
                firebaseAuth.addAuthStateListener(firebaseAuthListener);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(requireContext(), "Sign in cancelled!", Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private StartingActivity getHostActivity() {
        return (StartingActivity) getActivity();
    }

    private void showTheSigningINProgressBarLayout() {
        mBinding.signingInProgressBar.setVisibility(View.VISIBLE);
        mBinding.signingInProgressBar.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_animation));
        mBinding.mainLayout.setAlpha(0.5f);
    }

    private void hideTheSigningINProgressBarLayout() {
        mBinding.signingInProgressBar.startAnimation(AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_out));
        mBinding.signingInProgressBar.setVisibility(View.GONE);
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

    private void openForgetPasswordFragment(String email) {
        NavDirections signInToForgetPassword = Fragment_signInDirections.actionFragmentSignInToFragmentForgetPassword(email);
        Navigation.findNavController(mBinding.forgetPassword).navigate(signInToForgetPassword);
    }

    private void showMainActivity(User user) {
        String userType = user.getUt();

        if (userType.equals("company_account")) {
            Navigation.findNavController(mBinding.logoImage).navigate(R.id.action_fragment_signIn_to_mainActivity);
            userPreference.setCompanyUID(requireContext(), user.getCuid());
            userPreference.setCompanyName(requireContext(), user.getCn());
            userPreference.setUserName(requireContext(), user.getUn());
            userPreference.setUserType(requireContext(), user.getUt());
        } else if (userType.equals("sales_account")) {
            Navigation.findNavController(mBinding.logoImage).navigate(R.id.action_fragment_signIn_to_main2Activity);
            userPreference.setBranchUID(requireContext(), user.getBuid());
            userPreference.setCompanyUID(requireContext(), user.getCuid());
            userPreference.setCompanyName(requireContext(), user.getCn());
            userPreference.setSalesmanStatus(requireContext(), user.isStatus());
            userPreference.setUserName(requireContext(), user.getUn());
            userPreference.setUserType(requireContext(), user.getUt());
        }

        requireActivity().finish();
    }


    //check if the InputText is not empty and did not has an error
    private boolean checkInputTextErrors(TextInputLayout inputLayout) {
        String text = Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
        if (!text.isEmpty()) {
            return inputLayout.getError() == null;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        accessTokenTracker.startTracking();
    }

    @Override
    public void onPause() {
        super.onPause();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onDestroy() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        super.onDestroy();
    }
}
