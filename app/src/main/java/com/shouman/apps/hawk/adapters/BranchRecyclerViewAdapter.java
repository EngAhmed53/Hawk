package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
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
import com.shouman.apps.hawk.ui.main.companyUi.IMainClickHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BranchRecyclerViewAdapter extends RecyclerView.Adapter<BranchRecyclerViewAdapter.BranchesViewHolder> {

    private Map<String, String> branchesMap;
    private List<String> branchesNames;
    private List<String> branchesUID;
    private Context mContext;

    public BranchRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBranchesMap(Map<String, String> branchesMap) {
        this.branchesMap = branchesMap;
        this.branchesNames = new ArrayList<>();
        this.branchesNames.addAll(branchesMap.values());
        this.branchesUID = new ArrayList<>();
        this.branchesUID.addAll(branchesMap.keySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BranchRecyclerViewAdapter.BranchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BranchesListItemLayoutBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.branches_list_item_layout, parent, false);
        return new BranchesViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BranchRecyclerViewAdapter.BranchesViewHolder holder, int position) {
        String branchName = branchesNames.get(position);
        holder.mBinding.branchNameTxt.setText(branchName);
        //set on clickHandler
        IMainClickHandler iMainClickHandler = (IMainClickHandler) mContext;
        String branchUID = branchesUID.get(position);
        holder.mBinding.setBranchUID(branchUID);
        holder.mBinding.setBranchName(branchName);
        holder.mBinding.setIMainClickHandler(iMainClickHandler);

        //set the 2 letters
        setThe2Letters(holder, position, branchName);
    }

    private void setThe2Letters(@NonNull BranchesViewHolder holder, int position, String branchName) {
        char c1 = branchName.charAt(0);
        Character c2 = null;
        int spaceIndex = branchName.indexOf(" ");
        if (spaceIndex != -1 && branchName.length() > spaceIndex) {
            for (int i = spaceIndex + 1; i < branchName.length(); i++) {
                if (branchName.charAt(i) != ' ') {
                    c2 = branchName.charAt(i);
                    break;
                }
            }
        }
        String s = String.valueOf(c1) + (c2 != null ? c2 : "");
        holder.mBinding.first2Letters.setText(s);
        holder.mBinding.first2Letters.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "varela_round_regular.ttf"));
        holder.mBinding.mtrlCardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Common.getRandomColor(position))));
    }

    @Override
    public int getItemCount() {
        if (branchesMap != null) {
            return branchesMap.size();
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
