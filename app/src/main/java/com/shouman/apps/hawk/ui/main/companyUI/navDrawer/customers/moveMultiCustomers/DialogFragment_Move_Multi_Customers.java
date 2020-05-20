package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.customers.moveMultiCustomers;

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
import com.shouman.apps.hawk.databinding.DialogFragmentMoveMutliCustomersBinding;

import java.util.ArrayList;
import java.util.List;

public class DialogFragment_Move_Multi_Customers extends DialogFragment {

    private DialogFragmentMoveMutliCustomersBinding mBinding;
    private List<String> allSalesmenList;
    private DropDownArrayAdapter salesAdapter;
    private BiMap<String, String> allSalesmenMap;
    private FirebaseCompanyRepo firebaseCompanyRepo;
    private MoveCustomerViewModel mViewModel;
    private String newSalesmanName;
    private String newSalesmanUID;
    private String[] selectedCustomersUIDs;
    private String oldSalesUID;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        selectedCustomersUIDs = DialogFragment_Move_Multi_CustomersArgs.fromBundle(getArguments()).getSelectedCustomersUIDs();
        oldSalesUID = DialogFragment_Move_Multi_CustomersArgs.fromBundle(getArguments()).getSalesUID();
        initViewModel();
    }

//    @Override
//    public int getTheme() {
//        return R.style.BottomSheetDialogTheme;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DialogFragmentMoveMutliCustomersBinding.inflate(inflater);

        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }

        mBinding.getRoot().setClipToOutline(true);

        mBinding.filledExposedDropdown.setInputType(InputType.TYPE_NULL);


        mBinding.filledExposedDropdown.setOnItemClickListener((parent, view, position, id) -> {
            newSalesmanName = allSalesmenList.get(position);
            newSalesmanUID = allSalesmenMap.inverse().get(newSalesmanName);
            mBinding.btnMove.setEnabled(true);
        });

        mBinding.btnMove.setOnClickListener(v -> {
            firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
            moveCustomer(newSalesmanUID, newSalesmanName, selectedCustomersUIDs);
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
            if (oldSalesUID != null) allSalesmenMap.remove(oldSalesUID);
            allSalesmenList = new ArrayList<>(allSalesmenMap.values());
            salesAdapter = new DropDownArrayAdapter(requireContext(), allSalesmenList);
            mBinding.filledExposedDropdown.setAdapter(salesAdapter);
        });
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(MoveCustomerViewModel.class);
    }

    private void moveCustomer(String newSalesmanUID, String newSalesmanName, String[] selectedCustomersUIDs) {
        firebaseCompanyRepo.moveCustomerToAnotherSalesman(getContext(), newSalesmanUID, newSalesmanName, selectedCustomersUIDs);
    }
}
