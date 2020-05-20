package com.shouman.apps.hawk.ui.main.companyUI.salesman.salesInfo.moveSalesMan;

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
import com.shouman.apps.hawk.databinding.DialogFragmentMoveSalesBinding;

import java.util.ArrayList;
import java.util.List;

public class DialogFragment_Move_Sales extends DialogFragment {

    private DialogFragmentMoveSalesBinding mBinding;
    private String salesName;
    private String salesUID;
    private boolean salesStatus;
    private List<String> allBranchesList;
    private DropDownArrayAdapter branchesAdapter;
    private BiMap<String, String> allBranchesMap;
    private String oldBranchUID;
    private String newBranchUID;
    private FirebaseCompanyRepo firebaseCompanyRepo;
    private MoveSalesmanViewModel moveSalesmanViewModel;
    private String selectedBranchName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        salesName = DialogFragment_Move_SalesArgs.fromBundle(getArguments()).getSalesName();
        salesUID = DialogFragment_Move_SalesArgs.fromBundle(getArguments()).getSalesUID();
        salesStatus = DialogFragment_Move_SalesArgs.fromBundle(getArguments()).getStatus();
        oldBranchUID = DialogFragment_Move_SalesArgs.fromBundle(getArguments()).getOldBranchUID();

        initViewModel();
    }

//    @Override
//    public int getTheme() {
//        return R.style.BottomSheetDialogTheme;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DialogFragmentMoveSalesBinding.inflate(inflater);

        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
        mBinding.filledExposedDropdown.setInputType(InputType.TYPE_NULL);


        mBinding.headline.setText(getString(R.string.move_sales_member_to_another_branch, salesName));

        mBinding.filledExposedDropdown.setOnItemClickListener((parent, view, position, id) -> {
            selectedBranchName = allBranchesList.get(position);
            newBranchUID = allBranchesMap.inverse().get(selectedBranchName);
            mBinding.btnMove.setEnabled(true);
        });

        mBinding.btnMove.setOnClickListener(v -> {
            firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
            moveSalesMember(salesName, salesUID, salesStatus, oldBranchUID, newBranchUID, selectedBranchName);
            Toast.makeText(requireContext(), R.string.move_salesman_done, Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        moveSalesmanViewModel.getMapMediatorLiveData().observe(getViewLifecycleOwner(), allBranches -> {
            allBranchesMap = allBranches;
            allBranchesMap.remove(oldBranchUID);
            allBranchesList = new ArrayList<>(allBranchesMap.values());
            branchesAdapter = new DropDownArrayAdapter(requireContext(), allBranchesList);
            mBinding.filledExposedDropdown.setAdapter(branchesAdapter);
        });

    }

    private void initViewModel() {
        moveSalesmanViewModel = new ViewModelProvider(this).get(MoveSalesmanViewModel.class);
    }

    private void moveSalesMember(String salesName, String salesUID, boolean salesStatus, String oldBranchUID, String newBranchUID, String selectedBranchName) {
        firebaseCompanyRepo.moveSalesMemberToAnotherBranch(getContext(), salesName, salesUID, salesStatus, oldBranchUID, newBranchUID, selectedBranchName);
    }
}
