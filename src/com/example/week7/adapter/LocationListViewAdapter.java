package com.example.week7.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.domain.Location;

/**
 * 位置的ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class LocationListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Location> locationList;

	public LocationListViewAdapter(Context context,
			ArrayList<Location> locationList) {
		this.context = context;
		this.locationList = locationList;
	}

	@Override
	public int getCount() {
		return locationList.size();
	}

	@Override
	public Object getItem(int position) {
		return locationList.get(position);
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
			view = View.inflate(context, R.layout.listview_item_location, null);
			holder = new ViewHolder();
			holder.tv_province = (TextView) view.findViewById(R.id.tv_province);
			holder.tv_city = (TextView) view.findViewById(R.id.tv_city);
			holder.tv_address = (TextView) view.findViewById(R.id.tv_address);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_province.setText(locationList.get(position).getProvince());
		holder.tv_city.setText(locationList.get(position).getCity());
		holder.tv_address.setText(locationList.get(position).getAddress());
		return view;
	}

	class ViewHolder {
		TextView tv_province;
		TextView tv_city;
		TextView tv_address;
	}

}
