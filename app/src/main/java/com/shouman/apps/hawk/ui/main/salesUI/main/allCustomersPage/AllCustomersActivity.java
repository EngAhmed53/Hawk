package com.shouman.apps.hawk.ui.main.salesUI.main.allCustomersPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.AllCustomersRecyclerViewAdapter;
import com.shouman.apps.hawk.databinding.ActivityAllCustomersBinding;
import com.shouman.apps.hawk.ui.main.OnCustomerItemClickHandler;
import com.shouman.apps.hawk.ui.main.companyUi.customers.customerInfo.Fragment_customers_info;

import java.util.Map;
import java.util.Objects;

public class AllCustomersActivity extends AppCompatActivity implements OnCustomerItemClickHandler {
    private ActivityAllCustomersBinding mBinding;
    private FragmentManager fragmentManager;
    private Fragment_customers_info fragment_customers_info;
    public static final String SALES_UID = "salesUID";
    private String salesUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_customers);
        fragmentManager = getSupportFragmentManager();

        initToolbar();
        Intent intent = getIntent();
        if (intent != null) {
            salesUID = intent.getStringExtra(SALES_UID);
        }

        initViewModel();
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("All Customers");
        mBinding.toolbar.inflateMenu(R.menu.all_customers_search_bar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViewModel() {
        AllCustomersViewModelFactory factory = new AllCustomersViewModelFactory(this, salesUID);
        AllCustomersViewModel allCustomersViewModel = new ViewModelProvider(this, factory).get(AllCustomersViewModel.class);
        allCustomersViewModel.getMapMediatorLiveData().observe(this, new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> allSalesCustomersMap) {
                mBinding.setAllCustomersMap(allSalesCustomersMap);
            }
        });
    }

    @Override
    public void onCustomerItemClickHandler(String customerUID, String customerName) {
        fragment_customers_info = Fragment_customers_info.getInstance(customerName, customerUID);

        fragmentManager
                .beginTransaction()
                .addToBackStack("customer_info")
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.fragment_container, fragment_customers_info, "fragment_customer_info")
                .commit();
    }

    @Override
    public void onBackPressed() {

        if (fragment_customers_info != null && fragment_customers_info.isVisible()) {

            fragmentManager.popBackStack("customer_info", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        } else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.all_customers_search_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Filter filter = ((AllCustomersRecyclerViewAdapter) Objects.requireNonNull(mBinding.recAllCustomers.getAdapter())).getFilter();
                filter.filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
