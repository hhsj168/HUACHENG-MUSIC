package com.song.study.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.song.study.MyApplication;
import com.song.study.musicutil.ColorFont;
import com.song.study.musicutil.LrcProcess.LrcContent;

import java.util.ArrayList;
import java.util.List;

/**
 * MyFloatView.java该类继承TextView，显示桌面歌词，处理移动事件
 ***/
public class MyFloatView extends TextView {

    public static ColorFont colorFont;
    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private int Index = 0;
    private float width;
    private float high;
    private Paint CurrentPaint;
    // 保存每句歌词和时间的集合
    private List<LrcContent> mSentenceEntities = new ArrayList<LrcContent>();
    // 控制渲染速度
    private float speed1 = 0.01f;
    private float speed2 = 0.01f;
    private float speed;
    private int oldindex = Index;

    public MyFloatView(Context context) {
        super(context);
        wm = (WindowManager) getContext().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        wmParams = ((MyApplication) getContext().getApplicationContext())
                .getMywmParams();

        this.setText("");
        this.setTextColor(Color.RED);
        this.setTextSize(30.0f);

        // 成绩设置歌词的颜色大小透明度的对象
        colorFont = new ColorFont(getContext());

        // *******************高亮部分********************
        // 初始化画笔
        CurrentPaint = new Paint();
        // 设置画笔抗锯齿,就会圆滑些了
        CurrentPaint.setAntiAlias(true);
        // 设置画笔的透明度
        CurrentPaint.setAlpha(255);
        // 设置画笔的对齐方式
        CurrentPaint.setTextAlign(Paint.Align.CENTER);

    }

    public void setSentenceEntities(List<LrcContent> mSentenceEntities) {
        this.mSentenceEntities = mSentenceEntities;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 用于获取触摸点离屏幕左上角的距离
        x = event.getRawX();
        y = event.getRawY() - 25; // 25是系统状态栏的高度

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                break;
            // 手指移动时
            case MotionEvent.ACTION_MOVE:
                // 更新视图
                updateViewPosition();
                break;
            // 手指松开时
            case MotionEvent.ACTION_UP:
                updateViewPosition();
                mTouchStartX = mTouchStartY = 0;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void updateViewPosition() {
        // 更新浮动窗口位置参数
        // 两者相减，就是View左上角的坐标了。
        wmParams.x = (int) (x - mTouchStartX);
        wmParams.y = (int) (y - mTouchStartY);
        wm.updateViewLayout(this, wmParams);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) {
            return;
        }
        if (Index == -1) {
            setText("");
            return;
        }

        // 设置歌词高亮色的颜色,透明度,大小
        CurrentPaint.setColor(colorFont.getColor_current());
        CurrentPaint.setTextSize(colorFont.getFont_current_size());
        CurrentPaint.setAlpha(colorFont.getFont_current_alpha());
        CurrentPaint.setTypeface(Typeface.SERIF);

        try {
            setText("");
            if ((Index + 1) <= mSentenceEntities.size()) {
                long time = mSentenceEntities.get(Index + 1).getLrc_time()
                        - mSentenceEntities.get(Index).getLrc_time();
                speed = time / mSentenceEntities.get(Index).getLrc().length();
                speed1 += 1.2 / speed;
                speed2 += 1.2 / speed;
            }

            String s = mSentenceEntities.get(Index).getLrc();
            int len = (int) (this.getTextSize() * s.length());

            // 渲染歌词,LinearGradient前四个参数是从那里渲染到那里，第五个参数是渲染的颜色，第六个是对应渲染颜色的速度，第7个是模式
            LinearGradient shader = new LinearGradient(20, 0, len, 0,
                    new int[]{Color.YELLOW, Color.RED}, new float[]{speed1,
                    speed2}, TileMode.CLAMP);
            CurrentPaint.setShader(shader);
            // 画出当前的歌词
            canvas.drawText(s, width / 2, high / 2, CurrentPaint);
        } catch (Exception e) {
            setText("");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.high = h;
    }

    public void SetIndex(int index) {
        this.Index = index;
        if (oldindex != index) {
            speed1 = 0.01f;
            speed2 = 0.01f;
        }
        oldindex = index;
    }
}
