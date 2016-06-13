package com.example.week7.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week7.R;

/**
 * 定义GridView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class GridViewAdapter extends BaseAdapter {
	private String[] items = new String[] { "教务通知", "就业信息", "课表查询", "失物招领",
			"校园小铺", "电脑维修" };
	private int[] resIds = new int[] { R.drawable.jiaowu, R.drawable.jiuye,
			R.drawable.lesson, R.drawable.shiwu, R.drawable.shop,
			R.drawable.home_computer_fix };
	private Context context;

	public GridViewAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		return resIds[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.gridview_item, null);
		ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
		TextView tvIconInfo = (TextView) view.findViewById(R.id.tv_icon_info);
		int resId = (Integer) getItem(position);
		ivIcon.setImageResource(resId);
		tvIconInfo.setText(items[position]);
		return view;
	}
}
