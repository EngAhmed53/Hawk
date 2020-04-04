package com.shouman.apps.hawk.ui.main.companyUi.sales_members.sales_info;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentSalesInfoBinding;
import com.shouman.apps.hawk.model.User;
import com.shouman.apps.hawk.ui.main.companyUi.MainActivity;
import com.shouman.apps.hawk.utils.AppExecutors;

import net.glxn.qrgen.android.QRCode;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sales_info extends Fragment {

    private static final String SALES_UID = "sales_uid";
    private String salesUID;
    private FragmentSalesInfoBinding mBinding;

    public static Fragment_sales_info getInstance() {
        return new Fragment_sales_info();
    }

    public static Fragment_sales_info getInstance(String salesUID) {
        Bundle args = new Bundle();
        args.putString(SALES_UID, salesUID);
        Fragment_sales_info sales_info = Fragment_sales_info.getInstance();
        sales_info.setArguments(args);
        return sales_info;
    }

    public Fragment_sales_info() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentSalesInfoBinding.inflate(inflater);

        Bundle args = getArguments();
        if (args != null) {
            salesUID = args.getString(SALES_UID);
        }
        intViewModel();
        initToolbar();
        return mBinding.getRoot();
    }

    private void intViewModel() {
        SalesInfoViewModelFactory factory = new SalesInfoViewModelFactory(salesUID);
        SalesInfoViewModel salesInfoViewModel = new ViewModelProvider(this, factory).get(SalesInfoViewModel.class);
        salesInfoViewModel.getSalesMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setUI(user);
            }
        });
    }

    private void setUI(User user) {
        mBinding.setUid(salesUID);
        mBinding.setUser(user);
        StringBuilder qrInfo = new StringBuilder(getString(R.string.qr_code_key))
                .append(", ")
                .append(user.getUn())
                .append(", ")
                .append(salesUID)
                .append(", ")
                .append(user.getCuid())
                .append(", ")
                .append(user.getBuid());

        setTheQRCode(qrInfo.toString());
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
                        mBinding.salesQrCode.setImageBitmap(scaledBitMap);
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
                .popBackStack("sales_info", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private MainActivity getHostActivity() {
        return (MainActivity) getActivity();
    }
}
