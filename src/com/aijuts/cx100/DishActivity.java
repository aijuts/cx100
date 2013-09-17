package com.aijuts.cx100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aijuts.cx100.HotelDetailActivity.ResNewsItemClick;
import com.aijuts.cx100.NoticeDetailActivity.AsyncTaskHelper;
import com.aijuts.cx100.NoticeDetailActivity.NoticeWebViewClient;
import com.aijuts.cx100.adapter.DishLvAdapter;
import com.aijuts.cx100.adapter.HotelOrderLvAdapter;
import com.aijuts.cx100.adapter.TagLvAdapter;
import com.aijuts.cx100.entity.Dish;
import com.aijuts.cx100.entity.DishData;
import com.aijuts.cx100.entity.Notice;
import com.aijuts.cx100.entity.NoticeList;
import com.aijuts.cx100.entity.Order;
import com.aijuts.cx100.entity.OrderList;
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

public class DishActivity extends Activity {
	
	private ProgressBar pbWait;
	private TextView tvTitle;
	private ImageView ivLeft;
	private LinearLayout layBody;
	private ListView lvDish;
	private DishLvAdapter dishLvAdapter;
	private int id = 0, type = 0;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	private Intent intent;
	private Bundle bundle;
	private GetWebServiceData getWebServiceData;
	private Dish dish;
	private List<Map<String, Object>> list;
	private Map<String, Object> map;
	//-2表示访问webservice有异常，0，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_dish);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		if (savedInstanceState != null) {
			id = savedInstanceState.getInt("id");
			type = savedInstanceState.getInt("type");
		}else {
			bundle = getIntent().getExtras();
			id = bundle.getInt("id");
			type = bundle.getInt("type");
		}
		
		list = new ArrayList<Map<String,Object>>();
		getWebServiceData = new GetWebServiceData();
		dish = new Dish();
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		lvDish = (ListView) findViewById(R.id.lvDish);
		
		tvTitle.setText("餐厅菜品");
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
		outState.putInt("type", type);
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
					list = getDish();
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
					asyDish();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(DishActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyDish() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(DishActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(DishActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			lvDish.setSelector(new ColorDrawable(Color.TRANSPARENT));
			dishLvAdapter = new DishLvAdapter(DishActivity.this, list);
			lvDish.setAdapter(dishLvAdapter);
			lvDish.setOnItemClickListener(new DishItemClick());
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
	
	class DishItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intent = new Intent(DishActivity.this, DishDetailActivity.class);
			bundle = new Bundle();
			String disid = (String) list.get(arg2).get("disid");
			bundle.putInt("id", Integer.valueOf(disid));
			intent.putExtras(bundle);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	public List<Map<String, Object>> getDish() throws Exception {
		dish = getWebServiceData.getSellerDishAll(id, type);
		String success = dish.getSuccess();
		if (success.equals("true")) {
			List<DishData> list_dish = dish.getData();
			for (int i = 0; i < list_dish.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("disid", list_dish.get(i).getDisid());
				map.put("code", list_dish.get(i).getCode());
				map.put("title", list_dish.get(i).getName());
				map.put("hotel", list_dish.get(i).getSename());
				map.put("dishType", list_dish.get(i).getCaname());
				map.put("price", list_dish.get(i).getPrice());
				map.put("unit", "/" + list_dish.get(i).getUnit());
				List<String> list_image = list_dish.get(i).getImage();
				String image;
				if (list_image.get(0).equals("")) {
					image = "";
				}else {
					image = Constants.url_list_image + list_image.get(0);
				}
				map.put("image", image);
				list.add(map);
			}
			exceptionNo = 1;
		}else {
			exceptionNo = -1;
		}
		return list;
	}

}
