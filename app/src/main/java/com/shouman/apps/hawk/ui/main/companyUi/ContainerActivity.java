package com.shouman.apps.hawk.ui.main.companyUi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.ui.main.companyUi.all_branches.Fragment_all_branches;
import com.shouman.apps.hawk.ui.main.companyUi.all_branches.add_new_branch.Fragment_add_new_branch;
import com.shouman.apps.hawk.ui.main.companyUi.all_branches.branch_details.branch_home.Fragment_branch;
import com.shouman.apps.hawk.ui.main.companyUi.all_sales_members.Fragment_All_SalesMembers;
import com.shouman.apps.hawk.ui.main.companyUi.all_sales_members.moveSales.DialogFragment_Move_Sales;
import com.shouman.apps.hawk.ui.main.companyUi.customers.customerInfo.Fragment_customers_info;
import com.shouman.apps.hawk.ui.main.companyUi.sales_members.sales_main.Fragment_sales_main;


public class ContainerActivity extends AppCompatActivity implements IMainClickHandler, FirebaseCompanyRepo.OnSalesMemberDeleteAction {

    private static final String TAG = "MainActivity";
    public static final String SELECTED_MENU_ITEM = "selected_fragment";
    public FragmentManager fragmentManager;
    private Fragment_add_new_branch fragment_add_new_branch;
    private Fragment_branch fragment_branch;
    private FirebaseCompanyRepo firebaseCompanyRepo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.shouman.apps.hawk.databinding.ActivityFragmentContainerBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_fragment_container);
        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SELECTED_MENU_ITEM)) {
            String action = intent.getStringExtra(SELECTED_MENU_ITEM);
            assert action != null;
            if (action.equals(getString(R.string.branches_title))) {
                showBranchesFragment();
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
        firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
        Fragment_All_SalesMembers fragment_all_salesMembers = (Fragment_All_SalesMembers) fragmentManager.findFragmentByTag("all_sales_members");
        if (fragment_all_salesMembers == null) {
            fragment_all_salesMembers = Fragment_All_SalesMembers.getInstance();
            fragmentManager.beginTransaction().add(R.id.home_container, fragment_all_salesMembers, "all_sales_members").commit();
        }
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
        if (fragment_branch != null && fragment_branch.isAdded()) {
            return;
        }
        fragment_branch = Fragment_branch.getInstance(branchUID, branchName);
        fragmentManager
                .beginTransaction()
                .addToBackStack("branch_details")
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.home_container, fragment_branch, "fragment_branch_details")
                .commit();
    }

    @Override
    public void onSalesItemClickHandler(String salesUID, String salesName) {
        Fragment_sales_main fragment_sales_main = Fragment_sales_main.getInstance(salesUID, salesName);

        fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.home_container, fragment_sales_main, "fragment_branch_details")
                .commit();
    }


    @Override
    public void onCustomerItemClickHandler(String customerUID) {
        Fragment_customers_info fragment_customers_info = Fragment_customers_info.getInstance(customerUID);
        fragmentManager
                .beginTransaction()
                .addToBackStack("customer_info")
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.home_container, fragment_customers_info, "fragment_customers_info")
                .commit();
    }

    @Override
    public void onActionMove(String salesUID, String salesName, boolean status, String branchDetails) {
        Toast.makeText(this, "" + salesUID + " " + salesName + " " + branchDetails, Toast.LENGTH_SHORT).show();
        String branchUID = branchDetails.substring(0, branchDetails.indexOf(", "));
        openMoveToDialogFragment(salesUID, salesName, status, branchUID);
    }

    @Override
    public void onActionDisable(String salesUID, String branchDetails) {
        String branchUID = branchDetails.substring(0, branchDetails.indexOf(", "));
        showDisableDialog(salesUID, branchUID);
    }

    @Override
    public void onActionEnable(String salesUID, String branchDetails) {
        String branchUID = branchDetails.substring(0, branchDetails.indexOf(", "));
        firebaseCompanyRepo.enableSalesMember(getApplicationContext(), salesUID, branchUID);
        Toast.makeText(ContainerActivity.this, "Enabled", Toast.LENGTH_SHORT).show();
    }

    private void showDisableDialog(final String salesUID, final String branchUID) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle(R.string.disable_sales_title)
                .setMessage(R.string.disable_sales_msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.continue_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseCompanyRepo.disableSalesMember(getApplicationContext(), salesUID, branchUID);
                        Toast.makeText(ContainerActivity.this, "Disabled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_info);
        builder.create().show();
    }

    @Override
    public void onActionDelete(final String salesUID, final String branchDetails) {
        final String branchUID = branchDetails.substring(0, branchDetails.indexOf(", "));

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle(R.string.delete_sales_member)
                .setMessage("Are you sure you want to delete this sales member ?")
                .setCancelable(true)
                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performDelete(salesUID, branchUID);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_info);
        builder.create().show();
    }

    private void performDelete(String salesUID, String branchUID) {
        firebaseCompanyRepo.deleteSalesMember(this, this, salesUID, branchUID);
    }

    @Override
    public void onDeleteSuccess() {
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteFailed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle(R.string.delete_sales_member)
                .setMessage(R.string.sales_member_delete_failed_msg)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_report_problem);
        builder.create().show();
    }

    private void openMoveToDialogFragment(String salesUID, String salesName, boolean status, String branchUID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment_Move_Sales dialogFragment_move_sales =
                DialogFragment_Move_Sales.getInstance(salesUID, salesName, status, branchUID);

        dialogFragment_move_sales.showNow(fragmentManager, "move_to");
    }
}
