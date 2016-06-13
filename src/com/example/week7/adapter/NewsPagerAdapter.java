package com.example.week7.adapter;

import com.example.week7.fragment.NewsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 教务通知（就业信息）等新闻的数据适配器
 * 
 * @author Administrator
 * 
 */
public class NewsPagerAdapter extends FragmentPagerAdapter {

	private String[] data;
	private String type;

	public NewsPagerAdapter(FragmentManager fm, String[] data, String type) {
		super(fm);
		this.data = data;
		this.type = type;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return data[position];
	}

	@Override
	public Fragment getItem(int position) {
		NewsFragment newsFragment = new NewsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", position + 1);
		bundle.putString("reuestActivity", type);
		newsFragment.setArguments(bundle);
		return newsFragment;
	}
}
