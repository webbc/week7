package com.example.week7.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.week7.R;
import com.example.week7.activity.ParkPublishActivity;
import com.example.week7.adapter.ParkListViewAdapter;
import com.example.week7.dao.ParkDao;
import com.example.week7.domain.Park;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.RefreshListView;
import com.example.week7.view.RefreshListView.onRefreshListener;

/**
 * 广场Fragment
 * 
 * @author Administrator
 * 
 */
public class ParkFragment extends Fragment implements OnClickListener {
	private static final int LOAD_FAIL = 0;// 加载失败
	private static final int LOAD_SUCCESS = 1;// 加载成功
	protected static final int EMPTY_DATA = 2;// 加载更多数据为空
	protected static final int LOAD_MORE_SUCCESS = 3;// 加载更多成功
	private RefreshListView lvPark;
	private Button btnPublish;
	private int count = 10;
	private int start = 0;
	private ArrayList<Park> parkList;// 从网络上获取的数据
	private ParkListViewAdapter adapter;// 数据适配器

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_FAIL:
				lvPark.onRefreshComplete(false);
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			case LOAD_SUCCESS:
				lvPark.onRefreshComplete(true);
				adapter = new ParkListViewAdapter(ParkFragment.this,
						getActivity(), parkList, getArguments().getString(
								"phone"));
				lvPark.setAdapter(adapter);
				break;
			case EMPTY_DATA:
				lvPark.onRefreshComplete(false);
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			case LOAD_MORE_SUCCESS:
				ArrayList<Park> newParkList = (ArrayList<Park>) msg.obj;
				parkList.addAll(newParkList);
				if (adapter != null) {
					adapter.notifyDataSetChanged();
					lvPark.onRefreshComplete(true);
				}
				break;
			default:
				ToastUtils.showToast(getActivity(), "加载错误");
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView();
		initData(false);
		initListener();
		return view;
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		btnPublish.setOnClickListener(this);
		lvPark.setOnRefreshListener(new onRefreshListener() {

			@Override
			public void onRefresh() {
				initData(true);
			}

			@Override
			public void onLoadMore() {
				initMoreData();
			}
		});
	}

	/**
	 * 加载更多数据
	 */
	protected void initMoreData() {
		// 从网络上获取广场信息
		new Thread() {

			public void run() {
				SystemClock.sleep(2000);
				start = parkList.size();
				ArrayList<Park> newParkList = new ParkDao().getPark(
						getArguments().getString("phone"), start, count);
				Message message = handler.obtainMessage();
				if (newParkList == null || newParkList.size() <= 0) {
					message.what = EMPTY_DATA;
					message.obj = "没有更多数据了";
				} else {
					message.what = LOAD_MORE_SUCCESS;
					message.obj = newParkList;
				}
				handler.sendMessage(message);
			};
		}.start();
	}

	/**
	 * 加载数据
	 */
	private void initData(final boolean isDelay) {
		// 从网络上获取广场信息
		new Thread() {

			public void run() {
				if (isDelay) {
					SystemClock.sleep(2000);
				}
				start = 0;
				parkList = new ParkDao().getPark(
						getArguments().getString("phone"), start, count);
				updateUI(parkList);
			};
		}.start();
	}

	/**
	 * 更新UI
	 * 
	 * @param parkList
	 */
	protected void updateUI(ArrayList<Park> parkList) {
		Message message = handler.obtainMessage();
		if (parkList == null || parkList.size() <= 0) {
			message.what = LOAD_FAIL;
			message.obj = "加载失败";
		} else {
			message.what = LOAD_SUCCESS;
		}
		handler.sendMessage(message);
	}

	/**
	 * 初始化UI
	 * 
	 * @return
	 */
	private View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_park, null);
		btnPublish = (Button) view.findViewById(R.id.btn_publish);
		lvPark = (RefreshListView) view.findViewById(R.id.lv_park);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 发布
		case R.id.btn_publish:
			Intent intent = new Intent(getActivity(), ParkPublishActivity.class);
			startActivityForResult(intent, Activity.RESULT_FIRST_USER);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 如果从发布页面返回，就重新刷新界面
		if (resultCode == Activity.RESULT_OK) {
			// 说明是进行评论操作
			if (requestCode == 1000) {
				int comment_number = data.getIntExtra("comment_number", 0);
				int position = data.getIntExtra("position", 0);
				parkList.get(position).setComment_count(comment_number + "");
				adapter.notifyDataSetChanged();
				return;
			}
			initData(false);
		}
	}
}
