package com.example.week7.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义的GridView
 * 
 * @author Administrator
 * 
 */
public class PictureGridView extends GridView {
	public PictureGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PictureGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PictureGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	// 重写onMeasure里面方法，使界面撑到最大
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}