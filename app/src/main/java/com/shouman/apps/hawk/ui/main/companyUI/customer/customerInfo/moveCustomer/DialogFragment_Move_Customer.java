package com.shouman.apps.hawk.ui.main.companyUI.customer.customerInfo.moveCustomer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.collect.BiMap;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.DropDownArrayAdapter;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.databinding.DialogFragmentMoveCustomerBinding;

import java.util.ArrayList;
import java.util.List;

public class DialogFragment_Move_Customer extends DialogFragment {

    private DialogFragmentMoveCustomerBinding mBinding;
    private String customerName;
    private String customerUID;
    private String oldSalesmenUID;
    private List<String> allSalesmenList;
    private DropDownArrayAdapter salesAdapter;
    private BiMap<String, String> allSalesmenMap;
    private FirebaseCompanyRepo firebaseCompanyRepo;
    private MoveCustomerViewModel mViewModel;
    private String newSalesmanName;
    private String newSalesmanUID;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        customerName = DialogFragment_Move_CustomerArgs.fromBundle(getArguments()).getCustomerName();
        customerUID = DialogFragment_Move_CustomerArgs.fromBundle(getArguments()).getCustomerUID();
        oldSalesmenUID = DialogFragment_Move_CustomerArgs.fromBundle(getArguments()).getOldSalesmanUID();
        initViewModel();
    }

//    @Override
//    public int getTheme() {
//        return R.style.BottomSheetDialogTheme;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DialogFragmentMoveCustomerBinding.inflate(inflater);

        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }

        mBinding.getRoot().setClipToOutline(true);

        mBinding.filledExposedDropdown.setInputType(InputType.TYPE_NULL);


        mBinding.headline.setText(getString(R.string.move_sales_member_to_another_branch, customerName));

        mBinding.filledExposedDropdown.setOnItemClickListener((parent, view, position, id) -> {
            newSalesmanName = allSalesmenList.get(position);
            newSalesmanUID = allSalesmenMap.inverse().get(newSalesmanName);
            mBinding.btnMove.setEnabled(true);
        });

        mBinding.btnMove.setOnClickListener(v -> {
            firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
            moveCustomer(newSalesmanUID, newSalesmanName, customerUID);
            Toast.makeText(requireContext(), R.string.customer_moved_successfully, Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.getSalesmenMediatorLiveData().observe(getViewLifecycleOwner(), allSalesmen -> {
            allSalesmenMap = allSalesmen;
            allSalesmenMap.remove(oldSalesmenUID);
            allSalesmenList = new ArrayList<>(allSalesmenMap.values());
            salesAdapter = new DropDownArrayAdapter(requireContext(), allSalesmenList);
            mBinding.filledExposedDropdown.setAdapter(salesAdapter);
        });
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(MoveCustomerViewModel.class);
    }

    private void moveCustomer(String newSalesmanUID, String newSalesmanName, String... customerUID) {
        firebaseCompanyRepo.moveCustomerToAnotherSalesman(getContext(), newSalesmanUID, newSalesmanName, customerUID);
    }
}
