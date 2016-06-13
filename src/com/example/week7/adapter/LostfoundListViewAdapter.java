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
import com.example.week7.domain.Lostfound;
import com.example.week7.utils.GlideRoundTransform;

/**
 * 失物招领ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class LostfoundListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Lostfound> lostfoundList;

	public LostfoundListViewAdapter(Context context,
			ArrayList<Lostfound> lostfoundList) {
		this.context = context;
		this.lostfoundList = lostfoundList;
	}

	@Override
	public int getCount() {
		return lostfoundList.size();
	}

	@Override
	public Object getItem(int position) {
		return lostfoundList.get(position);
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
			view = View
					.inflate(context, R.layout.listview_item_lostfound, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			holder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_name.setText(lostfoundList.get(position).getNickname());
		holder.tv_time.setText(lostfoundList.get(position).getTime());
		holder.tv_content.setText(lostfoundList.get(position).getContent());
		Glide.with(context).load(lostfoundList.get(position).getPhoto())
				.transform(new GlideRoundTransform(context, 15))
				.into(holder.iv_photo);
		return view;
	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_time;
		TextView tv_content;
		ImageView iv_photo;
	}

}
