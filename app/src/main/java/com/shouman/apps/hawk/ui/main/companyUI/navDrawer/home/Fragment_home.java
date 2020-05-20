package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_home extends Fragment {
    private static final String TAG = "Fragment_home";
    private FragmentHomeBinding mBinding;
    private HomeViewModel homeViewModel;


    public Fragment_home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentHomeBinding.inflate(inflater);


        mBinding.fabMenu.setOnMenuButtonClickListener(v -> {
            if (mBinding.fabMenu.isOpened()) {
                mBinding.protectionLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                mBinding.protectionLayout.setVisibility(View.GONE);
                mBinding.fabMenu.close(true);

            } else {
                mBinding.protectionLayout.setVisibility(View.VISIBLE);
                mBinding.protectionLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                mBinding.fabMenu.open(true);
            }
        });

        mBinding.fabItemAddSalesman.setOnClickListener(this::navigateToAddSalesman);

        mBinding.fabItemAddBranch.setOnClickListener(this::navigateToAddBranch);

        mBinding.protectionLayout.setOnClickListener(v -> closeFabMenu());

        return mBinding.getRoot();
    }

    private void navigateToAddBranch(View v) {
        Navigation.findNavController(v).navigate(R.id.action_fragment_home_to_fragment_add_new_branch);
        closeFabMenu();
    }

    private void navigateToAddSalesman(View v) {
        Navigation.findNavController(v).navigate(R.id.action_fragment_home_to_fragment_new_salesman);
        closeFabMenu();
    }

    private void closeFabMenu() {
        mBinding.fabMenu.close(true);
        mBinding.protectionLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        mBinding.protectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel.getAllSalesMembersMediatorLiveData().observe(getViewLifecycleOwner(), branch_sales_map ->
                mBinding.setBranchesSalesMap(branch_sales_map));
    }
}
