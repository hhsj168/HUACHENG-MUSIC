package com.song.study.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.song.study.conts.Constant;
import com.song.study.musicutil.MusicUtil;
import com.song.study.service.MusicService;

import java.util.List;

public class MusicListFrament extends Fragment {

    private static final String TAG = "MusicListFrament";
    public List<Music> musics;
    private ListView mListView;
    private MusicAdapter musicAdapter;
    private long firstTime = 0;
    // 保存设置文件的SharedPreferences对象
    private SharedPreferences sharedPreferences;
    // 是否杀死进程
    private boolean is_kill_process = false;

    private Handler handler;

    private final int REQEST_CODE = 0x0000;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.musiclist, null);
        mListView = (ListView) view.findViewById(R.id.listView_id);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == REQEST_CODE) {
                    musicAdapter = new MusicAdapter(getActivity(), musics);
                    mListView.setAdapter(musicAdapter);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                musics = MusicUtil.getAllMusics(getActivity().getApplicationContext());
                handler.sendEmptyMessageDelayed(REQEST_CODE, 1000);
            }
        }).start();
        // 启动播放服务
        getActivity().startService(new Intent(getActivity(), MusicService.class));
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(getActivity(), MusicActivity.class);
                intent.putExtra("index", arg2);
                intent.putExtra("FLAG", Constant.FLAG_ALL);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
        return view;
    }


    @Override
    public void onStart() {
        sharedPreferences = getActivity().getSharedPreferences("setting", Activity.MODE_PRIVATE);
        is_kill_process = sharedPreferences.getBoolean("KILLPROCESS", false);
        super.onStart();
    }

}
