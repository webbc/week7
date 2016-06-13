package com.example.week7.adapter;

import java.util.ArrayList;

import com.example.week7.R;
import com.example.week7.activity.OrderDetailActivity;
import com.example.week7.domain.Express;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 订单列表ListView数据适配器
 * 
 * @author Administrator
 * 
 */
public class OrderListAdapter extends BaseAdapter implements
		OnItemClickListener {

	private Context context;// 上下文对象
	private ArrayList<Express> expressLists;// 订单集合对象
	private int currentClickItem;// 当前点击的条目position

	public OrderListAdapter(Context context, ArrayList<Express> expressLists) {
		this.context = context;
		this.expressLists = expressLists;
	}

	@Override
	public int getCount() {
		return expressLists.size();
	}

	@Override
	public Express getItem(int position) {
		return expressLists.get(position);
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
			view = View.inflate(context, R.layout.listview_item_order, null);
			holder = new ViewHolder();
			holder.tv_company = (TextView) view.findViewById(R.id.tv_company);
			holder.tv_esttime = (TextView) view.findViewById(R.id.tv_esttime);
			holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
			holder.tv_paystyle = (TextView) view.findViewById(R.id.tv_paystyle);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		Express express = getItem(position);
		holder.tv_company.setText(express.getCompany());
		holder.tv_esttime.setText(express.getEsttime());
		if (express.getPaystyle() == 1) {
			holder.tv_paystyle.setText("代拿快递订单");
			if (express.getMoney() == 0) {
				if (express.getLarge() == 1) {
					holder.tv_money.setText("30积分");
				} else if (express.getLarge() == 2) {
					holder.tv_money.setText("60积分");
				} else if (express.getLarge() == 2) {
					holder.tv_money.setText("90积分");
				}
			} else {
				holder.tv_money.setText("￥" + express.getMoney() + "元");
			}
		} else if (express.getPaystyle() == 2) {
			holder.tv_paystyle.setText("加急快递订单");
			holder.tv_money.setText("￥" + express.getMoney() + "元");
		} else if (express.getPaystyle() == 3) {
			holder.tv_paystyle.setText("寄快递订单");
			holder.tv_money.setText("当面交易");
			holder.tv_esttime.setText("预约收件时间：" + express.getArrivetime());
		}
		return view;
	}

	class ViewHolder {
		private TextView tv_company;
		private TextView tv_esttime;
		private TextView tv_money;
		private TextView tv_paystyle;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		currentClickItem = position;
		Intent intent = new Intent(context, OrderDetailActivity.class);
		intent.putExtra("args", expressLists.get(position));
		context.startActivity(intent);
	}

	/**
	 * 删除某一条目的数据
	 */
	public void deleteItem() {
		if (expressLists != null && expressLists.size() > 0) {
			expressLists.remove(currentClickItem);
		}
	}
}
