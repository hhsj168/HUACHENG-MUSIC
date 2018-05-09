package com.huacheng.platform.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.huacheng.platform.R;
import com.huacheng.platform.utils.ScreenUtils;


/**
 * Created by shaohua on 2016/10/30.
 */

public class MyThemeVerticalLinearLayout extends LinearLayout {

    private Context mContext;

    public MyThemeVerticalLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public MyThemeVerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyThemeVerticalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        this.setOrientation(VERTICAL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            t += ScreenUtils.getStatusBarHeight(mContext);
            b -= ScreenUtils.getNavigationBarHeight(mContext);
        }
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.addRect(new RectF(0, 0, getWidth(), ScreenUtils.getStatusBarHeight(mContext)), Path.Direction.CW);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(0, 0, 255));

        Path path1 = new Path();
        path1.addRect(new RectF(0, getHeight() - ScreenUtils.getNavigationBarHeight(mContext), getWidth(), getHeight()), Path.Direction.CW);
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.FILL);
        paint1.setColor(Color.rgb(255, 0, 0));

        canvas.drawPath(path, paint);
        canvas.drawPath(path1, paint1);
    }
}
