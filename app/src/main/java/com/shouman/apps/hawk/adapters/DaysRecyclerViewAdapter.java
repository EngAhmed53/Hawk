package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.CalenderListItemBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DaysRecyclerViewAdapter extends RecyclerView.Adapter<DaysRecyclerViewAdapter.DaysViewHolder> {

    private List<Long> daysList;
    private Context mContext;
    private SimpleDateFormat simpleDateFormatForDay;
    private SimpleDateFormat simpleDateFormatForMonth;
    private Calendar calendar;
    private long currentDateMillisecond;
    private int checkedPosition = 0;
    private OnDayItemClickListener onDayItemClickListener;

    public interface OnDayItemClickListener {
        void onDayItemClick();
    }


    public DaysRecyclerViewAdapter(Context context, OnDayItemClickListener onDayItemClickListener) {
        this.mContext = context;
        this.onDayItemClickListener = onDayItemClickListener;
        simpleDateFormatForMonth = new SimpleDateFormat("MMM", Locale.getDefault());
        simpleDateFormatForDay = new SimpleDateFormat("E", Locale.getDefault());
        calendar = Calendar.getInstance(Locale.getDefault());
        currentDateMillisecond = Common.getCurrentDateWithoutTime().getTime();
    }

    public void setDaysList(List<Long> daysList) {
        this.daysList = daysList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CalenderListItemBinding mBinding =
                DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.calender_list_item, parent, false);
        return new DaysViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull DaysViewHolder holder, int position) {
        long dateMillisecond = daysList.get(position);
        calendar.setTimeInMillis(dateMillisecond);
        String monthText;
        if (dateMillisecond == currentDateMillisecond) {
            monthText = mContext.getString(R.string.today);
        } else {
            long seconds = Math.abs(currentDateMillisecond - dateMillisecond) / 1000;
            if (seconds > 86400 && seconds < 172800)
                monthText = mContext.getString(R.string.yesterday);
            else {
                monthText = simpleDateFormatForMonth.format(calendar.getTime());
            }
        }
        holder.mBinding.monthTxt.setText(monthText);
        holder.mBinding.dayNumTxt.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        holder.mBinding.dayTxt.setText(simpleDateFormatForDay.format(calendar.getTime()));
        holder.setSelected();
    }

    @Override
    public int getItemCount() {
        if (daysList != null) return daysList.size();
        return 0;
    }

    public long getSelectedItem() {
        if (daysList.size() > checkedPosition) return daysList.get(checkedPosition);
        else return 0L;
    }

    class DaysViewHolder extends RecyclerView.ViewHolder {
        CalenderListItemBinding mBinding;

        DaysViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);

            itemView.setOnClickListener(v -> {
                mBinding.monthTxt.setTypeface(null, Typeface.BOLD);
                mBinding.dayFrame.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorAccent)));

                if (checkedPosition != getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                    onDayItemClickListener.onDayItemClick();
                }
            });
        }

        private void setSelected() {
            if (checkedPosition == getAdapterPosition()) {
                mBinding.monthTxt.setTypeface(null, Typeface.BOLD);
                mBinding.dayFrame.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorAccent)));
            } else {
                mBinding.monthTxt.setTypeface(null, Typeface.NORMAL);
                mBinding.dayFrame.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.gray2)));
            }
        }
    }
}
