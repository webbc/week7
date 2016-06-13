package com.example.week7.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.week7.R;
import com.example.week7.domain.ParkImg;
import com.example.week7.utils.WindowSize;

/**
 * GridVie的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ParkGridViewAdapter extends BaseAdapter {

	private Context context;// 上下文
	private int col;// gridview要显示的列数
	private ArrayList<ParkImg> parkImgList;// 图片的集合

	public ParkGridViewAdapter(Context context, ArrayList<ParkImg> parkImgList,
			int col) {
		this.context = context;
		this.parkImgList = parkImgList;
		this.col = col;
	}

	@Override
	public int getCount() {
		return parkImgList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		int width = WindowSize.getWidth(context);// 获取屏幕宽度
		Log.i("tag", "width" + width);
		int height = 0;
		width = width / col;// 对当前的列数进行设置imgView的宽度
		height = width;
		imageView.setLayoutParams(new AbsListView.LayoutParams(width, height));
		Glide.with(context).load(parkImgList.get(position).getImg_url())
				.into(imageView);// 显示图片
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "" + position, 0).show();
			}
		});
		return imageView;
	}
}
