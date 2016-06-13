package com.example.week7.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.constant.Config;

/**
 * 新闻详情界面
 * 
 * @author Administrator
 * 
 */
public class NewsDetailActivity extends BaseActivity implements OnClickListener {

	private ImageView ivBack;
	private WebView webView;
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initData();
		initListener();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String title = bundle.getString("title");
		String url = bundle.getString("url");
		String number = bundle.getString("number");
		String date = bundle.getString("date");
		// 请求加载的网络地址
		String address = Config.SERVER_URL + "?c=Newspage&a=show&url=" + url
				+ "&title=" + title + "&number=" + number + "&date=" + date;
		webView.loadUrl(address);
		// 配置WebView
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);// 设置支持js
		webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true);// 页面支持缩放
		webSettings.setSupportZoom(true);
		// 让这个WebView打开所有页面
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		tvTitle.setText(title);
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		ivBack.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_news_detail);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		webView = (WebView) findViewById(R.id.webView);
		tvTitle = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		}
	}

}
