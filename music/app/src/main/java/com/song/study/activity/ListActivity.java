package com.song.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.song.study.R;
import com.song.study.adpter.MusicAdapter;
import com.song.study.base.BaseActivity;
import com.song.study.musicobject.Music;
import com.song.study.musicutil.AnimCommon;
import com.song.study.conts.Constant;
import com.song.study.musicutil.MusicUtil;
import com.song.study.service.MusicService;

import java.util.List;

public class ListActivity extends BaseActivity {

    private static final String TAG = "MusicListFrament";
    public List<Music> musics;
    private ListView mListView;
    private MusicAdapter musicAdapter;
    private long firstTime = 0;
    // 保存设置文件的SharedPreferences对象
    private SharedPreferences sharedPreferences;
    // 是否杀死进程
    private boolean is_kill_process = false;

    private android.os.Handler handler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musiclist);
        mListView = (ListView) findViewById(R.id.listView_id);
        handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                musicAdapter = new MusicAdapter(ListActivity.this, musics);
                mListView.setAdapter(musicAdapter);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                musics = MusicUtil.getAllMusics(getApplicationContext());
            }
        }).start();
        // 启动播放服务
        startService(new Intent(this, MusicService.class));

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(ListActivity.this,
                        MusicActivity.class);
                intent.putExtra("index", arg2);
                intent.putExtra("FLAG", Constant.FLAG_ALL);

                startActivity(intent);
//                 overridePendingTransition(R.anim.anim1, R.anim.anim1);
//                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim
//                        .slide_out_right);
                AnimCommon.set(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        mListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mListView.setBackgroundResource(R.drawable.listbg);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mListView.setBackgroundResource(android.R
                                .color.background_dark);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        if (Constant.D)
            Log.e("--------------", "onRestart()");
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onStart() {
        sharedPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        is_kill_process = sharedPreferences.getBoolean("KILLPROCESS", false);
        super.onStart();
    }

    // @see覆盖onKeyDown方法 捕获KEYCODE_BACK(返回键)设置点击2次退出的效果

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            // 当连续按返回键两次的时间不超过800毫秒就退出
            if (secondTime - firstTime > 800) {
                Toast.makeText(getBaseContext(), "亲...再按一次返回键退出程序",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                if (!is_kill_process) {
                    // 退出程序,后台播放音乐
                    finish();
                } else {
                    // 停止后台服务，不支持后台播放音乐
                    stopService(new Intent(this, MusicService.class));
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
