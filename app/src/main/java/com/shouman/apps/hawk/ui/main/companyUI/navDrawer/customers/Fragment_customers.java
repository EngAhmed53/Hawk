package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.customers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.AllCustomersRecyclerViewAdapter;
import com.shouman.apps.hawk.databinding.FragmentCustomersBinding;
import com.shouman.apps.hawk.ui.main.companyUI.navDrawer.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_customers extends Fragment {

    private FragmentCustomersBinding mBinding;
    private AllCustomersViewModel allCustomersViewModel;
    private AllCustomersRecyclerViewAdapter allCustomersAdapter;


    public Fragment_customers() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //transparentTheActionBar();
        initViewModel();
        allCustomersAdapter = new AllCustomersRecyclerViewAdapter(requireContext());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCustomersBinding.inflate(inflater);
        initRecView();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allCustomersViewModel.getMapMediatorLiveData().observe(getViewLifecycleOwner(), allCustomersList -> {
            String header = allCustomersList.size() >= 2 ? allCustomersList.size() + " Customers" : allCustomersList.size() + " Customer";
            mBinding.headerTxt.setText(header);
            allCustomersAdapter.setCustomersList(allCustomersList);
            mBinding.customersRec.setVisibility(View.VISIBLE);
            mBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private void initViewModel() {
        allCustomersViewModel = new ViewModelProvider(this).get(AllCustomersViewModel.class);
    }

    private void initRecView() {
        mBinding.customersRec.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.customersRec.setAdapter(allCustomersAdapter);
        mBinding.customersRec.setHasFixedSize(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        new MenuInflater(requireContext()).inflate(R.menu.all_customers_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        changeSearchViewTextColor(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Filter filter = allCustomersAdapter.getFilter();
                filter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_name_a_z:
                allCustomersAdapter.sortByNameAscendingOrder();
                return true;
            case R.id.action_sort_by_name_z_a:
                allCustomersAdapter.sortByNameDescendingOrder();
                return true;
            case R.id.action_sort_by_date_a_z:
                allCustomersAdapter.sortByDateAscendingOrder();
                return true;
            case R.id.action_sort_by_date_z_a:
                allCustomersAdapter.sortByDateDescendingOrder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

//    private void transparentTheActionBar() {
//        ((MainActivity) requireActivity()).mBinding.toolbar.setBackgroundColor(getResources().getColor(R.color.black_transparent));
//    }

//    private void actionBarNormalColor() {
//        ((MainActivity) requireActivity()).mBinding.toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//    }


    @Override
    public void onDestroy() {
       // actionBarNormalColor();
        super.onDestroy();
    }
}
