package com.shouman.apps.hawk.ui.auth;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentForgetPasswordBinding;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Forget_Password extends Fragment {

    private FragmentForgetPasswordBinding mBinding;
    private FirebaseAuth auth;
    private String userEmail;

    public Fragment_Forget_Password() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        userEmail = Fragment_Forget_PasswordArgs.fromBundle(getArguments()).getArgEmail();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentForgetPasswordBinding.inflate(inflater);

        auth = FirebaseAuth.getInstance();

        if (userEmail != null)
            Objects.requireNonNull(mBinding.emailTextField.getEditText()).setText(userEmail);

        mBinding.btnSendPasswordEmail.setOnClickListener(v -> {
            final String email = Objects.requireNonNull(mBinding.edtEmailSignUp.getText()).toString();

            if (!email.isEmpty()) {

                auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(getContext(), "email send to " + email, Toast.LENGTH_SHORT).show();

                    } else {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (Exception e) {
                            e.printStackTrace();
                            String message = e.getMessage();
                            showErrorDialog(message);
                        }
                    }
                });

            } else {
                Toast.makeText(getContext(), "Please type your email address", Toast.LENGTH_SHORT).show();
            }
        });

        return mBinding.getRoot();
    }

    private void showErrorDialog(String message) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                .setIcon(R.drawable.ic_report_problem);
        builder.create().show();
    }
}
