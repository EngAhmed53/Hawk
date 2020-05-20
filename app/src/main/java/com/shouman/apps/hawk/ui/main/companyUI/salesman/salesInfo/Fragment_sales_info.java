package com.shouman.apps.hawk.ui.main.companyUI.salesman.salesInfo;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.data.model.User;
import com.shouman.apps.hawk.databinding.FragmentSalesInfoBinding;
import com.shouman.apps.hawk.network.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sales_info extends Fragment implements
        FirebaseCompanyRepo.OnSalesMemberDeleteAction {


    private FragmentSalesInfoBinding mBinding;
    private SalesInfoViewModel salesInfoViewModel;
    private FirebaseCompanyRepo firebaseCompanyRepo;
    private String salesUID;
    private User salesman;
    private NavController navController;


    public Fragment_sales_info() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        salesUID = Fragment_sales_infoArgs.fromBundle(getArguments()).getSalesUID();
        firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
        intViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentSalesInfoBinding.inflate(inflater);
        navController = Navigation.findNavController((container));
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        salesInfoViewModel.getSalesMediatorLiveData().observe(getViewLifecycleOwner(), this::setUI);

        mBinding.moveBtn.setOnClickListener(v -> onMoveSalesClick());

        mBinding.deleteBtn.setOnClickListener(v -> onDeleteClick());

        mBinding.changeStatusBtn.setOnClickListener(v -> onChangeSalesStatusClick());
    }

    private void intViewModel() {
        SalesInfoViewModelFactory factory = new SalesInfoViewModelFactory(salesUID);
        salesInfoViewModel = new ViewModelProvider(this, factory).get(SalesInfoViewModel.class);
    }

    private void setUI(User user) {
        if (user == null) {
            navController.navigate(R.id.action_fragment_sales_info_to_fragment_home);
            return;
        }
        salesman = user;
        mBinding.salesTitleBigTxt.setText(user.getUn());
        mBinding.salesTitleSmallTxt.setText(user.getUn());
        mBinding.salesEmailTxt.setText(user.getE());
        mBinding.salesPhoneTxt.setText("Not Defined");
        if (user.isStatus()) {
            mBinding.salesStatusTxt.setText(R.string.active_label);
            mBinding.changeStatus.setImageResource(R.drawable.ic_disable);
            mBinding.changeStatusTitle.setText(getString(R.string.disable));
        } else {
            mBinding.salesStatusTxt.setText(R.string.disabled_label);
            mBinding.changeStatus.setImageResource(R.drawable.ic_check_circle);
            mBinding.changeStatusTitle.setText(getString(R.string.enable));
        }
        mBinding.salesBranchNameTxt.setText(user.getBn());
    }

    @Override
    public void onResume() {
        mBinding.infoFrame.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_down));
        super.onResume();
    }


    private void onDeleteClick() {
        showDeleteDialog();
    }


    private void onChangeSalesStatusClick() {
        if (!salesman.isStatus()){
            firebaseCompanyRepo.enableSalesMember(requireContext(), salesUID, salesman.getBuid());
            Toast.makeText(requireContext(), R.string.enabled, Toast.LENGTH_SHORT).show();
        } else {
            showChangeStatusDialog();
        }
    }

    private void onMoveSalesClick() {
        openMoveToDialogFragment();
    }

    private void showChangeStatusDialog() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.disable_sales_title)
                .setMessage(R.string.disable_sales_msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.continue_btn), (dialog, which) -> {
                    if (NetworkUtils.isConnectedToInternet(requireContext())) {
                        if (salesman.isStatus()) {
                            firebaseCompanyRepo.disableSalesMember(requireContext(), salesUID, salesman.getBuid());
                            Toast.makeText(requireContext(), R.string.sales_disabled, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    } else {
                        showNoInternetConnectionDialog();
                    }
                }).setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showDeleteDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.delete_sales_member)
                .setMessage(R.string.delete_sales_confirm)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {

                    if (NetworkUtils.isConnectedToInternet(requireContext())) {
                        performDelete(salesUID, salesman.getBuid());
                        dialog.dismiss();
                    } else {
                        showNoInternetConnectionDialog();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showNoInternetConnectionDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.no_internet_connection)
                .setMessage(R.string.check_your_internet_connection)
                .setCancelable(true)
                .setPositiveButton(R.string.ok_dialog_btn, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void performDelete(String salesUID, String branchUID) {
        firebaseCompanyRepo.deleteSalesMember(requireContext(), this, salesUID, branchUID);
    }

    @Override
    public void onDeleteSuccess() {
        Toast.makeText(requireContext(), R.string.sales_deleted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteFailed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.error)
                .setMessage(R.string.sales_member_delete_failed_msg)
                .setCancelable(true)
                .setPositiveButton(R.string.ok_dialog_btn, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void openMoveToDialogFragment() {
        NavDirections toMoveSalesDialog =
                Fragment_sales_infoDirections.actionFragmentSalesInfoToDialogFragmentMoveSales(salesman.getUn(), salesUID, salesman.getBuid(), salesman.isStatus());
        navController.navigate(toMoveSalesDialog);
    }
}
