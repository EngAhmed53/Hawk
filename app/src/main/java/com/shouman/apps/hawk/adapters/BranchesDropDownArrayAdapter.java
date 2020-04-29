package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shouman.apps.hawk.R;

import java.util.List;
import java.util.Objects;

public class BranchesDropDownArrayAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> branchesList;

    public BranchesDropDownArrayAdapter(@NonNull Context context, @NonNull List<String> branchesList) {
        super(context, 0, branchesList);
        this.branchesList = branchesList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        if (branchesList != null && branchesList.size() > 0) {

            ((TextView) convertView.findViewById(android.R.id.text1)).setText(branchesList.get(position));
        }
        return convertView;
    }

    public void setBranchesList(List<String> branchesList) {
        this.branchesList = branchesList;
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }
}
