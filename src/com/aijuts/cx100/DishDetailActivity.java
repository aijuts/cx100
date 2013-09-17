package com.aijuts.cx100;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.aijuts.cx100.DishActivity.AsyncTaskHelper;
import com.aijuts.cx100.DishActivity.DishItemClick;
import com.aijuts.cx100.adapter.DishLvAdapter;
import com.aijuts.cx100.entity.Dish;
import com.aijuts.cx100.entity.DishData;
import com.aijuts.cx100.entity.DishDetail;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DishDetailActivity extends Activity {
	
	private ProgressBar pbWait;
	private LinearLayout layBody, layCode, layType, layPrice, layInfo, layHotel;
	private ImageView ivLeft, ivDish;
	private TextView tvTitle, tvCode, tvType, tvPrice, tvInfo, tvHotel;
	private WebView wvContent;
	private WebSettings webSettings;
	private int id = 0;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	private Intent intent;
	private Bundle bundle;
	private GetWebServiceData getWebServiceData;
	private DishDetail dishDetail;
	private DishData dishData;
	private FinalBitmap finalBitmap;
	//-2表示访问webservice有异常，0，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_dish_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		if (savedInstanceState != null) {
			id = savedInstanceState.getInt("id");
		}else {
			bundle = getIntent().getExtras();
			id = bundle.getInt("id");
		}
		
		getWebServiceData = new GetWebServiceData();
		dishDetail = new DishDetail();
		dishData = new DishData();
		finalBitmap = FinalBitmap.create(this);
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		ivDish = (ImageView) findViewById(R.id.ivDish);
		layCode = (LinearLayout) findViewById(R.id.layCode);
		layType = (LinearLayout) findViewById(R.id.layType);
		layPrice = (LinearLayout) findViewById(R.id.layPrice);
		layInfo = (LinearLayout) findViewById(R.id.layInfo);
		layHotel = (LinearLayout) findViewById(R.id.layHotel);
		tvCode = (TextView) findViewById(R.id.tvCode);
		tvType = (TextView) findViewById(R.id.tvType);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		tvInfo = (TextView) findViewById(R.id.tvInfo);
		tvHotel = (TextView) findViewById(R.id.tvHotel);
		wvContent = (WebView) findViewById(R.id.wvContent);
		
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
					getDish();
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
					asyDishDetail();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(DishDetailActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyDishDetail() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(DishDetailActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(DishDetailActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			if (dishData.getName().equals("")) {
				tvTitle.setText("餐厅菜品");
			}else {
				tvTitle.setText(dishData.getName());
			}
			
			List<String> list_image = dishData.getImage();
			String image = Constants.url_list_image + list_image.get(0);
			try {
				finalBitmap.display(ivDish, image);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(DishDetailActivity.this, "", Toast.LENGTH_SHORT).show();
			}
			
			if (dishData.getCode().equals("")) {
				layCode.setVisibility(View.GONE);
			}else {
				tvCode.setText(dishData.getCode());
			}
			
			if (dishData.getCaname().equals("")) {
				layType.setVisibility(View.GONE);
			}else {
				tvType.setText(dishData.getCaname());
			}
			
			if (dishData.getPrice().equals("")) {
				layPrice.setVisibility(View.GONE);
			}else {
				tvPrice.setText(dishData.getPrice());
			}
			
			if (dishData.getInfo().equals("")) {
				layInfo.setVisibility(View.GONE);
			}else {
				tvInfo.setText(dishData.getInfo());
			}
			
			if (dishData.getSename().equals("")) {
				layHotel.setVisibility(View.GONE);
			}else {
				tvHotel.setText(dishData.getSename());
			}
			
			String content = dishData.getContent();
			if (content.equals("")) {
				wvContent.setVisibility(View.GONE);
			}else {
				webSettings = wvContent.getSettings();
				webSettings.setJavaScriptEnabled(true);
				String html = "";
		        html += "<head><body>";
				Tool tool = new Tool(DishDetailActivity.this);
				html += tool.replaceStr(content, "white-space:nowrap;", "word-break:break-all;");
		        html += "</body></head>";
		        System.out.println(html);
		        wvContent.loadDataWithBaseURL("", html, "text/html", "utf-8", "");
			}
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
	
	public void getDish() throws Exception {
		dishDetail = getWebServiceData.getSellerDishDetail(id);
		String success = dishDetail.getSuccess();
		if (success.equals("true")) {
			dishData = dishDetail.getData();
			exceptionNo = 1;
		}else {
			exceptionNo = -1;
		}
	}

}
