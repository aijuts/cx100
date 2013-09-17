package com.aijuts.cx100;

import java.util.List;

import net.tsz.afinal.FinalDb;

import com.aijuts.cx100.data.entity.AppSetting;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.Location;
import com.baidu.location.LocationClient;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends Activity {
	
	private IWXAPI api;
	
	private Handler handler;
	private Intent intent;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	private Location location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		
		regToWx();
		
		location = (Location) getApplication();
		
		asy = "0";
		asyncTaskHelper = new AsyncTaskHelper();
		asyncTaskHelper.execute(asy);
	}
	
	private void regToWx() {
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
		api.registerApp(Constants.APP_ID);
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
			if (asy.equals("0")) {
				Constants.db = FinalDb.create(WelcomeActivity.this, "cxlm");
//				List<AppSetting> list = Constants.db.findAll(AppSetting.class);
//				if (list.size() == 0) {
//					AppSetting appSetting = new AppSetting();
//					appSetting.setUserCount(1);
//					Constants.db.save(appSetting);
//				}else {
//					int id = list.get(0).getId();
//					int count = list.get(0).getUserCount();
//					AppSetting appSetting = new AppSetting();
//					appSetting.setId(id);
//					appSetting.setUserCount(count + 1);
//					Constants.db.update(appSetting);
//				}
				
				handler = new Handler();
				handler.postDelayed(runnable, 1000);
			}
		}
		
	}

	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
//			List<AppSetting> list = Constants.db.findAll(AppSetting.class);
//			int count = list.get(0).getUserCount();
//			if (count == 1) {
//				intent = new Intent(WelcomeActivity.this, GuidePageActivity.class);
//			}else {
//				intent = new Intent(WelcomeActivity.this, MainActivity.class);
//			}
//			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
//			finish();
			
			SharedPreferences preferences = getSharedPreferences("appSetting", 0);
			int id = preferences.getInt("id", 0);
			int count = preferences.getInt("count", 0);
			System.out.println("count:" + count);
//			if (count == 0) {
//				intent = new Intent(WelcomeActivity.this, GuidePageActivity.class);
//			}else {
//				intent = new Intent(WelcomeActivity.this, MainActivity.class);
//			}
			intent = new Intent(WelcomeActivity.this, MainActivity.class);
			preferences.edit().putInt("id", id).commit();
			preferences.edit().putInt("count", count + 1).commit();
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
			finish();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.welcome, menu);
//		return true;
//	}

}
