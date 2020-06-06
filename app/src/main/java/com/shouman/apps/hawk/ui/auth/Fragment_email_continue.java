package com.shouman.apps.hawk.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.databinding.FragmentEmailContinueBinding;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_email_continue extends Fragment {

    private FirebaseAuth firebaseAuth;

    private UserPreference userPreference;


    public FragmentEmailContinueBinding mBinding;

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
        Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_fragment_email_continue_to_fragment_select_user_type);
    }


    public Fragment_email_continue() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        userPreference = UserPreference.getInstance();

        initViewModel();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentEmailContinueBinding.inflate(inflater);

        initEmailPasswordSignIn();
        initEmailPasswordSignUp();

        //open forget password fragment
        mBinding.forgetPassword.setOnClickListener(v -> {
            String email = Objects.requireNonNull(mBinding.edtEmailSignIn.getText()).toString();
            openForgetPasswordFragment(email);
        });

        return mBinding.getRoot();
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }


    // Sign In Functionality
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
    private void handelEmailPasswordSignIn(final String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        authViewModel.setupMediatorLiveData();
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
                }
        );
    }


    // Sign Up Functionality
    private void initEmailPasswordSignUp() {
        //email and password sign up button
        mBinding.btnEmailSignUp.setOnClickListener(v -> {

            if (checkInputTextErrors(mBinding.emailInputLayout)
                    && checkInputTextErrors(mBinding.passwordInputLayout)) {

                showTheSigningINProgressBarLayout();

                final String email =
                        Objects.requireNonNull(mBinding.emailInputLayout.getEditText()).getText().toString().trim();
                final String password =
                        Objects.requireNonNull(mBinding.passwordInputLayout.getEditText()).getText().toString().trim();

                handelEmailPasswordSignUP(email, password);

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
    private void handelEmailPasswordSignUP(final String email, String password) {
        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        authViewModel.setupMediatorLiveData();
                        //hide progressBar
                        hideTheSigningINProgressBarLayout();
                        navigateToSelectUserType();

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


    private void openForgetPasswordFragment(String email) {
        NavDirections signInToForgetPassword = Fragment_email_continueDirections.actionFragmentEmailContinueToFragmentForgetPassword(email);
        Navigation.findNavController(mBinding.forgetPassword).navigate(signInToForgetPassword);
    }

    private void showMainActivity(User user) {
        String userType = user.getUt();

        if (userType.equals("company_account")) {
            Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_fragment_email_continue_to_mainActivity);
            userPreference.setCompanyUID(requireContext(), user.getCuid());
            userPreference.setCompanyName(requireContext(), user.getCn());
            userPreference.setUserName(requireContext(), user.getUn());
            userPreference.setUserType(requireContext(), user.getUt());
        } else if (userType.equals("sales_account")) {
            Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_fragment_email_continue_to_main2Activity);
            userPreference.setBranchUID(requireContext(), user.getBuid());
            userPreference.setCompanyUID(requireContext(), user.getCuid());
            userPreference.setCompanyName(requireContext(), user.getCn());
            userPreference.setSalesmanStatus(requireContext(), user.isStatus());
            userPreference.setUserName(requireContext(), user.getUn());
            userPreference.setUserType(requireContext(), user.getUt());
        }

        requireActivity().finish();
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
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(getString(R.string.error))
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok_dialog_btn), (dialog, which) -> dialog.dismiss())
                .setIcon(R.drawable.ic_report_problem);

        builder.create().show();
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
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
        super.onResume();
    }

    @Override
    public void onStop() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        super.onStop();
    }
}
