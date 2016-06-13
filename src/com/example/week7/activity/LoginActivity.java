package com.example.week7.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.dao.UserDao;
import com.example.week7.domain.User;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * 登录界面
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private EditText etPhone;
	private EditText etPassword;
	private Button btnLogin;
	private TextView tvForget;
	private TextView tvReg;
	private String mPhone;// 输入的电话
	private String mPassword;// 输入的密码
	private static final String tag = "TAG";
	private static final int REQUEST_FAIL = 0;// 请求失败
	private static final int REQUEST_ERROR = 1;// 请求错误
	private static final int REQUEST_SUCCESS = 2;// 请求成功
	private static final int ERROR_USER_STATE = 3;// 错误的用户状态
	private ProgressDialog progressDialog;// 加载中的对话框
	private UserDao userDao;// 用户业务层对象

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERROR_USER_STATE:
			case REQUEST_ERROR:
			case REQUEST_FAIL:
				// 失败，弹出吐司对话框
				ToastUtils.showToast(LoginActivity.this, (String) msg.obj);
				break;
			case REQUEST_SUCCESS:
				// 登录成功，跳转页面
				ToastUtils.showToast(LoginActivity.this, "登录成功");
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
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
		btnLogin.setOnClickListener(this);
		tvForget.setOnClickListener(this);
		tvReg.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_login);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etPassword = (EditText) findViewById(R.id.et_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
		tvForget = (TextView) findViewById(R.id.tv_forget);
		tvReg = (TextView) findViewById(R.id.tv_reg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 登录
		case R.id.btn_login:
			getInputData();// 获取输入的数据
			// 检查数据的合法性
			if (checkData()) {
				userDao = new UserDao(this);
				showProgressDialog();// 开启加载更多对话框
				// 开启线程进行登录操作
				new Thread() {
					public void run() {
						SystemClock.sleep(2000);
						HashMap<String, Object> returnMap = userDao.login(
								mPhone, mPassword);// 进行登录操作
						closeProgressDialog();// 关闭加载更多的对话框
						updateUI(returnMap);
					};
				}.start();
			}
			break;
		// 注册
		case R.id.tv_reg:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		// 忘记密码
		case R.id.tv_forget:
			Intent i = new Intent(this, ForgetPasswordActivity.class);
			startActivity(i);
			break;
		}
	}

	/**
	 * 检查数据输入的合法性
	 * 
	 * @return
	 */
	private boolean checkData() {
		if (TextUtils.isEmpty(mPhone)) {
			ToastUtils.showToast(this, "用户名不能为空");
			return false;
		}
		if (TextUtils.isEmpty(mPassword)) {
			ToastUtils.showToast(this, "密码不能为空");
			return false;
		}
		if (!VerificationFormat.isMobile(mPhone)) {
			ToastUtils.showToast(this, "手机号格式不正确");
			return false;
		}
		return true;
	}

	/**
	 * 根据返回的数据，进行刷新UI
	 * 
	 * @param respMap返回的数据
	 */
	public void updateUI(Map<String, Object> respMap) {
		Message message = handler.obtainMessage();
		if (respMap == null) {
			// 请求错误
			message.what = REQUEST_ERROR;
			message.obj = "请求失败";
		} else {
			// 请求成功
			if ((Integer) respMap.get("respCode") == 1) {
				User user = (User) respMap.get("user");
				if (!user.isUseful()) {
					// 用户被拉黑
					message.what = ERROR_USER_STATE;
					message.obj = "用户状态为不可用，请联系管理员";
				} else {
					// 登录成功
					message.what = REQUEST_SUCCESS;
					message.obj = respMap.get("respMsg");
					userDao.saveUserInfo(user);// 登录成功后，保存用户信息
				}
			} else {
				// 登录失败
				message.what = REQUEST_FAIL;
				message.obj = respMap.get("respMsg");
			}
		}
		handler.sendMessage(message);
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
	 * 获取输入的数据
	 */
	private void getInputData() {
		mPhone = etPhone.getText().toString().trim();
		mPassword = etPassword.getText().toString().trim();
	}
}
