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

public class HotelReviewLvAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;
	private FinalBitmap finalBitmap;
	
	public HotelReviewLvAdapter(Context context, List<Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.listview_item_hotel_review, null);
			holder = new ViewHolder(convertView);
			holder.ivUser = holder.getIvUser();
			holder.tvUser = holder.getUser();
			holder.tvTime = holder.getTime();
			holder.tvKouwei = holder.getKouwei();
			holder.tvHuanjing = holder.getHuanjing();
			holder.tvFuwu = holder.getFuwu();
			holder.tvContent = holder.getContent();
			holder.tvRenjun = holder.getRenjun();
			holder.tvTese = holder.getTese();
			holder.tvLoveCooking = holder.getLoveCooking();
			holder.tvConsumeTime = holder.getConsumeTime();
			holder.tvResReply = holder.getResReply();
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
		holder.tvTime.setText((String) list.get(position).get("time"));
		holder.tvKouwei.setText((String) list.get(position).get("kouwei"));
		holder.tvHuanjing.setText((String) list.get(position).get("huanjing"));
		holder.tvFuwu.setText((String) list.get(position).get("Fuwu"));
		holder.tvContent.setText((String) list.get(position).get("content"));
		holder.tvRenjun.setText((String) list.get(position).get("renjun"));
		holder.tvTese.setText((String) list.get(position).get("tese"));
		holder.tvLoveCooking.setText((String) list.get(position).get("loveCooking"));
		holder.tvConsumeTime.setText((String) list.get(position).get("consumeTime"));
		holder.tvResReply.setText((String) list.get(position).get("resReply"));
		
		return convertView;
	}
	
	public class ViewHolder{
		
		private View convertView;
		private ImageView ivUser;
		private TextView tvUser, tvTime, tvKouwei, tvHuanjing, tvFuwu, tvContent, tvRenjun, tvTese, 
			tvLoveCooking, tvConsumeTime, tvResReply;
		
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
		
		public TextView getTime() {
			if (tvTime == null) {
				tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			}
			return tvTime;
		}
		
		public TextView getKouwei() {
			if (tvKouwei == null) {
				tvKouwei = (TextView) convertView.findViewById(R.id.tvKouwei);
			}
			return tvKouwei;
		}
		
		public TextView getHuanjing() {
			if (tvHuanjing == null) {
				tvHuanjing = (TextView) convertView.findViewById(R.id.tvHuanjing);
			}
			return tvHuanjing;
		}
		
		public TextView getFuwu() {
			if (tvFuwu == null) {
				tvFuwu = (TextView) convertView.findViewById(R.id.tvFuwu);
			}
			return tvFuwu;
		}
		
		public TextView getContent() {
			if (tvContent == null) {
				tvContent = (TextView) convertView.findViewById(R.id.tvContent);
			}
			return tvContent;
		}
		
		public TextView getRenjun() {
			if (tvRenjun == null) {
				tvRenjun = (TextView) convertView.findViewById(R.id.tvRenjun);
			}
			return tvRenjun;
		}
		
		public TextView getTese() {
			if (tvTese == null) {
				tvTese = (TextView) convertView.findViewById(R.id.tvTese);
			}
			return tvTese;
		}
		
		public TextView getLoveCooking() {
			if (tvLoveCooking == null) {
				tvLoveCooking = (TextView) convertView.findViewById(R.id.tvLoveCooking);
			}
			return tvLoveCooking;
		}
		
		public TextView getConsumeTime() {
			if (tvConsumeTime == null) {
				tvConsumeTime = (TextView) convertView.findViewById(R.id.tvConsumeTime);
			}
			return tvConsumeTime;
		}
		
		public TextView getResReply() {
			if (tvResReply == null) {
				tvResReply = (TextView) convertView.findViewById(R.id.tvResReply);
			}
			return tvResReply;
		}
		
	}

}
