package com.shouman.apps.hawk.ui.main.salesUI.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.shouman.apps.hawk.adapters.DaysRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.LogEntriesRecyclerViewAdapter;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.databinding.FragmentSalesHomeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sales_home extends Fragment implements DaysRecyclerViewAdapter.OnDayItemClickListener {

    private FragmentSalesHomeBinding mBinding;
    private SalesHomeViewModel salesHomeViewModel;
    private DaysRecyclerViewAdapter daysAdapter;
    private LogEntriesRecyclerViewAdapter logsAdapter;
    private List<Long> mDaysList;
    private Map<Long, List<DailyLogEntry>> mDayLogMap;

    public Fragment_sales_home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initViewModel();
        daysAdapter = new DaysRecyclerViewAdapter(requireContext(), this);
        logsAdapter = new LogEntriesRecyclerViewAdapter(requireContext(), Common.SALES_ACCOUNT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSalesHomeBinding.inflate(inflater);

        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecViews();

        mBinding.logRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    mBinding.fab.hide();
                } else{
                    mBinding.fab.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mBinding.fab.setOnClickListener(v -> {
            NavDirections toAddDialog = Fragment_sales_homeDirections.actionFragmentSalesHomeToDialogFragmentAddNew();
            Navigation.findNavController(v).navigate(toAddDialog);
        });

        salesHomeViewModel.getDaysLogLiveData().observe(getViewLifecycleOwner(), day_logs_map -> {
            mBinding.progressBar.setVisibility(View.GONE);
            if (day_logs_map.size() > 0) {
                mDayLogMap = day_logs_map;
                mDaysList = new ArrayList<>(mDayLogMap.keySet());
                Collections.sort(mDaysList, Collections.reverseOrder());
                daysAdapter.setDaysList(mDaysList);
                logsAdapter.setLogEntriesList(mDayLogMap.get(daysAdapter.getSelectedItem()));
                mBinding.daysRecView.setVisibility(View.VISIBLE);
            } else {

            }
        });
    }

    private void initRecViews() {
        mBinding.daysRecView.setAdapter(daysAdapter);
        mBinding.daysRecView.setHasFixedSize(true);

        mBinding.logRecView.setAdapter(logsAdapter);
        mBinding.logRecView.setHasFixedSize(true);
    }


    private void initViewModel() {
        salesHomeViewModel = new ViewModelProvider(this).get(SalesHomeViewModel.class);
    }


    @Override
    public void onDayItemClick() {
        logsAdapter.setLogEntriesList(mDayLogMap.get(daysAdapter.getSelectedItem()));
    }
}