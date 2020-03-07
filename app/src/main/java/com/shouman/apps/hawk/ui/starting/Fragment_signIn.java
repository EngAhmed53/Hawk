package com.shouman.apps.hawk.ui.starting;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.shouman.apps.hawk.databinding.FragmnetSignInBinding;


public class Fragment_signIn extends Fragment {

    public FragmnetSignInBinding mBinding;

    public Fragment_signIn() {

    }

    public static Fragment_signIn getInstance() {
        return new Fragment_signIn();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmnetSignInBinding.inflate(inflater);
        return mBinding.getRoot();
    }

}
