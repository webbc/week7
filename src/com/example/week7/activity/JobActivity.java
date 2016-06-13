package com.example.week7.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.example.week7.R;
import com.example.week7.adapter.NewsPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 就业信息页面
 * 
 * @author Administrator
 * 
 */
public class JobActivity extends BaseActivity implements OnClickListener {
	private String[] data = { "校内招聘", "校外招聘", "最新信息", "通知公告", "就业指导", "就业政策",
			"课程建设" };
	private ImageView ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initListener();
	}

	private void initListener() {
		ivBack.setOnClickListener(this);
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		setContentView(R.layout.activity_job);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		ViewPager viewPager = (ViewPager) findViewById(R.id.vp_news);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		viewPager.setAdapter(new NewsPagerAdapter(getSupportFragmentManager(),
				data, "job"));
		viewPager.setOffscreenPageLimit(data.length);// 设置缓存3页数据
		indicator.setViewPager(viewPager);
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
