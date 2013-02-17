package com.meishimeike;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.meishimeike.Adapter.PersonFansAdapter;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.UserTabWidget.OnUserNavLeftClickListener;
import com.meishimeike.UserTabWidget.OnUserNavRightClickListener;
import com.meishimeike.UserTabWidget.UserNavigateBar;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-5-28 class desc
 */
public class ActPersonFriend extends ActivityTemplate {
	private UserNavigateBar navBar;
	private ListView lvFans;
	private ArrayList<BeanUserInfo> arrList = null;
	private BllGet bllGet = null;
	private PersonFansAdapter adapter = null;
	private LocalVariable lv = null;
	private LinearLayout llLoad = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_person_friend);
		lv = new LocalVariable(this);
		bllGet = new BllGet(this);
		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		llLoad = (LinearLayout) findViewById(R.id.llLoad);

		navBar = (UserNavigateBar) findViewById(R.id.NAV);
		navBar.setOnUserNavLeftClickListener(NavLeftClickListener);
		navBar.setOnUserNavRightClickListener(NavRightClickListener);

		lvFans = (ListView) findViewById(R.id.lvFans);
	}

	private void init_data() {
		if (!"".equals(lv.getUid())) {
			arrList = bllGet.getUserFans(lv.getUid());
		}
	}

	private void setCtrlData() {
		adapter = new PersonFansAdapter(this, arrList);
		lvFans.setAdapter(adapter);
	}

	private class myTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			llLoad.setVisibility(View.VISIBLE);
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

	private OnUserNavLeftClickListener NavLeftClickListener = new OnUserNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActPersonFriend.this.finish();
		}
	};

	private OnUserNavRightClickListener NavRightClickListener = new OnUserNavRightClickListener() {
		@Override
		public void onClickListenr() {

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
}
