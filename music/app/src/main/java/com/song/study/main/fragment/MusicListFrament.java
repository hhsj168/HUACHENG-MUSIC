package com.song.study.main.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.song.study.R;
import com.song.study.activity.MusicActivity;
import com.song.study.adpter.MusicListAdapter;
import com.song.study.musicobject.Music;
import com.song.study.conts.Constant;
import com.song.study.musicutil.MusicUtil;
import com.song.study.service.MusicService;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 全部歌曲列表
 */
public class MusicListFrament extends Fragment {

    private static final String TAG = "MusicListFrament";
    public List<Music> musics;
    private ListView mListView;
    private MusicListAdapter musicAdapter;
    private long firstTime = 0;
    // 保存设置文件的SharedPreferences对象
    private SharedPreferences sharedPreferences;
    // 是否杀死进程
    private boolean is_kill_process = false;

    private final Handler handler = new MyHandler(this);
    private final int REQEST_CODE = 0x0000;


    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.musiclist, null);
        mListView = (ListView) view.findViewById(R.id.listView_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                musics = MusicUtil.getAllMusics(getActivity().getApplicationContext());
                handler.sendEmptyMessageDelayed(REQEST_CODE, 500L);
            }
        }).start();
        // 启动播放服务
        mContext.startService(new Intent(mContext, MusicService.class));
//        mListView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//
//            }
//        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = mContext.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        is_kill_process = sharedPreferences.getBoolean("KILLPROCESS", false);
    }


    private static class MyHandler extends Handler {
        private WeakReference<MusicListFrament> mFragment;

        public MyHandler(MusicListFrament frament) {
            this.mFragment = new WeakReference<MusicListFrament>(frament);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mFragment.get() != null) {
                mFragment.get().handleMessage(msg);
            }

        }
    }

    private void handleMessage(Message msg) {
        if (msg.what == REQEST_CODE) {
            if (musicAdapter == null) {
                musicAdapter = new MusicListAdapter(getActivity(), musics);
                mListView.setAdapter(musicAdapter);
            }
            musicAdapter.notifyDataSetChanged();
        }
    }

}
