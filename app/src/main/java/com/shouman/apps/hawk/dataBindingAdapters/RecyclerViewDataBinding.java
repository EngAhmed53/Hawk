package com.shouman.apps.hawk.dataBindingAdapters;

import android.view.View;
import android.widget.ProgressBar;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.AllCustomersRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.BranchSalesRecyclerAdapter;
import com.shouman.apps.hawk.adapters.CustomersLogRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.DaysRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.SalesRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.VisitsRecyclerViewAdapter;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.data.model.SalesListItem;
import com.shouman.apps.hawk.data.model.Visit;

import java.util.List;
import java.util.Map;

public class RecyclerViewDataBinding {
    private static final String TAG = "RecyclerViewDataBinding";


    @BindingAdapter({"setSalesMap", "branchName"})
    public static void setSalesRecyclerViewMap(RecyclerView view, Map<String, SalesListItem> salesList, String branchDetails) {

        if (salesList == null) {
            return;
        }
        if (view.getLayoutManager() == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            linearLayoutManager.setInitialPrefetchItemCount(10);
            view.setHasFixedSize(true);
            view.setLayoutManager(linearLayoutManager);
        }

        if (view.getAdapter() == null) {
            SalesRecyclerViewAdapter adapter = new SalesRecyclerViewAdapter(view.getContext(), branchDetails);
            adapter.setSalesMap(salesList);
            view.setAdapter(adapter);
        } else {
            ((SalesRecyclerViewAdapter) view.getAdapter()).setSalesMap(salesList);
        }
    }


    @BindingAdapter("setLogMap")
    public static void setLogMap(final RecyclerView view, final Map<String, List<DailyLogEntry>> date_logEntries_map) {
        if (date_logEntries_map == null) {
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            view.setHasFixedSize(true);
        }
        if (view.getAdapter() == null) {

            DaysRecyclerViewAdapter adapter = new DaysRecyclerViewAdapter(view.getContext());
            adapter.setDate_logEntries_map(date_logEntries_map);
            view.setAdapter(adapter);

        } else {

            ((DaysRecyclerViewAdapter) view.getAdapter()).setDate_logEntries_map(date_logEntries_map);
        }
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                ProgressBar progressBar = view.getRootView().findViewById(R.id.progress_bar);
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
        }, 1);
    }

    @BindingAdapter("setLogList")
    public static void setLogList(RecyclerView view, List<DailyLogEntry> logEntries) {
        if (logEntries == null) {
            return;
        }

        view.setHasFixedSize(true);

        if (view.getAdapter() == null) {
            CustomersLogRecyclerViewAdapter adapter = new CustomersLogRecyclerViewAdapter(view.getContext());
            adapter.setLogEntriesList(logEntries);
            view.setAdapter(adapter);
        } else {
            ((CustomersLogRecyclerViewAdapter) view.getAdapter()).setLogEntriesList(logEntries);
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
            //adapter.setCustomersList(customersMap);
            view.setAdapter(adapter);
        } else {
            //((AllCustomersRecyclerViewAdapter) view.getAdapter()).setCustomersList(customersMap);

        }
    }


    @BindingAdapter("setVisitsLog")
    public static void setVisitsLog(RecyclerView view, List<Visit> visitsLog) {

        if (visitsLog == null) {
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            view.setHasFixedSize(true);
        }
        if (view.getAdapter() == null) {
            VisitsRecyclerViewAdapter adapter = new VisitsRecyclerViewAdapter(view.getContext());
            adapter.setVisitsLog(visitsLog);
            view.setAdapter(adapter);
        } else {
            ((VisitsRecyclerViewAdapter) view.getAdapter()).setVisitsLog(visitsLog);

        }
    }

    @BindingAdapter("setBranchesSalesMap")
    public static void setBranchSalesMemberMap(RecyclerView view, Map<String, Map<String, SalesListItem>> branch_salesMember_map) {
        if (branch_salesMember_map == null) {
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            view.setHasFixedSize(true);
        }
        if (view.getAdapter() == null) {
            BranchSalesRecyclerAdapter adapter = new BranchSalesRecyclerAdapter(view.getContext());
            adapter.setBranch_salesMembers_map(branch_salesMember_map);
            view.setAdapter(adapter);
        } else {
            ((BranchSalesRecyclerAdapter) view.getAdapter()).setBranch_salesMembers_map(branch_salesMember_map);
        }
    }
}
