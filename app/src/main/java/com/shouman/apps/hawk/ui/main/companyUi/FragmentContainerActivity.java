package com.shouman.apps.hawk.ui.main.companyUi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.ActivityFragmentContainerBinding;
import com.shouman.apps.hawk.ui.main.companyUi.add_new_branch.Fragment_add_new_branch;
import com.shouman.apps.hawk.ui.main.companyUi.all_branches.Fragment_all_branches;
import com.shouman.apps.hawk.ui.main.companyUi.branch_details.branch_home.Fragment_branch;
import com.shouman.apps.hawk.ui.main.companyUi.customers.customerInfo.Fragment_customers_info;
import com.shouman.apps.hawk.ui.main.companyUi.sales_members.sales_main.Fragment_sales_details;


public class FragmentContainerActivity extends AppCompatActivity implements IMainClickHandler {

    private static final String TAG = "MainActivity";
    public static final String SELECTED_MENU_ITEM = "selected_fragment";
    private ActivityFragmentContainerBinding mainBinding;
    public FragmentManager fragmentManager;
    private Fragment_add_new_branch fragment_add_new_branch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_fragment_container);
        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SELECTED_MENU_ITEM)) {
            String action = intent.getStringExtra(SELECTED_MENU_ITEM);
            assert action != null;
            if (action.equals(getString(R.string.branches_title))) {
                showBranchesFragment();
                Toast.makeText(this, "branches", Toast.LENGTH_SHORT).show();
            } else if (action.equals(getString(R.string.sales_team_title))) {
                showAllSalesFragment();
            } else if (action.equals(getString(R.string.customers_title))) {
                showAllCustomersFragment();
            } else if (action.equals(getString(R.string.company_notification))) {
                showNotificationsFragment();
            } else if (action.equals(getString(R.string.reports_title))) {
                showReportsFragment();
            }
        }

    }

    private void showReportsFragment() {

    }

    private void showNotificationsFragment() {

    }

    private void showAllCustomersFragment() {

    }

    private void showAllSalesFragment() {

    }

    private void showBranchesFragment() {
        Fragment_all_branches fragment_all_branches = (Fragment_all_branches) fragmentManager.findFragmentByTag("all_branches");
        if (fragment_all_branches == null) {
            fragment_all_branches = Fragment_all_branches.getInstance();
            fragmentManager.beginTransaction().add(R.id.home_container, fragment_all_branches, "all_branches").commit();
        }
    }


    public void showAddNewBranchFragment() {
        if (!fragment_add_new_branch.isAdded()) {
            fragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .add(R.id.home_container, fragment_add_new_branch, "fragment_add_new_branch")
                    .commit();
        } else {
            fragmentManager.beginTransaction().show(fragment_add_new_branch).commit();
        }
    }

    @Override
    public void onBranchItemClickHandler(String branchUID, String branchName) {
        //show branch fragment
        Fragment_branch fragment_branch = Fragment_branch.getInstance(branchUID, branchName);
        fragmentManager
                .beginTransaction()
                .addToBackStack("branch_details")
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.home_container, fragment_branch, "fragment_branch_details")
                .commit();
    }

    @Override
    public void onSalesItemClickHandler(String salesUID, String salesName) {
        Fragment_sales_details fragment_sales_details = Fragment_sales_details.getInstance(salesUID, salesName);
        fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.home_container, fragment_sales_details, "fragment_branch_details")
                .commit();
    }

    @Override
    public void onCustomerItemClickHandler(String customerUID, String customerName) {
        Fragment_customers_info fragment_customers_info = Fragment_customers_info.getInstance(customerName, customerUID);
        fragmentManager
                .beginTransaction()
                .addToBackStack("customer_info")
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.home_container, fragment_customers_info, "fragment_customers_info")
                .commit();
    }
}
