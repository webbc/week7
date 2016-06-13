package com.example.week7.utils;

import android.content.Context;

/**
 * 尺寸适配工具类
 * 
 * @author Administrator
 * 
 */
public class DensityUtils {

	public static int dp2px(Context context, float dp) {
		// 获取屏幕的密度
		float density = context.getResources().getDisplayMetrics().density;
		// dp和px的关系：dp = px / 屏幕密度
		int px = (int) (dp * density + 0.5f);// 进行四舍五入
		return px;
	}

	public static float px2dp(Context context, int px) {
		// 获取屏幕的密度
		float density = context.getResources().getDisplayMetrics().density;
		// dp和px的关系：dp = px / 屏幕密度
		float dp = px / density;
		return dp;
	}
}
