package com.aijuts.cx100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import com.aijuts.cx100.adapter.MainGvTitleAdapter;
import com.aijuts.cx100.adapter.UserCenterGvTitleAdapter;
import com.aijuts.cx100.data.entity.UserLogin;
import com.aijuts.cx100.entity.Member;
import com.aijuts.cx100.entity.MemberData;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.GetWebServiceData;
import com.aijuts.cx100.util.Location;
import com.aijuts.cx100.util.Tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserCenterActivity extends Activity {
	
	private ProgressBar pbWait;
	private ProgressDialog pdWait;
	private TextView tvTitle, tvOrder, tvCollection, tvIntegration, tvReview, tvUserName;
	private ImageView ivLeft, ivUserBg, ivUser;
	private LinearLayout layBody;
	private GridView gvUserCenter;
	private List<Map<String, Object>> list;
	private List<UserLogin> list_user;
	private Map<String, Object> map;
//	private UserLogin userLogin;
	private String asy, userid;
	private AsyncTaskHelper asyncTaskHelper;
	private Tool tool;
	private FinalBitmap finalBitmap;
	private Location location;
	private UserCenterGvTitleAdapter gvTitleAdapter;
	private int type = 0;
	private GetWebServiceData getWebServiceData;
	private Member member;
	private MemberData memberData;
	private Intent intent;
	private Bundle bundle;
	//-2表示访问webservice有异常，0，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_user_center);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		list = new ArrayList<Map<String,Object>>();
		list_user = new ArrayList<UserLogin>();
		tool = new Tool(this);
		getWebServiceData = new GetWebServiceData();
		member = new Member();
		memberData = new MemberData();
		finalBitmap = FinalBitmap.create(this);
		location = (Location) getApplication();
		
		if (savedInstanceState != null) {
			type = savedInstanceState.getInt("type");
			userid = savedInstanceState.getString("userid");
		}else {
			bundle = getIntent().getExtras();
			type = bundle.getInt("type");
			userid = bundle.getString("userid");
		}
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		ivUserBg = (ImageView) findViewById(R.id.ivUserBg);
		ivUser = (ImageView) findViewById(R.id.ivUser);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvOrder = (TextView) findViewById(R.id.tvOrder);
		tvCollection = (TextView) findViewById(R.id.tvCollection);
		tvIntegration = (TextView) findViewById(R.id.tvIntegration);
		tvReview = (TextView) findViewById(R.id.tvReview);
		gvUserCenter = (GridView) findViewById(R.id.gvUserCenter);
		
		tvTitle.setText("个人中心");
		ivLeft.setOnClickListener(new LeftClick());
		layBody.setVisibility(View.GONE);
		
		asy = "0";
		asyncTaskHelper = new AsyncTaskHelper();
		asyncTaskHelper.execute(asy);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int isUpdate = location.getIsUpdateUserCenter();
		if (isUpdate == 1) {
			pdWait = new ProgressDialog(UserCenterActivity.this);
			pdWait.setMessage("正在同步个人中心...");
			pdWait.setCancelable(false);
			pdWait.show();
			asy = "1";
			asyncTaskHelper = new AsyncTaskHelper();
			asyncTaskHelper.execute(asy);
		}
		location.setIsUpdateUserCenter(0);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("type", type);
		outState.putString("userid", userid);
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
				list = getUserCenter();
//				try {
//					getUser();
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//					exceptionNo = -3;
//				}
				try {
					getMember();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = -2;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = -2;
				}
			}
			if (asy.equals("1")) {
				getMemberData();
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
					asyUserCenter();
				}
				if (asy.equals("1")) {
					asyUserCenter();
					pdWait.dismiss();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(UserCenterActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyUserCenter() {
		switch (exceptionNo) {
		case -3:
			Toast.makeText(UserCenterActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
			break;
		case -2:
			Toast.makeText(UserCenterActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(UserCenterActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			ivUserBg.setImageResource(R.drawable.userbg);
			
			String image = "";
			if (memberData.getImage().equals("")) {
				image = "";
			}else {
				image = Constants.url_image + memberData.getImage();
			}
			finalBitmap.display(ivUser, image);
			
			tvUserName.setText(memberData.getName());
			
			tvOrder.setText("普通订单：" + memberData.getGeneralOrderCount() + "\n外卖订单：" + memberData.getTakeoutOrderCount());
			tvCollection.setText("餐厅收藏：" + memberData.getFavoriteSellerCount() + "\n菜品收藏：" + memberData.getFavoriteDishCount());
			tvIntegration.setText("当前积分：" + memberData.getUsablevantages());
			tvReview.setText("我的点评：" + memberData.getReviewCount());
			
			gvUserCenter.setSelector(new ColorDrawable(Color.TRANSPARENT));
			gvTitleAdapter = new UserCenterGvTitleAdapter(UserCenterActivity.this, list);
			gvUserCenter.setAdapter(gvTitleAdapter);
			gvUserCenter.setOnItemClickListener(new UserCenterItemClick());
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
	
	class UserCenterItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			switch (arg2) {
			case 0:
				intent = new Intent(UserCenterActivity.this, AccountActivity.class);
				startActivity(intent);
//				Toast.makeText(UserCenterActivity.this, "即将开放", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(UserCenterActivity.this, "即将开放", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(UserCenterActivity.this, "即将开放", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(UserCenterActivity.this, "即将开放", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(UserCenterActivity.this, "即将开放", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(UserCenterActivity.this, "即将开放", Toast.LENGTH_SHORT).show();
				break;
			case 6:
				Toast.makeText(UserCenterActivity.this, "即将开放", Toast.LENGTH_SHORT).show();
				break;
			case 7:
				Toast.makeText(UserCenterActivity.this, "即将开放", Toast.LENGTH_SHORT).show();
				break;
			}
			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}
	
	public List<Map<String, Object>> getUserCenter() {
		for (int i = 0; i < Constants.userCenter.length; i++) {
			map = new HashMap<String, Object>();
			map.put("title", Constants.userCenter[i]);
			map.put("image", Constants.userCenter_image[i]);
			list.add(map);
		}
		return list;
	}
	
//	public void getUser() throws Exception {
////		list_user = Constants.db.findAllByWhere(UserLogin.class, "userid = '" + userid + "'");
////		userLogin = list_user.get(0);
//		
//		SharedPreferences preferences = getSharedPreferences("userLogin", 0);
//		userLogin.setUserid(preferences.getString("userid", ""));
//		userLogin.setSiteid(preferences.getString("siteid", ""));
//		userLogin.setAccount(preferences.getString("account", ""));
//		userLogin.setMailaccount(preferences.getString("mailaccount", ""));
//		userLogin.setMobileaccount(preferences.getString("mobileaccount", ""));
//		userLogin.setNameaccount(preferences.getString("nameaccount", ""));
//		userLogin.setPwd(preferences.getString("pwd", ""));
//		userLogin.setV_pwd(preferences.getString("v_pwd", ""));
//		userLogin.setType(preferences.getString("type", ""));
//		userLogin.setRegtime(preferences.getString("regtime", ""));
//		userLogin.setName(preferences.getString("name", ""));
//		userLogin.setState(preferences.getString("state", ""));
//		userLogin.setIsdelete(preferences.getString("isdelete", ""));
//	}
	
	public void getMember() throws NumberFormatException, Exception {
		member = getWebServiceData.getMember(Integer.valueOf(userid));
		String success = member.getSuccess();
		if (success.equals("true")) {
			memberData = member.getData();
			Location location = (Location) getApplication();
			location.setMemberData(memberData);
			exceptionNo = 1;
		}else {
			exceptionNo = -1;
		}
	}
	
	public void getMemberData() {
		memberData = location.getMemberData();
		exceptionNo = 1;
	}
	
	public void exit() {
		System.out.println("type: " + type);
		switch (type) {
		case 0:
			finish();
			break;
		case 1:
			intent = new Intent(UserCenterActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
		overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
	}
	
	public void exitLogin() {
		// 确认对话框
		final AlertDialog isExit = new AlertDialog.Builder(this).create();
		// 对话框标题
		isExit.setTitle("退出登录？");
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
//		    		Constants.db.deleteByWhere(UserLogin.class, "userid = '" + userid + "'");
		    		
		    		SharedPreferences preferences = getSharedPreferences("userLogin", 0);
					preferences.edit().putString("userid", "").commit();
					preferences.edit().putString("siteid", "").commit();
					preferences.edit().putString("account", "").commit();
					preferences.edit().putString("mailaccount", "").commit();
					preferences.edit().putString("mobileaccount", "").commit();
					preferences.edit().putString("nameaccount", "").commit();
					preferences.edit().putString("pwd", "").commit();
					preferences.edit().putString("v_pwd", "").commit();
					preferences.edit().putString("type", "").commit();
					preferences.edit().putString("regtime", "").commit();
					preferences.edit().putString("name", "").commit();
					preferences.edit().putString("state", "").commit();
					preferences.edit().putString("isdelete", "").commit();
					
		    		exit();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(1, 1, 1, R.string.action_settings_back);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:
			exitLogin();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exit();
			return false;
		}
		return false;
	}

}
