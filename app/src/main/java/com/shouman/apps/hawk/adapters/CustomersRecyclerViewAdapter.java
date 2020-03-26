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
import com.shouman.apps.hawk.databinding.CustomersListItemLayoutBinding;
import com.shouman.apps.hawk.ui.main.OnCustomerItemClickHandler;
import com.shouman.apps.hawk.ui.main.companyUi.IMainClickHandler;
import com.shouman.apps.hawk.ui.main.salesMemberUI.home.homeFragment.IMain2ClickHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomersRecyclerViewAdapter extends RecyclerView.Adapter<CustomersRecyclerViewAdapter.CustomersViewHolder> {

    private Map<String, String> customersMap;
    private List<String> customersNames;
    private List<String> customersUIDs;
    private Context mContext;

    public CustomersRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setCustomersMap(Map<String, String> customersMap) {
        this.customersMap = customersMap;
        this.customersNames = new ArrayList<>();
        this.customersNames.addAll(customersMap.values());
        this.customersUIDs = new ArrayList<>();
        this.customersUIDs.addAll(customersMap.keySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CustomersListItemLayoutBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.customers_list_item_layout, parent, false);
        return new CustomersViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CustomersViewHolder holder, int position) {
        String customerName = customersNames.get(position);
        holder.mBinding.customerNameTxt.setText(customerName);
        //set on clickHandler

        String customerUID = customersUIDs.get(position);
        holder.mBinding.setCustomerUID(customerUID);
        holder.mBinding.setCustomerName(customerName);

        OnCustomerItemClickHandler onCustomerItemClickHandler;

        try {
            //try if the user is company and this is company ui
            onCustomerItemClickHandler = (IMainClickHandler) mContext;

        } catch (ClassCastException e) {
            // catch that the user is sales_mamber and the ui is sales member ui
            onCustomerItemClickHandler = (IMain2ClickHandler) mContext;

        }
        holder.mBinding.setOnCustomerClickListener(onCustomerItemClickHandler);

        //set the 2 letters
        setThe2Letters(holder, position, customerName);
    }

    private void setThe2Letters(@NonNull CustomersViewHolder holder, int position, String branchName) {
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
        if (customersMap != null) {
            return customersMap.size();
        }
        return 0;
    }


    public class CustomersViewHolder extends RecyclerView.ViewHolder {

        CustomersListItemLayoutBinding mBinding;

        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
