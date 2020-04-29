package com.shouman.apps.hawk.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.SalesListItem;
import com.shouman.apps.hawk.databinding.SalesListItemLayoutBinding;
import com.shouman.apps.hawk.ui.main.companyUi.IMainClickHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalesRecyclerViewAdapter extends RecyclerView.Adapter<SalesRecyclerViewAdapter.SalesViewHolder> {

    private Map<String, SalesListItem> salesMap;
    private List<SalesListItem> salesListItem;
    private List<String> salesUIDs;
    private Context mContext;
    private IMainClickHandler iMainClickHandler;
    private String mBranchDetails;

    public SalesRecyclerViewAdapter(Context mContext, String branchDetails) {
        this.mContext = mContext;
        iMainClickHandler = (IMainClickHandler) mContext;
        this.mBranchDetails = branchDetails;
    }

    public void setSalesMap(Map<String, SalesListItem> salesMap) {
        this.salesMap = salesMap;
        this.salesListItem = new ArrayList<>();
        this.salesListItem.addAll(salesMap.values());
        this.salesUIDs = new ArrayList<>();
        this.salesUIDs.addAll(salesMap.keySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SalesListItemLayoutBinding mBinding =
                DataBindingUtil
                        .inflate(LayoutInflater.from(mContext), R.layout.sales_list_item_layout, parent, false);
        return new SalesViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final SalesViewHolder holder, final int position) {
        SalesListItem salesItem = salesListItem.get(position);
        holder.mBinding.salesNameTxt.setText(salesItem.getName());
        //set on clickHandler
        String salesUID = salesUIDs.get(position);
        holder.mBinding.setSalesUID(salesUID);
        holder.mBinding.setSalesName(salesItem.getName());
        //set the 2 letters
        setThe2Letters(holder, position, salesItem.getName());
        //set the status
        if (salesItem.isStatus()) {
            holder.mBinding.imgStatus.setImageResource(R.drawable.light_blue_circle);
            holder.mBinding.txtStatus.setText(mContext.getString(R.string.status_enabled));
            holder.statusEnabled = true;
        } else {
            holder.mBinding.imgStatus.setImageResource(R.drawable.red_circle);
            holder.mBinding.txtStatus.setText(mContext.getString(R.string.status_disabled));
            holder.statusEnabled = false;
        }
    }

    private void setThe2Letters(@NonNull SalesViewHolder holder, int position, String salesName) {
        char c1 = salesName.charAt(0);
        Character c2 = null;
        int spaceIndex = salesName.indexOf(" ");
        if (spaceIndex != -1 && salesName.length() > spaceIndex) {
            for (int i = spaceIndex + 1; i < salesName.length(); i++) {
                if (salesName.charAt(i) != ' ') {
                    c2 = salesName.charAt(i);
                    break;
                }
            }
        }
        String s = String.valueOf(c1) + (c2 != null ? c2 : "");
        holder.mBinding.first2Letters.setText(s);
        holder.mBinding.first2Letters.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "varela_round_regular.ttf"));
        holder.mBinding.frame.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Common.getRandomColor(position))));
    }

    @Override
    public int getItemCount() {
        if (salesMap != null) {
            return salesMap.size();
        }
        return 0;
    }

    class SalesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {

        SalesListItemLayoutBinding mBinding;
        boolean statusEnabled;

        SalesViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String salesUID = salesUIDs.get(getAdapterPosition());
            String salesName = salesListItem.get(getAdapterPosition()).getName();
            iMainClickHandler.onSalesItemClickHandler(salesUID, salesName);
        }

        @Override
        public boolean onLongClick(View v) {
            showCheckMark();
            showMenu();
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

            ObjectAnimator fadeIn2Letters = ObjectAnimator.ofFloat(mBinding.first2Letters, "alpha", 0f, 1f);
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

            ObjectAnimator fadeOut2Letters = ObjectAnimator.ofFloat(mBinding.first2Letters, "alpha", 1f, 0f);
            fadeOut2Letters.setDuration(300);
            fadeOut2Letters.setInterpolator(new LinearInterpolator());
            fadeOut2Letters.start();
        }

        private void showMenu() {
            PopupMenu popup = new PopupMenu(mContext, mBinding.layout);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.sales_item_menu);
            popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu menu) {
                    hideCheckMark();
                }
            });
            if (statusEnabled) {
                popup.getMenu().findItem(R.id.action_disable).setVisible(true);
                popup.getMenu().findItem(R.id.action_enable).setVisible(false);
            } else {
                popup.getMenu().findItem(R.id.action_disable).setVisible(false);
                popup.getMenu().findItem(R.id.action_enable).setVisible(true);
            }
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String salesUID = salesUIDs.get(getAdapterPosition());
            String salesName = salesListItem.get(getAdapterPosition()).getName();
            boolean status = salesListItem.get(getAdapterPosition()).isStatus();

            switch (item.getItemId()) {
                case R.id.action_move:
                    iMainClickHandler.onActionMove(salesUID, salesName, status, mBranchDetails);
                    return true;
                case R.id.action_disable:
                    iMainClickHandler.onActionDisable(salesUID, mBranchDetails);
                    return true;
                case R.id.action_enable:
                    iMainClickHandler.onActionEnable(salesUID, mBranchDetails);
                    return true;
                case R.id.action_delete:
                    iMainClickHandler.onActionDelete(salesUID, mBranchDetails);
                    return true;
                default:
                    return false;
            }
        }
    }
}