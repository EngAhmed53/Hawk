package com.shouman.apps.hawk.ui.main.salesMemberUI.home;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentSalesDetailsBinding;
import com.shouman.apps.hawk.databinding.FragmentSalesHomeBinding;
import com.shouman.apps.hawk.preferences.UserPreference;
import com.shouman.apps.hawk.ui.main.companyUi.sales_members.Fragment_sales_details;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sales_home extends Fragment {
    private static final String TAG = "Fragment_sales_home";

    private FragmentSalesHomeBinding mBinding;
    public static Fragment_sales_home getInstance() {
        return new Fragment_sales_home();
    }


    public Fragment_sales_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSalesHomeBinding.inflate(inflater);
        mBinding.recCustomers.setNestedScrollingEnabled(false);

        String userUID = UserPreference.getUserUID(getContext());
        SalesHomeViewModelFactory factory = new SalesHomeViewModelFactory(userUID);
        Log.e(TAG, "onCreateView: " + userUID );
        SalesHomeViewModel salesHomeViewModel = new ViewModelProvider(this, factory).get(SalesHomeViewModel.class);

        salesHomeViewModel.getMediatorSalesLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, Map<String, String>>>() {
            @Override
            public void onChanged(Map<String, Map<String, String>> dates_customers_map) {
                mBinding.setDatesCustomersMap(dates_customers_map);
            }
        });


        initializeChart();
        return mBinding.getRoot();
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
