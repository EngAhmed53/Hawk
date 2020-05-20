package com.shouman.apps.hawk.ui.main.companyUI.customer.visitsLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shouman.apps.hawk.adapters.VisitsRecyclerViewAdapter;
import com.shouman.apps.hawk.data.model.Visit;
import com.shouman.apps.hawk.databinding.DialogFragmentVisitLogBinding;

import java.util.Arrays;
import java.util.List;

public class DialogFragment_visits_Log extends DialogFragment {

    private DialogFragmentVisitLogBinding mBinding;
    private List<Visit> visitList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        visitList = Arrays.asList(DialogFragment_visits_LogArgs.fromBundle(getArguments()).getVisitList());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DialogFragmentVisitLogBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dismissBtn();
        initRecView();
    }

    private void initRecView() {
        VisitsRecyclerViewAdapter viewAdapter = new VisitsRecyclerViewAdapter(requireContext());
        viewAdapter.setVisitsLog(visitList);
        mBinding.recVisits.setAdapter(viewAdapter);
        mBinding.recVisits.setHasFixedSize(true);
    }

    private void dismissBtn() {
        mBinding.toolbar.setNavigationOnClickListener(v -> dismiss());
    }
}
