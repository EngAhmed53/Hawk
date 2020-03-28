package com.shouman.apps.hawk.ui.auth;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.ActivityStartingBinding;
import com.shouman.apps.hawk.model.User;
import com.shouman.apps.hawk.preferences.UserPreference;
import com.shouman.apps.hawk.ui.main.companyUi.MainActivity;
import com.shouman.apps.hawk.ui.main.salesMemberUI.home.homeFragment.Main2Activity;

public class StartingActivity extends AppCompatActivity {

    private static final String TAG = "StartingActivity";

    public ActivityStartingBinding mBinding;

    public FragmentManager fragmentManager;

    private AuthViewModel authViewModel;

    private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                //new user or user wes logged out
                showEntryFragment();

            } else {
                Log.e(TAG, "onAuthStateChanged: " + "user is not null");
                //reload firebase user
                firebaseUser.reload();
                authViewModel.setupMediatorLiveData();


                authViewModel.getUserMediatorLiveData().observe(StartingActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user.getUt() == null) {

                            showSelectUserTypeFragment();
                        } else {
                            showMainActivity(user);
                        }
                    }
                });
            }
        }
    };

    //public static int SPLASH_SCREEN_TIMEOUT = 3500;

    private Fragment_entry_screen fragment_entry_screen = Fragment_entry_screen.getInstance();
    private Fragment_select_user_type fragment_select_user_type = Fragment_select_user_type.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_starting);
        fragmentManager = getSupportFragmentManager();
        firebaseAuth = FirebaseAuth.getInstance();

        initViewModel();


        //check if the user is exist or not
        firebaseAuth.addAuthStateListener(firebaseAuthListener);


    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(StartingActivity.this).get(AuthViewModel.class);
    }


    public void showSignUpFragment(String email) {
        Fragment_signUp fragment_signUp = Fragment_signUp.getInstance(email);
        if (fragment_signUp.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_signUp).commit();
            return;
        }
        fragmentManager
                .beginTransaction()
                .replace(R.id.starting_container, fragment_signUp, "fragment_sign_up")
                .commit();

    }


    public void showSelectUserTypeFragment() {
        if (fragment_select_user_type.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_select_user_type);
            return;
        }
        fragmentManager.popBackStack("entry", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager
                .beginTransaction()
                .replace(R.id.starting_container, fragment_select_user_type, "fragment_select_user_type")
                .commit();
    }

    private void showMainActivity(User user) {
        //check if the user if company type or sales member
        //to navigate him to the company ui or sales member ui
        String userType = user.getUt();
        Intent intent = null;

        if (userType.equals("company_account")) {
            intent = new Intent(StartingActivity.this, MainActivity.class);
            UserPreference.setCompanyUID(this, user.getCuid());
            UserPreference.setCompanyName(this, user.getCn());
        } else if (userType.equals("sales_account")) {
            intent = new Intent(StartingActivity.this, Main2Activity.class);
            UserPreference.setBranchUID(this, user.getBuid());
            UserPreference.setCompanyUID(this, user.getCuid());
        }
        if (intent != null)
            startActivity(intent);
        finish();
    }


    public void showEntryFragment() {
        if (fragment_entry_screen.isAdded()) {
            fragmentManager
                    .beginTransaction()
                    .show(fragment_entry_screen)
                    .commit();
            return;
        }
        fragmentManager
                .beginTransaction()
                .replace(R.id.starting_container, fragment_entry_screen, "fragment_entry_screen")
                .addToBackStack("entry")
                .commit();


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fragmentManager
//                        .beginTransaction()
//                        .addSharedElement(fragment_entry_screen.mBinding.appLogoImage, "logo_image")
//                        .replace(R.id.starting_container, fragment_signIn)
//                        .commit();
//            }
//        }, SPLASH_SCREEN_TIMEOUT);
    }

    @Override
    public void onBackPressed() {
        Fragment_entry_screen fragment_entry_screen = (Fragment_entry_screen) fragmentManager.findFragmentByTag("fragment_entry_screen");

        if (fragment_entry_screen != null && fragment_entry_screen.isVisible()) {
            finish();
        }

        super.onBackPressed();

    }

    //@Override
    //public void onBackPressed() {
    //if (count > 0) {
//            Fragment_signUp fragment_signUp = (Fragment_signUp) fragmentManager.findFragmentByTag("sign_up_fragment");
//            if (fragment_signUp != null && fragment_signUp.isVisible()) {
//                Log.e(TAG, "onBackPressed here fragment != null ");
//                try {
//                    onBackButtonPressed = (OnBackButtonPressed) fragment_signUp;
//                    if (fragment_signUp.count > 0) {
//                        Log.e(TAG, "onBackPressed here fragment.count > 0");
//                        onBackButtonPressed.doBack();
//                    } else {
//                        Log.e(TAG, "onBackPressed: " + fragment_signUp.count);
//                        fragmentManager.beginTransaction().show(fragment_entry_screen).commit();
//                        super.onBackPressed();
//                    }
//                } catch (ClassCastException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                fragmentManager.beginTransaction().show(fragment_entry_screen).commit();
//                super.onBackPressed();
//            }
//
//        } else {
    //    super.onBackPressed();
    //}

    public void showSignInFragment(String email) {
        Fragment_signIn fragment_signIn = Fragment_signIn.getInstance(email);
        if (fragment_signIn.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_signIn).commit();
            return;
        }
        fragmentManager
                .beginTransaction()
                .add(R.id.starting_container, fragment_signIn, "fragment_entry_screen")
                .commit();
    }

    public void showForgetPasswordFragment(String email) {
        Fragment_Forget_Password forget_password = Fragment_Forget_Password.getInstance(email);
        if (forget_password.isAdded()) {
            fragmentManager.beginTransaction().show(forget_password).commit();
            return;
        }
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.starting_container, forget_password, "fragment_forget_password")
                .addToBackStack(null)
                .commit();
    }


    public void requestCameraPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                showScanFragment();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(StartingActivity.this, "Permission needed to open camera", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
    }

    public void showScanFragment() {
        Fragment_Scan_QR scanQr = Fragment_Scan_QR.getInstance();
        fragmentManager
                .beginTransaction()
                .addToBackStack("qr_scan")
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.starting_container, scanQr, "fragment_scan_qr")
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
