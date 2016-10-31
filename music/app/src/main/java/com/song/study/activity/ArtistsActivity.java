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
import com.song.study.adpter.AlbumsAdapter2;
import com.song.study.base.BaseActivity;
import com.song.study.musicutil.AnimCommon;
import com.song.study.conts.Constant;

/***
 * 艺术家界面
 *
 * @author Kevin Song
 **/
public class ArtistsActivity extends BaseActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musiclist);

        mListView = (ListView) findViewById(R.id.listView_id);
        mListView.setBackgroundResource(R.drawable.listbg1);
        // MyExpandableListAdapter adapter=new
        // MyExpandableListAdapter(getApplicationContext());
        AlbumsAdapter2 adapter = new AlbumsAdapter2(this);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(ArtistsActivity.this,
                        MusicActivity.class);
                intent.putExtra("index", arg2);
                // 艺术家我们播放全部就行了
                intent.putExtra("FLAG", Constant.FLAG_ALL);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return true;
    }
}
