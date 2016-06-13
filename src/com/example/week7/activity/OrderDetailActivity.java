package com.example.week7.activity;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.week7.R;
import com.example.week7.dao.ExpressDao;
import com.example.week7.domain.Deliveryman;
import com.example.week7.domain.Express;
import com.example.week7.utils.ToastUtils;

/**
 * 订单详情界面
 * 
 * @author Administrator
 * 
 */
public class OrderDetailActivity extends BaseActivity implements OnClickListener {
	private static final int REQUEST_ERROR = 0;// 请求错误
	private static final int REQUEST_SUCCESS = 1;// 请求成功
	private static final int REQUEST_FAIL = 2;// 请求失败
	public static boolean isPressBack = true;
	private ImageView ivBack;
	private TextView tvState;
	private TextView tvEsttime;
	private TextView tvOrdercode;
	private TextView tvName;
	private TextView tvRoomid;
	private TextView tvPhone;
	private TextView tvCompany;
	private TextView tvOrdertime;
	private ImageView ivPhoto;
	private TextView tvSendman;
	private Button btnCancel;
	private LinearLayout ll_bottom;
	private Deliveryman sendman;// 配送员
	private ImageView ivSms;
	private ImageView ivCall;
	private Express express;// 订单
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_ERROR:
			case REQUEST_FAIL:
				ToastUtils
						.showToast(OrderDetailActivity.this, (String) msg.obj);
				break;
			case REQUEST_SUCCESS:
				isPressBack = false;
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
		initData();
		initListener();
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		ivBack.setOnClickListener(this);
		ivSms.setOnClickListener(this);
		ivCall.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Intent intent = getIntent();
		express = (Express) intent.getSerializableExtra("args");
		if (express.getState() == 0) {
			tvState.setText("已完成");
			btnCancel.setVisibility(View.GONE);
		} else if (express.getState() == 1) {
			tvState.setText("正在审核");
			btnCancel.setVisibility(View.VISIBLE);
		} else if (express.getState() == 2) {
			tvState.setText("正在取件");
			btnCancel.setVisibility(View.GONE);
		} else if (express.getState() == 3) {
			tvState.setText("正在派件");
			btnCancel.setVisibility(View.GONE);
		} else if (express.getState() == 4) {
			tvState.setText("已取消");
			btnCancel.setVisibility(View.GONE);
		}
		if (express.getPaystyle() == 1 || express.getPaystyle() == 2) {
			tvEsttime.setText(express.getEsttime());
		} else if (express.getPaystyle() == 3) {
			tvEsttime.setText("感谢使用星期七快递");
		}
		tvOrdercode.setText("订单号：" + express.getOrder_code());
		tvName.setText("收件人：" + express.getName());
		tvRoomid.setText("收件人寝室：" + express.getAddress());
		tvPhone.setText("收件人手机：" + express.getPhone());
		tvCompany.setText("快递公司：" + express.getCompany());
		tvOrdertime.setText("下单时间：" + express.getStime());

		sendman = express.getDeliveryman();
		if (sendman != null) {
			ll_bottom.setVisibility(View.VISIBLE);
			Glide.with(this).load(sendman.getPhoto()).into(ivPhoto);
			tvSendman.setText(sendman.getName());
		} else {
			ll_bottom.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		setContentView(R.layout.activity_order_detail);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvState = (TextView) findViewById(R.id.tv_state);
		tvEsttime = (TextView) findViewById(R.id.tv_esttime);
		tvOrdercode = (TextView) findViewById(R.id.tv_ordercode);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvRoomid = (TextView) findViewById(R.id.tv_roomid);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		tvCompany = (TextView) findViewById(R.id.tv_company);
		tvOrdertime = (TextView) findViewById(R.id.tv_ordertime);
		ivPhoto = (ImageView) findViewById(R.id.iv_photo);
		ivSms = (ImageView) findViewById(R.id.iv_sms);
		ivCall = (ImageView) findViewById(R.id.iv_call);
		tvSendman = (TextView) findViewById(R.id.tv_sendman);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			// 返回按钮
			finish();
			break;
		case R.id.iv_sms:
			// 进行发短信操作
			Uri smsToUri = Uri.parse("smsto:" + sendman.getPhone());
			Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
			intent.putExtra("sms_body", "");
			startActivity(intent);
			break;
		case R.id.iv_call:
			// 进行打电话操作
			Intent i = new Intent(Intent.ACTION_DIAL);
			i.setData(Uri.parse("tel:" + sendman.getPhone()));
			startActivity(i);
			break;
		case R.id.btn_cancel:
			// 取消订单
			showConfirmDialog();
			break;
		}
	}

	/**
	 * 显示取消订单对话框
	 */
	private void showConfirmDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("取消订单");
		builder.setMessage("真的忍心取消订单吗？小弟还想为您服务呢！");
		builder.setPositiveButton("继续等待", null);
		builder.setNegativeButton("忍心取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences sp = getSharedPreferences("userinfo",
								MODE_PRIVATE);
						final String phone = sp.getString("phone", "");
						new Thread() {
							public void run() {
								Map<String, Object> returnMap = new ExpressDao()
										.cancelOrder(express.getId(), phone);
								updateUI(returnMap);
							};
						}.start();
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * 取消订单之后更新界面
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		isPressBack = true;
	}
}
