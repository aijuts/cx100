package com.aijuts.cx100;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.aijuts.cx100.HotelDetailActivity.AsyncTaskHelper;
import com.aijuts.cx100.entity.Member;
import com.aijuts.cx100.entity.MemberConsum;
import com.aijuts.cx100.entity.MemberCui;
import com.aijuts.cx100.entity.MemberData;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.GetWebServiceData;
import com.aijuts.cx100.util.Location;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AccountActivity extends Activity {
	
	private LinearLayout layBody;
	private FrameLayout flNickname, flPhone, flEmail, flSex, flRealName, flAddr, flAddress, flPro, 
		flBirthday, flQQ, flMSN, flMarriage, flBlood, flCuisines, flConsume;
	private TextView tvUser, tvDetail, tvNickname, tvPhone, tvEmail, tvSex, tvRealName, tvAddr, tvAddress, 
		tvPro, tvBirthday, tvQQ, tvMSN, tvMarriage, tvBlood, tvCuisines, tvConsume, tvNicknameValue, 
		tvPhoneValue, tvEmailValue, tvSexValue, tvRealNameValue, tvAddrValue, tvAddressValue, tvProValue, 
		tvBirthdayValue, tvQQValue, tvMSNValue, tvMarriageValue, tvBloodValue, tvCuisinesValue, 
		tvConsumeValue;
	private ProgressBar pbWait;
	private ProgressDialog pdWait;
	private TextView tvTitle;
	private ImageView ivLeft, ivUser, ivNickname;
	private FinalBitmap finalBitmap;
	private GetWebServiceData getWebServiceData;
	private Member member;
	private int userid;
	private String asy, cuisines = "", birthday = "";
	private AsyncTaskHelper asyncTaskHelper;
	private Location location;
	private MemberData memberData;
	private MemberConsum memberConsum;
	private Intent intent;
	private Bundle bundle;
	//-2表示访问webservice有异常，0，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_account);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		getWebServiceData = new GetWebServiceData();
		member = new Member();
		memberData = new MemberData();
		memberConsum = new MemberConsum();
		finalBitmap = FinalBitmap.create(this);
		location = (Location) getApplication();
		
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		ivUser = (ImageView) findViewById(R.id.ivUser);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		tvUser = (TextView) findViewById(R.id.tvUser);
		flNickname = (FrameLayout) findViewById(R.id.flNickname);
		tvNickname = (TextView) findViewById(R.id.tvNickname);
		tvNicknameValue = (TextView) findViewById(R.id.tvNicknameValue);
		ivNickname = (ImageView) findViewById(R.id.ivNickname);
		flPhone = (FrameLayout) findViewById(R.id.flPhone);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		tvPhoneValue = (TextView) findViewById(R.id.tvPhoneValue);
		flEmail = (FrameLayout) findViewById(R.id.flEmail);
		tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvEmailValue = (TextView) findViewById(R.id.tvEmailValue);
		flSex = (FrameLayout) findViewById(R.id.flSex);
		tvSex = (TextView) findViewById(R.id.tvSex);
		tvSexValue = (TextView) findViewById(R.id.tvSexValue);
		flRealName = (FrameLayout) findViewById(R.id.flRealName);
		tvRealName = (TextView) findViewById(R.id.tvRealName);
		tvRealNameValue = (TextView) findViewById(R.id.tvRealNameValue);
		tvDetail = (TextView) findViewById(R.id.tvDetail);
		flAddr = (FrameLayout) findViewById(R.id.flAddr);
		tvAddr = (TextView) findViewById(R.id.tvAddr);
		tvAddrValue = (TextView) findViewById(R.id.tvAddrValue);
		flAddress = (FrameLayout) findViewById(R.id.flAddress);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvAddressValue = (TextView) findViewById(R.id.tvAddressValue);
		flPro = (FrameLayout) findViewById(R.id.flPro);
		tvPro = (TextView) findViewById(R.id.tvPro);
		tvProValue = (TextView) findViewById(R.id.tvProValue);
		flBirthday = (FrameLayout) findViewById(R.id.flBirthday);
		tvBirthday = (TextView) findViewById(R.id.tvBirthday);
		tvBirthdayValue = (TextView) findViewById(R.id.tvBirthdayValue);
		flQQ = (FrameLayout) findViewById(R.id.flQQ);
		tvQQ = (TextView) findViewById(R.id.tvQQ);
		tvQQValue = (TextView) findViewById(R.id.tvQQValue);
		flMSN = (FrameLayout) findViewById(R.id.flMSN);
		tvMSN = (TextView) findViewById(R.id.tvMSN);
		tvMSNValue = (TextView) findViewById(R.id.tvMSNValue);
		flMarriage = (FrameLayout) findViewById(R.id.flMarriage);
		tvMarriage = (TextView) findViewById(R.id.tvMarriage);
		tvMarriageValue = (TextView) findViewById(R.id.tvMarriageValue);
		flBlood = (FrameLayout) findViewById(R.id.flBlood);
		tvBlood = (TextView) findViewById(R.id.tvBlood);
		tvBloodValue = (TextView) findViewById(R.id.tvBloodValue);
		flCuisines = (FrameLayout) findViewById(R.id.flCuisines);
		tvCuisines = (TextView) findViewById(R.id.tvCuisines);
		tvCuisinesValue = (TextView) findViewById(R.id.tvCuisinesValue);
		flConsume = (FrameLayout) findViewById(R.id.flConsume);
		tvConsume = (TextView) findViewById(R.id.tvConsume);
		tvConsumeValue = (TextView) findViewById(R.id.tvConsumeValue);
		
		ivLeft.setOnClickListener(new LeftClick());
		tvTitle.setText("账户设置");
		layBody.setVisibility(View.GONE);
		
		tvUser.setTypeface(Typeface.DEFAULT_BOLD);
		tvNickname.setTypeface(Typeface.DEFAULT_BOLD);
		tvPhone.setTypeface(Typeface.DEFAULT_BOLD);
		tvEmail.setTypeface(Typeface.DEFAULT_BOLD);
		tvSex.setTypeface(Typeface.DEFAULT_BOLD);
		tvRealName.setTypeface(Typeface.DEFAULT_BOLD);
		tvDetail.setTypeface(Typeface.DEFAULT_BOLD);
		tvAddr.setTypeface(Typeface.DEFAULT_BOLD);
		tvAddress.setTypeface(Typeface.DEFAULT_BOLD);
		tvPro.setTypeface(Typeface.DEFAULT_BOLD);
		tvBirthday.setTypeface(Typeface.DEFAULT_BOLD);
		tvQQ.setTypeface(Typeface.DEFAULT_BOLD);
		tvMSN.setTypeface(Typeface.DEFAULT_BOLD);
		tvMarriage.setTypeface(Typeface.DEFAULT_BOLD);
		tvBlood.setTypeface(Typeface.DEFAULT_BOLD);
		tvCuisines.setTypeface(Typeface.DEFAULT_BOLD);
		tvConsume.setTypeface(Typeface.DEFAULT_BOLD);
		
		if (savedInstanceState != null) {
			userid = savedInstanceState.getInt("id");
			pdWait = new ProgressDialog(AccountActivity.this);
			pdWait.setMessage("正在同步用户资料...");
			pdWait.setCancelable(false);
			pdWait.show();
			asy = "2";
			asyncTaskHelper = new AsyncTaskHelper();
			asyncTaskHelper.execute(asy);
		}else {
			asy = "0";
			asyncTaskHelper = new AsyncTaskHelper();
			asyncTaskHelper.execute(asy);
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
				Toast.makeText(AccountActivity.this, info, Toast.LENGTH_SHORT).show();
				pdWait = new ProgressDialog(AccountActivity.this);
				pdWait.setMessage("正在同步用户资料...");
				pdWait.setCancelable(false);
				pdWait.show();
				asy = "1";
				asyncTaskHelper = new AsyncTaskHelper();
				asyncTaskHelper.execute(asy);
				break;
			case 1:
				
				break;
			}
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//如果按下的是返回键，并且没有重复
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			location.setIsUpdateUserCenter(1);
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
		outState.putInt("id", Integer.valueOf(memberData.getUserid()));
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
				getMember();
			}
			if (asy.equals("1")) {
				try {
					getMemberData(Integer.valueOf(memberData.getUserid()));
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
			if (asy.equals("2")) {
				try {
					getMemberData(userid);
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
					asyAccount();
				}
				if (asy.equals("1")) {
					asyAccount2();
				}
				if (asy.equals("2")) {
					asyAccount2();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(AccountActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyAccount() {
		String image = "";
		if (memberData.getImage().equals("")) {
			image = "";
		}else {
			image = Constants.url_image + memberData.getImage();
		}
		finalBitmap.display(ivUser, image);
		
		String nickname = memberData.getName();
		if (!nickname.equals("")) {
			tvNicknameValue.setText(nickname);
		}
		
		String phone = memberData.getMobile().trim();
		if (!phone.equals("") || phone != null) {
			tvPhoneValue.setText(phone);
		}
		
		String email = memberData.getMail();
		if (!email.equals("")) {
			tvEmailValue.setText(email);
		}
		
		String sex = memberData.getSex();
		if (sex.equals("1")) {
			tvSexValue.setText("男");
		}else if (sex.equals("2")) {
			tvSexValue.setText("女");
		}
		
		String realname = memberData.getRealname();
		if (!realname.equals("")) {
			tvRealNameValue.setText(realname);
		}
		
		String ssq = memberData.getSsq();
		if (!ssq.equals("")) {
			tvAddrValue.setText(ssq);
		}
		
		String address = memberData.getAddr();
		if (!address.equals("")) {
			tvAddressValue.setText(address);
		}
		
		String pro = memberData.getPro();
		if (!pro.equals("")) {
			tvProValue.setText(pro);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String bir = "";
		try {
			bir = sdf.format(sdf.parse(memberData.getBirthday()));
			birthday = bir;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			Toast.makeText(AccountActivity.this, "您获得的生日格式不正确", Toast.LENGTH_SHORT).show();
			bir = "您的生日？";
			birthday = "";
		}
		tvBirthdayValue.setText(bir);
		
		String qq = memberData.getQq();
		if (!qq.equals("")) {
			tvQQValue.setText(qq);
		}
		
		String msn = memberData.getMsn();
		if (!msn.equals("")) {
			tvMSNValue.setText(msn);
		}
		
		String marriage = memberData.getMstate();
		if (marriage.equals("0")) {
			tvMarriageValue.setText("未婚");
		}else if (marriage.equals("1")) {
			tvMarriageValue.setText("已婚");
		}
		
		String blood = memberData.getBtype();
		if (blood.equals("1")) {
			tvBloodValue.setText("A型血");
		}else if (blood.equals("2")) {
			tvBloodValue.setText("B型血");
		}else if (blood.equals("3")) {
			tvBloodValue.setText("AB型血");
		}else if (blood.equals("4")) {
			tvBloodValue.setText("O型血");
		}
		
		List<MemberCui> list_member_cui = memberData.getCuisines();
		if (list_member_cui.size() != 0) {
			cuisines = "";
			for (int i = 0; i < list_member_cui.size(); i++) {
				if (i == list_member_cui.size() - 1) {
					cuisines += list_member_cui.get(i).getName();
				}else {
					cuisines += list_member_cui.get(i).getName() + "　|　";
				}
			}
			tvCuisinesValue.setText(cuisines);
		}
		
		memberConsum = memberData.getConsum();
		if (!memberConsum.getConsumid().equals("0")) {
			tvConsumeValue.setText(memberConsum.getName());
		}
		
//		ivUser.setOnClickListener(new UpdateClick(0));
		flNickname.setOnClickListener(new UpdateClick(1));
		flPhone.setOnClickListener(new UpdateClick(2));
		flEmail.setOnClickListener(new UpdateClick(3));
		flSex.setOnClickListener(new UpdateClick(4));
		flRealName.setOnClickListener(new UpdateClick(5));
		flAddr.setOnClickListener(new UpdateClick(6));
		flAddress.setOnClickListener(new UpdateClick(7));
		flPro.setOnClickListener(new UpdateClick(8));
		flBirthday.setOnClickListener(new UpdateClick(9));
		flQQ.setOnClickListener(new UpdateClick(10));
		flMSN.setOnClickListener(new UpdateClick(11));
		flMarriage.setOnClickListener(new UpdateClick(12));
		flBlood.setOnClickListener(new UpdateClick(13));
		flCuisines.setOnClickListener(new UpdateClick(14));
		flConsume.setOnClickListener(new UpdateClick(15));
		
		pbWait.setVisibility(View.GONE);
		layBody.setVisibility(View.VISIBLE);
	}
	
	public void asyAccount2() {
		switch (exceptionNo) {
		case -3:
			Toast.makeText(AccountActivity.this, "数据格式有异常", Toast.LENGTH_SHORT).show();
			break;
		case -2:
			Toast.makeText(AccountActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(AccountActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			asyAccount();
			break;
		}
		pdWait.dismiss();
	}
	
	public void getMember() {
		memberData = location.getMemberData();
	}
	
	public void getMemberData(int id) throws NumberFormatException, Exception {
		member = getWebServiceData.getMember(id);
		String success = member.getSuccess();
		if (success.equals("true")) {
			memberData = member.getData();
			location.setMemberData(memberData);
			exceptionNo = 1;
		}else {
			exceptionNo = -1;
		}
	}
	
	class LeftClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			location.setIsUpdateUserCenter(1);
			finish();
//			overridePendingTransition(R.anim.zoom_enter_2, R.anim.push_right_out);
		}
		
	}
	
	class UpdateClick implements OnClickListener{
		
		private int n;
		
		public UpdateClick(int n) {
			this.n = n;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intent = new Intent(AccountActivity.this, AccountUpdateActivity.class);
			location.setMbUpdateId(n);
			switch (n) {
			case 1:
				location.setMbUpdateInfo(memberData.getName());
				break;
			case 2:
				location.setMbUpdateInfo(memberData.getMobile().trim());
				break;
			case 3:
				location.setMbUpdateInfo(memberData.getMail());
				break;
			case 4:
				location.setMbUpdateInfo(memberData.getSex());
				break;
			case 5:
				location.setMbUpdateInfo(memberData.getRealname());
				break;
			case 6:
				
				break;
			case 7:
				location.setMbUpdateInfo(memberData.getAddr());
				break;
			case 8:
				location.setMbUpdateInfo(memberData.getPro());
				break;
			case 9:
				location.setMbUpdateInfo(birthday);
				break;
			case 10:
				location.setMbUpdateInfo(memberData.getQq());
				break;
			case 11:
				location.setMbUpdateInfo(memberData.getMsn());
				break;
			case 12:
				location.setMbUpdateInfo(memberData.getMstate());
				break;
			case 13:
				location.setMbUpdateInfo(memberData.getBtype());
				break;
			case 14:
				location.setMbUpdateInfo(cuisines.replaceAll("　\\|　", "\\|"));
				break;
			case 15:
				location.setMbUpdateInfo(memberConsum.getConsumid());
				break;
			}
			startActivityForResult(intent, 1);
//			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
		
	}

}
