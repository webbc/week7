package com.example.week7.utils;

import java.util.ArrayList;

import android.app.Activity;

/**
 * Activity的管理类
 * 
 * @author Administrator
 * 
 */
public class ActivityCollector {
	private static ArrayList<Activity> activities = new ArrayList<Activity>();

	/*
	 * 把Activity添加进集合中
	 */
	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	/*
	 * 从集合中移除某个指定的Activity
	 */
	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	/*
	 * 销毁所有的Activity
	 */
	public static void killAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
