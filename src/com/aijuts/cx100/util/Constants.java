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
		"餐厅", "搜索", "个人中心"
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
		"小吃快餐", "苏州江浙", "粤菜", "川菜", "湘菜"
	};
	
	public static String[] Purpose = {
		"情侣约会", "商务宴请", "朋友同事聚餐", "家庭聚会", "特色口味", "特色情境", "商务洽谈", "商会聚餐", 
		"车友会聚餐", "工作餐", "休闲小憩", "随便吃吃", "休闲度假", "商务会议", "生日宴", "寿宴", "婚宴", 
		"满月宴", "年终晚宴", "下午茶", "自助"
	};
	
	public static String[] percapita = {
		"0-50", "50-100", "100-150", "150-200", "200-250", "250-300", "300-350", "350-400", "400-450", 
		"450-500", "500以上"
	};
	
	public static String[] AdminArea = {
		"高新区", "工业园区", "沧浪区", "常熟市", "平江区"
	};
	
	public static String[] Circle = {
		"南门", "盘门", "十全街/凤凰街", "吴江市", "独墅湖大学城"
	};
	
	public static String[] Subway = {
		"地铁一号"
	};
	
	public static String[] userCenter = {
		"账户设置", "我的订单", "我的收藏", "我的积分", "点评管理", "退货维权", "好友邀请", "爱心捐赠"
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
		"500米", "1公里", "2公里", "5公里", "10公里", "全部"
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
