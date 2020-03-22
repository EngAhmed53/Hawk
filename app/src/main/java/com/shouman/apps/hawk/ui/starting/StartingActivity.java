package com.shouman.apps.hawk.ui.starting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.ActivityStartingBinding;
import com.shouman.apps.hawk.model.UserMap;
import com.shouman.apps.hawk.preferences.UserPreference;
import com.shouman.apps.hawk.ui.main.companyUi.MainActivity;
import com.shouman.apps.hawk.ui.main.salesMemberUI.Main2Activity;

public class StartingActivity extends AppCompatActivity {

    private static final String TAG = "StartingActivity";
    private static final String USER_EMAIL = "user_email";

    public ActivityStartingBinding mBinding;

    private FragmentManager fragmentManager;

    //public static int SPLASH_SCREEN_TIMEOUT = 3500;

    private Fragment_verify_email fragment_verify_email = Fragment_verify_email.getInstance();
    private Fragment_entry_screen fragment_entry_screen = Fragment_entry_screen.getInstance();
    private Fragment_select_user_type fragment_select_user_type = Fragment_select_user_type.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_starting);
        fragmentManager = getSupportFragmentManager();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //check if the user is exist or not
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            //new user or user wes logged out
            showEntryFragment();

        } else {
            //reload firebase user
            firebaseUser.reload();

            // user is already exist and signed in
            //check if the user email is verified
            Common.userMap = new UserMap();
            Common.userMap.setUserUID(UserPreference.getUserUID(StartingActivity.this));

            //check if the user is facebook user
            boolean isFacebook = false;
            for (UserInfo user : firebaseUser.getProviderData()) {
                if (user.getProviderId().equals("facebook.com")) {
                    isFacebook = true;
                }
            }

            if (isFacebook) {

                // user email is verified
                Common.userMap.setVerified(true);

                //check if the user type is defined
                if (!UserPreference.isUserInfoSetted(StartingActivity.this)) {
                    //user type is not defined

                    showSelectUserTypeFragment();
                } else {
                    //user is logged in and his email is verified and his type is selected and all is set
                    //run the main activity depend on his type (company or sales member)

                    //set user type
                    String userType = UserPreference.getUserType(StartingActivity.this);
                    Common.userMap.setPath(userType);

                    //set user branchUID if the user is sales_member
                    if (userType.equals("sales_members")) {
                        Common.userMap.setBranchUID(UserPreference.getBranchUID(StartingActivity.this));
                    } else {
                        Common.userMap.setBranchUID(null);
                    }
                    //show the main activity
                    showMainActivity();
                }


            } else {
                // user is not a facebook so we need to check if he is verified or not
                if (firebaseUser.isEmailVerified()) {
                    // user email is verified
                    Common.userMap.setVerified(true);

                    //check if the user type is defined
                    if (!UserPreference.isUserInfoSetted(StartingActivity.this)) {
                        //user type is not defined

                        showSelectUserTypeFragment();
                    } else {
                        //user is logged in and his email is verified and his type is selected and all is set
                        //run the main activity depend on his type (company or sales member)

                        //set user type
                        String userType = UserPreference.getUserType(StartingActivity.this);
                        Common.userMap.setPath(userType);

                        //set user branchUID if the user is sales_member
                        if (userType.equals("sales_members")) {
                            Common.userMap.setBranchUID(UserPreference.getBranchUID(StartingActivity.this));
                        } else {
                            Common.userMap.setBranchUID(null);
                        }
                        //show the main activity
                        showMainActivity();
                    }

                } else {
                    //user email is not verified
                    showVerifyEmailFragment();
                }
            }
        }
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

    public void showVerifyEmailFragment() {
        if (fragment_verify_email.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_verify_email).commit();
            return;
        }
        fragmentManager.popBackStack("entry", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager
                .beginTransaction()
                .replace(R.id.starting_container, fragment_verify_email, "fragment_verify_email")
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

    private void showMainActivity() {
        //check if the user if company type or sales member
        //to navigate him to the company ui or sales member ui
        String userType = UserPreference.getUserType(StartingActivity.this);
        Intent intent = null;
        if (userType.equals("companies")) intent = new Intent(StartingActivity.this, MainActivity.class);
        else if (userType.equals("sales_members")) intent = new Intent(StartingActivity.this, Main2Activity.class);
        if (intent != null)
        startActivity(intent);
        finish();
    }


    public void showEntryFragment() {
        if (fragment_entry_screen.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_entry_screen).commit();
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
}
