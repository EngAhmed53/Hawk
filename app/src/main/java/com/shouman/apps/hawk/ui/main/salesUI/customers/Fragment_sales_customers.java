package com.shouman.apps.hawk.ui.main.salesUI.customers;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.MyCustomersRecyclerViewAdapter;
import com.shouman.apps.hawk.databinding.FragmentSalesMyCustomersBinding;

public class Fragment_sales_customers extends Fragment {

    private CustomersViewModel mViewModel;
    private FragmentSalesMyCustomersBinding mBinding;
    private MyCustomersRecyclerViewAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initViewModel();
        mAdapter = new MyCustomersRecyclerViewAdapter(requireContext());
    }


    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(CustomersViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSalesMyCustomersBinding.inflate(inflater);
        initRecView();

        mViewModel.getSalesCustomersMediatorLiveData().observe(getViewLifecycleOwner(), customersMap -> {
            mAdapter.setCustomersMap(customersMap);
            String header = customersMap.size() >= 2 ? customersMap.size() + " Customers" : customersMap.size() + " Customer";
            mBinding.headerTxt.setText(header);
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.mainFrame.setVisibility(View.VISIBLE);
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initToolbar();

    }

    private void initToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        mBinding.toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        SearchView searchView = (SearchView) mBinding.toolbar.getMenu().findItem(R.id.action_search).getActionView();
        changeSearchViewTextColor(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(this.getClass().getName(), "onQueryTextChange: " + newText);
                Filter filter = mAdapter.getFilter();
                filter.filter(newText);
                return true;
            }
        });
    }

    private void initRecView() {
        mBinding.customersRec.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.customersRec.setAdapter(mAdapter);
        mBinding.customersRec.setHasFixedSize(true);
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_name_a_z:
                mAdapter.sortByNameAscendingOrder();
                return true;
            case R.id.action_sort_by_name_z_a:
                mAdapter.sortByNameDescendingOrder();
                return true;
            case R.id.action_sort_by_date_a_z:
                mAdapter.sortByDateAscendingOrder();
                return true;
            case R.id.action_sort_by_date_z_a:
                mAdapter.sortByDateDescendingOrder();
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
}
