package com.example.week7.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.week7.R;
import com.example.week7.adapter.LostfoundListViewAdapter;
import com.example.week7.dao.LostFoundDao;
import com.example.week7.domain.Lostfound;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.RefreshListView;
import com.example.week7.view.RefreshListView.onRefreshListener;

/**
 * 失物招领界面
 * 
 * @author Administrator
 * 
 */
public class LostFoundActivity extends BaseActivity implements OnClickListener,
		onRefreshListener, OnItemClickListener {
	private static final int FRESH_SUCCESS = 0;// 刷新成功
	private static final int EMPTY_DATA = 1;// 数据为空
	private static final int LOAD_FAIL = 2;// 加载失败
	private static final int LOAD_MORE_SUCCESS = 3;// 加载更多成功
	private ImageView ivBack;
	private Button btnPublish;
	private RefreshListView lvLostfound;
	private int start = 0;
	private int count = 10;
	private ArrayList<Lostfound> lostfoundList;// 请求的数据
	private LostfoundListViewAdapter adapter;// ListView的数据适配器
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_SUCCESS:
				llProgress.setVisibility(View.GONE);
				lvLostfound.onRefreshComplete(true);
				adapter = new LostfoundListViewAdapter(LostFoundActivity.this,
						lostfoundList);
				lvLostfound.setAdapter(adapter);
				break;
			case EMPTY_DATA:
			case LOAD_FAIL:
				lvLostfound.onRefreshComplete(false);
				ToastUtils.showToast(LostFoundActivity.this, (String) msg.obj);
				break;
			case LOAD_MORE_SUCCESS:
				lvLostfound.onRefreshComplete(true);
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				ToastUtils.showToast(LostFoundActivity.this, "请求错误");
				break;
			}
		}

	};
	private LinearLayout llProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initData();
		initListener();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		new Thread() {

			public void run() {
				SystemClock.sleep(2000);
				start = 0;
				lostfoundList = new LostFoundDao().getLostfound(start, count);
				updateUI(lostfoundList);
			};
		}.start();
	}

	/**
	 * 根据请求的数据来更新界面
	 * 
	 * @param newLostfoundList
	 * 
	 * @param type刷新界面的类型
	 * 
	 * @param lostfoundList
	 */
	protected void updateUI(ArrayList<Lostfound> lostfoundList) {
		Message message = handler.obtainMessage();
		if (lostfoundList != null) {
			if (lostfoundList.size() > 0) {
				message.what = FRESH_SUCCESS;
			} else {
				message.what = EMPTY_DATA;
				message.obj = "数据为空";
			}
		} else {
			message.what = LOAD_FAIL;
			message.obj = "请求失败";
		}
		handler.sendMessage(message);
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		ivBack.setOnClickListener(this);
		btnPublish.setOnClickListener(this);
		lvLostfound.setOnRefreshListener(this);
		lvLostfound.setOnItemClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_lostfound);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		btnPublish = (Button) findViewById(R.id.btn_publish);
		lvLostfound = (RefreshListView) findViewById(R.id.lv_lostfound);
		llProgress = (LinearLayout) findViewById(R.id.ll_progress);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 标题栏的退出按钮
		case R.id.iv_back:
			finish();
			break;
		// 发布按钮
		case R.id.btn_publish:
			Intent intent = new Intent(LostFoundActivity.this,
					LostfoundPulishActivity.class);
			startActivityForResult(intent, 0);
			break;
		}
	}

	// 从另外一个activity返回时回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			// 当从另外一个界面传过来数据时，把这个数据放置在集合的首位，然后刷新ListView
			Lostfound lostfound = (Lostfound) data.getSerializableExtra("data");
			lostfoundList.add(0, lostfound);
			adapter.notifyDataSetChanged();
			break;
		}
	}

	// 刷新时回调
	@Override
	public void onRefresh() {
		initData();
	}

	// 加载更多是回调
	@Override
	public void onLoadMore() {
		initMoreData();
	}

	/**
	 * 加载更多数据
	 */
	private void initMoreData() {
		new Thread() {

			public void run() {
				Message message = handler.obtainMessage();
				SystemClock.sleep(2000);
				start = lostfoundList.size();
				ArrayList<Lostfound> newLostfoundList = new LostFoundDao()
						.getLostfound(start, count);
				if (newLostfoundList == null || newLostfoundList.size() <= 0) {
					message.what = EMPTY_DATA;
					message.obj = "没有更多数据了";
				} else {
					newLostfoundList.addAll(lostfoundList);
					message.what = LOAD_MORE_SUCCESS;
				}
				handler.sendMessage(message);
			};
		}.start();
	}

	// 条目点击事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, LostfoundDetailActivity.class);
		intent.putExtra("id", lostfoundList.get(position).getId());
		intent.putExtra("tel", lostfoundList.get(position).getTel());
		startActivity(intent);
	}

}
