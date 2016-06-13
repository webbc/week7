package com.example.week7.activity;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.week7.R;
import com.example.week7.adapter.CommentListViewAdapter;
import com.example.week7.dao.ParkDao;
import com.example.week7.domain.ParkComment;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.RefreshListView;

/**
 * 微博评论界面
 * 
 * @author Administrator
 * 
 */
public class ParkCommentActivity extends BaseActivity implements
		OnClickListener {
	private static final int COMMENT_SUCCESS = 0;// 评论成功
	private static final int COMMENT_ERROR = 1;// 评论失败
	private static final int LOAD_COMMENT_SUCCESS = 2;// 加载评论成功
	private static final int LOAD_COMMENT_ERROR = 3;// 加载评论失败
	private static final int EMPTY_DATA = 4;// 评论数据为空
	private Button btnOk;
	private EditText etContent;
	private ListView lvComment;
	private String pid;
	private ArrayList<ParkComment> commentList;
	private Handler handler = new Handler() {

		private CommentListViewAdapter adapter;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 评论成功
			case COMMENT_SUCCESS:
				etContent.setText("");
				ToastUtils.showToast(ParkCommentActivity.this, "评论成功");
				initData();
				break;
			// 评论失败
			case COMMENT_ERROR:
				ToastUtils.showToast(ParkCommentActivity.this, "评论失败");
				break;
			// 加载评论列表成功
			case LOAD_COMMENT_SUCCESS:
				adapter = new CommentListViewAdapter(ParkCommentActivity.this,
						commentList);
				lvComment.setAdapter(adapter);
				break;
			// 加载评论列表失败
			case LOAD_COMMENT_ERROR:
				ToastUtils.showToast(ParkCommentActivity.this, "加载评论失败");
				break;
			// 评论数据为空
			case EMPTY_DATA:
				ToastUtils.showToast(ParkCommentActivity.this, "当前微博暂时没有任何评论");
				break;
			default:
				ToastUtils.showToast(ParkCommentActivity.this, "请求错误");
				break;
			}
		}

	};
	private int comment_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		comment_number = Integer.parseInt(getIntent().getStringExtra(
				"comment_number"));
		initView();
		initData();
		initListener();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		pid = getIntent().getStringExtra("pid");
		new Thread() {

			public void run() {
				commentList = new ParkDao().getCommentList(pid, 0,
						Integer.MAX_VALUE);
				updateCommentList(commentList);
			};
		}.start();
	}

	@Override
	public void onBackPressed() {
		back();
	}

	@Override
	public void onBack(View view) {
		back();
	}

	/**
	 * 返回parkfragment时，进行刷新数据
	 */
	private void back() {
		Intent data = new Intent();
		data.putExtra("comment_number", comment_number);
		data.putExtra("position", getIntent().getIntExtra("position", 0));
		setResult(RESULT_OK, data);
		finish();
	}

	/**
	 * 更新评论列表
	 * 
	 * @param commentList
	 */
	protected void updateCommentList(ArrayList<ParkComment> commentList) {
		Message message = handler.obtainMessage();
		if (commentList != null) {
			if (commentList.size() > 0) {
				message.what = LOAD_COMMENT_SUCCESS;
			} else {
				message.what = EMPTY_DATA;
			}
		} else {
			message.what = LOAD_COMMENT_ERROR;
		}
		handler.sendMessage(message);
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		btnOk.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_park_comment);
		btnOk = (Button) findViewById(R.id.btn_ok);
		etContent = (EditText) findViewById(R.id.et_content);
		lvComment = (ListView) findViewById(R.id.lv_comment);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 确定按钮
		case R.id.btn_ok:
			final String content = etContent.getText().toString();
			if (checkData(content)) {
				new Thread() {
					public void run() {
						Map<String, Object> returnMap = new ParkDao()
								.writeComment(phone, pid, content);
						updateUI(returnMap);
					};
				}.start();
			}
			break;
		}
	}

	/**
	 * 刷新UI
	 * 
	 * @param returnMap
	 */
	protected void updateUI(Map<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.what = COMMENT_SUCCESS;
				comment_number++;
			} else {
				message.what = COMMENT_ERROR;
			}
		} else {
			message.what = COMMENT_ERROR;
		}
		handler.sendMessage(message);
	}

	/**
	 * 检测数据输入的合法性
	 * 
	 * @param content
	 * @return
	 */
	private boolean checkData(String content) {
		if (TextUtils.isEmpty(content)) {
			ToastUtils.showToast(this, "还是说点东西吧~");
			return false;
		}
		if (content.length() > 144) {
			ToastUtils.showToast(this, "评论字数大于144个字");
			return false;
		}
		return true;
	}
}
