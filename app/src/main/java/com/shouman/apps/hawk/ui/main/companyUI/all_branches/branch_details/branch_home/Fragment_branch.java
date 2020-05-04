package com.shouman.apps.hawk.ui.main.companyUI.all_branches.branch_details.branch_home;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentBranchDetailsBinding;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_branch extends Fragment {

    private static final String BRANCH_UID = "branch_uid";
    private static final String BRANCH_NAME = "branch_name";
    private String branchUID;
    private String branchName;
    public FragmentBranchDetailsBinding mBinding;


    public static Fragment_branch getInstance() {
        return new Fragment_branch();
    }

    public static Fragment_branch getInstance(String branchUID, String branchName) {
        Bundle args = new Bundle();
        args.putString(BRANCH_UID, branchUID);
        args.putString(BRANCH_NAME, branchName);
        Fragment_branch fragment_branch = Fragment_branch.getInstance();
        fragment_branch.setArguments(args);
        return fragment_branch;
    }

    public Fragment_branch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentBranchDetailsBinding.inflate(inflater);
        Bundle arguments = getArguments();
        if (arguments != null) {
            branchUID = arguments.getString(BRANCH_UID);
            branchName = arguments.getString(BRANCH_NAME);
        }

        BranchViewModelFactory factory = new BranchViewModelFactory(getContext(), branchUID);
        BranchViewModel branchViewModel = new ViewModelProvider(this, factory).get(BranchViewModel.class);
        branchViewModel.getMediatorBranchLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> salesMap) {
                mBinding.setSalesMembersMap(salesMap);
            }
        });

        toolbarCustomization();
        initializeChart();

        mBinding.recSalesMember.setNestedScrollingEnabled(false);
        return mBinding.getRoot();
    }


    private void toolbarCustomization() {
        mBinding.toolbar.setTitle(branchName);
        mBinding.toolbar.inflateMenu(R.menu.branch_toolbar_menu);

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backToHomeFragment();
            }
        });

        mBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_branch_info:
                       // openBranchInfoFragment();
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

//    private void backToHomeFragment() {
//        getHostActivity()
//                .fragmentManager
//                .popBackStack("branch_details", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//    }
//
//    private void openBranchInfoFragment() {
//        getHostActivity()
//                .fragmentManager
//                .beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
//                .addToBackStack("branch_info")
//                .add(R.id.home_container, Fragment_branch_info.getInstance(branchUID, branchName))
//                .commit();
//    }


    private void initializeChart() {
        BarDataSet dataSet = new BarDataSet(getChartEntries(), "Total Customers");
        dataSet.setValueFormatter(new MyValueFormatter());
        dataSet.setGradientColor(getResources().getColor(R.color.colorPrimaryLight), getResources().getColor(R.color.old_rose_light));
        ArrayList<IBarDataSet> dataSetArray = new ArrayList<>();
        dataSetArray.add(dataSet);
        BarData barData = new BarData(dataSetArray);
        barData.setValueTextSize(10);
        barData.setValueTextColor(Color.BLACK);


        mBinding.chartView.setData(barData);
        mBinding.chartView.animateY(1500);
        mBinding.chartView.setDescription(null);
        mBinding.chartView.setNoDataText("No data to view");
        mBinding.chartView.setNoDataTextColor(Color.BLACK);
        mBinding.chartView.getAxisRight().setDrawLabels(false);
        mBinding.chartView.setPinchZoom(false);
        mBinding.chartView.setDoubleTapToZoomEnabled(false);
        mBinding.chartView.getXAxis().setDrawGridLines(false);
        mBinding.chartView.invalidate();
    }

    private ArrayList<BarEntry> getChartEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 1));
        entries.add(new BarEntry(2, 2));
        entries.add(new BarEntry(4, 6));
        entries.add(new BarEntry(5, 9));
        entries.add(new BarEntry(7, 8));
        entries.add(new BarEntry(10, 2));
        entries.add(new BarEntry(15, 6));
        return entries;
    }

    private static class MyValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }


}
