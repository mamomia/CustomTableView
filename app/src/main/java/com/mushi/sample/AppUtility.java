package com.mushi.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public final class AppUtility {

    private static final String TAG = AppUtility.class.getSimpleName();

    private AppUtility() {
        throw new IllegalStateException("Instance not allowed");
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void setupUI(final Activity activity, View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyboard(activity, view);
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(activity, innerView);
            }
        }
    }

    public static void hideKeyboard(final Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static int parseInt(String value) {
        if (value.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static long parseLong(String value) {
        if (value.trim().isEmpty()) return 0;
        try {
            return Long.parseLong(value.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static double parseDouble(String value) {
        if (value.trim().isEmpty()) return 0;
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

}
