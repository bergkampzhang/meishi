package com.meishimeike.Bll;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.meishimeike.AppMsmk;
import com.meishimeike.Bean.BeanCity;
import com.meishimeike.Bean.BeanComment;
import com.meishimeike.Bean.BeanDinner;
import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Bean.BeanLoc;
import com.meishimeike.Bean.BeanMessage;
import com.meishimeike.Bean.BeanRimDinner;
import com.meishimeike.Bean.BeanTag;
import com.meishimeike.Bean.BeanTagNum;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.Constants;
import com.meishimeike.Utils.LocalVariable;

public class BllGet {
	private Commons coms = null;
	private LocalVariable lv = null;
	private int reloadn = 0;
	public BllGet(Context context) {
		coms = new Commons();
		lv = new LocalVariable(context);
	}

	/**
	 * get main food list
	 * 
	 * @param _type
	 *            1-new,2-hot,3-rim
	 * @param _start
	 * @param _num
	 * @return ArrayList<BeanFood>
	 * http://www.meishimeike.com/interface/food.php?_a=list&type=&city=&login_user_idnew&num=15
	 */
    
	public ArrayList<BeanFood> getMainFoodList(int _type, int _start, int _num) {
		InputStream is = null;
		ArrayList<BeanFood> arrList = null;
		BeanFood bean = null;
		String strResult = "";
		String fid = "", name = "", rid = "", price = "";
		String intro = "", pic = "", pic2 = "", pic3 = "";
		String uid = "", like_num = "", want_num = "";
		String comment_num = "", deleted = "", source = "";
		String c_date = "", m_date = "", biggest_pic = "";
		String big_pic = "", small_pic = "", source_name = "";
		String user_name = "", avatar_60 = "", res_name = "";
		String middle_pic = "";
		
		double longitude = 0, latitude = 0;
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_FOOD_LIST);
			strUrl.append("&type=");
			strUrl.append("&city=");
			strUrl.append(lv.getQuanZiCityId());
			strUrl.append("&login_user_id");
			strUrl.append(lv.getUid());
			switch (_type) {
			case 1:
				strUrl.append("new");
				break;
			case 2:
				strUrl.append("hot");
				break;
			case 3:
				strUrl.append("all");
				break;
			}
			if (_start > 0) {
				strUrl.append("&start=");
				strUrl.append(_start);
			}
			if (_num > 0) {
				strUrl.append("&num=");
				strUrl.append(_num);
			}
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			
			
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanFood>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						fid = jo.optString("fid");
						name = jo.optString("name");
						rid = jo.optString("rid");
						price = jo.optString("price");
						intro = jo.optString("intro");

						pic = jo.optString("pic");
						if (!pic.contains("http:") && !pic.equals("")) {
							pic = Constants.APP_SERVICE + "/upload" + pic;
						}
						pic2 = jo.optString("pic2");
						if (!pic2.contains("http:") && !pic2.equals("")) {
							pic2 = Constants.APP_SERVICE + "/upload" + pic2;
						}
						pic3 = jo.optString("pic3");
						if (!pic3.contains("http:") && !pic3.equals("")) {
							pic3 = Constants.APP_SERVICE + "/upload" + pic3;
						}
						biggest_pic = jo.optString("300_pic");
						if (!biggest_pic.contains("http:")
								&& !biggest_pic.equals("")) {
							biggest_pic = Constants.APP_SERVICE + "/upload"
									+ biggest_pic;
						}
						big_pic = jo.optString("big_pic");
						if (!big_pic.contains("http:") && !big_pic.equals("")) {
							big_pic = Constants.APP_SERVICE + "/upload"
									+ big_pic;
						}
						small_pic = jo.optString("small_pic");
						if (!small_pic.contains("http:")
								&& !small_pic.equals("")) {
							small_pic = Constants.APP_SERVICE + "/upload"
									+ small_pic;
						}
						middle_pic = jo.optString("middle_pic");
						if (!middle_pic.contains("http:")
								&& !middle_pic.equals("")) {
							middle_pic = Constants.APP_SERVICE + "/upload"
									+ middle_pic;
						}

						uid = jo.optString("uid");
						like_num = jo.optString("like_num");
						want_num = jo.optString("want_num");
						comment_num = jo.optString("comment_num");
						deleted = jo.optString("deleted");
						source = jo.optString("source");
						c_date = jo.optString("c_date");
						m_date = jo.optString("m_date");
						source_name = jo.optString("source_name");
						user_name = jo.optString("user_name");
						res_name = jo.optString("res_name");
						avatar_60 = jo.optString("avatar_60");
						longitude = jo.optDouble("longitude", 0);
						latitude = jo.optDouble("latitude", 0);

						bean = new BeanFood();
						bean.setFid(fid);
						bean.setName(name);
						bean.setRid(rid);
						bean.setPrice(price);
						bean.setIntro(intro);
						bean.setPic(pic);
						bean.setPic2(pic2);
						bean.setPic3(pic3);
						bean.setUid(uid);
						bean.setLike_num(like_num);
						bean.setWant_num(want_num);
						bean.setComment_num(comment_num);
						bean.setDeleted(deleted);
						bean.setSource(source);
						bean.setC_date(c_date);
						bean.setM_date(m_date);
						bean.setBiggest_pic(biggest_pic);
						bean.setBig_pic(big_pic);
						bean.setSmall_pic(small_pic);
						bean.setMiddle_pic(middle_pic);
						bean.setSource_name(source_name);
						bean.setuName(user_name);
						bean.setrName(res_name);
						bean.setuHead(avatar_60);
						bean.setLng(longitude);
						bean.setLat(latitude);

						Location locDinner = new Location(
								LocationManager.NETWORK_PROVIDER);
						locDinner.setLatitude(latitude);
						locDinner.setLongitude(longitude);

						BeanLoc b = AppMsmk.getBeanLoc();
						float f = 0;
						if (b != null && b.getLng() != 0 && b.getLat() != 0
								&& latitude != 0 && longitude != 0) {
							f = b.getLoc().distanceTo(locDinner);
							f = Math.abs(f);
							f = (float) Math.rint(f);
						}
						bean.setDistance(f);

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		
		return arrList;
	}

	public ArrayList<BeanFood> getMainFoodByAttention() {
		InputStream is = null;
		ArrayList<BeanFood> arrList = null;
		BeanFood bean = null;
		String strResult = "";
		String fid = "", name = "", rid = "", price = "";
		String intro = "", pic = "", pic2 = "", pic3 = "";
		String uid = "", like_num = "", want_num = "";
		String comment_num = "", deleted = "", source = "";
		String c_date = "", m_date = "", biggest_pic = "";
		String big_pic = "", small_pic = "", source_name = "";
		String user_name = "", avatar_60 = "", res_name = "";
		String middle_pic = "";
		double longitude = 0, latitude = 0;
		int num = 0;
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_FOOD_BY_ATTENTION);
			strUrl.append(lv.getUid());
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanFood>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject joMain = ja.getJSONObject(i);

						JSONObject jaUser = joMain.optJSONObject("user_info");
						if (jaUser != null) {
							user_name = jaUser.optString("name");
							avatar_60 = jaUser.optString("avatar_60");
						}

						JSONArray jaSub = joMain.optJSONArray("food_info");
						if (jaSub == null) {
							continue;
						}
						num = 0;
						for (int x = 0, xmax = jaSub.length(); x < xmax; x++) {
							if (num > 0) {
								break;
							}
							JSONObject joSubMain = jaSub.getJSONObject(x);

							fid = joSubMain.optString("fid");
							name = joSubMain.optString("name");
							rid = joSubMain.optString("rid");
							price = joSubMain.optString("price");
							intro = joSubMain.optString("intro");

							pic = joSubMain.optString("pic");
							if (!pic.contains("http:") && !pic.equals("")) {
								pic = Constants.APP_SERVICE + "/upload" + pic;
							}
							pic2 = joSubMain.optString("pic2");
							if (!pic2.contains("http:") && !pic2.equals("")) {
								pic2 = Constants.APP_SERVICE + "/upload" + pic2;
							}
							pic3 = joSubMain.optString("pic3");
							if (!pic3.contains("http:") && !pic3.equals("")) {
								pic3 = Constants.APP_SERVICE + "/upload" + pic3;
							}
							biggest_pic = joSubMain.optString("300_pic");
							if (!biggest_pic.contains("http:")
									&& !biggest_pic.equals("")) {
								biggest_pic = Constants.APP_SERVICE + "/upload"
										+ biggest_pic;
							}
							big_pic = joSubMain.optString("big_pic");
							if (!big_pic.contains("http:")
									&& !big_pic.equals("")) {
								big_pic = Constants.APP_SERVICE + "/upload"
										+ big_pic;
							}
							small_pic = joSubMain.optString("small_pic");
							if (!small_pic.contains("http:")
									&& !small_pic.equals("")) {
								small_pic = Constants.APP_SERVICE + "/upload"
										+ small_pic;
							}
							middle_pic = joSubMain.optString("middle_pic");
							if (!middle_pic.contains("http:")
									&& !middle_pic.equals("")) {
								middle_pic = Constants.APP_SERVICE + "/upload"
										+ middle_pic;
							}

							uid = joSubMain.optString("uid");
							like_num = joSubMain.optString("like_num");
							want_num = joSubMain.optString("want_num");
							comment_num = joSubMain.optString("comment_num");
							deleted = joSubMain.optString("deleted");
							source = joSubMain.optString("source");
							c_date = joSubMain.optString("c_date");
							m_date = joSubMain.optString("m_date");
							source_name = joSubMain.optString("source_name");
							// user_name = joSubMain.optString("user_name");
							res_name = joSubMain.optString("res_name");
							// avatar_60 = joSubMain.optString("avatar_60");
							longitude = joSubMain.optDouble("longitude", 0);
							latitude = joSubMain.optDouble("latitude", 0);

							bean = new BeanFood();
							bean.setFid(fid);
							bean.setName(name);
							bean.setRid(rid);
							bean.setPrice(price);
							bean.setIntro(intro);
							bean.setPic(pic);
							bean.setPic2(pic2);
							bean.setPic3(pic3);
							bean.setUid(uid);
							bean.setLike_num(like_num);
							bean.setWant_num(want_num);
							bean.setComment_num(comment_num);
							bean.setDeleted(deleted);
							bean.setSource(source);
							bean.setC_date(c_date);
							bean.setM_date(m_date);
							bean.setBiggest_pic(biggest_pic);
							bean.setBig_pic(big_pic);
							bean.setSmall_pic(small_pic);
							bean.setMiddle_pic(middle_pic);
							bean.setSource_name(source_name);
							bean.setuName(user_name);
							bean.setrName(res_name);
							bean.setuHead(avatar_60);
							bean.setLng(longitude);
							bean.setLat(latitude);

							Location locDinner = new Location(
									LocationManager.NETWORK_PROVIDER);
							locDinner.setLatitude(latitude);
							locDinner.setLongitude(longitude);

							BeanLoc b = AppMsmk.getBeanLoc();
							float f = 0;
							if (b != null && b.getLng() != 0 && b.getLat() != 0
									&& latitude != 0 && longitude != 0) {
								f = b.getLoc().distanceTo(locDinner);
								f = Math.abs(f);
								f = (float) Math.rint(f);
							}
							bean.setDistance(f);

							arrList.add(bean);
							num++;
						}

					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get food info
	 * 
	 * @param _fid
	 * @return BeanFood
	 */
	public BeanFood getFoodInfo(String _fid) {
		InputStream is = null;
		BeanFood bean = null;
		String strResult = "";
		String fid = "", name = "", rid = "", price = "";
		String intro = "", pic = "", pic2 = "", pic3 = "";
		String uid = "", like_num = "", want_num = "";
		String comment_num = "", deleted = "", source = "";
		String c_date = "", m_date = "", biggest_pic = "";
		String big_pic = "", small_pic = "", source_name = "";
		String tag = "", tips = "", menu = "", middle_pic = "";
		String userName = "", userHead = "", dinnerName = "";
		double lng = 0, lat = 0;
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_FOOD_INFO);
			strUrl.append(_fid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					JSONObject jo = new JSONObject(strResult);
					fid = jo.optString("fid");
					name = jo.optString("name");
					rid = jo.optString("rid");
					price = jo.optString("price");
					intro = jo.optString("intro");

					pic = jo.optString("pic");
					if (!pic.contains("http:") && !pic.equals("")) {
						pic = Constants.APP_SERVICE + "/upload" + pic;
					}
					pic2 = jo.optString("pic2");
					if (!pic2.contains("http:") && !pic2.equals("")) {
						pic2 = Constants.APP_SERVICE + "/upload" + pic2;
					}
					pic3 = jo.optString("pic3");
					if (!pic3.contains("http:") && !pic3.equals("")) {
						pic3 = Constants.APP_SERVICE + "/upload" + pic3;
					}
					biggest_pic = jo.optString("biggest_pic");
					if (!biggest_pic.contains("http:")
							&& !biggest_pic.equals("")) {
						biggest_pic = Constants.APP_SERVICE + "/upload"
								+ biggest_pic;
					}
					big_pic = jo.optString("big_pic");
					if (!big_pic.contains("http:") && !big_pic.equals("")) {
						big_pic = Constants.APP_SERVICE + "/upload" + big_pic;
					}
					small_pic = jo.optString("small_pic");
					if (!small_pic.contains("http:") && !small_pic.equals("")) {
						small_pic = Constants.APP_SERVICE + "/upload"
								+ small_pic;
					}
					middle_pic = jo.optString("middle_pic");
					if (!middle_pic.contains("http:") && !middle_pic.equals("")) {
						middle_pic = Constants.APP_SERVICE + "/upload"
								+ middle_pic;
					}

					uid = jo.optString("uid");
					like_num = jo.optString("like_num");
					want_num = jo.optString("want_num");
					comment_num = jo.optString("comment_num");
					deleted = jo.optString("deleted");
					source = jo.optString("source");
					c_date = jo.optString("c_date");
					m_date = jo.optString("m_date");
					source_name = jo.optString("source_name");
					menu = jo.optString("menu");
					tips = jo.optString("tips");
					JSONArray jaTips = jo.getJSONArray("tag");
					if (jaTips != null && jaTips.length() > 0) {
						tag = jaTips.getJSONObject(0).optString("tag");
					}
					userName = jo.optString("user_name");
					userHead = jo.optString("avatar_60");
					dinnerName = jo.optString("res_name");
					lng = jo.optDouble("longitude", 0);
					lat = jo.optDouble("latitude", 0);

					bean = new BeanFood();
					bean.setFid(fid);
					bean.setName(name);
					bean.setRid(rid);
					bean.setPrice(price);
					bean.setIntro(intro);
					bean.setPic(pic);
					bean.setPic2(pic2);
					bean.setPic3(pic3);
					bean.setUid(uid);
					bean.setLike_num(like_num);
					bean.setWant_num(want_num);
					bean.setComment_num(comment_num);
					bean.setDeleted(deleted);
					bean.setSource(source);
					bean.setC_date(c_date);
					bean.setM_date(m_date);
					bean.setBiggest_pic(biggest_pic);
					bean.setBig_pic(big_pic);
					bean.setSmall_pic(small_pic);
					bean.setMiddle_pic(middle_pic);
					bean.setSource_name(source_name);
					bean.setMenu(menu);
					bean.setTips(tips);
					bean.setTypeName(tag);
					bean.setuName(userName);
					bean.setuHead(userHead);
					bean.setrName(dinnerName);

					Location locDinner = new Location(
							LocationManager.NETWORK_PROVIDER);
					locDinner.setLatitude(lat);
					locDinner.setLongitude(lng);

					BeanLoc b = AppMsmk.getBeanLoc();
					float f = 0;
					if (b != null && b.getLng() != 0 && b.getLat() != 0
							&& lat != 0 && lng != 0) {
						f = b.getLoc().distanceTo(locDinner);
						f = Math.abs(f);
						f = (float) Math.rint(f);
					}
					bean.setDistance(f);
				}
			} else {
				bean = null;
			}
		} catch (Exception e) {
			bean = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return bean;
	}

	/**
	 * get fans of food
	 * 
	 * @param _fid
	 * @return ArrayList<BeanUserInfo>
	 */
	public ArrayList<BeanUserInfo> getFoodFans(String _fid) {
		InputStream is = null;
		ArrayList<BeanUserInfo> arrList = null;
		BeanUserInfo bean = null;
		String strResult = "";
		String type = "";// 1：发布；2：喜欢；3：想吃；4：评论；5：分享
		String uid = "", email = "", password = "";
		String name = "", name_py = "", name_py_short = "", gender = "";
		String city_id = "", blood_type = "", mobile = "";
		String star = "", decade = "", intro = "", invite_code = "";
		String face_folder = "", face_name = "", cdate = "";
		String mdate = "", login_days = "", deleted = "", fans_num = "";
		String follow_num = "", lastlogin = "", is_active = "", city = "";
		String birthcity = "", city_p = "", city_c = "", gender_name = "";
		String gender_ta = "", home_link = "", name_link = "", photo_30_url = "";
		String photo_60_url = "", photo_180_url = "", photo_url = "";
		String photo_80_url = "";

		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_FOOD_FANS);
			strUrl.append(_fid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanUserInfo>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						type = jo.optString("type");
						JSONObject joSub = jo.getJSONObject("user_info");
						uid = joSub.optString("uid");
						email = joSub.optString("email");
						password = joSub.optString("password");
						name = joSub.optString("name");
						name_py = joSub.optString("name_py");
						name_py_short = joSub.optString("name_py_short");
						gender = joSub.optString("gender");
						city_id = joSub.optString("city_id");
						blood_type = joSub.optString("blood_type");
						mobile = joSub.optString("mobile");
						star = joSub.optString("star");
						decade = joSub.optString("decade");
						intro = joSub.optString("intro");
						invite_code = joSub.optString("invite_code");
						face_folder = joSub.optString("face_folder");
						face_name = joSub.optString("face_name");
						cdate = joSub.optString("cdate");
						mdate = joSub.optString("mdate");
						login_days = joSub.optString("login_days");
						deleted = joSub.optString("deleted");
						fans_num = joSub.optString("fans_num");
						follow_num = joSub.optString("follow_num");
						lastlogin = joSub.optString("lastlogin");
						is_active = joSub.optString("is_active");
						city = joSub.optString("city");
						birthcity = joSub.optString("birthcity");
						city_p = joSub.optString("city_p");
						city_c = joSub.optString("city_c");
						gender_name = joSub.optString("gender_name");
						gender_ta = joSub.optString("gender_ta");
						home_link = joSub.optString("home_link");
						name_link = joSub.optString("name_link");

						photo_30_url = joSub.optString("avatar_30");
						if (!photo_30_url.contains("http:")
								&& !photo_30_url.equals("")) {
							photo_30_url = Constants.APP_SERVICE + "/upload"
									+ photo_30_url;
						}
						photo_60_url = joSub.optString("avatar_60");
						if (!photo_60_url.contains("http:")
								&& !photo_60_url.equals("")) {
							photo_60_url = Constants.APP_SERVICE + "/upload"
									+ photo_60_url;
						}
						photo_80_url = joSub.optString("avatar_80");
						if (!photo_80_url.contains("http:")
								&& !photo_80_url.equals("")) {
							photo_80_url = Constants.APP_SERVICE + "/upload"
									+ photo_80_url;
						}
						photo_180_url = joSub.optString("avatar_180");
						if (!photo_180_url.contains("http:")
								&& !photo_180_url.equals("")) {
							photo_180_url = Constants.APP_SERVICE + "/upload"
									+ photo_180_url;
						}
						photo_url = joSub.optString("avatar_180");
						if (!photo_url.contains("http:")
								&& !photo_url.equals("")) {
							photo_url = Constants.APP_SERVICE + "/upload"
									+ photo_url;
						}

						bean = new BeanUserInfo();
						bean.setType(type);// 1：发布；2：喜欢；3：想吃；4：评论；5：分享
						switch (Integer.valueOf(type)) {
						case 1:
							bean.setTypeName("发布");
							break;
						case 2:
							bean.setTypeName("喜欢");
							break;
						case 3:
							bean.setTypeName("收集");
							break;
						case 4:
							bean.setTypeName("评论");
							break;
						case 5:
							bean.setTypeName("分享");
							break;
						}
						bean.setUid(uid);
						bean.setEmail(email);
						bean.setPassword(password);
						bean.setName(name);
						bean.setName_py(name_py);
						bean.setName_py_short(name_py_short);
						bean.setGender(gender);
						bean.setCity_id(city_id);
						bean.setBlood_type(blood_type);
						bean.setMobile(mobile);
						bean.setStar(star);
						bean.setDecade(decade);
						bean.setIntro(intro);
						bean.setInvite_code(invite_code);
						bean.setFace_folder(face_folder);
						bean.setFace_name(face_name);
						bean.setCdate(cdate);
						bean.setMdate(mdate);
						bean.setLogin_days(login_days);
						bean.setDeleted(deleted);
						bean.setFans_num(fans_num);
						bean.setFollow_num(follow_num);
						bean.setLastlogin(lastlogin);
						bean.setIs_active(is_active);
						bean.setCity(city);
						bean.setBirthcity(birthcity);
						bean.setCity_p(city_p);
						bean.setCity_c(city_c);
						bean.setGender_name(gender_name);
						bean.setGender_ta(gender_ta);
						bean.setHome_link(home_link);
						bean.setName_link(name_link);
						bean.setPhoto_30_url(photo_30_url);
						bean.setPhoto_60_url(photo_60_url);
						bean.setPhoto_80_url(photo_80_url);
						bean.setPhoto_180_url(photo_180_url);
						bean.setPhoto_url(photo_url);

						bean.setAttention(isAttention(uid));

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get dinner info
	 * 
	 * @param _rid
	 * @return BeanDinner
	 */
	public BeanDinner getDinnerInfo(String _rid) {
		InputStream is = null;
		BeanDinner bean = null;
		String strResult = "";
		String rid = "", name = "", city = "", tel = "";
		String c_date = "", city_name = "", address = "";
		double lng = 0, lat = 0;
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_DINNER_INFO);
			strUrl.append(_rid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					JSONObject jo = new JSONObject(strResult);
					rid = jo.optString("rid");
					name = jo.optString("name");
					city = jo.optString("city");
					c_date = jo.optString("c_date");
					city_name = jo.optString("city_name");
					address = jo.optString("address");
					tel = jo.optString("tel");
					lng = jo.optDouble("longitude");
					lat = jo.optDouble("latitude");
					bean = new BeanDinner();
					bean.setRid(rid);
					bean.setName(name);
					bean.setCity(city);
					bean.setC_date(c_date);
					bean.setCity_name(city_name);
					bean.setAddress(address);
					bean.setTel(tel);
					bean.setLng(lng);
					bean.setLat(lat);

					Location locDinner = new Location(
							LocationManager.NETWORK_PROVIDER);
					locDinner.setLatitude(lat);
					locDinner.setLongitude(lng);

					BeanLoc b = AppMsmk.getBeanLoc();
					float f = 0;
					if (b != null && b.getLng() != 0 && b.getLat() != 0
							&& lat != 0 && lng != 0) {
						f = b.getLoc().distanceTo(locDinner);
						f = Math.abs(f);
						f = (float) Math.rint(f);
					}
					bean.setDistance(f);

				}
			} else {
				bean = null;
			}
		} catch (Exception e) {
			bean = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return bean;
	}

	/**
	 * get comment of food
	 * 
	 * @param _fid
	 * @return ArrayList<BeanComment>
	 */
	public ArrayList<BeanComment> getFoodComment(String _fid) {
		InputStream is = null;
		ArrayList<BeanComment> arrList = null;
		BeanComment bean = null;
		String strResult = "";
		// comment info
		String id = "", tid = "", content = "";
		String rid = "", is_secret = "", status = "";
		String c_time = "", m_time = "";
		// user info
		String uid = "", name = "", photo_url = "", intro = "";
		String photo_30_url = "", photo_60_url = "";
		String photo_80_url = "", photo_180_url = "";
		// a user info
		String a_uid = "", a_uname = "";

		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_FOOD_COMMENT);
			strUrl.append(_fid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanComment>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.optJSONObject(i);
						// comment info
						id = jo.optString("id");
						tid = jo.optString("tid");
						content = jo.optString("content");
						rid = jo.optString("rid");
						is_secret = jo.optString("is_secret");
						status = jo.optString("status");
						c_time = jo.optString("c_time");
						m_time = jo.optString("m_time");
						// 评论人信息
						JSONObject joSub = jo.optJSONObject("a_user_info");
						uid = joSub.optString("uid");
						name = joSub.optString("name");
						intro = joSub.optString("intro");

						photo_30_url = joSub.optString("avatar_30");
						if (!photo_30_url.contains("http:")
								&& !photo_30_url.equals("")) {
							photo_30_url = Constants.APP_SERVICE + "/upload"
									+ photo_30_url;
						}
						photo_60_url = joSub.optString("avatar_60");
						if (!photo_60_url.contains("http:")
								&& !photo_60_url.equals("")) {
							photo_60_url = Constants.APP_SERVICE + "/upload"
									+ photo_60_url;
						}
						photo_80_url = joSub.optString("avatar_80");
						if (!photo_80_url.contains("http:")
								&& !photo_80_url.equals("")) {
							photo_80_url = Constants.APP_SERVICE + "/upload"
									+ photo_80_url;
						}
						photo_180_url = joSub.optString("avatar_180");
						if (!photo_180_url.contains("http:")
								&& !photo_180_url.equals("")) {
							photo_180_url = Constants.APP_SERVICE + "/upload"
									+ photo_180_url;
						}
						photo_url = joSub.optString("photo_url");
						if (!photo_url.contains("http:")
								&& !photo_url.equals("")) {
							photo_url = Constants.APP_SERVICE + "/upload"
									+ photo_url;
						}

						// 被评论人信息
						a_uid = "";
						a_uname = "";
						joSub = jo.optJSONObject("r_user_info");
						if (joSub != null) {
							a_uid = joSub.optString("uid");
							a_uname = joSub.optString("name");
						}

						bean = new BeanComment();
						bean.setId(id);
						bean.setTid(tid);
						bean.setContent(content);
						bean.setRid(rid);
						bean.setIs_secret(is_secret);
						bean.setStatus(status);
						bean.setC_time(c_time);
						bean.setM_time(m_time);
						bean.setUid(uid);
						bean.setName(name);
						bean.setIntro(intro);
						bean.setPhoto_url(photo_60_url);
						bean.setA_uid(a_uid);
						bean.setA_uName(a_uname);
						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get city list
	 * 
	 * @param _pid
	 *            为空取省列表，_pid不为空取相应省下的城市
	 * @return ArrayList<BeanCity>
	 */
	public ArrayList<BeanCity> getCityList(String _pid) {
		InputStream is = null;
		ArrayList<BeanCity> arrList = null;
		BeanCity bean = null;
		String strResult = "";
		String id = "", name = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_CITY_LIST);
			strUrl.append(_pid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanCity>();
					JSONObject jo = new JSONObject(strResult);
					Iterator<?> it = jo.keys();
					while (it.hasNext()) {
						id = (String) it.next().toString();
						if (!id.equals(_pid)) {
							name = jo.optString(id);
							bean = new BeanCity();
							bean.setId(id);
							bean.setName(name);
							arrList.add(bean);
						}
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get quanzi city list
	 * 
	 * @return ArrayList<BeanCity>
	 */
	public ArrayList<BeanCity> getQuanZiCityList() {
		InputStream is = null;
		ArrayList<BeanCity> arrList = null;
		BeanCity bean = null;
		String strResult = "";
		String id = "", name = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_QUANZI_CITY_LIST);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanCity>();
					JSONObject jo = new JSONObject(strResult);
					Iterator<?> it = jo.keys();
					while (it.hasNext()) {
						id = (String) it.next().toString();
						name = jo.optString(id);
						bean = new BeanCity();
						bean.setId(id);
						bean.setName(name);
						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get attention of user
	 * 
	 * @param _uid
	 * @return ArrayList<BeanUserInfo>
	 */
	public ArrayList<BeanUserInfo> getAttention(String _uid) {
		InputStream is = null;
		ArrayList<BeanUserInfo> arrList = null;
		BeanUserInfo bean = null;
		String strResult = "";
		String uid = "", email = "", password = "";
		String name = "", name_py = "", name_py_short = "", gender = "";
		String city_id = "", blood_type = "", mobile = "";
		String star = "", decade = "", intro = "", invite_code = "";
		String face_folder = "", face_name = "", cdate = "";
		String mdate = "", login_days = "", deleted = "", fans_num = "";
		String follow_num = "", lastlogin = "", is_active = "", city = "";
		String birthcity = "", city_p = "", city_c = "", gender_name = "";
		String gender_ta = "", home_link = "", name_link = "", photo_30_url = "";
		String photo_60_url = "", photo_180_url = "", photo_url = "";
		String photo_80_url = "";

		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_FRIENDS_LIST);
			strUrl.append(_uid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanUserInfo>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						uid = jo.optString("uid");
						email = jo.optString("email");
						password = jo.optString("password");
						name = jo.optString("name");
						name_py = jo.optString("name_py");
						name_py_short = jo.optString("name_py_short");
						gender = jo.optString("gender");
						city_id = jo.optString("city_id");
						blood_type = jo.optString("blood_type");
						mobile = jo.optString("mobile");
						star = jo.optString("star");
						decade = jo.optString("decade");
						intro = jo.optString("intro");
						invite_code = jo.optString("invite_code");
						face_folder = jo.optString("face_folder");
						face_name = jo.optString("face_name");
						cdate = jo.optString("cdate");
						mdate = jo.optString("mdate");
						login_days = jo.optString("login_days");
						deleted = jo.optString("deleted");
						fans_num = jo.optString("fans_num");
						follow_num = jo.optString("follow_num");
						lastlogin = jo.optString("lastlogin");
						is_active = jo.optString("is_active");
						city = jo.optString("city");
						birthcity = jo.optString("birthcity");
						city_p = jo.optString("city_p");
						city_c = jo.optString("city_c");
						gender_name = jo.optString("gender_name");
						gender_ta = jo.optString("gender_ta");
						home_link = jo.optString("home_link");
						name_link = jo.optString("name_link");

						photo_30_url = jo.optString("avatar_30");
						if (!photo_30_url.contains("http:")) {
							photo_30_url = Constants.APP_SERVICE + photo_30_url;
						}
						photo_60_url = jo.optString("avatar_60");
						if (!photo_60_url.contains("http:")) {
							photo_60_url = Constants.APP_SERVICE + photo_60_url;
						}
						photo_180_url = jo.optString("avatar_180");
						if (!photo_180_url.contains("http:")) {
							photo_180_url = Constants.APP_SERVICE
									+ photo_180_url;
						}
						photo_80_url = jo.optString("avatar_180");
						if (!photo_80_url.contains("http:")) {
							photo_80_url = Constants.APP_SERVICE + photo_80_url;
						}
						photo_url = jo.optString("photo_url");
						if (!photo_url.contains("http:")) {
							photo_url = Constants.APP_SERVICE + photo_url;
						}

						bean = new BeanUserInfo();
						bean.setUid(uid);
						bean.setEmail(email);
						bean.setPassword(password);
						bean.setName(name);
						bean.setName_py(name_py);
						bean.setName_py_short(name_py_short);
						bean.setGender(gender);
						bean.setCity_id(city_id);
						bean.setBlood_type(blood_type);
						bean.setMobile(mobile);
						bean.setStar(star);
						bean.setDecade(decade);
						bean.setIntro(intro);
						bean.setInvite_code(invite_code);
						bean.setFace_folder(face_folder);
						bean.setFace_name(face_name);
						bean.setCdate(cdate);
						bean.setMdate(mdate);
						bean.setLogin_days(login_days);
						bean.setDeleted(deleted);
						bean.setFans_num(fans_num);
						bean.setFollow_num(follow_num);
						bean.setLastlogin(lastlogin);
						bean.setIs_active(is_active);
						bean.setCity(city);
						bean.setBirthcity(birthcity);
						bean.setCity_p(city_p);
						bean.setCity_c(city_c);
						bean.setGender_name(gender_name);
						bean.setGender_ta(gender_ta);
						bean.setHome_link(home_link);
						bean.setName_link(name_link);
						bean.setPhoto_30_url(photo_30_url);
						bean.setPhoto_60_url(photo_60_url);
						bean.setPhoto_80_url(photo_80_url);
						bean.setPhoto_180_url(photo_180_url);
						bean.setPhoto_url(photo_url);

						bean.setAttention(isAttention(uid));

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get the user send number
	 * 
	 * @param _uid
	 * @return String
	 */
	public String getSendNum(String _uid) {
		InputStream is = null;
		String strResult = "", strNum = "0";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_SEND_NUM);
			strUrl.append(_uid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					strNum = strResult;
				}
			} else {
				strNum = "0";
			}
		} catch (Exception e) {
			strNum = "0";
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return strNum;
	}

	/**
	 * get the user send food list
	 * 
	 * @param _uid
	 * @return ArrayList<BeanFood>
	 */
	public ArrayList<BeanFood> getUserFood(String _uid) {
		InputStream is = null;
		ArrayList<BeanFood> arrList = null;
		BeanFood bean = null;
		String strResult = "", strType = "";
		String fid = "", name = "", rid = "", price = "";
		String intro = "", pic = "", pic2 = "", pic3 = "";
		String uid = "", like_num = "", want_num = "";
		String comment_num = "", deleted = "", source = "";
		String c_date = "", m_date = "", biggest_pic = "";
		String big_pic = "", small_pic = "", source_name = "";
		String userName = "", userHead = "", dinnerName = "";
		String middle_pic = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_USER_FOOD);
			strUrl.append(_uid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanFood>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						strType = jo.optString("type");// 1：发布；2：喜欢；3：想吃；4：评论；5：分享
						JSONObject joSub = jo.getJSONObject("food_info");
						fid = joSub.optString("fid");
						name = joSub.optString("name");
						rid = joSub.optString("rid");
						price = joSub.optString("price");
						intro = joSub.optString("intro");

						pic = joSub.optString("pic");
						if (!pic.contains("http:") && !pic.equals("")) {
							pic = Constants.APP_SERVICE + "/upload" + pic;
						}
						pic2 = joSub.optString("pic2");
						if (!pic2.contains("http:") && !pic2.equals("")) {
							pic2 = Constants.APP_SERVICE + "/upload" + pic2;
						}
						pic3 = joSub.optString("pic3");
						if (!pic3.contains("http:") && !pic3.equals("")) {
							pic3 = Constants.APP_SERVICE + "/upload" + pic3;
						}
						biggest_pic = joSub.optString("biggest_pic");
						if (!biggest_pic.contains("http:")
								&& !biggest_pic.equals("")) {
							biggest_pic = Constants.APP_SERVICE + "/upload"
									+ biggest_pic;
						}
						big_pic = joSub.optString("big_pic");
						if (!big_pic.contains("http:") && !big_pic.equals("")) {
							big_pic = Constants.APP_SERVICE + "/upload"
									+ big_pic;
						}
						small_pic = joSub.optString("small_pic");
						if (!small_pic.contains("http:")
								&& !small_pic.equals("")) {
							small_pic = Constants.APP_SERVICE + "/upload"
									+ small_pic;
						}
						middle_pic = joSub.optString("middle_pic");
						if (!middle_pic.contains("http:")
								&& !middle_pic.equals("")) {
							middle_pic = Constants.APP_SERVICE + "/upload"
									+ middle_pic;
						}

						uid = joSub.optString("uid");
						like_num = joSub.optString("like_num");
						want_num = joSub.optString("want_num");
						comment_num = joSub.optString("comment_num");
						deleted = joSub.optString("deleted");
						source = joSub.optString("source");
						c_date = joSub.optString("c_date");
						m_date = joSub.optString("m_date");
						source_name = joSub.optString("source_name");
						userName = joSub.optString("user_name");
						userHead = joSub.optString("avatar_60");
						dinnerName = joSub.optString("res_name");
						if ("null".equals(dinnerName)) {
							dinnerName = "";
						}
						bean = new BeanFood();
						bean.setType(strType);
						if ("1".equals(strType)) {
							bean.setTypeName("发布");
						} else if ("2".equals(strType)) {
							bean.setTypeName("喜欢");
						} else if ("3".equals(strType)) {
							bean.setTypeName("收集");
						} else if ("4".equals(strType)) {
							bean.setTypeName("评论");
						} else if ("5".equals(strType)) {
							bean.setTypeName("分享");
						}
						bean.setFid(fid);
						bean.setName(name);
						bean.setRid(rid);
						bean.setPrice(price);
						bean.setIntro(intro);
						bean.setPic(pic);
						bean.setPic2(pic2);
						bean.setPic3(pic3);
						bean.setUid(uid);
						bean.setLike_num(like_num);
						bean.setWant_num(want_num);
						bean.setComment_num(comment_num);
						bean.setDeleted(deleted);
						bean.setSource(source);
						bean.setC_date(c_date);
						bean.setM_date(m_date);
						bean.setBiggest_pic(biggest_pic);
						bean.setBig_pic(big_pic);
						bean.setSmall_pic(small_pic);
						bean.setMiddle_pic(middle_pic);
						bean.setSource_name(source_name);
						bean.setuHead(userHead);
						bean.setuName(userName);
						bean.setrName(dinnerName);

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get the dinner food list
	 * 
	 * @param _uid
	 * @return ArrayList<BeanFood>
	 */
	public ArrayList<BeanFood> getDinnerFood(String _dinnerId) {
		InputStream is = null;
		ArrayList<BeanFood> arrList = null;
		BeanFood bean = null;
		String strResult = "";
		String fid = "", name = "", rid = "", price = "";
		String intro = "", pic = "", pic2 = "", pic3 = "";
		String uid = "", like_num = "", want_num = "";
		String comment_num = "", deleted = "", source = "";
		String c_date = "", m_date = "", biggest_pic = "";
		String big_pic = "", small_pic = "", source_name = "";
		String avatar_60 = "", userName = "", middle_pic = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_DINNER_FOOD_LIST);
			strUrl.append(_dinnerId);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanFood>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						fid = jo.optString("fid");
						name = jo.optString("name");
						rid = jo.optString("rid");
						price = jo.optString("price");
						intro = jo.optString("intro");

						pic = jo.optString("pic");
						if (!pic.contains("http:") && !pic.equals("")) {
							pic = Constants.APP_SERVICE + "/upload" + pic;
						}
						pic2 = jo.optString("pic2");
						if (!pic2.contains("http:") && !pic2.equals("")) {
							pic2 = Constants.APP_SERVICE + "/upload" + pic2;
						}
						pic3 = jo.optString("pic3");
						if (!pic3.contains("http:") && !pic3.equals("")) {
							pic3 = Constants.APP_SERVICE + "/upload" + pic3;
						}
						biggest_pic = jo.optString("biggest_pic");
						if (!biggest_pic.contains("http:")
								&& !biggest_pic.equals("")) {
							biggest_pic = Constants.APP_SERVICE + "/upload"
									+ biggest_pic;
						}
						big_pic = jo.optString("big_pic");
						if (!big_pic.contains("http:") && !big_pic.equals("")) {
							big_pic = Constants.APP_SERVICE + "/upload"
									+ big_pic;
						}
						small_pic = jo.optString("small_pic");
						if (!small_pic.contains("http:")
								&& !small_pic.equals("")) {
							small_pic = Constants.APP_SERVICE + "/upload"
									+ small_pic;
						}
						middle_pic = jo.optString("middle_pic");
						if (!middle_pic.contains("http:")
								&& !middle_pic.equals("")) {
							middle_pic = Constants.APP_SERVICE + "/upload"
									+ middle_pic;
						}

						uid = jo.optString("uid");
						like_num = jo.optString("like_num");
						want_num = jo.optString("want_num");
						comment_num = jo.optString("comment_num");
						deleted = jo.optString("deleted");
						source = jo.optString("source");
						c_date = jo.optString("c_date");
						m_date = jo.optString("m_date");
						source_name = jo.optString("source_name");
						avatar_60 = jo.optString("avatar_60");
						userName = jo.optString("user_name");
						bean = new BeanFood();
						bean.setTypeName("发布了");
						bean.setFid(fid);
						bean.setName(name);
						bean.setRid(rid);
						bean.setPrice(price);
						bean.setIntro(intro);
						bean.setPic(pic);
						bean.setPic2(pic2);
						bean.setPic3(pic3);
						bean.setUid(uid);
						bean.setLike_num(like_num);
						bean.setWant_num(want_num);
						bean.setComment_num(comment_num);
						bean.setDeleted(deleted);
						bean.setSource(source);
						bean.setC_date(c_date);
						bean.setM_date(m_date);
						bean.setBiggest_pic(biggest_pic);
						bean.setBig_pic(big_pic);
						bean.setSmall_pic(small_pic);
						bean.setMiddle_pic(middle_pic);
						bean.setSource_name(source_name);
						bean.setuHead(avatar_60);
						bean.setuName(userName);
						bean.setrName(name);

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	public ArrayList<BeanUserInfo> getUserFans(String _uid) {
		InputStream is = null;
		ArrayList<BeanUserInfo> arrList = null;
		BeanUserInfo bean = null;
		String strResult = "";
		String uid = "", email = "", password = "";
		String name = "", name_py = "", name_py_short = "", gender = "";
		String city_id = "", blood_type = "", mobile = "";
		String star = "", decade = "", intro = "", invite_code = "";
		String face_folder = "", face_name = "", cdate = "";
		String mdate = "", login_days = "", deleted = "", fans_num = "";
		String follow_num = "", lastlogin = "", is_active = "", city = "";
		String birthcity = "", city_p = "", city_c = "", gender_name = "";
		String gender_ta = "", home_link = "", name_link = "", photo_30_url = "";
		String photo_60_url = "", photo_180_url = "", photo_url = "";
		String photo_80_url = "";

		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_USER_FANS);
			strUrl.append(_uid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanUserInfo>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						uid = jo.optString("uid");
						email = jo.optString("email");
						password = jo.optString("password");
						name = jo.optString("name");
						name_py = jo.optString("name_py");
						name_py_short = jo.optString("name_py_short");
						gender = jo.optString("gender");
						city_id = jo.optString("city_id");
						blood_type = jo.optString("blood_type");
						mobile = jo.optString("mobile");
						star = jo.optString("star");
						decade = jo.optString("decade");
						intro = jo.optString("intro");
						invite_code = jo.optString("invite_code");
						face_folder = jo.optString("face_folder");
						face_name = jo.optString("face_name");
						cdate = jo.optString("cdate");
						mdate = jo.optString("mdate");
						login_days = jo.optString("login_days");
						deleted = jo.optString("deleted");
						fans_num = jo.optString("fans_num");
						follow_num = jo.optString("follow_num");
						lastlogin = jo.optString("lastlogin");
						is_active = jo.optString("is_active");
						city = jo.optString("city");
						birthcity = jo.optString("birthcity");
						city_p = jo.optString("city_p");
						city_c = jo.optString("city_c");
						gender_name = jo.optString("gender_name");
						gender_ta = jo.optString("gender_ta");
						home_link = jo.optString("home_link");
						name_link = jo.optString("name_link");

						photo_30_url = jo.optString("avatar_30");
						if (!photo_30_url.contains("http:")
								&& !photo_30_url.equals("")) {
							photo_30_url = Constants.APP_SERVICE + "/upload"
									+ photo_30_url;
						}
						photo_60_url = jo.optString("avatar_60");
						if (!photo_60_url.contains("http:")
								&& !photo_60_url.equals("")) {
							photo_60_url = Constants.APP_SERVICE + "/upload"
									+ photo_60_url;
						}
						photo_80_url = jo.optString("avatar_80");
						if (!photo_80_url.contains("http:")
								&& !photo_80_url.equals("")) {
							photo_80_url = Constants.APP_SERVICE + "/upload"
									+ photo_80_url;
						}
						photo_180_url = jo.optString("avatar_180");
						if (!photo_180_url.contains("http:")
								&& !photo_180_url.equals("")) {
							photo_180_url = Constants.APP_SERVICE + "/upload"
									+ photo_180_url;
						}
						photo_url = jo.optString("avatar_180");
						if (!photo_url.contains("http:")
								&& !photo_url.equals("")) {
							photo_url = Constants.APP_SERVICE + "/upload"
									+ photo_url;
						}

						bean = new BeanUserInfo();
						bean.setUid(uid);
						bean.setEmail(email);
						bean.setPassword(password);
						bean.setName(name);
						bean.setName_py(name_py);
						bean.setName_py_short(name_py_short);
						bean.setGender(gender);
						bean.setCity_id(city_id);
						bean.setBlood_type(blood_type);
						bean.setMobile(mobile);
						bean.setStar(star);
						bean.setDecade(decade);
						bean.setIntro(intro);
						bean.setInvite_code(invite_code);
						bean.setFace_folder(face_folder);
						bean.setFace_name(face_name);
						bean.setCdate(cdate);
						bean.setMdate(mdate);
						bean.setLogin_days(login_days);
						bean.setDeleted(deleted);
						bean.setFans_num(fans_num);
						bean.setFollow_num(follow_num);
						bean.setLastlogin(lastlogin);
						bean.setIs_active(is_active);
						bean.setCity(city);
						bean.setBirthcity(birthcity);
						bean.setCity_p(city_p);
						bean.setCity_c(city_c);
						bean.setGender_name(gender_name);
						bean.setGender_ta(gender_ta);
						bean.setHome_link(home_link);
						bean.setName_link(name_link);
						bean.setPhoto_30_url(photo_30_url);
						bean.setPhoto_60_url(photo_60_url);
						bean.setPhoto_80_url(photo_80_url);
						bean.setPhoto_180_url(photo_180_url);
						bean.setPhoto_url(photo_url);

						bean.setAttention(isAttention(uid));

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get user info
	 * 
	 * @param _uid
	 * @return BeanUserInfo
	 */
	public BeanUserInfo getUserInfo(String _uid) {
		InputStream is = null;
		BeanUserInfo bean = null;
		String strResult = "";
		String uid = "", name = "", gender = "";
		String city = "", intro = "", errno = "", cityId = "";
		String fans = "", attentions = "", avatar_30 = "";
		String avatar_60 = "", avatar_80 = "", avatar_180 = "";
		String gender_name = "", photo = "";
		String tags = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_USER_INFO);
			strUrl.append(_uid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					JSONObject jo = new JSONObject(strResult);
					errno = jo.optString("errno");
					if (errno.equals("0")) {
						uid = jo.optString("uid");
						name = jo.optString("name");
						city = jo.optString("city");
						cityId = jo.optString("city_id");
						gender = jo.optString("gender");
						gender_name = jo.optString("gender_name");
						intro = jo.optString("intro");
						fans = jo.optString("fans_num");
						attentions = jo.optString("follow_num");
						avatar_30 = jo.optString("avatar_30");
						avatar_60 = jo.optString("avatar_60");
						avatar_80 = jo.optString("avatar_80");
						avatar_180 = jo.optString("avatar_180");
						// tags = jo.optString("tag");
						JSONArray ja = jo.getJSONArray("tag");
						for (int i = 0, max = ja.length(); i < max; i++) {
							JSONObject joTemp = ja.getJSONObject(i);
							tags += joTemp.optString("tid") + "|";
						}
						if (!"".equals(tags)) {
							tags = tags.substring(0, tags.length() - 1);
						}
						if ("null".equals(intro)) {
							intro = "";
						}
						photo = jo.optString("photo");
						if (!photo.contains("http:") && !photo.equals("")) {
							photo = Constants.APP_SERVICE + "/upload" + photo;
						}
						bean = new BeanUserInfo();
						bean.setUid(uid);
						bean.setName(name);
						bean.setCity(city);
						bean.setCity_id(cityId);
						bean.setGender(gender);
						bean.setGender_name(gender_name);
						bean.setIntro(intro);
						bean.setPhoto_url(photo);
						bean.setPhoto_30_url(avatar_30);
						bean.setPhoto_60_url(avatar_60);
						bean.setPhoto_80_url(avatar_80);
						bean.setPhoto_180_url(avatar_180);
						bean.setFans_num(fans);
						bean.setFollow_num(attentions);
						bean.setTags(tags);

						bean.setAttention(isAttention(uid));
					}
				}
			} else {
				bean = null;
			}
		} catch (Exception e) {
			bean = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return bean;
	}

	/**
	 * get tag list
	 * 
	 * @param _fid
	 * @return BeanFood
	 */
	public ArrayList<BeanTag> getTagList() {
		InputStream is = null;
		ArrayList<BeanTag> arrList = null;
		BeanTag bean = null;
		String strResult = "";
		String id = "", name = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_TAG_LIST);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanTag>();
					JSONObject jo = new JSONObject(strResult);
					Iterator<?> it = jo.keys();
					while (it.hasNext()) {
						id = (String) it.next().toString();
						name = jo.optString(id);
						bean = new BeanTag();
						bean.setTagId(id);
						bean.setTagName(name);
						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get tag list
	 * 
	 * @param _fid
	 * @return BeanFood
	 */
	public ArrayList<BeanTag> getSQList() {
		InputStream is = null;
		ArrayList<BeanTag> arrList = null;
		BeanTag bean = null;
		String strResult = "";
		String id = "", name = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_SQ_LIST);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanTag>();
					JSONObject jo = new JSONObject(strResult);
					Iterator<?> it = jo.keys();
					while (it.hasNext()) {
						id = (String) it.next().toString();
						name = jo.optString(id);
						bean = new BeanTag();
						bean.setTagId(id);
						bean.setTagName(name);
						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * search food
	 * 
	 * @param _key
	 * @return ArrayList<BeanFood>
	 */
	public ArrayList<BeanFood> searchFood(String _key) {
		InputStream is = null;
		ArrayList<BeanFood> arrList = null;
		BeanFood bean = null;
		BeanUserInfo beanUser = null;
		BeanDinner beanDinner = null;
		String strResult = "";
		String fid = "", name = "", rid = "", price = "";
		String intro = "", pic = "", pic2 = "", pic3 = "";
		String uid = "", like_num = "", want_num = "";
		String comment_num = "", deleted = "", source = "";
		String c_date = "", m_date = "", biggest_pic = "";
		String big_pic = "", small_pic = "", source_name = "";
		String middle_pic = "";
		String strDist = "", strLat = "", strLng = "";
		double longitude = 0, latitude = 0;
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_FOOD_SEARCH);
			strUrl.append(URLEncoder.encode(_key, Constants.ENCODE));
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanFood>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						fid = jo.optString("fid");
						name = jo.optString("name");
						rid = jo.optString("rid");
						price = jo.optString("price");
						intro = jo.optString("intro");
						uid = jo.optString("uid");
						like_num = jo.optString("like_num");
						want_num = jo.optString("want_num");
						comment_num = jo.optString("comment_num");
						deleted = jo.optString("deleted");
						source = jo.optString("source");
						c_date = jo.optString("c_date");
						m_date = jo.optString("m_date");
						source_name = jo.optString("source_name");

						longitude = jo.optDouble("rest_long", 0);
						latitude = jo.optDouble("rest_lati", 0);

						pic = jo.optString("pic");
						if (!pic.contains("http:") && !pic.equals("")) {
							pic = Constants.APP_SERVICE + "/upload" + pic;
						}
						pic2 = jo.optString("pic2");
						if (!pic2.contains("http:") && !pic2.equals("")) {
							pic2 = Constants.APP_SERVICE + "/upload" + pic2;
						}
						pic3 = jo.optString("pic3");
						if (!pic3.contains("http:") && !pic3.equals("")) {
							pic3 = Constants.APP_SERVICE + "/upload" + pic3;
						}
						biggest_pic = jo.optString("biggest_pic");
						if (!biggest_pic.contains("http:")
								&& !biggest_pic.equals("")) {
							biggest_pic = Constants.APP_SERVICE + "/upload"
									+ biggest_pic;
						}
						big_pic = jo.optString("big_pic");
						if (!big_pic.contains("http:") && !big_pic.equals("")) {
							big_pic = Constants.APP_SERVICE + "/upload"
									+ big_pic;
						}
						small_pic = jo.optString("small_pic");
						if (!small_pic.contains("http:")
								&& !small_pic.equals("")) {
							small_pic = Constants.APP_SERVICE + "/upload"
									+ small_pic;
						}
						middle_pic = jo.optString("middle_pic");
						if (!middle_pic.contains("http:")
								&& !middle_pic.equals("")) {
							middle_pic = Constants.APP_SERVICE + "/upload"
									+ middle_pic;
						}

						bean = new BeanFood();
						bean.setFid(fid);
						bean.setName(name);
						bean.setRid(rid);
						bean.setPrice(price);
						bean.setIntro(intro);
						bean.setPic(pic);
						bean.setPic2(pic2);
						bean.setPic3(pic3);
						bean.setUid(uid);
						bean.setLike_num(like_num);
						bean.setWant_num(want_num);
						bean.setComment_num(comment_num);
						bean.setDeleted(deleted);
						bean.setSource(source);
						bean.setC_date(c_date);
						bean.setM_date(m_date);
						bean.setBiggest_pic(biggest_pic);
						bean.setBig_pic(big_pic);
						bean.setSmall_pic(small_pic);
						bean.setMiddle_pic(middle_pic);
						bean.setSource_name(source_name);

						beanUser = getUserInfo(uid);
						if (beanUser != null) {
							bean.setuHead(beanUser.getPhoto_url());
							bean.setuName(beanUser.getName());
						}
						beanDinner = getDinnerInfo(rid);
						if (beanDinner != null) {
							bean.setrName(beanDinner.getName());
						}

						Location locDinner = new Location(
								LocationManager.NETWORK_PROVIDER);
						locDinner.setLatitude(latitude);
						locDinner.setLongitude(longitude);

						BeanLoc b = AppMsmk.getBeanLoc();
						float f = 0;
						if (b != null && b.getLng() != 0 && b.getLat() != 0
								&& latitude != 0 && longitude != 0) {
							f = b.getLoc().distanceTo(locDinner);
							f = Math.abs(f);
							f = (float) Math.rint(f);
						}
						bean.setDistance(f);

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get food list by tag
	 * 
	 * @param _tagId
	 * @return ArrayList<BeanFood>
	 */
	public ArrayList<BeanFood> getFoodListByTag(String _tagId) {
		InputStream is = null;
		ArrayList<BeanFood> arrList = null;
		BeanFood bean = null;
		BeanUserInfo beanUser = null;
		BeanDinner beanDinner = null;
		String strResult = "";
		String fid = "", name = "", rid = "", price = "";
		String intro = "", pic = "", pic2 = "", pic3 = "";
		String uid = "", like_num = "", want_num = "";
		String comment_num = "", deleted = "", source = "";
		String c_date = "", m_date = "", biggest_pic = "";
		String big_pic = "", small_pic = "", source_name = "";
		String middle_pic = "";
		String strDist = "", strLat = "", strLng = "";
		double longitude = 0, latitude = 0;
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_FOOD_BY_TAG);
			strUrl.append(_tagId);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanFood>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						fid = jo.optString("fid");
						name = jo.optString("name");
						rid = jo.optString("rid");
						price = jo.optString("price");
						intro = jo.optString("intro");

						pic = jo.optString("pic");
						if (!pic.contains("http:") && !pic.equals("")) {
							pic = Constants.APP_SERVICE + "/upload" + pic;
						}
						pic2 = jo.optString("pic2");
						if (!pic2.contains("http:") && !pic2.equals("")) {
							pic2 = Constants.APP_SERVICE + "/upload" + pic2;
						}
						pic3 = jo.optString("pic3");
						if (!pic3.contains("http:") && !pic3.equals("")) {
							pic3 = Constants.APP_SERVICE + "/upload" + pic3;
						}
						biggest_pic = jo.optString("biggest_pic");
						if (!biggest_pic.contains("http:")
								&& !biggest_pic.equals("")) {
							biggest_pic = Constants.APP_SERVICE + "/upload"
									+ biggest_pic;
						}
						big_pic = jo.optString("big_pic");
						if (!big_pic.contains("http:") && !big_pic.equals("")) {
							big_pic = Constants.APP_SERVICE + "/upload"
									+ big_pic;
						}
						small_pic = jo.optString("small_pic");
						if (!small_pic.contains("http:")
								&& !small_pic.equals("")) {
							small_pic = Constants.APP_SERVICE + "/upload"
									+ small_pic;
						}
						middle_pic = jo.optString("middle_pic");
						if (!middle_pic.contains("http:")
								&& !middle_pic.equals("")) {
							middle_pic = Constants.APP_SERVICE + "/upload"
									+ middle_pic;
						}

						uid = jo.optString("uid");
						like_num = jo.optString("like_num");
						want_num = jo.optString("want_num");
						comment_num = jo.optString("comment_num");
						deleted = jo.optString("deleted");
						source = jo.optString("source");
						c_date = jo.optString("c_date");
						m_date = jo.optString("m_date");
						source_name = jo.optString("source_name");

						longitude = jo.optDouble("longitude", 0);
						strDist = jo.optString("dist");
						latitude = jo.optDouble("latitude", 0);

						bean = new BeanFood();
						bean.setFid(fid);
						bean.setName(name);
						bean.setRid(rid);
						bean.setPrice(price);
						bean.setIntro(intro);
						bean.setPic(pic);
						bean.setPic2(pic2);
						bean.setPic3(pic3);
						bean.setUid(uid);
						bean.setLike_num(like_num);
						bean.setWant_num(want_num);
						bean.setComment_num(comment_num);
						bean.setDeleted(deleted);
						bean.setSource(source);
						bean.setC_date(c_date);
						bean.setM_date(m_date);
						bean.setBiggest_pic(biggest_pic);
						bean.setBig_pic(big_pic);
						bean.setSmall_pic(small_pic);
						bean.setMiddle_pic(middle_pic);
						bean.setSource_name(source_name);

						beanUser = getUserInfo(uid);
						if (beanUser != null) {
							bean.setuHead(beanUser.getPhoto_url());
							bean.setuName(beanUser.getName());
						}
						beanDinner = getDinnerInfo(rid);
						if (beanDinner != null) {
							bean.setrName(beanDinner.getName());
						}

						Location locDinner = new Location(
								LocationManager.NETWORK_PROVIDER);
						locDinner.setLatitude(latitude);
						locDinner.setLongitude(longitude);

						BeanLoc b = AppMsmk.getBeanLoc();
						float f = 0;
						if (b != null && b.getLng() != 0 && b.getLat() != 0
								&& latitude != 0 && longitude != 0) {
							f = b.getLoc().distanceTo(locDinner);
							f = Math.abs(f);
							f = (float) Math.rint(f);
						}
						bean.setDistance(f);

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get food list by rim
	 * 
	 * @param _lng
	 * @param _lat
	 * @param _range
	 * @param _num
	 * @return ArrayList<BeanFood>
	 */
	public ArrayList<BeanFood> getFoodListByRim(double _lng, double _lat,
			long _range, int _num) {
		InputStream is = null;
		ArrayList<BeanFood> arrList = null;
		BeanFood bean = null;
		String strResult = "";
		String fid = "", name = "", rid = "", price = "";
		String intro = "", pic = "", pic2 = "", pic3 = "";
		String uid = "", like_num = "", want_num = "";
		String comment_num = "", deleted = "", source = "";
		String c_date = "", m_date = "", biggest_pic = "";
		String big_pic = "", small_pic = "", source_name = "";
		String uName = "", uHeadUrl = "", dName = "", strLng = "";
		String strDist = "", strLat = "", middle_pic = "";
		double longitude = 0, latitude = 0;
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_FOOD_BY_RIM);
			strUrl.append("&long=");
			strUrl.append(String.valueOf(_lng));
			strUrl.append("&lati=");
			strUrl.append(String.valueOf(_lat));
			strUrl.append("&range=");
			strUrl.append(String.valueOf(_range));
			strUrl.append("&num=");
			strUrl.append(String.valueOf(_num));
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanFood>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						fid = jo.optString("fid");
						name = jo.optString("food_name");
						rid = jo.optString("rid");
						intro = jo.optString("food_intro");
						pic = jo.optString("food_pic ");
						uid = jo.optString("uid");
						like_num = jo.optString("like_num");
						want_num = jo.optString("want_num");
						comment_num = jo.optString("comment_num");
						biggest_pic = jo.optString("food_pic");
						big_pic = jo.optString("food_pic");
						small_pic = jo.optString("food_pic");
						middle_pic = jo.optString("food_pic");
						uName = jo.optString("user_name");
						uHeadUrl = jo.optString("avatar");
						dName = jo.optString("rest_name");
						longitude = jo.optDouble("rest_long", 0);
						strDist = jo.optString("dist");
						latitude = jo.optDouble("rest_lati", 0);

						bean = new BeanFood();
						bean.setFid(fid);
						bean.setName(name);
						bean.setRid(rid);
						bean.setPrice(price);
						bean.setIntro(intro);
						bean.setPic(pic);
						bean.setPic2(pic2);
						bean.setPic3(pic3);
						bean.setUid(uid);
						bean.setLike_num(like_num);
						bean.setWant_num(want_num);
						bean.setComment_num(comment_num);
						bean.setDeleted(deleted);
						bean.setSource(source);
						bean.setC_date(c_date);
						bean.setM_date(m_date);
						bean.setBiggest_pic(biggest_pic);
						bean.setBig_pic(big_pic);
						bean.setSmall_pic(small_pic);
						bean.setMiddle_pic(middle_pic);
						bean.setSource_name(source_name);
						bean.setuHead(uHeadUrl);
						bean.setuName(uName);
						bean.setrName(dName);
						bean.setLng(longitude);
						bean.setLat(latitude);

						Location locDinner = new Location(
								LocationManager.NETWORK_PROVIDER);
						locDinner.setLatitude(latitude);
						locDinner.setLongitude(longitude);

						BeanLoc b = AppMsmk.getBeanLoc();
						float f = 0;
						if (b != null && b.getLng() != 0 && b.getLat() != 0
								&& latitude != 0 && longitude != 0) {
							f = b.getLoc().distanceTo(locDinner);
							f = Math.abs(f);
							f = (float) Math.rint(f);
						}
						bean.setDistance(f);

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get dinner by rim
	 * 
	 * @param _lng
	 * @param _lat
	 * @param _range
	 * @param _key
	 * @return
	 */
	public ArrayList<BeanRimDinner> getDinnerByRim(double _lng, double _lat,
			long _range, String _key) {
		InputStream is = null;
		ArrayList<BeanRimDinner> arrList = null;
		BeanRimDinner bean = null;
		String strResult = "";
		String id = "", name = "", addr = "", tel = "";
		double lng = 0.0, lat = 0.0;
		long dist = 0;
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_DINNER_BY_RIM);
			strUrl.append("&long=");
			strUrl.append("" + _lng);
			strUrl.append("&lati=");
			strUrl.append("" + _lat);
			strUrl.append("&range=");
			strUrl.append("" + _range);
			strUrl.append("&key=");
			strUrl.append(URLEncoder.encode(_key, Constants.ENCODE));
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanRimDinner>();
					JSONObject joMain = new JSONObject(strResult);
					JSONArray jo = joMain.getJSONArray("items");
					for (int i = 0, max = jo.length(); i < max; i++) {
						JSONObject joSub = jo.getJSONObject(i);
						id = joSub.optString("rid");
						name = joSub.optString("name");
						addr = joSub.optString("addr");
						tel = joSub.optString("tel");
						dist = joSub.optLong("dist");
						lng = joSub.optDouble("long");
						lat = joSub.optDouble("lati");
						bean = new BeanRimDinner();
						bean.setRid(id);
						bean.setName(name);
						bean.setAddr(addr);
						bean.setTel(tel);
						bean.setDist(dist);
						bean.setLng(lng);
						bean.setLat(lat);
						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get the user was be attention wether
	 * 
	 * @param _fuid
	 * @return String
	 */
	public boolean isAttention(String _fuid) {
		InputStream is = null;
		boolean b = false;
		String strResult = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_IS_ATTENTION);
			strUrl.append("&uid=");
			strUrl.append(_fuid);
			strUrl.append("&fuid=");
			strUrl.append(lv.getUid());
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					if ("1".equals(strResult)) {
						b = true;
					} else {
						b = false;
					}
				}
			} else {
				b = false;
			}
		} catch (Exception e) {
			b = false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return b;
	}

	public ArrayList<BeanUserInfo> getRecommentdUser(String _uid) {
		InputStream is = null;
		ArrayList<BeanUserInfo> arrList = null;
		BeanUserInfo bean = null;
		String strResult = "";
		String uid = "", email = "", password = "";
		String name = "", name_py = "", name_py_short = "", gender = "";
		String city_id = "", blood_type = "", mobile = "";
		String star = "", decade = "", intro = "", invite_code = "";
		String face_folder = "", face_name = "", cdate = "";
		String mdate = "", login_days = "", deleted = "", fans_num = "";
		String follow_num = "", lastlogin = "", is_active = "", city = "";
		String birthcity = "", city_p = "", city_c = "", gender_name = "";
		String gender_ta = "", home_link = "", name_link = "", photo_30_url = "";
		String photo_60_url = "", photo_180_url = "", photo_url = "";
		String photo_80_url = "";

		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			// strUrl = new StringBuilder(Constants.API_USER_COMMENT);
			strUrl = new StringBuilder(Constants.API_RECOMMEND_USER);
			strUrl.append(_uid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanUserInfo>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						uid = jo.optString("uid");
						email = jo.optString("email");
						password = jo.optString("password");
						name = jo.optString("name");
						name_py = jo.optString("name_py");
						name_py_short = jo.optString("name_py_short");
						gender = jo.optString("gender");
						city_id = jo.optString("city_id");
						blood_type = jo.optString("blood_type");
						mobile = jo.optString("mobile");
						star = jo.optString("star");
						decade = jo.optString("decade");
						intro = jo.optString("intro");
						invite_code = jo.optString("invite_code");
						face_folder = jo.optString("face_folder");
						face_name = jo.optString("face_name");
						cdate = jo.optString("cdate");
						mdate = jo.optString("mdate");
						login_days = jo.optString("login_days");
						deleted = jo.optString("deleted");
						fans_num = jo.optString("fans_num");
						follow_num = jo.optString("follow_num");
						lastlogin = jo.optString("lastlogin");
						is_active = jo.optString("is_active");
						city = jo.optString("city");
						birthcity = jo.optString("birthcity");
						city_p = jo.optString("city_p");
						city_c = jo.optString("city_c");
						gender_name = jo.optString("gender_name");
						gender_ta = jo.optString("gender_ta");
						home_link = jo.optString("home_link");
						name_link = jo.optString("name_link");

						photo_30_url = jo.optString("avatar_30");
						if (!photo_30_url.contains("http:")
								&& !photo_30_url.equals("")) {
							photo_30_url = Constants.APP_SERVICE + "/upload"
									+ photo_30_url;
						}
						photo_60_url = jo.optString("avatar_60");
						if (!photo_60_url.contains("http:")
								&& !photo_60_url.equals("")) {
							photo_60_url = Constants.APP_SERVICE + "/upload"
									+ photo_60_url;
						}
						photo_80_url = jo.optString("avatar_80");
						if (!photo_80_url.contains("http:")
								&& !photo_80_url.equals("")) {
							photo_80_url = Constants.APP_SERVICE + "/upload"
									+ photo_80_url;
						}
						photo_180_url = jo.optString("avatar_180");
						if (!photo_180_url.contains("http:")
								&& !photo_180_url.equals("")) {
							photo_180_url = Constants.APP_SERVICE + "/upload"
									+ photo_180_url;
						}
						photo_url = jo.optString("avatar_180");
						if (!photo_url.contains("http:")
								&& !photo_url.equals("")) {
							photo_url = Constants.APP_SERVICE + "/upload"
									+ photo_url;
						}

						bean = new BeanUserInfo();
						bean.setUid(uid);
						bean.setEmail(email);
						bean.setPassword(password);
						bean.setName(name);
						bean.setName_py(name_py);
						bean.setName_py_short(name_py_short);
						bean.setGender(gender);
						bean.setCity_id(city_id);
						bean.setBlood_type(blood_type);
						bean.setMobile(mobile);
						bean.setStar(star);
						bean.setDecade(decade);
						bean.setIntro(intro);
						bean.setInvite_code(invite_code);
						bean.setFace_folder(face_folder);
						bean.setFace_name(face_name);
						bean.setCdate(cdate);
						bean.setMdate(mdate);
						bean.setLogin_days(login_days);
						bean.setDeleted(deleted);
						bean.setFans_num(fans_num);
						bean.setFollow_num(follow_num);
						bean.setLastlogin(lastlogin);
						bean.setIs_active(is_active);
						bean.setCity(city);
						bean.setBirthcity(birthcity);
						bean.setCity_p(city_p);
						bean.setCity_c(city_c);
						bean.setGender_name(gender_name);
						bean.setGender_ta(gender_ta);
						bean.setHome_link(home_link);
						bean.setName_link(name_link);
						bean.setPhoto_30_url(photo_30_url);
						bean.setPhoto_60_url(photo_60_url);
						bean.setPhoto_80_url(photo_80_url);
						bean.setPhoto_180_url(photo_180_url);
						bean.setPhoto_url(photo_url);

						bean.setAttention(isAttention(uid));

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	public ArrayList<BeanUserInfo> searchUser(String _key) {
		InputStream is = null;
		ArrayList<BeanUserInfo> arrList = null;
		BeanUserInfo bean = null;
		String strResult = "";
		String uid = "", email = "", password = "";
		String name = "", name_py = "", name_py_short = "", gender = "";
		String city_id = "", blood_type = "", mobile = "";
		String star = "", decade = "", intro = "", invite_code = "";
		String face_folder = "", face_name = "", cdate = "";
		String mdate = "", login_days = "", deleted = "", fans_num = "";
		String follow_num = "", lastlogin = "", is_active = "", city = "";
		String birthcity = "", city_p = "", city_c = "", gender_name = "";
		String gender_ta = "", home_link = "", name_link = "", photo_30_url = "";
		String photo_60_url = "", photo_180_url = "", photo_url = "";
		String photo_80_url = "";

		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_USER_SEARCH);
			strUrl.append(URLEncoder.encode(_key, Constants.ENCODE));
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanUserInfo>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						uid = jo.optString("uid");
						email = jo.optString("email");
						password = jo.optString("password");
						name = jo.optString("name");
						name_py = jo.optString("name_py");
						name_py_short = jo.optString("name_py_short");
						gender = jo.optString("gender");
						city_id = jo.optString("city_id");
						blood_type = jo.optString("blood_type");
						mobile = jo.optString("mobile");
						star = jo.optString("star");
						decade = jo.optString("decade");
						intro = jo.optString("intro");
						invite_code = jo.optString("invite_code");
						face_folder = jo.optString("face_folder");
						face_name = jo.optString("face_name");
						cdate = jo.optString("cdate");
						mdate = jo.optString("mdate");
						login_days = jo.optString("login_days");
						deleted = jo.optString("deleted");
						fans_num = jo.optString("fans_num");
						follow_num = jo.optString("follow_num");
						lastlogin = jo.optString("lastlogin");
						is_active = jo.optString("is_active");
						city = jo.optString("city");
						birthcity = jo.optString("birthcity");
						city_p = jo.optString("city_p");
						city_c = jo.optString("city_c");
						gender_name = jo.optString("gender_name");
						gender_ta = jo.optString("gender_ta");
						home_link = jo.optString("home_link");
						name_link = jo.optString("name_link");

						photo_30_url = jo.optString("avatar_30");
						if (!photo_30_url.contains("http:")
								&& !photo_30_url.equals("")) {
							photo_30_url = Constants.APP_SERVICE + "/upload"
									+ photo_30_url;
						}
						photo_60_url = jo.optString("avatar_60");
						if (!photo_60_url.contains("http:")
								&& !photo_60_url.equals("")) {
							photo_60_url = Constants.APP_SERVICE + "/upload"
									+ photo_60_url;
						}
						photo_80_url = jo.optString("avatar_80");
						if (!photo_80_url.contains("http:")
								&& !photo_80_url.equals("")) {
							photo_80_url = Constants.APP_SERVICE + "/upload"
									+ photo_80_url;
						}
						photo_180_url = jo.optString("avatar_180");
						if (!photo_180_url.contains("http:")
								&& !photo_180_url.equals("")) {
							photo_180_url = Constants.APP_SERVICE + "/upload"
									+ photo_180_url;
						}
						photo_url = jo.optString("photo_url");
						if (!photo_url.contains("http:")
								&& !photo_url.equals("")) {
							photo_url = Constants.APP_SERVICE + "/upload"
									+ photo_url;
						}

						bean = new BeanUserInfo();
						bean.setUid(uid);
						bean.setEmail(email);
						bean.setPassword(password);
						bean.setName(name);
						bean.setName_py(name_py);
						bean.setName_py_short(name_py_short);
						bean.setGender(gender);
						bean.setCity_id(city_id);
						bean.setBlood_type(blood_type);
						bean.setMobile(mobile);
						bean.setStar(star);
						bean.setDecade(decade);
						bean.setIntro(intro);
						bean.setInvite_code(invite_code);
						bean.setFace_folder(face_folder);
						bean.setFace_name(face_name);
						bean.setCdate(cdate);
						bean.setMdate(mdate);
						bean.setLogin_days(login_days);
						bean.setDeleted(deleted);
						bean.setFans_num(fans_num);
						bean.setFollow_num(follow_num);
						bean.setLastlogin(lastlogin);
						bean.setIs_active(is_active);
						bean.setCity(city);
						bean.setBirthcity(birthcity);
						bean.setCity_p(city_p);
						bean.setCity_c(city_c);
						bean.setGender_name(gender_name);
						bean.setGender_ta(gender_ta);
						bean.setHome_link(home_link);
						bean.setName_link(name_link);
						bean.setPhoto_30_url(photo_30_url);
						bean.setPhoto_60_url(photo_60_url);
						bean.setPhoto_80_url(photo_80_url);
						bean.setPhoto_180_url(photo_180_url);
						bean.setPhoto_url(photo_url);

						bean.setAttention(isAttention(uid));

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	/**
	 * get comment of user
	 * 
	 * @param _uid
	 * @return ArrayList<BeanComment>
	 */
	public ArrayList<BeanComment> getUserCommentReply(String _uid) {
		InputStream is = null;
		ArrayList<BeanComment> arrList = null;
		BeanComment bean = null;
		String strResult = "";
		// comment info
		String id = "", tid = "", content = "", a_uid = "";
		String rid = "", is_secret = "", status = "";
		String c_time = "", m_time = "", food_name = "";
		// user info
		String a_uname = "";
		String uid = "", name = "", photo_url = "", intro = "";
		String photo_30_url = "", photo_60_url = "", photo_180_url = "";

		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_USER_COMMENT);
			strUrl.append(_uid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanComment>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						// comment info
						id = jo.optString("id");
						tid = jo.optString("tid");
						content = jo.optString("content");
						a_uid = jo.optString("a_uid");
						rid = jo.optString("rid");
						is_secret = jo.optString("is_secret");
						status = jo.optString("status");
						c_time = jo.optString("c_time");
						m_time = jo.optString("m_time");
						uid = jo.optString("uid");
						food_name = jo.optString("food_name");
						// 评论人信息
						JSONObject joSub = jo.getJSONObject("a_user_info");
						uid = joSub.optString("uid");
						name = joSub.optString("name");
						intro = joSub.optString("intro");

						photo_30_url = joSub.optString("avatar_30");
						if (!photo_30_url.contains("http:")
								&& !photo_30_url.equals("")) {
							photo_30_url = Constants.APP_SERVICE + "/upload"
									+ photo_30_url;
						}
						photo_60_url = joSub.optString("avatar_60");
						if (!photo_60_url.contains("http:")
								&& !photo_60_url.equals("")) {
							photo_60_url = Constants.APP_SERVICE + "/upload"
									+ photo_60_url;
						}
						photo_180_url = joSub.optString("avatar_80");
						if (!photo_180_url.contains("http:")
								&& !photo_180_url.equals("")) {
							photo_180_url = Constants.APP_SERVICE + "/upload"
									+ photo_180_url;
						}
						photo_url = joSub.optString("avatar_180");
						if (!photo_url.contains("http:")
								&& !photo_url.equals("")) {
							photo_url = Constants.APP_SERVICE + "/upload"
									+ photo_url;
						}

						// 接收评论 人信息
						a_uid = "";
						a_uname = "";
						joSub = jo.optJSONObject("r_user_info");
						if (joSub != null) {
							a_uid = joSub.optString("uid");
							a_uname = joSub.optString("name");
						}

						bean = new BeanComment();
						bean.setId(id);
						bean.setTid(tid);
						bean.setContent(content);
						bean.setFood_name(food_name);

						bean.setA_uid(a_uid);
						bean.setA_uName(a_uname);

						bean.setRid(rid);
						bean.setIs_secret(is_secret);
						bean.setStatus(status);
						bean.setC_time(c_time);
						bean.setM_time(m_time);

						bean.setUid(uid);
						bean.setName(name);

						bean.setIntro(intro);
						bean.setPhoto_url(photo_60_url);
						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	public ArrayList<BeanMessage> getUserMessage(String _uid) {
		InputStream is = null;
		ArrayList<BeanMessage> arrList = null;
		BeanMessage bean = null;
		String strResult = "";
		String strId = "", strSUid = "", strRUid = "", strUid = "";
		String strContents = "", strCDate = "", strStatus = "";
		String strSUName = "", strRUName = "", strAvatar60 = "";
		String strUName = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_USER_Message);
			strUrl.append(_uid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanMessage>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						JSONObject joMsg = jo.optJSONObject("message");
						strId = joMsg.optString("id");
						strSUid = joMsg.optString("suid");
						strRUid = joMsg.optString("ruid");
						strContents = joMsg.optString("contents");
						strCDate = joMsg.optString("c_time");
						strStatus = joMsg.optString("status");

						JSONObject joUser = jo.optJSONObject("suserinfo");
						strUid = joUser.optString("uid");
						if (!strUid.equals(lv.getUid())) {
							strAvatar60 = joUser.optString("avatar_60");
						}
						strUName = joUser.optString("name");

						joUser = jo.optJSONObject("ruserinfo");
						strUid = joUser.optString("uid");
						if (!strUid.equals(lv.getUid())) {
							strAvatar60 = joUser.optString("avatar_60");
						}

						bean = new BeanMessage();
						bean.setuName(strUName);
						if (strUid.equals(strSUid)) {
							strSUName = strUName;
						} else {
							strRUName = strUName;
						}
						if (strSUid.equals(lv.getUid())) {
							strSUName = "我";
						}
						if (strRUid.equals(lv.getUid())) {
							strRUName = "我";
						}
						if (strUid.equals(lv.getUid())) {
							strUName = "我";
						}
						bean.setId(strId);
						bean.setRuid(strRUid);
						bean.setRuName(strRUName);
						bean.setSuid(strSUid);
						bean.setSuName(strSUName);
						bean.setContents(strContents);
						bean.setC_date(strCDate);
						bean.setStatus(strStatus);
						bean.setuHeadUrl(strAvatar60);
						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	public ArrayList<BeanMessage> getMessageDialog(String _uid) {
		InputStream is = null;
		ArrayList<BeanMessage> arrList = null;
		BeanMessage bean = null;
		String strResult = "";
		String strId = "", strSUid = "";
		String strSUHead = "", strRUHead = "";
		String strContents = "", strCDate = "";
		String strSUName = "", strRUName = "";
		String strRUid = "", strStatus = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_MESSAGE_DIALOG);
			strUrl.append("&suid=");
			strUrl.append(lv.getUid());
			strUrl.append("&ruid=");
			strUrl.append(_uid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanMessage>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						strId = jo.optString("id");
						strContents = jo.optString("contents");
						strCDate = jo.optString("c_time");
						strStatus = jo.optString("status");

						JSONObject joSUser = jo.optJSONObject("suserinfo");
						strSUid = joSUser.optString("uid");
						if (strSUid.equals(lv.getUid())) {
							strSUName = "我";
						} else {
							strSUName = joSUser.optString("name");
						}
						strSUName = joSUser.optString("name");
						strSUHead = joSUser.optString("avatar_60");

						JSONObject joRUser = jo.optJSONObject("ruserinfo");
						strRUid = joRUser.optString("uid");
						if (strRUid.equals(lv.getUid())) {
							strRUName = "我";
						} else {
							strRUName = joRUser.optString("name");
						}
						strRUHead = joRUser.optString("avatar_60");

						bean = new BeanMessage();
						bean.setId(strId);
						bean.setRuid(strRUid);
						bean.setRuName(strRUName);
						bean.setRuHeadUrl(strRUHead);
						bean.setSuid(strSUid);
						bean.setSuName(strSUName);
						bean.setSuHeadUrl(strSUHead);
						bean.setContents(strContents);
						bean.setC_date(strCDate);
						bean.setStatus(strStatus);
						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	public ArrayList<BeanMessage> getCommentDialog(String _auid, String _tid) {
		InputStream is = null;
		ArrayList<BeanMessage> arrList = null;
		BeanMessage bean = null;
		String strResult = "";
		String strId = "", strSUid = "";
		String strSUHead = "", strRUHead = "";
		String strContents = "", strCDate = "";
		String strSUName = "", strRUName = "";
		String strRUid = "", strStatus = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_COMMENT_DIALOG);
			strUrl.append("&tid=");
			strUrl.append(_tid);
			strUrl.append("&uid=");
			strUrl.append(lv.getUid());
			strUrl.append("&a_uid=");
			strUrl.append(_auid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanMessage>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						strId = jo.optString("id");
						strContents = jo.optString("content");
						strCDate = jo.optString("c_time");
						strStatus = jo.optString("status");

						JSONObject joSUser = jo.optJSONObject("a_user_info");
						if (joSUser != null) {
							strSUid = joSUser.optString("uid");
							if (strSUid.equals(lv.getUid())) {
								strSUName = "我";
							} else {
								strSUName = joSUser.optString("name");
							}
							strSUName = joSUser.optString("name");
							strSUHead = joSUser.optString("avatar_60");
						}
						JSONObject joRUser = jo.optJSONObject("r_user_info");
						if (joRUser != null) {
							strRUid = joRUser.optString("uid");
							if (strRUid.equals(lv.getUid())) {
								strRUName = "我";
							} else {
								strRUName = joRUser.optString("name");
							}
							strRUHead = joRUser.optString("avatar_60");
						}
						bean = new BeanMessage();
						bean.setId(strId);
						bean.setRuid(strRUid);
						bean.setRuName(strRUName);
						bean.setRuHeadUrl(strRUHead);
						bean.setSuid(strSUid);
						bean.setSuName(strSUName);
						bean.setSuHeadUrl(strSUHead);
						bean.setContents(strContents);
						bean.setC_date(strCDate);
						bean.setStatus(strStatus);
						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	public ArrayList<BeanTagNum> getTagNum(String _uid) {
		InputStream is = null;
		ArrayList<BeanTagNum> arrList = null;
		BeanTagNum bean = null;
		String strResult = "";
		String strUid = "", strFid = "";
		String strTid = "", strName = "", strNum = "";

		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_TAG_NUM);
			strUrl.append(_uid);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanTagNum>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						strUid = jo.optString("uid");
						strFid = jo.optString("fid");
						strTid = jo.optString("tid");
						strName = jo.optString("name");
						strNum = jo.optString("num");

						bean = new BeanTagNum();
						bean.setFid(strFid);
						bean.setTid(strTid);
						bean.setUid(strUid);
						bean.setName(strName);
						bean.setNum(strNum);
						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

	public ArrayList<BeanUserInfo> getDaren() {
		InputStream is = null;
		ArrayList<BeanUserInfo> arrList = null;
		BeanUserInfo bean = null;
		String strResult = "";
		String uid = "", email = "", password = "";
		String name = "", name_py = "", name_py_short = "", gender = "";
		String city_id = "", blood_type = "", mobile = "";
		String star = "", decade = "", intro = "", invite_code = "";
		String face_folder = "", face_name = "", cdate = "";
		String mdate = "", login_days = "", deleted = "", fans_num = "";
		String follow_num = "", lastlogin = "", is_active = "", city = "";
		String birthcity = "", city_p = "", city_c = "", gender_name = "";
		String gender_ta = "", home_link = "", name_link = "", photo_30_url = "";
		String photo_60_url = "", photo_180_url = "", photo_url = "";
		String photo_80_url = "", identity = "";
		String photoImg = "";

		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_GET_DAREN);
			strUrl.append("&city=");
            strUrl.append(lv.getQuanZiCityId());
            strUrl.append("&login_user_id");
            strUrl.append(lv.getUid());
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)) {
					arrList = new ArrayList<BeanUserInfo>();
					JSONArray ja = new JSONArray(strResult);
					for (int i = 0, max = ja.length(); i < max; i++) {
						JSONObject jo = ja.getJSONObject(i);
						uid = jo.optString("uid");
						email = jo.optString("email");
						password = jo.optString("password");
						name = jo.optString("name");
						name_py = jo.optString("name_py");
						name_py_short = jo.optString("name_py_short");
						gender = jo.optString("gender");
						city_id = jo.optString("city_id");
						blood_type = jo.optString("blood_type");
						mobile = jo.optString("mobile");
						star = jo.optString("star");
						decade = jo.optString("decade");
						intro = jo.optString("intro");
						invite_code = jo.optString("invite_code");
						face_folder = jo.optString("face_folder");
						face_name = jo.optString("face_name");
						cdate = jo.optString("cdate");
						mdate = jo.optString("mdate");
						login_days = jo.optString("login_days");
						deleted = jo.optString("deleted");
						fans_num = jo.optString("fans_num");
						follow_num = jo.optString("follow_num");
						lastlogin = jo.optString("lastlogin");
						is_active = jo.optString("is_active");
						city = jo.optString("city_name");
						birthcity = jo.optString("birthcity");
						city_p = jo.optString("city_p");
						city_c = jo.optString("city_c");
						gender_name = jo.optString("gender_name");
						gender_ta = jo.optString("gender_ta");
						home_link = jo.optString("home_link");
						name_link = jo.optString("name_link");

						photo_30_url = jo.optString("avatar_30");
						if (!photo_30_url.contains("http:")
								&& !photo_30_url.equals("")) {
							photo_30_url = Constants.APP_SERVICE + "/upload"
									+ photo_30_url;
						}
						photo_60_url = jo.optString("avatar_60");
						if (!photo_60_url.contains("http:")
								&& !photo_60_url.equals("")) {
							photo_60_url = Constants.APP_SERVICE + "/upload"
									+ photo_60_url;
						}
						photo_80_url = jo.optString("avatar_80");
						if (!photo_80_url.contains("http:")
								&& !photo_80_url.equals("")) {
							photo_80_url = Constants.APP_SERVICE + "/upload"
									+ photo_80_url;
						}
						photo_180_url = jo.optString("avatar_180");
						if (!photo_180_url.contains("http:")
								&& !photo_180_url.equals("")) {
							photo_180_url = Constants.APP_SERVICE + "/upload"
									+ photo_180_url;
						}
						photo_url = jo.optString("avatar_180");
						if (!photo_url.contains("http:")
								&& !photo_url.equals("")) {
							photo_url = Constants.APP_SERVICE + "/upload"
									+ photo_url;
						}
						identity = jo.optString("identity");

						bean = new BeanUserInfo();
						bean.setUid(uid);
						bean.setEmail(email);
						bean.setPassword(password);
						bean.setName(name);
						bean.setName_py(name_py);
						bean.setName_py_short(name_py_short);
						bean.setGender(gender);
						bean.setCity_id(city_id);
						bean.setBlood_type(blood_type);
						bean.setMobile(mobile);
						bean.setStar(star);
						bean.setDecade(decade);
						bean.setIntro(intro);
						bean.setInvite_code(invite_code);
						bean.setFace_folder(face_folder);
						bean.setFace_name(face_name);
						bean.setCdate(cdate);
						bean.setMdate(mdate);
						bean.setLogin_days(login_days);
						bean.setDeleted(deleted);
						bean.setFans_num(fans_num);
						bean.setFollow_num(follow_num);
						bean.setLastlogin(lastlogin);
						bean.setIs_active(is_active);
						bean.setCity(city);
						bean.setBirthcity(birthcity);
						bean.setCity_p(city_p);
						bean.setCity_c(city_c);
						bean.setGender_name(gender_name);
						bean.setGender_ta(gender_ta);
						bean.setHome_link(home_link);
						bean.setName_link(name_link);
						bean.setPhoto_30_url(photo_30_url);
						bean.setPhoto_60_url(photo_60_url);
						bean.setPhoto_180_url(photo_180_url);
						bean.setPhoto_url(photo_url);
						bean.setIdentity(identity);

						bean.setAttention(isAttention(uid));

						JSONArray jaimg = jo.optJSONArray("pub_food");
						for (int c = 0, maxC = jaimg.length(); c < maxC; c++) {
							JSONObject joimg = jaimg.getJSONObject(c);
							photoImg = joimg.optString("small_pic");
							switch (c) {
							case 0:
								bean.setPhoto1(photoImg);
								break;
							case 1:
								bean.setPhoto2(photoImg);
								break;
							case 2:
								bean.setPhoto3(photoImg);
								break;
							}
						}

						arrList.add(bean);
					}
				}
			} else {
				arrList = null;
			}
		} catch (Exception e) {
			arrList = null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			http.disconnect();
		}
		return arrList;
	}

}
