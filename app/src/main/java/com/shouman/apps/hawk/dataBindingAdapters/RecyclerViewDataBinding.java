package com.shouman.apps.hawk.dataBindingAdapters;

import android.util.Log;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.adapters.BranchesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewDataBinding {
    private static final String TAG = "RecyclerViewDataBinding";

    @BindingAdapter("setBranchesList")
    public static void setBranchesList(RecyclerView view, List<String> branches) {

        if (branches == null) {
            Log.e(TAG, "setBranchesList: " + "branches list is null");
            return;
        }
        if (view.getLayoutManager() == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
            view.setHasFixedSize(true);
        }
        if (view.getAdapter() == null) {
            BranchesRecyclerAdapter adapter = new BranchesRecyclerAdapter(view.getContext());
            adapter.setBranchesName(branches);
            view.setAdapter(adapter);
        } else {
            ((BranchesRecyclerAdapter) view.getAdapter()).setBranchesName(branches);
        }
    }
}
