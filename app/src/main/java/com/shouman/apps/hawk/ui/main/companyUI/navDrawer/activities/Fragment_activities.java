package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.activities;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.ActivitiesRecyclerViewAdapter;
import com.shouman.apps.hawk.adapters.DaysRecyclerViewAdapter;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.DayActivity;
import com.shouman.apps.hawk.databinding.FragmentActivitiesBinding;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_activities extends Fragment implements DaysRecyclerViewAdapter.OnDayItemClickListener {
    private static final String TAG = "Fragment_activities";
    private FragmentActivitiesBinding mBinding;
    private ActivitiesViewModel activitiesViewModel;
    private DaysRecyclerViewAdapter daysAdapter;
    private ActivitiesRecyclerViewAdapter activitiesAdapter;
    private List<Long> mDaysList;
    private Map<Long, List<DayActivity>> mDayActivitiesMap;

    public Fragment_activities() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        initAdapters();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentActivitiesBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecViews();

        activitiesViewModel.getActivitiesMediatorLiveData().observe(getViewLifecycleOwner(), activitiesMap -> {
            Log.e(TAG, "onViewCreated: " + activitiesMap.size());
            mDayActivitiesMap = activitiesMap;
            mDaysList = new ArrayList<>(mDayActivitiesMap.keySet());
            Collections.sort(mDaysList, Collections.reverseOrder());
            daysAdapter.setDaysList(mDaysList);

            activitiesAdapter.setActivitiesList(mDayActivitiesMap.get(daysAdapter.getSelectedItem()));
            getActivitiesCount(mDayActivitiesMap.get(daysAdapter.getSelectedItem()));

            mBinding.progressFrame.setVisibility(View.GONE);
            mBinding.infoFrame.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_down));
        });
    }

    @Override
    public void onDayItemClick() {
        activitiesAdapter.setActivitiesList(mDayActivitiesMap.get(daysAdapter.getSelectedItem()));
        getActivitiesCount(mDayActivitiesMap.get(daysAdapter.getSelectedItem()));
    }

    private void getActivitiesCount(List<DayActivity> activities) {
        final int[] customerCount = {0};
        final int[] visitsCount = {0};
        final int[] salesCounts = {0};
        if (activities == null || activities.size() == 0) return;
        AppExecutors.getsInstance().getDiskIO().execute(() -> {
            for (DayActivity activity : activities) {
                String activityType = activity.getActivityType();
                switch (activityType) {
                    case Common.ACTIVITY_NEW_CUSTOMER:
                        customerCount[0]++;
                        break;
                    case Common.ACTIVITY_NEW_VISIT:
                        visitsCount[0]++;
                        break;
                    case Common.ACTIVITY_NEW_SALESMAN:
                        salesCounts[0]++;
                        break;
                }
            }
            AppExecutors.getsInstance().getMainThread().execute(() -> {
                mBinding.newCustomersCountTxt.setText(String.valueOf(customerCount[0]));
                mBinding.newCustomer.setText(
                        customerCount[0] >= 2 ? getString(R.string.customers) : getString(R.string.customer)

                );
                mBinding.newVisitsCountTxt.setText(String.valueOf(visitsCount[0]));
                mBinding.newVisit.setText(
                        visitsCount[0] >= 2 ? getString(R.string.visits) : getString(R.string.visit)
                );

                mBinding.newSalesmenCountTxt.setText(String.valueOf(salesCounts[0]));
                mBinding.newSalesmen.setText(
                        salesCounts[0] >= 2 ? getString(R.string.salesmen) : getString(R.string.salesman)
                );
            });
        });
    }

    private void initRecViews() {
        mBinding.daysRecView.setAdapter(daysAdapter);
        mBinding.daysRecView.setHasFixedSize(true);

        mBinding.activitiesRecView.setAdapter(activitiesAdapter);
        mBinding.activitiesRecView.setHasFixedSize(true);
    }

    private void initAdapters() {
        daysAdapter = new DaysRecyclerViewAdapter(requireContext(), this);
        activitiesAdapter = new ActivitiesRecyclerViewAdapter(requireContext());
    }

    private void initViewModel() {
        activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);
    }
}
