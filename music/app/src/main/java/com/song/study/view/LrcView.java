package com.song.study.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.song.study.musicutil.ColorFont;
import com.song.study.musicutil.LrcProcess.LrcContent;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义绘画歌词，产生滚动效果
 */
public class LrcView extends TextView {

    public static ColorFont colorFont;

    private Paint CurrentPaint;// 高亮歌词的画笔
    private Paint NotCurrentPaint;// 非高亮歌词的画笔
    private float TextHigh = 25;
    private float width;
    private float high;
    private int Index = 0;

    // 保存每句歌词和时间的集合
    private List<LrcContent> mSentenceEntities = new ArrayList<LrcContent>();

    public LrcView(Context context) {
        super(context);
        init();
    }

    public LrcView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setSentenceEntities(List<LrcContent> mSentenceEntities) {
        this.mSentenceEntities = mSentenceEntities;
    }

    private void init() {
        setFocusable(true);

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

        // *******************非高亮部分********************
        NotCurrentPaint = new Paint();
        NotCurrentPaint.setAntiAlias(true);
        NotCurrentPaint.setAlpha(100);
        NotCurrentPaint.setTextAlign(Paint.Align.CENTER);
        // NotCurrentPaint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体
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

        // 设置歌词非高亮色的颜色,透明度,大小
        NotCurrentPaint.setColor(colorFont.getColor_not_current());
        NotCurrentPaint.setTextSize(colorFont.getFont_not_current_size());
        NotCurrentPaint.setAlpha(colorFont.getFont_not_current_alpha());
        NotCurrentPaint.setTypeface(Typeface.DEFAULT);

        try {
            setText("");
            // 画出当前的歌词
            canvas.drawText(mSentenceEntities.get(Index).getLrc(), width / 2,
                    high / 2, CurrentPaint);

            float tempY = high / 2;
            // 画出本句之前的句子
            for (int i = Index - 1; i >= 0; i--) {
                // 向上推移
                tempY = tempY - TextHigh;
                canvas.drawText(mSentenceEntities.get(i).getLrc(), width / 2,
                        tempY, NotCurrentPaint);
            }

            tempY = high / 2;
            // 画出本句之后的句子
            for (int i = Index + 1; i < mSentenceEntities.size(); i++) {
                // 往下推移
                tempY = tempY + TextHigh;
                canvas.drawText(mSentenceEntities.get(i).getLrc(), width / 2,
                        tempY, NotCurrentPaint);
            }

        } catch (Exception e) {
            setText("亲...木有歌词文件，赶紧去下载吧...");
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
    }
}
