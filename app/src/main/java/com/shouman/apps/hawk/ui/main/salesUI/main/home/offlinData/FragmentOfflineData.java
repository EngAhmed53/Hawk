package com.shouman.apps.hawk.ui.main.salesUI.main.home.offlinData;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.data.database.localRepo.LocalSalesRepo;
import com.shouman.apps.hawk.data.model.DailyLogEntry;
import com.shouman.apps.hawk.databinding.FragmentOffineDataBinding;
import com.shouman.apps.hawk.network.NetworkUtils;
import com.shouman.apps.hawk.sync.workManager.SyncWorker;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfflineData extends Fragment {

    private FragmentOffineDataBinding mBinding;

    public static FragmentOfflineData getInstance() {
        return new FragmentOfflineData();
    }

    public FragmentOfflineData() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentOffineDataBinding.inflate(inflater);

        initToolbar();
        intiViewModel();
        initUploading();


        return mBinding.getRoot();
    }

    private void initUploading() {
        mBinding.uploadDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnectedToInternet(v.getContext())) {
                    v.setVisibility(View.INVISIBLE);
                    mBinding.uploadingProgressBar.setVisibility(View.VISIBLE);
                    uploadData();
                } else {
                    showConnectionErrorDialog(v.getContext());
                }
            }
        });
    }

    private void showConnectionErrorDialog(Context context) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again")
                .setCancelable(true)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_report_problem);
        builder.create().show();
    }

    private void uploadData() {
        Toast.makeText(getContext(), "Uploading", Toast.LENGTH_SHORT).show();
        startImmediateSync();
    }

    synchronized private void startImmediateSync() {
        final Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        final OneTimeWorkRequest syncLocalData =
                new OneTimeWorkRequest
                        .Builder(SyncWorker.class)
                        .setConstraints(constraints)
                        .build();
        WorkManager.getInstance(getActivity()).enqueue(syncLocalData);

        WorkManager
                .getInstance(getActivity())
                .getWorkInfoByIdLiveData(syncLocalData.getId())
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null) {
                            if (workInfo.getState().isFinished()) {
                                LocalSalesRepo.getInstance().notifyAllLogDataUploaded();
                                Toast.makeText(getActivity(), "Uploading Done", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                });
    }

    private void intiViewModel() {
        OfflineDataViewModel offlineDataViewModel = new ViewModelProvider(this).get(OfflineDataViewModel.class);
        offlineDataViewModel.getLocalLogLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, List<DailyLogEntry>>>() {
            @Override
            public void onChanged(Map<String, List<DailyLogEntry>> localLogMap) {
                mBinding.setDatesLogEntriesMap(localLogMap);
            }
        });
    }

    private void initToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFragment();
            }
        });
    }

    private void hideFragment() {
        FragmentActivity host = getActivity();
        if (host != null) {
            host.getSupportFragmentManager().beginTransaction().hide(this).commit();
        }
    }

    private void finish() {
        FragmentActivity host = getActivity();
        if (host != null) {
            host.getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}
