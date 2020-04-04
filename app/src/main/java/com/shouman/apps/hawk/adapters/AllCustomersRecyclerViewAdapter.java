package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.CustomersListItemLayoutBinding;
import com.shouman.apps.hawk.ui.main.OnCustomerItemClickHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllCustomersRecyclerViewAdapter extends RecyclerView.Adapter<AllCustomersRecyclerViewAdapter.CustomersViewHolder> implements Filterable {

    private Map<String, String> customersMap;
    private Map<String, String> customersMapFull;
    private List<String> customersData;
    private List<String> customersUIDs;
    private Context mContext;
    private Filter mapFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Map<String, String> filteredMap = new HashMap<>();
            if (constraint == null || constraint.length() == 0) {
                filteredMap.putAll(customersMapFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (String key : customersMapFull.keySet()) {
                    String data = customersMapFull.get(key);
                    if (data != null && data.toLowerCase().contains(filterPattern)) {
                        filteredMap.put(key, data);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredMap;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            customersMap.clear();
            customersMap.putAll((Map<String, String>) results.values);
            customersData = new ArrayList<>();
            customersData.addAll(customersMap.values());
            customersUIDs = new ArrayList<>();
            customersUIDs.addAll(customersMap.keySet());
            notifyDataSetChanged();
        }
    };

    public AllCustomersRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setCustomersMap(Map<String, String> customersMap) {
        this.customersMap = customersMap;
        this.customersMapFull = new HashMap<>(customersMap);
        this.customersData = new ArrayList<>();
        this.customersData.addAll(customersMap.values());
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
        String[] customerData = customersData.get(position).split(", ");
        holder.mBinding.customerNameTxt.setText(customerData[0]);
        //set on clickHandler

        String customerUID = customersUIDs.get(position);
        holder.mBinding.setCustomerUID(customerUID);
        holder.mBinding.setCustomerName(customerData[0]);

        OnCustomerItemClickHandler onCustomerItemClickHandler;

        onCustomerItemClickHandler = (OnCustomerItemClickHandler) mContext;

        holder.mBinding.setOnCustomerClickListener(onCustomerItemClickHandler);

        //set the 2 letters
        //setThe2Letters(holder, customerData[0]);

        //set company name
        holder.mBinding.companyNameTxt.setText(customerData[1]);
        holder.mBinding.timeAdded.setVisibility(View.INVISIBLE);
        holder.mBinding.imgNewLabel.setVisibility(View.INVISIBLE);
    }

//    private void setThe2Letters(@NonNull CustomersViewHolder holder, String customerName) {
//        char c1 = customerName.charAt(0);
//        Character c2 = null;
//        int spaceIndex = customerName.lastIndexOf(" ");
//        if (spaceIndex != -1 && customerName.length() > spaceIndex) {
//            for (int i = spaceIndex + 1; i < customerName.length(); i++) {
//                if (customerName.charAt(i) != ' ') {
//                    c2 = customerName.charAt(i);
//                    break;
//                }
//            }
//        }
//        String s = String.valueOf(c1) + (c2 != null ? c2 : "");
//        holder.mBinding.first2Letters.setText(s);
//        holder.mBinding.first2Letters.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "varela_round_regular.ttf"));
 //   }

    @Override
    public int getItemCount() {
        if (customersMap != null) {
            return customersMap.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return mapFilter;
    }


    static class CustomersViewHolder extends RecyclerView.ViewHolder {

        CustomersListItemLayoutBinding mBinding;

        CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
