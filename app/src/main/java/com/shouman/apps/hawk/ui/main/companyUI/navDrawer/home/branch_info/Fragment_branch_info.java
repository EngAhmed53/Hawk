package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home.branch_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentBranchInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_branch_info extends Fragment {

    private FragmentBranchInfoBinding mBinding;
    private String branchName;

    public Fragment_branch_info() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentBranchInfoBinding.inflate(inflater);
        assert getArguments() != null;
        branchName = Fragment_branch_infoArgs.fromBundle(getArguments()).getBranchName();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI();
    }

    private void setUI() {
        mBinding.branchTitleBigTxt.setText(branchName);
        mBinding.branchTitleSamllTxt.setText(branchName);
        mBinding.branchCountryTxt.setText("Egypt");
        mBinding.branchCityTxt.setText("Cairo");
        mBinding.customersCountTxt.setText("15 Customers");
    }

    @Override
    public void onResume() {
        mBinding.infoFrame.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_down));
        super.onResume();
    }
}
