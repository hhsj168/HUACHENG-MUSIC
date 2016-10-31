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

public class SingerMusicAdapter extends BaseAdapter {

	private Context context;
	// 专辑数，一张专辑中的歌曲数
	private List<Music> musics;

	public SingerMusicAdapter(Context context, List<Music> musics) {
		this.context = context;
		this.musics = musics;
	}

	@Override
	public int getCount() {
		return musics.size();
	}

	@Override
	public Object getItem(int position) {
		return musics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 注意代码的书写系统将会减少创建很多View。性能得到了很大的提升。
		// if (convertView == null) ！！！！
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item,
					null);
		}
		TextView name = (TextView) convertView
				.findViewById(R.id.musicname_show_textView_id);
		name.setText(musics.get(position).getTitle());
		TextView music_singer = (TextView) convertView
				.findViewById(R.id.musicsinger_show_textView_id);
		music_singer.setText(musics.get(position).getSinger());

		TextView music_time = (TextView) convertView
				.findViewById(R.id.musictime_show_textView_id);
		music_time
				.setText(MusicUtil.formatTime(musics.get(position).getTime()));
		return convertView;
	}
}
