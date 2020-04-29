package com.shouman.apps.hawk.ui.main.companyUi.all_branches;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentAllBranchesBinding;
import com.shouman.apps.hawk.ui.main.companyUi.ContainerActivity;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_all_branches extends Fragment {

    private static final String TAG = "Fragment_company_home";
    private FragmentAllBranchesBinding mBinding;


    public Fragment_all_branches() {
        // Required empty public constructor
    }

    public static Fragment_all_branches getInstance() {
        return new Fragment_all_branches();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAllBranchesBinding.inflate(inflater);

        //init Toolbar
        initToolbar();
        //viewModel
        CompanyHomeViewModel companyHomeViewModel = new ViewModelProvider(this).get(CompanyHomeViewModel.class);

        //get all company BranchesUID
        companyHomeViewModel.getMapMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> branches_UID_Names_Map) {
                if (branches_UID_Names_Map != null) {

                    mBinding.setBranchesMap(branches_UID_Names_Map);
                } else {
                    Log.e(TAG, "onChanged: " + "branches list is null");
                }
            }
        });


        mBinding.fabAddNewBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNewBranchFragment();
            }
        });


        mBinding.recBranches.setNestedScrollingEnabled(false);
        return mBinding.getRoot();
    }

    private void initToolbar() {
        mBinding.toolbar.inflateMenu(R.menu.all_branches_toolbar_menu);
        mBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        openSearchFragment();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void openAddNewBranchFragment() {
        ContainerActivity host = (ContainerActivity) getActivity();
        if (host != null) {
            host.showAddNewBranchFragment();
        }
    }

    private void openSearchFragment() {
        Toast.makeText(getContext(), "Open Search Fragment", Toast.LENGTH_SHORT).show();
    }
}
