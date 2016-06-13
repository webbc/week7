package com.example.week7.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.example.week7.activity.ParttimeDetailActivity;
import com.example.week7.domain.Parttime;

/**
 * 兼职页面头部焦点图数据适配器
 * 
 * @author Administrator
 * 
 */
public class ParttimeFocusViewPagerAdapter extends PagerAdapter {
	private Context context;
	private ArrayList<Parttime> jdList;

	public ParttimeFocusViewPagerAdapter(Context context,
			ArrayList<Parttime> jdList) {
		this.context = context;
		this.jdList = jdList;
	}

	@Override
	public int getCount() {
		return jdList.size();
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
	public Object instantiateItem(ViewGroup container, final int position) {
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ScaleType.FIT_XY);
		// 加载图片
		Glide.with(context).load(jdList.get(position).getImg()).into(imageView);
		container.addView(imageView);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						ParttimeDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("title", jdList.get(position).getTitle());
				bundle.putInt("id", jdList.get(position).getId());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return imageView;
	}

}
