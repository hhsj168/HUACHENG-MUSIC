package com.huacheng.platform.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.huacheng.platform.R;


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
        statusBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        addView(statusBarView);

        FrameLayout frameLayout = new FrameLayout(context);
        LinearLayout.LayoutParams p = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 1;
        frameLayout.setLayoutParams(p);
        addView(frameLayout);
        BottomNavigationBarView navigationBarView = new BottomNavigationBarView(context);
        statusBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        addView(navigationBarView);
    }

    /**
     * 加载内部视图
     */
    public void addContentView() {

    }

}
