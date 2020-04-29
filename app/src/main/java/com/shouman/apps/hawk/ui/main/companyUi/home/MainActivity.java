package com.shouman.apps.hawk.ui.main.companyUi.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.ActivityMainBinding;
import com.shouman.apps.hawk.ui.main.companyUi.ContainerActivity;

import static com.shouman.apps.hawk.ui.main.companyUi.ContainerActivity.SELECTED_MENU_ITEM;

public class MainActivity extends AppCompatActivity implements OnMenuItemClick {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //set the actionBarToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mBinding.drawerLayout,
                mBinding.toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_closed);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @Override
    public void onMenuItemClickHandler(String selectedItem) {
        Intent intent = new Intent(this, ContainerActivity.class);
        intent.putExtra(SELECTED_MENU_ITEM, selectedItem);
        startActivity(intent);
    }
}
