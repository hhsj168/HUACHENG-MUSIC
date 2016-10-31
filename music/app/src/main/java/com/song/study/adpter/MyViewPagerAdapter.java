package com.song.study.adpter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.song.study.R;

public class MyViewPagerAdapter extends PagerAdapter {
	private LayoutInflater mInflater;
	private Animation animation;

	public MyViewPagerAdapter(Context context) {

		mInflater = LayoutInflater.from(context);
		animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = null;
		switch (position) {
			case 0 :
				view = mInflater.inflate(R.layout.layout1, null);
				break;
			case 1 :
				view = mInflater.inflate(R.layout.layout2, null);
				break;
			case 2 :
				view = mInflater.inflate(R.layout.layout3, null);
				view.findViewById(R.id.image_ball_id).setAnimation(animation);
				break;

		}
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
}
