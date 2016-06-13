package com.example.week7.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.utils.ActivityCollector;
import com.example.week7.utils.DataCleanUitls;

/**
 * 查看更多页面
 * 
 * @author Administrator
 * 
 */
public class MoreActivity extends BaseActivity implements OnClickListener {
	private Button btnLoginout;
	private TextView tvIntroduce;
	private TextView tvUpdate;
	private TextView tvVersion;
	private ImageView ivBack;

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
		tvIntroduce.setOnClickListener(this);
		tvUpdate.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		btnLoginout.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		tvVersion.setText("版本号:" + getVersionName());
	}

	/**
	 * 获取本机app的版本名
	 * 
	 * @return
	 */
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);// 获取版本信息
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			System.out.println("versionCode=" + versionCode + ";versionName="
					+ versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			// 包名不正确时会出现异常
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取本机app的版本名
	 * 
	 * @return
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);// 获取版本信息
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			// 包名不正确时会出现异常
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_more);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		btnLoginout = (Button) findViewById(R.id.btn_loginout);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvIntroduce = (TextView) findViewById(R.id.tv_introduce);
		tvUpdate = (TextView) findViewById(R.id.tv_update);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 标题栏的返回按钮
		case R.id.iv_back:
			finish();
			break;
		// 关于星期七
		case R.id.tv_introduce:
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		// 版本更新
		case R.id.tv_update:
			break;
		// 退出登录
		case R.id.btn_loginout:
			openConfirmLoginoutDialog();
			break;
		}
	}

	/**
	 * 打开确认退出的对话框
	 */
	private void openConfirmLoginoutDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("确认退出");
		builder.setMessage("真的要退出吗？");
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 清空shareprefrece
				emptySp();
				// 关闭所有已打开的Activity
				ActivityCollector.killAll();
				// 关闭对话框
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 清空sp中的数据
	 */
	protected void emptySp() {
		SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isLogin", false);
		editor.putString("nickname", "");
		editor.putString("score", "");
		editor.putString("phone", "");
		editor.putString("sex", "");
		editor.putString("province", "");
		editor.putString("city", "");
		editor.putString("address", "");
		editor.putString("photo", "");
		editor.commit();
	}
}
