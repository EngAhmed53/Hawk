package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.profile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentCompanyProfileBinding;
import com.shouman.apps.hawk.ui.main.companyUI.navDrawer.MainActivity;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_profile extends Fragment {

    private FragmentCompanyProfileBinding mBinding;
    private ProfileViewModel profileViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = getProfileViewModel();
        animateToolbarZDown();
    }

    public Fragment_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCompanyProfileBinding.inflate(inflater);

        profileViewModel.getCompanyMediatorLiveData().observe(getViewLifecycleOwner(), user -> mBinding.setUser(user));

        return mBinding.getRoot();
    }

    @NotNull
    private ProfileViewModel getProfileViewModel() {
        return new ViewModelProvider(this).get(ProfileViewModel.class);
    }


    private void animateToolbarZDown() {
        ((MainActivity) requireActivity()).mBinding.toolbar.setBackgroundColor(requireContext().getResources().getColor(R.color.colorPrimary));
        ViewPropertyAnimator animate = ((MainActivity) requireActivity()).mBinding.toolbar.animate();
        animate.z(1)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(500).start();
    }


    private void animateToolbarZUp() {
        ((MainActivity) requireActivity()).mBinding.toolbar.setBackgroundColor(requireContext().getResources().getColor(R.color.black_transparent));
        ViewPropertyAnimator animate = ((MainActivity) requireActivity()).mBinding.toolbar.animate();
        animate.z(8)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(500).start();
    }

    @Override
    public void onResume() {
        mBinding.infoFrame.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_down));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        animateToolbarZUp();
        super.onDestroy();
    }
}
