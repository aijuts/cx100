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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends Activity {
	
	private ImageView ivRight;
	private ProgressBar pbWait;
	private ProgressDialog pdWait;
	private TextView tvTitle;
	private ImageView ivLeft;
	private EditText etUserName, etPassword;
	private Button btnLogin;
	private TextView tvForgotPassword;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	private GetWebServiceData getWebServiceData;
	private User user;
	private UserData userData;
	private Tool tool;
	private String username, password;
	private int to = 0;
	private Intent intent;
	private Bundle bundle;
	//-2表示访问webservice有异常，0，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_user_login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		getWebServiceData = new GetWebServiceData();
		user = new User();
		userData = new UserData();
		tool = new Tool(this);
		
		bundle = getIntent().getExtras();
		to = bundle.getInt("to");
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		ivRight = (ImageView) findViewById(R.id.ivRight);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
		
		tvTitle.setText("用户登录");
		ivLeft.setOnClickListener(new LeftClick());
		pbWait.setVisibility(View.GONE);
		
		ivRight.setImageResource(R.drawable.reg_button_style);
		ivRight.setVisibility(View.VISIBLE);
		ivRight.setOnClickListener(new RightClick());
		btnLogin.setOnClickListener(new LoginClick());
		tvForgotPassword.setVisibility(View.GONE);
		tvForgotPassword.setOnClickListener(new ForgotPasswordClick());
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
	
	class RightClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intent = new Intent(UserLoginActivity.this, UserRegActivity.class);
			startActivityForResult(intent, 1);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	class LoginClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
			}
			pdWait = new ProgressDialog(UserLoginActivity.this);
			pdWait.setMessage("用户正在登录...");
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
					getUserLogin();
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
					asyUserLogin();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(UserLoginActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
	public void asyUserLogin() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(UserLoginActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(UserLoginActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(UserLoginActivity.this, "用户名密码错误或该账号不存在！", Toast.LENGTH_SHORT).show();
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
			
			switch (to) {
			case 0:
				Toast.makeText(UserLoginActivity.this, "登录成功，欢迎进入用户中心！", Toast.LENGTH_SHORT).show();
				intent = new Intent(UserLoginActivity.this, UserCenterActivity.class);
				bundle = new Bundle();
				bundle.putInt("type", 0);
				bundle.putString("userid", userData.getUserid());
				intent.putExtras(bundle);
				startActivity(intent);
//				overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
				break;
			case 1:
				Toast.makeText(UserLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
				intent = new Intent(UserLoginActivity.this, HotelDetailActivity.class);
				setResult(RESULT_OK, intent);
//				overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
				break;
			}
			finish();
			break;
		case 3:
			Toast.makeText(UserLoginActivity.this, "用户名不能低于4位！", Toast.LENGTH_SHORT).show();
			break;
		case 4:
			Toast.makeText(UserLoginActivity.this, "密码不能低于6位！", Toast.LENGTH_SHORT).show();
			break;
		}
		pbWait.setVisibility(View.GONE);
		pdWait.dismiss();
		
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
		//得到InputMethodManager的实例
		if (imm.isActive()) {
			//如果开启
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS); 
			//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
		}
	}
	
	public void getUserLogin() throws Exception {
		username = etUserName.getText() + "";
		password = etPassword.getText() + "";
		if (username.trim().length() >= 4 && password.trim().length() >= 6) {
			user = getWebServiceData.getUserLogin(tool.encode(username), tool.md5(password.trim()));
			String success = user.getSuccess();
			if (success.equals("true")) {
				userData = user.getData();
				if (userData.getUserid().equals("")) {
					exceptionNo = 0;
				}else {
					exceptionNo = 1;
				}
			}else {
				exceptionNo = -1;
			}
		}else {
			if (username.trim().length() < 4) {
				exceptionNo = 3;
			}
			if (password.trim().length() < 6) {
				exceptionNo = 4;
			}
		}
	}
	
	class ForgotPasswordClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			switch (resultCode) {
			case RESULT_OK:
				bundle = data.getExtras();  
				String info = bundle.getString("info");
				Toast.makeText(UserLoginActivity.this, requestCode + "\n" + resultCode + "\n" +info, Toast.LENGTH_SHORT).show();
				break;
			case 2:
				
				break;
			}
			break;
		case 2:
			
			break;
		}
	}

}
