package com.aijuts.cx100.adapter;

import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import com.aijuts.cx100.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HotelOrderLvAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;
	private FinalBitmap finalBitmap;
	
	public HotelOrderLvAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
		this.finalBitmap = FinalBitmap.create(context);
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
			convertView = mInflater.inflate(R.layout.listview_item_hotel_order, null);
			holder = new ViewHolder(convertView);
			holder.ivUser = holder.getIvUser();
			holder.tvUser = holder.getUser();
			holder.tvBookTime = holder.getBookTime();
			holder.tvEatTime = holder.getEatTime();
			holder.tvStatus = holder.getStatus();
			holder.tvMoney = holder.getMoney();
			holder.tvNumber = holder.getNumber();
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String image = (String) list.get(position).get("image");
		try {
			finalBitmap.display(holder.ivUser, image);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, "Õº∆¨º”‘ÿ ß∞‹", Toast.LENGTH_SHORT).show();
		}
		
		holder.tvUser.setText((String) list.get(position).get("user"));
		holder.tvBookTime.setText((String) list.get(position).get("bookTime"));
		holder.tvEatTime.setText((String) list.get(position).get("eatTime"));
		holder.tvStatus.setText((String) list.get(position).get("status"));
		holder.tvMoney.setText((String) list.get(position).get("money"));
		holder.tvNumber.setText((String) list.get(position).get("number"));
		
		return convertView;
	}
	
	public class ViewHolder{
		
		private View convertView;
		private ImageView ivUser;
		private TextView tvUser, tvBookTime, tvEatTime, tvStatus, tvMoney, tvNumber;
		
		public ViewHolder(View convertView){
			this.convertView = convertView;
		}
		
		public ImageView getIvUser() {
			if (ivUser == null) {
				ivUser = (ImageView) convertView.findViewById(R.id.ivUser);
			}
			return ivUser;
		}
		
		public TextView getUser() {
			if (tvUser == null) {
				tvUser = (TextView) convertView.findViewById(R.id.tvUser);
			}
			return tvUser;
		}
		
		public TextView getBookTime() {
			if (tvBookTime == null) {
				tvBookTime = (TextView) convertView.findViewById(R.id.tvBookTime);
			}
			return tvBookTime;
		}
		
		public TextView getEatTime() {
			if (tvEatTime == null) {
				tvEatTime = (TextView) convertView.findViewById(R.id.tvEatTime);
			}
			return tvEatTime;
		}
		
		public TextView getStatus() {
			if (tvStatus == null) {
				tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			}
			return tvStatus;
		}
		
		public TextView getMoney() {
			if (tvMoney == null) {
				tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);
			}
			return tvMoney;
		}
		
		public TextView getNumber() {
			if (tvNumber == null) {
				tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
			}
			return tvNumber;
		}
		
	}

}
