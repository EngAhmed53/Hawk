package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.SalesListItem;
import com.shouman.apps.hawk.databinding.BranchListItemBinding;
import com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home.Fragment_homeDirections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BranchSalesRecyclerAdapter extends RecyclerView.Adapter<BranchSalesRecyclerAdapter.BranchSalesViewHolder> {

    private Context mContext;
    private List<String> branchNames;
    private Map<String, Map<String, SalesListItem>> branch_salesMembers_map;
    private RecyclerView.RecycledViewPool viewPool;

    public BranchSalesRecyclerAdapter(Context context) {
        this.mContext = context;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    public void setBranch_salesMembers_map(Map<String, Map<String, SalesListItem>> branch_salesMembers_map) {
        this.branch_salesMembers_map = branch_salesMembers_map;
        this.branchNames = new ArrayList<>(branch_salesMembers_map.keySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BranchSalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BranchListItemBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.branch_list_item, parent, false);
        return new BranchSalesViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BranchSalesViewHolder holder, int position) {
        String branchDetails = branchNames.get(position);
        String branchName = branchDetails.substring(branchDetails.indexOf(", ") + 2);
        holder.mBinding.branchName.setText(branchName);
        holder.mBinding.childRecView.setRecycledViewPool(viewPool);
        holder.mBinding.setSaleMembers(branch_salesMembers_map.get(branchDetails));
        holder.mBinding.setParentPosition(position);
        holder.mBinding.getRoot().setOnClickListener( v -> {
                    NavDirections toBranchInfo =  Fragment_homeDirections.actionFragmentHomeToFragmentBranchInfo().setBranchName(branchName);
                    Navigation.findNavController(v).navigate(toBranchInfo);
                }
        );
    }

    @Override
    public int getItemCount() {
        if (branch_salesMembers_map != null) return branch_salesMembers_map.size();
        else return 0;
    }

    static class BranchSalesViewHolder extends RecyclerView.ViewHolder {

        BranchListItemBinding mBinding;

        BranchSalesViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
