package com.huacheng.platform.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.huacheng.platform.R;
import com.huacheng.platform.utils.ScreenUtils;


/**
 * Created by shaohua on 2016/10/30.
 */

public class BottomNavigationBarView extends View {

    private Context mContext;
    private int height = 0;

    public BottomNavigationBarView(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomNavigationBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        height = ScreenUtils.getNavigationBarHeight(context);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            params.height = this.height;
        } else {
            params.height = 0;
        }
        super.setLayoutParams(params);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
