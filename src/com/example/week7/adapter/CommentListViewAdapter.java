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
import com.example.week7.domain.ParkComment;
import com.example.week7.domain.Parttime;

/**
 * Œ¢≤©∆¿¬€ListView ˝æ›  ≈‰∆˜
 * 
 * @author Administrator
 * 
 */
public class CommentListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ParkComment> commentList;

	public CommentListViewAdapter(Context context,
			ArrayList<ParkComment> commentList) {
		this.context = context;
		this.commentList = commentList;
	}

	@Override
	public int getCount() {
		return commentList.size();
	}

	@Override
	public Object getItem(int position) {
		return commentList.get(position);
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
			view = View.inflate(context, R.layout.listview_item_parkcomment,
					null);
			holder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
			holder.tv_comment_content = (TextView) view
					.findViewById(R.id.tv_comment_content);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		// º”‘ÿÕº∆¨
		Glide.with(context).load(commentList.get(position).getPhoto())
				.into(holder.iv_photo);
		holder.tv_comment_content.setText(commentList.get(position)
				.getNickname() + "£∫" + commentList.get(position).getContent());
		return view;
	}

	class ViewHolder {
		ImageView iv_photo;
		TextView tv_comment_content;
	}
}
