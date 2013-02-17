package com.meishimeike;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.Commons;
import com.umeng.analytics.MobclickAgent;

public class ActTopic extends ActivityTemplate {
	private MainNavigateBar navBar;
	private WebView wv = null;
	private ProgressDialog pd = null;
	private Commons coms = null;
	private Thread loadThread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 MobclickAgent.onError(this);
		setContentView(R.layout.act_topic);
		coms = new Commons();
		init_ctrl();
		init_data();
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);
		wv = (WebView) findViewById(R.id.wv);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setScrollBarStyle(0);
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				loadurl(view, url);// 载入网页
				return true;
			}// 重写点击动作,用webview载入

		});
		wv.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
				if (progress == 100) {
					handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
				}
				super.onProgressChanged(view, progress);
			}
		});
		pd = new ProgressDialog(ActTopic.this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("数据载入中，请稍候！");
	}

	private void init_data() {
		loadurl(wv, "http://www.meishimeike.com/");
	}

	public void loadurl(final WebView view, final String url) {
		loadThread = new Thread() {
			public void run() {
				handler.sendEmptyMessage(0);
				try {
					view.loadUrl(url);// 载入网页
				} catch (Exception e) {
					handler.sendEmptyMessage(2);
				}
			}
		};
		loadThread.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					pd.show();// 显示进度对话框
					break;
				case 1:
					pd.hide();// 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
					break;
				case 2:
					pd.hide();
					Toast.makeText(ActTopic.this, "专题加载失败，请重试",
							Toast.LENGTH_LONG).show();
				}
			}
			super.handleMessage(msg);
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
			wv.goBack();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			coms.ExitSys(ActTopic.this);
			return false;
			// 按了返回键，但已经不能返回，则执行退出确认
		}
		return super.onKeyDown(keyCode, event);
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
			Intent intent = new Intent();
			intent.setClass(ActTopic.this, ActSetting.class);
			startActivity(intent);
		}
	};

	@Override
	protected void onDestroy() {
		try {
			if (!loadThread.isInterrupted()) {
				loadThread.interrupt();
			}
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
