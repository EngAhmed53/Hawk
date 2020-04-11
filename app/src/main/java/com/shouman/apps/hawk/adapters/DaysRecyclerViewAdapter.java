package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.DayListItemLayoutBinding;
import com.shouman.apps.hawk.model.CustomersLogDataEntry;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class DaysRecyclerViewAdapter extends RecyclerView.Adapter<DaysRecyclerViewAdapter.DaysViewHolder> {

    private TreeMap<String, Map<String, CustomersLogDataEntry>> date_customersTMap;
    private WeakReference<Context> mContext;
    private List<String> dates;
    private List<Map<String, CustomersLogDataEntry>> customersMapsList;

    public DaysRecyclerViewAdapter(Context mContext) {
        this.mContext = new WeakReference<>(mContext);
    }

    public void setDate_customersMap(Map<String, Map<String, CustomersLogDataEntry>> date_customersMap) {

        //sorting the map using treeMap in descending order
        this.date_customersTMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String date1, String date2) {
                int mark1 = date1.indexOf(',');
                int day1Num = Integer.parseInt(date1.substring(4, mark1));

                int mark2 = date2.indexOf(',');
                int day2Num = Integer.parseInt(date2.substring(4, mark2));

                return day2Num - day1Num;
            }
        });
        this.date_customersTMap.putAll(date_customersMap);

        //get the dates in array list
        this.dates = new ArrayList<>();
        this.dates.addAll(date_customersTMap.keySet());

        //get the customers map
        customersMapsList = new ArrayList<>();
        customersMapsList.addAll(date_customersTMap.values());

        //notify the adapter changes
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DayListItemLayoutBinding mBinding =
                DataBindingUtil.inflate(LayoutInflater.from(mContext.get()), R.layout.day_list_item_layout, parent, false);
        return new DaysViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull DaysViewHolder holder, int position) {
        String date = dates.get(position);
        DateFormat format = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        String currentDate = format.format(new Date());
        if (date.equals(currentDate)) {
            date = mContext.get().getString(R.string.today);

        } else {
            Date d1 = null;
            try {
                d1 = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date d2 = new Date();
            long seconds = (d2.getTime() - Objects.requireNonNull(d1).getTime()) / 1000;
            if (seconds > 86400 && seconds < 172800) date = mContext.get().getString(R.string.yesterday);
        }

        holder.mBinding.setDate(date);
        holder.mBinding.setCustomersMap(customersMapsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (date_customersTMap != null) return date_customersTMap.size();
        return 0;
    }

    static class DaysViewHolder extends RecyclerView.ViewHolder {
        DayListItemLayoutBinding mBinding;

        DaysViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
