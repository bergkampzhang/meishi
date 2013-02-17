package com.meishimeike.weibo.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.meishimeike.R;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-6-19 class desc
 */
public class ActWebView extends Activity {
	private WebView webView;
	private Intent intent = null;
	protected Context mContext;
	public static ActWebView webInstance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.act_web_view);
		setTitle(getResources().getString(R.string.app_name));

		webInstance = this;
		mContext = getApplicationContext();
		webView = (WebView) findViewById(R.id.web);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSaveFormData(true);
		webSettings.setSavePassword(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		webView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				webView.requestFocus();
				return false;
			}
		});

		intent = this.getIntent();
		if (!intent.equals(null)) {
			Bundle b = intent.getExtras();
			if (b != null && b.containsKey("url")) {
				webView.loadUrl(b.getString("url"));
				webView.setWebChromeClient(new WebChromeClient() {
					public void onProgressChanged(WebView view, int progress) {
						setTitle("请等待，加载中..." + progress + "%");
						setProgress(progress * 100);

						if (progress == 100)
							setTitle(R.string.app_name);
					}
				});
			}
		}
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

	/**
	 * 监听BACK键
	 * 
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			ActWeiboGuide.webInstance.finish();
			ActWebView.this.finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
