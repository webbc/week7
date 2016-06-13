package com.example.week7.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.week7.R;
import com.example.week7.adapter.TabViewPagerAdapter;
import com.example.week7.fragment.HomeFragment;
import com.example.week7.fragment.ParkFragment;
import com.example.week7.fragment.PersonFragment;

/**
 * MainActivity界面
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends BaseActivity implements OnClickListener {
	private ViewPager vpMain;// 主界面的ViewPager，用来放置3个Fragment
	private LinearLayout llTabHome;// 首页tab标签
	private LinearLayout llTabPark;// 广场tab标签
	private LinearLayout llTabPerson;// 个人tab标签
	private ImageView ivTabHome;
	private ImageView ivTabPark;
	private ImageView ivTabPerson;
	private ArrayList<Fragment> fragmentLists = new ArrayList<Fragment>();
	private String tag = "TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initData();
		initListener();
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		// 给ViewPager设置页面切换监听器
		vpMain.setOnPageChangeListener(new SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				showLightTab(position);
			}
		});
		// 给3个tab标签设置点击事件
		llTabHome.setOnClickListener(this);
		llTabPark.setOnClickListener(this);
		llTabPerson.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 给集合添加数据
		fragmentLists.add(new HomeFragment());
		fragmentLists.add(new ParkFragment());
		fragmentLists.add(new PersonFragment());
		// 设置ViewPager的数据设配器
		vpMain.setAdapter(new TabViewPagerAdapter(getSupportFragmentManager(),
				fragmentLists, user));
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_main);
		vpMain = (ViewPager) findViewById(R.id.vp_main);
		llTabHome = (LinearLayout) findViewById(R.id.ll_tab_home);
		llTabPark = (LinearLayout) findViewById(R.id.ll_tab_park);
		llTabPerson = (LinearLayout) findViewById(R.id.ll_tab_person);
		ivTabHome = (ImageView) findViewById(R.id.ivTabHome);
		ivTabPark = (ImageView) findViewById(R.id.ivTabPark);
		ivTabPerson = (ImageView) findViewById(R.id.ivTabPerson);
		showLightTab(0);// 将第一个页签选中
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_tab_home:
			vpMain.setCurrentItem(0);
			showLightTab(0);
			break;
		case R.id.ll_tab_park:
			vpMain.setCurrentItem(1);
			showLightTab(1);
			break;
		case R.id.ll_tab_person:
			vpMain.setCurrentItem(2);
			showLightTab(2);
			break;
		}
	}

	/**
	 * 根据当前ViewPager显示的页码来高亮显示tab导航
	 * 
	 * @param position当前页面的位置
	 */
	private void showLightTab(int position) {
		ivTabHome.setEnabled(false);
		ivTabPark.setEnabled(false);
		ivTabPerson.setEnabled(false);
		switch (position) {
		case 0:
			ivTabHome.setEnabled(true);
			break;
		case 1:
			ivTabPark.setEnabled(true);
			break;
		case 2:
			ivTabPerson.setEnabled(true);
			break;
		}
	}
}
