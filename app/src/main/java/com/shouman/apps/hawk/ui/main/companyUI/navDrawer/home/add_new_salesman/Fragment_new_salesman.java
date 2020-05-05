package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home.add_new_salesman;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.collect.BiMap;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.adapters.BranchesDropDownArrayAdapter;
import com.shouman.apps.hawk.databinding.FragmentAddNewSalesmanBinding;
import com.shouman.apps.hawk.preferences.UserPreference;
import com.shouman.apps.hawk.ui.main.companyUI.navDrawer.MainActivity;
import com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_new_salesman extends Fragment {

    private static final String TAG = "Fragment_new_salesman";
    private FragmentAddNewSalesmanBinding mBinding;
    private TextWatcher salesNameTextWatcher;
    private boolean nameSelected;
    private boolean branchSelected;
    private HomeViewModel homeViewModel;
    private BranchesDropDownArrayAdapter branchesAdapter;
    private String branchUID;
    private BiMap<String, String> allBranchesDetails;
    private List<String> allBranchesNames;


    public Fragment_new_salesman() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animateToolbarZDown();

        allBranchesNames = new ArrayList<>();

        initViewModel();

        initTextWatcher();
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    private void initTextWatcher() {
        salesNameTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nameSelected = !s.toString().isEmpty();
                mBinding.btnSendInvitation.setEnabled(nameSelected && branchSelected);
            }
        };
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentAddNewSalesmanBinding.inflate(inflater);

        mBinding.btnSendInvitation.setOnClickListener(v -> {
            Uri deepLink = buildDeepLink();

            Uri dynamicLink = buildDynamicLink(deepLink);

            shareDynamicLink(dynamicLink.toString());
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.filledExposedDropdown.setOnItemClickListener((parent, v, position, id) -> {
            branchSelected = true;
            mBinding.btnSendInvitation.setEnabled(nameSelected);
            String selectedName = allBranchesNames.get(position);
            branchUID = allBranchesDetails.inverse().get(selectedName);
            mBinding.filledExposedDropdown.setText(selectedName);
        });

        homeViewModel.getAllBranchesDetails().observe(getViewLifecycleOwner(), (branchesDetails) -> {
            allBranchesDetails = branchesDetails;
            allBranchesNames = new ArrayList<>(branchesDetails.values());
            branchesAdapter = new BranchesDropDownArrayAdapter(requireContext(), allBranchesNames);
            mBinding.filledExposedDropdown.setAdapter(branchesAdapter);
            branchesAdapter.notifyDataSetChanged();
        });
    }

    private Uri buildDeepLink() {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority("hawk.sales.com")
                .appendPath(UserPreference.getCompanyUID(requireContext()))
                .appendPath(branchUID)
                .appendPath(mBinding.edtSalesName.getEditableText().toString());

        return builder.build();
    }

    private Uri buildDynamicLink(Uri deepLink) {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(deepLink.toString()))
                .setDomainUriPrefix("https://hawk.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink();

        return dynamicLink.getUri();
    }

    private void shareDynamicLink(String deepLink) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Deep Link");
        intent.putExtra(Intent.EXTRA_TEXT, deepLink);

        Log.e(TAG, "shareDynamicLink: " + deepLink);

        startActivity(intent);
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
        if (salesNameTextWatcher == null) {
            initTextWatcher();
        }
        mBinding.edtSalesName.addTextChangedListener(salesNameTextWatcher);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (salesNameTextWatcher != null) {
            mBinding.edtSalesName.removeTextChangedListener(salesNameTextWatcher);
            salesNameTextWatcher = null;
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        animateToolbarZUp();
        super.onDestroy();
    }
}
