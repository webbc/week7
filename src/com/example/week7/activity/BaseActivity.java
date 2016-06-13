package com.example.week7.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.example.week7.domain.User;
import com.example.week7.utils.ActivityCollector;

/**
 * 所有Activity的基类
 * 
 * @author Administrator
 * 
 */
public class BaseActivity extends FragmentActivity {
	private SharedPreferences sp;
	protected String phone;// 电话（用户账号）
	protected String nickname;// 用户昵称
	protected String sex;// 用户性别
	protected String address;// 用户地址
	protected String photo;// 用户头像
	protected String province;// 省
	protected String city;// 市
	protected String score;// 积分
	protected User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		phone = sp.getString("phone", "");// 获取用户名
		nickname = sp.getString("nickname", "");
		sex = sp.getString("sex", "");
		province = sp.getString("province", "");
		city = sp.getString("city", "");
		address = sp.getString("address", "");
		photo = sp.getString("photo", "");
		score = sp.getString("score", "");
		user = new User();
		user.setPhone(phone);
		user.setNickname(nickname);
		user.setSex(sex);
		user.setAddress(address);
		user.setPhoto(photo);
		user.setProvince(province);
		user.setCity(city);
		user.setScore(score);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	/**
	 * 标题栏上退出按钮响应事件
	 */
	public void onBack(View view) {
		finish();
	}

}
