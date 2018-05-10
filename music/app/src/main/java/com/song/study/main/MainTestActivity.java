package com.song.study.main;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.song.study.R;
import com.song.study.activity.AlbumsActivity;
import com.song.study.activity.ArtistsActivity;
import com.song.study.activity.ListActivity;
import com.song.study.activity.RecentPlayActivity;
import com.song.study.musicutil.AnimCommon;
import com.song.study.conts.Constant;

public class MainTestActivity extends TabActivity {

    // *********************************
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static int maxTabIndex = 3;
    private TabHost mTabHost;
    private int currentView = 0;
    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.GINGERBREAD) {
            showNotSupportDialog(getApplicationInfo().targetSdkVersion);
        }
        initTabs();

        gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };

    }

    private void initTabs() {
        Intent mIntent = new Intent(this, ListActivity.class);
        mTabHost = this.getTabHost();
        LayoutInflater.from(this).inflate(R.layout.activity_main_test,
                mTabHost.getTabContentView(), true);

        mTabHost.addTab(mTabHost.newTabSpec("音乐列表").setIndicator("音乐列表",
                getResources().getDrawable(R.drawable.itemicon))
                .setContent(mIntent));

        mIntent = new Intent(this, ArtistsActivity.class);
        mTabHost.addTab(mTabHost.newTabSpec("艺术家").setIndicator("艺术家",
                getResources().getDrawable(R.drawable.artist))
                .setContent(mIntent));

        mIntent = new Intent(this, AlbumsActivity.class);
        mTabHost.addTab(mTabHost.newTabSpec("专辑").setIndicator("专辑",
                getResources().getDrawable(R.drawable.album))
                .setContent(mIntent));

        mIntent = new Intent(this, RecentPlayActivity.class);
        mTabHost.addTab(mTabHost.newTabSpec("最近播放").setIndicator("最近播放",
                getResources().getDrawable(R.drawable.recent))
                .setContent(mIntent));
        mTabHost.setCurrentTab(currentView);
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                currentView = mTabHost.getCurrentTab();
            }
        });
        mTabHost.setAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha_z));
    }

    private void showNotSupportDialog(int verson) {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_title))
                .setIcon(R.drawable.info)
                .setTitle("targetSdkVersion is:" + verson + "not support!")
                .setPositiveButton(
                        getResources().getString(R.string.dailog_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    finish();
                                } catch (Exception e) {
                                    finish();
                                }
                                dialog.dismiss();
                            }
                        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onPause() {
        if (AnimCommon.in != 0 && AnimCommon.out != 0) {
            super.overridePendingTransition(AnimCommon.in, AnimCommon.out);
            AnimCommon.clear();
        }
        super.onPause();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }

        return super.dispatchTouchEvent(event);
    }

    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    /*
                     * if (currentView == maxTabIndex) { currentView = 0; } else
					 * { currentView++; }
					 */
                    currentView = (currentView == maxTabIndex
                            ? 0
                            : ++currentView);
                    mTabHost.setCurrentTab(currentView);
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    currentView = (currentView == 0
                            ? maxTabIndex
                            : --currentView);
                    mTabHost.setCurrentTab(currentView);
                }
            } catch (Exception e) {
            }
            return false;
        }
    }

}
