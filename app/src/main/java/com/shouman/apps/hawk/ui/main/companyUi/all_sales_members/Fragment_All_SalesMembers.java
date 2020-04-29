package com.shouman.apps.hawk.ui.main.companyUi.all_sales_members;

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
import com.shouman.apps.hawk.databinding.FragmentAllSalesMembersBinding;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_All_SalesMembers extends Fragment {
    private static final String TAG = "FragmentAllSalesMember";
    private FragmentAllSalesMembersBinding mBinding;


    public Fragment_All_SalesMembers() {
        // Required empty public constructor
    }

    public static Fragment_All_SalesMembers getInstance() {
        return new Fragment_All_SalesMembers();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        mBinding = FragmentAllSalesMembersBinding.inflate(inflater);
        initViewModel();

        return mBinding.getRoot();
    }

    private void initViewModel() {
        AllSalesMembersViewModel allSalesMembersViewModel = new ViewModelProvider(this).get(AllSalesMembersViewModel.class);
        allSalesMembersViewModel.getAllSalesMembersMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, Map<String, SalesListItem>>>() {
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
