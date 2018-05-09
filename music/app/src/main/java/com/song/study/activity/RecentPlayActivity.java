package com.song.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.song.study.R;
import com.song.study.adpter.MusicAdapter;
import com.song.study.base.BaseActivity;
import com.song.study.musicobject.Music;
import com.song.study.musicutil.AnimCommon;
import com.song.study.conts.Constant;
import com.song.study.service.MusicService;

import java.util.List;

public class RecentPlayActivity extends BaseActivity {

    private ListView mListView;
    private MusicAdapter adapter;
    private List<Music> listMusics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.musiclist);

        mListView = (ListView) findViewById(R.id.listView_id);
        mListView.setBackgroundResource(R.drawable.listbg1);
        List<Music> listMusics = MusicService.recentMusics;
        adapter = new MusicAdapter(this, listMusics);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(RecentPlayActivity.this,
                        MusicActivity.class);
                intent.putExtra("index", arg2);
                intent.putExtra("FLAG", Constant.FLAG_RECENT);
                startActivity(intent);
                // 设置ACT跳转之间的动画
                AnimCommon.set(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
            }
        });
        mListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mListView.setBackgroundResource(R.drawable.listbg1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mListView
                            .setBackgroundResource(android.R.color.background_dark);
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        listMusics = MusicService.recentMusics;
        adapter.notifyDataSetChanged();
    }
}
