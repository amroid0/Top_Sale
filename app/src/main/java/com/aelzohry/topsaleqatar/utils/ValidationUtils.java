package com.aelzohry.topsaleqatar.utils;

import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

public class ValidationUtils {

    public static boolean isEmptyEditText(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    public static boolean isEmptyText(TextView editText) {
        return editText.getText().toString().trim().isEmpty();
    }


    public static void errorInput(EditText editText, String msg) {
        editText.requestFocus();
        editText.setError(msg);
    }

//    public static String getText(EditText editText) {
//        return ToolUtils.convertToEngNo(editText.getText().toString().trim()) + "";
//    }

    public static String getText(TextView editText) {
        return editText.getText().toString().trim();
    }

    public static boolean isValidEmailAddress(EditText editText) {
        return Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches();
    }

    public static boolean isValidPassword(EditText editText) {
        return editText.getText().toString().trim().length() >= 6;
    }

    public static boolean isInValidPassword(EditText editText) {
        return editText.getText().toString().trim().length() < 6;
    }

    public static boolean isInValidMobile(EditText editText) {
        return editText.getText().toString().trim().length() < 8;
    }

    public static boolean isValidMobile(EditText editText) {
        if (editText.getText().toString().trim().length() == 7)
            return true;
//        if ((editText.getText().toString().trim().length() == 10 && editText.getText().toString().trim().startsWith("05")))
//            return true;
//        else if ((editText.getText().toString().trim().length() == 9) && editText.getText().toString().trim().startsWith("5"))
//            return true;
        else return false;
    }


    public static boolean isPasswordsMatch(EditText editText1, EditText editText2) {
        return editText1.getText().toString().trim().equals(editText2.getText().toString().trim());
    }


}
