package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.HomeMenuListItemBinding;
import com.shouman.apps.hawk.data.model.MenuItem;
import com.shouman.apps.hawk.ui.main.companyUi.home.OnMenuItemClick;

import java.util.List;

public class HomeMenuRecyclerAdapter extends RecyclerView.Adapter<HomeMenuRecyclerAdapter.HomeViewHolder> {

    private List<MenuItem> menuItems;
    private Context mContext;

    public HomeMenuRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        HomeMenuListItemBinding mBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.home_menu_list_item, parent, false);

        return new HomeViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        String title = menuItems.get(position).getMenuTitle();
        int imageResource = menuItems.get(position).getMenuIcon();
        OnMenuItemClick onMenuItemClick = (OnMenuItemClick) mContext;
        holder.mBinding.setOnMenuItemClick(onMenuItemClick);
        holder.mBinding.setSelectedItem(title);
        holder.mBinding.menuItemTitle.setText(title);
        holder.mBinding.menuItemIcon.setImageResource(imageResource);
    }

    @Override
    public int getItemCount() {
        if (menuItems == null) return 0;
        else return menuItems.size();
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder {
        HomeMenuListItemBinding mBinding;

        HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
