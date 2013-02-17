package com.meishimeike.Bll;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import weibo4andriod.User;
import weibo4andriod.Weibo;
import weibo4andriod.androidexamples.OAuthConstant;
import android.content.Context;
import android.util.Log;

import com.meishimeike.R;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.Constants;
import com.meishimeike.Utils.LocalVariable;

/**
 * @author :LiuQiang
 * @version ：2012-5-17 class desc
 */
public class BllSend {
	private LocalVariable lv = null;
	private Commons coms = null;
	private Context mContext = null;

	public BllSend(Context context) {
		lv = new LocalVariable(context);
		coms = new Commons();
		mContext = context;
	}

	/**
	 * register
	 * 
	 * @param _email
	 * @param _name
	 * @param _pwd
	 * @param _confirm
	 * @return String
	 */
	public String register(String _email, String _name, String _pwd,
			String _confirm) {
		String str = "", strResult = "";
		String uid = "", name = "";
		String errno = "", error = "";
		StringBuilder strUrl, strParams;
		InputStreamReader in = null;
		HttpURLConnection http = null;
		DataOutputStream out = null;
		try {
			strParams = new StringBuilder("");
			strParams.append("&_a=register");
			strParams.append("&email=");
			strParams.append(URLEncoder.encode(_email, Constants.ENCODE));
			strParams.append("&name=");
			strParams.append(URLEncoder.encode(_name, Constants.ENCODE));
			strParams.append("&password=");
			strParams.append(URLEncoder.encode(_pwd, Constants.ENCODE));
			strParams.append("&confirmpass=");
			strParams.append(URLEncoder.encode(_confirm, Constants.ENCODE));
			strParams.append("&src=3");
			strParams.append("&ip=");
			strParams.append("&photo=");

			strUrl = new StringBuilder(Constants.API_USER);
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setDoOutput(true);
			http.setDoInput(true);
			http.setRequestMethod("POST");
			http.setConnectTimeout(40000);
			http.setUseCaches(false);
			http.setInstanceFollowRedirects(true);
			http.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			http.connect();

			out = new DataOutputStream(http.getOutputStream());
			out.writeBytes(strParams.toString());

			int code = http.getResponseCode();
			if (code == 200) {
				InputStream is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				JSONObject jo = new JSONObject(strResult);
				errno = jo.optString("errno");
				error = jo.optString("error");
				if (errno.equals("0")) {
					str = "1#注册成功！";
					uid = jo.optString("uid");
					name = jo.optString("name");
					lv.setUid(uid);
					lv.setName(name);
					lv.setLoginName(_email);
				} else {
					str = "0#" + error + "!";
				}
			} else {
				str = "0#服务器连接失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#注册失败，请稍候重试！";
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (in != null) {
					in.close();
				}
				http.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * login
	 * 
	 * @param _email
	 * @param _pwd
	 * @return String
	 */
	public String login(String _email, String _pwd) {
		String str = "", strResult = "";
		String uid = "", name = "", photo = "";
		String errno = "", error = "";
		StringBuilder strUrl, strParams;
		InputStreamReader in = null;
		HttpURLConnection http = null;
		DataOutputStream out = null;
		try {
			strParams = new StringBuilder("");
			strParams.append("&_a=login");
			strParams.append("&email=");
			strParams.append(URLEncoder.encode(_email, Constants.ENCODE));
			strParams.append("&password=");
			strParams.append(URLEncoder.encode(_pwd, Constants.ENCODE));
			strParams.append("&ip=");
			strParams.append("");

			strUrl = new StringBuilder(Constants.API_USER);
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setDoOutput(true);
			http.setDoInput(true);
			http.setRequestMethod("POST");
			http.setConnectTimeout(40000);
			http.setUseCaches(false);
			http.setInstanceFollowRedirects(true);
			http.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			http.connect();

			out = new DataOutputStream(http.getOutputStream());
			out.writeBytes(strParams.toString());

			int code = http.getResponseCode();
			if (code == 200) {
				InputStream is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				JSONObject jo = new JSONObject(strResult);
				errno = jo.optString("errno");
				error = jo.optString("error");
				if (errno.equals("0")) {
					str = "1#登录成功！";
					uid = jo.optString("uid");
					name = jo.optString("name");
					photo = jo.optString("photo");
					lv.setUid(uid);
					lv.setName(name);
					lv.setIsLogin(true);
					lv.setLoginName(_email);
					lv.setHeadUrl(photo);
				} else {
					str = "0#" + error + "!";
				}
			} else {
				str = "0#服务器连接失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#登录失败，请稍候重试！";
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (in != null) {
					in.close();
				}
				http.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * modify User info
	 * 
	 * @param _name
	 * @param _gender
	 * @param _intro
	 * @param _cityId
	 * @return String
	 */
	public String modifyUserInfo(String _name, String _gender, String _intro,
			String _cityId, String _tag) {
		String str = "", strResult = "";
		StringBuilder strUrl, strParams;
		InputStreamReader in = null;
		HttpURLConnection http = null;
		DataOutputStream out = null;
		try {
			strParams = new StringBuilder("");
			strParams.append("&_a=edit");
			strParams.append("&uid=");
			strParams.append(lv.getUid());
			strParams.append("&name=");
			strParams.append(URLEncoder.encode(_name, Constants.ENCODE));
			strParams.append("&gender=");
			strParams.append(URLEncoder.encode(_gender, Constants.ENCODE));
			strParams.append("&intro=");
			strParams.append(URLEncoder.encode(_intro, Constants.ENCODE));
			strParams.append("&city_id=");
			strParams.append(_cityId);
			strParams.append("&tag=");
			strParams.append(_tag);
			strParams.append("&city=");
			strParams.append(lv.getQuanZiCityId());
			strParams.append("&login_user_id");
			strParams.append(lv.getUid());

			strUrl = new StringBuilder(Constants.API_USER);
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setDoOutput(true);
			http.setDoInput(true);
			http.setRequestMethod("POST");
			http.setConnectTimeout(40000);
			http.setUseCaches(false);
			http.setInstanceFollowRedirects(true);
			http.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			http.connect();

			out = new DataOutputStream(http.getOutputStream());
			out.writeBytes(strParams.toString());

			int code = http.getResponseCode();
			if (code == 200) {
				InputStream is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult)
						&& strResult.contains("\"errno\":\"0\"")) {
					str = "1#用户信息修改成功！";
				} else {
					str = "0#" + "用户信息修改失败，请稍候重试！" + "!";
				}
			} else {
				str = "0#服务器连接失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#用户信息修改失败，请稍候重试！";
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (in != null) {
					in.close();
				}
				http.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * like food
	 * 
	 * @param _fid
	 * @return String
	 */
	public String likeFood(String _fid) {
		InputStream is = null;
		String str = "", strResult = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_LIKE_FOOD);
			strUrl.append("&uid=");
			strUrl.append(lv.getUid());
			strUrl.append("&fid=");
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
				if (!"".equals(strResult) && strResult.contains("true")) {
					str = "1#已添加喜欢！";
				} else {
					str = "0#已添加过喜欢！";
				}
			} else {
				str = "0#添加喜欢失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#添加喜欢失败，请稍候重试！";
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
		return str;
	}

	/**
	 * want food
	 * 
	 * @param _fid
	 * @return String
	 */
	public String wantFood(String _fid) {
		InputStream is = null;
		String str = "", strResult = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_WANT_FOOD);
			strUrl.append("&uid=");
			strUrl.append(lv.getUid());
			strUrl.append("&fid=");
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
				if (!"".equals(strResult) && strResult.contains("true")) {
					str = "1#已添加收集！";
				} else {
					str = "0#已添加过收集！";
				}
			} else {
				str = "0#添加收集失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#添加收集失败，请稍候重试！";
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
		return str;
	}

	/**
	 * attention user
	 * 
	 * @param _fuid
	 * @return String
	 */
	public String Attention(String _fuid) {
		InputStream is = null;
		String str = "", strResult = "";
		@SuppressWarnings("unused")
		String status = "", desc = "", strData = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_USER_ATTENTION);
			strUrl.append("&uid=");
			strUrl.append(lv.getUid());
			strUrl.append("&fuid=");
			strUrl.append(_fuid);
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
					status = jo.optString("status");
					desc = jo.optString("desc");
					strData = jo.optString("data");
					if ("false".equals(status)) {
						str = "0#" + desc + "！";
					} else {
						str = "1#" + "已关注！";
					}
				}
			} else {
				str = "0#服务器连接失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#获取失败，请稍候重试！";
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
		return str;
	}

	/**
	 * 
	 * @author :LiuQiang
	 * @version ：2012-7-22
	 * @param _fid
	 *            菜品ID
	 * @param _auid
	 *            菜品发布人的ID
	 * @param _ruid
	 *            接收评论的用户ID
	 * @param _rid
	 *            需要回复的评论ID
	 * @param _content
	 *            评论内容
	 * @return
	 */
	public String comment(String _fid, String _auid, String _ruid, String _rid,
			String _content) {
		InputStream is = null;
		String str = "", strResult = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_COMMENT);
			strUrl.append("&uid=");
			strUrl.append(lv.getUid());
			strUrl.append("&tid=");
			strUrl.append(_fid);
			strUrl.append("&a_uid=");
			strUrl.append(_auid);
			strUrl.append("&ruid=");
			strUrl.append(_ruid);
			strUrl.append("&rid=");
			strUrl.append(_rid);
			strUrl.append("&content=");
			strUrl.append((URLEncoder.encode(_content, Constants.ENCODE)));
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
				if (!"".equals(strResult) && strResult.contains("false")) {
					str = "0#评论失败！";
				} else {
					str = "1#评论成功！";
				}
			} else {
				str = "0#评论失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#添评论失败，请稍候重试！";
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
		return str;
	}

	public String sendFood(String _desc, String _price) {
		String str = "", strResult = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.API_SEND_FOOD);
			MultipartEntity mulentity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			// 不同的服务器要求不用有得服务器可加编码如
			// MultipartEntity
			// mulentity = new
			// MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName(HTTP.UTF_8));
			// 有的服务器加编码后会返回400错误，只要把编码去掉即把头文件Content-Type中的charset=UTF-8去掉就行了
			mulentity.addPart("_a", new StringBody("add"));
			// 中文最后加编码 如new StringBody(key,Charset.forName(HTTP.UTF_8))
			mulentity.addPart("uid", new StringBody(lv.getUid()));
			mulentity.addPart("rid", new StringBody(lv.getTempDinnerId()));
			mulentity.addPart("price", new StringBody(_price));
			mulentity.addPart("name", new StringBody(lv.getTempFoodName(),
					Charset.forName(HTTP.UTF_8)));
			mulentity.addPart("intro",
					new StringBody(_desc, Charset.forName(HTTP.UTF_8)));
			mulentity.addPart("name", new StringBody(lv.getTempFoodName(),
					Charset.forName(HTTP.UTF_8)));
			mulentity.addPart("src", new StringBody("3"));
			mulentity.addPart("&city=", new StringBody(lv.getQuanZiCityId()));
			mulentity.addPart("&login_user_id=", new StringBody(lv.getUid()));

			// 添加图片表单数据
			String tempPath = "";
			String path = lv.getTempPic1();
			FileBody filebody = null;
			File file = null;

			path = lv.getTempPic1();
			tempPath = coms.saveNewPic(path);
			path = "".equals(tempPath) ? path : tempPath;
			file = new File(path);
			if (file != null && file.isFile()) {
				filebody = new FileBody(file);
				mulentity.addPart("pic", filebody);
			}

			path = lv.getTempPic2();
			tempPath = coms.saveNewPic(path);
			path = "".equals(tempPath) ? path : tempPath;
			file = new File(path);
			if (file != null && file.isFile()) {
				filebody = new FileBody(file);
				mulentity.addPart("pic2", filebody);
			}

			path = lv.getTempPic3();
			tempPath = coms.saveNewPic(path);
			path = "".equals(tempPath) ? path : tempPath;
			file = new File(path);
			if (file != null && file.isFile()) {
				filebody = new FileBody(file);
				mulentity.addPart("pic3", filebody);
			}
			httpPost.setEntity(mulentity);
			HttpResponse response = httpclient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(response.getEntity());
				if (!"".equals(strResult) && strResult.contains("false")) {
					str = "0#发布失败！";
				} else {
					str = "1#发布成功！";
				}
			} else {
				str = "0#发布失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#发布失败，请确稍候重试！";
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * recommend person
	 * 
	 * @param _email
	 * @param _pwd
	 * @return
	 */
	public String invitePerson(String _email, String _sign, String _phone) {
		InputStream is = null;
		String str = "", strResult = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_INVIT);
			strUrl.append("&email=");
			strUrl.append((URLEncoder.encode(_email, Constants.ENCODE)));
			strUrl.append("&name=");
			strUrl.append((URLEncoder.encode(_sign, Constants.ENCODE)));
			strUrl.append("&phone=");
			strUrl.append((URLEncoder.encode(_phone, Constants.ENCODE)));
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
				if (!"".equals(strResult) && strResult.contains("\"ok\"")) {
					str = "1#邀请成功！";
				} else {
					str = "0#邀请失败！";
				}
			} else {
				str = "0#邀请失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#邀请失败，请稍候重试！";
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
		return str;
	}

	/**
	 * modify user tag
	 * 
	 * @param _tags
	 *            1|2|4
	 * @return
	 */
	public String modifyUserTag(String _tags) {
		InputStream is = null;
		String str = "", strResult = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_USER_TAG_EDIT);
			strUrl.append("&uid=");
			strUrl.append((URLEncoder.encode(lv.getUid(), Constants.ENCODE)));
			strUrl.append("&tag=");// 1|3
			strUrl.append((URLEncoder.encode(_tags, Constants.ENCODE)));
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
				if (!"".equals(strResult) && strResult.contains("\"succ\"")) {
					str = "1#Tag修改成功！";
				} else {
					str = "0#Tag修改失败！";
				}
			} else {
				str = "0#Tag修改失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#Tag修改失败，请稍候重试！";
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
		return str;
	}

	/**
	 * modify user password
	 * 
	 * @param _tags
	 *            1|2|4
	 * @return
	 */
	public String modifyUserPassword(String _oldPwd, String _newPwd) {
		InputStream is = null;
		String str = "", strResult = "";
		String strStatus = "", strDesc = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_USER_PWD_EDIT);
			strUrl.append("&uid=");
			strUrl.append((URLEncoder.encode(lv.getUid(), Constants.ENCODE)));
			strUrl.append("&old_pwd=");
			strUrl.append((URLEncoder.encode(_oldPwd, Constants.ENCODE)));
			strUrl.append("&new_pwd=");
			strUrl.append((URLEncoder.encode(_newPwd, Constants.ENCODE)));
			strUrl.append("&new_pwd_confirm=");
			strUrl.append((URLEncoder.encode(_newPwd, Constants.ENCODE)));
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
					strStatus = jo.optString("status");
					strDesc = jo.optString("desc");
					// strData = jo.optString("data");
					if (strStatus.equals("true")) {
						str = "1#" + strDesc + "！";
					} else {
						str = "0#" + strDesc + "！";
					}
				}
			} else {
				str = "0#修改失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#修改失败，请稍候重试！";
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
		return str;
	}

	public String sendMsg(String _sUid, String _rUid, String _content) {
		InputStream is = null;
		String str = "", strResult = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_USER_MSG_SEND);
			strUrl.append("&suid=");
			strUrl.append((URLEncoder.encode(_sUid, Constants.ENCODE)));
			strUrl.append("&ruid=");
			strUrl.append((URLEncoder.encode(_rUid, Constants.ENCODE)));
			strUrl.append("&contents=");
			strUrl.append((URLEncoder.encode(_content, Constants.ENCODE)));
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
				if (!"".equals(strResult) && strResult.contains("succ")) {
					str = "1#" + "发送成功" + "！";
				} else {
					str = "0#" + "发送失败" + "！";
				}
			} else {
				str = "0#发送失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#发送失败，请稍候重试！";
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
		return str;
	}

	public String modifyHeadImg(String _headPath) {
		String str = "", strResult = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.API_USER_HEAD_EDIT);
			MultipartEntity mulentity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			// 不同的服务器要求不用有得服务器可加编码如
			// MultipartEntity
			// mulentity = new
			// MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName(HTTP.UTF_8));
			// 有的服务器加编码后会返回400错误，只要把编码去掉即把头文件Content-Type中的charset=UTF-8去掉就行了
			mulentity.addPart("_a", new StringBody("avatar"));
			// 中文最后加编码 如new StringBody(key,Charset.forName(HTTP.UTF_8))
			mulentity.addPart("uid", new StringBody(lv.getUid()));
			mulentity.addPart("&city=", new StringBody(lv.getQuanZiCityId()));
			mulentity.addPart("&login_user_id=", new StringBody(lv.getUid()));

			// 添加图片表单数据
			FileBody filebody = null;
			File file = null;

			file = new File(_headPath);
			if (file != null && file.isFile()) {
				filebody = new FileBody(file);
				mulentity.addPart("avatar", filebody);
			}

			httpPost.setEntity(mulentity);
			HttpResponse response = httpclient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(response.getEntity());
				if (!"".equals(strResult) && strResult.contains("false")) {
					str = "0#头像设置失败！";
				} else {
					str = "1#头像设置成功！";
				}
			} else {
				str = "0#头像设置失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#头像设置失败，请确稍候重试！";
			e.printStackTrace();
		}
		return str;
	}

	public String findPwd(String _email) {
		InputStream is = null;
		String str = "", strResult = "";
		StringBuilder strUrl;
		HttpURLConnection http = null;
		try {
			strUrl = new StringBuilder(Constants.API_FIND_PWD);
			strUrl.append((URLEncoder.encode(_email, Constants.ENCODE)));
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
				if (!"".equals(strResult) && strResult.contains("false")) {
					str = "0#操作失败！";
				} else {
					str = "1#操作成功，请查收邮件！";
				}
			} else {
				str = "0#操作失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#操作失败，请稍候重试！";
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
		return str;
	}

	public String checkWUid(String _wUid, String _wToken, String _wSecret) {
		String str = "", strResult = "";
		String uid = "", name = "", photo = "";
		String email = "";
		StringBuilder strUrl;
		InputStreamReader in = null;
		HttpURLConnection http = null;
		DataOutputStream out = null;
		try {
			strUrl = new StringBuilder(Constants.API_CHECK_WUID);
			strUrl.append((URLEncoder.encode(_wUid, Constants.ENCODE)));
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
				InputStream is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if ("".equals(strResult) || strResult.contains("false")) {
					// 注册
					str = registerWeibo(_wUid, _wToken, _wSecret);
					if (str.split("#")[0].equals("1")) {
						str = "1#微博登录成功！";
					} else {
						str = "0#微博登录失败！";
					}
				} else {
					JSONObject jo = new JSONObject(strResult);
					uid = jo.optString("uid");
					name = jo.optString("name");
					email = jo.optString("email");
					photo = jo.optString("photo");
					lv.setUid(uid);
					lv.setName(name);
					lv.setIsLogin(true);
					lv.setLoginName(email);
					lv.setHeadUrl(photo);
					str = "1#微博登录成功！";
				}
			} else {
				str = "0#服务器连接失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#登录失败，请稍候重试！";
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (in != null) {
					in.close();
				}
				http.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	public String registerWeibo(String _wUid, String _wToken, String _wSecret) {
		String str = "", strResult = "";
		String uid = "", name = "";
		String errno = "", error = "";
		StringBuilder strUrl;
		InputStreamReader in = null;
		HttpURLConnection http = null;
		DataOutputStream out = null;
		try {
			System.setProperty("weibo4j.oauth.consumerKey", mContext
					.getResources().getString(R.string.app_sina_consumer_key));
			System.setProperty("weibo4j.oauth.consumerSecret", mContext
					.getResources()
					.getString(R.string.app_sina_consumer_secret));
			Weibo weibo = OAuthConstant.getInstance().getWeibo();
			weibo.setToken(_wToken, _wSecret);
			User user = weibo.showUser(_wUid);
			name = user.getName();
			strUrl = new StringBuilder(Constants.API_WEIBO_REGISTER);
			strUrl.append("&tuid=");
			strUrl.append((URLEncoder.encode(_wUid, Constants.ENCODE)));
			strUrl.append("&name=");
			strUrl.append((URLEncoder.encode(name, Constants.ENCODE)));
			strUrl.append("&email=");
			strUrl.append((URLEncoder.encode(_wUid + "@sinaweibo.com",
					Constants.ENCODE)));
			strUrl.append("&password=");
			strUrl.append((URLEncoder.encode(_wUid, Constants.ENCODE)));
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "text/html;charset=utf-8");
			http.connect();
			int code = http.getResponseCode();
			if (code == 200) {
				InputStream is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				JSONObject jo = new JSONObject(strResult);
				errno = jo.optString("errno");
				error = jo.optString("error");
				if (errno.equals("0")) {
					str = "1#微博站内注册成功！";
					uid = jo.optString("uid");
					name = jo.optString("name");
					lv.setUid(uid);
					lv.setName(name);
					lv.setIsLogin(true);
				} else {
					str = "0#" + error + "!";
				}
			} else {
				str = "0#服务器连接失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#微博站内注册失败，请稍候重试！";
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (in != null) {
					in.close();
				}
				http.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * login
	 * 
	 * @param _email
	 * @param _pwd
	 * @return String
	 */
	public String newDinner(String _dinnerName, String _lat, String _lng,
			String _addr) {
		String str = "", strResult = "";
		String dinnerId = "";
		StringBuilder strUrl, strParams;
		InputStreamReader in = null;
		HttpURLConnection http = null;
		DataOutputStream out = null;
		try {
			strParams = new StringBuilder("");
			strParams.append("&_a=add");
			strParams.append("&name=");
			strParams.append(URLEncoder.encode(_dinnerName, Constants.ENCODE));
			strParams.append("&longitude=");
			strParams.append(URLEncoder.encode(_lng, Constants.ENCODE));
			strParams.append("&latitude=");
			strParams.append(URLEncoder.encode(_lat, Constants.ENCODE));
			strParams.append("&address=");
			strParams.append(URLEncoder.encode(_addr, Constants.ENCODE));
			strParams.append("&city=");
			strParams.append("");
			strParams.append("&tel=");
			strParams.append("");
			strParams.append("&city=");
			strParams.append(lv.getQuanZiCityId());
			strParams.append("&login_user_id");
			strParams.append(lv.getUid());

			strUrl = new StringBuilder(Constants.API_NEW_DINNER);
			URL url = new URL(strUrl.toString());
			http = (HttpURLConnection) url.openConnection();
			http.setDoOutput(true);
			http.setDoInput(true);
			http.setRequestMethod("POST");
			http.setConnectTimeout(40000);
			http.setUseCaches(false);
			http.setInstanceFollowRedirects(true);
			http.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			http.connect();

			out = new DataOutputStream(http.getOutputStream());
			out.writeBytes(strParams.toString());

			int code = http.getResponseCode();
			if (code == 200) {
				InputStream is = http.getInputStream();
				byte[] data = coms.readStream(is);
				strResult = new String(data);
				if (!"".equals(strResult) && strResult.equals("false")) {
					str = "0#新建餐馆失败，请稍后重试！";
				} else {
					Log.e("newDinner", "strResult:" + strResult);
					dinnerId = strResult;
					lv.setTempDinnerId(dinnerId);
					lv.setTempDinnerName(_dinnerName);
					str = "1#新建餐馆成功！";
				}
			} else {
				str = "0#服务器连接失败，请确认网络状态后重试！";
			}
		} catch (Exception e) {
			str = "0#新建餐馆失败，请稍候重试！";
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (in != null) {
					in.close();
				}
				http.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

}
