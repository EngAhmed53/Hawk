package com.shouman.apps.hawk.ui.main.salesMemberUI.home;


import android.content.Intent;
import android.graphics.Color;
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
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentSalesHomeBinding;
import com.shouman.apps.hawk.ui.main.salesMemberUI.newCustomer.AddNewCustomerActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sales_home extends Fragment {
    private static final String TAG = "Fragment_sales_home";
    private static final String USER_UID = "user_uid";
    private String userUID;

    private FragmentSalesHomeBinding mBinding;

    public static Fragment_sales_home getInstance() {
        return new Fragment_sales_home();
    }

    public static Fragment_sales_home getInstance(String userUID) {
        Fragment_sales_home fragment_sales_home = Fragment_sales_home.getInstance();
        Bundle args = new Bundle();
        args.putString(USER_UID, userUID);
        fragment_sales_home.setArguments(args);
        return fragment_sales_home;
    }


    public Fragment_sales_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSalesHomeBinding.inflate(inflater);
        mBinding.recCustomers.setNestedScrollingEnabled(false);

        Bundle args = getArguments();
        if (args != null) {
            userUID = args.getString(USER_UID);
        }

        SalesHomeViewModelFactory factory = new SalesHomeViewModelFactory(getContext(), userUID);
        SalesHomeViewModel salesHomeViewModel = new ViewModelProvider(this, factory).get(SalesHomeViewModel.class);

        salesHomeViewModel.getMediatorSalesLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, Map<String, String>>>() {
            @Override
            public void onChanged(Map<String, Map<String, String>> dates_customers_map) {
                if (dates_customers_map != null) {
                    mBinding.setDatesCustomersMap(dates_customers_map);
                }
            }
        });


        initializeChart();

        mBinding.fabAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewCustomerActivity.class);
                startActivity(intent);
            }
        });
        return mBinding.getRoot();
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
