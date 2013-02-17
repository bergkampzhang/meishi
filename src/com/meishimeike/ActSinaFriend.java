package com.meishimeike;

import java.util.List;

import weibo4andriod.User;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.androidexamples.OAuthConstant;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.meishimeike.Adapter.SinaFriendAdapter;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.Constants;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-7-16 class desc
 */
public class ActSinaFriend extends ActivityTemplate {
	private String wToken = "", wSecret = "";
	private LocalVariable lv = null;
	private LinearLayout llLoad = null;
	private ListView listView = null;
	private List<User> arrList = null;
	private SinaFriendAdapter adapter = null;
	private Weibo weibo = null;
	private String strMsg1 = "", strMsg2 = "";
	private Thread comitThread = null;
	private User clickUser = null;
	private Commons coms = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_sina_friend);
		lv = new LocalVariable(this);
		coms = new Commons();

		System.setProperty("weibo4j.oauth.consumerKey",
				getString(R.string.app_sina_consumer_key));
		System.setProperty("weibo4j.oauth.consumerSecret",
				getString(R.string.app_sina_consumer_secret));

		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		listView = (ListView) findViewById(R.id.lvUser);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				llLoad.setVisibility(View.VISIBLE);
				clickUser = arrList.get(pos);
				comitThread = new Thread(runnable);
				comitThread.start();
			}
		});
	}

	private void init_data() {
		wToken = lv.getWToken();
		wSecret = lv.getWSecret();
		weibo = OAuthConstant.getInstance().getWeibo();
		weibo.setToken(wToken, wSecret);
		try {
			arrList = weibo.getFriendsStatuses();
			adapter = new SinaFriendAdapter(this, arrList);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		strMsg1 = "邀请 ";
		strMsg2 = " 加入“美食美刻” （@美食美刻）。美食美刻是一个美食图片分享平台，";
		strMsg2 += "是一个兴趣社交平台，更是一个共享生活中美好时刻的平台。 ";
		strMsg2 += Constants.APP_SERVICE;
	}

	private void setCtrlData() {
		listView.setAdapter(adapter);
	}

	private class myTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			llLoad.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			init_data();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			setCtrlData();
			llLoad.setVisibility(View.GONE);
		}
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			try {
				weibo.sendDirectMessage(String.valueOf(clickUser.getId()),
						strMsg1 + clickUser.getName() + strMsg2);
				handler.sendEmptyMessage(1);
			} catch (WeiboException e) {
				handler.sendEmptyMessage(2);
				e.printStackTrace();
			}
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(ActSinaFriend.this,
						"已成功发送邀请给  @" + clickUser.getName(), Toast.LENGTH_SHORT)
						.show();
				llLoad.setVisibility(View.GONE);
				break;
			case 2:
				Toast.makeText(ActSinaFriend.this,
						"发送邀请给  @" + clickUser.getName() + "  失败",
						Toast.LENGTH_SHORT).show();
				llLoad.setVisibility(View.GONE);
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActSinaFriend.this.finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		if (!lv.getIsWLogin() && !lv.getIsBinding()) {
			coms.clearWeiboLogin(ActSinaFriend.this);
		}
		super.onDestroy();
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
	}
}
