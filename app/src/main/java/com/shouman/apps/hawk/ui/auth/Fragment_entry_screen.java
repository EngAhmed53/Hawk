package com.shouman.apps.hawk.ui.auth;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.databinding.FragmentEntryScreenBinding;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Collections;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_entry_screen extends Fragment {

    public FragmentEntryScreenBinding mBinding;

    private static final int RC_SIGN_IN = 2525;

    private FirebaseAuth firebaseAuth;

    private UserPreference userPreference;

    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;

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

    public Fragment_entry_screen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        userPreference = UserPreference.getInstance();

        initViewModel();
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEntryScreenBinding.inflate(inflater);

        mBinding.emailCardView.setOnClickListener(v -> navigateToEmailContinueFragment());

        initGoogleSignIn();

        initFacebookSignIn();


        return mBinding.getRoot();
    }

    private void initFacebookSignIn() {
        //facebook callbackManager
        callbackManager = CallbackManager.Factory.create();

        //setting the facebook login button
        mBinding.facebookLoginButton.setPermissions("email", "public_profile");
        mBinding.facebookLoginButton.setFragment(this);
        mBinding.facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handelFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(requireContext(), R.string.log_in_cancelled, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
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
        mBinding.facebookCardView.setOnClickListener(v -> mBinding.facebookLoginButton.performClick());
    }

    private void initGoogleSignIn() {
        //define the google sign up button
        final AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.fragment_entry_screen)
                .setGoogleButtonId(R.id.google_card_view)
                .setFacebookButtonId(R.id.facebook_card_view)
                .build();

        //the intent to open the google sign up
        final Intent googleIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAuthMethodPickerLayout(customLayout)
                .setIsSmartLockEnabled(true)
                .setAvailableProviders(
                        Collections.singletonList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()
                        )
                )
                .build();

        //open the google sign in
        mBinding.googleCardView.setOnClickListener(v -> startActivityForResult(
                googleIntent,
                RC_SIGN_IN));
    }

    private void handelFacebookToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                authViewModel.setupMediatorLiveData();

            } else {

                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorDialog(e.getMessage());
                }
                //sign out from facebook account because there was an error
                LoginManager.getInstance().logOut();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {

                authViewModel.setupMediatorLiveData();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(requireContext(), R.string.log_in_cancelled, Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showErrorDialog(String message) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(getString(R.string.error))
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok_dialog_btn), (dialog, which) -> dialog.dismiss())
                .setIcon(R.drawable.ic_report_problem);

        builder.create().show();
    }

    private void showMainActivity(User user) {
        String userType = user.getUt();

        if (userType.equals("company_account")) {
            Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_fragment_entry_screen_to_mainActivity);
            userPreference.setCompanyUID(requireContext(), user.getCuid());
            userPreference.setCompanyName(requireContext(), user.getCn());
            userPreference.setUserName(requireContext(), user.getUn());
            userPreference.setUserType(requireContext(), user.getUt());
        } else if (userType.equals("sales_account")) {
            Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_fragment_entry_screen_to_main2Activity);
            userPreference.setBranchUID(requireContext(), user.getBuid());
            userPreference.setCompanyUID(requireContext(), user.getCuid());
            userPreference.setCompanyName(requireContext(), user.getCn());
            userPreference.setSalesmanStatus(requireContext(), user.isStatus());
            userPreference.setUserName(requireContext(), user.getUn());
            userPreference.setUserType(requireContext(), user.getUt());
        }

        requireActivity().finish();
    }

    private void navigateToSelectUserType() {
        Navigation.findNavController(requireActivity(), R.id.starting_container).navigate(R.id.fragment_select_user_type);
    }

    private void navigateToEmailContinueFragment() {
        Navigation.findNavController(requireActivity(), R.id.starting_container).navigate(R.id.action_fragment_entry_screen_to_fragment_email_continue);
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
        accessTokenTracker.startTracking();
    }

    @Override
    public void onPause() {
        super.onPause();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onStop() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        super.onStop();
    }
}
