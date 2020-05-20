package com.shouman.apps.hawk.ui.main.companyUI.salesman.salesMain;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.DaysRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.LogEntriesRecyclerViewAdapter;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.databinding.FragmentSalesMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sales_main extends Fragment implements DaysRecyclerViewAdapter.OnDayItemClickListener {
    private String salesMemberUID;
    private String salesMemberName;
    private LogViewModel logViewModel;
    private FragmentSalesMainBinding mBinding;
    private DaysRecyclerViewAdapter daysAdapter;
    private LogEntriesRecyclerViewAdapter logsAdapter;
    private List<Long> mDaysList;
    private Map<Long, List<DailyLogEntry>> mDayLogMap;


    public Fragment_sales_main() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getArgs();
        initViewModel();
        daysAdapter = new DaysRecyclerViewAdapter(requireContext(), this);
        logsAdapter = new LogEntriesRecyclerViewAdapter(requireContext(), Common.COMPANY_ACCOUNT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSalesMainBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecViews();

        mBinding.salesNameTxt.setText(salesMemberName);

        logViewModel.getDaysLogLiveData().observe(getViewLifecycleOwner(), day_logs_map -> {
            mDayLogMap = day_logs_map;
            mDaysList = new ArrayList<>(mDayLogMap.keySet());
            Collections.sort(mDaysList, Collections.reverseOrder());
            daysAdapter.setDaysList(mDaysList);
            logsAdapter.setLogEntriesList(mDayLogMap.get(daysAdapter.getSelectedItem()));
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.daysRecView.setVisibility(View.VISIBLE);
            mBinding.mainFrame.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onDayItemClick() {
        logsAdapter.setLogEntriesList(mDayLogMap.get(daysAdapter.getSelectedItem()));
    }

    private void initRecViews() {
        mBinding.daysRecView.setAdapter(daysAdapter);
        mBinding.daysRecView.setHasFixedSize(true);

        mBinding.logRecView.setAdapter(logsAdapter);
        mBinding.logRecView.setHasFixedSize(true);
    }

    private void getArgs() {
        assert getArguments() != null;
        salesMemberName = Fragment_sales_mainArgs.fromBundle(getArguments()).getSalesName();
        salesMemberUID = Fragment_sales_mainArgs.fromBundle(getArguments()).getSalesUID();
    }

    private void initViewModel() {
        LogViewModelFactory factory = new LogViewModelFactory(getContext(), salesMemberUID);
        logViewModel = new ViewModelProvider(this, factory).get(LogViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        new MenuInflater(requireContext()).inflate(R.menu.sales_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sales_info:
                openSalesInfoFragment();
                return true;
            case R.id.action_all_customers:
                openSalesAllCustomersFragment();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSalesInfoFragment() {
        NavDirections toSalesInfo = Fragment_sales_mainDirections.actionFragmentSalesMainToFragmentSalesInfo(salesMemberUID);
        Navigation.findNavController(mBinding.salesNameTxt).navigate(toSalesInfo);
    }

    private void openSalesAllCustomersFragment() {
        NavDirections toSalesCustomers = Fragment_sales_mainDirections.actionFragmentSalesMainToFragmentSalesmanCustomers(salesMemberUID);
        Navigation.findNavController(mBinding.salesNameTxt).navigate(toSalesCustomers);
    }

//    private void initializeChart() {
//        LineDataSet dataSet = new LineDataSet(getChartEntries(), "Total Customers");
//        dataSet.setDrawFilled(true);
//        dataSet.setFillDrawable(requireContext().getResources().getDrawable(R.drawable.chart_gradient_fill));
//        ArrayList<ILineDataSet> dataSetArray = new ArrayList<>();
//        dataSetArray.add(dataSet);
//        LineData lineData = new LineData(dataSetArray);
//        lineData.setValueTextSize(10);
//        lineData.setValueTextColor(Color.BLACK);
//        lineData.setValueFormatter(new MyValueFormatter());
//
//
//        //chart description
//        Description chartDescription = new Description();
//        chartDescription.setText(getString(R.string.chart_label));
//        chartDescription.setTextSize(10);
//
//        mBinding.chartView.setData(lineData);
//        mBinding.chartView.setDescription(chartDescription);
//        mBinding.chartView.setNoDataText("No data to view");
//        mBinding.chartView.setNoDataTextColor(Color.BLACK);
//        mBinding.chartView.getAxisRight().setDrawLabels(false);
//        mBinding.chartView.setPinchZoom(false);
//        mBinding.chartView.setDoubleTapToZoomEnabled(false);
//        mBinding.chartView.invalidate();
//    }
//
//    private ArrayList<Entry> getChartEntries() {
//        ArrayList<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(0, 1));
//        entries.add(new Entry(2, 2));
//        entries.add(new Entry(4, 6));
//        entries.add(new Entry(5, 9));
//        entries.add(new Entry(7, 8));
//        entries.add(new Entry(10, 2));
//        entries.add(new Entry(15, 6));
//        return entries;
//    }
//
//    private static class MyValueFormatter extends ValueFormatter {
//        @Override
//        public String getFormattedValue(float value) {
//            return String.valueOf((int) value);
//        }
//    }

}
