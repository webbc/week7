package com.example.week7.activity;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week7.R;
import com.example.week7.constant.Config;
import com.example.week7.dao.ParttimeDao;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * 兼职详细界面
 * 
 * @author Administrator
 * 
 */
public class ParttimeDetailActivity extends BaseActivity implements OnClickListener {
	private static final int ISPAST = 0;// 兼职已过期
	private static final int ISFULL = 1;// 报名人数已满
	private static final int ISAPPLY = 2;// 已报名
	private static final int CANAPPLY = 3;// 可报名
	private static final int APPLY_SUCCESS = 4;// 报名成功
	private static final int APPLY_FAIL = 5;// 报名失败
	private static final int APPLY_ERROR = 6;// 报名错误
	private TextView tvTitle;
	private WebView webView;
	private Button btnApply;
	private int id;// 当前兼职在数据库中的id
	private LinearLayout llProgress;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 报名截止
			case ISPAST:
				btnApply.setVisibility(View.VISIBLE);
				btnApply.setEnabled(false);
				btnApply.setBackgroundColor(getResources().getColor(
						R.color.disable_button));
				btnApply.setText("报名已截止");
				break;
			// 报名已满
			case ISFULL:
				btnApply.setVisibility(View.VISIBLE);
				btnApply.setEnabled(false);
				btnApply.setBackgroundColor(getResources().getColor(
						R.color.disable_button));
				btnApply.setText("名额已满");
				break;
			// 已报名
			case ISAPPLY:
				btnApply.setVisibility(View.VISIBLE);
				btnApply.setEnabled(false);
				btnApply.setBackgroundColor(getResources().getColor(
						R.color.disable_button));
				btnApply.setText("已报名");
				break;
			// 可以报名
			case CANAPPLY:
				btnApply.setVisibility(View.VISIBLE);
				btnApply.setEnabled(true);
				int remainperson = (Integer) msg.obj;
				btnApply.setText("点我报名（剩余" + remainperson + "人）");
				break;
			// 报名成功
			case APPLY_SUCCESS:
				ToastUtils.showToast(ParttimeDetailActivity.this,
						(String) msg.obj);
				dialog.dismiss();
				handler.sendEmptyMessage(ISAPPLY);// 发送已报名消息
				break;
			// 报名失败
			case APPLY_FAIL:
				ToastUtils.showToast(ParttimeDetailActivity.this,
						(String) msg.obj);
				dialog.dismiss();
				break;
			// 报名错误
			case APPLY_ERROR:
				ToastUtils.showToast(ParttimeDetailActivity.this,
						(String) msg.obj);
				dialog.dismiss();
				break;
			default:
				ToastUtils.showToast(ParttimeDetailActivity.this, "加载错误");
				break;
			}
		}

	};
	private ImageView ivBack;
	private AlertDialog dialog;
	private EditText etPhone;
	private EditText etSid;
	private EditText etRoomid;
	private EditText etName;
	private String tel;
	private String sid;
	private String roomid;
	private String name;
	private String phone;

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
		ivBack.setOnClickListener(this);
		btnApply.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		id = (Integer) bundle.get("id");
		String title = (String) bundle.get("title");
		tvTitle.setText(title);
		SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		phone = sp.getString("phone", "");
		initWebView();// 初始化静态界面
		initApplyInfo();// 初始化报名数据
	}

	/**
	 * 初始化报名信息
	 */
	private void initApplyInfo() {
		new Thread() {
			public void run() {
				Map<String, Integer> returnMap = new ParttimeDao()
						.getApplyInfo(id, phone);
				updateUI(returnMap);
			};
		}.start();
	}

	/**
	 * 如果是查看报名情况后，刷新UI
	 * 
	 * @param returnMap
	 * @param type
	 */
	protected void updateUI(Map<String, Integer> returnMap) {
		if (returnMap != null) {
			int respCode = returnMap.get("respCode");
			if (respCode == 1) {
				int ispast = returnMap.get("ispast");
				int isfull = returnMap.get("isfull");
				int isapply = returnMap.get("isapply");
				int remainperson = returnMap.get("remainperson");
				// 判断兼职是否过期
				if (ispast == 1) {
					handler.sendEmptyMessage(ISPAST);
					return;
				}
				// 判断报名人数是否已满
				if (isfull == 1) {
					handler.sendEmptyMessage(ISFULL);
					return;
				}
				// 判断是否已报名
				if (isapply == 1) {
					handler.sendEmptyMessage(ISAPPLY);
					return;
				}
				// 可报名
				if (remainperson > 0) {
					Message message = handler.obtainMessage();
					message.what = CANAPPLY;
					message.obj = remainperson;
					handler.sendMessage(message);
					return;
				}
			}
		}
	}

	/**
	 * 初始化webview
	 */
	private void initWebView() {
		String url = Config.SERVER_URL + "?c=Parttime&a=get_ptjob_by_id&id="
				+ id;
		webView.loadUrl(url);
		// 设置javascript支持
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				llProgress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				llProgress.setVisibility(View.GONE);
			}

		});
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_parttime_detail);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		llProgress = (LinearLayout) findViewById(R.id.ll_progress);
		webView = (WebView) findViewById(R.id.webView);
		btnApply = (Button) findViewById(R.id.btn_apply);
		ivBack = (ImageView) findViewById(R.id.iv_back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 标题栏的退出按钮
		case R.id.iv_back:
			finish();
			break;
		// 报名按钮
		case R.id.btn_apply:
			showApplyDialog();
			break;
		// 取消报名按钮
		case R.id.btn_cancel:
			if (dialog != null) {
				dialog.dismiss();
			}
			break;
		// 确认报名按钮
		case R.id.btn_ok:
			if (checkData()) {
				// 向服务器写入报名信息
				new Thread() {
					public void run() {
						Map<String, Object> returnMap = new ParttimeDao()
								.writeParttimeJob(id, phone, tel, sid, roomid,
										name);
						updateApplyUI(returnMap);
					};
				}.start();
			}
			break;
		}
	}

	/**
	 * 报名之后刷新UI
	 * 
	 * @param returnMap
	 */
	private void updateApplyUI(Map<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			String respMsg = (String) returnMap.get("respMsg");
			if (respCode == 1) {
				int isapply = (Integer) returnMap.get("isapply");
				if (isapply == 1) {
					message.what = APPLY_SUCCESS;
					message.obj = returnMap.get("respMsg");
					handler.sendMessage(message);
				} else {
					message.what = APPLY_FAIL;
					message.obj = returnMap.get("respMsg");
					handler.sendMessage(message);
				}
			} else {
				message.what = APPLY_FAIL;
				message.obj = returnMap.get("respMsg");
				handler.sendMessage(message);
			}
		} else {
			message.what = APPLY_ERROR;
			message.obj = "报名错误";
			handler.sendMessage(message);
		}
	}

	/**
	 * 检测输入的信息
	 * 
	 * @return
	 */
	private boolean checkData() {
		tel = etPhone.getText().toString().trim();
		sid = etSid.getText().toString().trim();
		roomid = etRoomid.getText().toString().trim();
		name = etName.getText().toString().trim();
		if (TextUtils.isEmpty(tel)) {
			ToastUtils.showToast(this, "手机号不能为空");
			return false;
		}
		if (!VerificationFormat.isMobile(tel)) {
			ToastUtils.showToast(this, "手机号格式不正确");
			return false;
		}
		if (TextUtils.isEmpty(sid)) {
			ToastUtils.showToast(this, "学号不能为空");
			return false;
		}
		if (TextUtils.isEmpty(roomid)) {
			ToastUtils.showToast(this, "寝室号不能为空");
			return false;
		}
		if (TextUtils.isEmpty(name)) {
			ToastUtils.showToast(this, "真实姓名不能为空");
			return false;
		}
		return true;
	}

	/**
	 * 显示报名填写信息对话框
	 */
	private void showApplyDialog() {
		AlertDialog.Builder builder = new Builder(ParttimeDetailActivity.this);
		dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_write_parttime, null);
		etPhone = (EditText) view.findViewById(R.id.et_phone);
		etSid = (EditText) view.findViewById(R.id.et_sid);
		etRoomid = (EditText) view.findViewById(R.id.et_roomid);
		etName = (EditText) view.findViewById(R.id.et_name);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		dialog.setView(view);
		dialog.show();
	}
}
