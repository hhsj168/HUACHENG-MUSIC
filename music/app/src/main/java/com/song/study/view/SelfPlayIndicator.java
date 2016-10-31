package com.song.study.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.song.study.R;

public class SelfPlayIndicator extends LinearLayout {
	private Context mContext;
	private int mPageIndicatorImgResId = R.drawable.dot;	
	private int mSize = 0;

	@SuppressLint("NewApi")
	public SelfPlayIndicator(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SelfPlayIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SelfPlayIndicator(Context context) {
		super(context);
		LayoutParams params = (LayoutParams) getLayoutParams();
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		
		init(context);
	}
	
	private void init(Context context) {
		mContext = context;		
		setOrientation(LinearLayout.HORIZONTAL);	
	}
	
	private void setPageIndicator(int size, int imgResId) {
		mSize = size;
		mPageIndicatorImgResId = imgResId;
		
		removeAllViews();
		
		if (size <= 1) return;
		
		for (int i = 0; i < size; i++) {
			ImageView iv = new ImageView(mContext);
			iv.setImageResource(mPageIndicatorImgResId);
			iv.setSelected(i == 0 ? true : false);
			addView(iv);
		}
	}
	
	public void setSize(int size) {
		this.mSize = size;		
		setPageIndicator(mSize, mPageIndicatorImgResId);
	}
	
	public void setPosition(int position) {
		if (mSize <= 1) {
			return;
		}
		
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			v.setSelected(position == i ? true : false);
		}
	}
	
}