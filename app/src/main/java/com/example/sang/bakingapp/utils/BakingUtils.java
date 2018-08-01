package com.example.sang.bakingapp.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class BakingUtils {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 230);
        return noOfColumns;
    }
}
