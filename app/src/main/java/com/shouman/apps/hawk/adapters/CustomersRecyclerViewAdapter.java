package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.CustomersListItemLayoutBinding;
import com.shouman.apps.hawk.model.CustomersLogDataEntry;
import com.shouman.apps.hawk.ui.main.OnCustomerItemClickHandler;
import com.shouman.apps.hawk.ui.main.companyUi.IMainClickHandler;
import com.shouman.apps.hawk.ui.main.salesMemberUI.home.homeFragment.IMain2ClickHandler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomersRecyclerViewAdapter extends RecyclerView.Adapter<CustomersRecyclerViewAdapter.CustomersViewHolder> {

    private Map<String, CustomersLogDataEntry> customersMap;
    private List<CustomersLogDataEntry> customersData;
    private List<String> customersUIDs;
    private Context mContext;

    public CustomersRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setCustomersMap(Map<String, CustomersLogDataEntry> customersMap) {
        this.customersMap = customersMap;
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
        CustomersLogDataEntry customerData = customersData.get(position);
        holder.mBinding.customerNameTxt.setText(customerData.getCustomerName());
        //set on clickHandler

        String customerUID = customersUIDs.get(position);
        holder.mBinding.setCustomerUID(customerUID);
        holder.mBinding.setCustomerName(customerData.getCustomerName());

        OnCustomerItemClickHandler onCustomerItemClickHandler;

        try {
            //try if the user is company and this is company ui
            onCustomerItemClickHandler = (IMainClickHandler) mContext;

        } catch (ClassCastException e) {
            // catch that the user is sales_member and the ui is sales member ui
            onCustomerItemClickHandler = (IMain2ClickHandler) mContext;

        }
        holder.mBinding.setOnCustomerClickListener(onCustomerItemClickHandler);

        //set the 2 letters
        //setThe2Letters(holder, customerData.getCustomerName());

        //set company name
        holder.mBinding.companyNameTxt.setText(customerData.getCustomerCompanyName());

        //set the time
        DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(customerData.getTimeMillieSeconds());
        holder.mBinding.timeAdded.setText(formatter.format(calendar.getTime()));

        //show the label if the customer is new and this is not just a visit
        if (customerData.isNewCustomer()) {
            holder.mBinding.labelView.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            holder.mBinding.imgNewLabel.setVisibility(View.VISIBLE);
            holder.mBinding.customerImage.setImageResource(R.drawable.ic_ceo);
        }
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
   // }

    @Override
    public int getItemCount() {
        if (customersMap != null) {
            return customersMap.size();
        }
        return 0;
    }


    static class CustomersViewHolder extends RecyclerView.ViewHolder {

        CustomersListItemLayoutBinding mBinding;

        CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
