package com.song.study.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.song.study.R;
import com.song.study.activity.ShowAlbumsMusicActivity;
import com.song.study.adpter.AlbumsAdapter;
import com.song.study.musicobject.Music;
import com.song.study.musicutil.AnimCommon;
import com.song.study.conts.Constant;
import com.song.study.musicutil.MusicUtil;

import java.util.List;

public class AlbumsListFragment extends ListFragment {
    private AlbumsAdapter albumsAdapter;
    private List<Music> albums;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.musiclist, container, false);
        listView = (ListView) view.findViewById(R.id.listView_id);
        listView.setBackgroundResource(R.drawable.listbg);
        albums = MusicUtil.getAllMusics(getActivity().getApplicationContext());
        albumsAdapter = new AlbumsAdapter(getActivity(), albums);
        listView.setAdapter(albumsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String album = albums.get(arg2).getAlbum();
                Intent intent = new Intent(getActivity(),
                        ShowAlbumsMusicActivity.class);
                intent.putExtra("albumname", album);
                intent.putExtra("FLAG", Constant.FLAG_PART);
                startActivity(intent);
                AnimCommon.set(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {

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

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
