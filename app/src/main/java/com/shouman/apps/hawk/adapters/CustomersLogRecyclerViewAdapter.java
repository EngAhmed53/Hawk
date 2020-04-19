package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.databinding.CustomersListItemLayoutBinding;
import com.shouman.apps.hawk.ui.main.OnCustomerItemClickHandler;
import com.shouman.apps.hawk.ui.main.companyUi.IMainClickHandler;
import com.shouman.apps.hawk.ui.main.salesUI.main.home.IMain2ClickHandler;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CustomersLogRecyclerViewAdapter extends RecyclerView.Adapter<CustomersLogRecyclerViewAdapter.CustomersViewHolder> {

    private List<DailyLogEntry> logEntries;

    private Context mContext;

    private DateFormat formatter;

    private Calendar calendar;

    public CustomersLogRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        formatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH);
        calendar = Calendar.getInstance();
    }

    public void setLogEntriesList(List<DailyLogEntry> logEntries) {
        this.logEntries = logEntries;
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
        DailyLogEntry dailyLogEntry = logEntries.get(position);
        holder.mBinding.customerNameTxt.setText(dailyLogEntry.getCustomerName());
        //show the label if the customer is new and this is not just a visit
        if (dailyLogEntry.isNewCustomer()) {
            holder.mBinding.customerImage.setImageResource(R.drawable.ic_ceo);
        } else {
            holder.mBinding.customerImage.setImageResource(R.drawable.ic_pin);
        }
        String customerUID = dailyLogEntry.getCUID();
        holder.mBinding.setCustomerUID(customerUID);
        holder.mBinding.setCustomerName(dailyLogEntry.getCustomerName());

        //set on clickHandler
        OnCustomerItemClickHandler onCustomerItemClickHandler;

        try {
            //try if the user is company and this is company ui
            onCustomerItemClickHandler = (IMainClickHandler) mContext;

        } catch (ClassCastException e) {
            // catch that the user is sales_member and the ui is sales member ui
            onCustomerItemClickHandler = (IMain2ClickHandler) mContext;

        }
        holder.mBinding.setOnCustomerClickListener(onCustomerItemClickHandler);

        //set company name
        holder.mBinding.companyNameTxt.setText(dailyLogEntry.getCustomerCompanyName());

        //set the time

        calendar.setTimeInMillis(dailyLogEntry.getTimeMillieSeconds());
        holder.mBinding.timeAdded.setText(formatter.format(calendar.getTime()));
    }

    @Override
    public int getItemCount() {
        if (logEntries != null) {
            return logEntries.size();
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
