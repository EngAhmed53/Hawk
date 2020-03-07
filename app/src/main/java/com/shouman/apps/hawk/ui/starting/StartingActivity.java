package com.shouman.apps.hawk.ui.starting;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.databinding.ActivityStartingBinding;
import com.shouman.apps.hawk.model.Company;

public class StartingActivity extends AppCompatActivity {

    private static final String TAG = "StartingActivity";
    Company company = new Company();

    public ActivityStartingBinding mBinding;

    private FragmentManager fragmentManager;

    //public static int SPLASH_SCREEN_TIMEOUT = 3500;

    private Fragment_entry_screen fragment_entry_screen = Fragment_entry_screen.getInstance();

    private Fragment_signIn fragment_signIn = Fragment_signIn.getInstance();

    private OnBackButtonPressed onBackButtonPressed;

    public static int count = 0;

    public interface OnBackButtonPressed {
        void doBack();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_starting);
        fragmentManager = getSupportFragmentManager();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    Log.e(TAG, "onAuthStateChanged: null");
                    showEntryFragment();
                } else {
                    Log.e(TAG, "onAuthStateChanged: not null");

                    //showEntryFragment();
                }

            }
        });

    }

    private void showMainActivity() {
        //check if the user if company type or sales member
        //to navigate him to the company ui or sales member ui
    }


    private void showEntryFragment() {

        fragmentManager
                .beginTransaction()
                .add(R.id.starting_container, fragment_entry_screen, "fragment_entry_screen")
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
        if (count > 0) {
            Fragment_signUp fragment_signUp = (Fragment_signUp) fragmentManager.findFragmentByTag("sign_up_fragment");
            if (fragment_signUp != null && fragment_signUp.isVisible()) {
                Log.e(TAG, "onBackPressed here fragment != null ");
                try {
                    onBackButtonPressed = (OnBackButtonPressed) fragment_signUp;
                    if (fragment_signUp.count > 0) {
                        Log.e(TAG, "onBackPressed here fragment.count > 0");
                        onBackButtonPressed.doBack();
                    } else {
                        Log.e(TAG, "onBackPressed: " + fragment_signUp.count);
                        fragmentManager.beginTransaction().show(fragment_entry_screen).commit();
                        super.onBackPressed();
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

            } else {
                fragmentManager.beginTransaction().show(fragment_entry_screen).commit();
                super.onBackPressed();
            }

        } else {
            super.onBackPressed();
        }
    }
}
