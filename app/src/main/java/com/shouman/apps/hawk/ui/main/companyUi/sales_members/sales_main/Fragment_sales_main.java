package com.shouman.apps.hawk.ui.main.companyUi.sales_members.sales_main;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.databinding.FragmentSalesDetailsBinding;
import com.shouman.apps.hawk.ui.main.companyUi.FragmentContainerActivity;
import com.shouman.apps.hawk.ui.main.companyUi.sales_members.sales_info.Fragment_sales_info;
import com.shouman.apps.hawk.ui.main.salesUI.main.allCustomersPage.AllCustomersActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.shouman.apps.hawk.ui.main.salesUI.main.allCustomersPage.AllCustomersActivity.SALES_UID;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sales_main extends Fragment {

    private static final String SALES_MEMBER_UID = "sales_uid";
    private static final String SALES_MEMBER_NAME = "sales_name";
    private String salesMemberUID;
    private String salesMemberName;
    public FragmentSalesDetailsBinding mBinding;


    public static Fragment_sales_main getInstance() {
        return new Fragment_sales_main();
    }

    public static Fragment_sales_main getInstance(String salesUID, String salesName) {
        Bundle args = new Bundle();
        args.putString(SALES_MEMBER_UID, salesUID);
        args.putString(SALES_MEMBER_NAME, salesName);
        Fragment_sales_main sales_details = Fragment_sales_main.getInstance();
        sales_details.setArguments(args);
        return sales_details;
    }

    public Fragment_sales_main() {
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
        initViewModel();
        toolbarCustomization();
        initializeChart();
        mBinding.recCustomers.setNestedScrollingEnabled(false);

        return mBinding.getRoot();
    }

    private void initViewModel() {
        //setting up the viewModel
        SaleViewModelFactory factory = new SaleViewModelFactory(getContext(), salesMemberUID);
        SalesViewModel salesViewModel = new ViewModelProvider(this, factory).get(SalesViewModel.class);
        salesViewModel.getMediatorSalesLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, List<DailyLogEntry>>>() {
            @Override

            public void onChanged(Map<String, List<DailyLogEntry>> date_logEntries_map) {
                mBinding.setDatesLogEntriesMap(date_logEntries_map);
            }
        });
    }


    private void toolbarCustomization() {
        mBinding.toolbar.setTitle(salesMemberName);
        mBinding.toolbar.inflateMenu(R.menu.sales_toolbar_menu);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPrevious();
            }
        });


        mBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_sales_info:
                        openSalesInfoFragment();
                        return true;
                    case R.id.action_all_customers:
                        openAllCustomersActivity();
                    default:
                        return false;
                }
            }
        });
    }

    private void openAllCustomersActivity() {
        Intent intent = new Intent(getContext(), AllCustomersActivity.class);
        intent.putExtra(SALES_UID, salesMemberUID);
        startActivity(intent);
    }

    private void openSalesInfoFragment() {
        getHostActivity()
                .fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .addToBackStack("sales_info")
                .add(R.id.home_container, Fragment_sales_info.getInstance(salesMemberUID))
                .commit();

    }

    private void backToPrevious() {
        getHostActivity()
                .fragmentManager
                .popBackStack("sales_details", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private FragmentContainerActivity getHostActivity() {
        return (FragmentContainerActivity) getActivity();
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
