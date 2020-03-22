package com.shouman.apps.hawk.ui.starting;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.FragmentSelectUserTypeBinding;
import com.shouman.apps.hawk.model.Company;
import com.shouman.apps.hawk.model.SalesMan;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_select_user_type extends Fragment {

    private static final String TAG = "Fragment_select_user_ty";

    private static int SELECTED_POSITION = -1;

    private FragmentSelectUserTypeBinding mBinding;

    private ArrayAdapter<String> positionsAdapter;

    private FirebaseAuth auth;

    private FirebaseDatabase database;

    private DatabaseReference companiesReference;

    private DatabaseReference branchesReference;

    private DatabaseReference salesMembersReferences;

    private DatabaseReference userMapReference;


    public static Fragment_select_user_type getInstance() {
        return new Fragment_select_user_type();
    }

    public Fragment_select_user_type() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentSelectUserTypeBinding.inflate(inflater);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        companiesReference = database.getReference().child("companies");
        branchesReference = database.getReference().child("branches");

        salesMembersReferences = database.getReference().child("sales_members");

        final FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            String name = firebaseUser.getDisplayName();
            if (name != null) {
                mBinding.edtName.setText(name);
            }
        }
        //get user Email to get userMapUID
        String userEmail = UserPreference.getUserEmail(getContext());

        if (userEmail != null) {
            userMapReference = database.getReference().child("usersMap").child(Common.EmailToUID(userEmail));
        } else {
            Toast.makeText(getContext(), "email is empty", Toast.LENGTH_SHORT).show();
        }

        positionsAdapter = new ArrayAdapter<>(getContext(),
                R.layout.drop_down_list_item,
                Common.getAllPositions(getContext()));
        mBinding.filledExposedDropdown.setAdapter(positionsAdapter);

        mBinding.filledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                switch (position) {
                    case 0:
                        SELECTED_POSITION = Common.MANAGER_POSITION;
                        showCompanyFields();
                        mBinding.btnConfirmInfo.setEnabled(true);
                        break;
                    case 1:
                        SELECTED_POSITION = Common.SALES_POSITION;
                        showSalesFields();
                        mBinding.btnConfirmInfo.setEnabled(true);
                        break;
                }
            }
        });


        mBinding.btnConfirmInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SELECTED_POSITION == Common.MANAGER_POSITION) {
                    if (checkInputTextErrors(mBinding.nameInputLayout)
                            && checkInputTextErrors(mBinding.companyNameInputLayout)) {

                        //add company profile
                        Company company = new Company();
//
                        //set company info
                        company.setU(mBinding.edtName.getText().toString());
                        company.setC(mBinding.edtCompanyName.getText().toString());
                        company.setE(UserPreference.getUserEmail(getContext()));

                        //get the newUID
                        String newKey = firebaseUser.getUid();
                        companiesReference.child(newKey).setValue(company);


                        //continue setting the userMap
                        Common.userMap.setPath("companies");
                        Common.userMap.setUserUID(newKey);

                        //push the updated userMap to database
                        HashMap<String, Object> newValues = new HashMap<>();
                        newValues.put("userUID", newKey);
                        newValues.put("path", "companies");
                        if (userMapReference != null) {
                            userMapReference.updateChildren(newValues);
                        }


                        //update the user preference
                        UserPreference.setUserInfoSettedTrue(getContext());
                        UserPreference.setUserTypeTrue(getContext());
                        UserPreference.setUserType(getContext(), SELECTED_POSITION);
                        UserPreference.setUserUID(getContext(), newKey);

                    } else {
                        if (mBinding.nameInputLayout.getError() != null ||
                                mBinding.nameInputLayout.getEditText().getText().toString().isEmpty()) {
                            mBinding.nameInputLayout.requestFocus();

                        } else if (mBinding.companyNameInputLayout.getError() != null ||
                                mBinding.companyNameInputLayout.getEditText().getText().toString().isEmpty()) {
                            mBinding.companyNameInputLayout.requestFocus();

                        }
                    }
                } else if (SELECTED_POSITION == Common.SALES_POSITION) {
                    if (checkInputTextErrors(mBinding.nameInputLayout)
                            && checkInputTextErrors(mBinding.comIdTextField)) {

                        //add sales member profile
                        SalesMan salesMan = new SalesMan();

                        //set sales_member info
                        salesMan.setUserName(mBinding.edtName.getText().toString());
                        salesMan.setEmail(UserPreference.getUserEmail(getContext()));

                        //get the newUID
                        String newKey = firebaseUser.getUid();
                        salesMembersReferences.child(newKey).setValue(salesMan);

                        //continue setting the userMap
                        Common.userMap.setPath("sales_member");
                        Common.userMap.setUserUID(newKey);

                        //push the updated userMap to data base
                        String branchUID = mBinding.edtBranchId.getText().toString();
                        HashMap<String, Object> newValues = new HashMap<>();
                        newValues.put("userUID", newKey);
                        newValues.put("path", "sales_member");
                        newValues.put("branchUID", branchUID);
                        if (userMapReference != null) {
                            userMapReference.updateChildren(newValues);
                        }

                        //update user preferences
                        UserPreference.setUserInfoSettedTrue(getContext());
                        UserPreference.setUserTypeTrue(getContext());
                        UserPreference.setUserType(getContext(), SELECTED_POSITION);
                        UserPreference.setUserUID(getContext(), newKey);
                        UserPreference.setBranchUID(getContext(), branchUID);
                        branchesReference.child(branchUID).child("salesMemberList").child(newKey).setValue(salesMan.getUserName());

                    } else {
                        if (mBinding.nameInputLayout.getError() != null ||
                                mBinding.nameInputLayout.getEditText().getText().toString().isEmpty()) {
                            mBinding.nameInputLayout.requestFocus();

                        } else if (mBinding.comIdTextField.getEditText().getText().toString().isEmpty() ||
                                mBinding.comIdTextField.getError() != null) {
                            mBinding.comIdTextField.requestFocus();
                        }
                    }
                }
            }


        });

        return mBinding.getRoot();
    }

    private boolean checkInputTextErrors(TextInputLayout inputLayout) {
        String text = inputLayout.getEditText().getText().toString();
        if (text != null && !text.isEmpty()) {
            return inputLayout.getError() == null;
        }
        return false;
    }


    private void showSalesFields() {
        mBinding.companyInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.companyInfoLayout.setVisibility(View.GONE);

        mBinding.salesInfoLayout.setVisibility(View.VISIBLE);
        mBinding.salesInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));
    }

    private void showCompanyFields() {
        mBinding.salesInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_and_fade_animator));
        mBinding.salesInfoLayout.setVisibility(View.GONE);

        mBinding.companyInfoLayout.setVisibility(View.VISIBLE);
        mBinding.companyInfoLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation));

    }


}
