package com.example.week7.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.Display;
import android.view.WindowManager;

/**
 * 位图的加载器
 * 
 * @author Administrator
 * 
 */
public class BitmapUtils {
	public static Bitmap load(Context context, String file) {
		// 创建options对象
		Options opts = new Options();
		// 仅仅加载图片的宽高
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file, opts);
		// 获取图片的宽高
		int imageWidth = opts.outWidth;
		int imageHeight = opts.outHeight;
		// 获取手机屏幕的宽高
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display dp = manager.getDefaultDisplay();
		int screenWidth = dp.getWidth();
		int screenHeight = dp.getHeight();
		// 算出缩放比例
		int scale = 1;
		int widthScale = imageWidth / screenWidth;
		int heightScale = imageHeight / screenHeight;
		if (widthScale >= heightScale && widthScale >= 1) {
			scale = widthScale;
		} else if (widthScale < heightScale && heightScale >= 1) {
			scale = heightScale;
		}
		// 设置加载图片的缩放比例
		opts.inSampleSize = scale;
		// 设置opts的属性不是仅仅加载图片大小
		opts.inJustDecodeBounds = false;
		// 获取位图对象
		Bitmap bm = BitmapFactory.decodeFile(file, opts);
		return bm;
	}

}
