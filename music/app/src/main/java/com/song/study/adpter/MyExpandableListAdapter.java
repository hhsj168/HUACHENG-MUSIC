package com.song.study.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.song.study.R;
import com.song.study.musicobject.Music;
import com.song.study.musicutil.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "MyExpandableListAdapter";

    private int musicsCount[] = {1, 2, 3, 4};
    private Context context;
    private List<String> singers_org = new ArrayList<String>();
    private List<String> musicSingers = new ArrayList<String>();
    private List<List<String>> albumNames = new ArrayList<List<String>>();
    private List<Music> musicInfos = new ArrayList<Music>();

    public MyExpandableListAdapter(Context context) {
        this.context = context;
        musicSingers = MusicUtil.getAllSingers(context);
        singers_org = MusicUtil.getAllSingers_Org(context);

        for (int i = 0; i < singers_org.size(); i++) {
            albumNames.add(MusicUtil.getAlbumNameBaseSingers(context,
                    singers_org.get(i)));
        }

		/*
         * for (int i = 0; i < albumNames.size(); i++) { for (int j = 0; j <
		 * albumNames.get(i).size(); j++) {
		 * musicsCount[i][j]=MusicUtil.getAlbumNumber(context,
		 * albumNames.get(i).get(j)); } }
		 */

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return albumNames.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // 注意代码的书写系统将会减少创建很多View。性能得到了很大的提升。
        // if (convertView == null) ！！！！
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_music_list, null);
        }

        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.imageView_id);
        imageView.setImageResource(R.drawable.album);

        // 因为用的是同一个布局文件，这里显示专辑字
        TextView music_name = (TextView) convertView
                .findViewById(R.id.musicname_show_textView_id);
        music_name.setText(getChild(groupPosition, childPosition).toString());
        // 这里显示专辑中的歌曲数目
        TextView music_singer = (TextView) convertView
                .findViewById(R.id.musicsinger_show_textView_id);
        music_singer.setText(albumNames.get(groupPosition).size() + "首歌曲");
        // 这里显示""
        TextView music_time = (TextView) convertView
                .findViewById(R.id.musictime_show_textView_id);
        music_time.setText("");
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return albumNames.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return musicSingers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return musicSingers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.groupitem, null);
        }
        TextView singerName = (TextView) convertView
                .findViewById(R.id.singername_show_textView_id);
        singerName.setText(getGroup(groupPosition).toString());

        TextView albumCount = (TextView) convertView
                .findViewById(R.id.albumcount_show_textView_id);
        albumCount.setText(albumNames.get(groupPosition).size() + "张专辑");
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
