package com.shouman.apps.hawk.ui.main.salesMemberUI.home;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.ActivityMain2Binding;
import com.shouman.apps.hawk.ui.main.companyUi.customers.Fragment_customers_info;

public class Main2Activity extends AppCompatActivity implements IMain2ClickHandler {

    private ActivityMain2Binding mainBinding;
    private Fragment_sales_home fragment_sales_home;
    Fragment_customers_info fragment_customers_info;
    private FragmentManager fragmentManager;
    private String userUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main2);
        fragmentManager = getSupportFragmentManager();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userUID = firebaseUser.getUid();
        }

        if (savedInstanceState == null) {
            fragment_sales_home = Fragment_sales_home.getInstance(userUID);
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
        }

        if (fragment_customers_info != null && fragment_customers_info.isVisible()) {
            Toast.makeText(this, "hide fragment", Toast.LENGTH_SHORT).show();
            fragmentManager.popBackStack("customer_info", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mainBinding.fullCustomerInfoContainer.setVisibility(View.GONE);
        }

        else super.onBackPressed();
    }

    @Override
    public void onCustomerItemClickHandler(String customerUID, String customerName) {
        fragment_customers_info = Fragment_customers_info.getInstance(customerName, customerUID);
        mainBinding.fullCustomerInfoContainer.setVisibility(View.VISIBLE);
        fragmentManager.
                beginTransaction()
                .addToBackStack("customer_info")
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.full_customer_info_container, fragment_customers_info, "fragment_customer_info")
                .commit();

    }


}
