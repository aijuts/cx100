package com.aijuts.cx100.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class MainVpAdapter extends PagerAdapter {

	private Context context;
	private List<View> pageViews;
	
	public MainVpAdapter(Context context, List<View> pageViews) {
		this.context = context;
		this.pageViews = pageViews;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pageViews.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view.equals(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		((ViewPager) container).addView(pageViews.get(position));
		return pageViews.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		if (object instanceof View)
		{
			container.removeView((View) object);
		}
	}

//	@Override
//	public CharSequence getPageTitle(int position) {
//		// TODO Auto-generated method stub
//		String title = (String) list.get(position).get("title");
//		return title;
//	}

}
