package com.song.study.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.song.study.R;
import com.song.study.musicobject.Music;

import java.util.List;

public class AlbumsAdapter extends BaseAdapter {

    private Context context;
    // 专辑数，一张专辑可能有很多个歌曲
    private List<Music> albums_count;

    public AlbumsAdapter(Context context, List<Music> albums_count) {
        this.context = context;
        this.albums_count = albums_count;
    }

    @Override
    public int getCount() {
        return albums_count.size();
    }

    @Override
    public Object getItem(int arg0) {
        return albums_count.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // ListView的优化方法1-----------
        // 注意代码的书写系统将会减少创建很多View。性能得到了很大的提升。
        // if (arg1 == null) ！！！！
        if (arg1 == null) {
            arg1 = LayoutInflater.from(this.context).inflate(R.layout.item, null);
        }
        ImageView imageView = (ImageView) arg1.findViewById(R.id.imageView_id);
        imageView.setImageResource(R.drawable.album);

        // 因为用的是同一个布局文件，这里显示专辑名字
        TextView music_name = (TextView) arg1.findViewById(R.id.musicname_show_textView_id);
        music_name.setText(albums_count.get(arg0).getAlbum());
        // 这里显示专辑的作者
        TextView music_singer = (TextView) arg1.findViewById(R.id.musicsinger_show_textView_id);
        music_singer.setText(albums_count.get(arg0).getSinger());
        // 这里显示专辑的发行年份
        TextView music_time = (TextView) arg1.findViewById(R.id.musictime_show_textView_id);
        String year = albums_count.get(arg0).getYear();
        if (year == null) {
            year = "未知年份";
        } else {
            year = year + "年发行";
        }
        music_time.setText(year);
        return arg1;
    }
}
