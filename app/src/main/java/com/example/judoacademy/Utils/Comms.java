package com.example.judoacademy.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;

public class Comms {

    public static final String name = "name";
    public static final String age = "age";
    public static final String father = "father";
    public static final String gender = "gender";
    public static final String height = "height";
    public static final String weight = "weight";
    public static final String date = "date";
    public static final String time = "time";
    public static final String dayofweek = "dayofweek";
    public static final String attendance = "attendance";
    public static final String studentId = "id";
    public static final String number = "number";

    public static final String clearance = "clearance";
    public static final String category = "category";
    public static final String email = "email";
    public static final String key = "key";

    public static final String title = "title";
    public static final String content = "content";


    //Hides Keyboard
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Verify Necessary Permissions
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
