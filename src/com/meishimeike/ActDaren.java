package com.meishimeike;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.meishimeike.Adapter.DarenAdapter;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.Commons;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-6-18 class desc
 */
public class ActDaren extends ActivityTemplate {
	private MainNavigateBar navBar;
	private ListView lvDaren;
	private ArrayList<BeanUserInfo> arrList = null;
	private Commons coms = null;
	private BllGet bllGet = null;
	private DarenAdapter adapter = null;
	private LinearLayout llLoad = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_daren);
		coms = new Commons();
		bllGet = new BllGet(this);
		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);

		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		lvDaren = (ListView) findViewById(R.id.lvDaren);
	}

	private void init_data() {
		arrList = bllGet.getDaren();
	}

	private void setCtrlData() {
		adapter = new DarenAdapter(this, arrList);
		lvDaren.setAdapter(adapter);
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			coms.ExitSys(ActDaren.this);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			Intent intent = new Intent();
			intent.setClass(ActDaren.this, ActSetting.class);
			startActivity(intent);
		}
	};

	private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
		@Override
		public void onClickListenr() {
		}
	};

	@Override
	protected void onDestroy() {
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
