package com.example.week7.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.example.week7.R;

/**
 * 闪屏界面
 * 
 * @author Administrator
 * 
 */
public class SplashActivity extends BaseActivity {
	private Intent intent;// Intent对象
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
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
		setContentView(R.layout.activity_splash);
		// 从sp中读取数据，来判断用户是否已经登录
		SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		boolean isLogin = sp.getBoolean("isLogin", false);
		if (isLogin) {
			intent = new Intent(SplashActivity.this, MainActivity.class);
		} else {
			intent = new Intent(SplashActivity.this, LoginActivity.class);
		}
		// 在闪屏页面停留三秒
		handler.sendEmptyMessageDelayed(0, 3000);
	}

}
