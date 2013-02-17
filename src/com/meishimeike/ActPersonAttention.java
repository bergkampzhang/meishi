package com.meishimeike;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.meishimeike.Adapter.PersonAttentionAdapter;
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
public class ActPersonAttention extends ActivityTemplate {
	private UserNavigateBar navBar;
	private ListView lvAttention;
	private ArrayList<BeanUserInfo> arrList = null;
	private BllGet bllGet = null;
	private PersonAttentionAdapter adapter = null;
	private LocalVariable lv = null;
	private LinearLayout llLoad = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_person_attention);
		init_ctrl();
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

		lvAttention = (ListView) findViewById(R.id.lvAttention);
		lvAttention.setOnItemClickListener(itemClickListener);
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			String strUid = arrList.get(pos).getUid();
			Intent intent = new Intent();
			if (strUid.equals(lv.getUid())) {
				intent.setClass(ActPersonAttention.this, ActPerson.class);
			} else {
				intent.setClass(ActPersonAttention.this, ActTa.class);
				intent.putExtra(ActTa.KEY_UID, strUid);
			}
			ActPersonAttention.this.startActivity(intent);

		}

	};

	private void init_data() {
		if (!"".equals(lv.getUid())) {
			arrList = bllGet.getAttention(lv.getUid());
		}
	}

	private void setCtrlData() {
		adapter = new PersonAttentionAdapter(this, arrList);
		lvAttention.setAdapter(adapter);
	}

	private OnUserNavLeftClickListener NavLeftClickListener = new OnUserNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActPersonAttention.this.finish();
		}
	};

	private OnUserNavRightClickListener NavRightClickListener = new OnUserNavRightClickListener() {
		@Override
		public void onClickListenr() {

		}
	};

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
