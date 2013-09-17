package com.aijuts.cx100.adapter;

import java.util.List;
import java.util.Map;

import com.aijuts.cx100.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainGvTitleAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;
	
	public MainGvTitleAdapter(Context context, List<Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.gridview_item_main_title, null);
			holder = new ViewHolder(convertView);
			holder.ivTitle = holder.getIvTitle();
//			holder.tvTitle = holder.getTvTitle();
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.ivTitle.setImageResource((Integer) list.get(position).get("image"));
//		holder.tvTitle.setText((String) list.get(position).get("title"));
		
		return convertView;
	}
	
	public class ViewHolder{
		
		private View convertView;
		private ImageView ivTitle;
//		private TextView tvTitle;
		
		public ViewHolder(View convertView){
			this.convertView = convertView;
		}
		
		public ImageView getIvTitle() {
			if (ivTitle == null) {
				ivTitle = (ImageView) convertView.findViewById(R.id.ivTitle);
			}
			return ivTitle;
		}
		
//		public TextView getTvTitle() {
//			if (tvTitle == null) {
//				tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//			}
//			return tvTitle;
//		}
		
	}

}
