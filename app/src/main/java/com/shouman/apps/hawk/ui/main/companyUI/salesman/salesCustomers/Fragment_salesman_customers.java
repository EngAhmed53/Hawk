package com.shouman.apps.hawk.ui.main.companyUI.salesman.salesCustomers;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.CustomersBySalesmanRecyclerViewAdapter;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.databinding.FragmentSalesmanCustomersBinding;
import com.shouman.apps.hawk.ui.main.companyUI.navDrawer.MainActivity;

public class Fragment_salesman_customers extends Fragment {

    private SalesmanCustomersViewModel mViewModel;
    private FragmentSalesmanCustomersBinding mBinding;
    private CustomersBySalesmanRecyclerViewAdapter mAdapter;
    private String salesUID;
    private FirebaseCompanyRepo firebaseCompanyRepo;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    private boolean isActionModeEnabled;
    private ObservableList.OnListChangedCallback<ObservableList<String>> onSelectedItemsChange = new ObservableList.OnListChangedCallback<ObservableList<String>>() {
        @Override
        public void onChanged(ObservableList<String> strings) {
        }

        @Override
        public void onItemRangeChanged(ObservableList<String> strings, int i, int i1) {
        }

        @Override
        public void onItemRangeInserted(ObservableList<String> strings, int i, int i1) {
            controlActionMode(strings);
        }

        @Override
        public void onItemRangeMoved(ObservableList<String> strings, int i, int i1, int i2) {
        }

        @Override
        public void onItemRangeRemoved(ObservableList<String> strings, int i, int i1) {
            controlActionMode(strings);
        }
    };

    private void controlActionMode(ObservableList<String> strings) {
        if (strings.size() == 0) {
            actionMode.finish();
            isActionModeEnabled = false;
        } else {
            if (!isActionModeEnabled) {
                actionMode = ((MainActivity) requireActivity()).startSupportActionMode(actionModeCallback);
                isActionModeEnabled = true;
            }
            assert actionMode != null;
            actionMode.setTitle(String.valueOf(strings.size()));
            actionMode.invalidate();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        actionModeCallback = new ActionModeCallback();

        assert getArguments() != null;
        salesUID = Fragment_salesman_customersArgs.fromBundle(getArguments()).getSalesUID();
        initViewModel();
        mAdapter = new CustomersBySalesmanRecyclerViewAdapter(requireContext());
    }


    private void initViewModel() {
        SalesmanCustomersViewModelFactory factory = new SalesmanCustomersViewModelFactory(requireActivity().getApplication(), salesUID);
        mViewModel = new ViewModelProvider(this, factory).get(SalesmanCustomersViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSalesmanCustomersBinding.inflate(inflater);
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


    private void initRecView() {
        mBinding.customersRec.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.customersRec.setAdapter(mAdapter);
        mBinding.customersRec.setHasFixedSize(true);
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Common.setSystemBarColor(requireActivity(), R.color.colorAccent);
            mode.getMenuInflater().inflate(R.menu.all_customers_action_menu, menu);
            firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {
                deleteCustomersDialog(mode);
                return true;
            } else if (id == R.id.action_move) {
                moveSelectedCustomers();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
            Common.setSystemBarColor(requireActivity(), R.color.colorPrimaryDark);
        }
    }

    private void moveSelectedCustomers() {
        String[] customersUIDs = new String[mAdapter.selectedUIDs.size()];
        NavDirections toSelectSalesman =
                Fragment_salesman_customersDirections.
                        actionFragmentSalesmanCustomersToDialogFragmentMoveMultiCustomers(mAdapter.selectedUIDs.toArray(customersUIDs), salesUID);
        Navigation.findNavController(mBinding.getRoot()).navigate(toSelectSalesman);
    }

    private void deleteCustomersDialog(ActionMode mode) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.delete_customer)
                .setMessage(R.string.delete_customers_msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.delete_customer_forever), (dialog, which) -> {
                    deleteSelectedCustomers();
                    dialog.dismiss();
                    mode.finish();
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void deleteSelectedCustomers() {
        firebaseCompanyRepo.deleteCustomers(requireContext(), mAdapter.selectedUIDs);
        Toast.makeText(requireContext(), getString(R.string.customer_deleted), Toast.LENGTH_SHORT).show();
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
                Filter filter = mAdapter.getFilter();
                filter.filter(newText);
                return true;
            }
        });
    }

    @Override
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

    @Override
    public void onResume() {
        mAdapter.selectedUIDs.addOnListChangedCallback(onSelectedItemsChange);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (onSelectedItemsChange != null) {
            mAdapter.selectedUIDs.removeOnListChangedCallback(onSelectedItemsChange);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mAdapter != null) {
            mAdapter.clearSelections();
        }
        super.onDestroy();
    }
}
