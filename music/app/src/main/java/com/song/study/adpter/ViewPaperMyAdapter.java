package com.song.study.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.song.study.R;

public class ViewPaperMyAdapter extends PagerAdapter {
	private Context context;
	private List<View> list;
	private LayoutInflater mInflater;
	private Animation animation;
	public ViewPaperMyAdapter(Context context) {
		this.context = context;
		list = new ArrayList<View>();
		mInflater = LayoutInflater.from(context);
		list.add(mInflater.inflate(R.layout.layout1, null));
		list.add(mInflater.inflate(R.layout.layout2, null));
		list.add(mInflater.inflate(R.layout.layout3, null));
		animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
		list.get(2).findViewById(R.id.image_ball_id).setAnimation(animation);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(list.get(position));
		if (position == 2) {
			list.get(2).findViewById(R.id.image_ball_id)
					.setAnimation(animation);
		}
		return list.get(position);
	}
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
}
