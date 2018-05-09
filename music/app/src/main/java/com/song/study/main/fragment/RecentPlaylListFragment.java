package com.song.study.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.song.study.R;
import com.song.study.activity.MusicActivity;
import com.song.study.adpter.MusicAdapter;
import com.song.study.musicobject.Music;
import com.song.study.musicutil.AnimCommon;
import com.song.study.conts.Constant;
import com.song.study.service.MusicService;

import java.util.List;

/**
 * 最近播放列表
 */
public class RecentPlaylListFragment extends ListFragment {

    private ListView mListView;
    private MusicAdapter adapter;
    private List<Music> listMusics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.musiclist, container, false);
        mListView = (ListView) view.findViewById(R.id.listView_id);
        mListView.setBackgroundResource(R.drawable.listbg1);
        List<Music> listMusics = MusicService.recentMusics;
        adapter = new MusicAdapter(getActivity(), listMusics);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(getActivity(), MusicActivity.class);
                intent.putExtra("index", arg2);
                intent.putExtra("FLAG", Constant.FLAG_RECENT);
                startActivity(intent);
                // 设置ACT跳转之间的动画
                AnimCommon.set(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        mListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mListView.setBackgroundResource(R.drawable.listbg1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mListView.setBackgroundResource(android.R.color.background_dark);
                }
                return false;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
