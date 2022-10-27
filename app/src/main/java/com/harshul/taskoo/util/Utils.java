package com.harshul.taskoo.util;

import static android.content.Context.MODE_PRIVATE;
import static com.harshul.taskoo.util.Constants.MESSAGE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.harshul.taskoo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    private static Utils utils;

    private Utils() {
    }

    public static Utils getInstance() {
        if (utils == null) {
            utils = new Utils();
        }
        return utils;
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern(pattern);
        return simpleDateFormat.format(date);
    }

    public static boolean isToday(String dateToCheck) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return today.equals(dateToCheck);
    }

    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view.getRootView(), InputMethodManager.SHOW_IMPLICIT);
        view.requestFocus();
    }

    public static void strikeThrough(TextView textView) {
        if (!textView.getPaint().isStrikeThruText()) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public static void bottomBarColor(Activity activity) {
        activity.getWindow().setNavigationBarColor(activity.getColor(R.color.main_color));
    }

    public static long getDate(Date date) {
        String reqDate = formatDate(date, "dd/M/yyyy");
        String[] parts = reqDate.split("/");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1;
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTimeInMillis();
    }

    public static SpannableString textColor(Context context, String text, int textColor, int start, int end) {
        ForegroundColorSpan color = new ForegroundColorSpan(context.getColor(textColor));
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(color, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void saveToSharedPreference(Context context, String key, Object value) {
        String localValue;
        if (value != null && context != null) {
            localValue = value.toString();
        } else {
            localValue = null;
        }
        assert context != null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(MESSAGE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, localValue);
        editor.apply();
    }

    public String getFromSharedPreference(Context context, String key) {
        if (context == null) {
            return null;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(MESSAGE, MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }


}
