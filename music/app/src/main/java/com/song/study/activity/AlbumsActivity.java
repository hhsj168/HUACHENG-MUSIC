package com.song.study.activity;

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
import com.song.study.adpter.AlbumsAdapter;
import com.song.study.base.BaseActivity;
import com.song.study.musicobject.Music;
import com.song.study.musicutil.AnimCommon;
import com.song.study.conts.Constant;
import com.song.study.musicutil.MusicUtil;

import java.util.List;

public class AlbumsActivity extends BaseActivity {
    private AlbumsAdapter albumsAdapter;
    private List<Music> albums;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musiclist);
        listView = (ListView) findViewById(R.id.listView_id);
        listView.setBackgroundResource(R.drawable.listbg);
        albums = MusicUtil.getAllMusics(getApplicationContext());
        albumsAdapter = new AlbumsAdapter(this, albums);
        listView.setAdapter(albumsAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String album = albums.get(arg2).getAlbum();
                Intent intent = new Intent(AlbumsActivity.this,
                        ShowAlbumsMusicActivity.class);
                intent.putExtra("albumname", album);
                intent.putExtra("FLAG", Constant.FLAG_PART);
                startActivity(intent);
                AnimCommon.set(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    listView.setBackgroundResource(R.drawable.listbg);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    listView.setBackgroundResource(android.R.color.background_dark);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
