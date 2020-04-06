package com.shouman.apps.hawk.dataBindingAdapters;

import android.util.Log;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.adapters.AllCustomersRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.BranchRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.CustomersRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.DaysRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.SalesRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.VisitsRecyclerViewAdapter;
import com.shouman.apps.hawk.model.CustomersLogDataEntry;
import com.shouman.apps.hawk.model.Visit;

import java.util.List;
import java.util.Map;

public class RecyclerViewDataBinding {
    private static final String TAG = "RecyclerViewDataBinding";

    @BindingAdapter("setBranchesMap")
    public static void setRecyclerViewMap(RecyclerView view, Map<String, String> branches) {

        if (branches == null) {
            Log.e(TAG, "setB: " + "branches list is null");
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
            view.setHasFixedSize(true);
        }
        if (view.getAdapter() == null) {
            BranchRecyclerViewAdapter adapter = new BranchRecyclerViewAdapter(view.getContext());
            adapter.setBranchesMap(branches);
            view.setAdapter(adapter);
        } else {
            ((BranchRecyclerViewAdapter) view.getAdapter()).setBranchesMap(branches);
        }
    }

    @BindingAdapter("setSalesMap")
    public static void setSalesRecyclerViewMap(RecyclerView view, Map<String, String> salesList) {

        if (salesList == null) {
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            view.setHasFixedSize(true);
        }
        if (view.getAdapter() == null) {
            SalesRecyclerViewAdapter adapter = new SalesRecyclerViewAdapter(view.getContext());
            adapter.setSalesMap(salesList);
            view.setAdapter(adapter);
        } else {
            ((SalesRecyclerViewAdapter) view.getAdapter()).setSalesMap(salesList);
        }
    }


    @BindingAdapter("setDaysMap")
    public static void setDaysCustomersMap(RecyclerView view, Map<String, Map<String, CustomersLogDataEntry>> days_customers_map) {

        if (days_customers_map == null) {
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            view.setHasFixedSize(true);
        }
        if (view.getAdapter() == null) {
            DaysRecyclerViewAdapter adapter = new DaysRecyclerViewAdapter(view.getContext());
            adapter.setDate_customersMap(days_customers_map);
            view.setAdapter(adapter);
        } else {
            ((DaysRecyclerViewAdapter) view.getAdapter()).setDate_customersMap(days_customers_map);

        }
    }

    @BindingAdapter("setCustomersMap")
    public static void setCustomersRecyclerViewMap(RecyclerView view, Map<String, CustomersLogDataEntry> customersMap) {
        if (customersMap == null) {
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            view.setHasFixedSize(true);
        }
        if (view.getAdapter() == null) {
            CustomersRecyclerViewAdapter adapter = new CustomersRecyclerViewAdapter(view.getContext());
            adapter.setCustomersMap(customersMap);
            view.setAdapter(adapter);
        } else {
            ((CustomersRecyclerViewAdapter) view.getAdapter()).setCustomersMap(customersMap);

        }
    }

    @BindingAdapter("setAllCustomersMap")
    public static void setAllCustomersRecyclerViewMap(RecyclerView view, Map<String, String> customersMap) {

        if (customersMap == null) {
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            view.setHasFixedSize(true);
        }
        if (view.getAdapter() == null) {
            AllCustomersRecyclerViewAdapter adapter = new AllCustomersRecyclerViewAdapter(view.getContext());
            adapter.setCustomersMap(customersMap);
            view.setAdapter(adapter);
        } else {
            ((AllCustomersRecyclerViewAdapter) view.getAdapter()).setCustomersMap(customersMap);

        }
    }


    @BindingAdapter("setVisitsLog")
    public static void setVisitsLog(RecyclerView view, List<Visit> visitsLog) {

        if (visitsLog == null) {
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            view.setHasFixedSize(false);
        }
        if (view.getAdapter() == null) {
            VisitsRecyclerViewAdapter adapter = new VisitsRecyclerViewAdapter(view.getContext());
            adapter.setVisitsLog(visitsLog);
            view.setAdapter(adapter);
        } else {
            ((VisitsRecyclerViewAdapter) view.getAdapter()).setVisitsLog(visitsLog);

        }
    }
}
