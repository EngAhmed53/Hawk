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
import com.shouman.apps.hawk.databinding.SalesListItemLayoutBinding;
import com.shouman.apps.hawk.ui.main.companyUi.IMainClickHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalesRecyclerViewAdapter extends RecyclerView.Adapter<SalesRecyclerViewAdapter.SalesViewHolder> {

    private Map<String, String> salesMap;
    private List<String> salesNames;
    private List<String> salesUIDs;
    private Context mContext;

    public SalesRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setSalesMap(Map<String, String> salesMap) {
        this.salesMap = salesMap;
        this.salesNames = new ArrayList<>();
        this.salesNames.addAll(salesMap.values());
        this.salesUIDs = new ArrayList<>();
        this.salesUIDs.addAll(salesMap.keySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SalesListItemLayoutBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.sales_list_item_layout, parent, false);
        return new SalesViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, int position) {
        String salesName = salesNames.get(position);
        holder.mBinding.salesNameTxt.setText(salesName);
        //set on clickHandler
        IMainClickHandler iMainClickHandler = (IMainClickHandler) mContext;
        String salesUID = salesUIDs.get(position);
        holder.mBinding.setSalesUID(salesUID);
        holder.mBinding.setSalesName(salesName);
        holder.mBinding.setIMainClickHandler(iMainClickHandler);

        //set the 2 letters
        setThe2Letters(holder, position, salesName);
    }

    private void setThe2Letters(@NonNull SalesViewHolder holder, int position, String salesName) {
        char c1 = salesName.charAt(0);
        Character c2 = null;
        int spaceIndex = salesName.indexOf(" ");
        if (spaceIndex != -1 && salesName.length() > spaceIndex) {
            for (int i = spaceIndex + 1; i < salesName.length(); i++) {
                if (salesName.charAt(i) != ' ') {
                    c2 = salesName.charAt(i);
                    break;
                }
            }
        }
        String s = String.valueOf(c1) + (c2 != null ? c2 : "");
        holder.mBinding.first2Letters.setText(s);
        holder.mBinding.first2Letters.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "varela_round_regular.ttf"));
        holder.mBinding.first2Letters.setBackgroundColor(Color.parseColor(Common.getRandomColor(position)));
    }

    @Override
    public int getItemCount() {
        if (salesMap != null) {
            return salesMap.size();
        }
        return 0;
    }


    static class SalesViewHolder extends RecyclerView.ViewHolder {

        SalesListItemLayoutBinding mBinding;

        SalesViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = DataBindingUtil.bind(itemView);

        }
    }
}
