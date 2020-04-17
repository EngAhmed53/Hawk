package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.VisitLogListItemBinding;
import com.shouman.apps.hawk.data.model.Visit;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class VisitsRecyclerViewAdapter extends RecyclerView.Adapter<VisitsRecyclerViewAdapter.VisitViewHolder> {


    private List<Visit> visitsList;
    private Context mContext;

    public VisitsRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setVisitsLog(List<Visit> visitList) {
        this.visitsList = visitList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        VisitLogListItemBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.visit_log_list_item, parent, false);
        return new VisitViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull VisitViewHolder holder, int position) {
        Visit visit = visitsList.get(position);
        String visitTime = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(new Date(visit.getVisitTime()));
        holder.mBinding.txtVisitDateTime.setText(visitTime);
        holder.mBinding.txtVisitNotes.setText(visit.getVisitNote());
    }

    @Override
    public int getItemCount() {
        if (visitsList != null) {
            return visitsList.size();
        }
        return 0;
    }


    static class VisitViewHolder extends RecyclerView.ViewHolder {

        VisitLogListItemBinding mBinding;

        VisitViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = DataBindingUtil.bind(itemView);

        }
    }
}
