package com.example.week7.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.week7.R;
import com.example.week7.domain.Parttime;

/**
 * 兼职页面ListView数据适配器
 * 
 * @author Administrator
 * 
 */
public class ParttimeListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Parttime> parttimeList;

	public ParttimeListViewAdapter(Context context,
			ArrayList<Parttime> parttimeList) {
		this.context = context;
		this.parttimeList = parttimeList;
	}

	@Override
	public int getCount() {
		return parttimeList.size();
	}

	@Override
	public Object getItem(int position) {
		return parttimeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.listview_item_parttime, null);
			holder.iv_parttime = (ImageView) view
					.findViewById(R.id.iv_parttime);
			holder.tv_company = (TextView) view.findViewById(R.id.tv_company);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
			holder.iv_top = (ImageView) view.findViewById(R.id.iv_top);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		// 加载图片
		Glide.with(context).load(parttimeList.get(position).getImg())
				.into(holder.iv_parttime);
		holder.tv_title.setText(parttimeList.get(position).getTitle());
		holder.tv_company.setText("公司："
				+ parttimeList.get(position).getCompany());
		holder.tv_desc.setText(parttimeList.get(position).getRemarks());
		if (parttimeList.get(position).getIstop() == 1) {
			holder.iv_top.setVisibility(View.VISIBLE);
		} else {
			holder.iv_top.setVisibility(View.INVISIBLE);
		}
		return view;
	}

	class ViewHolder {
		ImageView iv_parttime;
		TextView tv_company;
		TextView tv_title;
		TextView tv_desc;
		ImageView iv_top;
	}
}
