package com.aijuts.cx100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aijuts.cx100.adapter.TagGvAdapter;
import com.aijuts.cx100.entity.Businesszone;
import com.aijuts.cx100.entity.BusinesszoneData;
import com.aijuts.cx100.entity.Cuisines;
import com.aijuts.cx100.entity.CuisinesData;
import com.aijuts.cx100.entity.PerCapita;
import com.aijuts.cx100.entity.PerCapitaData;
import com.aijuts.cx100.entity.Purpose;
import com.aijuts.cx100.entity.PurposeData;
import com.aijuts.cx100.entity.Railway;
import com.aijuts.cx100.entity.RailwayData;
import com.aijuts.cx100.entity.Zone;
import com.aijuts.cx100.entity.ZoneData;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.GetWebServiceData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity {
	
	private EditText etSearch;
	private Button btnSearch;
	private TextView tvTitle, tvCuisines, tvPurpose, tvPercapita, tvAdminArea, tvCircle, tvSubway;
	private ImageView ivLeft;
	private LinearLayout layBody;
	private ProgressBar pbWait;
	private GridView gvCuisines, gvPurpose, gvPercapita, gvAdminArea, gvCircle, gvSubway;
	private int totalCuisines, totalPurpose, totalPercapita, totalAdminArea, totalCircle, totalSubway;
	private List<Map<String, Object>> list_cuisines, list_purpose, list_percapita, list_adminArea, 
		list_circle, list_subway;
	private Map<String, Object> map;
	private TagGvAdapter tagGvAdapter;
	private String asy, likeName;
	private AsyncTaskHelper asyncTaskHelper;
	private Intent intent;
	private Bundle bundle;
	private GetWebServiceData getWebServiceData;
	
	private boolean flagCuisines = false, flagPurpose = false, flagPercapita = false, 
			flagAdminArea = false, flagCircle = false, flagSubway = false;
	//-2表示访问webservice有异常，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_search);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		list_cuisines = new ArrayList<Map<String,Object>>();
		list_purpose = new ArrayList<Map<String,Object>>();
		list_percapita = new ArrayList<Map<String,Object>>();
		list_adminArea = new ArrayList<Map<String,Object>>();
		list_circle = new ArrayList<Map<String,Object>>();
		list_subway = new ArrayList<Map<String,Object>>();
		getWebServiceData = new GetWebServiceData();
//		cuisines = new Cuisines();
//		purpose = new Purpose();
//		perCapita = new PerCapita();
//		zone = new Zone();
//		businesszone = new Businesszone();
//		railway = new Railway();
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		etSearch = (EditText) findViewById(R.id.etSearch);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		tvCuisines = (TextView) findViewById(R.id.tvCuisines);
		tvPurpose = (TextView) findViewById(R.id.tvPurpose);
		tvPercapita = (TextView) findViewById(R.id.tvPercapita);
		tvAdminArea = (TextView) findViewById(R.id.tvAdminArea);
		tvCircle = (TextView) findViewById(R.id.tvCircle);
		tvSubway = (TextView) findViewById(R.id.tvSubway);
		gvCuisines = (GridView) findViewById(R.id.gvCuisines);
		gvPurpose = (GridView) findViewById(R.id.gvPurpose);
		gvPercapita = (GridView) findViewById(R.id.gvPercapita);
		gvAdminArea = (GridView) findViewById(R.id.gvAdminArea);
		gvCircle = (GridView) findViewById(R.id.gvCircle);
		gvSubway = (GridView) findViewById(R.id.gvSubway);
		
		tvTitle.setText("餐厅搜索");
		ivLeft.setOnClickListener(new LeftClick());
		layBody.setVisibility(View.GONE);
		gvCuisines.setVisibility(View.GONE);
		gvPurpose.setVisibility(View.GONE);
		gvPercapita.setVisibility(View.GONE);
		gvAdminArea.setVisibility(View.GONE);
		gvCircle.setVisibility(View.GONE);
		gvSubway.setVisibility(View.GONE);
		
		btnSearch.setOnClickListener(new SearchClick());
		
		asy = "0";
		asyncTaskHelper = new AsyncTaskHelper();
		asyncTaskHelper.execute(asy);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//如果按下的是返回键，并且没有重复
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
			}
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
			return false;
		}
		return false;
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
					list_cuisines = getCuisines();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Constants.cuisines = null;
					exceptionNo = -2;
				}
				try {
					list_purpose = getPurpose();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Constants.purpose = null;
					exceptionNo = -2;
				}
				try {
					list_percapita = getPercapita();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Constants.perCapita = null;
					exceptionNo = -2;
				}
				try {
					list_adminArea = getAdminArea();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Constants.zone = null;
					exceptionNo = -2;
				}
				try {
					list_circle = getCircle();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Constants.businesszone = null;
					exceptionNo = -2;
				}
				try {
					list_subway = getSubway();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Constants.railway = null;
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
					asySearch();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(SearchActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asySearch() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(SearchActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(SearchActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			tvCuisines.setText("菜系（" + totalCuisines + "）");
			tvCuisines.setOnClickListener(new CuisinesClick());
			
			tvPurpose.setText("目的（" + totalPurpose + "）");
			tvPurpose.setOnClickListener(new PurposeClick());
			
			tvPercapita.setText("人均（" + totalPercapita + "）");
			tvPercapita.setOnClickListener(new PercapitaClick());
			
			tvAdminArea.setText("行政区（" + totalAdminArea + "）");
			tvAdminArea.setOnClickListener(new AdminAreaClick());
			
			tvCircle.setText("商圈（" + totalCircle + "）");
			tvCircle.setOnClickListener(new CircleClick());
			
			tvSubway.setText("地铁（" + totalSubway + "）");
			tvSubway.setOnClickListener(new SubwayClick());
			
			gvCuisines.setSelector(new ColorDrawable(Color.TRANSPARENT));
			tagGvAdapter = new TagGvAdapter(SearchActivity.this, list_cuisines);
			gvCuisines.setAdapter(tagGvAdapter);
			gvCuisines.setOnItemClickListener(new CuisinesItemClick());
			
			gvPurpose.setSelector(new ColorDrawable(Color.TRANSPARENT));
			tagGvAdapter = new TagGvAdapter(SearchActivity.this, list_purpose);
			gvPurpose.setAdapter(tagGvAdapter);
			gvPurpose.setOnItemClickListener(new PurposeItemClick());
			
			gvPercapita.setSelector(new ColorDrawable(Color.TRANSPARENT));
			tagGvAdapter = new TagGvAdapter(SearchActivity.this, list_percapita);
			gvPercapita.setAdapter(tagGvAdapter);
			gvPercapita.setOnItemClickListener(new PercapitaItemClick());
			
			gvAdminArea.setSelector(new ColorDrawable(Color.TRANSPARENT));
			tagGvAdapter = new TagGvAdapter(SearchActivity.this, list_adminArea);
			gvAdminArea.setAdapter(tagGvAdapter);
			gvAdminArea.setOnItemClickListener(new AdminAreaItemClick());
			
			gvCircle.setSelector(new ColorDrawable(Color.TRANSPARENT));
			tagGvAdapter = new TagGvAdapter(SearchActivity.this, list_circle);
			gvCircle.setAdapter(tagGvAdapter);
			gvCircle.setOnItemClickListener(new CircleItemClick());
			
			gvSubway.setSelector(new ColorDrawable(Color.TRANSPARENT));
			tagGvAdapter = new TagGvAdapter(SearchActivity.this, list_subway);
			gvSubway.setAdapter(tagGvAdapter);
			gvSubway.setOnItemClickListener(new SubwayItemClick());
			break;
		}
		pbWait.setVisibility(View.GONE);
		layBody.setVisibility(View.VISIBLE);
	}
	
	class LeftClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
			}
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
		}
		
	}
	
	class CuisinesClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (flagCuisines == false) {
				gvCuisines.setVisibility(View.VISIBLE);
				flagCuisines = true;
			}else {
				gvCuisines.setVisibility(View.GONE);
				flagCuisines = false;
			}
		}
		
	}
	
	class PurposeClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (flagPurpose == false) {
				gvPurpose.setVisibility(View.VISIBLE);
				flagPurpose = true;
			}else {
				gvPurpose.setVisibility(View.GONE);
				flagPurpose = false;
			}
		}
		
	}
	
	class PercapitaClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (flagPercapita == false) {
				gvPercapita.setVisibility(View.VISIBLE);
				flagPercapita = true;
			}else {
				gvPercapita.setVisibility(View.GONE);
				flagPercapita = false;
			}
		}
		
	}
	
	class AdminAreaClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (flagAdminArea == false) {
				gvAdminArea.setVisibility(View.VISIBLE);
				flagAdminArea = true;
			}else {
				gvAdminArea.setVisibility(View.GONE);
				flagAdminArea = false;
			}
		}
		
	}
	
	class CircleClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (flagCircle == false) {
				gvCircle.setVisibility(View.VISIBLE);
				flagCircle = true;
			}else {
				gvCircle.setVisibility(View.GONE);
				flagCircle = false;
			}
		}
		
	}
	
	class SubwayClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (flagSubway == false) {
				gvSubway.setVisibility(View.VISIBLE);
				flagSubway = true;
			}else {
				gvSubway.setVisibility(View.GONE);
				flagSubway = false;
			}
		}
		
	}
	
	class CuisinesItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intent = new Intent(SearchActivity.this, HotelActivity.class);
			bundle = new Bundle();
			bundle.putString("tagKey", list_cuisines.get(arg2).get("tag") + "");
			bundle.putString("tagValue", list_cuisines.get(arg2).get("id") + "");
			bundle.putString("tagInfo", list_cuisines.get(arg2).get("title") + "");
			bundle.putString("likeName", "0");
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class PurposeItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intent = new Intent(SearchActivity.this, HotelActivity.class);
			bundle = new Bundle();
			bundle.putString("tagKey", list_purpose.get(arg2).get("tag") + "");
			bundle.putString("tagValue", list_purpose.get(arg2).get("id") + "");
			bundle.putString("tagInfo", list_purpose.get(arg2).get("title") + "");
			bundle.putString("likeName", "0");
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class PercapitaItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intent = new Intent(SearchActivity.this, HotelActivity.class);
			bundle = new Bundle();
			bundle.putString("tagKey", list_percapita.get(arg2).get("tag") + "");
			bundle.putString("tagValue", list_percapita.get(arg2).get("id") + "");
			bundle.putString("tagInfo", list_percapita.get(arg2).get("title") + "");
			bundle.putString("likeName", "0");
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class AdminAreaItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intent = new Intent(SearchActivity.this, HotelActivity.class);
			bundle = new Bundle();
			bundle.putString("tagKey", list_adminArea.get(arg2).get("tag") + "");
			bundle.putString("tagValue", list_adminArea.get(arg2).get("id") + "");
			bundle.putString("tagInfo", list_adminArea.get(arg2).get("title") + "");
			bundle.putString("likeName", "0");
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class CircleItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intent = new Intent(SearchActivity.this, HotelActivity.class);
			bundle = new Bundle();
			bundle.putString("tagKey", list_circle.get(arg2).get("tag") + "");
			bundle.putString("tagValue", list_circle.get(arg2).get("id") + "");
			bundle.putString("tagInfo", list_circle.get(arg2).get("title") + "");
			bundle.putString("likeName", "0");
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class SubwayItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intent = new Intent(SearchActivity.this, HotelActivity.class);
			bundle = new Bundle();
			bundle.putString("tagKey", list_subway.get(arg2).get("tag") + "");
			bundle.putString("tagValue", list_subway.get(arg2).get("id") + "");
			bundle.putString("tagInfo", list_subway.get(arg2).get("title") + "");
			bundle.putString("likeName", "0");
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class SearchClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intent = new Intent(SearchActivity.this, HotelActivity.class);
			bundle = new Bundle();
			bundle.putString("tagKey", "0");
			bundle.putString("tagValue", "0");
			bundle.putString("tagInfo", "");
			likeName = etSearch.getText() + "";
			if (likeName.trim().equals("")) {
				likeName = "0";
			}
			System.out.println("likeName:" + likeName.trim() + "lllll");
			bundle.putString("likeName", likeName.trim());
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	public List<Map<String, Object>> getCuisines() throws Exception {
		if (Constants.cuisines == null) {
			Constants.cuisines = getWebServiceData.getCuisinesAll();
		}
		totalCuisines = Constants.cuisines.getTotal();
		String success = Constants.cuisines.getSuccess();
		if (success.equals("true")) {
			List<CuisinesData> listCuisines = Constants.cuisines.getData();
			for (int i = 0; i < listCuisines.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", listCuisines.get(i).getVid());
				map.put("title", listCuisines.get(i).getName());
				map.put("tag", 1);
				list_cuisines.add(map);
			}
			exceptionNo = 1;
		}else {
			Constants.cuisines = null;
			exceptionNo = -1;
		}
		return list_cuisines;
	}
	
	public List<Map<String, Object>> getPurpose() throws Exception {
		if (Constants.purpose == null) {
			Constants.purpose = getWebServiceData.getPurposeAll();
		}
		totalPurpose = Constants.purpose.getTotal();
		String success = Constants.purpose.getSuccess();
		if (success.equals("true")) {
			List<PurposeData> listPurpose = Constants.purpose.getData();
			for (int i = 0; i < listPurpose.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", listPurpose.get(i).getPurid());
				map.put("title", listPurpose.get(i).getName());
				map.put("tag", 2);
				list_purpose.add(map);
			}
			exceptionNo = 1;
		}else {
			Constants.purpose = null;
			exceptionNo = -1;
		}
		return list_purpose;
	}
	
	public List<Map<String, Object>> getPercapita() throws Exception {
		if (Constants.perCapita == null) {
			Constants.perCapita = getWebServiceData.getPerCapitaAll();
		}
		totalPercapita = Constants.perCapita.getTotal();
		String success = Constants.perCapita.getSuccess();
		if (success.equals("true")) {
			List<PerCapitaData> listPerCapita = Constants.perCapita.getData();
			for (int i = 0; i < listPerCapita.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", listPerCapita.get(i).getConsumid());
				map.put("title", listPerCapita.get(i).getName());
				map.put("tag", 3);
				list_percapita.add(map);
			}
			exceptionNo = 1;
		}else {
			Constants.perCapita = null;
			exceptionNo = -1;
		}
		return list_percapita;
	}
	
	public List<Map<String, Object>> getAdminArea() throws Exception {
		if (Constants.zone == null) {
			Constants.zone = getWebServiceData.getZoneAll();
		}		
		totalAdminArea = Constants.zone.getTotal();
		String success = Constants.zone.getSuccess();
		if (success.equals("true")) {
			List<ZoneData> listZone = Constants.zone.getData();
			for (int i = 0; i < listZone.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", listZone.get(i).getZoneid());
				map.put("title", listZone.get(i).getName());
				map.put("tag", 4);
				list_adminArea.add(map);
			}
			exceptionNo = 1;
		}else {
			Constants.zone = null;
			exceptionNo = -1;
		}
		return list_adminArea;
	}
	
	public List<Map<String, Object>> getCircle() throws Exception {
		if (Constants.businesszone == null) {
			Constants.businesszone = getWebServiceData.getBusinesszoneAll();
		}
		totalCircle = Constants.businesszone.getTotal();
		String success = Constants.businesszone.getSuccess();
		if (success.equals("true")) {
			List<BusinesszoneData> listBusinesszone = Constants.businesszone.getData();
			for (int i = 0; i < listBusinesszone.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", listBusinesszone.get(i).getZid());
				map.put("title", listBusinesszone.get(i).getName());
				map.put("tag", 5);
				list_circle.add(map);
			}
			exceptionNo = 1;
		}else {
			Constants.businesszone = null;
			exceptionNo = -1;
		}
		return list_circle;
	}
	
	public List<Map<String, Object>> getSubway() throws Exception {
		if (Constants.railway == null) {
			Constants.railway = getWebServiceData.getRailwayAll();
		}
		totalSubway = Constants.railway.getTotal();
		String success = Constants.railway.getSuccess();
		if (success.equals("true")) {
			List<RailwayData> list_railway = Constants.railway.getData();
			for (int i = 0; i < list_railway.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", list_railway.get(i).getRailid());
				map.put("title", list_railway.get(i).getName());
				map.put("tag", 6);
				list_subway.add(map);
			}
			exceptionNo = 1;
		}else {
			Constants.railway = null;
			exceptionNo = -1;
		}
		return list_subway;
	}

}
