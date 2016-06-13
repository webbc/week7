package com.example.week7.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.example.week7.domain.Focus;

/**
 * HomeFragment中的广告viewPager的数据适配器
 * 
 * @author Administrator
 * 
 */
public class AdViewPagerAdapter extends PagerAdapter {

	private ArrayList<Focus> focusLists;
	private Context context;

	public AdViewPagerAdapter(Context context, ArrayList<Focus> focusLists) {
		this.context = context;
		this.focusLists = focusLists;
	}

	@Override
	public int getCount() {
		return focusLists.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ScaleType.FIT_XY);
		// 加载图片
		Glide.with(context).load(focusLists.get(position).getImg())
				.into(imageView);
		container.addView(imageView);
		return imageView;
	}

}
