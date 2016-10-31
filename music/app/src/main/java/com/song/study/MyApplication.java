package com.song.study;

import android.app.Application;
import android.view.WindowManager;

public class MyApplication extends Application {

    private WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getMywmParams() {
        return layoutParams;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
