package com.shouman.apps.hawk.ui.main.companyUi.sales_members;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.shouman.apps.hawk.databinding.FragmentSalesDetailsBinding;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sales_details extends Fragment {

    public static final String SALES_MEMBER_UID = "sales_uid";
    public static final String SALES_MEMBER_NAME = "sales_name";
    private String salesMemberUID;
    private String salesMemberName;
    public FragmentSalesDetailsBinding mBinding;


    public static Fragment_sales_details getInstance() {
        return new Fragment_sales_details();
    }

    public static Fragment_sales_details getInstance(String salesUID, String salesName) {
        Bundle args = new Bundle();
        args.putString(SALES_MEMBER_UID, salesUID);
        args.putString(SALES_MEMBER_NAME, salesName);
        Fragment_sales_details sales_details = Fragment_sales_details.getInstance();
        sales_details.setArguments(args);
        return sales_details;
    }

    public Fragment_sales_details() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentSalesDetailsBinding.inflate(inflater);
        Bundle arguments = getArguments();
        if (arguments != null) {
            salesMemberUID = arguments.getString(SALES_MEMBER_UID);
            salesMemberName = arguments.getString(SALES_MEMBER_NAME);
        }

        //setting up the viewModel
        SalesDetailsViewModelFactory factory = new SalesDetailsViewModelFactory(salesMemberUID);
        SalesDetailsViewModel salesDetailsViewModel = new ViewModelProvider(this, factory).get(SalesDetailsViewModel.class);
        salesDetailsViewModel.getMediatorSalesLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, Map<String, String>>>() {
            @Override

            public void onChanged(Map<String, Map<String, String>> customersMap) {
                mBinding.setDatesCustomersMap(customersMap);
            }
        });


//        BranchDetailsViewModel branchDetailsViewModel = new ViewModelProvider(this, factory).get(BranchDetailsViewModel.class);
//        branchDetailsViewModel.getMediatorSalesLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
//            @Override
//            public void onChanged(Map<String, String> salesMap) {
//                mBinding.setCustomersMap(salesMap);
//            }
//        });

        toolbarCustomization();
        initializeChart();

        return mBinding.getRoot();
    }


    private void toolbarCustomization() {
        mBinding.toolbar.setTitle(salesMemberName);
        mBinding.collapsingToolbar.setCollapsedTitleTypeface(Typeface.createFromAsset(getContext().getAssets(), "russo_one_regular.ttf"));
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
        dataSet.setFillDrawable(getContext().getResources().getDrawable(R.drawable.chart_gradient_fill));
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
        entries.add(new Entry(0, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(4, 6));
        entries.add(new Entry(5, 9));
        entries.add(new Entry(7, 8));
        entries.add(new Entry(10, 2));
        entries.add(new Entry(15, 6));
        return entries;
    }

    private class MyValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }


}
