package com.shouman.apps.hawk.ui.main.companyUI.customers.visitsLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shouman.apps.hawk.databinding.DialogFragmentVisitLogBinding;
import com.shouman.apps.hawk.data.model.Visit;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DialogFragment_Visits_Log extends DialogFragment {

    DialogFragmentVisitLogBinding mBinding;
    private static final String VISITS_KEY = "visits_key";
    private static final String VISITS_VALUES = "visits_value";
    private static final String CUSTOMER_NAME = "customer_name";
    private List<Visit> visitList;
    private List<String> visitsKeyList;
    private String customerName;

    public static DialogFragment_Visits_Log getInstance() {
        return new DialogFragment_Visits_Log();
    }

    public static DialogFragment_Visits_Log getInstance(TreeMap<String, Visit> visitsMap, String customerName) {
        DialogFragment_Visits_Log dialogFragment_visits_log = DialogFragment_Visits_Log.getInstance();

        ArrayList<Visit> visits = new ArrayList<>(visitsMap.values());
        ArrayList<String> visitsKey = new ArrayList<>(visitsMap.keySet());

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(VISITS_VALUES, visits);
        bundle.putStringArrayList(VISITS_KEY, visitsKey);
        bundle.putString(CUSTOMER_NAME, customerName);

        dialogFragment_visits_log.setArguments(bundle);
        return dialogFragment_visits_log;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DialogFragmentVisitLogBinding.inflate(inflater);

        initToolbar();

        Bundle arg = getArguments();

        if (arg != null) {
            visitList = arg.getParcelableArrayList(VISITS_VALUES);
            visitsKeyList = arg.getStringArrayList(VISITS_KEY);
            customerName = arg.getString(CUSTOMER_NAME);
        }

        mBinding.setVisitsLog(visitList);

        return mBinding.getRoot();
    }

    private void initToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getParentFragmentManager().beginTransaction().remove(DialogFragment_Visits_Log.this).commit();
            }
        });
    }
}
