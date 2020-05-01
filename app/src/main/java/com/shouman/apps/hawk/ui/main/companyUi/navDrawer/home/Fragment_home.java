package com.shouman.apps.hawk.ui.main.companyUi.navDrawer.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.shouman.apps.hawk.data.model.SalesListItem;
import com.shouman.apps.hawk.databinding.FragmentHomeBinding;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_home extends Fragment {
    private static final String TAG = "FragmentAllSalesMember";
    private FragmentHomeBinding mBinding;


    public Fragment_home() {
        // Required empty public constructor
    }

    public static Fragment_home getInstance() {
        return new Fragment_home();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        mBinding = FragmentHomeBinding.inflate(inflater);
        initViewModel();

        return mBinding.getRoot();
    }

    private void initViewModel() {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAllSalesMembersMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, Map<String, SalesListItem>>>() {
            @Override
            public void onChanged(Map<String, Map<String, SalesListItem>> branch_sales_map) {
                Log.e(TAG, "onChanged: " + branch_sales_map.keySet().toString());
                mBinding.setBranchesSalesMap(branch_sales_map);
            }
        });
    }
//    private void initToolbar() {
////        mBinding.bar.setNavigationOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                getActivity().finish();
////            }
////        });
//    }
}
