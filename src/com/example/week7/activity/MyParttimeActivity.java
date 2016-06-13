package com.example.week7.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

/**
 * 我的兼职界面
 * 
 * @author Administrator
 * 
 */
public class MyParttimeActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		TextView textView = new TextView(this);
		textView.setText("这是我的兼职界面");
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.RED);
		setContentView(textView);
	}

}
