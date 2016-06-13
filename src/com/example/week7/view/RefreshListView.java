package com.example.week7.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.week7.R;

/**
 * 下拉刷新的ListView
 * 
 * @author Administrator
 * 
 */
public class RefreshListView extends ListView implements OnScrollListener,
		android.widget.AdapterView.OnItemClickListener {
	int startY = -1;// 用户触摸屏幕时的y坐标
	private int measuredHeight;// 头布局的高度
	private View headerView;// 头布局view
	private ImageView iv_arr;// 下拉刷新的箭头
	private ProgressBar pb_refresh;// 下拉刷新的进度条
	private TextView tv_refresh_info;// 刷新状态的文字
	private TextView tv_time;// 最后刷新时间
	public static final int STATE_PULL_REFRESH = 0;// 下拉刷新
	public static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
	public static final int STATE_REFRESHING = 2;// 正在刷新
	private int currentState = STATE_PULL_REFRESH;// 当前的刷新状态
	private RotateAnimation upAnimation;
	private RotateAnimation downAnimation;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}

	/**
	 * 初始化头布局
	 */
	private void initHeaderView() {
		headerView = View.inflate(getContext(),
				R.layout.listview_header_refresh, null);
		iv_arr = (ImageView) headerView.findViewById(R.id.iv_arr);
		pb_refresh = (ProgressBar) headerView.findViewById(R.id.pb_refresh);
		tv_time = (TextView) headerView.findViewById(R.id.tv_time);
		tv_refresh_info = (TextView) headerView
				.findViewById(R.id.tv_refresh_info);
		headerView.measure(0, 0);
		measuredHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -measuredHeight, 0, 0);
		addHeaderView(headerView);
		initAnimation();
		tv_time.setText("最后时间：" + getCurrentTime());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			// 确保startY中有值
			if (startY == -1) {
				startY = (int) ev.getRawY();
			}
			if (currentState == STATE_REFRESHING) {
				break;
			}

			int endY = (int) ev.getRawY();
			int dy = endY - startY;
			// 确保手指是往下拉并且显示的是第一个view
			if (dy > 0 && getFirstVisiblePosition() == 0) {
				int padding = dy - measuredHeight;
				headerView.setPadding(0, padding, 0, 0);
				// 如果当前状态不是松开刷新并且padding大于0
				if (padding > 0 && currentState != STATE_RELEASE_REFRESH) {
					currentState = STATE_RELEASE_REFRESH;
					refreshState();
					// 如果当前状态不是下拉刷新并且padding小于0
				} else if (padding <= 0 && currentState != STATE_PULL_REFRESH) {
					currentState = STATE_PULL_REFRESH;
					refreshState();
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// 重置
			// 如果当前的状态是松开刷新,设置为正在刷新
			if (currentState == STATE_RELEASE_REFRESH) {
				currentState = STATE_REFRESHING;
				headerView.setPadding(0, 0, 0, 0);
				refreshState();
			} else if (currentState == STATE_PULL_REFRESH) {
				headerView.setPadding(0, -measuredHeight, 0, 0);
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 刷新下拉刷新的状态
	 */
	private void refreshState() {
		switch (currentState) {
		// 下拉刷新
		case STATE_PULL_REFRESH:
			tv_refresh_info.setText("下拉刷新");
			iv_arr.setVisibility(View.VISIBLE);
			iv_arr.startAnimation(downAnimation);
			pb_refresh.setVisibility(View.INVISIBLE);
			break;
		// 松开刷新
		case STATE_RELEASE_REFRESH:
			tv_refresh_info.setText("松开刷新");
			iv_arr.setVisibility(View.VISIBLE);
			iv_arr.startAnimation(upAnimation);
			pb_refresh.setVisibility(View.INVISIBLE);
			break;
		// 正在刷新
		case STATE_REFRESHING:
			tv_refresh_info.setText("正在刷新...");
			iv_arr.clearAnimation();// 在隐藏之前必须先销毁动画
			iv_arr.setVisibility(View.INVISIBLE);
			pb_refresh.setVisibility(View.VISIBLE);
			// 加载接口中的刷新方法
			if (listener != null) {
				listener.onRefresh();
			}
			break;

		}
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		upAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(300);
		upAnimation.setFillAfter(true);
		downAnimation = new RotateAnimation(-180, -360,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(300);
		upAnimation.setFillAfter(true);

	}

	/**
	 * 刷新完成的方法
	 * 
	 * @param b
	 * 
	 */
	public void onRefreshComplete(boolean success) {
		if (isLoadMore) {
			isLoadMore = false;
			footerView.setPadding(0, -footerViewHeight, 0, 0);
		} else {
			currentState = STATE_PULL_REFRESH;
			tv_refresh_info.setText("下拉刷新");
			iv_arr.setVisibility(View.VISIBLE);
			iv_arr.startAnimation(downAnimation);
			pb_refresh.setVisibility(View.INVISIBLE);
			headerView.setPadding(0, -measuredHeight, 0, 0);
			if (success) {
				tv_time.setText("最后时间：" + getCurrentTime());
			}
		}
	}

	public void setOnRefreshListener(onRefreshListener listener) {
		this.listener = listener;
	}

	private onRefreshListener listener;
	private View footerView;

	public interface onRefreshListener {
		public void onRefresh();

		public void onLoadMore();
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	private String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		footerView = View.inflate(getContext(),
				R.layout.listview_footer_refresh, null);
		this.addFooterView(footerView);
		footerView.measure(0, 0);
		footerViewHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		this.setOnScrollListener(this);
	}

	private boolean isLoadMore;
	private int footerViewHeight;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				|| scrollState == OnScrollListener.SCROLL_STATE_FLING) {
			if (getLastVisiblePosition() == getCount() - 1 && !isLoadMore) {
				// 显示脚布局
				Log.d("TAG", "到底啦...");
				footerView.setPadding(0, 0, 0, 0);
				setSelection(getCount());// 设置ListView显示最后一个
				isLoadMore = true;

				// 调用接口中的加载更多数据方法
				if (listener != null) {
					listener.onLoadMore();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	private OnItemClickListener onItemClickListener;

	// 重写ListView的setOnItemClickListener方法
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);
		onItemClickListener = listener;
	}

	// 重写onItemClick方法
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getAdapter().getCount() - 1 == position)
			return;
		onItemClickListener.onItemClick(parent, view, position
				- getHeaderViewsCount(), id);
	}

}
