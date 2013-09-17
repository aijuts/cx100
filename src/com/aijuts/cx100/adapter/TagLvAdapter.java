package com.aijuts.cx100.adapter;

import java.util.List;
import java.util.Map;

import com.aijuts.cx100.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TagLvAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;
	
	public TagLvAdapter(Context context, List<Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.listview_item_tag, null);
			holder = new ViewHolder(convertView);
			holder.tvResCooking = holder.getResCooking();
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvResCooking.setText((String) list.get(position).get("title"));
		
		return convertView;
	}
	
	public class ViewHolder{
		
		private View convertView;
		private TextView tvResCooking;
		
		public ViewHolder(View convertView){
			this.convertView = convertView;
		}
		
		public TextView getResCooking() {
			if (tvResCooking == null) {
				tvResCooking = (TextView) convertView.findViewById(R.id.tvResCooking);
			}
			return tvResCooking;
		}
		
	}

}
