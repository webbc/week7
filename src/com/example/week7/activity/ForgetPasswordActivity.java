package com.example.week7.activity;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View; 
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.dao.UserDao;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * 忘记密码界面
 * 
 * @author Administrator
 * 
 */
public class ForgetPasswordActivity extends BaseActivity implements OnClickListener {
	private static final int REQUEST_ERROR = 0;// 请求错误
	private static final int REQUEST_FAIL = 1;// 请求失败
	private static final int REQUEST_SUCCESS = 3;// 请求成功
	private EditText etPhone;
	private EditText etVcode;
	private EditText etPassword;
	private EditText etRpassword;
	private Button btnEditPassword;
	private TextView tvGetVcode;
	private ImageView ivBack;

	private String mPhone;// 输入电话
	private String mPassword;// 输入的密码
	private String mRpassword;// 输入的确认密码
	private String mVcode;// 输入的验证码
	private ProgressDialog progressDialog;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_ERROR:
				break;
			case REQUEST_FAIL:
				ToastUtils.showToast(ForgetPasswordActivity.this,
						(String) msg.obj);
				break;
			case REQUEST_SUCCESS:
				ToastUtils.showToast(ForgetPasswordActivity.this,
						(String) msg.obj);
				Intent intent = new Intent(ForgetPasswordActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		}

	};

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
		tvGetVcode.setOnClickListener(this);
		btnEditPassword.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_forgetpassword);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etVcode = (EditText) findViewById(R.id.et_vcode);
		etPassword = (EditText) findViewById(R.id.et_password);
		etRpassword = (EditText) findViewById(R.id.et_rpassword);
		btnEditPassword = (Button) findViewById(R.id.btn_edit_password);
		tvGetVcode = (TextView) findViewById(R.id.tv_get_vcode);
		ivBack = (ImageView) findViewById(R.id.iv_back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击标题栏上的返回按钮
		case R.id.iv_back:
			finish();
			break;
		// 获取验证码
		case R.id.tv_get_vcode:
			break;
		// 修改密码
		case R.id.btn_edit_password:
			getInputData();
			if (checkData()) {
				// 让dao层帮忙请求网络
				final UserDao userDao = new UserDao(this);
				showProgressDialog();
				new Thread() {
					public void run() {
						SystemClock.sleep(2000);
						HashMap<String, Object> returnMap = userDao
								.editPassword(mPhone, mPassword);
						closeProgressDialog();
						updateUI(returnMap);
					};
				}.start();
			}
			break;
		}
	}

	/**
	 * 根据解析后的数据进行刷新UI
	 * 
	 * @param returnMap
	 */
	protected void updateUI(HashMap<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap == null) {
			message.what = REQUEST_ERROR;
			message.obj = "请求错误";
		} else {
			int respCode = (Integer) returnMap.get("respCode");
			// 修改密码失败
			if (respCode == 0) {
				message.what = REQUEST_FAIL;
				message.obj = returnMap.get("respMsg");
			} else {
				// 修改密码成功
				message.what = REQUEST_SUCCESS;
				message.obj = returnMap.get("respMsg");
			}
		}
		handler.sendMessage(message);
	}

	/**
	 * 检查数据输入的合法性
	 * 
	 * @return
	 */
	private boolean checkData() {
		if (TextUtils.isEmpty(mPhone)) {
			ToastUtils.showToast(this, "手机号不能为空");
			return false;
		}
		if (!VerificationFormat.isMobile(mPhone)) {
			ToastUtils.showToast(this, "手机号格式不正确");
			return false;
		}
		if (TextUtils.isEmpty(mPassword)) {
			ToastUtils.showToast(this, "新密码不能为空");
			return false;
		}
		if (TextUtils.isEmpty(mRpassword)) {
			ToastUtils.showToast(this, "确认密码不能为空");
			return false;
		}
		if (!mPassword.equals(mRpassword)) {
			ToastUtils.showToast(this, "两次密码不一致");
			return false;
		}
		return true;
	}

	/**
	 * 显示加载的对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载中...");
			progressDialog.show();
		}
	}

	/**
	 * 关闭对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * 获取输入的内容
	 */
	private void getInputData() {
		mPhone = etPhone.getText().toString().trim();
		mVcode = etVcode.getText().toString().trim();
		mPassword = etPassword.getText().toString().trim();
		mRpassword = etRpassword.getText().toString().trim();
	}
}
