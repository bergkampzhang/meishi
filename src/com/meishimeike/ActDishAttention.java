package com.meishimeike;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.meishimeike.Adapter.DishAttentionAdapter;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.DishTabWidget.DishNavigateBar;
import com.meishimeike.DishTabWidget.OnDishNavLeftClickListener;
import com.meishimeike.DishTabWidget.OnDishNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-5-25 class desc
 */
public class ActDishAttention extends ActivityTemplate {
	public static final String KEY_FID = "KEY_FID";
	private DishNavigateBar navBar;
	private ListView lvAttention;
	private ArrayList<BeanUserInfo> arrList = null;

	private String strFid = "";
	private BllGet bllGet = null;
	private DishAttentionAdapter adapter = null;
	private LocalVariable lv = null;
	private LinearLayout llLoad = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_dish_attention);
		lv = new LocalVariable(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			strFid = bundle.getString(KEY_FID);
		} else {
			strFid = lv.getTempFId();
		}
		bllGet = new BllGet(this);
		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		navBar = (DishNavigateBar) findViewById(R.id.DishNAV);
		navBar.setOnDishNavLeftClickListener(NavLeftClickListener);
		navBar.setOnDishNavRightClickListener(NavRightClickListener);
		lvAttention = (ListView) findViewById(R.id.lvAttention);
	}

	private void init_data() {
		if (!"".equals(strFid)) {
			arrList = bllGet.getFoodFans(strFid);
		}
	}

	private void setCtrlData() {
		adapter = new DishAttentionAdapter(this, arrList);
		lvAttention.setAdapter(adapter);
	}

	private OnDishNavLeftClickListener NavLeftClickListener = new OnDishNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActDishAttention.this.finish();
		}
	};

	private OnDishNavRightClickListener NavRightClickListener = new OnDishNavRightClickListener() {
		@Override
		public void onClickListenr() {
			if (!lv.getIsLogin()) {
				Toast.makeText(ActDishAttention.this, "亲~你还没登录呢",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent();
				intent.setClass(ActDishAttention.this, ActSearchUser.class);
				startActivity(intent);
			}
		}
	};

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
