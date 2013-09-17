package com.aijuts.cx100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aijuts.cx100.adapter.MainVpAdapter;
import com.aijuts.cx100.util.Constants;
import com.aijuts.cx100.util.Tool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuidePageActivity extends Activity {
	
	private ViewPager vpGuide;
	private ImageView ivHotel, imageView;
	private ImageView[] imageViews;
	private ViewGroup group;
	private Button btnEnter;
	private List<View> pageViews;
	private List<Map<String, Object>> list;
	private Map<String, Object> map;
	private MainVpAdapter mainPagerAdapter;
	private Intent intent;
	private Tool tool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guidepage);
		
		list = new ArrayList<Map<String,Object>>();
		
		vpGuide = (ViewPager) findViewById(R.id.vpGuide);
		group = (ViewGroup) findViewById(R.id.viewGroup);
		btnEnter = (Button) findViewById(R.id.btnEnter);
		
		list = getGuide();
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		for (int i = 0; i < list.size(); i++) {
			pageViews.add(inflater.inflate(R.layout.viewpager_item_hotel, null));
		}
		mainPagerAdapter = new MainVpAdapter(GuidePageActivity.this, pageViews);
		vpGuide.setAdapter(mainPagerAdapter);
		vpGuide.setOnPageChangeListener(new GuidePageChangeListener());
		
		for (int i = 0; i < list.size(); i++) {
			ivHotel = (ImageView) pageViews.get(i).findViewById(R.id.ivHotel);
			ivHotel.setImageResource((Integer) list.get(i).get("image"));
		}
		
		imageViews = new ImageView[list.size()];
		for (int i = 0; i < list.size(); i++) {
			imageView = new ImageView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			tool = new Tool(this);
            lp.setMargins(tool.dip2px(this, 8), 0, tool.dip2px(this, 8), 0);
            imageView.setLayoutParams(lp);
            imageViews[i] = imageView;
            if (i == 0) {
                //默认选中第一张图片
                imageViews[i].setBackgroundResource(R.drawable.guide_dot_white);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.guide_dot_black);
            }
            group.addView(imageViews[i]);
		}
		
		btnEnter.setVisibility(View.GONE);
		btnEnter.setOnClickListener(new EnterClick());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/** 指引页面改监听器 */
    class GuidePageChangeListener implements OnPageChangeListener {
    
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
    
        }
    
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
    
        }
    
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0].setBackgroundResource(R.drawable.guide_dot_white);
                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.drawable.guide_dot_black);
                }
            }
            if (arg0 == list.size() - 1) {
            	Handler handler = new Handler();
            	handler.postDelayed(runnable, 500);
			}else {
				btnEnter.setVisibility(View.GONE);
			}
        }
    
    }
    
    Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			btnEnter.setVisibility(View.VISIBLE);
		}
	};
    
    class EnterClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intent = new Intent(GuidePageActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit_2);
		}
    	
    }
	
	public List<Map<String, Object>> getGuide() {
		for (int i = 0; i < Constants.guide_image.length; i++) {
			map = new HashMap<String, Object>();
			map.put("image", Constants.guide_image[i]);
			list.add(map);
		}
		return list;
	}

}
