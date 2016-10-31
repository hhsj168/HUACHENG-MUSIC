package com.song.study.enter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.song.study.R;
import com.song.study.conts.SharedKeywords;
import com.song.study.main.MainActivity;
import com.song.study.adpter.MyViewPagerAdapter;
import com.song.study.socket.InetAddressTest;
import com.song.study.view.autoplayviewpager.AutoPlayViewPager;

import java.net.UnknownHostException;

public class StartActivity extends Activity {
    private static final String strs[] = {"换歌只需摇摇手机", "功能简介", "点击进入..."};
    private AutoPlayViewPager viewPager;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences spf = getSharedPreferences(SharedKeywords.SHARED_NAME_SETTING, 0);
        if (spf.getBoolean(SharedKeywords.SETTING_IS_FIRST_RUN, true)) {
            spf.edit().putBoolean(SharedKeywords.SETTING_IS_FIRST_RUN, false).commit();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_start);

        textView = (TextView) findViewById(R.id.tv_title);
        textView.setText(strs[0]);
        viewPager = (AutoPlayViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyViewPagerAdapter(this));
        viewPager.setFlipInterval(5000);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddressTest.getInetAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (viewPager != null) {
            viewPager.stopAutoFlip();
        }
    }

    public void openActivity(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            textView.setText(strs[arg0]);
        }
    }
}
