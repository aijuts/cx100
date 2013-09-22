package com.aijuts.cx100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import com.aijuts.cx100.adapter.MainGvTitleAdapter;
import com.aijuts.cx100.adapter.MainVpAdapter;
import com.aijuts.cx100.data.entity.UserLogin;
import com.aijuts.cx100.entity.PopularSeller;
import com.aijuts.cx100.entity.PopularSellerData;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.GetWebServiceData;
import com.aijuts.cx100.util.Location;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.R.integer;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private FrameLayout layBody;
//	private ViewPager vpHotel;
	private GridView gvTitle;
	private FrameLayout layHotel1, layHotel2, layHotel3, layHotel4, layHotel5;
	private ImageView ivLeft, ivHotel, ivHotel1, ivHotel2, ivHotel3, ivHotel4, ivHotel5;
	private TextView tvHotel1, tvHotel2, tvHotel3, tvHotel4, tvHotel5, tvAddress;
	private ProgressBar pbWait;
	private List<View> pageViews;
	private List<Map<String, Object>> list_hotel, list_title;
	private Map<String, Object> map;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	private MainVpAdapter mainPagerAdapter;
	private MainGvTitleAdapter mainGvTitleAdapter;
	private FinalBitmap finalBitmap;
	private LocationClient locationClient;
	private Vibrator vibrator;
	private Intent intent;
	private Bundle bundle;
	private int no;
	//-2表示访问webservice有异常，-1表示返回数据有异常，0表示没有获取经纬度，1表示正常
	private int exceptionNo = 1;
	private GetWebServiceData getWebServiceData;
//	private PopularSeller popularSeller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		list_hotel = new ArrayList<Map<String,Object>>();
		list_title = new ArrayList<Map<String,Object>>();
		
		getWebServiceData = new GetWebServiceData();
//		popularSeller = new PopularSeller();
		
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (FrameLayout) findViewById(R.id.layBody);
//		vpHotel = (ViewPager) findViewById(R.id.vpHotel);
		layHotel1 = (FrameLayout) findViewById(R.id.layHotel1);
		layHotel2 = (FrameLayout) findViewById(R.id.layHotel2);
		layHotel3 = (FrameLayout) findViewById(R.id.layHotel3);
		layHotel4 = (FrameLayout) findViewById(R.id.layHotel4);
		layHotel5 = (FrameLayout) findViewById(R.id.layHotel5);
		ivHotel1 = (ImageView) findViewById(R.id.ivHotel1);
		ivHotel2 = (ImageView) findViewById(R.id.ivHotel2);
		ivHotel3 = (ImageView) findViewById(R.id.ivHotel3);
		ivHotel4 = (ImageView) findViewById(R.id.ivHotel4);
		ivHotel5 = (ImageView) findViewById(R.id.ivHotel5);
		tvHotel1 = (TextView) findViewById(R.id.tvHotel1);
		tvHotel2 = (TextView) findViewById(R.id.tvHotel2);
		tvHotel3 = (TextView) findViewById(R.id.tvHotel3);
		tvHotel4 = (TextView) findViewById(R.id.tvHotel4);
		tvHotel5 = (TextView) findViewById(R.id.tvHotel5);
		gvTitle = (GridView) findViewById(R.id.gvTitle);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		
		//手机定位
		locationClient = ((Location)getApplication()).mLocationClient;
		((Location)getApplication()).mTv = tvAddress;
		vibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		((Location)getApplication()).mVibrator01 = vibrator;
		setLocationOption();
		locationClient.start();
		
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("onStop");
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
				try {
					list_hotel = getHotelImageList();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Constants.popularSeller = null;
					exceptionNo = -2;
				}
				list_title = getTitleList();
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
					asyHotel();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(MainActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyHotel() {
		pbWait.setVisibility(View.GONE);
		layBody.setVisibility(View.VISIBLE);
		switch (exceptionNo) {
		case -2:
			Toast.makeText(MainActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(MainActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
//			LayoutInflater inflater = getLayoutInflater();
//			pageViews = new ArrayList<View>();
//			for (int i = 0; i < list_hotel.size(); i++) {
//				pageViews.add(inflater.inflate(R.layout.viewpager_item_hotel, null));
//			}
//			mainPagerAdapter = new MainVpAdapter(MainActivity.this, pageViews);
//			vpHotel.setAdapter(mainPagerAdapter);
//			try {
//				finalBitmap = FinalBitmap.create(MainActivity.this);
//				for (int i = 0; i < list_hotel.size(); i++) {
//					ivHotel = (ImageView) pageViews.get(i).findViewById(R.id.ivHotel);
//					finalBitmap.display(ivHotel, (String) list_hotel.get(i).get("image"));
//					ivHotel.setOnClickListener(new HotelClick(i));
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//				Toast.makeText(MainActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
//			}
//			vpHotel.setOnPageChangeListener(new HotelPageChange());
			
			try {
				finalBitmap = FinalBitmap.create(MainActivity.this);
				finalBitmap.display(ivHotel1, (String) list_hotel.get(0).get("image"));
				finalBitmap.display(ivHotel2, (String) list_hotel.get(1).get("image"));
				finalBitmap.display(ivHotel3, (String) list_hotel.get(2).get("image"));
				finalBitmap.display(ivHotel4, (String) list_hotel.get(3).get("image"));
				finalBitmap.display(ivHotel5, (String) list_hotel.get(4).get("image"));
				
				tvHotel1.setText((String) list_hotel.get(0).get("name"));
				tvHotel2.setText((String) list_hotel.get(1).get("name"));
				tvHotel3.setText((String) list_hotel.get(2).get("name"));
				tvHotel4.setText((String) list_hotel.get(3).get("name"));
				tvHotel5.setText((String) list_hotel.get(4).get("name"));
				
				layHotel1.setOnClickListener(new HotelClick(0));
				layHotel2.setOnClickListener(new HotelClick(1));
				layHotel3.setOnClickListener(new HotelClick(2));
				layHotel4.setOnClickListener(new HotelClick(3));
				layHotel5.setOnClickListener(new HotelClick(4));
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(MainActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		
		gvTitle.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mainGvTitleAdapter = new MainGvTitleAdapter(MainActivity.this, list_title);
		gvTitle.setAdapter(mainGvTitleAdapter);
		gvTitle.setOnItemClickListener(new TitleItemClick());
		
//		pbWait.setVisibility(View.GONE);
//		layBody.setVisibility(View.VISIBLE);
	}
	
	class LeftClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
		}
		
	}
	
	class HotelClick implements OnClickListener{
		
		private int id;
		
		public HotelClick(int id) {
			this.id = id;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intent = new Intent(MainActivity.this, HotelDetailActivity.class);
			Constants.sellerid = Integer.valueOf(list_hotel.get(id).get("id") + "");
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class HotelPageChange implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			no = arg0;
		}
		
	}
	
	class TitleItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			switch (arg2) {
			case 0:
				intent = new Intent(MainActivity.this, HotelActivity.class);
				bundle = new Bundle();
				bundle.putString("tagKey", "0");
				bundle.putString("tagValue", "0");
				bundle.putString("tagInfo", "");
				bundle.putString("likeName", "0");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case 1:
				intent = new Intent(MainActivity.this, SearchActivity.class);
				startActivity(intent);
				break;
			case 2:
				SharedPreferences preferences = getSharedPreferences("userLogin", 0);
				UserLogin userLogin = new UserLogin();
				userLogin.setUserid(preferences.getString("userid", ""));
				userLogin.setSiteid(preferences.getString("siteid", ""));
				userLogin.setAccount(preferences.getString("account", ""));
				userLogin.setMailaccount(preferences.getString("mailaccount", ""));
				userLogin.setMobileaccount(preferences.getString("mobileaccount", ""));
				userLogin.setNameaccount(preferences.getString("nameaccount", ""));
				userLogin.setPwd(preferences.getString("pwd", ""));
				userLogin.setV_pwd(preferences.getString("v_pwd", ""));
				userLogin.setType(preferences.getString("type", ""));
				userLogin.setRegtime(preferences.getString("regtime", ""));
				userLogin.setName(preferences.getString("name", ""));
				userLogin.setState(preferences.getString("state", ""));
				userLogin.setIsdelete(preferences.getString("isdelete", ""));
				
//				List<UserLogin> list = Constants.db.findAll(UserLogin.class);
//				if (list.size() == 0) {
				if (userLogin.getUserid().equals("")) {
//					Toast.makeText(MainActivity.this, "未登录", Toast.LENGTH_SHORT).show();
					intent = new Intent(MainActivity.this, UserLoginActivity.class);
					bundle = new Bundle();
					bundle.putInt("to", 0);
					intent.putExtras(bundle);
				}else {
//					Toast.makeText(MainActivity.this, "已登录", Toast.LENGTH_SHORT).show();
					intent = new Intent(MainActivity.this, UserCenterActivity.class);
					bundle = new Bundle();
					bundle.putInt("type", 0);
					bundle.putString("userid", userLogin.getUserid());
					intent.putExtras(bundle);
				}
				startActivity(intent);
				break;
			}
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	public List<Map<String, Object>> getHotelImageList() throws Exception {
		if (Constants.popularSeller == null) {
			Constants.popularSeller = getWebServiceData.getPopularSellerAll();
		}
		String success = Constants.popularSeller.getSuccess();
		if (success.equals("true")) {
			List<PopularSellerData> list_popularSellerData = Constants.popularSeller.getData();
			for (int i = 0; i < list_popularSellerData.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", list_popularSellerData.get(i).getUserid());
				String image_dir = list_popularSellerData.get(i).getDir();
				String image;
				if (image_dir.equals("")) {
					image = "";
				}else {
					image = Constants.url_image + image_dir;
				}
				map.put("image", image);
				map.put("name", list_popularSellerData.get(i).getName());
				list_hotel.add(map);
			}
			exceptionNo = 1;
		}else {
			Constants.popularSeller = null;
			exceptionNo = -1;
		}
		return list_hotel;
	}
	
	public List<Map<String, Object>> getTitleList() {
		for (int i = 0; i < Constants.menu.length; i++) {
			map = new HashMap<String, Object>();
			map.put("title", Constants.menu[i]);
			map.put("image", Constants.menu_image[i]);
			list_title.add(map);
		}
		return list_title;
	}
	
	//设置相关参数
	private void setLocationOption(){
	    LocationClientOption option = new LocationClientOption();
	    option.setOpenGps(true);
	    option.setAddrType("all");//返回的定位结果包含地址信息
	    option.setCoorType("gcj02");//返回的定位结果是百度经纬度,默认值gcj02
	    option.setScanSpan(60000);//设置发起定位请求的间隔时间为5000ms
	    option.disableCache(true);//禁止启用缓存定位
	    option.setPoiNumber(5);	//最多返回POI个数
	    option.setPoiDistance(1000); //poi查询距离
	    option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息
	    locationClient.setLocOption(option);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		locationClient.stop();
	}

}
