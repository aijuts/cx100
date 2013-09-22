package com.aijuts.cx100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;

import com.aijuts.cx100.MainActivity.AsyncTaskHelper;
import com.aijuts.cx100.MainActivity.TitleItemClick;
import com.aijuts.cx100.adapter.HotelOrderLvAdapter;
import com.aijuts.cx100.adapter.HotelReviewLvAdapter;
import com.aijuts.cx100.adapter.MainGvTitleAdapter;
import com.aijuts.cx100.adapter.MainVpAdapter;
import com.aijuts.cx100.adapter.TagGvAdapter;
import com.aijuts.cx100.adapter.TagLvAdapter;
import com.aijuts.cx100.data.entity.UserLogin;
import com.aijuts.cx100.entity.Cateclassify;
import com.aijuts.cx100.entity.Favorite;
import com.aijuts.cx100.entity.FavoriteData;
import com.aijuts.cx100.entity.Notice;
import com.aijuts.cx100.entity.Order;
import com.aijuts.cx100.entity.OrderList;
import com.aijuts.cx100.entity.PurposeData;
import com.aijuts.cx100.entity.Replymsg;
import com.aijuts.cx100.entity.Reviews;
import com.aijuts.cx100.entity.SellerCuisines;
import com.aijuts.cx100.entity.SellerDetail;
import com.aijuts.cx100.entity.SellerDetailData;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.GetWebServiceData;
import com.aijuts.cx100.util.Tool;
import com.aijuts.cx100.view.FixGridLayout;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HotelDetailActivity extends Activity {
	
	private ViewPager vpHotel;
	private ImageView ivHotel, ivLeft, ivRight;
	private FrameLayout layPhone, layAddress;
	private LinearLayout layBody, layCuisines, layPurpose, layCooking, layNews, layOrder, layReview, layInfo;
	private TextView tvTitle, tvKouwei, tvhuanjing, tvFuwu, tvRenjun, tvYysj, tvPhone, tvAddress;
	private WebView wvInfo;
	private WebSettings webSettings;
	private ProgressBar pbWait;
	private ProgressDialog pdWait;
	private GridView gvCaixi, gvSsyfw, gvResCooking;
	private ListView lvResNews, lvOrder, lvReview;
	private Button btnResNews, btnOrder, btnReview, btnBooking, btnCollection;
	private List<View> pageViews;
	private List<Map<String, Object>> list_hotel, list_caixi, list_sheshi, list_cooking, list_news, 
		list_order, list_review;
	private Map<String, Object> map;
	private OrderList orderList;
	private MainVpAdapter mainVpAdapter;
	private TagGvAdapter tagGvAdapter;
	private TagLvAdapter tagLvAdapter;
	private HotelOrderLvAdapter hotelOrderLvAdapter;
	private HotelReviewLvAdapter hotelReviewLvAdapter;
	private FinalBitmap finalBitmap;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	private Intent intent;
	private Bundle bundle;
	private GetWebServiceData getWebServiceData;
	private Tool tool;
	private SellerDetail sellerDetail;
	private SellerDetailData sellerDetailData;
	private Favorite favorite;
	private FavoriteData favoriteData;
	//-2表示访问webservice有异常，0表示字符转换有异常，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1, exceptionNo2 = 1, isLogin = 0, isUpdateSeller = 0;
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_hotel_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
		
		getWebServiceData = new GetWebServiceData();
		tool = new Tool(this);
		favorite = new Favorite();
		favoriteData = new FavoriteData();
		
		if (savedInstanceState != null) {
			Constants.db = FinalDb.create(HotelDetailActivity.this, "cxlm");
			Constants.sellerid = savedInstanceState.getInt("sellerid");
		}
		
		list_hotel = new ArrayList<Map<String,Object>>();
		list_caixi = new ArrayList<Map<String,Object>>();
		list_sheshi = new ArrayList<Map<String,Object>>();
		list_cooking = new ArrayList<Map<String,Object>>();
		list_news = new ArrayList<Map<String,Object>>();
		list_order = new ArrayList<Map<String,Object>>();
		list_review = new ArrayList<Map<String,Object>>();
		
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivRight = (ImageView) findViewById(R.id.ivRight);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		vpHotel = (ViewPager) findViewById(R.id.vpHotel);
		tvKouwei = (TextView) findViewById(R.id.tvKouwei);
		tvhuanjing = (TextView) findViewById(R.id.tvhuanjing);
		tvFuwu = (TextView) findViewById(R.id.tvFuwu);
		tvRenjun = (TextView) findViewById(R.id.tvRenjun);
		btnBooking = (Button) findViewById(R.id.btnBooking);
		btnCollection = (Button) findViewById(R.id.btnCollection);
		wvInfo = (WebView) findViewById(R.id.wvInfo);
		gvCaixi = (GridView) findViewById(R.id.gvCaixi);
		tvYysj = (TextView) findViewById(R.id.tvYysj);
		gvSsyfw = (GridView) findViewById(R.id.gvSsyfw);
		layPhone = (FrameLayout) findViewById(R.id.layPhone);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		layAddress = (FrameLayout) findViewById(R.id.layAddress);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		gvResCooking = (GridView) findViewById(R.id.gvResCooking);
		lvResNews = (ListView) findViewById(R.id.lvResNews);
		btnResNews = (Button) findViewById(R.id.btnResNews);
		lvOrder = (ListView) findViewById(R.id.lvOrder);
		btnOrder = (Button) findViewById(R.id.btnOrder);
		lvReview = (ListView) findViewById(R.id.lvReview);
		btnReview = (Button) findViewById(R.id.btnReview);
		layCuisines = (LinearLayout) findViewById(R.id.layCuisines);
		layPurpose = (LinearLayout) findViewById(R.id.layPurpose);
		layCooking = (LinearLayout) findViewById(R.id.layCooking);
		layNews = (LinearLayout) findViewById(R.id.layNews);
		layOrder = (LinearLayout) findViewById(R.id.layOrder);
		layReview = (LinearLayout) findViewById(R.id.layReview);
		layInfo = (LinearLayout) findViewById(R.id.layInfo);
		
		ivLeft.setOnClickListener(new LeftClick());
		ivRight.setOnClickListener(new RightClick());
		layBody.setVisibility(View.GONE);
		
		asy = "0";
		asyncTaskHelper = new AsyncTaskHelper();
		asyncTaskHelper.execute(asy);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			switch (resultCode) {
			case RESULT_OK:
				asy = "2";
				asyncTaskHelper = new AsyncTaskHelper();
				asyncTaskHelper.execute(asy);
				break;
			}
			break;
		case 2:
			switch (resultCode) {
			case RESULT_OK:
				bundle = data.getExtras();
				String info = bundle.getString("info");
				Toast.makeText(HotelDetailActivity.this, info, Toast.LENGTH_SHORT).show();
				pdWait = new ProgressDialog(HotelDetailActivity.this);
				pdWait.setMessage("正在更新商家资料...");
				pdWait.setCancelable(false);
				pdWait.show();
				asy = "3";
				asyncTaskHelper = new AsyncTaskHelper();
				asyncTaskHelper.execute(asy);
				break;
			}
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//如果按下的是返回键，并且没有重复
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (isUpdateSeller == 1) {
				Constants.list_seller_detail = null;
			}
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
		outState.putInt("sellerid", Constants.sellerid);
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
			pbWait.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			asy = params[0];
			if (asy.equals("0")) {
//				list_hotel = getHotelImageList();
//				list_caixi = getCaixi();
//				list_sheshi = getSheshi();
//				list_cooking = getCooking();
//				list_news = getResNews();
//				list_order = getOrder();
//				list_review = getReview();
				try {
					getHotelDetail();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = 0;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Constants.list_seller_detail = null;
					exceptionNo = -2;
				}
				try {
					getFavoriteCount();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = 0;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = -2;
				}
			}
			
			if (asy.equals("1")) {
				try {
					InsertFavorite();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = 0;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = -2;
				}
			}
			
			if (asy.equals("2")) {
				try {
					getFavoriteCount();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = 0;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = -2;
				}
			}
			
			if (asy.equals("3")) {
				try {
					getOrderTop5();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = -2;
				}
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
				if (asy.equals("1")) {
					asyInsertFavorite();
				}
				if (asy.equals("2")) {
					asyFavoriteCount();
				}
				if (asy.equals("3")) {
					asyOrderTop5();
				}
				pbWait.setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(HotelDetailActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyHotel() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(HotelDetailActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(HotelDetailActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(HotelDetailActivity.this, "数据格式有异常", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			
			tvTitle.setText(sellerDetailData.getName());
			tvKouwei.setText("口味：" + sellerDetailData.getTaste());
			tvhuanjing.setText("环境：" + sellerDetailData.getEnvironment());
			tvFuwu.setText("服务：" + sellerDetailData.getService());
			tvRenjun.setText("人均：" + sellerDetailData.getPerCapita());
			
			String info = sellerDetailData.getInfo();
			System.out.println(info);
			if (info.equals("")) {
				layInfo.setVisibility(View.GONE);
			}else {
				webSettings = wvInfo.getSettings();
				webSettings.setJavaScriptEnabled(true);
				String html = "";
		        html += "<head><body>";
				Tool tool = new Tool(HotelDetailActivity.this);
				html += tool.replaceStr(info, "white-space:nowrap;", "word-break:break-all;");
		        html += "</body></head>";
		        System.out.println(html);
		        wvInfo.loadDataWithBaseURL("", html, "text/html", "utf-8", "");
			}
			
			if (list_caixi.size() > 0) {
				gvCaixi.setSelector(new ColorDrawable(Color.TRANSPARENT));
				tagGvAdapter = new TagGvAdapter(HotelDetailActivity.this, list_caixi);
				gvCaixi.setAdapter(tagGvAdapter);
			}else {
				layCuisines.setVisibility(View.GONE);
			}
			
			tvYysj.setText("营业时间：" + sellerDetailData.getBusinesshours());
			
			if (list_sheshi.size() > 0) {
				gvSsyfw.setSelector(new ColorDrawable(Color.TRANSPARENT));
				tagGvAdapter = new TagGvAdapter(HotelDetailActivity.this, list_sheshi);
				gvSsyfw.setAdapter(tagGvAdapter);
			}else {
				layPurpose.setVisibility(View.GONE);
			}
			
			tvPhone.setText("电话：" + sellerDetailData.getTel());
			layPhone.setOnClickListener(new PhoneClick());
			
			tvAddress.setText("地址：" + sellerDetailData.getAddr());
//			layAddress.setOnClickListener(new AddressClick());
			
			if (list_cooking.size() > 0) {
				gvResCooking.setSelector(new ColorDrawable(Color.TRANSPARENT));
				tagGvAdapter = new TagGvAdapter(HotelDetailActivity.this, list_cooking);
				gvResCooking.setAdapter(tagGvAdapter);
				gvResCooking.setOnItemClickListener(new ResCookingItemClick());
			}else {
				layCooking.setVisibility(View.GONE);
			}
			
			if (list_news.size() > 0) {
				lvResNews.setSelector(new ColorDrawable(Color.TRANSPARENT));
				tagLvAdapter = new TagLvAdapter(HotelDetailActivity.this, list_news);
				lvResNews.setAdapter(tagLvAdapter);
				lvResNews.setOnItemClickListener(new ResNewsItemClick());
				if (list_news.size() >= 5) {
					btnResNews.setOnClickListener(new ResNewsClick());
					btnResNews.setVisibility(View.VISIBLE);
				}else {
					btnResNews.setVisibility(View.GONE);
				}
			}else {
				layNews.setVisibility(View.GONE);
			}
			
			if (list_order.size() > 0) {
				lvOrder.setSelector(new ColorDrawable(Color.TRANSPARENT));
				hotelOrderLvAdapter = new HotelOrderLvAdapter(HotelDetailActivity.this, list_order);
				lvOrder.setAdapter(hotelOrderLvAdapter);
				if (list_order.size() >= 5) {
					btnOrder.setOnClickListener(new OrderClick());
					btnOrder.setVisibility(View.VISIBLE);
				}else {
					btnOrder.setVisibility(View.GONE);
				}
			}else {
				layOrder.setVisibility(View.GONE);
			}
			
			if (list_review.size() > 0) {
				lvReview.setSelector(new ColorDrawable(Color.TRANSPARENT));
				hotelReviewLvAdapter = new HotelReviewLvAdapter(HotelDetailActivity.this, list_review);
				lvReview.setAdapter(hotelReviewLvAdapter);
				if (list_review.size() >= 5) {
					btnReview.setOnClickListener(new ReviewClick());
					btnReview.setVisibility(View.VISIBLE);
				}else {
					btnReview.setVisibility(View.GONE);
				}
				
			}else {
				layReview.setVisibility(View.GONE);
			}
			
			btnBooking.setOnClickListener(new BookingClick());
			
			btnCollection.setOnClickListener(new CollectionClick());
			switch (isLogin) {
			case 0:  //未登录
				
				break;
			case 1:  //已登录
				switch (exceptionNo2) {
				case -2:
					Toast.makeText(HotelDetailActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
					break;
				case -1:
					Toast.makeText(HotelDetailActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(HotelDetailActivity.this, "数据格式有异常", Toast.LENGTH_SHORT).show();
					break;
				case 1:  //未收藏
					
					break;
				case 2:  //已收藏
					btnCollection.setText("已收藏");
					btnCollection.setBackgroundResource(R.drawable.booking_button_style2);
					btnCollection.setEnabled(false);
					break;
				}
				break;
			}
			
			break;
		}
		
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		for (int i = 0; i < list_hotel.size(); i++) {
			pageViews.add(inflater.inflate(R.layout.viewpager_item_hotel, null));
		}
		mainVpAdapter = new MainVpAdapter(HotelDetailActivity.this, pageViews);
		vpHotel.setAdapter(mainVpAdapter);
		try {
			finalBitmap = FinalBitmap.create(HotelDetailActivity.this);
			for (int i = 0; i < list_hotel.size(); i++) {
				ivHotel = (ImageView) pageViews.get(i).findViewById(R.id.ivHotel);
				finalBitmap.display(ivHotel, (String) list_hotel.get(i).get("image"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(HotelDetailActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
		}
		
		layBody.setVisibility(View.VISIBLE);
	}
	
	public void asyInsertFavorite() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(HotelDetailActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(HotelDetailActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(HotelDetailActivity.this, "数据格式有异常", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			Toast.makeText(HotelDetailActivity.this, favoriteData.getInfo(), Toast.LENGTH_SHORT).show();
			btnCollection.setText("已收藏");
			btnCollection.setBackgroundResource(R.drawable.booking_button_style2);
			btnCollection.setEnabled(false);
			break;
		case 2:
			Toast.makeText(HotelDetailActivity.this, favoriteData.getInfo(), Toast.LENGTH_SHORT).show();
			btnCollection.setText("已收藏");
			btnCollection.setBackgroundResource(R.drawable.booking_button_style2);
			btnCollection.setEnabled(false);
			break;
		}
		pdWait.dismiss();
	}
	
	public void asyFavoriteCount() {
		switch (isLogin) {
		case 0:  //未登录
			
			break;
		case 1:  //已登录
			switch (exceptionNo2) {
			case -2:
				Toast.makeText(HotelDetailActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
				break;
			case -1:
				Toast.makeText(HotelDetailActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(HotelDetailActivity.this, "数据格式有异常", Toast.LENGTH_SHORT).show();
				break;
			case 1:  //未收藏
				
				break;
			case 2:  //已收藏
				btnCollection.setText("已收藏");
				btnCollection.setBackgroundResource(R.drawable.booking_button_style2);
				btnCollection.setEnabled(false);
				break;
			}
			break;
		}
	}
	
	public void asyOrderTop5() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(HotelDetailActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(HotelDetailActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			if (list_order.size() > 0) {
				lvOrder.setSelector(new ColorDrawable(Color.TRANSPARENT));
				hotelOrderLvAdapter = new HotelOrderLvAdapter(HotelDetailActivity.this, list_order);
				lvOrder.setAdapter(hotelOrderLvAdapter);
				if (list_order.size() >= 5) {
					btnOrder.setOnClickListener(new OrderClick());
					btnOrder.setVisibility(View.VISIBLE);
				}else {
					btnOrder.setVisibility(View.GONE);
				}
				isUpdateSeller = 1;
			}else {
				layOrder.setVisibility(View.GONE);
			}
			break;
		}
		pdWait.dismiss();
	}
	
	class ResCookingItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intent = new Intent(HotelDetailActivity.this, DishActivity.class);
			bundle = new Bundle();
			bundle.putInt("id", Constants.sellerid);
			String type = (String) list_cooking.get(arg2).get("classid");
			bundle.putInt("type", Integer.valueOf(type));
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class ResNewsItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intent = new Intent(HotelDetailActivity.this, NoticeDetailActivity.class);
			bundle = new Bundle();
			String idstr = (String) list_news.get(arg2).get("id");
			bundle.putInt("id", Integer.valueOf(idstr));
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class ResNewsClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intent = new Intent(HotelDetailActivity.this, NoticeActivity.class);
			bundle = new Bundle();
			bundle.putInt("id", Constants.sellerid);
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class OrderClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intent = new Intent(HotelDetailActivity.this, OrderActivity.class);
			bundle = new Bundle();
			bundle.putInt("id", Constants.sellerid);
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class ReviewClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intent = new Intent(HotelDetailActivity.this, ReviewsActivity.class);
			bundle = new Bundle();
			bundle.putInt("id", Constants.sellerid);
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class BookingClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
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
			
//			List<UserLogin> list_user = Constants.db.findAll(UserLogin.class);
//			if (list_user.size() == 0) {
			if (userLogin.getUserid().equals("")) {
				//未登录
				toLogin();
			}else {
				//已登录
				intent = new Intent(HotelDetailActivity.this, BookingActivity.class);
				bundle = new Bundle();
				bundle.putInt("id", Constants.sellerid);
				intent.putExtras(bundle);
				startActivityForResult(intent, 2);
//				overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
			}
		}
		
	}
	
	class CollectionClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
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
			
//			List<UserLogin> list_user = Constants.db.findAll(UserLogin.class);
//			if (list_user.size() == 0) {
			if (userLogin.getUserid().equals("")) {
				//未登录
				toLogin();
			}else {
				//已登录
				pdWait = new ProgressDialog(HotelDetailActivity.this);
				pdWait.setMessage("正在收藏...");
				pdWait.setCancelable(false);
				pdWait.show();
				asy = "1";
				asyncTaskHelper = new AsyncTaskHelper();
				asyncTaskHelper.execute(asy);
			}
		}
		
	}
	
	public void wxShare() {
		String text = "微信分享测试";
		WXTextObject textObject = new WXTextObject();
		textObject.text = text;
		
		WXMediaMessage message = new WXMediaMessage();
		message.mediaObject = textObject;
		message.description = text;
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = message;
		req.scene = req.WXSceneTimeline;
		
		api.sendReq(req);
	}
	
	class LeftClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (isUpdateSeller == 1) {
				Constants.list_seller_detail = null;
			}
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
		}
		
	}
	
	class RightClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			intent = new Intent(Intent.ACTION_SEND);
//			intent.putExtra(Intent.EXTRA_TEXT, "分享测试");
//			intent.setType("text/plain");
//			startActivity(Intent.createChooser(intent,"分享到:"));
			
			wxShare();
		}
		
	}
	
	class PhoneClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String phone = sellerDetailData.getTel();
			if (phone.equals("") || phone == null) {
				Toast.makeText(HotelDetailActivity.this, "该餐厅无订餐电话", Toast.LENGTH_SHORT).show();
			}else {
				toPhone(phone);
			}
		}
		
	}
	
	class AddressClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				String point = sellerDetailData.getPoint();
				String[] p = point.split(",");
				point = p[1] + "," + p[0];
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + point + "(" + sellerDetailData.getName() + ")"));
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				startActivity(i);
//				overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
				
//				Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="
//						+ 31.386608 + "," + 120.79959 + "(" + "澄湖之星（阳澄湖店）" + ")"));
//				i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//						& Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//				i2.setClassName("brut.googlemaps",
//						"com.google.android.maps.MapsActivity");
//				startActivity(i2);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(HotelDetailActivity.this, "很抱歉，您的设备没有地图服务", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void toPhone(final String phone) {
		// 确认对话框
		final AlertDialog isExit = new AlertDialog.Builder(this).create();
		// 对话框标题
		isExit.setTitle("您要电话预定吗？");
		// 对话框图标
		isExit.setIcon(R.drawable.logo);
		// 对话框消息
		//isExit.setMessage("一定要走吗？");
		// 实例化对话框上的按钮点击事件监听
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	switch (which) {
		    	case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
		    		isExit.cancel();
		    		break;
		    	case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
		    		isExit.cancel();
//		    		intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
		    		intent = new Intent (Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
					startActivity(intent);
//					overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		    		break;
		    	default:
		    		break;
		    	}
		    }
		};
		// 注册监听
		isExit.setButton(AlertDialog.BUTTON_POSITIVE, "取消", listener);
		isExit.setButton(AlertDialog.BUTTON_NEGATIVE, "拨号", listener);
		// 显示对话框
		isExit.show();
	}
	
	public void toLogin() {
		// 确认对话框
		final AlertDialog isExit = new AlertDialog.Builder(this).create();
		// 对话框标题
		isExit.setTitle("请先登录");
		// 对话框图标
		isExit.setIcon(R.drawable.logo);
		// 对话框消息
		//isExit.setMessage("一定要走吗？");
		// 实例化对话框上的按钮点击事件监听
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	switch (which) {
		    	case AlertDialog.BUTTON_POSITIVE:// "取消"第二个按钮取消对话框
		    		isExit.cancel();
		    		break;
		    	case AlertDialog.BUTTON_NEGATIVE:// "确认"按钮退出程序
		    		isExit.cancel();
		    		intent = new Intent(HotelDetailActivity.this, UserLoginActivity.class);
		    		bundle = new Bundle();
		    		bundle.putInt("to", 1);
		    		intent.putExtras(bundle);
		    		startActivityForResult(intent, 1);
//		    		overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		    		break;
		    	default:
		    		break;
		    	}
		    }
		};
		// 注册监听
		isExit.setButton(AlertDialog.BUTTON_POSITIVE, "取消", listener);
		isExit.setButton(AlertDialog.BUTTON_NEGATIVE, "确定", listener);
		// 显示对话框
		isExit.show();
	}
	
	public void getHotelDetail() throws NumberFormatException, Exception {
		if (Constants.list_seller_detail == null) {
			sellerDetail = getWebServiceData.getSellerDetail(Constants.sellerid);
			Constants.list_seller_detail = new ArrayList<Map<String,Object>>();
			map = new HashMap<String, Object>();
			map.put("sellerid", Constants.sellerid);
			map.put("sellerDetail", sellerDetail);
			Constants.list_seller_detail.add(map);
		}
		int is = 0;
		for (int i = 0; i < Constants.list_seller_detail.size(); i++) {
			int sellerid = (Integer) Constants.list_seller_detail.get(i).get("sellerid");
			if (Constants.sellerid == sellerid) {
				is = 1;
				sellerDetail = (SellerDetail) Constants.list_seller_detail.get(i).get("sellerDetail");
			}
		}
		if (is == 0) {
			sellerDetail = getWebServiceData.getSellerDetail(Constants.sellerid);
			map = new HashMap<String, Object>();
			map.put("sellerid", Constants.sellerid);
			map.put("sellerDetail", sellerDetail);
			Constants.list_seller_detail.add(map);
		}
		
		String success = sellerDetail.getSuccess();
		if (success.equals("true")) {
			sellerDetailData = sellerDetail.getData();
			List<String> image = sellerDetailData.getImage();
			for (int i = 0; i < image.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("image", Constants.url_list_image + image.get(i));
				list_hotel.add(map);
			}
			List<SellerCuisines> cuisines = sellerDetailData.getCuisines();
			for (int i = 0; i < cuisines.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", cuisines.get(i).getVid());
				map.put("title", cuisines.get(i).getName());
				list_caixi.add(map);
			}
			List<PurposeData> purposeData = sellerDetailData.getPurpose();
			for (int i = 0; i < purposeData.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", purposeData.get(i).getPurid());
				map.put("title", purposeData.get(i).getName());
				list_sheshi.add(map);
			}
			List<Cateclassify> cateclassify = sellerDetailData.getCateclassify();
			for (int i = 0; i < cateclassify.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("classid", cateclassify.get(i).getClassid());
				map.put("sid", cateclassify.get(i).getSid());
				map.put("title", cateclassify.get(i).getName());
				list_cooking.add(map);
			}
			List<Notice> notice = sellerDetailData.getNotice();
			for (int i = 0; i < notice.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", notice.get(i).getNoticeid());
				map.put("title", notice.get(i).getTitle());
				list_news.add(map);
			}
			List<Order> order = sellerDetailData.getOrder();
			for (int i = 0; i < order.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("orderid", order.get(i).getOrderid());
				map.put("code", order.get(i).getCode());
				map.put("image", Constants.url_image + order.get(i).getDir());
				map.put("user", order.get(i).getName());
				map.put("bookTime", "预订时间：" + order.get(i).getAddtime());
				map.put("eatTime", "就餐时间：" + order.get(i).getSchedule());
				String state = order.get(i).getState();
				String status = "";
				if (state.equals("0")) {
					status = "不限制";
				}
				if (state.equals("1")) {
					status = "待付款";
				}
				if (state.equals("2")) {
					status = "已取消";
				}
				if (state.equals("3")) {
					status = "待确认";
				}
				if (state.equals("4")) {
					status = "预定成功";
				}
				if (state.equals("5")) {
					status = "预定失败";
				}
				if (state.equals("6")) {
					status = "配送中";
				}
				if (state.equals("7")) {
					status = "用餐失败";
				}
				if (state.equals("8")) {
					status = "用餐成功";
				}
				if (state.equals("9")) {
					status = "订单关闭";
				}
				if (state.equals("10")) {
					status = "订单过期";
				}
				map.put("status", status);
				map.put("money", "消费金额：" + order.get(i).getTotal());
				map.put("number", "就餐人数：" + order.get(i).getConsuercount());
				list_order.add(map);
			}
			List<Reviews> reviews = sellerDetailData.getReviews();
			for (int i = 0; i < reviews.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("revid", reviews.get(i).getRevid());
				map.put("image", Constants.url_image + reviews.get(i).getDir());
				map.put("user", reviews.get(i).getName());
				map.put("time", reviews.get(i).getAddtime());
				map.put("kouwei", "口味：" + reviews.get(i).getTaste());
				map.put("huanjing", "环境：" + reviews.get(i).getEnvironment());
				map.put("Fuwu", "服务：" + reviews.get(i).getService());
				map.put("content", reviews.get(i).getMessage());
				map.put("renjun", "人均：" + reviews.get(i).getCapita() + " 元");
				map.put("tese", "餐厅特色：" + reviews.get(i).getSpecial());
				map.put("loveCooking", "喜欢的菜：" + reviews.get(i).getLikefood());
				map.put("consumeTime", "消费时间：" + reviews.get(i).getSpendingtime());
				List<Replymsg> replymsg = reviews.get(i).getReplymsg();
				String resReply = "";
				for (int j = 0; j < replymsg.size(); j++) {
					resReply += "商家回复：(" + replymsg.get(j).getRtime() + ")　" + replymsg.get(j).getContent();
				}
				map.put("resReply", resReply);
				list_review.add(map);
			}
			exceptionNo = 1;
		}else {
			Constants.list_seller_detail = null;
			exceptionNo = -1;
		}
	}
	
	public void getFavoriteCount() throws NumberFormatException, Exception {
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
		
//		List<UserLogin> list_user = Constants.db.findAll(UserLogin.class);
//		if (list_user.size() > 0) {
		if (!userLogin.getUserid().equals("")) {
			isLogin = 1;
//			String userid = list_user.get(0).getUserid();
			String userid = userLogin.getUserid();
			favorite = getWebServiceData.getFavoriteCount(Constants.sellerid, Integer.valueOf(userid), 1);
			String success = favorite.getSuccess();
			if (success.equals("true")) {
				favoriteData = favorite.getData();
				switch (favoriteData.getIsFav()) {
				case 0:
					exceptionNo2 = 2;
					break;
				case 1:
					exceptionNo2 = 1;
					break;
				}
			}else {
				exceptionNo2 = -1;
			}
		}else {
			isLogin = 0;
		}
	}
	
	public void InsertFavorite() throws NumberFormatException, Exception {
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
		
//		List<UserLogin> list_user = Constants.db.findAll(UserLogin.class);
//		String userid = list_user.get(0).getUserid();
		String userid = userLogin.getUserid();
		favorite = getWebServiceData.getInsertFavorite(Constants.sellerid, Integer.valueOf(userid), 1, tool.encode(sellerDetailData.getName()));
		String success = favorite.getSuccess();
		if (success.equals("true")) {
			favoriteData = favorite.getData();
			switch (favoriteData.getIsFav()) {
			case 0:
				exceptionNo = 2;
				break;
			case 1:
				exceptionNo = 1;
				break;
			}
		}else {
			exceptionNo = -1;
		}
	}
	
	public void getOrderTop5() throws Exception {
		list_order = new ArrayList<Map<String,Object>>();
		orderList = getWebServiceData.getOrderTop5(Constants.sellerid);
		String success = orderList.getSuccess();
		if (success.equals("true")) {
			List<Order> order = orderList.getData();
			for (int i = 0; i < order.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("orderid", order.get(i).getOrderid());
				map.put("code", order.get(i).getCode());
				map.put("image", Constants.url_image + order.get(i).getDir());
				map.put("user", order.get(i).getName());
				map.put("bookTime", "预订时间：" + order.get(i).getAddtime());
				map.put("eatTime", "就餐时间：" + order.get(i).getSchedule());
				String state = order.get(i).getState();
				String status = "";
				if (state.equals("0")) {
					status = "不限制";
				}
				if (state.equals("1")) {
					status = "待付款";
				}
				if (state.equals("2")) {
					status = "已取消";
				}
				if (state.equals("3")) {
					status = "待确认";
				}
				if (state.equals("4")) {
					status = "预定成功";
				}
				if (state.equals("5")) {
					status = "预定失败";
				}
				if (state.equals("6")) {
					status = "配送中";
				}
				if (state.equals("7")) {
					status = "用餐失败";
				}
				if (state.equals("8")) {
					status = "用餐成功";
				}
				if (state.equals("9")) {
					status = "订单关闭";
				}
				if (state.equals("10")) {
					status = "订单过期";
				}
				map.put("status", status);
				map.put("money", "消费金额：" + order.get(i).getTotal());
				map.put("number", "就餐人数：" + order.get(i).getConsuercount());
				list_order.add(map);
			}
			exceptionNo = 1;
		}else {
			exceptionNo = -1;
		}
	}
	
	public List<Map<String, Object>> getHotelImageList() {
		for (int i = 0; i < Constants.hotel_image.length; i++) {
			map = new HashMap<String, Object>();
			map.put("image", Constants.hotel_image[i]);
			list_hotel.add(map);
		}
		return list_hotel;
	}
	
	public List<Map<String, Object>> getCaixi() {
		for (int i = 0; i < 10; i++) {
			map = new HashMap<String, Object>();
			map.put("title", "苏帮菜");
			list_caixi.add(map);
		}
		return list_caixi;
	}
	
	public List<Map<String, Object>> getSheshi() {
		for (int i = 0; i < 12; i++) {
			map = new HashMap<String, Object>();
			map.put("title", "可以刷卡");
			list_sheshi.add(map);
		}
		return list_sheshi;
	}
	
	public List<Map<String, Object>> getCooking() {
		for (int i = 0; i < 8; i++) {
			map = new HashMap<String, Object>();
			map.put("title", "特色菜");
			list_cooking.add(map);
		}
		return list_cooking;
	}
	
	public List<Map<String, Object>> getResNews() {
		for (int i = 0; i < 5; i++) {
			map = new HashMap<String, Object>();
			map.put("title", "餐厅动态餐厅动态餐厅动态餐厅动态餐厅动态餐厅动态餐厅动态餐厅动态");
			list_news.add(map);
		}
		return list_news;
	}
	
	public List<Map<String, Object>> getOrder() {
		for (int i = 0; i < 5; i++) {
			map = new HashMap<String, Object>();
			map.put("image", "");
			map.put("user", "匿名会员");
			map.put("bookTime", "预订时间：2013-06-19 10:51:16");
			map.put("eatTime", "就餐时间：2013/7/25 19:00:00 ");
			map.put("status", "用餐成功");
			map.put("money", "消费金额：120");
			map.put("number", "就餐人数：3");
			list_order.add(map);
		}
		return list_order;
	}
	
	public List<Map<String, Object>> getReview() {
		for (int i = 0; i < 5; i++) {
			map = new HashMap<String, Object>();
			map.put("image", "");
			map.put("user", "匿名会员");
			map.put("time", "2013-04-25 15:06:00");
			map.put("kouwei", "口味: 10");
			map.put("huanjing", "环境: 10");
			map.put("Fuwu", "服务:10");
			map.put("content", "餐厅环境不错，菜味道很好！");
			map.put("renjun", "人均: 50 元");
			map.put("tese", "餐厅特色: ******");
			map.put("loveCooking", "喜欢的菜: ******");
			map.put("consumeTime", "消费时间: 2013-04-12");
			map.put("resReply", "商家回复: (2013-05-08 16:20:20)  你二妹");
			list_review.add(map);
		}
		return list_review;
	}

}
