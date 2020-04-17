package com.shouman.apps.hawk.ui.main.companyUi.branch_details.branch_info;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentBranchInfoBinding;
import com.shouman.apps.hawk.preferences.UserPreference;
import com.shouman.apps.hawk.ui.main.companyUi.FragmentContainerActivity;
import com.shouman.apps.hawk.utils.AppExecutors;

import net.glxn.qrgen.android.QRCode;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_branch_info extends Fragment {

    private static final String BRANCH_UID = "branch_uid";
    private static final String BRANCH_NAME = "branch_name";
    private String companyName;
    private String companyUID;
    private String branchUID;
    private String branchName;
    private FragmentBranchInfoBinding mBinding;

    public static Fragment_branch_info getInstance() {
        return new Fragment_branch_info();
    }

    public static Fragment_branch_info getInstance(String branchUID, String branchName) {
        Bundle args = new Bundle();
        args.putString(BRANCH_UID, branchUID);
        args.putString(BRANCH_NAME, branchName);
        Fragment_branch_info branch_info = Fragment_branch_info.getInstance();
        branch_info.setArguments(args);
        return branch_info;
    }

    public Fragment_branch_info() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentBranchInfoBinding.inflate(inflater);

        Bundle args = getArguments();
        if (args != null) {
            branchUID = args.getString(BRANCH_UID);
            branchName = args.getString(BRANCH_NAME);
        }

        companyName = UserPreference.getUserCompanyName(getContext());
        companyUID = UserPreference.getCompanyUID(getContext());

        initToolbar();
        setUI();

        return mBinding.getRoot();
    }

    private void setUI() {
        mBinding.setBranchName(branchName);
        mBinding.edtBranchCountry.setText("Egypt");
        mBinding.edtBranchCity.setText("Cairo");
        mBinding.edtBranchPhone.setText("01028700014");

        String qrInfo = getString(R.string.qr_code_key) + ", " + branchName + ", " + branchUID + ", " + companyName + ", " + companyUID;
        setTheQRCode(qrInfo);
    }

    private void setTheQRCode(String qrInfo) {
        final Bitmap qrBitMap = QRCode.from(qrInfo).bitmap();
        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
               final Bitmap scaledBitMap = Bitmap.createScaledBitmap(qrBitMap, 600, 600, false);
               AppExecutors.getsInstance().getMainThread().execute(new Runnable() {
                   @Override
                   public void run() {
                       mBinding.branchQrCode.setImageBitmap(scaledBitMap);
                   }
               });
            }
        });
    }

    private void initToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToBranchHome();
            }
        });
    }

    private void backToBranchHome() {
        getHostActivity()
                .fragmentManager
                .popBackStack("branch_info", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private FragmentContainerActivity getHostActivity() {
        return (FragmentContainerActivity) getActivity();
    }
}
