package com.example.week7.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.example.week7.R;
import com.example.week7.activity.NewsDetailActivity;
import com.example.week7.adapter.NewsListViewAdapter;
import com.example.week7.dao.NewsDao;
import com.example.week7.domain.News;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.RefreshListView;
import com.example.week7.view.RefreshListView.onRefreshListener;

/**
 * 包裹新闻列表的Fragment
 * 
 * @author Administrator
 * 
 */
public class NewsFragment extends Fragment implements OnItemClickListener {
	private static final int LOAD_FAIL = 0;// 加载失败
	private static final int EMPTY_DATA = 1;// 数据为空
	private static final int LOAD_SUCCESS = 2;// 加载成功
	private static final int LOAD_MORE_SUCCESS = 3;// 加载更多成功
	private int page = 1;// 当前请求新闻页数，默认是第一页
	private RefreshListView lvNews;
	private LinearLayout llProgress;
	private ArrayList<News> newsList;// 请求网络的新闻数据集合
	private int position;// 当前viewPager切换的Fragment的页数
	private NewsListViewAdapter adapter;// ListView的数据适配器
	private String reuestActivity;// 那个Activity请求了这个Fragment
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 加载失败
			case LOAD_FAIL:
				// 数据为空
			case EMPTY_DATA:
				lvNews.onRefreshComplete(false);
				llProgress.setVisibility(View.GONE);
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			// 加载成功
			case LOAD_SUCCESS:
				lvNews.onRefreshComplete(true);
				llProgress.setVisibility(View.GONE);
				adapter = new NewsListViewAdapter(getActivity(), newsList,
						reuestActivity);
				lvNews.setAdapter(adapter);
				break;
			// 加载更多成功
			case LOAD_MORE_SUCCESS:
				if (adapter != null) {
					lvNews.onRefreshComplete(true);
					adapter.notifyDataSetChanged();
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
		View view = initView(inflater);
		initListener();
		return view;
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		lvNews.setOnRefreshListener(new onRefreshListener() {

			@Override
			public void onRefresh() {
				refresh();
			}

			@Override
			public void onLoadMore() {
				loadMore();
			}
		});
	}

	/**
	 * 加载更多数据
	 */
	private void loadMore() {
		new Thread() {
			public void run() {
				SystemClock.sleep(2000);
				page++;// 请求下一页
				ArrayList<News> newNewsList = null;
				if (reuestActivity.equals("edu")) {
					newNewsList = new NewsDao().getEduNews(page, position);
				} else if (reuestActivity.equals("job")) {
					newNewsList = new NewsDao().getJobNews(page, position);
				}
				Message message = handler.obtainMessage();
				if (newNewsList == null || newNewsList.size() <= 0) {
					message.what = EMPTY_DATA;
					message.obj = "没有更多数据了";
				} else {
					message.what = LOAD_MORE_SUCCESS;
				}
				handler.sendMessage(message);
			};
		}.start();
	}

	/**
	 * 下拉刷新
	 */
	private void refresh() {
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		new Thread() {
			public void run() {
				SystemClock.sleep(2000);
				// 当下拉刷新时重新设置请求的页数为第一页
				page = 1;
				if (reuestActivity.equals("edu")) {
					newsList = new NewsDao().getEduNews(page, position);
				} else if (reuestActivity.equals("job")) {
					newsList = new NewsDao().getJobNews(page, position);
				}
				updateUI(newsList);
			};
		}.start();
	}

	/**
	 * 加载数据之后，刷新UI
	 * 
	 * @param newsList这一次所请求到的数据
	 * 
	 * @param type刷新界面的类型
	 * 
	 * @param newsList
	 */
	protected void updateUI(ArrayList<News> newsList) {
		Message message = handler.obtainMessage();
		if (newsList != null) {
			if (newsList.size() > 0) {
				message.what = LOAD_SUCCESS;
				message.obj = newsList;
			} else {
				message.what = EMPTY_DATA;
				message.obj = "数据为空";
			}
		} else {
			message.what = LOAD_FAIL;
			message.obj = "加载失败";
		}
		handler.sendMessage(message);
	}

	/**
	 * 初始化UI
	 * 
	 * @param inflater
	 * 
	 * @return
	 */
	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_news, null);
		lvNews = (RefreshListView) view.findViewById(R.id.lv_news);
		llProgress = (LinearLayout) view.findViewById(R.id.ll_progress);
		lvNews.setOnItemClickListener(this);
		// 获取当前切换的是那个界面
		Bundle bundle = getArguments();
		position = bundle.getInt("position");
		reuestActivity = bundle.getString("reuestActivity");

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

	// ListView的条目点击事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("url", newsList.get(position).getUrl());
		bundle.putString("title", newsList.get(position).getTitle());
		bundle.putString("number", newsList.get(position).getNumber());
		bundle.putString("date", newsList.get(position).getDate());
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
