package com.shouman.apps.hawk.ui.main.companyUi;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.shouman.apps.hawk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_company_notification extends Fragment {

    public static Fragment_company_notification getInstance() {
        return new Fragment_company_notification();
    }

    public Fragment_company_notification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

}
