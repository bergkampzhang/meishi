package com.meishimeike;

import java.io.File;
import java.net.URLEncoder;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.androidexamples.OAuthConstant;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.Constants;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

public class ActShare extends Activity implements OnClickListener {
	private MainNavigateBar navBar;
	private ImageView img = null;
	private EditText txtInput = null;
	private Button btnShare = null;
	private String strShare = "", strInput = "";
	private String strFid = "", strFName = "";
	private String strMsg = "", strFoodUrl = "";
	private String strUploadName = "";
	private LinearLayout llLoad = null;
	private BeanFood beanFood = null;
	private LocalVariable lv = null;
	private BllGet bllGet = null;
	private Bitmap bitmap = null;
	private AlertDialog dlgProgress = null;
	private String strMessage;
	private Thread comitThread = null;
	private String accessToken, accessSecret;
	private Commons coms = null;
	private LinearLayout llShare = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_share);
		lv = new LocalVariable(this);
		coms = new Commons();
		bllGet = new BllGet(this);
		System.setProperty("weibo4j.oauth.consumerKey",
				getString(R.string.app_sina_consumer_key));
		System.setProperty("weibo4j.oauth.consumerSecret",
				getString(R.string.app_sina_consumer_secret));
		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		llShare = (LinearLayout) findViewById(R.id.ll_act_share);
		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);

		img = (ImageView) findViewById(R.id.img);
		txtInput = (EditText) findViewById(R.id.txtInput);
		btnShare = (Button) findViewById(R.id.btnShare);
		btnShare.setOnClickListener(this);

		strFid = lv.getTempFId();
		accessToken = lv.getWToken();
		accessSecret = lv.getWSecret();
	}

	private void init_data() {
		strFName = beanFood.getName();
		strShare = "推荐【" + strFName + "】- ( @美食美刻 分享生活好滋味！) >> "
				+ Constants.APP_FOOD_SHOW_URL + strFid;

		strFoodUrl = beanFood.getBig_pic();
		if (!"".equals(strFoodUrl)) {
			img.setTag(strFoodUrl);
			ImageDownloadItem item = new ImageDownloadItem();
			item.imageUrl = strFoodUrl;
			item.callback = new ImageDownloadCallback() {
				@Override
				public void update(Bitmap bitmap, String imageUrl) {
					ImageView imageViewByTag = (ImageView) llShare
							.findViewWithTag(imageUrl);
					if (imageViewByTag != null)
						imageViewByTag.setImageBitmap(bitmap);
					bitmap = null;
				}
			};
			ImageDownloadThread imageDownloadThread = ImageDownloadThread
					.getInstance();
			bitmap = imageDownloadThread.downloadWithCache(item);
			if (bitmap != null) {// 从缓存中取到
				img.setImageBitmap(bitmap);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnShare:
			share();
			break;
		}
	}

	private void share() {
		dlgProgress = ProgressDialog.show(ActShare.this, "请等待...",
				"正在进行操作.....", true);
		dlgProgress.show();
		comitThread = new Thread() {
			public void run() {
				try {
					if (netCheck()) {
						handler.sendEmptyMessage(1);
					}
				} catch (Exception ex) {
					strMessage = ex.getLocalizedMessage();
					handler.sendEmptyMessage(2);
				}
			};
		};
		comitThread.start();
	}

	private boolean netCheck() {
		boolean b = false;
		strInput = txtInput.getText().toString().trim();
		if (!strInput.equals("")) {
			Status status = null;
			try {
				strMsg = URLEncoder.encode(strShare + strInput,
						Constants.ENCODE);

				Weibo weibo = OAuthConstant.getInstance().getWeibo();
				weibo.setToken(accessToken, accessSecret);
				File file = new File(strUploadName);
				status = weibo.uploadStatus(strMsg, file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (status != null) {
				strMessage = "分享成功！";
				b = true;
			} else {
				strMessage = "分享失败，请稍候重试！";
				handler.sendEmptyMessage(2);
			}
		} else {
			strMessage = "分享说明不能为空哦！";
			handler.sendEmptyMessage(2);
		}
		return b;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(ActShare.this, strMessage, Toast.LENGTH_LONG)
						.show();
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				ActShare.this.finish();
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActShare.this, strMessage, Toast.LENGTH_LONG)
						.show();
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActShare.this.finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private class myTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			llLoad.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			beanFood = bllGet.getFoodInfo(strFid);
			if (beanFood != null) {
				lv.setTempDId(beanFood.getRid());
				strUploadName = coms.downTempFile(beanFood.getBig_pic());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (beanFood != null) {
				init_data();
			}
			llLoad.setVisibility(View.GONE);
		}
	}

	private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			// TODO Auto-generated method stub
		}
	};

	private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
		@Override
		public void onClickListenr() {
			// TODO Auto-generated method stub
		}
	};

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

	@Override
	protected void onDestroy() {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		if (!lv.getIsLogin() && !lv.getIsWLogin() && !lv.getIsBinding()) {
			coms.clearWeiboLogin(ActShare.this);
		}
		super.onDestroy();
	}

}
