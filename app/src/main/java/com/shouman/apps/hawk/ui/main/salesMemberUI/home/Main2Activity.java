package com.shouman.apps.hawk.ui.main.salesMemberUI.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.ActivityMain2Binding;
import com.shouman.apps.hawk.ui.main.salesMemberUI.newCustomer.AddNewCustomerActivity;

import static com.shouman.apps.hawk.ui.main.companyUi.customers.Fragment_customers_info.CUSTOMER_NAME;
import static com.shouman.apps.hawk.ui.main.companyUi.customers.Fragment_customers_info.CUSTOMER_UID;

public class Main2Activity extends AppCompatActivity implements IMain2ClickHandler{

    private ActivityMain2Binding mainBinding;
    private Fragment_sales_home fragment_sales_home;
    private FragmentManager fragmentManager;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main2);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragment_sales_home = Fragment_sales_home.getInstance();
        } else {
            fragment_sales_home = (Fragment_sales_home) fragmentManager.findFragmentByTag("fragment_sales_home");
        }

        //set the actionBar
        setSupportActionBar(mainBinding.toolbar);

        //set the actionBarToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mainBinding.drawerLayout,
                mainBinding.toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_closed);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));
        mainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //show the home fragment
        showHomeFragment();
    }

    private void showHomeFragment() {
        fragmentManager.
                beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.fragment_container_drw, fragment_sales_home, "fragment_sales_home")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @Override
    public void onCustomerItemClickHandler(String customerUID, String customerName) {

//        Intent intent = new Intent(Main2Activity.this, AddNewCustomerActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(CUSTOMER_UID, customerUID);
//        bundle.putString(CUSTOMER_NAME, customerName);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }
}
