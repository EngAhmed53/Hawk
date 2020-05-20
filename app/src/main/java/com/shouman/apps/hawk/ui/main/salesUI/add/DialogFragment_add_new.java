package com.shouman.apps.hawk.ui.main.salesUI.add;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.DialogFragmentAddNewBinding;

public class DialogFragment_add_new extends DialogFragment {

    private DialogFragmentAddNewBinding mBinding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DialogFragmentAddNewBinding.inflate(inflater);

        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }

        mBinding.getRoot().setClipToOutline(true);


        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.newCustomer.setOnClickListener(v -> {
            NavDirections toAddNewCustomer = DialogFragment_add_newDirections.actionDialogFragmentAddNewToFragmentAddNewCustomer();
            Navigation.findNavController(requireActivity(), R.id.sales_container).navigate(toAddNewCustomer);
        });

        mBinding.newVisit.setOnClickListener(v -> {

        });
    }
}
