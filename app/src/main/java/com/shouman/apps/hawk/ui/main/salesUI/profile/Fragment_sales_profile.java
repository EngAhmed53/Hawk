package com.shouman.apps.hawk.ui.main.salesUI.profile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.databinding.FragmentSalesProfileBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sales_profile extends Fragment {


    private FragmentSalesProfileBinding mBinding;
    private SalesProfileViewModel salesProfileViewModel;


    public Fragment_sales_profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        intViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentSalesProfileBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
        salesProfileViewModel.getSalesMediatorLiveData().observe(getViewLifecycleOwner(), this::setUI);
    }

    private void intViewModel() {
        salesProfileViewModel = new ViewModelProvider(this).get(SalesProfileViewModel.class);
    }

    private void setUI(User user) {
        if (user == null) return;
        mBinding.salesTitleBigTxt.setText(user.getUn());
        mBinding.salesTitleSmallTxt.setText(user.getUn());
        mBinding.salesEmailTxt.setText(user.getE());
        mBinding.salesPhoneTxt.setText("Not Defined");
        if (user.isStatus()) {
            mBinding.salesStatusTxt.setText(R.string.active_label);
        } else {
            mBinding.salesStatusTxt.setText(R.string.disabled_label);
        }
        mBinding.salesBranchNameTxt.setText(user.getBn());
    }

    @Override
    public void onResume() {
        mBinding.infoFrame.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_down));
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

        }
        return super.onOptionsItemSelected(item);
    }
}
