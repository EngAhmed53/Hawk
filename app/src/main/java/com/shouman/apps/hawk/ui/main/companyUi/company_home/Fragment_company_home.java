package com.shouman.apps.hawk.ui.main.companyUi.company_home;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.shouman.apps.hawk.databinding.FragmentCompanyHomeBinding;
import com.shouman.apps.hawk.ui.main.companyUi.MainActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_company_home extends Fragment {

    private static final String TAG = "Fragment_company_home";
    private FragmentCompanyHomeBinding mBinding;


    public static Fragment_company_home getInstance() {
        return new Fragment_company_home();
    }


    public Fragment_company_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentCompanyHomeBinding.inflate(inflater);

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

        //set up the chart
        initializeChart();

        mBinding.recBranches.setNestedScrollingEnabled(false);
        return mBinding.getRoot();
    }

    private void initToolbar() {
        mBinding.toolbar.inflateMenu(R.menu.company_ui_home_menu);
        mBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        openSearchFragment();
                        return true;
                    case R.id.action_settings:
                        openSettingsFragment();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void openAddNewBranchFragment() {
        MainActivity host = (MainActivity) getActivity();
        if (host != null) {
            host.showAddNewBranchFragment();
        }
    }

    private void initializeChart() {
        BarDataSet dataSet = new BarDataSet(getChartEntries(), "Total Customers");
        dataSet.setGradientColor(getResources().getColor(R.color.colorPrimaryLight), getResources().getColor(R.color.old_rose_light));
        ArrayList<IBarDataSet> dataSetArray = new ArrayList<>();
        dataSetArray.add(dataSet);
        BarData barData = new BarData(dataSetArray);
        barData.setValueTextSize(10);
        barData.setValueTextColor(Color.BLACK);
        barData.setValueFormatter(new MyValueFormatter());


        mBinding.chartView.setData(barData);
        mBinding.chartView.animateY(1500);
        mBinding.chartView.setDescription(null);
        mBinding.chartView.setNoDataText("No data to view");
        mBinding.chartView.setNoDataTextColor(Color.BLACK);
        mBinding.chartView.getAxisRight().setDrawLabels(false);
        mBinding.chartView.setPinchZoom(false);
        mBinding.chartView.setDoubleTapToZoomEnabled(false);
        mBinding.chartView.setDrawGridBackground(false);
        mBinding.chartView.getXAxis().setDrawGridLines(false);
        mBinding.chartView.invalidate();
    }

    private ArrayList<BarEntry> getChartEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 2));
        entries.add(new BarEntry(2, 5));
        entries.add(new BarEntry(4, 8));
        entries.add(new BarEntry(5, 10));
        entries.add(new BarEntry(7, 9));
        entries.add(new BarEntry(10, 12));
        entries.add(new BarEntry(15, 20));
        return entries;
    }


    private class MyValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }

    private void openSettingsFragment() {
        Toast.makeText(getContext(), "Open Settings Fragment", Toast.LENGTH_SHORT).show();
    }

    private void openSearchFragment() {
        Toast.makeText(getContext(), "Open Search Fragment", Toast.LENGTH_SHORT).show();
    }
}
