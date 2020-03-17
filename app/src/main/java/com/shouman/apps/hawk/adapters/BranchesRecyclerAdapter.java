package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.BranchesListItemLayoutBinding;

import java.util.List;

public class BranchesRecyclerAdapter extends RecyclerView.Adapter<BranchesRecyclerAdapter.BranchesViewHolder> {

    private List<String> branchesName;
    private Context mContext;

    public BranchesRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBranchesName(List<String> branchesName) {
        this.branchesName = branchesName;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BranchesRecyclerAdapter.BranchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BranchesListItemLayoutBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.branches_list_item_layout, parent, false);
        return new BranchesViewHolder(mBinding.getRoot());

    }

    @Override
    public void onBindViewHolder(@NonNull BranchesRecyclerAdapter.BranchesViewHolder holder, int position) {
        String branchName = branchesName.get(position);
        holder.mBinding.branchNameTxt.setText(branchName);


        String[] branchArray = branchName.split(" ");
        char c1;
        char c2;
        if (branchArray.length == 1) {
            c1 = branchArray[0].charAt(0);
            if (branchArray[0].length() > 1) {
                c2 = branchArray[0].charAt(1);
                String s = String.valueOf(c1) + c2;
                holder.mBinding.first2Letters.setText(s);
            } else {
                holder.mBinding.first2Letters.setText(String.valueOf(c1));
            }
        } else if (branchArray.length > 1) {
            c1 = branchArray[0].charAt(0);
            c2 = branchArray[1].charAt(0);
            String s = String.valueOf(c1) + c2;
            holder.mBinding.first2Letters.setText(s);
        }
        holder.mBinding.first2Letters.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "varela_round_regular.ttf"));
        holder.mBinding.first2Letters.setBackgroundColor(Color.parseColor(Common.getRandomColor(position)));
    }

    @Override
    public int getItemCount() {
        if (branchesName != null) {
            return branchesName.size();
        }
        return 0;
    }


    public class BranchesViewHolder extends RecyclerView.ViewHolder {

        BranchesListItemLayoutBinding mBinding;

        public BranchesViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = DataBindingUtil.bind(itemView);

        }
    }
}
