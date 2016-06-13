package com.example.week7.activity;

import com.example.week7.R;
import android.os.Bundle;
import android.view.Window;

/**
 * 关于界面
 * 
 * @author Administrator
 * 
 */
public class AboutActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
	}
}
