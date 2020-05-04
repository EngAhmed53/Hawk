package com.shouman.apps.hawk.ui.auth;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.shouman.apps.hawk.R;

import java.util.Arrays;

public class StartingActivity extends AppCompatActivity {

    private static final String TAG = "StartingActivity";
    public String[] dynamicLinkData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init dynamic link
        handelDynamicLink();

        DataBindingUtil.setContentView(this, R.layout.activity_starting);

        initViewModel();
    }

    private void handelDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                    }
                    if (deepLink != null) {
                        String path = deepLink.getPath();
                        if (path != null) {
                            dynamicLinkData = path.split("/");
                            Log.e(TAG, "handelDynamicLink: " + Arrays.toString(dynamicLinkData));
                        }
                    }
                })
                .addOnFailureListener(this, e -> Log.w(TAG, "getDynamicLink:onFailure", e));
    }

    private void initViewModel() {
        AuthViewModel authViewModel = new ViewModelProvider(StartingActivity.this).get(AuthViewModel.class);
        authViewModel.setupMediatorLiveData();
    }


//    public void showSignUpFragment(String email) {
//        Fragment_signUp fragment_signUp = Fragment_signUp.getInstance(email);
//        if (fragment_signUp.isAdded()) {
//            fragmentManager.beginTransaction().show(fragment_signUp).commit();
//            return;
//        }
//        fragmentManager
//                .beginTransaction()
//                .replace(R.id.starting_container, fragment_signUp, "fragment_sign_up")
//                .commit();
//    }
//
//
//    public void showSelectUserTypeFragment() {
//        if (fragment_select_user_type.isAdded()) {
//            fragmentManager.beginTransaction().show(fragment_select_user_type);
//            return;
//        }
//        fragmentManager.popBackStack("entry", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        fragmentManager
//                .beginTransaction()
//                .replace(R.id.starting_container, fragment_select_user_type, "fragment_select_user_type")
//                .commit();
//    }
//
//    private void showMainActivity(User user) {
//        //check if the user if company type or sales member
//        //to navigate him to the company ui or sales member ui
//        String userType = user.getUt();
//        Intent intent = null;
//
//        if (userType.equals("company_account")) {
//            intent = new Intent(StartingActivity.this, MainActivity.class);
//            UserPreference.setCompanyUID(this, user.getCuid());
//            UserPreference.setCompanyName(this, user.getCn());
//        } else if (userType.equals("sales_account")) {
//            intent = new Intent(StartingActivity.this, Main2Activity.class);
//
//            UserPreference.setBranchUID(this, user.getBuid());
//            UserPreference.setCompanyUID(this, user.getCuid());
//            UserPreference.setCompanyName(this, user.getCn());
//            UserPreference.setSalesmanStatus(this, user.isStatus());
//        }
//        if (intent != null)
//            startActivity(intent);
//        finish();
//    }
//
//
//    public void showEntryFragment() {
//        if (fragment_entry_screen.isAdded()) {
//            fragmentManager
//                    .beginTransaction()
//                    .show(fragment_entry_screen)
//                    .commit();
//            return;
//        }
//        fragmentManager
//                .beginTransaction()
//                .replace(R.id.starting_container, fragment_entry_screen, "fragment_entry_screen")
//                .addToBackStack("entry")
//                .commit();
//
//
////        new Handler().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                fragmentManager
////                        .beginTransaction()
////                        .addSharedElement(fragment_entry_screen.mBinding.appLogoImage, "logo_image")
////                        .replace(R.id.starting_container, fragment_signIn)
////                        .commit();
////            }
////        }, SPLASH_SCREEN_TIMEOUT);
//    }
//
//    @Override
//    public void onBackPressed() {
//        Fragment_entry_screen fragment_entry_screen = (Fragment_entry_screen) fragmentManager.findFragmentByTag("fragment_entry_screen");
//
//        if (fragment_entry_screen != null && fragment_entry_screen.isVisible()) {
//            finish();
//        }
//
//        super.onBackPressed();
//
//    }
//
//
//    public void showSignInFragment(String email) {
//        Fragment_signIn fragment_signIn = Fragment_signIn.getInstance(email);
//        if (fragment_signIn.isAdded()) {
//            fragmentManager.beginTransaction().show(fragment_signIn).commit();
//            return;
//        }
//        fragmentManager
//                .beginTransaction()
//                .add(R.id.starting_container, fragment_signIn, "fragment_entry_screen")
//                .commit();
//    }
//
//    public void showForgetPasswordFragment(String email) {
//        Fragment_Forget_Password forget_password = Fragment_Forget_Password.getInstance(email);
//        if (forget_password.isAdded()) {
//            fragmentManager.beginTransaction().show(forget_password).commit();
//            return;
//        }
//        fragmentManager
//                .beginTransaction()
//                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//                .add(R.id.starting_container, forget_password, "fragment_forget_password")
//                .addToBackStack(null)
//                .commit();
//    }


//    public void requestCameraPermission() {
//        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
//            @Override
//            public void onPermissionGranted(PermissionGrantedResponse response) {
//                showScanFragment();
//            }
//
//            @Override
//            public void onPermissionDenied(PermissionDeniedResponse response) {
//                Toast.makeText(StartingActivity.this, "Permission needed to open camera", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//            }
//        }).check();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
//    }
}
