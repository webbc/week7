package com.example.week7.activity;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.week7.R;
import com.example.week7.dao.ExpressDao;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * 寄快递界面
 * 
 * @author Administrator
 * 
 */
public class SendExpressActivity extends BaseActivity implements OnClickListener,
		OnItemSelectedListener {
	private static final int REQUEST_ERROR = 0;// 请求错误
	private static final int REQUEST_SUCCESS = 1;// 请求成功
	private static final int REQUEST_FAIL = 2;// 请求失败
	private ImageView ivBack;
	private Spinner spShop;
	private EditText etName;
	private EditText etPhone;
	private EditText etRoomId;
	private EditText etTime;
	private Button btnSend;
	private String mCompany;
	private String mName;
	private String mPhone;
	private String mRoomid;
	private String mTime;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_ERROR:
			case REQUEST_FAIL:
				ToastUtils
						.showToast(SendExpressActivity.this, (String) msg.obj);
				break;
			case REQUEST_SUCCESS:
				ToastUtils
						.showToast(SendExpressActivity.this, (String) msg.obj);
				Intent intent = new Intent(SendExpressActivity.this,
						OrderListActivity.class);
				startActivity(intent);
				break;
			}
		}

	};
	private Button btnOrder;

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
		ivBack.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		spShop.setOnItemSelectedListener(this);
		btnOrder.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_sendexpress);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		spShop = (Spinner) findViewById(R.id.sp_shop);
		etName = (EditText) findViewById(R.id.et_name);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etRoomId = (EditText) findViewById(R.id.et_room_id);
		etTime = (EditText) findViewById(R.id.et_time);
		btnSend = (Button) findViewById(R.id.btn_send);
		btnOrder = (Button) findViewById(R.id.btn_order);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 标题栏的返回按钮
		case R.id.iv_back:
			finish();
			break;
		// 提交按钮
		case R.id.btn_send:
			getInputData();
			if (checkData()) {
				new Thread() {
					public void run() {
						SharedPreferences sp = getSharedPreferences("userinfo",
								MODE_PRIVATE);
						String userPhone = sp.getString("phone", "");
						Map<String, Object> returnMap = new ExpressDao()
								.sendExpress(userPhone, mCompany, mName,
										mPhone, mRoomid, mTime);
						updateUI(returnMap);
					};
				}.start();
			}
			break;
		// 查看订单按钮
		case R.id.btn_order:
			startActivity(new Intent(this, OrderListActivity.class));
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
				message.obj = returnMap.get("respMsg");
				message.what = REQUEST_SUCCESS;
			} else {
				message.obj = returnMap.get("respMsg");
				message.what = REQUEST_FAIL;
			}
		} else {
			message.obj = "请求错误";
			message.what = REQUEST_ERROR;
		}
		handler.sendMessage(message);
	}

	/**
	 * 检查数据输入的合法性
	 * 
	 * @return
	 */
	private boolean checkData() {
		if (TextUtils.isEmpty(mCompany)) {
			ToastUtils.showToast(this, "快递公司不能为空");
			return false;
		}
		if (TextUtils.isEmpty(mName)) {
			ToastUtils.showToast(this, "姓名不能为空");
			return false;
		}
		if (TextUtils.isEmpty(mPhone)) {
			ToastUtils.showToast(this, "手机号不能为空");
			return false;
		}
		if (!VerificationFormat.isMobile(mPhone)) {
			ToastUtils.showToast(this, "手机号格式不正确");
			return false;
		}
		if (TextUtils.isEmpty(mRoomid)) {
			ToastUtils.showToast(this, "寝室号不能为空");
			return false;
		}
		if (TextUtils.isEmpty(mTime)) {
			ToastUtils.showToast(this, "预约时间不能为空");
			return false;
		}
		return true;
	}

	/**
	 * 获取输入的值
	 */
	private void getInputData() {
		mName = etName.getText().toString().trim();
		mPhone = etPhone.getText().toString().trim();
		mRoomid = etRoomId.getText().toString().trim();
		mTime = etTime.getText().toString().trim();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mCompany = (String) parent.getAdapter().getItem(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
