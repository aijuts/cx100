package com.aijuts.cx100;

import com.aijuts.cx100.data.entity.UserLogin;
import com.aijuts.cx100.entity.User;
import com.aijuts.cx100.entity.UserData;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.GetWebServiceData;
import com.aijuts.cx100.util.Tool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserRegActivity extends Activity {
	
	private ProgressBar pbWait;
	private ProgressDialog pdWait;
	private TextView tvTitle;
	private ImageView ivLeft;
	private EditText etUserName, etPassword, etRePassword;
	private Button btnReg;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	private GetWebServiceData getWebServiceData;
	private User user;
	private UserData userData;
	private Tool tool;
	private String username, password, rePassword;
	private Intent intent;
	private Bundle bundle;
	//-2表示访问webservice有异常，0，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_user_reg);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		getWebServiceData = new GetWebServiceData();
		user = new User();
		userData = new UserData();
		tool = new Tool(this);
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etRePassword = (EditText) findViewById(R.id.etRePassword);
		btnReg = (Button) findViewById(R.id.btnReg);
		
		tvTitle.setText("用户注册");
		ivLeft.setOnClickListener(new LeftClick());
		pbWait.setVisibility(View.GONE);
		
		btnReg.setOnClickListener(new RegClick());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//如果按下的是返回键，并且没有重复
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
			}
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
			return false;
		}
		return false;
	}
	
	class LeftClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
			}
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
		}
		
	}
	
	class RegClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
			}
			pdWait = new ProgressDialog(UserRegActivity.this);
			pdWait.setMessage("用户正在注册...");
			pdWait.setCancelable(false);
			pdWait.show();
			asy = "0";
			asyncTaskHelper = new AsyncTaskHelper();
			asyncTaskHelper.execute(asy);
		}
		
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
				try {
					getUserReg();
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
					asyUserReg();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(UserRegActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyUserReg() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(UserRegActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(UserRegActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(UserRegActivity.this, userData.getInfo(), Toast.LENGTH_SHORT).show();
			break;
		case 1:
//			UserLogin userLogin = new UserLogin();
//			userLogin.setUserid(userData.getUserid());
//			userLogin.setSiteid(userData.getSiteid());
//			userLogin.setAccount(userData.getAccount());
//			userLogin.setMailaccount(userData.getMailaccount());
//			userLogin.setMobileaccount(userData.getMobileaccount());
//			userLogin.setNameaccount(userData.getNameaccount());
//			userLogin.setPwd(userData.getPwd());
//			userLogin.setV_pwd(userData.getV_pwd());
//			userLogin.setType(userData.getType());
//			userLogin.setRegtime(userData.getRegtime());
//			userLogin.setName(userData.getName());
//			userLogin.setState(userData.getState());
//			userLogin.setIsdelete(userData.getIsdelete());
//			Constants.db.save(userLogin);
			
			SharedPreferences preferences = getSharedPreferences("userLogin", 0);
			preferences.edit().putString("userid", userData.getUserid()).commit();
			preferences.edit().putString("siteid", userData.getSiteid()).commit();
			preferences.edit().putString("account", userData.getAccount()).commit();
			preferences.edit().putString("mailaccount", userData.getMailaccount()).commit();
			preferences.edit().putString("mobileaccount", userData.getMobileaccount()).commit();
			preferences.edit().putString("nameaccount", userData.getNameaccount()).commit();
			preferences.edit().putString("pwd", userData.getPwd()).commit();
			preferences.edit().putString("v_pwd", userData.getV_pwd()).commit();
			preferences.edit().putString("type", userData.getType()).commit();
			preferences.edit().putString("regtime", userData.getRegtime()).commit();
			preferences.edit().putString("name", userData.getName()).commit();
			preferences.edit().putString("state", userData.getState()).commit();
			preferences.edit().putString("isdelete", userData.getIsdelete()).commit();
			
			Toast.makeText(UserRegActivity.this, userData.getInfo(), Toast.LENGTH_SHORT).show();
			intent = new Intent(UserRegActivity.this, UserCenterActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			bundle = new Bundle();
			bundle.putInt("type", 1);
			bundle.putString("userid", userData.getUserid());
			intent.putExtras(bundle);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
//			intent.putExtra("info", "这是测试数据");
//			setResult(RESULT_OK, intent);
			finish();
			break;
		case 2:
			Toast.makeText(UserRegActivity.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			Toast.makeText(UserRegActivity.this, "用户名不能低于4位！", Toast.LENGTH_SHORT).show();
			break;
		case 4:
			Toast.makeText(UserRegActivity.this, "密码不能低于6位！", Toast.LENGTH_SHORT).show();
			break;
		case 5:
			Toast.makeText(UserRegActivity.this, "确认密码不能低于6位！", Toast.LENGTH_SHORT).show();
			break;
		}
		pbWait.setVisibility(View.GONE);
		pdWait.dismiss();
	}
	
	public void getUserReg() throws Exception {
		username = etUserName.getText() + "";
		password = etPassword.getText() + "";
		rePassword = etRePassword.getText() + "";
		if (username.trim().length() >= 4 && password.trim().length() >= 6 && rePassword.trim().length() >= 6) {
			if (password.equals(rePassword)) {
				user = getWebServiceData.getUserReg(tool.encode(username), tool.md5(password.trim()));
				String success = user.getSuccess();
				if (success.equals("true")) {
					userData = user.getData();
					switch (userData.getIsReg()) {
					case 0:
						exceptionNo = 0;
						break;
					case 1:
						exceptionNo = 1;
						break;
					}
				}else {
					exceptionNo = -1;
				}
			}else {
				exceptionNo = 2;
			}
		}else {
			if (username.trim().length() < 4) {
				exceptionNo = 3;
			}
			if (password.trim().length() < 6) {
				exceptionNo = 4;
			}
			if (rePassword.trim().length() < 6) {
				exceptionNo = 5;
			}
		}
	}

}
