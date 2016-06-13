package com.example.week7.activity;

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

import com.example.week7.R;
import com.example.week7.dao.ParkDao;
import com.example.week7.utils.ToastUtils;

/**
 * 转发微博界面
 * 
 * @author Administrator
 * 
 */
public class ParkZhuanfaActivity extends BaseActivity implements
		OnClickListener {
	private static final int ZHUANFA_SUCCESS = 0;// 转发成功
	private static final int ZHUANFA_ERROR = 1;// 转发失败
	private EditText etContent;
	private Button btnZhuanfa;
	private String pid;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ZHUANFA_SUCCESS:
				ToastUtils.showToast(ParkZhuanfaActivity.this, "转发成功");
				Intent data = new Intent();
				setResult(RESULT_OK, data);
				finish();
				break;
			case ZHUANFA_ERROR:
				ToastUtils.showToast(ParkZhuanfaActivity.this, "转发失败");
				break;
			default:
				ToastUtils.showToast(ParkZhuanfaActivity.this, "请求错误");
				break;
			}
		}

	};

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
		pid = getIntent().getStringExtra("pid");
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		btnZhuanfa.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_park_zhuanfa);
		etContent = (EditText) findViewById(R.id.et_content);
		btnZhuanfa = (Button) findViewById(R.id.btn_zhuanfa);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 转发按钮
		case R.id.btn_zhuanfa:
			final String content = etContent.getText().toString().trim();
			if (checkData(content)) {
				new Thread() {
					public void run() {
						Map<String, Object> returnMap = new ParkDao().zhuanfa(
								phone, pid, content);
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
				message.what = ZHUANFA_SUCCESS;
			} else {
				message.what = ZHUANFA_ERROR;
			}
		} else {
			message.what = ZHUANFA_ERROR;
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
			ToastUtils.showToast(this, "分享字数大于144个字");
			return false;
		}
		return true;
	}
}
