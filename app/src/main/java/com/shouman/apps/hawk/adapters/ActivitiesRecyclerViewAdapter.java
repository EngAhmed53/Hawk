package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.DayActivity;
import com.shouman.apps.hawk.databinding.ActivityListItemLayoutBinding;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivitiesRecyclerViewAdapter extends RecyclerView.Adapter<ActivitiesRecyclerViewAdapter.ActivityViewHolder> {

    private List<DayActivity> activities;

    private Context mContext;

    private DateFormat formatter;

    private Calendar calendar;

    public ActivitiesRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        formatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        calendar = Calendar.getInstance();
    }

    public void setActivitiesList(List<DayActivity> activities) {
        this.activities = activities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ActivityListItemLayoutBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.activity_list_item_layout, parent, false);
        return new ActivityViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        DayActivity activity = activities.get(position);
        String activityType = activity.getActivityType();
        String salesmanName;
        String customerName;
        String body;

        switch (activityType) {
            case Common.ACTIVITY_NEW_CUSTOMER:

                salesmanName = activity.getSalesName();
                customerName = activity.getCustomerName();

                holder.mBinding.typeCardView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.material_green)));

                holder.mBinding.activityTypeImg.setImageResource(R.drawable.ic_new_customer);
                holder.mBinding.activityTypeTxt.setText(mContext.getString(R.string.notify_new_customer_title));

                body = salesmanName + mContext.getString(R.string.notify_new_customer_body) + customerName + "\".";
                holder.mBinding.activityBodyTxt.setText(body);

                break;

            case Common.ACTIVITY_NEW_VISIT:

                salesmanName = activity.getSalesName();
                customerName = activity.getCustomerName();

                holder.mBinding.typeCardView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.material_yellow)));

                holder.mBinding.activityTypeImg.setImageResource(R.drawable.ic_redo);
                holder.mBinding.activityTypeTxt.setText(mContext.getString(R.string.notify_new_visit_title));

                body = salesmanName + mContext.getString(R.string.notify_new_visit_body) + customerName + "\".";
                holder.mBinding.activityBodyTxt.setText(body);
                break;

            case Common.ACTIVITY_NEW_SALESMAN:

                salesmanName = activity.getSalesName();

                holder.mBinding.typeCardView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.material_blue)));

                holder.mBinding.activityTypeImg.setImageResource(R.drawable.ic_person_add);
                holder.mBinding.activityTypeTxt.setText(mContext.getString(R.string.notify_new_salesman_title));

                body = salesmanName + mContext.getString(R.string.notify_new_salesman_body);
                holder.mBinding.activityBodyTxt.setText(body);
                break;
        }

        calendar.setTimeInMillis(activity.getActivityTime());
        holder.mBinding.activityTimeTxt.setText(formatter.format(calendar.getTime()));
    }

    @Override
    public int getItemCount() {
        if (activities != null) {
            return activities.size();
        }
        return 0;
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ActivityListItemLayoutBinding mBinding;

        ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
