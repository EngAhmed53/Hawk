package com.shouman.apps.hawk.ui.main.companyUi.add_new_branch;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.shouman.apps.hawk.data.Repo;
import com.shouman.apps.hawk.databinding.FragmentAddNewBranchBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_add_new_branch extends Fragment {

    public FragmentAddNewBranchBinding mBinding;

    public static Fragment_add_new_branch getInstance() {
        return new Fragment_add_new_branch();
    }


    public Fragment_add_new_branch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddNewBranchBinding.inflate(inflater);

        mBinding.btnAddBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchName = mBinding.edtBranchName.getEditableText().toString();
                if (branchName != null && !branchName.isEmpty()) {
                    Repo.addNewBranchToMyCompany(getContext(), branchName);
                    Toast.makeText(getContext(), branchName + " added to Company", Toast.LENGTH_SHORT).show();
                    mBinding.edtBranchName.setText("");
                } else {
                    mBinding.branchNameTextField.requestFocus();
                }
            }
        });

        return mBinding.getRoot();
    }

}
