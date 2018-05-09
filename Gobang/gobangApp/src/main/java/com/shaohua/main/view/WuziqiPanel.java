package com.shaohua.main.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.shaohua.game.wuziqi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaohua on 2016/11/15.
 */

public class WuziqiPanel extends View {

    private int mPannelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

    Paint mPaint = new Paint();

    public WuziqiPanel(Context context) {
        this(context, null);
    }

    public WuziqiPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WuziqiPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(0X44ff3300);
        init();
    }

    private void init() {
        mPaint.setColor(0X88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b2);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getSize(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPannelWidth = w;
        mLineHeight = mPannelWidth * 1.0f / MAX_LINE;
        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);

    }


    List<Point> mWhiteArray = new ArrayList<>();

    List<Point> mBlaceArray = new ArrayList<>();

    boolean mIsWhtie = true;//白起先手，或者当前白起

    private boolean mIsGameover;
    private boolean mIsWhtieWinner;


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (mIsGameover) {
            return false;
        }

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            return true;
        } else if (action == MotionEvent.ACTION_UP) {


            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x, y);

            if (mWhiteArray.contains(p) || mBlaceArray.contains(p)) {
                return false;
            }


            if (mIsWhtie) {
                mWhiteArray.add(p);
            } else {
                mBlaceArray.add(p);
            }
            invalidate();

            mIsWhtie = !mIsWhtie;


            return true;
        }
        return super.onTouchEvent(event);
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);

        drawPieces(canvas);

        checkGameover();
    }

    private void checkGameover() {
        boolean whiteWin = checkFiveInline(mWhiteArray);

        boolean blaceWin = checkFiveInline(mBlaceArray);

        if (whiteWin || blaceWin) {
            mIsGameover = true;
            mIsWhtieWinner = whiteWin;
            String text = mIsWhtieWinner ? "白棋胜利" : "黑棋胜利";
            Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private boolean checkFiveInline(List<Point> points) {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            boolean win = checkHorizontal(x, y, points);
            if (win) return true;
            win = checkVertical(x, y, points);
            if (win) return true;
            win = checkLeftTopRightBottom(x, y, points);
            if (win) return true;
            win = checkLeftBottomRightTop(x, y, points);
            if (win) return true;
        }
        return false;
    }

    private int MAX_COUNT_IN_LINE = 5;

    private boolean checkHorizontal(int x, int y, List<Point> points) {

        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }


    private boolean checkVertical(int x, int y, List<Point> points) {

        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }


    private boolean checkLeftTopRightBottom(int x, int y, List<Point> points) {

        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }


    private boolean checkLeftBottomRightTop(int x, int y, List<Point> points) {

        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0; i < mWhiteArray.size(); i++) {

            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight,
                    (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }

        for (int i = 0; i < mBlaceArray.size(); i++) {

            Point blackPoint = mBlaceArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight,
                    (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }
    }

    private void drawBoard(Canvas canvas) {

        int w = mPannelWidth;
        float lineHeight = mLineHeight;
        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            int y = (int) ((0.5f + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
