package com.meishimeike.Utils;

public class Constants {
	/** api */
	public static final String APP_SERVICE = "http://www.meishimeike.com";
	public static final String APP_SERVICE_API = APP_SERVICE + "/interface/";
	public static final String APP_FOOD_SHOW_URL = APP_SERVICE + "/show.php?id=";
	public static final String APP_PIC_UPLOAD_SERVICE_API = "http://www.meishimeike.com/interface/";
	
	/** encode */
	public static final String ENCODE = "utf-8";
	//get
	public static final String API_FOOD_LIST = APP_SERVICE_API + "food.php?_a=list";
	public static final String API_FOOD_INFO = APP_SERVICE_API + "food.php?_a=show&fid=";
	public static final String API_FOOD_FANS = APP_SERVICE_API + "food.php?_a=fans&fid=";
	public static final String API_DINNER_INFO = APP_SERVICE_API + "restaurant.php?_a=show&rid=";
	public static final String API_FOOD_COMMENT = APP_SERVICE_API + "comment.php?_a=list&tid=";
	public static final String API_USER_COMMENT = APP_SERVICE_API + "comment.php?_a=list&uid=";
	public static final String API_CITY_LIST = APP_SERVICE_API + "city.php?_a=list&pid=";
	public static final String API_QUANZI_CITY_LIST = APP_SERVICE_API + "city.php?_a=citylist";
	public static final String API_FRIENDS_LIST = APP_SERVICE_API + "user.php?_a=friendlist&uid=";
	public static final String API_SEND_NUM = APP_SERVICE_API + "food.php?_a=addfoodnum&uid=";
	public static final String API_USER_FOOD = APP_SERVICE_API + "user.php?_a=activity&uid=";
	public static final String API_USER_FANS = APP_SERVICE_API + "user.php?_a=fans&uid=";
	public static final String API_USER_INFO = APP_SERVICE_API + "user.php?_a=show&uid=";
	public static final String API_DINNER_FOOD_LIST = APP_SERVICE_API + "food.php?_a=getrestfood&rid=";
	public static final String API_TAG_LIST = APP_SERVICE_API + "tag.php?_a=taglist";
	public static final String API_SQ_LIST = APP_SERVICE_API + "tag.php?_a=districtlist&city=";
	public static final String API_FOOD_SEARCH = APP_SERVICE_API + "food.php?_a=search&key=";
	public static final String API_FOOD_BY_TAG = APP_SERVICE_API + "food.php?_a=getfoodbytagid&tagid=";
	public static final String API_FOOD_BY_RIM = APP_SERVICE_API + "gps.php?_a=foods";
	public static final String API_DINNER_BY_RIM = APP_SERVICE_API + "gps.php?_a=rests";
	public static final String API_RECOMMEND_USER = APP_SERVICE_API + "user.php?_a=recommend&uid=";
	public static final String API_USER_SEARCH = APP_SERVICE_API + "user.php?_a=search&key=";
	public static final String API_USER_COMMENT_REPLY = APP_SERVICE_API + "comment.php?_a=reply&uid=";
	public static final String API_USER_Message = APP_SERVICE_API + "message.php?_a=list&uid=";
	public static final String API_TAG_NUM = APP_SERVICE_API + "user.php?_a=gettagnum&uid=";
	public static final String API_GET_DAREN = APP_SERVICE_API + "user.php?_a=drlist&start=0&num=20";
	public static final String API_FOOD_BY_ATTENTION = APP_SERVICE_API + "user.php?_a=foodlist&uid=";
	public static final String API_MESSAGE_DIALOG = APP_SERVICE_API + "message.php?_a=chat";
	public static final String API_COMMENT_DIALOG = APP_SERVICE_API + "comment.php?_a=list";
	//send
	public static final String API_USER = APP_SERVICE_API + "user.php";
	public static final String API_LIKE_FOOD = APP_SERVICE_API + "food.php?_a=like";
	public static final String API_WANT_FOOD = APP_SERVICE_API + "food.php?_a=want";
	public static final String API_COMMENT = APP_SERVICE_API + "comment.php?_a=add";
	public static final String API_SEND_FOOD = APP_PIC_UPLOAD_SERVICE_API + "food.php";
	public static final String API_USER_ATTENTION = APP_SERVICE_API + "user.php?_a=friend";
	public static final String API_IS_ATTENTION = APP_SERVICE_API + "user.php?_a=isfans";
	public static final String API_INVIT = APP_SERVICE_API + "sys.php?_a=invite";
	public static final String API_USER_TAG_EDIT = APP_SERVICE_API + "user.php?_a=edittag";
	public static final String API_USER_PWD_EDIT = APP_SERVICE_API + "user.php?_a=editpass";
	public static final String API_USER_MSG_SEND = APP_SERVICE_API + "message.php?_a=send";
	public static final String API_USER_HEAD_EDIT = APP_SERVICE_API + "user.php";
	public static final String API_FIND_PWD = APP_SERVICE_API + "user.php?_a=forgetpass&email=";
	public static final String API_CHECK_WUID = APP_SERVICE_API + "user.php?_a=getuserbytuid&tuid=";
	public static final String API_WEIBO_REGISTER = APP_SERVICE_API + "user.php?_a=weiboreg";
	public static final String API_NEW_DINNER = APP_SERVICE_API + "restaurant.php";
	//---------------------------------------------
	public static final String MSMK_CACHE_PATH = "/sdcard/Msmk/Cache/";
}
