package com.example.week7.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.example.week7.R;
import com.example.week7.adapter.OrderTabPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 订单列表界面
 * 
 * @author Administrator
 * 
 */
public class OrderListActivity extends BaseActivity implements
		OnClickListener {
	private ViewPager vpOrderTab;// 订单的ViewPager
	private ImageView ivBack;// 后退按钮
	private TabPageIndicator indicator;
	private String[] tabs = new String[] { "已完成", "正在审核", "正在取件", "正在派件", "已取消" };
	private OrderTabPagerAdapter orderTabPagerAdapter;// 数据适配器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initListener();
	}

	/**
	 * 绑定监听器
	 */
	private void initListener() {
		ivBack.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_order_list);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		// 初始化ViewPager对象
		vpOrderTab = (ViewPager) findViewById(R.id.vp_order_tab);
		orderTabPagerAdapter = new OrderTabPagerAdapter(
				getSupportFragmentManager(), tabs);
		vpOrderTab.setOffscreenPageLimit(tabs.length);
		vpOrderTab.setAdapter(orderTabPagerAdapter);
		// 初始化tabpageIndicator对象
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		// 让两者进行关联
		indicator.setViewPager(vpOrderTab);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		}
	}

}
