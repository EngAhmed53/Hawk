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
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.databinding.LogEntryListItemBinding;
import com.shouman.apps.hawk.ui.main.companyUI.salesman.salesMain.Fragment_sales_mainDirections;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LogEntriesRecyclerViewAdapter extends RecyclerView.Adapter<LogEntriesRecyclerViewAdapter.CustomersViewHolder> {

    private List<DailyLogEntry> logEntries;

    private Context mContext;

    private DateFormat formatter;

    private Calendar calendar;

    private int userType;

    public LogEntriesRecyclerViewAdapter(Context mContext, int userType) {
        this.mContext = mContext;
        formatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        calendar = Calendar.getInstance();
        this.userType = userType;
    }

    public void setLogEntriesList(List<DailyLogEntry> logEntries) {
        this.logEntries = logEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LogEntryListItemBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.log_entry_list_item, parent, false);
        return new CustomersViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CustomersViewHolder holder, int position) {
        DailyLogEntry dailyLogEntry = logEntries.get(position);
        holder.mBinding.customerNameTxt.setText(dailyLogEntry.getCustomerName());
        if (dailyLogEntry.isNewCustomer()) {
            holder.mBinding.statusImage.setImageResource(R.drawable.ic_new_customer);
        } else {
            holder.mBinding.statusImage.setImageResource(R.drawable.ic_redo);
        }
        //holder.mBinding.setCustomerUID(dailyLogEntry.getCUID());

        //set company name
        holder.mBinding.companyNameTxt.setText(dailyLogEntry.getCustomerCompanyName());
        //set the time
        calendar.setTimeInMillis(dailyLogEntry.getTimeMillieSeconds());
        holder.mBinding.timeTxt.setText(formatter.format(calendar.getTime()));
    }

    @Override
    public int getItemCount() {
        if (logEntries != null) {
            return logEntries.size();
        }
        return 0;
    }


    class CustomersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LogEntryListItemBinding mBinding;

        CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (userType == Common.COMPANY_ACCOUNT) {
                NavDirections toCustomerDetails =
                        Fragment_sales_mainDirections.actionFragmentSalesMainToFragmentCustomersInfo(
                                null,
                                logEntries.get(getAdapterPosition()).getCUID()
                        );
                Navigation.findNavController(v).navigate(toCustomerDetails);
            } else if (userType == Common.SALES_ACCOUNT) {
                //
            }
        }
    }
}
