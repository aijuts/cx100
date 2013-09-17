package com.aijuts.cx100.util;

import java.lang.reflect.Type;

import com.aijuts.cx100.entity.Businesszone;
import com.aijuts.cx100.entity.Cuisines;
import com.aijuts.cx100.entity.Dish;
import com.aijuts.cx100.entity.DishData;
import com.aijuts.cx100.entity.DishDetail;
import com.aijuts.cx100.entity.Favorite;
import com.aijuts.cx100.entity.File;
import com.aijuts.cx100.entity.Logeinfo;
import com.aijuts.cx100.entity.Member;
import com.aijuts.cx100.entity.MemberUpdate;
import com.aijuts.cx100.entity.NoticeDetail;
import com.aijuts.cx100.entity.NoticeList;
import com.aijuts.cx100.entity.OrderCommit;
import com.aijuts.cx100.entity.OrderList;
import com.aijuts.cx100.entity.PerCapita;
import com.aijuts.cx100.entity.PopularSeller;
import com.aijuts.cx100.entity.Purpose;
import com.aijuts.cx100.entity.Railway;
import com.aijuts.cx100.entity.ReviewsList;
import com.aijuts.cx100.entity.Seller;
import com.aijuts.cx100.entity.SellerDetail;
import com.aijuts.cx100.entity.User;
import com.aijuts.cx100.entity.Zone;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetWebServiceData {
	
	private Gson gson;
	private Type type;
	private Seller seller;
	private SellerDetail sellerDetail;
	private Cuisines cuisines;
	private Purpose purpose;
	private PerCapita perCapita;
	private Zone zone;
	private Businesszone businesszone;
	private Railway railway;
	private NoticeDetail noticeDetail;
	private PopularSeller popularSeller;
	private NoticeList noticeList;
	private OrderList orderList;
	private ReviewsList reviewsList;
	private Dish dish;
	private DishDetail dishDetail;
	private User user;
	private File file;
	private Member member;
	private Favorite favorite;
	private Logeinfo logeinfo;
	private OrderCommit orderCommit;
	private MemberUpdate memberUpdate;
	
	public Seller getSellerAll(String lat, String lng, String dis, String pageStart, String pageSize, String tagKey, String tagValue, String likeName) throws Exception {
		String url = Constants.url_data + "seller/list/" + lat + "/" + lng + "/" + dis + "/" + pageStart + "/" + pageSize + "/" + tagKey + "/" + tagValue + "/" + likeName;
//		System.out.println(url);
		String s = Tool.geTemplate().getForObject(url, String.class);
//		System.out.println(s);
		gson = new Gson();
		seller = new Seller();
		type = new TypeToken<Seller>(){}.getType();
		seller = gson.fromJson(s, type);
		return seller;
	}
	
	public SellerDetail getSellerDetail(int id) throws Exception {
		String url = Constants.url_data + "seller/detail/" + id;
		System.out.println(url);
		String s = Tool.geTemplate().getForObject(url, String.class);
//		System.out.println(s);
		gson = new Gson();
		sellerDetail = new SellerDetail();
		type = new TypeToken<SellerDetail>(){}.getType();
		sellerDetail = gson.fromJson(s, type);
		return sellerDetail;
	}
	
	public Cuisines getCuisinesAll() throws Exception {
		String url = Constants.url_data + "vegetable/list";
		String s = Tool.geTemplate().getForObject(url, String.class);
//		System.out.println(s);
		gson = new Gson();
		cuisines = new Cuisines();
		type = new TypeToken<Cuisines>(){}.getType();
		cuisines = gson.fromJson(s, type);
		return cuisines;
	}
	
	public Purpose getPurposeAll() throws Exception {
		String url = Constants.url_data + "purpose/list";
		String s = Tool.geTemplate().getForObject(url, String.class);
//		System.out.println(s);
		gson = new Gson();
		purpose = new Purpose();
		type = new TypeToken<Purpose>(){}.getType();
		purpose = gson.fromJson(s, type);
		return purpose;
	}
	
	public PerCapita getPerCapitaAll() throws Exception {
		String url = Constants.url_data + "consumption/list";
		String s = Tool.geTemplate().getForObject(url, String.class);
//		System.out.println(s);
		gson = new Gson();
		perCapita = new PerCapita();
		type = new TypeToken<PerCapita>(){}.getType();
		perCapita = gson.fromJson(s, type);
		return perCapita;
	}
	
	public Zone getZoneAll() throws Exception {
		String url = Constants.url_data + "zone/list";
		String s = Tool.geTemplate().getForObject(url, String.class);
//		System.out.println(s);
		gson = new Gson();
		zone = new Zone();
		type = new TypeToken<Zone>(){}.getType();
		zone = gson.fromJson(s, type);
		return zone;
	}
	
	public Businesszone getBusinesszoneAll() throws Exception {
		String url = Constants.url_data + "businesszone/list";
		String s = Tool.geTemplate().getForObject(url, String.class);
//		System.out.println(s);
		gson = new Gson();
		businesszone = new Businesszone();
		type = new TypeToken<Businesszone>(){}.getType();
		businesszone = gson.fromJson(s, type);
		return businesszone;
	}
	
	public Railway getRailwayAll() throws Exception {
		String url = Constants.url_data + "railway/list";
		String s = Tool.geTemplate().getForObject(url, String.class);
//		System.out.println(s);
		gson = new Gson();
		railway = new Railway();
		type = new TypeToken<Railway>(){}.getType();
		railway = gson.fromJson(s, type);
		return railway;
	}
	
	public NoticeList getSellerNoticeAll(int userid) throws Exception {
		String url = Constants.url_data + "seller/notice/list/" + userid;
		String s = Tool.geTemplate().getForObject(url, String.class);
		gson = new Gson();
		noticeList = new NoticeList();
		type = new TypeToken<NoticeList>(){}.getType();
		noticeList = gson.fromJson(s, type);
		return noticeList;
	}
	
	public NoticeDetail getNoticeDetail(int noticeid) throws Exception {
		String url = Constants.url_data + "seller/notice/detail/" + noticeid;
		String s = Tool.geTemplate().getForObject(url, String.class);
		gson = new Gson();
		noticeDetail = new NoticeDetail();
		type = new TypeToken<NoticeDetail>(){}.getType();
		noticeDetail = gson.fromJson(s, type);
		return noticeDetail;
	}
	
	public PopularSeller getPopularSellerAll() throws Exception {
		String url = Constants.url_data + "popularSeller/list";
		String s = Tool.geTemplate().getForObject(url, String.class);
		gson = new Gson();
		popularSeller = new PopularSeller();
		type = new TypeToken<PopularSeller>(){}.getType();
		popularSeller = gson.fromJson(s, type);
		return popularSeller;
	}
	
	public OrderList getSellerOrderAll(int userid) throws Exception {
		String url = Constants.url_data + "seller/order/list/" + userid;
		String s = Tool.geTemplate().getForObject(url, String.class);
		gson = new Gson();
		orderList = new OrderList();
		type = new TypeToken<OrderList>(){}.getType();
		orderList = gson.fromJson(s, type);
		return orderList;
	}
	
	public ReviewsList getSellerReviewsAll(int userid) throws Exception {
		String url = Constants.url_data + "seller/reviews/list/" + userid;
		String s = Tool.geTemplate().getForObject(url, String.class);
		gson = new Gson();
		reviewsList = new ReviewsList();
		type = new TypeToken<ReviewsList>(){}.getType();
		reviewsList = gson.fromJson(s, type);
		return reviewsList;
	}
	
	public Dish getSellerDishAll(int userid, int dishType) throws Exception {
		String url = Constants.url_data + "seller/dish/list/" + userid + "/" + dishType;
		String s = Tool.geTemplate().getForObject(url, String.class);
		gson = new Gson();
		dish = new Dish();
		type = new TypeToken<Dish>(){}.getType();
		dish = gson.fromJson(s, type);
		return dish;
	}
	
	public DishDetail getSellerDishDetail(int disid) throws Exception {
		String url = Constants.url_data + "dish/detail/" + disid;
		String s = Tool.geTemplate().getForObject(url, String.class);
		gson = new Gson();
		dishDetail = new DishDetail();
		type = new TypeToken<DishDetail>(){}.getType();
		dishDetail = gson.fromJson(s, type);
		return dishDetail;
	}
	
	public User getUserReg(String username, String password) throws Exception {
		String url = Constants.url_data + "user/reg/" + username + "/" + password;
		String s = Tool.geTemplate().getForObject(url, String.class);
		System.out.println(s);
		gson = new Gson();
		user = new User();
		type = new TypeToken<User>(){}.getType();
		user = gson.fromJson(s, type);
		return user;
	}
	
	public User getUserLogin(String username, String password) throws Exception {
		String url = Constants.url_data + "user/login/" + username + "/" + password;
		String s = Tool.geTemplate().getForObject(url, String.class);
		System.out.println(s);
		gson = new Gson();
		user = new User();
		type = new TypeToken<User>(){}.getType();
		user = gson.fromJson(s, type);
		return user;
	}
	
	public Member getMember(int userid) throws Exception {
		String url = Constants.url_data + "user/member/detail/" + userid;
		String s = Tool.geTemplate().getForObject(url, String.class);
		System.out.println(s);
		gson = new Gson();
		member = new Member();
		type = new TypeToken<Member>(){}.getType();
		member = gson.fromJson(s, type);
		return member;
	}
	
	public Favorite getFavoriteCount(int sellerid, int userid, int types) throws Exception {
		String url = Constants.url_data + "user/favorite/count/" + sellerid + "/" + userid + "/" + types;
		String s = Tool.geTemplate().getForObject(url, String.class);
		System.out.println(s);
		gson = new Gson();
		favorite = new Favorite();
		type = new TypeToken<Favorite>(){}.getType();
		favorite = gson.fromJson(s, type);
		return favorite;
	}
	
	public Favorite getInsertFavorite(int sellerid, int userid, int types, String title) throws Exception {
		String url = Constants.url_data + "user/favorite/insert/" + sellerid + "/" + userid + "/" + types + "/" + title;
		String s = Tool.geTemplate().getForObject(url, String.class);
		System.out.println(s);
		gson = new Gson();
		favorite = new Favorite();
		type = new TypeToken<Favorite>(){}.getType();
		favorite = gson.fromJson(s, type);
		return favorite;
	}
	
	public Logeinfo getLogeinfoAll(int sellerid) throws Exception {
		String url = Constants.url_data + "seller/logeinfo/list/" + sellerid;
		String s = Tool.geTemplate().getForObject(url, String.class);
		System.out.println(s);
		gson = new Gson();
		logeinfo = new Logeinfo();
		type = new TypeToken<Logeinfo>(){}.getType();
		logeinfo = gson.fromJson(s, type);
		return logeinfo;
	}
	
	public OrderCommit getCommitOrder(String bookingDate, String bookingTime, int sellerid, int userid, int number, int loge, int types, String remark) throws Exception {
		String url = Constants.url_data + "order/commit/" + bookingDate + "/" + bookingTime + "/" + sellerid + "/" + userid + "/" + number + "/" + loge + "/" + types + "/" + remark;
		String s = Tool.geTemplate().getForObject(url, String.class);
		System.out.println(s);
		gson = new Gson();
		orderCommit = new OrderCommit();
		type = new TypeToken<OrderCommit>(){}.getType();
		orderCommit = gson.fromJson(s, type);
		return orderCommit;
	}
	
	public MemberUpdate getUpdateMember(int userid, String key, String value) throws Exception {
		String url = Constants.url_data + "user/member/update/" + userid + "/" + key + "/" + value;
		String s = Tool.geTemplate().getForObject(url, String.class);
		System.out.println(s);
		gson = new Gson();
		memberUpdate = new MemberUpdate();
		type = new TypeToken<MemberUpdate>(){}.getType();
		memberUpdate = gson.fromJson(s, type);
		return memberUpdate;
	}
	
	public OrderList getOrderTop5(int sellerid) throws Exception {
		String url = Constants.url_data + "seller/detail/order/top5/" + sellerid;
		String s = Tool.geTemplate().getForObject(url, String.class);
		System.out.println(s);
		gson = new Gson();
		orderList = new OrderList();
		type = new TypeToken<OrderList>(){}.getType();
		orderList = gson.fromJson(s, type);
		return orderList;
	}

}
