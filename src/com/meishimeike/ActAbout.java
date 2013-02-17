package com.meishimeike;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-5-25 class desc
 */
public class ActAbout extends ActivityTemplate implements OnClickListener {
	private MainNavigateBar navBar;
	private ImageView imgNet, imgEmail, imgPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_about);
		init_ctrl();
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);

		imgNet = (ImageView) findViewById(R.id.about3_2);
		imgNet.setOnClickListener(this);
		imgEmail = (ImageView) findViewById(R.id.about3_3);
		imgEmail.setOnClickListener(this);
		imgPhone = (ImageView) findViewById(R.id.about3_4);
		imgPhone.setOnClickListener(this);
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
			ActAbout.this.finish();
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
	public void onClick(View v) {
		Intent intent = null;
		Uri uri = null;
		switch (v.getId()) {
		case R.id.about3_2:
			uri = Uri.parse("http://www.meishimeike.com");
			intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			break;
		case R.id.about3_3:
			uri = Uri.parse("mailto:hezuo@meishimeike.com");
			intent = new Intent(Intent.ACTION_SENDTO, uri);
			startActivity(intent);
			break;
		case R.id.about3_4:
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ "010-62684368"));
			startActivity(intent);
			break;
		}

	}
}
