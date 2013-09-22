package com.aijuts.cx100;

import com.aijuts.cx100.entity.Notice;
import com.aijuts.cx100.entity.NoticeDetail;
import com.aijuts.cx100.util.GetWebServiceData;
import com.aijuts.cx100.util.Tool;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NoticeDetailActivity extends Activity {
	
	private ProgressBar pbWait;
	private TextView tvTitle, tvNoticeTitle, tvNoticeDate;
	private ImageView ivLeft;
	private LinearLayout layBody;
	private WebView wvNotice;
	private WebSettings webSettings;
	private int id = 0;
	private String asy;
	private AsyncTaskHelper asyncTaskHelper;
	private GetWebServiceData getWebServiceData;
	private NoticeDetail noticeDetail;
	private Notice notice;
	private Intent intent;
	private Bundle bundle;
	//-2表示访问webservice有异常，0，-1表示返回数据有异常，1表示正常
	private int exceptionNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_notice_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		if (savedInstanceState != null) {
			id = savedInstanceState.getInt("id");
		}else {
			bundle = getIntent().getExtras();
			id = bundle.getInt("id");
		}
		
		getWebServiceData = new GetWebServiceData();
		noticeDetail = new NoticeDetail();
		notice = new Notice();
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		pbWait = (ProgressBar) findViewById(R.id.pbWait);
		layBody = (LinearLayout) findViewById(R.id.layBody);
		tvNoticeTitle = (TextView) findViewById(R.id.tvNoticeTitle);
		tvNoticeDate = (TextView) findViewById(R.id.tvNoticeDate);
		wvNotice = (WebView) findViewById(R.id.wvNotice);
		
		tvTitle.setText("餐厅动态");
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
					getNotice();
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
					asyNoticeDetail();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(NoticeDetailActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void asyNoticeDetail() {
		switch (exceptionNo) {
		case -2:
			Toast.makeText(NoticeDetailActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
			break;
		case -1:
			Toast.makeText(NoticeDetailActivity.this, "获取内容失败", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			tvNoticeTitle.setText(notice.getTitle());
			tvNoticeDate.setText(notice.getStime());
			
			webSettings = wvNotice.getSettings();
			webSettings.setJavaScriptEnabled(true);
			String html = "";
	        html += "<head><body>";
	        String noticeStr = notice.getContent();
			Tool tool = new Tool(NoticeDetailActivity.this);
			html += tool.replaceStr(noticeStr, "white-space:nowrap;", "word-break:break-all;");
//	        html += "<div style=\"word-break:break-all\">" + notice.getContent() + "</div>";
	        html += "</body></head>";
	        System.out.println(html);
			wvNotice.loadDataWithBaseURL("", html, "text/html", "utf-8", "");
			wvNotice.setWebViewClient(new NoticeWebViewClient());
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
	
	class NoticeWebViewClient extends WebViewClient{

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// TODO Auto-generated method stub
			
			return true;
		}
		
	}
	
	public void getNotice() throws Exception {
		noticeDetail = getWebServiceData.getNoticeDetail(id);
		String success = noticeDetail.getSuccess();
		if (success.equals("true")) {
			notice = noticeDetail.getData();
			exceptionNo = 1;
		}else {
			exceptionNo = -1;
		}
	}

}
