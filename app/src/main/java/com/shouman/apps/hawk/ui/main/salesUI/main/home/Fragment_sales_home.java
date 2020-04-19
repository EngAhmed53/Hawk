package com.shouman.apps.hawk.ui.main.salesUI.main.home;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.databinding.FragmentSalesHomeBinding;
import com.shouman.apps.hawk.ui.main.salesUI.add.AddNewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sales_home extends Fragment {
    private static final String TAG = "Fragment_sales_home";
    public static final int FRAGMENT_NEW_CUSTOMER = 0;
    public static final int FRAGMENT_NEW_VISIT = 1;
    public static final String OPEN_FRAGMENT = "open_fragment";
    private String userUID;

    private FragmentSalesHomeBinding mBinding;


    public Fragment_sales_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSalesHomeBinding.inflate(inflater);
        mBinding.recCustomers.setNestedScrollingEnabled(false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userUID = firebaseUser.getUid();
        }

        initViewModel();

        initializeChart();
        mBinding.fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.fabMenu.isOpened()) {
                    mBinding.protectionLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                    mBinding.protectionLayout.setVisibility(View.GONE);
                    mBinding.fabMenu.close(true);

                } else {
                    mBinding.protectionLayout.setVisibility(View.VISIBLE);
                    mBinding.protectionLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                    mBinding.fabMenu.open(true);
                }
            }
        });

        mBinding.fabItemAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewActivity.class);
                intent.putExtra(OPEN_FRAGMENT, FRAGMENT_NEW_CUSTOMER);
                startActivity(intent);
                closeFabMenu();
            }
        });

        mBinding.fabItemAddVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewActivity.class);
                intent.putExtra(OPEN_FRAGMENT, FRAGMENT_NEW_VISIT);
                startActivity(intent);
                closeFabMenu();
            }
        });

        mBinding.protectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabMenu();
            }
        });

        return mBinding.getRoot();
    }

    private void initViewModel() {
        SalesHomeViewModelFactory factory = new SalesHomeViewModelFactory(getContext(), userUID);
        SalesHomeViewModel salesHomeViewModel = new ViewModelProvider(this, factory).get(SalesHomeViewModel.class);

        salesHomeViewModel.getMediatorSalesLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, List<DailyLogEntry>>>() {
            @Override
            public void onChanged(final Map<String, List<DailyLogEntry>> dates_customers_map) {
                if (dates_customers_map != null) {
                    mBinding.setLogEntriesMap(dates_customers_map);

                    mBinding.progressBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.progressBar.setVisibility(View.INVISIBLE);
                        }
                    }, 800);
                }
            }
        });
    }

    private void closeFabMenu() {
        mBinding.fabMenu.close(true);
        mBinding.protectionLayout.setVisibility(View.GONE);
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

    private static class MyValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }
}
