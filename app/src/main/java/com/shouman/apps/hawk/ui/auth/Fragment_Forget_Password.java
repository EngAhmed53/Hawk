package com.shouman.apps.hawk.ui.auth;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentForgetPasswordBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Forget_Password extends Fragment {

    private static final String USER_EMAIL = "user_email";
    private FragmentForgetPasswordBinding mBinding;
    private FirebaseAuth auth;
    private String email;

    public Fragment_Forget_Password() {
        // Required empty public constructor
    }

    public static Fragment_Forget_Password getInstance(String email) {
        Fragment_Forget_Password fragment_forget_password = new Fragment_Forget_Password();
        Bundle bundle = new Bundle();
        bundle.putString(USER_EMAIL, email);
        fragment_forget_password.setArguments(bundle);
        return fragment_forget_password;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentForgetPasswordBinding.inflate(inflater);

        auth = FirebaseAuth.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(USER_EMAIL)) {
                email = bundle.getString(USER_EMAIL);
                if (email != null && !email.isEmpty()) {
                    mBinding.edtEmailSignUp.setText(email);
                } else {
                    mBinding.edtEmailSignUp.setText("");
                }
            }
        }

        mBinding.btnSendPasswordEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mBinding.edtEmailSignUp.getText().toString();
                if (email != null && !email.isEmpty()) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "email send to " + email, Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    String message = e.getMessage();
                                    showErrorDialog(message);
                                }
                            }
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Please type your email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mBinding.getRoot();
    }

    private void showErrorDialog(String message) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_report_problem);
        builder.create().show();
    }
}
