package com.shouman.apps.hawk.ui.auth;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.FragmentEntryScreenBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_entry_screen extends Fragment {
    private static final String TAG = "Fragment_entry_screen";
    public FragmentEntryScreenBinding mBinding;


    public Fragment_entry_screen() {
        // Required empty public constructor
    }

    public static Fragment_entry_screen getInstance() {
        return new Fragment_entry_screen();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition customTransition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.logo_linear_transition);
        setSharedElementEnterTransition(customTransition);
//        Fade fade = new Fade();
//        fade.setDuration(1000);
//        setEnterTransition(fade);
//        setExitTransition(fade);
        setSharedElementReturnTransition(customTransition);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEntryScreenBinding.inflate(inflater);
        mBinding.btnAlreadyHaveAccount.setOnClickListener(this::startSignInFragment);

        mBinding.btnCreateAccount.setOnClickListener(this::startSignUpFragment);
        return mBinding.getRoot();
    }

    private void startSignUpFragment(View v) {

        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(mBinding.appLogoImage, getString(R.string.logo_image))
                .build();

        Navigation.findNavController(v).navigate(R.id.action_fragment_entry_screen_to_fragment_signUp2, null, null, extras);


//        Fragment_signUp fragment_signUp = Fragment_signUp.getInstance();
//        Transition customTransition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.logo_curve_transition);
//
//        Fade fade = new Fade();
//        fade.setDuration(1000);
//
//        fragment_signUp.setSharedElementEnterTransition(customTransition);
//        fragment_signUp.setEnterTransition(fade);
//        fragment_signUp.setExitTransition(fade);
//        fragment_signUp.setSharedElementReturnTransition(customTransition);
//        fragmentManager
//                .beginTransaction()
//                .addSharedElement(mBinding.appLogoImage, "logo_image")
//                .replace(R.id.starting_container, fragment_signUp, "sign_up_fragment")
//                .addToBackStack("sign_up_fragment")
//                .commit();
    }

    private void startSignInFragment(View v) {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(mBinding.appLogoImage, getString(R.string.logo_image))
                .build();

        Navigation.findNavController(v).navigate(R.id.action_fragment_entry_screen_to_fragment_signIn2, null, null, extras);
//        Fragment_signIn fragment_signIn = Fragment_signIn.getInstance();
//        Transition customTransition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.logo_curve_transition);
//
//        Fade fade = new Fade();
//        fade.setDuration(1000);
//
//        fragment_signIn.setSharedElementEnterTransition(customTransition);
//        fragment_signIn.setEnterTransition(fade);
//        fragment_signIn.setExitTransition(fade);
//        fragment_signIn.setSharedElementReturnTransition(customTransition);
//        fragmentManager
//                .beginTransaction()
//                .addSharedElement(mBinding.appLogoImage, "logo_image")
//                .replace(R.id.starting_container, fragment_signIn, "sign_in_fragment")
//                .addToBackStack("sign_in_fragment")
//                .commit();
    }
}
