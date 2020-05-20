package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home.addNewBranch;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.database.firebaseRepo.FirebaseCompanyRepo;
import com.shouman.apps.hawk.databinding.FragmentAddNewBranchBinding;
import com.shouman.apps.hawk.ui.main.companyUI.navDrawer.MainActivity;
import com.shouman.apps.hawk.utils.AppExecutors;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_add_new_branch extends Fragment {

    public FragmentAddNewBranchBinding mBinding;
    private FirebaseCompanyRepo firebaseCompanyRepo;


    public Fragment_add_new_branch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animateToolbarZDown();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        firebaseCompanyRepo = FirebaseCompanyRepo.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddNewBranchBinding.inflate(inflater);

        mBinding.btnAddBranch.setOnClickListener(v -> {
            final String branchName = mBinding.edtBranchName.getEditableText().toString();
            if (!branchName.isEmpty()) {
                AppExecutors.getsInstance().getNetworkIO().execute(() -> firebaseCompanyRepo.addNewBranchToMyCompany(getContext(), branchName));
                Toast.makeText(getContext(), branchName + " added to Company", Toast.LENGTH_SHORT).show();
                mBinding.edtBranchName.setText("");
            } else {
                mBinding.branchNameTextField.requestFocus();
            }
        });
        return mBinding.getRoot();
    }


    private void animateToolbarZDown() {
        ((MainActivity) requireActivity()).mBinding.toolbar.setBackgroundColor(requireContext().getResources().getColor(R.color.colorPrimary));
        ViewPropertyAnimator animate = ((MainActivity) requireActivity()).mBinding.toolbar.animate();
        animate.z(1)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(500).start();
    }


    private void animateToolbarZUp() {
        ((MainActivity) requireActivity()).mBinding.toolbar.setBackgroundColor(requireContext().getResources().getColor(R.color.black_transparent));
        ViewPropertyAnimator animate = ((MainActivity) requireActivity()).mBinding.toolbar.animate();
        animate.z(8)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(500).start();
    }

    @Override
    public void onResume() {
        mBinding.infoFrame.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_down));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        animateToolbarZUp();
        super.onDestroy();
    }
}
