package com.aijuts.cx100.adapter;

import java.util.List;
import java.util.Map;

import com.aijuts.cx100.HotelActivity;
import com.aijuts.cx100.R;
import com.aijuts.cx100.SearchActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TagGvAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;
	private String title;
	
	public TagGvAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.gridview_item_tag, null);
			holder = new ViewHolder(convertView);
			holder.tvCheck = holder.getCheck();
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		title = (String) list.get(position).get("title");
		holder.tvCheck.setText(title);
		
		return convertView;
	}
	
	public class ViewHolder{
		
		private View convertView;
		private TextView tvCheck;
		
		public ViewHolder(View convertView){
			this.convertView = convertView;
		}
		
		public TextView getCheck() {
			if (tvCheck == null) {
				tvCheck = (TextView) convertView.findViewById(R.id.tvCheck);
			}
			return tvCheck;
		}
		
	}

}
