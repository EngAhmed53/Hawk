package com.shouman.apps.hawk.ui.main.companyUi.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentHomeBinding;
import com.shouman.apps.hawk.data.model.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class Fragment_home extends Fragment {

    private FragmentHomeBinding mBinding;
    private List<MenuItem> menuItems;

    public Fragment_home() {
        // Required empty public constructor
    }

    public static Fragment_home getInstance() {
        return new Fragment_home();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater);
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(getString(R.string.branches_title), R.drawable.ic_menu_branches));
        menuItems.add(new MenuItem(getString(R.string.sales_team_title), R.drawable.ic_employee));
        menuItems.add(new MenuItem(getString(R.string.customers_title), R.drawable.ic_team));
        menuItems.add(new MenuItem(getString(R.string.company_notification), R.drawable.ic_notification));
        menuItems.add(new MenuItem(getString(R.string.reports_title), R.drawable.ic_report));

        mBinding.setMenuItems(menuItems);
        return mBinding.getRoot();
    }


}
