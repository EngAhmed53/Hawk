package com.shouman.apps.hawk.ui.main.salesMemberUI.home.personalPage;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.ActivityPersonalPageBinding;
import com.shouman.apps.hawk.model.User;

public class PersonalPageActivity extends AppCompatActivity {

    private ActivityPersonalPageBinding mBinding;
    private PersonalPageViewModel personalPageViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_personal_page);

        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initViewModel();

        personalPageViewModel.getUserMediatorLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    mBinding.setUser(user);
                }
            }
        });
    }

    private void initViewModel() {
        personalPageViewModel = new ViewModelProvider(this).get(PersonalPageViewModel.class);
    }
}
