package com.meishimeike;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.LocalVariable;
import com.meishimeike.weibo.sina.ActWeiboGuide;
import com.umeng.analytics.MobclickAgent;

public class ActSetting extends ActivityTemplate implements OnClickListener,
		OnCheckedChangeListener {
	private MainNavigateBar navBar;
	private Button btnScroe, btnSettingSina, btnLogout;
	private Button btnSettingKaixin, btnSettingRenren;
	private TextView txtAbout, txtScore;
	private CheckBox cbPush;
	private LocalVariable lv = null;
	private Commons coms = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_setting);
		lv = new LocalVariable(this);
		coms = new Commons();
		init_ctrl();
		init_data();
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);
		btnScroe = (Button) findViewById(R.id.btnScroe);
		btnScroe.setOnClickListener(this);
		btnSettingSina = (Button) findViewById(R.id.btnSettingSina);
		btnSettingSina.setOnClickListener(this);
		btnSettingKaixin = (Button) findViewById(R.id.btnSettingKaixin);
		btnSettingKaixin.setOnClickListener(this);
		btnSettingRenren = (Button) findViewById(R.id.btnSettingRenren);
		btnSettingRenren.setOnClickListener(this);
		txtAbout = (TextView) findViewById(R.id.txtAbout);
		txtAbout.setOnClickListener(this);
		txtScore = (TextView) findViewById(R.id.txtScore);
		cbPush = (CheckBox) findViewById(R.id.cbPush);
		cbPush.setOnCheckedChangeListener(this);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
	}

	private void init_data() {
		String strTemp = "亲~给" + "<font color='red'>" + "美食美刻" + "</font>"
				+ "评个分吧:)~";
		txtScore.setText(Html.fromHtml(strTemp));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnScroe:
			break;
		case R.id.txtAbout:
			Intent intent = new Intent();
			intent.setClass(ActSetting.this, ActAbout.class);
			startActivity(intent);
			break;
		case R.id.btnSettingSina:
			if (lv.getIsBinding()) {
				lv.setIsBinding(false);
				coms.clearWeiboLogin(ActSetting.this);
				btnSettingSina.setText("连接");
			} else {
				intent = new Intent();
				intent.setClass(ActSetting.this, ActWeiboGuide.class);
				lv.setIsBinding(true);
				startActivity(intent);
			}
			break;
		case R.id.btnSettingKaixin:
			break;
		case R.id.btnSettingRenren:
			break;
		case R.id.btnLogout:
			coms.ExitLogin(ActSetting.this);
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {

		} else {

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
			ActSetting.this.finish();
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		if (lv.getIsWLogin()) {
			lv.setIsBinding(true);
			btnSettingSina.setText("已连接");
			// btnSettingKaixin.setVisibility(View.INVISIBLE);
			// btnSettingRenren.setVisibility(View.INVISIBLE);
		} else {
			// btnSettingSina.setVisibility(View.VISIBLE);
			// btnSettingKaixin.setVisibility(View.VISIBLE);
			// btnSettingRenren.setVisibility(View.VISIBLE);
		}

		if (lv.getIsBinding() && !lv.getWUid().equals("")) {
			btnSettingSina.setText("已连接");
		} else {
			btnSettingSina.setText("连接");
			lv.setIsBinding(false);
		}
		// btnSettingKaixin.setVisibility(View.INVISIBLE);
		// btnSettingRenren.setVisibility(View.INVISIBLE);
		if (lv.getIsLogin()) {
			btnLogout.setVisibility(View.VISIBLE);
		} else {
			btnLogout.setVisibility(View.INVISIBLE);
		}
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
