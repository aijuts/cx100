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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DishLvAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;
	private FinalBitmap finalBitmap;
	
	public DishLvAdapter(Context context, List<Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.listview_item_dish, null);
			holder = new ViewHolder(convertView);
			holder.ivTitle = holder.getIvTitle();
			holder.tvTitle = holder.getTvTitle();
			holder.tvPrice = holder.getPrice();
			holder.tvUnit = holder.getUnit();
			holder.tvHotel = holder.getHotel();
			holder.tvDishType = holder.getDishType();
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String image = (String) list.get(position).get("image");
		try {
			finalBitmap.display(holder.ivTitle, image);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, "Õº∆¨º”‘ÿ ß∞‹", Toast.LENGTH_SHORT).show();
		}
		
		holder.tvTitle.setText((String) list.get(position).get("title"));
		holder.tvPrice.setText((String) list.get(position).get("price"));
		holder.tvUnit.setText((String) list.get(position).get("unit"));
		holder.tvHotel.setText((String) list.get(position).get("hotel"));
		holder.tvDishType.setText((String) list.get(position).get("dishType"));
		
		return convertView;
	}
	
	public class ViewHolder{
		
		private View convertView;
		private ImageView ivTitle;
		private TextView tvTitle, tvPrice, tvUnit, tvHotel, tvDishType;
		
		public ViewHolder(View convertView){
			this.convertView = convertView;
		}
		
		public ImageView getIvTitle() {
			if (ivTitle == null) {
				ivTitle = (ImageView) convertView.findViewById(R.id.ivTitle);
			}
			return ivTitle;
		}
		
		public TextView getTvTitle() {
			if (tvTitle == null) {
				tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			}
			return tvTitle;
		}
		
		public TextView getPrice() {
			if (tvPrice == null) {
				tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
			}
			return tvPrice;
		}
		
		public TextView getUnit() {
			if (tvUnit == null) {
				tvUnit = (TextView) convertView.findViewById(R.id.tvUnit);
			}
			return tvUnit;
		}
		
		public TextView getHotel() {
			if (tvHotel == null) {
				tvHotel = (TextView) convertView.findViewById(R.id.tvHotel);
			}
			return tvHotel;
		}
		
		public TextView getDishType() {
			if (tvDishType == null) {
				tvDishType = (TextView) convertView.findViewById(R.id.tvDishType);
			}
			return tvDishType;
		}
		
	}

}
