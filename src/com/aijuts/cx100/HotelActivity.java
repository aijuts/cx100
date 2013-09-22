package com.aijuts.cx100;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aijuts.cx100.adapter.HotelLvAdapter;
import com.aijuts.cx100.entity.Seller;
import com.aijuts.cx100.entity.SellerCuisines;
import com.aijuts.cx100.entity.SellerData;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.GetWebServiceData;
import com.aijuts.cx100.util.Tool;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HotelActivity extends Activity {
	
	private Spinner spDistance;
	private ListView lvHotel;
	private View footView;
	private TextView tvTitle, tvTag, loading_msg;
	private ImageView ivLeft;
	private ProgressBar pbWait;
	private LinearLayout layBody;
	private List<Map<String, Object>> list;
	private Map<String, Object> map;
	private List<String> list_distance;
	private HotelLvAdapter hotelLvAdapter;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	/*判断listview中当前第几个item*/
	private int lastItem = 0, spnCount = 0;
	/*判断是否可更新*/
	private boolean isRefreshFoot = false;
	/*判断是否正在更新*/
	private boolean isRefreshFootIng = false;
	/* 是否是最后一个Item*/
	private boolean isLastItem = false;
	private int v = 0;
	private Intent intent;
	private Bundle bundle;
	private GetWebServiceData getWebServiceData;
	private Seller seller;
	//-2表示访问webservice有异常，-1表示返回数据有异常，0表示没有获取经纬度，1表示正常，2表示不从服务器更新数据
	private int exceptionNo = 1;
	//记录pageStart,初始值为0，设pageSize，值为20, tagKey、tagValue表示查询标签，likeName为根据餐厅名称模糊查询
	private String page_start = "0", page_size = "10", tagKey = "0", tagValue = "0", tagInfo = "", likeName = "0";
	//判断是否更新服务器数据
	private boolean isUpdate = true;
	private Tool tool;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_hotel);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		list = new ArrayList<Map<String,Object>>();
		list_distance = new ArrayList<String>();
		tool = new Tool(HotelActivity.this);
		
		// 从savedInstanceState中恢复数据, 如果没有数据需要恢复savedInstanceState为null  
        if (savedInstanceState != null) {  
        	tagKey = savedInstanceState.getString("tagKey");
    		tagValue = savedInstanceState.getString("tagValue");
    		tagInfo = savedInstanceState.getString("tagInfo");
    		likeName = savedInstanceState.getString("likeName");
    		Constants.sp_dis = savedInstanceState.getInt("sp_dis");
    		Constants.latitude = savedInstanceState.getDouble("latitude");
    		Constants.longitude = savedInstanceState.getDouble("longitude");
        }else {
        	bundle = getIntent().getExtras();
    		tagKey = bundle.getString("tagKey");
    		tagValue = bundle.getString("tagValue");
    		tagInfo = bundle.getString("tagInfo");
    		likeName = bundle.getString("likeName");
		}
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		tvTag = (TextView) findViewById(R.id.tvTag);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		spDistance = (Spinner) findViewById(R.id.spDistance);
		lvHotel = (ListView) findViewById(R.id.lvHotel);
		footView = LayoutInflater.from(this).inflate(R.layout.list_foot, null);
		loading_msg = (TextView) footView.findViewById(R.id.loading_msg);
		lvHotel.addFooterView(footView);
		
		tvTitle.setText("附近餐厅");
		ivLeft.setOnClickListener(new LeftClick());
		layBody.setVisibility(View.GONE);
		
		asy = "0";
		asyncTaskHelper = new AsyncTaskHelper();
		asyncTaskHelper.execute(asy);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//如果按下的是返回键，并且没有重复
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
			return false;
		}
		return false;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("tagKey", tagKey);
		outState.putString("tagValue", tagValue);
		outState.putString("tagInfo", tagInfo);
		outState.putString("likeName", likeName);
		outState.putInt("sp_dis", Constants.sp_dis);
		outState.putDouble("latitude", Constants.latitude);
		outState.putDouble("longitude", Constants.longitude);
	}

	class AsyncTaskHelper extends AsyncTask<String, String, String>{
		
		public String asy = "";

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			asy = params[0];
			if (asy.equals("0")) {
				list_distance = getDistance();
				list = getHotel();
			}
			if (asy.equals("1")) {
				list = getHotel();
			}
			if (asy.equals("2")) {
				list = getHotel();
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if (asy.equals("0")) {
					asyHotel0();
				}
				if (asy.equals("1")) {
					asyHotel1();
				}
				if (asy.equals("2")) {
					asyHotel2();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(HotelActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyHotel0() {
		pbWait.setVisibility(View.GONE);
		tvTag.setText(tagInfo);
		tvTag.setVisibility(View.VISIBLE);
		layBody.setVisibility(View.VISIBLE);
		//距离
		ArrayAdapter<String> disAdapter = new ArrayAdapter<String>(HotelActivity.this, R.layout.myspinner, list_distance);
		disAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spDistance.setAdapter(disAdapter);
		spDistance.setOnItemSelectedListener(new DistanceSelect());
		spDistance.setSelection(Constants.sp_dis);
		switch (exceptionNo) {
		case -2:
			Toast.makeText(HotelActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(HotelActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(HotelActivity.this, "获取当前坐标失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			hotelLvAdapter = new HotelLvAdapter(HotelActivity.this, list);
			lvHotel.setAdapter(hotelLvAdapter);
			lvHotel.setOnScrollListener(new ScrollLoad());
			lvHotel.setOnItemClickListener(new HotelItemClick());
			footView.setVisibility(View.GONE);
			break;
		case 2:
			Toast.makeText(HotelActivity.this, "内容已经到底", Toast.LENGTH_SHORT).show();
			footView.setVisibility(View.GONE);
			break;
		}
//		pbWait.setVisibility(View.GONE);
//		tvTag.setText(tagInfo);
//		tvTag.setVisibility(View.VISIBLE);
//		layBody.setVisibility(View.VISIBLE);
	}
	
	public void asyHotel1() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(HotelActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(HotelActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(HotelActivity.this, "获取当前坐标失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			hotelLvAdapter.notifyDataSetChanged();
//			lvHotel.setSelection(lastItem - v + 1);
			isRefreshFootIng = false;
			footView.setVisibility(View.GONE);
			break;
		case 2:
			Toast.makeText(HotelActivity.this, "内容已经到底", Toast.LENGTH_SHORT).show();
			footView.setVisibility(View.GONE);
			break;
		}
	}

	public void asyHotel2() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(HotelActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(HotelActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(HotelActivity.this, "获取当前坐标失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			hotelLvAdapter = new HotelLvAdapter(HotelActivity.this, list);
			lvHotel.setAdapter(hotelLvAdapter);
			lvHotel.setOnScrollListener(new ScrollLoad());
			lvHotel.setOnItemClickListener(new HotelItemClick());
			footView.setVisibility(View.GONE);
			break;
		case 2:
			Toast.makeText(HotelActivity.this, "内容已经到底", Toast.LENGTH_SHORT).show();
			footView.setVisibility(View.GONE);
			break;
		}
	}
	
	class LeftClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
		}
		
	}
	
	class ScrollLoad implements OnScrollListener{

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			try {
				lastItem = firstVisibleItem + visibleItemCount;
				//判断 最后一条开始显示，那么加载新数据 
				if (lastItem == totalItemCount) {
					isRefreshFoot = true;
				}else {
					isRefreshFoot = false;
				}
				v = visibleItemCount;
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(HotelActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			try {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && isRefreshFoot) {
					if (isRefreshFootIng == false) {
						footView.setVisibility(View.VISIBLE);
						asy = "1";
						asyncTaskHelper = new AsyncTaskHelper();
						asyncTaskHelper.execute(asy);
					}else {
//						Toast.makeText(HotelActivity.this, "内容正在加载", Toast.LENGTH_SHORT).show();
					}
					isRefreshFootIng = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(HotelActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	class HotelItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intent = new Intent(HotelActivity.this, HotelDetailActivity.class);
//			bundle = new Bundle();
//			bundle.putString("id", (String) list.get(arg2).get("userid"));
//			intent.putExtras(bundle);
			Constants.sellerid = Integer.valueOf(list.get(arg2).get("userid") + "");
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class DistanceSelect implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (spnCount == 1) {
				try {
					Constants.sp_dis = arg2;
					isRefreshFootIng = false;
					isUpdate = true;
					page_start = "0";
					list.removeAll(list);
					hotelLvAdapter.notifyDataSetChanged();
					footView.setVisibility(View.VISIBLE);
					asy = "2";
					asyncTaskHelper = new AsyncTaskHelper();
					asyncTaskHelper.execute(asy);
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(HotelActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
				}
			}
			spnCount = 1;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public List<Map<String, Object>> getHotel() {
		if (isUpdate == true) {
			try {
				list = getHotels();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				exceptionNo = -2;
			}
		}else {
			exceptionNo = 2;
		}
		return list;
	}
	
	public List<Map<String, Object>> getHotels() throws Exception {
		System.out.println(Constants.latitude + " " + Constants.longitude);
		if (Constants.latitude == 10000 || Constants.longitude == 10000) {
			exceptionNo = 0;
		}else {
			getWebServiceData = new GetWebServiceData();
			System.out.println(Constants.dis[Constants.sp_dis]);
			String likename = tool.encode(likeName);
			seller = getWebServiceData.getSellerAll(
					Constants.latitude+"", 
					Constants.longitude+"", 
					Constants.dis[Constants.sp_dis], 
					page_start, 
					page_size, 
					tagKey, 
					tagValue, 
					likename);
			String success = seller.getSuccess();
			if (success.equals("true")) {
				List<SellerData> list_seller = seller.getData();
				if (list_seller.size() < Integer.valueOf(page_size)) {
					isUpdate = false;
				}else {
					isUpdate = true;
				}
				for (int i = 0; i < list_seller.size(); i++) {
					map = new HashMap<String, Object>();
					map.put("userid", list_seller.get(i).getUserid());
					String image_dir = list_seller.get(i).getImage();
					String image = "";
					if (image_dir.equals("")) {
						image = "";
					}else {
						image = Constants.url_list_image + image_dir;
					}
//					System.out.println(i + " image: " + image);
					map.put("image", image);
					map.put("title", list_seller.get(i).getName());
					map.put("perCapita", "人均：¥ " + list_seller.get(i).getPercapita());
					map.put("address", list_seller.get(i).getAddr());
					List<SellerCuisines> list_sellerCuisines = list_seller.get(i).getCuisines();
					String sellerCuisines = "";
					for (int j = 0; j < list_sellerCuisines.size(); j++) {
						sellerCuisines += list_sellerCuisines.get(j).getName();
					}
					map.put("cuisines", sellerCuisines);
					double distance = Double.valueOf(list_seller.get(i).getDistance());
					String dis = "";
					if (distance < 1) {
						distance = distance * 1000;
						BigDecimal d = new BigDecimal(distance).setScale(0, BigDecimal.ROUND_HALF_UP);
						dis = d + "m";
					}else {
						Tool tool = new Tool(HotelActivity.this);
						distance = tool.saveDistance(distance);
						dis = distance + "公里";
					}
					map.put("distance", dis);
					String evaluate = list_seller.get(i).getEvaluate();
					if (evaluate.equals("")) {
						evaluate = "0";
					}
					float score = Float.valueOf(evaluate);
					map.put("score", score);
					map.put("latitude", list_seller.get(i).getLatitude());
					map.put("longitude", list_seller.get(i).getLongitude());
					list.add(map);
				}
				if (list_seller.size() > 0) {
					page_start = list_seller.get(list_seller.size() - 1).getDistance();
				}
				exceptionNo = 1;
			}else {
				exceptionNo = -1;
			}
		}
//		for (int i = 0; i < 20; i++) {
//			map = new HashMap<String, Object>();
//			map.put("image", "http://www.cx100.cn/Content/upload/00/00/0A/A4.jpg");
//			map.put("title", "阿拉提西域文化主题餐厅（丽丰店）");
//			map.put("perCapita", "人均：¥ 50-100");
//			map.put("address", "苏州吴中区宝带东路399号丽丰广场铂金大道115商铺");
//			map.put("cuisines", "川菜,烧烤,其他小吃");
//			map.put("distance", "550m");
//			map.put("score", 5f);
//			list.add(map);
//		}
		return list;
	}
	
	public List<String> getDistance() {
		for (int i = 0; i < Constants.distance.length; i++) {
			list_distance.add(Constants.distance[i]);
		}
		return list_distance;
	}

}
