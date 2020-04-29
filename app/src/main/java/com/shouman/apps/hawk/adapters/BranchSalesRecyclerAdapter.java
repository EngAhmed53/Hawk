package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.SalesListItem;
import com.shouman.apps.hawk.databinding.BranchNameListItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BranchSalesRecyclerAdapter extends RecyclerView.Adapter<BranchSalesRecyclerAdapter.BranchSalesViewHolder> {

    private Context mContext;
    private List<String> branchNames;
    private Map<String, Map<String, SalesListItem>> branch_salesMembers_map;

    public BranchSalesRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    public void setBranch_salesMembers_map(Map<String, Map<String, SalesListItem>> branch_salesMembers_map) {
        this.branch_salesMembers_map = branch_salesMembers_map;
        this.branchNames = new ArrayList<>(branch_salesMembers_map.keySet());
        Log.e("adapter", "setBranch_salesMembers_map: " + branchNames.toString());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BranchSalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BranchNameListItemBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.branch_name_list_item, parent, false);
        return new BranchSalesViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BranchSalesViewHolder holder, int position) {
        String branchDetails = branchNames.get(position);
        String branchName = branchDetails.substring(branchDetails.indexOf(", ") + 2);
        holder.mBinding.branchName.setText(branchName);
        holder.mBinding.setSaleMembers(branch_salesMembers_map.get(branchDetails));
        holder.mBinding.setBranchDetails(branchDetails);
    }

    @Override
    public int getItemCount() {
        if (branch_salesMembers_map != null) return branch_salesMembers_map.size();
        else return 0;
    }

    static class BranchSalesViewHolder extends RecyclerView.ViewHolder {

        BranchNameListItemBinding mBinding;

        BranchSalesViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
