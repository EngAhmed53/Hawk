package com.shouman.apps.hawk.ui.main.companyUi.company_profile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.shouman.apps.hawk.databinding.FragmentCompanyProfileBinding;
import com.shouman.apps.hawk.model.Company;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_company_profile extends Fragment {


    private static final String COMPANY_UID = "company_uid";

    private String company_uid;

    private FragmentCompanyProfileBinding mBinding;


    public static Fragment_company_profile getInstance() {
        return new Fragment_company_profile();
    }

    public static Fragment_company_profile getInstance(String companyUID) {
        Fragment_company_profile fragment_company_profile = Fragment_company_profile.getInstance();
        Bundle args = new Bundle();
        args.putString(COMPANY_UID, companyUID);
        fragment_company_profile.setArguments(args);
        return fragment_company_profile;
    }

    public Fragment_company_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCompanyProfileBinding.inflate(inflater);

        company_uid = getArguments().getString(COMPANY_UID);

        //viewModel
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getCompanyMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<Company>() {
            @Override
            public void onChanged(Company company) {
                mBinding.setCompany(company);
                mBinding.edtCompanyUid.setText(company_uid);
            }
        });
        return mBinding.getRoot();
    }

}
