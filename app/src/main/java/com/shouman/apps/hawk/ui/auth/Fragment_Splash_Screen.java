package com.shouman.apps.hawk.ui.auth;

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
import com.shouman.apps.hawk.preferences.UserPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Splash_Screen extends Fragment {

    private static final String TAG = "Fragment_Splash_Screen";

    private FragmentSplashScreenBinding mBinding;

    private FirebaseAuth firebaseAuth;

    private UserPreference userPreference;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreference = UserPreference.getInstance();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StartingActivity host = (StartingActivity) getActivity();

        firebaseAuth = FirebaseAuth.getInstance();

        assert host != null;
        authViewModel = new ViewModelProvider(host).get(AuthViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSplashScreenBinding.inflate(inflater);

        Handler handler = new Handler();
        handler.postDelayed(() -> firebaseAuth.addAuthStateListener(firebaseAuthListener), 0);

        return mBinding.getRoot();
    }

    private void showSelectUserTypeFragment() {
        Navigation.findNavController(mBinding.appNameText).navigate(R.id.action_fragment_Splash_Screen_to_fragment_select_user_type);
    }

    private void showMainActivity(User user) {
        String userType = user.getUt();

        if (userType.equals("company_account")) {
            Navigation.findNavController(mBinding.appNameText).navigate(R.id.action_fragment_Splash_Screen_to_mainActivity);
            Log.e(TAG, "showMainActivity: to company");
            userPreference.setCompanyUID(requireContext(), user.getCuid());
            userPreference.setCompanyName(requireContext(), user.getCn());
            userPreference.setUserName(requireContext(), user.getUn());
            userPreference.setUserType(requireContext(), user.getUt());
        } else if (userType.equals("sales_account")) {
            Navigation.findNavController(mBinding.appNameText).navigate(R.id.action_fragment_Splash_Screen_to_main2Activity);
            userPreference.setBranchUID(requireContext(), user.getBuid());
            userPreference.setCompanyUID(requireContext(), user.getCuid());
            userPreference.setCompanyName(requireContext(), user.getCn());
            userPreference.setSalesmanStatus(requireContext(), user.isStatus());
            userPreference.setUserName(requireContext(), user.getUn());
            userPreference.setUserType(requireContext(), user.getUt());
        }
        requireActivity().finish();

//        if (userType.equals("company_account")) {
//            intent = new Intent(requireContext(), MainActivity.class);
//            userPreference.setCompanyUID(requireContext(), user.getCuid());
//            userPreference.setCompanyName(requireContext(), user.getCn());
//        } else if (userType.equals("sales_account")) {
//            intent = new Intent(requireContext(), Main2Activity.class);
//
//            userPreference.setBranchUID(requireContext(), user.getBuid());
//            userPreference.setCompanyUID(requireContext(), user.getCuid());
//            userPreference.setCompanyName(requireContext(), user.getCn());
//            userPreference.setSalesmanStatus(requireContext(), user.isStatus());
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
