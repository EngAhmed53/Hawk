package com.shouman.apps.hawk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.Customer;

import java.util.List;

public class CustomersDropDownArrayAdapter extends ArrayAdapter<Customer> {

    private Context mContext;
    private List<Customer> customersList;

    public CustomersDropDownArrayAdapter(@NonNull Context context, @NonNull List<Customer> customersList) {
        super(context, 0, customersList);
        this.customersList = customersList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.customer_dropdown_list_item, parent, false);
        }

        if (customersList != null && customersList.size() > 0) {

            Customer customer = getItem(position);

            if (customer != null) {
                String customerName = customer.getN();
                String companyName = customer.getCn();
                String twoLitters = getThe2Letters(customerName);
                ((TextView) convertView.findViewById(R.id.customer_name_txt)).setText(customerName);
                ((TextView) convertView.findViewById(R.id.company_name_txt)).setText(companyName);
                ((TextView) convertView.findViewById(R.id.first_2_letters)).setText(twoLitters);
            }
        }
        return convertView;
    }

    private String getThe2Letters(String customerName) {
        char c1 = customerName.charAt(0);
        Character c2 = null;
        int spaceIndex = customerName.lastIndexOf(" ");
        if (spaceIndex != -1 && customerName.length() > spaceIndex) {
            for (int i = spaceIndex + 1; i < customerName.length(); i++) {
                if (customerName.charAt(i) != ' ') {
                    c2 = customerName.charAt(i);
                    break;
                }
            }
        }
        return String.valueOf(c1) + (c2 != null ? c2 : "");
    }


    public void setCustomersList(List<Customer> customersList) {
        this.customersList = customersList;
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }
}
