package com.example.week7.adapter;

import java.util.ArrayList;

import com.example.week7.domain.User;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * MainActivity中3个页签的ViewPager的数据适配器
 * 
 * @author Administrator
 * 
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragmentLists;// 存放Fragment的集合
	private User user;// 用户对象

	public TabViewPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragmentLists, User user) {
		super(fm);
		this.fragmentLists = fragmentLists;
		this.user = user;
	}

	@Override
	public Fragment getItem(int position) {
		// 如果是个人界面
		if (position == 2) {
			Fragment fragment = fragmentLists.get(position);
			Bundle bundle = new Bundle();
			bundle.putSerializable("user", user);
			fragment.setArguments(bundle);
			return fragment;
		}
		// 如果是广场界面
		if (position == 1) {
			Fragment fragment = fragmentLists.get(position);
			Bundle bundle = new Bundle();
			bundle.putString("phone", user.getPhone());
			fragment.setArguments(bundle);
			return fragment;
		}
		return fragmentLists.get(position);
	}

	@Override
	public int getCount() {
		return fragmentLists.size();
	}

}
