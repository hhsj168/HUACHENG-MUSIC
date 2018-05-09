package com.song.study.view.autoplayviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class AutoPlayViewPager extends ViewPager {
    private static final long DEFAULT_FLIP_INTERVAL = 3000;
    private static final int PAGE_SCROLL_DURAIOTN = 1500;

    private AutoPlayPagerAdapter mAdapterWrapper;
    private OnPageChangeListener mOnPageChangeListener;
    private long mFlipInterval = DEFAULT_FLIP_INTERVAL; // unit: millisecond
    private boolean mVisible, mStartAutoFlip;
    private boolean mAutoFlipEnabled = true;
    private SelfPlayScroller mSelfPlayScroller;

    private Runnable mAutoFlip = new Runnable() {

        @Override
        public void run() {
            toNextPage();
        }
    };

    public AutoPlayViewPager(Context context) {
        super(context);
        init(context);
    }

    public AutoPlayViewPager(final Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setPageScrollDuration(PAGE_SCROLL_DURAIOTN);
    }

    private void setPageScrollDuration(int duration) {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);
            mSelfPlayScroller = new SelfPlayScroller(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mSelfPlayScroller);
            mSelfPlayScroller.setScrollDuration(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
        super.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (getRealAdapter() != null && getRealAdapter().getCount() > 1) {
                    startAutoFlip();
                }

                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(getRealPosition(position));
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(
                            getRealPosition(position), positionOffset,
                            positionOffsetPixels);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        mOnPageChangeListener.onPageSelected(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoFlip();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStartAutoFlip) {
                    stopAutoFlip();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startAutoFlip();
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public int getCurrentItem() {
        if (mAdapterWrapper == null) {
            return super.getCurrentItem();
        } else {
            return mAdapterWrapper.toRealPosition(super.getCurrentItem());
        }
    }

    @Override
    public void setCurrentItem(int item) {
        int offset = getFlipItemsOffset(item);
        if (offset == 0) {
            return;
        } else {
            super.setCurrentItem(super.getCurrentItem() + offset, true);
        }
    }

    public void toNextPage() {
        super.setCurrentItem(super.getCurrentItem() + 1);
    }

    public void toPreviousPage() {
        super.setCurrentItem(super.getCurrentItem() - 1);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (mAdapterWrapper == null) {
            super.setCurrentItem(item, smoothScroll);
        } else {
            int offset = getFlipItemsOffset(item);
            if (offset == 0) {
                return;
            } else {
                super.setCurrentItem(super.getCurrentItem() + offset, smoothScroll);
            }
        }
    }

    private int getFlipItemsOffset(int item) {
        int outItem = super.getCurrentItem();

        int outOffset = outItem % mAdapterWrapper.getRealCount();
        int offset = item % mAdapterWrapper.getRealCount();

        return offset - outOffset;
    }

    private void updateFlipping() {
        removeCallbacks(mAutoFlip);
        if (mVisible && mStartAutoFlip && mAutoFlipEnabled
                && getRealAdapter() != null && getRealAdapter().getCount() > 1) {
            postDelayed(mAutoFlip, mFlipInterval);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;

        updateFlipping();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;

        updateFlipping();
    }

    public void startAutoFlip() {
        mStartAutoFlip = true;
        updateFlipping();
    }

    public void stopAutoFlip() {
        mStartAutoFlip = false;
        updateFlipping();
    }

    public void setAutoFlipEnabled(boolean enabled) {
        mAutoFlipEnabled = enabled;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (adapter.getCount() <= 1) {
            super.setAdapter(adapter);
        } else {
            mAdapterWrapper = new AutoPlayPagerAdapter(adapter);
            super.setAdapter(mAdapterWrapper);
            super.setCurrentItem(mAdapterWrapper.getFisrtPosition(), false);
        }
    }

    public void setFlipInterval(long milliseconds) {
        mFlipInterval = milliseconds;
    }

    public PagerAdapter getRealAdapter() {
        if (mAdapterWrapper != null) {
            return mAdapterWrapper.getRealAdapter();
        } else {
            return getAdapter();
        }
    }

    private int getRealPosition(int position) {
        if (mAdapterWrapper == null) {
            return position;
        } else {
            return mAdapterWrapper.toRealPosition(position);
        }
    }

    private class SelfPlayScroller extends Scroller {
        private int mDuration = 1;

        public SelfPlayScroller(Context context) {
            super(context);
        }

        public SelfPlayScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @SuppressLint("NewApi")
        public SelfPlayScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        public void setScrollDuration(int duration) {
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
