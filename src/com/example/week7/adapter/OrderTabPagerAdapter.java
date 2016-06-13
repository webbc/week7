package com.example.week7.adapter;

import com.example.week7.fragment.OrderListFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 订单列表的Fragment数据适配器
 * 
 * @author Administrator
 * 
 */
public class OrderTabPagerAdapter extends FragmentPagerAdapter {

	private String[] tabs;

	public OrderTabPagerAdapter(FragmentManager fm, String[] tabs) {
		super(fm);
		this.tabs = tabs;
	}

	@Override
	public Fragment getItem(int position) {
		// 新建一个Fragment来展示ViewPager item的内容，并传递参数(参数为fragment的位置)
		Fragment fragment = new OrderListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("args", position);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCount() {
		return tabs.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabs[position % tabs.length];
	}

}
