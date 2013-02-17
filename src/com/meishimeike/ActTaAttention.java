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
import com.meishimeike.TaTabWidget.OnTaNavLeftClickListener;
import com.meishimeike.TaTabWidget.OnTaNavRightClickListener;
import com.meishimeike.TaTabWidget.TaNavigateBar;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

public class ActTaAttention extends ActivityTemplate {
	private TaNavigateBar navBar;
	private ListView lvAttentions;
	private ArrayList<BeanUserInfo> arrList = null;
	private BllGet bllGet = null;
	private PersonFansAdapter adapter = null;
	private LocalVariable lv = null;
	private LinearLayout llLoad = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_ta_attention);
		lv = new LocalVariable(this);
		bllGet = new BllGet(this);
		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		navBar = (TaNavigateBar) findViewById(R.id.NAV);
		navBar.setOnTaNavLeftClickListener(NavLeftClickListener);
		navBar.setOnTaNavRightClickListener(NavRightClickListener);

		lvAttentions = (ListView) findViewById(R.id.lvAttentions);
	}

	private void init_data() {
		if (!"".equals(lv.getTempUId())) {
			arrList = bllGet.getAttention(lv.getTempUId());
		}
	}

	private void setCtrlData() {
		adapter = new PersonFansAdapter(this, arrList);
		lvAttentions.setAdapter(adapter);
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

	private OnTaNavLeftClickListener NavLeftClickListener = new OnTaNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActTaAttention.this.finish();
		}
	};

	private OnTaNavRightClickListener NavRightClickListener = new OnTaNavRightClickListener() {
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
}
