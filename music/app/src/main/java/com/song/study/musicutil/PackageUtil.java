package com.song.study.musicutil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by shaohua on 2016/10/28.
 */

public class PackageUtil {

    public static PackageManager getPackageManager(Context context) {
        return context.getPackageManager();
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---  
            PackageManager packageManager = context.getPackageManager();
            PackageInfo pi = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }

        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versionName = 0;
        try {
            // ---get the package info---  
            PackageManager packageManager = context.getPackageManager();
            PackageInfo pi = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
