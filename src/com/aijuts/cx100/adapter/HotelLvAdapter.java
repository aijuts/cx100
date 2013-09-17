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

public class HotelLvAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;
	private FinalBitmap finalBitmap;
	
	public HotelLvAdapter(Context context, List<Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.listview_item_hotel, null);
			holder = new ViewHolder(convertView);
			holder.ivTitle = holder.getIvTitle();
			holder.tvTitle = holder.getTvTitle();
			holder.tvPerCapita = holder.getPerCapita();
			holder.tvAddress = holder.getAddress();
			holder.tvCuisines = holder.getCuisines();
			holder.tvDistance = holder.getDistance();
			holder.rbScore = holder.getScore();
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
		holder.rbScore.setRating((Float) list.get(position).get("score"));
		holder.tvPerCapita.setText((String) list.get(position).get("perCapita"));
		holder.tvAddress.setText((String) list.get(position).get("address"));
		holder.tvCuisines.setText((String) list.get(position).get("cuisines"));
		holder.tvDistance.setText((String) list.get(position).get("distance"));
		
		return convertView;
	}
	
	public class ViewHolder{
		
		private View convertView;
		private ImageView ivTitle;
		private TextView tvTitle, tvPerCapita, tvAddress, tvCuisines, tvDistance;
		private RatingBar rbScore;
		
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
		
		public TextView getPerCapita() {
			if (tvPerCapita == null) {
				tvPerCapita = (TextView) convertView.findViewById(R.id.tvPerCapita);
			}
			return tvPerCapita;
		}
		
		public TextView getAddress() {
			if (tvAddress == null) {
				tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
			}
			return tvAddress;
		}
		
		public TextView getCuisines() {
			if (tvCuisines == null) {
				tvCuisines = (TextView) convertView.findViewById(R.id.tvCuisines);
			}
			return tvCuisines;
		}
		
		public TextView getDistance() {
			if (tvDistance == null) {
				tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
			}
			return tvDistance;
		}
		
		public RatingBar getScore() {
			if (rbScore == null) {
				rbScore = (RatingBar) convertView.findViewById(R.id.rbScore);
			}
			return rbScore;
		}
		
	}

}
