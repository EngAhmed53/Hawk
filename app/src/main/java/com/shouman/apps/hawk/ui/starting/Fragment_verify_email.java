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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.FragmentVerifyEmailBinding;
import com.shouman.apps.hawk.model.BaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_verify_email extends Fragment {

    private static final String TAG = "Fragment_verify_email";
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private FragmentVerifyEmailBinding mBinding;

    public static Fragment_verify_email getInstance() {
        return new Fragment_verify_email();
    }

    public Fragment_verify_email() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        mBinding = FragmentVerifyEmailBinding.inflate(inflater);

        mBinding.resendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationEmail();
            }
        });

        mBinding.btnBackToChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "email deleted please resign up", Toast.LENGTH_SHORT).show();
                        openSignUpFragment();
                    }
                });
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        checkIfUserIsVerified();
        super.onResume();
    }

    private void resendVerificationEmail() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                "Email Sent Successfully to " + firebaseUser.getEmail() + ", Please check your inbox or spam",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Send Verification Email failed, Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void checkIfUserIsVerified() {
        if (firebaseUser != null) {
            Log.e(TAG, "checkIfUserIsVerified: firebase user is not null");
            firebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        isEmailVerified();
                    }
                }
            });
        }
    }

    private void isEmailVerified() {
        if (firebaseUser.isEmailVerified()) {
            BaseUser user = Common.baseUserLiveData.getValue();
            Log.e(TAG, "checkIfUserIsVerified: email is verified ");
            if (user != null) {
                Log.e(TAG, "checkIfUserIsVerified: base user is not null");
                user.setVerified(true);
                showSelectTypeFragment();
            } else {
                throw new IllegalStateException("the base user of the app is null");
            }
        } else {
            Log.e(TAG, "checkIfUserIsVerified: not verified");
        }
    }

    private void openSignUpFragment() {
        StartingActivity host = (StartingActivity) getActivity();
        if (host != null) host.showSignUpFragment();
    }

    private void showSelectTypeFragment() {
        StartingActivity host = (StartingActivity) getActivity();
        if (host != null) host.showSelectUserTypeFragment();
    }
}
