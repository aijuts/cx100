package com.aijuts.cx100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aijuts.cx100.HotelDetailActivity.ResNewsItemClick;
import com.aijuts.cx100.NoticeDetailActivity.AsyncTaskHelper;
import com.aijuts.cx100.NoticeDetailActivity.NoticeWebViewClient;
import com.aijuts.cx100.adapter.HotelOrderLvAdapter;
import com.aijuts.cx100.adapter.TagLvAdapter;
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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OrderActivity extends Activity {
	
	private ProgressBar pbWait;
	private TextView tvTitle;
	private ImageView ivLeft;
	private LinearLayout layBody;
	private ListView lvOrder;
	private HotelOrderLvAdapter hotelOrderLvAdapter;
	private int id = 0;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	private Intent intent;
	private Bundle bundle;
	private GetWebServiceData getWebServiceData;
	private OrderList orderList;
	private List<Map<String, Object>> list;
	private Map<String, Object> map;
	//-2��ʾ����webservice���쳣��0��-1��ʾ�����������쳣��1��ʾ����
	private int exceptionNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_order);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		if (savedInstanceState != null) {
			id = savedInstanceState.getInt("id");
		}else {
			bundle = getIntent().getExtras();
			id = bundle.getInt("id");
		}
		
		list = new ArrayList<Map<String,Object>>();
		getWebServiceData = new GetWebServiceData();
		orderList = new OrderList();
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		lvOrder = (ListView) findViewById(R.id.lvOrder);
		
		tvTitle.setText("������¼");
		ivLeft.setOnClickListener(new LeftClick());
		layBody.setVisibility(View.GONE);
		
		asy = "0";
		asyncTaskHelper = new AsyncTaskHelper();
		asyncTaskHelper.execute(asy);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//������µ��Ƿ��ؼ�������û���ظ�
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
					asyOrder();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(OrderActivity.this, "������", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyOrder() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(OrderActivity.this, "���ʷ�����ʧ��", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(OrderActivity.this, "��ȡ����ʧ��", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			lvOrder.setSelector(new ColorDrawable(Color.TRANSPARENT));
			hotelOrderLvAdapter = new HotelOrderLvAdapter(OrderActivity.this, list);
			lvOrder.setAdapter(hotelOrderLvAdapter);
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
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
		}
		
	}
	
	public List<Map<String, Object>> getOrder() throws Exception {
		orderList = getWebServiceData.getSellerOrderAll(id);
		String success = orderList.getSuccess();
		if (success.equals("true")) {
			List<Order> list_order = orderList.getData();
			for (int i = 0; i < list_order.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("orderid", list_order.get(i).getOrderid());
				map.put("code", list_order.get(i).getCode());
				map.put("image", Constants.url_image + list_order.get(i).getDir());
				map.put("user", list_order.get(i).getName());
				map.put("bookTime", "Ԥ��ʱ�䣺" + list_order.get(i).getAddtime());
				map.put("eatTime", "�Ͳ�ʱ�䣺" + list_order.get(i).getSchedule());
				String state = list_order.get(i).getState();
				String status = "";
				if (state.equals("0")) {
					status = "������";
				}
				if (state.equals("1")) {
					status = "������";
				}
				if (state.equals("2")) {
					status = "��ȡ��";
				}
				if (state.equals("3")) {
					status = "��ȷ��";
				}
				if (state.equals("4")) {
					status = "Ԥ���ɹ�";
				}
				if (state.equals("5")) {
					status = "Ԥ��ʧ��";
				}
				if (state.equals("6")) {
					status = "������";
				}
				if (state.equals("7")) {
					status = "�ò�ʧ��";
				}
				if (state.equals("8")) {
					status = "�òͳɹ�";
				}
				if (state.equals("9")) {
					status = "�����ر�";
				}
				if (state.equals("10")) {
					status = "��������";
				}
				map.put("status", status);
				map.put("money", "���ѽ�" + list_order.get(i).getTotal());
				map.put("number", "�Ͳ�������" + list_order.get(i).getConsuercount());
				list.add(map);
			}
			exceptionNo = 1;
		}else {
			exceptionNo = -1;
		}
		return list;
	}

}
