package com.aijuts.cx100;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.tsz.afinal.FinalDb;

import com.aijuts.cx100.HotelActivity.DistanceSelect;
import com.aijuts.cx100.NoticeActivity.AsyncTaskHelper;
import com.aijuts.cx100.NoticeActivity.ResNewsItemClick;
import com.aijuts.cx100.adapter.TagLvAdapter;
import com.aijuts.cx100.data.entity.UserLogin;
import com.aijuts.cx100.entity.Logeinfo;
import com.aijuts.cx100.entity.LogeinfoData;
import com.aijuts.cx100.entity.Member;
import com.aijuts.cx100.entity.MemberData;
import com.aijuts.cx100.entity.Order;
import com.aijuts.cx100.entity.OrderCommit;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.GetWebServiceData;
import com.aijuts.cx100.util.Tool;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class BookingActivity extends Activity {
	
	private TextView tvTitle;
	private ImageView ivLeft;
	private ProgressBar pbWait;
	private LinearLayout layBody;
	private ProgressDialog pdWait;
	private Button btnDate, btnTime, btnCommit;
	private EditText etNumber, etPhone, etName, etRemark;
	private RadioGroup radioGroup1;
	private RadioButton radio0, radio1;
	private Spinner spLocation;
	private Builder dialog;
	private DatePicker dp;
	private TimePicker tp;
	private DatePickerDialog datePickerDialog;
	private TimePickerDialog timePickerDialog;
	private int year, month, day, hour, minute, spnCount = 0, logeid = 0;
	private String date, time, number, phone, name, sexState = "0", remark;
	private boolean flag = true;
	private Tool tool;
	private GetWebServiceData getWebServiceData;
	private Member member;
	private MemberData memberData;
	private OrderCommit orderCommit;
	private Order order;
	private List<UserLogin> list_user;
	private List<String> list_location;
	private Logeinfo loge;
	private List<LogeinfoData> list_logeinfo;
	private UserLogin userLogin;
	private String asy, verInfo = "";;
	private AsyncTaskHelper asyncTaskHelper;
	//-2表示访问webservice有异常，0，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;
	private Intent intent;
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_booking);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		tool = new Tool(this);
		getWebServiceData = new GetWebServiceData();
		member = new Member();
		memberData = new MemberData();
		orderCommit = new OrderCommit();
		order = new Order();
		loge = new Logeinfo();
		list_logeinfo = new ArrayList<LogeinfoData>();
		list_location = new ArrayList<String>();
		list_user = new ArrayList<UserLogin>();
		userLogin = new UserLogin();
		
		list_location.add("大厅（人数不限）");
		if (savedInstanceState != null) {
			Constants.sellerid = savedInstanceState.getInt("sellerid");
		}
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		btnDate = (Button) findViewById(R.id.btnDate);
		btnTime = (Button) findViewById(R.id.btnTime);
		etNumber = (EditText) findViewById(R.id.etNumber);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etName = (EditText) findViewById(R.id.etName);
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		spLocation = (Spinner) findViewById(R.id.spLocation);
		etRemark = (EditText) findViewById(R.id.etRemark);
		btnCommit = (Button) findViewById(R.id.btnCommit);
		
		tvTitle.setText("餐厅预订");
		ivLeft.setOnClickListener(new LeftClick());
		pbWait.setVisibility(View.GONE);
		layBody.setVisibility(View.GONE);
		
		Time time = new Time();
		time.setToNow();
		year = time.year;
		month = time.month + 1;
		day = time.monthDay;
		hour = time.hour;
		minute = time.minute;
		
		SharedPreferences preferences = getSharedPreferences("userLogin", 0);
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
		
//		list_user = Constants.db.findAll(UserLogin.class);
//		userLogin = list_user.get(0);
		
		btnDate.setOnClickListener(new DateClick());
		btnTime.setOnClickListener(new TimeClick());
		radioGroup1.setOnCheckedChangeListener(new SexCheckedChange());
		btnCommit.setOnClickListener(new CommitClick());
		
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
				imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
			}
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
		outState.putInt("sellerid", Constants.sellerid);
	}

	class DateClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			dialog = new Builder(BookingActivity.this);
//			dialog.setTitle("请选择日期");
//			LayoutInflater inflater = LayoutInflater.from(BookingActivity.this);
//			View view = inflater.inflate(R.layout.dialog_date, null);
//			dp = (DatePicker) view.findViewById(R.id.dp);
//			dialog.setView(view);
//			dialog.setNegativeButton("确　　定", new NegativeClick());
//			dialog.show();
//			
//			dp.init(year, month - 1, day, new DateChanged());
			
			datePickerDialog = new DatePickerDialog(BookingActivity.this, new DateListener(), year, month - 1, day);
			datePickerDialog.show();
		}
		
		class DateListener implements OnDateSetListener{

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				BookingActivity.this.year = year;
				BookingActivity.this.month = monthOfYear + 1;
				BookingActivity.this.day = dayOfMonth;
				BookingActivity.this.btnDate.setText(year + "-" + month + "-" + day);
			}
			
		}
		
	}
	
	class TimeClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			dialog = new Builder(BookingActivity.this);
//			dialog.setTitle("请选择时间");
//			LayoutInflater inflater = LayoutInflater.from(BookingActivity.this);
//			View view = inflater.inflate(R.layout.dialog_time, null);
//			tp = (TimePicker) view.findViewById(R.id.tp);
//			dialog.setView(view);
//			dialog.setNegativeButton("确　　定", new NegativeClick());
//			dialog.show();
//			
//			System.out.println("hour " + hour);
//			tp.setIs24HourView(true);
//			tp.setCurrentHour(hour);
//			tp.setCurrentMinute(minute);
//			tp.setOnTimeChangedListener(new TimeChanged());
			
			timePickerDialog = new TimePickerDialog(BookingActivity.this, new TimeListener(), hour, minute, true);
			timePickerDialog.show();
		}
		
		class TimeListener implements OnTimeSetListener{

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				BookingActivity.this.hour = hourOfDay;
				BookingActivity.this.minute = minute;
				BookingActivity.this.btnTime.setText(hour + ":" + minute);
			}
			
		}
		
	}
	
	class SexCheckedChange implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.radio0:
				sexState = "0";
//				Toast.makeText(BookingActivity.this, "先生", Toast.LENGTH_SHORT).show();
				break;
			case R.id.radio1:
				sexState = "1";
//				Toast.makeText(BookingActivity.this, "女士", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	}
	
	class CommitClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
			}
			flag = verCommit();
			if (flag == true) {
				pdWait = new ProgressDialog(BookingActivity.this);
				pdWait.setMessage("正在订单提交...");
				pdWait.setCancelable(false);
				pdWait.show();
				asy = "1";
				asyncTaskHelper = new AsyncTaskHelper();
				asyncTaskHelper.execute(asy);
			}else {
				Toast.makeText(BookingActivity.this, verInfo, Toast.LENGTH_SHORT).show();
				flag = true;
			}
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
					getLocation();
					getMember();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = -2;
				}
			}
			if (asy.equals("1")) {
				try {
					Commit();
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
					asyBooking0();
				}
				if (asy.equals("1")) {
					asyBooking1();
				}
				pbWait.setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(BookingActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyBooking0() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(BookingActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(BookingActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			ArrayAdapter<String> disAdapter = new ArrayAdapter<String>(BookingActivity.this, R.layout.myspinner, list_location);
			disAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spLocation.setAdapter(disAdapter);
			spLocation.setOnItemSelectedListener(new LocationSelect());
//			spLocation.setSelection(sp_loc);
			
			etPhone.setText(memberData.getMobile());
			etName.setText(memberData.getRealname());
			break;
		}
		layBody.setVisibility(View.VISIBLE);
	}
	
	public void asyBooking1() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(BookingActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(BookingActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(BookingActivity.this, order.getCommitInfo(), Toast.LENGTH_SHORT).show();
			break;
		case 1:
			intent = new Intent(BookingActivity.this, HotelDetailActivity.class);
			intent.putExtra("info", order.getCommitInfo());
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
			break;
		}
		pdWait.dismiss();
	}
	
	class LeftClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
			}
			finish();
			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
		}
		
	}
	
	class LocationSelect implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (spnCount == 1) {
				if (arg2 == 0) {
					logeid = 0;
				}else {
					logeid = Integer.valueOf(list_logeinfo.get(arg2 - 1).getLogid());
				}
//				Toast.makeText(BookingActivity.this, list_location.get(arg2) + " " + logeid, Toast.LENGTH_SHORT).show();
			}
			spnCount = 1;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public boolean verCommit() {
		date = btnDate.getText() + "";
		time = btnTime.getText() + "";
		number = etNumber.getText() + "";
		phone = etPhone.getText() + "";
		name = etName.getText() + "";
		remark = etRemark.getText() + "";
		verInfo = "";
		if (date.trim().equals("选择日期")) {
			verInfo += "请选择日期\n";
//			Toast.makeText(BookingActivity.this, "请选择日期", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		if (time.trim().equals("选择时间")) {
			verInfo += "请选择时间\n";
//			Toast.makeText(BookingActivity.this, "请选择时间", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		if (number.trim().equals("")) {
			verInfo += "请输入就餐人数\n";
//			Toast.makeText(BookingActivity.this, "请输入就餐人数", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		if (phone.trim().equals("")) {
			verInfo += "请输入预订号码\n";
//			Toast.makeText(BookingActivity.this, "请输入预订号码", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		if (name.trim().equals("")) {
			verInfo += "请输入预订人姓名";
//			Toast.makeText(BookingActivity.this, "请输入预订人姓名", Toast.LENGTH_SHORT).show();
			flag = false;
		}
		return flag;
	}
	
	public void Commit() throws NumberFormatException, Exception {
		String reMark = tool.encode("remark:" + remark);
		orderCommit = getWebServiceData.getCommitOrder(date, time, Constants.sellerid, Integer.valueOf(userLogin.getUserid()), Integer.valueOf(number), logeid, 2, reMark);
		String success = orderCommit.getSuccess();
		if (success.equals("true")) {
			order = orderCommit.getData();
			switch (order.getCommitState()) {
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
	}
	
	public void getLocation() throws Exception {
		loge = getWebServiceData.getLogeinfoAll(Constants.sellerid);
		String success = loge.getSuccess();
		if (success.equals("true")) {
			list_logeinfo = loge.getData();
			for (int i = 0; i < list_logeinfo.size(); i++) {
				list_location.add(list_logeinfo.get(i).getName() + "（" + list_logeinfo.get(i).getCount() + " 人）");
			}
			exceptionNo = 1;
		}else {
			exceptionNo = -1;
		}
	}
	
	public void getMember() throws NumberFormatException, Exception {
		member = getWebServiceData.getMember(Integer.valueOf(userLogin.getUserid()));
		String success = member.getSuccess();
		if (success.equals("true")) {
			memberData = member.getData();
			exceptionNo = 1;
		}else {
			exceptionNo = -1;
		}
	}
	
}
