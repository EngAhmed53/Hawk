package com.shouman.apps.hawk.ui.main.companyUI.all_sales_members.moveSales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.collect.BiMap;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.BranchesDropDownArrayAdapter;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.databinding.DialogFragmentMoveSalesBinding;

import java.util.ArrayList;
import java.util.List;

public class DialogFragment_Move_Sales extends DialogFragment {

    private DialogFragmentMoveSalesBinding mBinding;
    private static final String SALES_NAME = "sales_name";
    //    private static final String BRANCH_NAME = "branch_name";
    private static final String OLD_BRANCH_UID = "old_branch_uid";
    private static final String SALES_UID = "sales_uid";
    private static final String SALES_STATUS = "sales_status";
    private String salesName;
    private String salesUID;
    private boolean salesStatus;
    //    private String branchName;
    private List<String> allBranchesList;
    private BranchesDropDownArrayAdapter branchesAdapter;
    private BiMap<String, String> allBranchesMap;
    private String oldBranchUID;
    private String newBranchUID;
    private FirebaseCompanyRepo firebaseCompanyRepo;


    public static DialogFragment_Move_Sales getInstance() {
        return new DialogFragment_Move_Sales();
    }

    public static DialogFragment_Move_Sales getInstance(String salesUID, String salesName, boolean status, String oldBranchUID) {
        DialogFragment_Move_Sales dialogFragment_move_sales = DialogFragment_Move_Sales.getInstance();


        Bundle bundle = new Bundle();
        bundle.putString(SALES_UID, salesUID);
        bundle.putString(SALES_NAME, salesName);
        bundle.putString(OLD_BRANCH_UID, oldBranchUID);
        bundle.putBoolean(SALES_STATUS, status);
//        bundle.putString(BRANCH_NAME, branchName);

        dialogFragment_move_sales.setArguments(bundle);
        return dialogFragment_move_sales;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DialogFragmentMoveSalesBinding.inflate(inflater);

        Bundle args = getArguments();
        if (args != null) {
            salesName = args.getString(SALES_NAME);
            salesUID = args.getString(SALES_UID);
//            branchName = args.getString(BRANCH_NAME);
            oldBranchUID = args.getString(OLD_BRANCH_UID);
            salesStatus = args.getBoolean(SALES_STATUS);
        }
        mBinding.headline.setText(getString(R.string.move_sales_member_to_another_branch, salesName));

        initToolbar();
        initViewModel();

        mBinding.filledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedBranchName = allBranchesList.get(position);
                newBranchUID = allBranchesMap.inverse().get(selectedBranchName);
                mBinding.btnMove.setEnabled(true);
            }
        });

        mBinding.btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
                moveSalesMember(salesName, salesUID, salesStatus, oldBranchUID, newBranchUID);
                Toast.makeText(getContext(), "Moving Done", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        return mBinding.getRoot();
    }

    private void finish() {
        getParentFragmentManager().beginTransaction().remove(DialogFragment_Move_Sales.this).commit();
    }

    private void initViewModel() {
        AllBranchesViewModel allBranchesViewModel = new ViewModelProvider(this).get(AllBranchesViewModel.class);
        allBranchesViewModel.getMapMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<BiMap<String, String>>() {
            @Override
            public void onChanged(BiMap<String, String> allBranches) {
                allBranchesMap = allBranches;
                allBranchesMap.remove(oldBranchUID);
                allBranchesList = new ArrayList<>(allBranchesMap.values());
                branchesAdapter = new BranchesDropDownArrayAdapter(requireContext(), allBranchesList);
                mBinding.filledExposedDropdown.setAdapter(branchesAdapter);
            }
        });
    }

    private void initToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void moveSalesMember(String salesName, String salesUID, boolean salesStatus, String oldBranchUID, String newBranchUID) {
        firebaseCompanyRepo.moveSalesMemberToAnotherBranch(getContext(), salesName, salesUID, salesStatus, oldBranchUID, newBranchUID);
    }
}
