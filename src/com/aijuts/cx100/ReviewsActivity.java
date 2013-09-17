package com.aijuts.cx100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aijuts.cx100.HotelDetailActivity.ResNewsItemClick;
import com.aijuts.cx100.NoticeDetailActivity.AsyncTaskHelper;
import com.aijuts.cx100.NoticeDetailActivity.NoticeWebViewClient;
import com.aijuts.cx100.adapter.HotelOrderLvAdapter;
import com.aijuts.cx100.adapter.HotelReviewLvAdapter;
import com.aijuts.cx100.adapter.TagLvAdapter;
import com.aijuts.cx100.entity.Notice;
import com.aijuts.cx100.entity.NoticeList;
import com.aijuts.cx100.entity.Order;
import com.aijuts.cx100.entity.OrderList;
import com.aijuts.cx100.entity.Replymsg;
import com.aijuts.cx100.entity.Reviews;
import com.aijuts.cx100.entity.ReviewsList;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.GetWebServiceData;
import com.aijuts.cx100.util.Tool;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ReviewsActivity extends Activity {
	
	private ProgressBar pbWait;
	private TextView tvTitle;
	private ImageView ivLeft;
	private LinearLayout layBody;
	private ListView lvReviews;
	private HotelReviewLvAdapter hotelReviewLvAdapter;
	private int id = 0;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	private Intent intent;
	private Bundle bundle;
	private GetWebServiceData getWebServiceData;
	private ReviewsList reviewsList;
	private List<Map<String, Object>> list;
	private Map<String, Object> map;
	//-2表示访问webservice有异常，0，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_reviews);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		if (savedInstanceState != null) {
			id = savedInstanceState.getInt("id");
		}else {
			bundle = getIntent().getExtras();
			id = bundle.getInt("id");
		}
		
		list = new ArrayList<Map<String,Object>>();
		getWebServiceData = new GetWebServiceData();
		reviewsList = new ReviewsList();
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		lvReviews = (ListView) findViewById(R.id.lvReviews);
		
		tvTitle.setText("餐厅点评");
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
			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
			return false;
		}
		return false;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("id", id);
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
					list = getOrder();
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
					asyReviews();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(ReviewsActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyReviews() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(ReviewsActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(ReviewsActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			lvReviews.setSelector(new ColorDrawable(Color.TRANSPARENT));
			hotelReviewLvAdapter = new HotelReviewLvAdapter(ReviewsActivity.this, list);
			lvReviews.setAdapter(hotelReviewLvAdapter);
			break;
		}
		pbWait.setVisibility(View.GONE);
		layBody.setVisibility(View.VISIBLE);
	}
	
	class LeftClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
		}
		
	}
	
	public List<Map<String, Object>> getOrder() throws Exception {
		reviewsList = getWebServiceData.getSellerReviewsAll(id);
		String success = reviewsList.getSuccess();
		if (success.equals("true")) {
			List<Reviews> list_reviews = reviewsList.getData();
			for (int i = 0; i < list_reviews.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("revid", list_reviews.get(i).getRevid());
				map.put("image", Constants.url_image + list_reviews.get(i).getDir());
				map.put("user", list_reviews.get(i).getName());
				map.put("time", list_reviews.get(i).getAddtime());
				map.put("kouwei", "口味：" + list_reviews.get(i).getTaste());
				map.put("huanjing", "环境：" + list_reviews.get(i).getEnvironment());
				map.put("Fuwu", "服务：" + list_reviews.get(i).getService());
				map.put("content", list_reviews.get(i).getMessage());
				map.put("renjun", "人均：" + list_reviews.get(i).getCapita() + " 元");
				map.put("tese", "餐厅特色：" + list_reviews.get(i).getSpecial());
				map.put("loveCooking", "喜欢的菜：" + list_reviews.get(i).getLikefood());
				map.put("consumeTime", "消费时间：" + list_reviews.get(i).getSpendingtime());
				List<Replymsg> replymsg = list_reviews.get(i).getReplymsg();
				String resReply = "";
				for (int j = 0; j < replymsg.size(); j++) {
					resReply += "商家回复：(" + replymsg.get(j).getRtime() + ")　" + replymsg.get(j).getContent();
				}
				map.put("resReply", resReply);
				list.add(map);
			}
			exceptionNo = 1;
		}else {
			exceptionNo = -1;
		}
		return list;
	}

}
