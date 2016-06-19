package com.krystianklimek.todolist.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by: Krystian Klimek
 * Date: 19.06.2016.
 */

public class ActivityUtils {
    public static void replaceActivity(Activity activity, Class cls) {
        Context context = activity.getApplicationContext();
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        overridePendingTransition(activity);
    }

    public static void replaceActivity(Activity activity, Class cls, String key_id, int id) {
        Context context = activity.getApplicationContext();
        Intent intent = new Intent(context, cls);
        intent.putExtra(key_id, id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        overridePendingTransition(activity);
    }

    public static void overridePendingTransition(Activity activity) {
        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(View view, Context context) {
        // Check if no view has focus
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
