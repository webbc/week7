package com.example.week7.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.week7.R;
import com.example.week7.activity.OrderDetailActivity;
import com.example.week7.activity.OrderListActivity;
import com.example.week7.adapter.OrderListAdapter;
import com.example.week7.dao.ExpressDao;
import com.example.week7.domain.Express;
import com.example.week7.utils.ToastUtils;

/**
 * 订单列表的Fragment
 * 
 * @author Administrator
 * 
 */
public class OrderListFragment extends Fragment {
	private static final int REQUEST_SUCCESS = 0;// 请求成功
	private static final int REQUEST_FAIL = 1;// 请求失败
	private static final int REQUEST_ERROR = 2;// 请求错误
	private static final int DATA_EMPTY = 4;// 请求的数据为空
	private ListView lvOrder;// 订单ListView
	private OrderListAdapter adapter;// ListView的数据适配器

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_SUCCESS:
				llProgress.setVisibility(View.GONE);
				adapter = new OrderListAdapter(getActivity(), expressLists);
				lvOrder.setAdapter(adapter);
				lvOrder.setOnItemClickListener(adapter);
				break;
			case REQUEST_FAIL:
			case REQUEST_ERROR:
			case DATA_EMPTY:
				llProgress.setVisibility(View.GONE);
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			}
		}

	};
	private ArrayList<Express> expressLists;
	private LinearLayout llProgress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		return view;
	}

	/**
	 * 初始化UI
	 * 
	 * @param inflater
	 */
	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_order_tab, null);
		lvOrder = (ListView) view.findViewById(R.id.lv_order);
		llProgress = (LinearLayout) view.findViewById(R.id.ll_progress);
		return view;
	}

	/**
	 * 在这里实现Fragment数据的缓加载.
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			onVisible();
		} else {
			onInvisible();
		}
	}

	/**
	 * Fragment中隐藏的时候调用
	 */
	private void onInvisible() {

	}

	/**
	 * Fragment显示的时候调用
	 */
	private void onVisible() {
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 获取要请求的状态
		Bundle arguments = getArguments();
		final int state = arguments.getInt("args");// position：订单的状态
		// 获取用户信息
		SharedPreferences sp = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		final String phone = sp.getString("phone", "");

		// 获取所有的订单信息
		new Thread() {

			public void run() {
				SystemClock.sleep(2000);
				Map<String, Object> returnMap = new ExpressDao().getAllOrder(
						phone, state);
				updataUI(returnMap);
			};
		}.start();
	}

	/**
	 * 更新界面
	 * 
	 * @param returnMap从网络上请求的数据
	 */
	protected void updataUI(Map<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				expressLists = (ArrayList<Express>) returnMap.get("data");
				if (expressLists.size() > 0) {
					message.what = REQUEST_SUCCESS;
				} else {
					message.what = DATA_EMPTY;
					message.obj = "数据为空";
				}
			} else {
				message.what = REQUEST_FAIL;
				message.obj = returnMap.get("resgMsg");
			}
		} else {
			message.what = REQUEST_ERROR;
			message.obj = "请求错误";
		}
		handler.sendMessage(message);
	}

	/**
	 * 当重新显示Fragment的时候，刷新ListView的数据
	 */
	@Override
	public void onStart() {
		super.onStart();
		if (adapter != null && !OrderDetailActivity.isPressBack) {
			// 删除条目并刷新UI
			adapter.deleteItem();
			adapter.notifyDataSetChanged();
		}
	}
}
