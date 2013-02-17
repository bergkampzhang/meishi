package com.meishimeike.weibo.sina;

import java.util.SortedSet;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.meishimeike.ActSetting;
import com.meishimeike.ActShare;
import com.meishimeike.ActSinaFriend;
import com.meishimeike.R;
import com.meishimeike.Bll.BllSend;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-6-19 class desc
 */
public class ActWeiboGuide extends Activity {
	private Context mContext;
	private AccessInfo accessInfo = null;
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private OAuthProvider httpOauthprovider;
	private final static String callBackUrl = "founderapp://ActWeiboGuide";
	protected final String SDCARD_MNT = "/mnt/sdcard";
	protected final String SDCARD = "/sdcard";
	private LocalVariable lv = null;
	private BllSend bllSend = null;
	private Commons coms = null;
	public static ActWeiboGuide webInstance = null;
	private String strMessage = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_weibo_guide);
		webInstance = this;
		bllSend = new BllSend(this);
		coms = new Commons();
		lv = new LocalVariable(this);
		mContext = getApplicationContext();
		accessInfo = InfoHelper.getAccessInfo(mContext);
		httpOauthConsumer = new CommonsHttpOAuthConsumer(
				getString(R.string.app_sina_consumer_key),
				getString(R.string.app_sina_consumer_secret));

		httpOauthprovider = new DefaultOAuthProvider(
				"http://api.t.sina.com.cn/oauth/request_token",
				"http://api.t.sina.com.cn/oauth/access_token",
				"http://api.t.sina.com.cn/oauth/authorize");

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

		// 之前登录过
		if (accessInfo != null && !lv.getWUid().equals("")) {
			Intent intent = new Intent();
			if (lv.getIsWLogin() && !lv.getUid().equals("")) {
				// intent.setClass(ActWeiboGuide.this, ActPerson.class);
			} else if (lv.getIsBinding()) {
				intent.setClass(ActWeiboGuide.this, ActSetting.class);
				startActivity(intent);
			} else if (lv.getIsSinaFriend()) {
				intent.setClass(ActWeiboGuide.this, ActSinaFriend.class);
				startActivity(intent);
			} else {
				intent.setClass(ActWeiboGuide.this, ActShare.class);
				startActivity(intent);
			}
			finish();
		} else {
			coms.clearBrowserLogin(ActWeiboGuide.this);
			startOAuthView();
		}

	}

	private void startOAuthView() {
		try {
			String authUrl = httpOauthprovider.retrieveRequestToken(
					httpOauthConsumer, callBackUrl);

			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("url", authUrl);
			intent.putExtras(bundle);
			intent.setClass(mContext, ActWebView.class);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Uri uri = intent.getData();
		if (uri == null) {
			return;
		}

		String verifier = uri
				.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

		try {
			httpOauthprovider.setOAuth10a(true);
			httpOauthprovider.retrieveAccessToken(httpOauthConsumer, verifier);
		} catch (OAuthMessageSignerException ex) {
			ex.printStackTrace();
		} catch (OAuthNotAuthorizedException ex) {
			ex.printStackTrace();
		} catch (OAuthExpectationFailedException ex) {
			ex.printStackTrace();
		} catch (OAuthCommunicationException ex) {
			ex.printStackTrace();
		}

		SortedSet<String> userInfoSet = httpOauthprovider
				.getResponseParameters().get("user_id");
		if (userInfoSet != null && !userInfoSet.isEmpty()) {
			String userID = userInfoSet.first();
			String accessToken = httpOauthConsumer.getToken();
			String accessSecret = httpOauthConsumer.getTokenSecret();

			if (lv.getIsWLogin()) {
				String str = bllSend.checkWUid(userID, accessToken,
						accessSecret);
				if (str.split("#")[0].equals("1")) {
					lv.setWUid(userID);
					lv.setWToken(accessToken);
					lv.setWSecret(accessSecret);
				} else {
					strMessage = str.split("#")[1];
					Toast.makeText(ActWeiboGuide.this, strMessage,
							Toast.LENGTH_SHORT).show();
					coms.clearBrowserLogin(ActWeiboGuide.this);
					ActWeiboGuide.this.finish();
					return;
				}
			} else {
				lv.setWUid(userID);
				lv.setWToken(accessToken);
				lv.setWSecret(accessSecret);
			}

			AccessInfo accessInfo = new AccessInfo();
			accessInfo.setUserID(userID);
			accessInfo.setAccessToken(accessToken);
			accessInfo.setAccessSecret(accessSecret);

			AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
			accessDBHelper.open();
			accessDBHelper.create(accessInfo);
			accessDBHelper.close();

			Intent intent2 = new Intent();
			if (lv.getIsWLogin() && !lv.getUid().equals("")) {
				// intent2.setClass(ActWeiboGuide.this, ActPerson.class);
				lv.setIsNewLogin(true);
			} else if (lv.getIsBinding()) {
				intent2.setClass(ActWeiboGuide.this, ActSetting.class);
				startActivity(intent2);
			} else if (lv.getIsSinaFriend()) {
				intent.setClass(ActWeiboGuide.this, ActSinaFriend.class);
				startActivity(intent);
			} else {
				intent2.setClass(ActWeiboGuide.this, ActShare.class);
				startActivity(intent2);
			}
			if (ActWebView.webInstance != null) {
				ActWebView.webInstance.finish();
			}
			lv.setIsSinaFriend(false);
			finish();
		}
	}

}
