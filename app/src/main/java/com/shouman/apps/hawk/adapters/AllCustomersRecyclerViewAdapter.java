package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.databinding.CustomersListItemLayoutBinding;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AllCustomersRecyclerViewAdapter extends RecyclerView.Adapter<AllCustomersRecyclerViewAdapter.CustomersViewHolder> implements Filterable {

    private List<Customer> allCustomers;
    private List<Customer> allCustomersFull;
    private Context mContext;
    private DateFormat formatter;
    private Calendar calendar;
    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Customer> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(allCustomersFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Customer customer : allCustomersFull) {
                    String data = customer.getCn() + " " + customer.getN();
                    if (data.toLowerCase().contains(filterPattern)) {
                        filteredList.add(customer);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            allCustomers.clear();
            allCustomers.addAll((List<Customer>) results.values);
            notifyDataSetChanged();
        }
    };

    public AllCustomersRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        formatter = DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.getDefault());
        calendar = Calendar.getInstance();
    }

    public void setCustomersList(List<Customer> customersList) {
        this.allCustomers = customersList;
        this.allCustomersFull = new ArrayList<>(customersList);
        notifyDataSetChanged();
    }

    public void sortByNameAscendingOrder () {
        if (allCustomers != null && allCustomers.size() > 0) {
            Collections.sort(allCustomers, (o1, o2) -> o1.getN().compareTo(o2.getN()));
        }
        notifyDataSetChanged();
    }

    public void sortByNameDescendingOrder () {
        if (allCustomers != null && allCustomers.size() > 0) {
            Collections.sort(allCustomers, (o1, o2) -> o2.getN().compareTo(o1.getN()));
        }
        notifyDataSetChanged();
    }

    public void sortByDateAscendingOrder () {
        if (allCustomers != null && allCustomers.size() > 0) {
            Collections.sort(allCustomers, (o1, o2) -> (int) (o1.getAddedTime() - o2.getAddedTime()));
        }
        notifyDataSetChanged();
    }

    public void sortByDateDescendingOrder () {
        if (allCustomers != null && allCustomers.size() > 0) {
            Collections.sort(allCustomers, (o1, o2) -> (int) (o2.getAddedTime() - o1.getAddedTime()));
        }
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
        Customer customer = allCustomers.get(position);
        holder.mBinding.customerNameTxt.setText(customer.getN());
        holder.mBinding.companyNameTxt.setText(customer.getCn());
        String visitsCount = customer.getVisitList().size() + " " + mContext.getString(R.string.customer_total_visits);
        holder.mBinding.totalVisitsTxt.setText(visitsCount);

        calendar.setTimeInMillis(customer.getAddedTime());
        holder.mBinding.addedDateTxt.setText(formatter.format(calendar.getTime()));

        holder.mBinding.frame.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Common.getRandomColor(position))));
    }


    @Override
    public int getItemCount() {
        if (allCustomers != null) {
            return allCustomers.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }


    static class CustomersViewHolder extends RecyclerView.ViewHolder {

        CustomersListItemLayoutBinding mBinding;

        CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
