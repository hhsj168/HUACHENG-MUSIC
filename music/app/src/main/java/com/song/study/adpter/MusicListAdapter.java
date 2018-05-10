package com.song.study.adpter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.song.study.R;
import com.song.study.musicobject.Music;
import com.song.study.service.MusicService;

import java.util.List;

public class MusicListAdapter extends BaseAdapter {
    private Context context;
    private List<Music> listMusics;

    private int lastSelectedPosition = -1;

    public MusicListAdapter(Context context, List<Music> listMusics) {
        this.context = context;
        this.listMusics = listMusics;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MusicService.getInstance() != null) {
                int i = MusicService.getInstance().getIndex();
                if (i != -1) {
                    lastSelectedPosition = i;
                    listMusics.get(i).setSelected(1);
                }
            }
        }
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
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        ViewHolder holder;
        if (arg1 == null) {
            arg1 = LayoutInflater.from(context).inflate(R.layout.item_music_list, null);
            holder = new ViewHolder();
            holder.music_name = (TextView) arg1.findViewById(R.id.musicname_show_textView_id);
            holder.music_singer = (TextView) arg1.findViewById(R.id.musicsinger_show_textView_id);
            holder.selectedStatus = (ImageView) arg1.findViewById(R.id.choose_status);
            holder.moreTextView = (ImageView) arg1.findViewById(R.id.musictime_show_textView_id);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        if (listMusics.get(arg0).isSelected() == 1) {
            holder.selectedStatus.setVisibility(View.VISIBLE);
        } else {
            holder.selectedStatus.setVisibility(View.INVISIBLE);
        }
        holder.music_name.setText(listMusics.get(arg0).getTitle());
        holder.music_singer.setText(listMusics.get(arg0).getSinger());
        holder.moreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "更多", Toast.LENGTH_SHORT).show();
            }
        });
        arg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedPosition != arg0) {
                    if (lastSelectedPosition != -1) {
                        listMusics.get(lastSelectedPosition).setSelected(0);
                    }
                }
                lastSelectedPosition = arg0;
                listMusics.get(arg0).setSelected(1);
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.onItemClick(arg0, listMusics.get(arg0));
                }

            }
        });

        return arg1;
    }

    static class ViewHolder {
        TextView music_name;
        TextView music_singer;
        ImageView selectedStatus;
        ImageView moreTextView;
    }

    private IMusicItemClickListener mListener;

    public void setMusicItemClickListener(IMusicItemClickListener listener) {
        this.mListener = listener;
    }

    public interface IMusicItemClickListener {
        void onItemClick(int position, Music music);
    }
}
