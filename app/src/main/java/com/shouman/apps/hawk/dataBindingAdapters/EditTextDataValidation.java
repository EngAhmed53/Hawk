package com.shouman.apps.hawk.dataBindingAdapters;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;
import com.shouman.apps.hawk.R;

public class EditTextDataValidation {


    @BindingAdapter("emailValidation")
    public static void emailValidation(final TextInputLayout inputLayout, String email) {
        final Resources resources = inputLayout.getContext().getResources();

        if (inputLayout.getEditText().getTag() == null) {

            inputLayout.getEditText().setTag("email_edit_validation");
            inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String email = s.toString();
                    if (TextUtils.isEmpty(email)) {
                        inputLayout.setError(resources.getString(R.string.blank_email_error));
                    } else if (email.length() < 10) {
                        inputLayout.setError(resources.getString(R.string.valid_email_address_error));
                    } else if (!email.contains("@") || !email.contains(".com")) {
                        inputLayout.setError(resources.getString(R.string.valid_email_address_error));
                    } else {
                        inputLayout.setError(null);
                    }
                }
            });
        }
    }


    @BindingAdapter("passwordValidation")
    public static void passwordValidation(final TextInputLayout inputLayout, String password) {
        final Resources resources = inputLayout.getContext().getResources();

        if (null == inputLayout.getEditText().getTag()) {
            inputLayout.getEditText().setTag("password_edit_validation");
            inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String password = s.toString();
                    if (TextUtils.isEmpty(password)) {
                        inputLayout.setError(resources.getString(R.string.blank_password_error));
                    } else if (password.length() < 6) {
                        inputLayout.setError(resources.getString(R.string.mini_chars_password_error));
                    } else {
                        inputLayout.setError(null);
                    }
                }
            });
        }


    }


    @BindingAdapter("userNameValidation")
    public static void userNameValidation(final TextInputLayout inputLayout, String userName) {
        final Resources resources = inputLayout.getContext().getResources();

        if (inputLayout.getEditText().getTag() == null) {
            inputLayout.getEditText().setTag("user_name_edit_text");
            inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String userName = s.toString();
                    if (TextUtils.isEmpty(userName)) {
                        inputLayout.setError(resources.getString(R.string.blank_user_name_error));
                        return;
                    } else {
                        inputLayout.setError(null);
                    }

                    for (char c : s.toString().toCharArray()) {
                        if (!Character.isLetterOrDigit(c) && c != ' ') {
                            inputLayout.setError(resources.getString(R.string.spacial_chars_error));
                            break;
                        } else {
                            inputLayout.setError(null);
                        }
                    }
                }
            });
            System.out.println("added");
        }
    }


    @BindingAdapter("companyNameValidation")
    public static void companyNameValidation(final TextInputLayout inputLayout, String companyName) {
        final Resources resources = inputLayout.getContext().getResources();

        if (inputLayout.getEditText().getTag() == null) {
            inputLayout.getEditText().setTag("company_name_edit_validation");
            inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String companyName = s.toString();
                    if (TextUtils.isEmpty(companyName)) {
                        inputLayout.setError(resources.getString(R.string.blank_compay_name_error));
                    } else {
                        inputLayout.setError(null);
                    }
                }
            });
        }
    }


    @BindingAdapter("companyIdValidation")
    public static void companyIdValidation(final TextInputLayout inputLayout, String companyId) {
        final Resources resources = inputLayout.getContext().getResources();

        if (inputLayout.getEditText().getTag() == null) {
            inputLayout.getEditText().setTag("companyIdValidation");
            inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String companyId = s.toString();
                    if (TextUtils.isEmpty(companyId)) {
                        inputLayout.setError(resources.getString(R.string.blank_company_id_error));
                    } else {
                        inputLayout.setError(null);
                    }

                    for (char c : companyId.toCharArray()) {
                        if (!Character.isLetterOrDigit(c)) {
                            inputLayout.setError(resources.getString(R.string.com_id_spacial_chars_error));
                        } else {
                            inputLayout.setError(null);
                        }
                    }
                }
            });
        }
    }

    @BindingAdapter("branchPinCodeValidation")
    public static void branchPinCodeValidation(final TextInputLayout inputLayout, String pinCode) {
        final Resources resources = inputLayout.getContext().getResources();

        if (inputLayout.getEditText().getTag() == null) {
            inputLayout.getEditText().setTag("branchPinCodeValidation");
            inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String companyId = s.toString();
                    if (TextUtils.isEmpty(companyId)) {
                        inputLayout.setError(resources.getString(R.string.blank_pib_code_error));
                    } else {
                        inputLayout.setError(null);
                    }

                    for (char c : companyId.toCharArray()) {
                        if (!Character.isDigit(c)) {
                            inputLayout.setError(resources.getString(R.string.valid_pin_code_error));
                        } else {
                            inputLayout.setError(null);
                        }
                    }
                }
            });
        }
    }
}
