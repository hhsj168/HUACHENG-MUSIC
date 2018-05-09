package com.huacheng.platform.view;

import android.content.Context;
import android.content.res.TypedArray;
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

public class BarView extends View {

    private Context mContext;
    private BarMode mBarMode;

    public BarView(Context context) {
        super(context);
        init(context);
    }

    public BarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.BarView);
        int mode = mTypedArray.getInteger(R.styleable.BarView_barMode, 1);
        mTypedArray.recycle();
        mBarMode = mode == 1 ? BarMode.STATUSBAR : BarMode.NAVIGATIONBAR;
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    public void setBarMode(BarMode barMode) {
        mBarMode = barMode;
        invalidate();
        requestLayout();
    }

    public enum BarMode {
        STATUSBAR, NAVIGATIONBAR
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            heightMeasureSpec = ScreenUtils.getStatusBarHeight(mContext);
        } else {
            heightMeasureSpec = 0;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
