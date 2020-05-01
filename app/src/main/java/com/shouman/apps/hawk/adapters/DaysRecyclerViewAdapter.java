package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.databinding.DayListItemLayoutBinding;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class DaysRecyclerViewAdapter extends RecyclerView.Adapter<DaysRecyclerViewAdapter.DaysViewHolder> {

    private TreeMap<String, List<DailyLogEntry>> date_logEntries_TMap;
    private Context mContext;
    private List<String> dates;
    private DateFormat dateFormat;
    private Calendar calendar;
    private long currentDateMillisecond;
    private RecyclerView.RecycledViewPool viewPool;
    private LinearLayoutManager linearLayoutManager;


    public DaysRecyclerViewAdapter(Context context) {
        this.mContext = context;
        this.date_logEntries_TMap = new TreeMap<>(Collections.reverseOrder());
        dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        calendar = Calendar.getInstance(Locale.getDefault());
        currentDateMillisecond = Common.getCurrentDateWithoutTime().getTime();
        viewPool = new RecyclerView.RecycledViewPool();
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setInitialPrefetchItemCount(10);
    }

    public void setDate_logEntries_map(Map<String, List<DailyLogEntry>> date_logEntries_map) {
        this.dates = new ArrayList<>();
        AppExecutors.getsInstance().getDiskIO().execute(() -> {
            date_logEntries_TMap.putAll(date_logEntries_map);
            //get the dates in array list
            dates.addAll(date_logEntries_TMap.keySet());
            //notify the adapter changes
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DayListItemLayoutBinding mBinding =
                DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.day_list_item_layout, parent, false);
        return new DaysViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull DaysViewHolder holder, int position) {
        holder.mBinding.childRecView.setRecycledViewPool(viewPool);
        long dateMillisecond = Long.parseLong(dates.get(position));
        String dateText;
        if (dateMillisecond == currentDateMillisecond) {
            dateText = mContext.getString(R.string.today);
        } else {
            long seconds = Math.abs(currentDateMillisecond - dateMillisecond) / 1000;
            if (seconds > 86400 && seconds < 172800)
                dateText = mContext.getString(R.string.yesterday);
            else {
                calendar.setTimeInMillis(dateMillisecond);
                dateText = dateFormat.format(calendar.getTime());
            }
        }
        holder.mBinding.setDate(dateText);
        holder.mBinding.childRecView.setLayoutManager(linearLayoutManager);
        holder.mBinding.setLogEntriesList(date_logEntries_TMap.get(dates.get(position)));
    }

    @Override
    public int getItemCount() {
        if (date_logEntries_TMap != null) return date_logEntries_TMap.size();
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
