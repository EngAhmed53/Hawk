package com.shouman.apps.hawk.ui.main.companyUi.navDrawer.profile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.shouman.apps.hawk.databinding.FragmentCompanyProfileBinding;
import com.shouman.apps.hawk.data.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_profile extends Fragment {


    private FragmentCompanyProfileBinding mBinding;


    public static Fragment_profile getInstance() {
        return new Fragment_profile();
    }

    public Fragment_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCompanyProfileBinding.inflate(inflater);

        //viewModel
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getCompanyMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mBinding.setUser(user);
            }
        });
        return mBinding.getRoot();
    }

}
