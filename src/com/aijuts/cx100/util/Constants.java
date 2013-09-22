package com.aijuts.cx100.util;

import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalDb;

import com.aijuts.cx100.R;
import com.aijuts.cx100.entity.Businesszone;
import com.aijuts.cx100.entity.Cuisines;
import com.aijuts.cx100.entity.Member;
import com.aijuts.cx100.entity.PerCapita;
import com.aijuts.cx100.entity.PopularSeller;
import com.aijuts.cx100.entity.Purpose;
import com.aijuts.cx100.entity.Railway;
import com.aijuts.cx100.entity.SellerDetail;
import com.aijuts.cx100.entity.Zone;

public class Constants {
	
	public static String[] menu = {
		"����", "����", "��������"
	};
	
	public static int[] menu_image = {
		R.drawable.icon_hotel, R.drawable.icon_search, R.drawable.icon_user
	};
	
	public static String[] hotel_image = {
		"http://www.cx100.cn/Content/upload/00/00/00/27.jpg",
		"http://www.cx100.cn/Content/upload/00/00/00/24.jpg",
		"http://www.cx100.cn/Content/upload/00/00/00/3E.jpg"
	};
	
	public static int[] guide_image = {
		R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3,
		R.drawable.guide_5, R.drawable.guide_6
	};
	
	public static String[] Cuisines = {
		"С�Կ��", "���ݽ���", "����", "����", "���"
	};
	
	public static String[] Purpose = {
		"����Լ��", "��������", "����ͬ�¾۲�", "��ͥ�ۻ�", "��ɫ��ζ", "��ɫ�龳", "����Ǣ̸", "�̻�۲�", 
		"���ѻ�۲�", "������", "����С�", "���Գ�", "���жȼ�", "�������", "������", "����", "����", 
		"������", "��������", "�����", "����"
	};
	
	public static String[] percapita = {
		"0-50", "50-100", "100-150", "150-200", "200-250", "250-300", "300-350", "350-400", "400-450", 
		"450-500", "500����"
	};
	
	public static String[] AdminArea = {
		"������", "��ҵ԰��", "������", "������", "ƽ����"
	};
	
	public static String[] Circle = {
		"����", "����", "ʮȫ��/��˽�", "�⽭��", "��������ѧ��"
	};
	
	public static String[] Subway = {
		"����һ��"
	};
	
	public static String[] userCenter = {
		"�˻�����", "�ҵĶ���", "�ҵ��ղ�", "�ҵĻ���", "��������", "�˻�άȨ", "��������", "���ľ���"
	};
	
	public static int[] userCenter_image = {
		R.drawable.user_title_1, R.drawable.user_title_2, R.drawable.user_title_3, R.drawable.user_title_4,
		R.drawable.user_title_5, R.drawable.user_title_6, R.drawable.user_title_7, R.drawable.user_title_8
	};
	
	public static double latitude = 10000;
	
	public static double longitude = 10000;
	
	public static String address;
	
	public static FinalDb db;
	
	public static String APP_ID = "wxda9f9664a75a7b10";
	
//	public static String url_data = "http://192.168.1.119:8080/cxll/";
//	public static String url_data = "http://cxwebservice.sinaapp.com/";
	public static String url_data = "http://service.cx100.cn:86/cxll/";
	
	public static String url_image = "http://www.cx100.cn/Content/upload/";
	
	public static String url_list_image = "http://www.cx100.cn/Content/upload/m_img/";
	
	public static String[] distance = {
		"500��", "1����", "2����", "5����", "10����", "ȫ��"
	};
	
	public static String[] dis = {
		"0.5", "1", "2", "5", "10", "0"
	};
	
	public static int sp_dis = 5;
	
	public static PopularSeller popularSeller;
	
	public static Cuisines cuisines;
	public static Purpose purpose;
	public static PerCapita perCapita;
	public static Zone zone;
	public static Businesszone businesszone;
	public static Railway railway;
	
	public static int sellerid;
	public static List<Map<String, Object>> list_seller_detail;
	
	public static Member member;

}
