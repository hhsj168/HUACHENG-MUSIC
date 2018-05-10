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
import com.song.study.musicutil.MusicUtil;

import java.util.List;

public class AlbumsAdapter2 extends BaseAdapter {

	private Context context;
	private List<Music> listMusics;

	public AlbumsAdapter2(Context context) {
		this.context = context;
		this.listMusics = MusicUtil.getAllMusics(context);
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
		// 注意代码的书写系统将会减少创建很多View。性能得到了很大的提升。
		// if (arg1 == null) ！！！！
		if (arg1 == null) {
			arg1 = LayoutInflater.from(this.context).inflate(
					R.layout.item_music_list, null);
		}
		ImageView imageView = (ImageView) arg1.findViewById(R.id.imageView_id);
		imageView.setImageResource(R.drawable.artist);
		TextView music_name = (TextView) arg1
				.findViewById(R.id.musicname_show_textView_id);
		music_name.setText(listMusics.get(arg0).getSinger());
		TextView music_singer = (TextView) arg1
				.findViewById(R.id.musicsinger_show_textView_id);
		music_singer.setText(listMusics.get(arg0).getAlbum());
		TextView music_time = (TextView) arg1
				.findViewById(R.id.musictime_show_textView_id);
		music_time
				.setText(MusicUtil.formatTime(listMusics.get(arg0).getTime()));
		return arg1;
	}
}
