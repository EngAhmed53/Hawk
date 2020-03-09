package com.shouman.apps.hawk.ui.starting;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.common.Common;
import com.shouman.apps.hawk.databinding.ActivityStartingBinding;
import com.shouman.apps.hawk.model.UserMap;
import com.shouman.apps.hawk.preferences.UserPreference;

public class StartingActivity extends AppCompatActivity {

    private static final String TAG = "StartingActivity";

    public ActivityStartingBinding mBinding;

    private FragmentManager fragmentManager;

    //public static int SPLASH_SCREEN_TIMEOUT = 3500;

    private Fragment_entry_screen fragment_entry_screen = Fragment_entry_screen.getInstance();

    private Fragment_signIn fragment_signIn = Fragment_signIn.getInstance();
    private Fragment_signUp fragment_signUp = Fragment_signUp.getInstance();


    private Fragment_verify_email fragment_verify_email = Fragment_verify_email.getInstance();

    private Fragment_select_user_type fragment_select_user_type = Fragment_select_user_type.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_starting);
        fragmentManager = getSupportFragmentManager();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //check if the user is exist or not
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            //new user or user wes logged out
            showEntryFragment();

        } else {
            // user is already exist and signed in
            //check if the user email is verified
            Common.userMap = new UserMap();
            Common.userMap.setUserUID(UserPreference.getUserUID(StartingActivity.this));
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
                    showMainActivity();
                }

            } else {
                //user email is not verified
                showVerifyEmailFragment();
            }
        }
    }


    public void showSignUpFragment() {
        Fragment_signUp f = (Fragment_signUp) fragmentManager.findFragmentByTag("fragment_sign_up");
        if (f == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.starting_container, fragment_signUp, "fragment_sign_up")
                    .commit();
        }
    }

    public void showVerifyEmailFragment() {
        Fragment_verify_email f = (Fragment_verify_email) fragmentManager.findFragmentByTag("fragment_verify_email");
        if (f == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.starting_container, fragment_verify_email, "fragment_verify_email")
                    .commit();
        }

    }

    public void showSelectUserTypeFragment() {
        Fragment_select_user_type f = (Fragment_select_user_type) fragmentManager.findFragmentByTag("fragment_select_user_type");
        if (f == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.starting_container, fragment_select_user_type, "fragment_select_user_type")
                    .commit();
        }
    }

    private void showMainActivity() {
        //check if the user if company type or sales member
        //to navigate him to the company ui or sales member ui
        Toast.makeText(this, "show main activity", Toast.LENGTH_SHORT).show();
    }


    public void showEntryFragment() {
        Fragment_entry_screen f = (Fragment_entry_screen) fragmentManager.findFragmentByTag("fragment_entry_screen");
        if (f == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.starting_container, fragment_entry_screen, "fragment_entry_screen")
                    .commit();
        }


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
        super.onBackPressed();
    }
}
