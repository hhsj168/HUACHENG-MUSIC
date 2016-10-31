package com.song.study.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.song.study.R;
import com.song.study.musicobject.Music;
import com.song.study.musicutil.MusicUtil;

import java.util.List;

public class MusicAdapter extends BaseAdapter {
    private Context context;
    private List<Music> listMusics;

    public MusicAdapter(Context context, List<Music> listMusics) {
        this.context = context;
        this.listMusics = listMusics;
    }

    @Override
    public int getCount() {
        return listMusics.size();
    }

    @Override
    public Object getItem(int arg0) {
        return listMusics.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHolder holder;
        if (arg1 == null) {
            arg1 = LayoutInflater.from(this.context).inflate(R.layout.musicitem, null);
            holder = new ViewHolder();
            holder.music_name = (TextView) arg1.findViewById(R.id.musicname_show_textView_id);
            holder.music_singer = (TextView) arg1.findViewById(R.id.musicsinger_show_textView_id);
            holder.music_time = (TextView) arg1.findViewById(R.id.musictime_show_textView_id);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        holder.music_name.setText(listMusics.get(arg0).getTitle());
        holder.music_singer.setText(listMusics.get(arg0).getSinger());
        holder.music_time.setText(MusicUtil.formatTime(listMusics.get(arg0).getTime()));
        return arg1;
    }

    static class ViewHolder {
        TextView music_name;
        TextView music_singer;
        TextView music_time;
    }
}
