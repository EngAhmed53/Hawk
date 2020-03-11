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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.FragmentVerifyEmailBinding;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_verify_email extends Fragment {

    private static final String TAG = "Fragment_verify_email";
    private FirebaseUser firebaseUser;

    private FirebaseAuth auth;

    private FragmentVerifyEmailBinding mBinding;

    private FirebaseDatabase database;
    private DatabaseReference userMapReference;

    public static Fragment_verify_email getInstance() {
        return new Fragment_verify_email();
    }

    public Fragment_verify_email() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();

        //get user Email to get userMapUID
        String userEmail = UserPreference.getUserEmail(getContext());

        if (userEmail != null) {
            Log.e(TAG, "onCreateView: " + userEmail);
            userMapReference = database.getReference().child("usersMap").child(Common.EmailToUID(userEmail));
        } else {
            Toast.makeText(getContext(), "email is empty", Toast.LENGTH_SHORT).show();
        }

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

    //resend email if the user did not get it at first time
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

    //check if the user verified his email
    private void checkIfUserIsVerified() {
        if (firebaseUser != null) {
            firebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        verificationDone();
                    }
                }
            });
        }
    }

    private void verificationDone() {
        boolean isFacebook = false;
        for (UserInfo user : firebaseUser.getProviderData()) {
            if (user.getProviderId().equals("facebook.com")) {
                isFacebook = true;
            }
        }

        //check if the user is facebook and make it verified
        if (isFacebook) {
            //push the updated userMap to data base
            HashMap<String, Object> newValues = new HashMap<>();
            newValues.put("verified", true);
            if (userMapReference != null) {
                userMapReference.updateChildren(newValues);
            }
            //then show select type fragment
            showSelectTypeFragment();
            return;
        }
        if (firebaseUser.isEmailVerified()) {
            Common.userMap.setVerified(true);

            //push the updated userMap to data base
            HashMap<String, Object> newValues = new HashMap<>();
            newValues.put("verified", true);
            if (userMapReference != null) {
                userMapReference.updateChildren(newValues);
            }

            //then show select type fragment
            showSelectTypeFragment();
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
