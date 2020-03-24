package com.shouman.apps.hawk.ui.main.companyUi.company_home;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.appbar.AppBarLayout;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentCompanyHomeBinding;
import com.shouman.apps.hawk.ui.main.companyUi.MainActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

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

        //appBar and toolbar customization
        toolbarCustomization();
        //set up the chart
        initializeChart();

        mBinding.nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                Log.e(TAG, "onScrollChange: " + scrollY);
                if (scrollY > 0) {
                    mBinding.fabAddNewBranch.hide();
                } else if (scrollY == 0) {
                    mBinding.fabAddNewBranch.show();
                }
            }
        });

        return mBinding.getRoot();
    }

    private void openAddNewBranchFragment() {
        MainActivity host = (MainActivity) getActivity();
        if (host != null) {
            host.showAddNewBranchFragment();
        }
    }

    private void toolbarCustomization() {
        mBinding.collapsingToolbar.setCollapsedTitleTypeface(Typeface.createFromAsset(Objects.requireNonNull(getContext()).getAssets(), "russo_one_regular.ttf"));
        mBinding.collapsingToolbar.setExpandedTitleTypeface(Typeface.createFromAsset(getContext().getAssets(), "russo_one_regular.ttf"));
        mBinding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScrollRange = mBinding.appBar.getTotalScrollRange();
                if (Math.abs(verticalOffset) == maxScrollRange) {
                    mBinding.chartView.setAlpha(0.0f);
                } else if (verticalOffset == 0) {
                    mBinding.chartView.setAlpha(1.0f);
                } else {
                    float alpha = (1 + ((float) verticalOffset / (float) maxScrollRange));
                    mBinding.chartView.setAlpha(alpha);
                }
            }
        });
    }

    private void initializeChart() {
        LineDataSet dataSet = new LineDataSet(getChartEntries(), "Total Customers");
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.chart_gradient_fill));
        ArrayList<ILineDataSet> dataSetArray = new ArrayList<>();
        dataSetArray.add(dataSet);
        LineData lineData = new LineData(dataSetArray);
        lineData.setValueTextSize(10);
        lineData.setValueTextColor(Color.BLACK);
        lineData.setValueFormatter(new MyValueFormatter());


        //chart description
        Description chartDescription = new Description();
        chartDescription.setText(getString(R.string.chart_label));
        chartDescription.setTextSize(10);

        mBinding.chartView.setData(lineData);
        mBinding.chartView.setDescription(chartDescription);
        mBinding.chartView.setNoDataText("No data to view");
        mBinding.chartView.setNoDataTextColor(Color.BLACK);
        mBinding.chartView.getAxisRight().setDrawLabels(false);
        mBinding.chartView.setPinchZoom(false);
        mBinding.chartView.setDoubleTapToZoomEnabled(false);
        mBinding.chartView.invalidate();
    }

    private ArrayList<Entry> getChartEntries() {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 2));
        entries.add(new Entry(2, 5));
        entries.add(new Entry(4, 8));
        entries.add(new Entry(5, 10));
        entries.add(new Entry(7, 9));
        entries.add(new Entry(10, 12));
        entries.add(new Entry(15, 20));
        return entries;
    }


//    private class MyXAxisFormatter extends ValueFormatter {
//        private Timestamp timestamp = Timestamp.valueOf(new Date().toString());
//        private String[] days = getContext().getResources().getStringArray(R.array.dayes_array);
//
//        @Override
//        public String getFormattedValue(float value) {
//            return null;
//        }
//    }


    private class MyValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }

}
