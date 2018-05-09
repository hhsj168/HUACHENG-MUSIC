package com.huacheng.platform.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Field;


public class ScreenUtils {

    private static final int DEFAULT_STATUS_BAR_HEIGHT = 50;

    private static final int DEFAULT_NAVIGATION_BAR_HEIGHT = 96;

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }


    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            int y = Integer.parseInt(field.get(obj).toString());
            x = context.getResources().getDimensionPixelSize(y);
            Log.i("TAG status_bar_height==", x + "");
            return x;
        } catch (Exception e1) {
            e1.printStackTrace();
            return DEFAULT_STATUS_BAR_HEIGHT;
        }
    }


    public static int getNavigationBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("navigation_bar_height");
            int y = Integer.parseInt(field.get(obj).toString());
            x = context.getResources().getDimensionPixelSize(y);
            Log.i("TAG navigation_height==", x + "");
            return x;
        } catch (Exception e1) {
            e1.printStackTrace();
            return DEFAULT_NAVIGATION_BAR_HEIGHT;
        }
    }


    public static int dp2px(Context context, int dip) {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }


}
