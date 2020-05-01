package com.shouman.apps.hawk.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.databinding.FragmentSplashScreenBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Splash_Screen extends Fragment {

    private static final String TAG = "Fragment_Splash_Screen";

    private FragmentSplashScreenBinding mBinding;

    private FirebaseAuth firebaseAuth;


    private AuthViewModel authViewModel;

    private FirebaseAuth.AuthStateListener firebaseAuthListener = firebaseAuth -> {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Log.e(TAG, "onAuthStateChanged: " + "user is not null");
            //reload firebase user
            firebaseUser.reload();
            authViewModel.getUserMediatorLiveData().observe(getViewLifecycleOwner(), user -> {
                if (user.getUt() == null) {
                    showSelectUserTypeFragment();
                } else {
                    showMainActivity(user);
                }
            });

        } else {
            showEntryScreen();
        }
    };

    public Fragment_Splash_Screen() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StartingActivity host = (StartingActivity) getActivity();

        firebaseAuth = FirebaseAuth.getInstance();

        assert host != null;
        authViewModel = new ViewModelProvider(host).get(AuthViewModel.class);

        Handler handler = new Handler();
        handler.postDelayed(() -> firebaseAuth.addAuthStateListener(firebaseAuthListener), 5000);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSplashScreenBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    private void showSelectUserTypeFragment() {
        Navigation.findNavController(mBinding.appNameText).navigate(R.id.action_fragment_Splash_Screen_to_fragment_select_user_type);
    }

    private void showMainActivity(User user) {
        String userType = user.getUt();

        Intent intent = null;

//        if (userType.equals("company_account")) {
//            intent = new Intent(requireContext(), MainActivity.class);
//            UserPreference.setCompanyUID(requireContext(), user.getCuid());
//            UserPreference.setCompanyName(requireContext(), user.getCn());
//        } else if (userType.equals("sales_account")) {
//            intent = new Intent(requireContext(), Main2Activity.class);
//
//            UserPreference.setBranchUID(requireContext(), user.getBuid());
//            UserPreference.setCompanyUID(requireContext(), user.getCuid());
//            UserPreference.setCompanyName(requireContext(), user.getCn());
//            UserPreference.setSalesmanStatus(requireContext(), user.isStatus());
//        }
//        if (intent != null)
//            startActivity(intent);
    }

    private void showEntryScreen() {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(mBinding.appLogoImage, getString(R.string.logo_image))
                .build();

        Navigation.findNavController(mBinding.appNameText).navigate(R.id.action_fragment_Splash_Screen_to_fragment_entry_screen,
                null,
                null,
                extras);
    }

    @Override
    public void onDestroy() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        super.onDestroy();
    }
}
