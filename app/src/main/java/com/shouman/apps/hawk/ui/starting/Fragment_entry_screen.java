package com.shouman.apps.hawk.ui.starting;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentEntryScreenBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_entry_screen extends Fragment {

    public FragmentEntryScreenBinding mBinding;
    private StartingActivity host;
    private FragmentManager fragmentManager;

    public Fragment_entry_screen() {
        // Required empty public constructor
    }

    public static Fragment_entry_screen getInstance() {
        return new Fragment_entry_screen();

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEntryScreenBinding.inflate(inflater);
        host = (StartingActivity) getActivity();
        if (host != null) {
            fragmentManager = host.getSupportFragmentManager();
        }

        mBinding.btnAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInFragment();
            }
        });

        mBinding.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpFragment();
            }
        });
        return mBinding.getRoot();
    }

    private void startSignUpFragment() {
        Fragment_signUp fragment_signUp = Fragment_signUp.getInstance();
        Transition customTransition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.logo_curve_transition);

        Fade fade = new Fade();
        fade.setDuration(1000);

        fragment_signUp.setSharedElementEnterTransition(customTransition);
        fragment_signUp.setEnterTransition(fade);
        fragment_signUp.setExitTransition(fade);
        fragment_signUp.setSharedElementReturnTransition(customTransition);
        fragmentManager
                .beginTransaction()
                .addSharedElement(mBinding.appLogoImage, "logo_image")
                .addToBackStack("sign_up_fragment")
                .replace(R.id.starting_container, fragment_signUp, "sign_up_fragment")
                .commit();
    }

    private void startSignInFragment() {
        Fragment_signIn fragment_signIn = Fragment_signIn.getInstance();
        Transition customTransition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.logo_curve_transition);

        Fade fade = new Fade();
        fade.setDuration(1000);

        fragment_signIn.setSharedElementEnterTransition(customTransition);
        fragment_signIn.setEnterTransition(fade);
        fragment_signIn.setExitTransition(fade);
        fragment_signIn.setSharedElementReturnTransition(customTransition);
        fragmentManager
                .beginTransaction()
                .addSharedElement(mBinding.appLogoImage, "logo_image")
                .addToBackStack("sign_in_fragment")
                .replace(R.id.starting_container, fragment_signIn, "sign_in_fragment")
                .commit();
    }
}
