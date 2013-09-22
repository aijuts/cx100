package com.aijuts.cx100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.interfaces.PBEKey;

import org.w3c.dom.TypeInfo;

import com.aijuts.cx100.BookingActivity.SexCheckedChange;
import com.aijuts.cx100.BookingActivity.DateClick.DateListener;
import com.aijuts.cx100.adapter.CheckboxGvAdapter;
import com.aijuts.cx100.entity.CuisinesData;
import com.aijuts.cx100.entity.MemberData;
import com.aijuts.cx100.entity.MemberUpdate;
import com.aijuts.cx100.entity.MemberUpdateData;
import com.aijuts.cx100.entity.PerCapita;
import com.aijuts.cx100.entity.PerCapitaData;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.GetWebServiceData;
import com.aijuts.cx100.util.Location;
import com.aijuts.cx100.util.Tool;

import android.R.bool;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Type;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class AccountUpdateActivity extends Activity {
	
	private LinearLayout layLeft, layRight, layBody, llMember, laySex, layBlood;
	private TextView tvMember;
	private EditText etMember, etMemberEmail, etMemberBirthday;
	private RadioGroup radioGroup1, rgBlood;
	private RadioButton radioMale, radioFemale, rbBloodA, rbBloodB, rbBloodAB, rbBloodO;
	private DatePickerDialog datePickerDialog;
	private ProgressDialog pdWait;
	private GridView gvCuisines;
	private CheckboxGvAdapter checkboxGvAdapter;
	private Location location;
	private Tool tool;
	private GetWebServiceData getWebServiceData;
	private MemberData memberData;
	private MemberUpdate memberUpdate;
	private MemberUpdateData memberUpdateData;
	private List<Map<String, Object>> list_cuisines, list_per;
	private Map<String, Object> map;
	private List<String> list_lovecuisines_name;
	private String asy, info = "", exceptionInfo, updateInfo, loadingInfo = "初始化个人资料";
	private AsyncTaskHelper asyncTaskHelper;
	//-2表示访问webservice有异常，0，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;
	private Intent intent;
	private Bundle bundle;
	private String sexState = "1", mstate = "0", bloodState = "1";
	private int year, month, day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_account_update);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_commit);
		
		list_cuisines = new ArrayList<Map<String,Object>>();
		list_per = new ArrayList<Map<String,Object>>();
		list_lovecuisines_name = new ArrayList<String>();
		
		tool = new Tool(this);
		getWebServiceData = new GetWebServiceData();
		location = (Location) getApplication();
		memberData = location.getMemberData();
		
		if (savedInstanceState != null) {
			location.setMbUpdateInfo(savedInstanceState.getString("updateInfo"));
		}
		
		layLeft = (LinearLayout) findViewById(R.id.layLeft);
		layRight = (LinearLayout) findViewById(R.id.layRight);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		llMember = (LinearLayout) findViewById(R.id.llMember);
		tvMember = (TextView) findViewById(R.id.tvMember);
		etMember = (EditText) findViewById(R.id.etMember);
		etMemberEmail = (EditText) findViewById(R.id.etMemberEmail);
		etMemberBirthday = (EditText) findViewById(R.id.etMemberBirthday);
		laySex = (LinearLayout) findViewById(R.id.laySex);
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radioMale = (RadioButton) findViewById(R.id.radioMale);
		radioFemale = (RadioButton) findViewById(R.id.radioFemale);
		layBlood = (LinearLayout) findViewById(R.id.layBlood);
		rgBlood = (RadioGroup) findViewById(R.id.rgBlood);
		rbBloodA = (RadioButton) findViewById(R.id.rbBloodA);
		rbBloodB = (RadioButton) findViewById(R.id.rbBloodB);
		rbBloodAB = (RadioButton) findViewById(R.id.rbBloodAB);
		rbBloodO = (RadioButton) findViewById(R.id.rbBloodO);
		gvCuisines = (GridView) findViewById(R.id.GvCuisines);
		
		layBody.setVisibility(View.GONE);
		layLeft.setOnClickListener(new TitleClick(1));
		layRight.setOnClickListener(new TitleClick(2));
		
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
				imm.hideSoftInputFromWindow(etMember.getWindowToken(), 0);
			}
			location.setMbUpdateInfo("");
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
		outState.putString("updateInfo", updateInfo);
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
			pdWait = new ProgressDialog(AccountUpdateActivity.this);
			pdWait.setMessage(loadingInfo);
			pdWait.setCancelable(false);
			pdWait.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			asy = params[0];
			if (asy.equals("0")) {
				try {
					getMemberInfo();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = -2;
				}
			}
			if (asy.equals("1")) {
				try {
					updateMember();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					exceptionNo = -3;
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
					asyAccountUpdate0();
				}
				if (asy.equals("1")) {
					asyAccountUpdate1();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(AccountUpdateActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
			pdWait.dismiss();
		}
		
	}
	
	public void asyAccountUpdate0() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(AccountUpdateActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(AccountUpdateActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			initMember();
			break;
		}
		layBody.setVisibility(View.VISIBLE);
	}
	
	public void initMember() {
		switch (location.getMbUpdateId()) {
		case 1:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("用户昵称");
			etMember.setVisibility(View.VISIBLE);
			etMember.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
			etMember.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			etMember.setHorizontallyScrolling(false);
			etMember.setSingleLine(true);
			etMember.setText(updateInfo);
			break;
		case 2:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("手机");
			etMember.setVisibility(View.VISIBLE);
			etMember.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
			etMember.setInputType(InputType.TYPE_CLASS_PHONE);
			etMember.setHorizontallyScrolling(false);
			etMember.setSingleLine(true);
			etMember.setText(updateInfo);
			break;
		case 3:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("E-mail");
			etMemberEmail.setVisibility(View.VISIBLE);
//			etMemberEmail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
//			etMemberEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			etMemberEmail.setHorizontallyScrolling(false);
			etMemberEmail.setSingleLine(true);
			etMemberEmail.setText(updateInfo);
			break;
		case 4:
			radioGroup1.setOnCheckedChangeListener(new SexCheckedChange());
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("性别");
			laySex.setVisibility(View.VISIBLE);
			if (updateInfo.equals("1")) {
				radioMale.setChecked(true);
			}
			if (updateInfo.equals("2")) {
				radioFemale.setChecked(true);
			}
			break;
		case 5:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("真实姓名");
			etMember.setVisibility(View.VISIBLE);
			etMember.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
			etMember.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			etMember.setHorizontallyScrolling(false);
			etMember.setSingleLine(true);
			etMember.setText(updateInfo);
			break;
		case 6:
			
			break;
		case 7:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("详细地址");
			etMember.setVisibility(View.VISIBLE);
			etMember.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			etMember.setHorizontallyScrolling(false);
			etMember.setSingleLine(true);
			etMember.setText(updateInfo);
			break;
		case 8:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("职业");
			etMember.setVisibility(View.VISIBLE);
			etMember.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
			etMember.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			etMember.setHorizontallyScrolling(false);
			etMember.setSingleLine(true);
			etMember.setText(updateInfo);
			break;
		case 9:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("生日");
			etMemberBirthday.setVisibility(View.VISIBLE);
//			etMemberBirthday.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
//			etMemberBirthday.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			etMemberBirthday.setHorizontallyScrolling(false);
			etMemberBirthday.setSingleLine(true);
			etMemberBirthday.setText(updateInfo);
			etMemberBirthday.setOnClickListener(new BirthDayClick());
			break;
		case 10:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("QQ");
			etMember.setVisibility(View.VISIBLE);
			etMember.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
			etMember.setInputType(InputType.TYPE_CLASS_NUMBER);
			etMember.setHorizontallyScrolling(false);
			etMember.setSingleLine(true);
			etMember.setText(updateInfo);
			break;
		case 11:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("MSN");
			etMemberEmail.setVisibility(View.VISIBLE);
//			etMemberEmail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
//			etMemberEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			etMemberEmail.setHorizontallyScrolling(false);
			etMemberEmail.setSingleLine(true);
			etMemberEmail.setText(updateInfo);
			break;
		case 12:
			radioGroup1.setOnCheckedChangeListener(new MarriageChange());
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("婚姻状态");
			laySex.setVisibility(View.VISIBLE);
			radioMale.setText("　未　婚");
			radioFemale.setText("　已　婚");
			if (updateInfo.equals("0")) {
				radioMale.setChecked(true);
			}
			if (updateInfo.equals("1")) {
				radioFemale.setChecked(true);
			}
			break;
		case 13:
			rgBlood.setOnCheckedChangeListener(new BloodChange());
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("血型");
			layBlood.setVisibility(View.VISIBLE);
			if (updateInfo.equals("1")) {
				rbBloodA.setChecked(true);
			}
			if (updateInfo.equals("2")) {
				rbBloodB.setChecked(true);
			}
			if (updateInfo.equals("3")) {
				rbBloodAB.setChecked(true);
			}
			if (updateInfo.equals("4")) {
				rbBloodO.setChecked(true);
			}
			break;
		case 14:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("您喜欢的菜系");
			gvCuisines.setSelector(new ColorDrawable(Color.TRANSPARENT));
			gvCuisines.setVisibility(View.VISIBLE);
			checkboxGvAdapter = new CheckboxGvAdapter(AccountUpdateActivity.this, list_cuisines);
			gvCuisines.setAdapter(checkboxGvAdapter);
			gvCuisines.setOnItemClickListener(new CuisinesItemClick());
			break;
		case 15:
			tvMember.setTypeface(Typeface.DEFAULT_BOLD);
			tvMember.setText("消费水平");
			gvCuisines.setSelector(new ColorDrawable(Color.TRANSPARENT));
			gvCuisines.setVisibility(View.VISIBLE);
			checkboxGvAdapter = new CheckboxGvAdapter(AccountUpdateActivity.this, list_per);
			gvCuisines.setAdapter(checkboxGvAdapter);
			gvCuisines.setOnItemClickListener(new PerItemClick());
			break;
		}
	}
	
	public void asyAccountUpdate1() {
		switch (exceptionNo) {
		case -3:
			Toast.makeText(AccountUpdateActivity.this, "数据格式有异常", Toast.LENGTH_SHORT).show();
			break;
		case -2:
			Toast.makeText(AccountUpdateActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(AccountUpdateActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(AccountUpdateActivity.this, exceptionInfo, Toast.LENGTH_SHORT).show();
			break;
		case 1:
			intent = new Intent(AccountUpdateActivity.this, AccountActivity.class);
			intent.putExtra("info", memberUpdateData.getUpdateInfo());
			setResult(RESULT_OK, intent);
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
			break;
		case 2:
			Toast.makeText(AccountUpdateActivity.this, memberUpdateData.getUpdateInfo(), Toast.LENGTH_SHORT).show();
			break;
		case 3:
			intent = new Intent(AccountUpdateActivity.this, AccountActivity.class);
			setResult(1, intent);
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
			break;
		}
	}
	
	public void getMemberInfo() throws Exception {
		updateInfo = location.getMbUpdateInfo();
		System.out.println(updateInfo);
		switch (location.getMbUpdateId()) {
		case 1:
			
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		case 4:
			
			break;
		case 5:
			
			break;
		case 6:
			
			break;
		case 7:
			
			break;
		case 8:
			
			break;
		case 9:
			Time time = new Time();
			time.setToNow();
			year = time.year;
			month = time.month + 1;
			day = time.monthDay;
			break;
		case 10:
			
			break;
		case 11:
			
			break;
		case 12:
			
			break;
		case 13:
			
			break;
		case 14:
			if (!updateInfo.equals("")) {
				String[] cuisines = updateInfo.split("\\|");
				for (int i = 0; i < cuisines.length; i++) {
					list_lovecuisines_name.add(cuisines[i]);
				}
			}
			getCuisines();
			break;
		case 15:
			getConsum();
			break;
		}
	}
	
	public void updateMember() throws NumberFormatException, Exception {
		switch (location.getMbUpdateId()) {
		case 1:
			info = etMember.getText() + "";
			if (info.trim().equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请输入您的用户昵称";
			}else {
				if (info.equals(updateInfo)) {
					exceptionNo = 3;
				}else {
					doUpdate("name");
				}
			}
			break;
		case 2:
			info = etMember.getText() + "";
			if (info.trim().equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请输入您的手机号码";
			}else {
//				boolean flag = tool.checkPhone(info);
				if (info.length() == 11) {
					if (info.equals(updateInfo)) {
						exceptionNo = 3;
					}else {
						doUpdate("mobile");
					}
				}else {
					exceptionNo = 0;
					exceptionInfo = "您的手机号码格式不正确";
				}
			}
			break;
		case 3:
			info = etMemberEmail.getText() + "";
			if (info.trim().equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请输入您的邮箱地址";
			}else {
				boolean flag = tool.checkEmail(info);
				if (flag == true) {
					if (info.equals(updateInfo)) {
						exceptionNo = 3;
					}else {
						doUpdate("mail");
					}
				}else {
					exceptionNo = 0;
					exceptionInfo = "您的邮箱格式不正确";
				}
			}
			break;
		case 4:
			info = sexState;
			if (info.equals(updateInfo)) {
				exceptionNo = 3;
			}else {
				doUpdate("sex");
			}
			break;
		case 5:
			info = etMember.getText() + "";
			if (info.trim().equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请输入您的真实姓名";
			}else {
				if (info.equals(updateInfo)) {
					exceptionNo = 3;
				}else {
					doUpdate("realname");
				}
			}
			break;
		case 6:
			
			break;
		case 7:
			info = etMember.getText() + "";
			if (info.trim().equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请输入您的详细地址";
			}else {
				if (info.equals(updateInfo)) {
					exceptionNo = 3;
				}else {
					doUpdate("addr");
				}
			}
			break;
		case 8:
			info = etMember.getText() + "";
			if (info.trim().equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请输入您的职业";
			}else {
				if (info.equals(updateInfo)) {
					exceptionNo = 3;
				}else {
					doUpdate("pro");
				}
			}
			break;
		case 9:
			info = etMemberBirthday.getText() + "";
			if (info.trim().equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请输入您的生日";
			}else {
				if (info.equals(updateInfo)) {
					exceptionNo = 3;
				}else {
					doUpdate("birthday");
				}
			}
			break;
		case 10:
			info = etMember.getText() + "";
			if (info.trim().equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请输入您的QQ账号";
			}else {
				if (info.equals(updateInfo)) {
					exceptionNo = 3;
				}else {
					doUpdate("qq");
				}
			}
			break;
		case 11:
			info = etMemberEmail.getText() + "";
			if (info.trim().equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请输入您的MSN账号";
			}else {
				if (info.equals(updateInfo)) {
					exceptionNo = 3;
				}else {
					doUpdate("msn");
				}
			}
			break;
		case 12:
			info = mstate;
			if (info.equals(updateInfo)) {
				exceptionNo = 3;
			}else {
				doUpdate("mstate");
			}
			break;
		case 13:
			info = bloodState;
			if (info.equals(updateInfo)) {
				exceptionNo = 3;
			}else {
				doUpdate("btype");
			}
			break;
		case 14:
			int isChecked;
			String string = "";
			for (int i = 0; i < list_cuisines.size(); i++) {
				isChecked = (Integer) list_cuisines.get(i).get("isCheck");
				if (isChecked == 2) {
					string += list_cuisines.get(i).get("id") + ";";
				}
			}
			if (!string.equals("")) {
				info = string.substring(0, string.length() - 1);
			}
			if (info.equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请至少选择一个您喜欢的菜系";
			}else {
				doUpdate("vid");
			}
			break;
		case 15:
			if (info.equals("")) {
				exceptionNo = 0;
				exceptionInfo = "请选择您的消费水平";
			}else {
				doUpdate("consumid");
			}
			break;
		}
	}
	
	public void doUpdate(String key) throws NumberFormatException, Exception {
		memberUpdate = getWebServiceData.getUpdateMember(Integer.valueOf(memberData.getUserid()), key, tool.encode(info));
		String success = memberUpdate.getSuccess();
		if (success.equals("true")) {
			memberUpdateData = memberUpdate.getData();
			switch (memberUpdateData.getUpdateState()) {
			case 0:
				exceptionNo = 2;
				break;
			case 1:
				exceptionNo = 1;
				break;
			}
		}else {
			exceptionNo = -1;
		}
	}
	
	class TitleClick implements OnClickListener{
		
		private int n;
		
		public TitleClick(int n) {
			this.n = n;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(etMember.getWindowToken(), 0);
			}
			location.setMbUpdateInfo("");
			switch (n) {
			case 1:
				finish();
//				overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
				break;
			case 2:
				loadingInfo = "正在更新个人资料";
				asy = "1";
				asyncTaskHelper = new AsyncTaskHelper();
				asyncTaskHelper.execute(asy);
				break;
			}
		}
		
	}
	
	class SexCheckedChange implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.radioMale:
				sexState = "1";
//				Toast.makeText(BookingActivity.this, "先生", Toast.LENGTH_SHORT).show();
				break;
			case R.id.radioFemale:
				sexState = "2";
//				Toast.makeText(BookingActivity.this, "女士", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	}
	
	class MarriageChange implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.radioMale:
				mstate = "0";
				break;
			case R.id.radioFemale:
				mstate = "1";
				break;
			}
		}
		
	}
	
	class BloodChange implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.rbBloodA:
				bloodState = "1";
				break;
			case R.id.rbBloodB:
				bloodState = "2";
				break;
			case R.id.rbBloodAB:
				bloodState = "3";
				break;
			case R.id.rbBloodO:
				bloodState = "4";
				break;
			}
		}
		
	}
	
	class BirthDayClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			datePickerDialog = new DatePickerDialog(AccountUpdateActivity.this, new DateListener(), year, month - 1, day);
			datePickerDialog.show();
		}
		
	}
	
	class DateListener implements OnDateSetListener{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			AccountUpdateActivity.this.year = year;
			AccountUpdateActivity.this.month = monthOfYear + 1;
			AccountUpdateActivity.this.day = dayOfMonth;
			AccountUpdateActivity.this.etMemberBirthday.setText(year + "-" + month + "-" + day);
		}
		
	}
	
	class CuisinesItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			map = list_cuisines.get(arg2);
			CheckBox checkBox = (CheckBox) arg1.findViewById(R.id.cbItem);
			boolean isChecked = checkBox.isChecked();
			if (isChecked == true) {
				checkBox.setChecked(false);
				map.put("isCheck", 1);
			}else {
				checkBox.setChecked(true);
				map.put("isCheck", 2);
			}
			list_cuisines.set(arg2, map);
		}
		
	}
	
	class PerItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
//			CheckBox checkBox = (CheckBox) arg1.findViewById(R.id.cbItem);
			for (int i = 0; i < list_per.size(); i++) {
				map = list_per.get(i);
				if (i == arg2) {
//					checkBox.setChecked(true);
					map.put("isCheck", 2);
				}else {
//					checkBox.setChecked(false);
					map.put("isCheck", 1);
				}
				list_per.set(i, map);
			}
			for (int i = 0; i < list_per.size(); i++) {
				System.out.println("isCheck:" + list_per.get(i).get("isCheck"));
			}
			info = (String) list_per.get(arg2).get("id");
			checkboxGvAdapter.notifyDataSetChanged();
		}
		
	}
	
	public void getCuisines() throws Exception{
//		for (int i = 0; i < 10; i++) {
//			map = new HashMap<String, Object>();
//			map.put("id", 1);
//			map.put("title", "菜系" + i);
//			list_cuisines.add(map);
//		}
		
		if (Constants.cuisines == null) {
			Constants.cuisines = getWebServiceData.getCuisinesAll();
		}
		String success = Constants.cuisines.getSuccess();
		if (success.equals("true")) {
			List<CuisinesData> listCuisines = Constants.cuisines.getData();
			for (int i = 0; i < listCuisines.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", listCuisines.get(i).getVid());
				String name = listCuisines.get(i).getName();
				map.put("title", name);
				int isCheck = 1;
				if (!updateInfo.equals("")) {
					for (int j = 0; j < list_lovecuisines_name.size(); j++) {
						if (list_lovecuisines_name.get(j).equals(name)) {
							isCheck = 2;
							break;
						}
					}
				}
				map.put("isCheck", isCheck);
				list_cuisines.add(map);
			}
			exceptionNo = 1;
		}else {
			Constants.cuisines = null;
			exceptionNo = -1;
		}
	}
	
	public void getConsum() throws Exception {
		if (Constants.perCapita == null) {
			Constants.perCapita = getWebServiceData.getPerCapitaAll();
		}
		String success = Constants.perCapita.getSuccess();
		if (success.equals("true")) {
			List<PerCapitaData> listPer = Constants.perCapita.getData();
			for (int i = 0; i < listPer.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", listPer.get(i).getConsumid());
				map.put("title", listPer.get(i).getName());
				int isCheck = 1;
				if (updateInfo.equals(listPer.get(i).getConsumid())) {
					isCheck = 2;
				}
				map.put("isCheck", isCheck);
				list_per.add(map);
			}
			exceptionNo = 1;
		}else {
			Constants.perCapita = null;
			exceptionNo = -1;
		}
	}

}
