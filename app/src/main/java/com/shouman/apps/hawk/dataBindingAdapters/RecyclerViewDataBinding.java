package com.shouman.apps.hawk.dataBindingAdapters;

import android.util.Log;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.adapters.BranchRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.CustomersRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.DaysRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.SalesRecyclerViewAdapter;

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
            Log.e(TAG, "setSalesRecyclerViewMap: salesList is null");
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
    public static void setDaysCustomersMap(RecyclerView view, Map<String, Map<String, String>> days_customers_map) {

        if (days_customers_map == null) {
            Log.e(TAG, "setDaysCustomersMap: days_customersMap is null");
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
    public static void setCustomersRecyclerViewMap(RecyclerView view, Map<String, String> customersList) {

        if (customersList == null) {
            Log.e(TAG, "setSalesRecyclerViewMap: customersList is null");
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            view.setHasFixedSize(true);
        }
        if (view.getAdapter() == null) {
            CustomersRecyclerViewAdapter adapter = new CustomersRecyclerViewAdapter(view.getContext());
            adapter.setCustomersMap(customersList);
            view.setAdapter(adapter);
        } else {
            ((CustomersRecyclerViewAdapter) view.getAdapter()).setCustomersMap(customersList);

        }
    }
}
