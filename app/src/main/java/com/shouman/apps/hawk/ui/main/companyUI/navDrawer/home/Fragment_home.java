package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.model.DayActivity;
import com.shouman.apps.hawk.databinding.FragmentHomeBinding;
import com.shouman.apps.hawk.utils.AppExecutors;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_home extends Fragment {
    private static final String TAG = "Fragment_home";
    private FragmentHomeBinding mBinding;
    private HomeViewModel homeViewModel;
    private TodayActivatesViewModel todayActivatesViewModel;


    public Fragment_home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        todayActivatesViewModel = new ViewModelProvider(this).get(TodayActivatesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentHomeBinding.inflate(inflater);


        mBinding.fabMenu.setOnMenuButtonClickListener(v -> {
            if (mBinding.fabMenu.isOpened()) {
                mBinding.protectionLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                mBinding.protectionLayout.setVisibility(View.GONE);
                mBinding.fabMenu.close(true);

            } else {
                mBinding.protectionLayout.setVisibility(View.VISIBLE);
                mBinding.protectionLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                mBinding.fabMenu.open(true);
            }
        });

        mBinding.fabItemAddSalesman.setOnClickListener(this::navigateToAddSalesman);

        mBinding.fabItemAddBranch.setOnClickListener(this::navigateToAddBranch);

        mBinding.protectionLayout.setOnClickListener(v -> closeFabMenu());

        return mBinding.getRoot();
    }

    private void navigateToAddBranch(View v) {
        Navigation.findNavController(v).navigate(R.id.action_fragment_home_to_fragment_add_new_branch);
        closeFabMenu();
    }

    private void navigateToAddSalesman(View v) {
        Navigation.findNavController(v).navigate(R.id.action_fragment_home_to_fragment_new_salesman);
        closeFabMenu();
    }

    private void closeFabMenu() {
        mBinding.fabMenu.close(true);
        mBinding.protectionLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        mBinding.protectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel.getAllSalesMembersMediatorLiveData().observe(getViewLifecycleOwner(), branch_sales_map ->
                mBinding.setBranchesSalesMap(branch_sales_map));

        todayActivatesViewModel.getTodayActivitiesLiveData().observe(getViewLifecycleOwner(), todayActivitiesList -> {
            if (todayActivitiesList == null || todayActivitiesList.size() == 0) {
                mBinding.customerCardView.setVisibility(View.INVISIBLE);
                mBinding.visitsCardView.setVisibility(View.INVISIBLE);
                mBinding.newSalesmenCardView.setVisibility(View.INVISIBLE);
                mBinding.noActivitiesTxt.setVisibility(View.VISIBLE);
            } else {
                getActivitiesCount(todayActivitiesList);
                mBinding.noActivitiesTxt.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getActivitiesCount(List<DayActivity> activities) {
        final int[] customerCount = {0};
        final int[] visitsCount = {0};
        final int[] salesCounts = {0};
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

}
