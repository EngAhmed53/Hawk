package com.shouman.apps.hawk.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.Customer;
import com.shouman.apps.hawk.databinding.CustomersListItemLayoutBySalesBinding;
import com.shouman.apps.hawk.ui.main.companyUI.salesman.salesCustomers.Fragment_salesman_customersDirections;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CustomersBySalesmanRecyclerViewAdapter extends RecyclerView.Adapter<CustomersBySalesmanRecyclerViewAdapter.CustomersViewHolder> implements Filterable {

    public ObservableArrayList<String> selectedUIDs;
    private ArrayList<Integer> selectedPositions;
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

    public CustomersBySalesmanRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        formatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
        calendar = Calendar.getInstance();
        selectedUIDs = new ObservableArrayList<>();
        selectedPositions = new ArrayList<>();
    }

    public void setCustomersList(List<Customer> customersList) {
        this.allCustomers = customersList;
        AppExecutors.getsInstance().getDiskIO().execute(() -> this.allCustomersFull = new ArrayList<>(customersList));
        notifyDataSetChanged();
    }

    public void sortByNameAscendingOrder() {
        if (allCustomers != null && allCustomers.size() > 0) {
            Collections.sort(allCustomers, (o1, o2) -> o1.getN().compareTo(o2.getN()));
        }
        notifyDataSetChanged();
    }

    public void sortByNameDescendingOrder() {
        if (allCustomers != null && allCustomers.size() > 0) {
            Collections.sort(allCustomers, (o1, o2) -> o2.getN().compareTo(o1.getN()));
        }
        notifyDataSetChanged();
    }

    public void sortByDateAscendingOrder() {
        if (allCustomers != null && allCustomers.size() > 0) {
            Collections.sort(allCustomers, (o1, o2) -> (int) (o1.getAddedTime() - o2.getAddedTime()));
        }
        notifyDataSetChanged();
    }

    public void sortByDateDescendingOrder() {
        if (allCustomers != null && allCustomers.size() > 0) {
            Collections.sort(allCustomers, (o1, o2) -> (int) (o2.getAddedTime() - o1.getAddedTime()));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CustomersListItemLayoutBySalesBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.customers_list_item_layout_by_sales, parent, false);
        return new CustomersViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CustomersViewHolder holder, int position) {
        Customer customer = allCustomers.get(position);
        holder.mBinding.customerNameTxt.setText(customer.getN());
        holder.mBinding.companyNameTxt.setText(customer.getCn());
        String visitsCount = customer.getVisitList().size() + " " + mContext.getString(R.string.customer_total_visits);
        holder.mBinding.totalVisitsTxt.setText(visitsCount);

        if (customer.isChecked()) {
            holder.mBinding.customerImage.setAlpha(0f);
            holder.mBinding.checkMark.setAlpha(1f);
        } else {
            holder.mBinding.customerImage.setAlpha(1f);
            holder.mBinding.checkMark.setAlpha(0f);
        }

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

    public void clearSelections() {
        selectedUIDs.clear();
        for (int position : selectedPositions) {
            allCustomers.get(position).setChecked(false);
            notifyItemChanged(position);
        }
        selectedPositions.clear();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }


    class CustomersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        CustomersListItemLayoutBySalesBinding mBinding;
        Customer customer;

        CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            customer = allCustomers.get(getAdapterPosition());
            if (selectedUIDs.size() == 0) {
                Customer customer = allCustomers.get(getAdapterPosition());
                NavDirections toCustomersDetails =
                        Fragment_salesman_customersDirections.actionFragmentSalesmanCustomersToFragmentCustomersInfo(
                                customer, customer.getUid());
                Navigation.findNavController(v).navigate(toCustomersDetails);
            } else {
                if (customer.isChecked()) {
                    customer.setChecked(false);
                    selectedUIDs.remove(customer.getUid());
                    selectedPositions.remove((Integer) getAdapterPosition());
                    hideCheckMark();
                } else {
                    customer.setChecked(true);
                    selectedUIDs.add(customer.getUid());
                    selectedPositions.add(getAdapterPosition());
                    showCheckMark();
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            customer = allCustomers.get(getAdapterPosition());
            if (customer.isChecked()) {
                customer.setChecked(false);
                selectedUIDs.remove(customer.getUid());
                selectedPositions.remove((Integer) getAdapterPosition());
                hideCheckMark();
            } else {
                customer.setChecked(true);
                selectedUIDs.add(customer.getUid());
                selectedPositions.add(getAdapterPosition());
                showCheckMark();
            }
            return true;
        }

        private void hideCheckMark() {
            ObjectAnimator fadeOutCheckMark = ObjectAnimator.ofFloat(mBinding.checkMark, "alpha", 1f, 0f);
            fadeOutCheckMark.setDuration(300);
            fadeOutCheckMark.setInterpolator(new LinearInterpolator());
            fadeOutCheckMark.start();

            ObjectAnimator rotateCheckMark = ObjectAnimator.ofFloat(mBinding.checkMark, "rotationY", 180f, 0f);
            rotateCheckMark.setDuration(300);
            rotateCheckMark.setInterpolator(new LinearInterpolator());
            rotateCheckMark.start();

            ObjectAnimator fadeIn2Letters = ObjectAnimator.ofFloat(mBinding.customerImage, "alpha", 0f, 1f);
            fadeIn2Letters.setDuration(300);
            fadeIn2Letters.setInterpolator(new LinearInterpolator());
            fadeIn2Letters.start();
        }

        private void showCheckMark() {
            ObjectAnimator fadeInCheckMark = ObjectAnimator.ofFloat(mBinding.checkMark, "alpha", 0f, 1f);
            fadeInCheckMark.setDuration(300);
            fadeInCheckMark.setInterpolator(new LinearInterpolator());
            fadeInCheckMark.start();

            ObjectAnimator rotateCheckMark = ObjectAnimator.ofFloat(mBinding.checkMark, "rotationY", 0.0f, 180f);
            rotateCheckMark.setDuration(300);
            rotateCheckMark.setInterpolator(new LinearInterpolator());
            rotateCheckMark.start();

            ObjectAnimator fadeOut2Letters = ObjectAnimator.ofFloat(mBinding.customerImage, "alpha", 1f, 0f);
            fadeOut2Letters.setDuration(300);
            fadeOut2Letters.setInterpolator(new LinearInterpolator());
            fadeOut2Letters.start();
        }
    }
}
