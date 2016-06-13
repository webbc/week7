package com.example.week7.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.week7.R;
import com.example.week7.activity.ParkCommentActivity;
import com.example.week7.activity.ParkZhuanfaActivity;
import com.example.week7.dao.ParkDao;
import com.example.week7.domain.Park;
import com.example.week7.fragment.ParkFragment;
import com.example.week7.utils.ToastUtils;
import com.example.week7.view.PictureGridView;

/**
 * 广场列表ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ParkListViewAdapter extends BaseAdapter {
	private static final int ZAN_SUCCESS = 0;// 点赞成功
	private static final int ZAN_ERROR = 1;// 点赞失败
	private static final int CANCEL_ZAN_SUCCESS = 2;// 取消赞成功
	private static final int CANCEL_ZAN_ERROR = 3;// 取消赞失败
	private Context context;// 上下文
	private ArrayList<Park> parkList;// 微博列表信息
	private ParkFragment fragment;// fragment对象
	private String phone;// 用户账户
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 点赞成功
			case ZAN_SUCCESS:
				ToastUtils.showToast(context, "点赞成功");
				Park park = (Park) msg.obj;
				park.setIsZan(1);// 设置已被赞
				park.setZan(Integer.parseInt(park.getZan()) + 1 + "");// 设置赞的数量+1
				notifyDataSetChanged();// 刷新界面
				break;
			// 点赞失败
			case ZAN_ERROR:
				ToastUtils.showToast(context, "点赞失败");
				break;
			// 取消赞失败
			case CANCEL_ZAN_ERROR:
				ToastUtils.showToast(context, "取消失败");
				break;
			// 取消赞成功
			case CANCEL_ZAN_SUCCESS:
				ToastUtils.showToast(context, "取消成功");
				Park p = (Park) msg.obj;
				p.setIsZan(0);// 设置已被赞
				p.setZan(Integer.parseInt(p.getZan()) - 1 + "");// 设置赞的数量+1
				notifyDataSetChanged();// 刷新界面
				break;
			default:
				ToastUtils.showToast(context, "请求错误");
				break;
			}
		}

	};

	public ParkListViewAdapter(ParkFragment fragment, Context context,
			ArrayList<Park> parkList, String phone) {
		this.fragment = fragment;
		this.context = context;
		this.parkList = parkList;
		this.phone = phone;
	}

	@Override
	public int getCount() {
		return parkList.size();
	}

	@Override
	public Object getItem(int position) {
		return parkList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View view;
		final ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.listview_item_park, null);
			holder = new ViewHolder();
			holder.tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			holder.tv_level = (TextView) view.findViewById(R.id.tv_level);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			holder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
			holder.iv_zan = (ImageView) view.findViewById(R.id.iv_zan);
			holder.iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
			holder.gv_park_img = (PictureGridView) view
					.findViewById(R.id.gv_park_img);
			holder.gv_zhuanfa_park_img = (PictureGridView) view
					.findViewById(R.id.gv_zhuanfa_park_img);
			holder.ll_zhuanfa_park = (LinearLayout) view
					.findViewById(R.id.ll_zhuanfa_park);
			holder.tv_zhuanfa_content = (TextView) view
					.findViewById(R.id.tv_zhuanfa_content);
			holder.ll_park_zhuanfa = (LinearLayout) view
					.findViewById(R.id.ll_park_zhuanfa);
			holder.ll_park_comment = (LinearLayout) view
					.findViewById(R.id.ll_park_comment);
			holder.ll_park_zan = (LinearLayout) view
					.findViewById(R.id.ll_park_zan);
			holder.tv_zhuanfa_number = (TextView) view
					.findViewById(R.id.tv_zhuanfa_number);
			holder.tv_comment_number = (TextView) view
					.findViewById(R.id.tv_comment_number);
			holder.tv_zan_number = (TextView) view
					.findViewById(R.id.tv_zan_number);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_nickname.setText(parkList.get(position).getUsername());
		holder.tv_time.setText(parkList.get(position).getTime());
		holder.tv_content.setText(parkList.get(position).getContent());
		holder.tv_level.setText(parkList.get(position).getLevel());
		// 判断是否是转发的微博
		if (parkList.get(position).getType().equals("1")) {
			// 显示被转发的内容
			holder.ll_zhuanfa_park.setVisibility(View.VISIBLE);
			holder.tv_zhuanfa_content.setText(parkList.get(position).getFrom()
					.getUsername()
					+ "： " + parkList.get(position).getFrom().getContent());
			int num = parkList.get(position).getParkimg().size();// 获取当前的图片数目
			int col = 1;// 默认列数
			Log.i("tag", "num" + num);
			if (num == 1) {
				holder.gv_park_img.setNumColumns(1);
				col = 1;
			} else if (num == 2 || num == 4) {
				holder.gv_park_img.setNumColumns(2);
				col = 2;
			} else {
				holder.gv_park_img.setNumColumns(3);
				col = 3;
			}
			holder.gv_zhuanfa_park_img.setAdapter(new ParkGridViewAdapter(
					context, parkList.get(position).getParkimg(), col));// 显示GridView的数据适配器
		} else {
			holder.ll_zhuanfa_park.setVisibility(View.GONE);// 隐藏转发模块
			int num = parkList.get(position).getParkimg().size();// 获取当前的图片数目
			int col = 1;// 默认列数
			Log.i("tag", "num" + num);
			if (num == 1) {
				holder.gv_park_img.setNumColumns(1);
				col = 1;
			} else if (num == 2 || num == 4) {
				holder.gv_park_img.setNumColumns(2);
				col = 2;
			} else {
				holder.gv_park_img.setNumColumns(3);
				col = 3;
			}
			holder.gv_park_img.setAdapter(new ParkGridViewAdapter(context,
					parkList.get(position).getParkimg(), col));// 显示GridView的数据适配器
		}
		// 加载头像
		Glide.with(context).load(parkList.get(position).getUserimg())
				.into(holder.iv_photo);
		// 显示性别图标
		if (parkList.get(position).getSex().equals("男")) {
			holder.iv_sex.setImageResource(R.drawable.sexmale);
		} else if (parkList.get(position).getSex().equals("女")) {
			holder.iv_sex.setImageResource(R.drawable.sexfemale);
		}

		// 显示赞的状态
		if (parkList.get(position).getIsZan() == 1) {
			holder.iv_zan.setImageResource(R.drawable.park_zaned);
		} else if (parkList.get(position).getIsZan() == 0) {
			holder.iv_zan.setImageResource(R.drawable.park_zan_normal);
		}
		// 显示转发的数字量
		holder.tv_zhuanfa_number.setText(parkList.get(position).getZhuanfa());
		// 显示评论的数据量
		holder.tv_comment_number.setText(parkList.get(position)
				.getComment_count());
		// 显示赞的数据量
		holder.tv_zan_number.setText(parkList.get(position).getZan());
		// 转发按钮
		holder.ll_park_zhuanfa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ParkZhuanfaActivity.class);
				intent.putExtra("pid", parkList.get(position).getId());
				fragment.startActivityForResult(intent,
						Activity.RESULT_FIRST_USER);
			}
		});
		// 评论按钮
		holder.ll_park_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ParkCommentActivity.class);
				intent.putExtra("pid", parkList.get(position).getId());
				intent.putExtra("position", position);
				intent.putExtra("comment_number", parkList.get(position)
						.getComment_count());
				fragment.startActivityForResult(intent, 1000);
			}
		});
		// 如果之前没有点赞
		if (parkList.get(position).getIsZan() == 0) {
			// 点赞按钮此时就是加赞的功能
			holder.ll_park_zan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 首先请求网络，把赞的信息写入服务器
					new Thread() {
						public void run() {
							Map<String, Object> returnMap = new ParkDao()
									.writeZan(phone, parkList.get(position)
											.getId());
							// 然后更新界面
							updateAddZanUI(returnMap, parkList.get(position));
						}

					}.start();
				}
			});
		} else {
			// 此时就是取消赞的功能
			holder.ll_park_zan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 首先请求网络，把取消赞的信息写入服务器
					new Thread() {
						public void run() {
							Map<String, Object> returnMap = new ParkDao()
									.cancelZan(phone, parkList.get(position)
											.getId());
							// 然后更新界面
							updateCancelZanUI(returnMap, parkList.get(position));
						}

					}.start();
				}
			});
		}
		return view;
	}

	/**
	 * 取消赞之后，更新UI
	 * 
	 * @param returnMap
	 * @param park
	 * @param iv_zan
	 */
	private void updateCancelZanUI(Map<String, Object> returnMap, Park park) {
		Message message = handler.obtainMessage();
		message.obj = park;
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.what = CANCEL_ZAN_SUCCESS;
			} else {
				message.what = CANCEL_ZAN_ERROR;
			}
		} else {
			message.what = CANCEL_ZAN_ERROR;
		}
		handler.sendMessage(message);
	}

	/**
	 * 点赞之后，更新UI
	 * 
	 * @param returnMap
	 * @param park
	 * @param iv_zan
	 */
	private void updateAddZanUI(Map<String, Object> returnMap, Park park) {
		Message message = handler.obtainMessage();
		message.obj = park;
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.what = ZAN_SUCCESS;
			} else {
				message.what = ZAN_ERROR;
			}
		} else {
			message.what = ZAN_ERROR;
		}
		handler.sendMessage(message);
	}

	class ViewHolder {
		TextView tv_nickname;
		TextView tv_time;
		TextView tv_content;
		TextView tv_level;
		ImageView iv_photo;
		ImageView iv_zan;
		ImageView iv_sex;
		LinearLayout ll_park_zhuanfa;
		LinearLayout ll_park_zan;
		LinearLayout ll_park_comment;
		LinearLayout ll_zhuanfa_park;
		TextView tv_zhuanfa_content;
		TextView tv_zhuanfa_number;
		TextView tv_comment_number;
		TextView tv_zan_number;
		PictureGridView gv_park_img;
		PictureGridView gv_zhuanfa_park_img;
	}

}
