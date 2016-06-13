package com.example.week7.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.domain.Shop;

/**
 * 校园小铺ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ShopListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Shop> shopList;

	public ShopListViewAdapter(Context context, ArrayList<Shop> shopList) {
		this.context = context;
		this.shopList = shopList;
	}

	@Override
	public int getCount() {
		return shopList.size();
	}

	@Override
	public Object getItem(int position) {
		return shopList.get(position);
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
			view = View.inflate(context, R.layout.listview_item_shop, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
			holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			holder.tv_hot = (TextView) view.findViewById(R.id.tv_hot);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_name.setText(shopList.get(position).getName());
		holder.tv_desc.setText(shopList.get(position).getDesc());
		holder.tv_hot.setText("销售量：" + shopList.get(position).getHot());
		holder.tv_price.setText("￥" + shopList.get(position).getPrice());
		return view;
	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_desc;
		TextView tv_price;
		TextView tv_hot;
	}

}
