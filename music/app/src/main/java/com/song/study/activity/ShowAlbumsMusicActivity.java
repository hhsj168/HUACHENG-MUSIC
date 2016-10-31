package com.song.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.song.study.R;
import com.song.study.adpter.SingerMusicAdapter;
import com.song.study.base.BaseActivity;
import com.song.study.musicobject.Music;
import com.song.study.musicutil.AnimCommon;
import com.song.study.conts.Constant;
import com.song.study.musicutil.MusicUtil;

import java.util.List;

public class ShowAlbumsMusicActivity extends BaseActivity {

    public List<Music> musics;
    private ListView mListView;
    private SingerMusicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musiclist);

        mListView = (ListView) findViewById(R.id.listView_id);
        final String album = getIntent().getStringExtra("albumname");
        musics = MusicUtil.getMusicInOneAlum(this, album);
        adapter = new SingerMusicAdapter(this, musics);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 点击列表中的歌曲进行播放
                Intent intent = new Intent(ShowAlbumsMusicActivity.this,
                        MusicActivity.class);
                intent.putExtra("FLAG", Constant.FLAG_PART);
                intent.putExtra("albumname", album);
                intent.putExtra("index", arg2);
                startActivity(intent);
                AnimCommon.set(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);

            }
        });
        mListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mListView.setBackgroundResource(R.drawable.listbg);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mListView
                            .setBackgroundResource(android.R.color.background_dark);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return true;
    }
}
