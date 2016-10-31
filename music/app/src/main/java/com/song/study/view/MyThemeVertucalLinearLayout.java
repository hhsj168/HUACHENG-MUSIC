package com.song.study.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.widget.LinearLayout;

import com.song.study.R;

/**
 * Created by shaohua on 2016/10/30.
 */

public class MyThemeVertucalLinearLayout extends LinearLayout {
    public MyThemeVertucalLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public MyThemeVertucalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyThemeVertucalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        this.setOrientation(VERTICAL);
        TopStatusBarView statusBarView = new TopStatusBarView(context);
        statusBarView.setBackgroundColor(getResources().getColor(R.color.theme_color));
        addView(statusBarView);
    }

}
