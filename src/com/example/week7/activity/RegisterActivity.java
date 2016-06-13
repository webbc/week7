package com.example.week7.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.week7.dao.SchoolDao;
import com.example.week7.dao.UserDao;
import com.example.week7.domain.School;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * 注册界面
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	protected static final int SHOW_DIALOG = 0;// 选择学校对话框
	private static final int EMPTY_DATA = 1;// 加载数据为空
	private static final int REQUEST_FAIL = 2;// 请求失败
	private static final int REQUEST_ERROR = 3;// 请求错误
	protected static final String tag = "TAG";
	private static final int REG_FAIL = 4;// 注册失败
	private static final int REG_SUCCESS = 5;// 注册成功
	private EditText etPhone;
	private EditText etVcode;
	private EditText etPassword;
	private EditText etRpassword;
	private TextView tvGetVcode;
	private TextView tvGetSchool;
	private ImageView ivBack;
	private Button btnReg;
	private String mPhone;
	private String mVcode;
	private String mPassword;
	private String mRpassword;
	private UserDao userDao;
	private ProgressDialog progressDialog;
	private TextView tvSchool;

	private int mChoosedItem = 0;// 当前单选对话框选中的学校条目
	private int mSid;// 当前选中的学校在数据库中的id

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_DIALOG:
				showChooseSchoolDialog((List<School>) msg.obj);// 显示选择学校的对话框
				break;
			case EMPTY_DATA:
			case REQUEST_FAIL:
			case REQUEST_ERROR:
			case REG_FAIL:
				ToastUtils.showToast(RegisterActivity.this, (String) msg.obj);
				break;
			case REG_SUCCESS:
				ToastUtils.showToast(RegisterActivity.this, (String) msg.obj);
				// 打开登录界面
				Intent intent = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();// 销毁注册界面
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
		btnReg.setOnClickListener(this);
		tvGetVcode.setOnClickListener(this);
		tvGetSchool.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_reg);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etVcode = (EditText) findViewById(R.id.et_vcode);
		etPassword = (EditText) findViewById(R.id.et_password);
		etRpassword = (EditText) findViewById(R.id.et_rpassword);
		tvGetVcode = (TextView) findViewById(R.id.tv_get_vcode);
		tvGetSchool = (TextView) findViewById(R.id.tv_get_school);
		btnReg = (Button) findViewById(R.id.btn_reg);
		tvSchool = (TextView) findViewById(R.id.tv_school);
		ivBack = (ImageView) findViewById(R.id.iv_back);
	}

	/**
	 * 获取输入的数据
	 */
	private void getInputData() {
		mPhone = etPhone.getText().toString().trim();
		mVcode = etVcode.getText().toString().trim();
		mPassword = etPassword.getText().toString().trim();
		mRpassword = etRpassword.getText().toString().trim();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 注册按钮
		case R.id.btn_reg:
			getInputData();
			if (checkData()) {
				userDao = new UserDao(this);
				showProgressDialog();
				new Thread() {
					public void run() {
						SystemClock.sleep(2000);
						Map<String, Object> regReturnMap = userDao.register(
								mPhone, mPassword, mSid);
						closeProgressDialog();
						updateUI(regReturnMap, "reg");
					};
				}.start();

			}
			break;
		// 发送验证码
		case R.id.tv_get_vcode:
			break;
		// 获取学校
		case R.id.tv_get_school:
			showProgressDialog();
			new Thread() {
				public void run() {
					Map<String, Object> schoolReturnMap = new SchoolDao(
							RegisterActivity.this).getSchool();
					closeProgressDialog();
					updateUI(schoolReturnMap, "getSchool");
				};
			}.start();
			break;
		// 点击标题栏的返回按钮
		case R.id.iv_back:
			finish();
			break;
		}
	}

	/**
	 * 根据获取的数据刷新UI
	 * 
	 * @param returnMap返回的数据
	 * @param string类型
	 */
	protected void updateUI(Map<String, Object> returnMap, String type) {
		Message message = handler.obtainMessage();
		// 请求错误
		if (returnMap == null) {
			message.what = REQUEST_ERROR;
			message.obj = "请求错误";
			handler.sendMessage(message);
			return;
		}
		if (type.equals("getSchool")) {
			int respCode = (Integer) returnMap.get("respCode");
			ArrayList<School> schoolList = (ArrayList<School>) returnMap
					.get("schoolList");
			// 请求成功
			if (respCode == 1) {
				// 数据不为空
				if (schoolList.size() > 0) {
					message.what = SHOW_DIALOG;
					message.obj = schoolList;
					handler.sendMessage(message);
				} else {
					// 数据为空
					message.what = EMPTY_DATA;
					message.obj = "学校数据为空";
					handler.sendMessage(message);
				}
			} else {
				// 请求失败
				message.obj = returnMap.get("respMsg");
				message.what = REQUEST_FAIL;
				handler.sendMessage(message);
			}
		} else if (type.equals("reg")) {
			int respCode = (Integer) returnMap.get("respCode");
			// 注册成功
			if (respCode == 1) {
				message.obj = returnMap.get("respMsg");
				message.what = REG_SUCCESS;
				handler.sendMessage(message);
			} else {
				// 注册失败
				message.obj = returnMap.get("respMsg");
				message.what = REG_FAIL;
				handler.sendMessage(message);
			}
		}

	}

	/**
	 * 检测数据的合法性
	 * 
	 * @return
	 */
	private boolean checkData() {
		if (TextUtils.isEmpty(mPhone)) {
			ToastUtils.showToast(this, "手机号不能为空");
			return false;
		}
		if (TextUtils.isEmpty(mPassword)) {
			ToastUtils.showToast(this, "密码不能为空");
			return false;
		}
		if (TextUtils.isEmpty(mRpassword)) {
			ToastUtils.showToast(this, "确认密码不能为空");
			return false;
		}
		if (!VerificationFormat.isMobile(mPhone)) {
			ToastUtils.showToast(this, "手机号格式不正确");
			return false;
		}
		if (!mPassword.equals(mRpassword)) {
			ToastUtils.showToast(this, "两次密码输入不一致");
			return false;
		}
		if (mSid == 0) {
			ToastUtils.showToast(this, "请选择学校");
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
	 * 显示选择学校的对话框
	 * 
	 * @param obj
	 */
	private void showChooseSchoolDialog(final List<School> schoolList) {
		AlertDialog.Builder builder = new Builder(this);
		final String items[] = new String[schoolList.size()];
		for (int i = 0; i < schoolList.size(); i++) {
			items[i] = schoolList.get(i).getName();
		}
		builder.setTitle("请选择学校");
		builder.setSingleChoiceItems(items, mChoosedItem,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tvSchool.setText(items[which]);// 显示选中的学校
						mSid = schoolList.get(which).getId();// 设置要注册学校的id
						mChoosedItem = which;// 设置点击的学校的条目，以便下次直接选中
						dialog.dismiss();// 关闭对话框
					}
				});
		builder.setCancelable(false);
		builder.show();
	}
}
