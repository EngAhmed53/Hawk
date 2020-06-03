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
import com.shouman.apps.hawk.adapters.AllCustomersRecyclerViewAdapter;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.databinding.FragmentCustomersBinding;
import com.shouman.apps.hawk.ui.main.companyUI.navDrawer.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_customers extends Fragment {

    private FragmentCustomersBinding mBinding;
    private AllCustomersViewModel allCustomersViewModel;
    private AllCustomersRecyclerViewAdapter allCustomersAdapter;
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

    public Fragment_customers() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        actionModeCallback = new ActionModeCallback();
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

        allCustomersViewModel.geCustomersListLiveData().observe(getViewLifecycleOwner(), allCustomersList -> {
            mBinding.customersRec.setVisibility(View.GONE);
            mBinding.progressBar.setVisibility(View.VISIBLE);
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
            allCustomersAdapter.clearSelections();
            actionMode = null;
            Common.setSystemBarColor(requireActivity(), R.color.colorPrimaryDark);
        }
    }

    private void moveSelectedCustomers() {
        String[] customersUIDs = new String[allCustomersAdapter.selectedUIDs.size()];
        NavDirections toSelectSalesman =
                Fragment_customersDirections.
                        actionFragmentCustomersToDialogFragmentMoveMultiCustomers(allCustomersAdapter.selectedUIDs.toArray(customersUIDs), null);
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
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                            dialog.dismiss();
                            mode.finish();
                        }
                );
        builder.create().show();
    }


    private void deleteSelectedCustomers() {
        firebaseCompanyRepo.deleteCustomers(requireContext(), allCustomersAdapter.selectedUIDs);
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

    @Override
    public void onResume() {
        super.onResume();
        allCustomersAdapter.selectedUIDs.addOnListChangedCallback(onSelectedItemsChange);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (onSelectedItemsChange != null) {
            allCustomersAdapter.selectedUIDs.removeOnListChangedCallback(onSelectedItemsChange);
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
        if (allCustomersAdapter != null) {
            allCustomersAdapter.clearSelections();
        }
        allCustomersAdapter = null;
        super.onDestroy();
    }
}
