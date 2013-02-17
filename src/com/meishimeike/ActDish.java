package com.meishimeike;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.DishTabWidget.DishNavigateBar;
import com.meishimeike.DishTabWidget.OnDishNavLeftClickListener;
import com.meishimeike.DishTabWidget.OnDishNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.LocalVariable;
import com.meishimeike.View.DishCommentView;
import com.meishimeike.View.DishDetailTips;
import com.meishimeike.View.DishDetailView;
import com.umeng.analytics.MobclickAgent;

public class ActDish extends ActivityTemplate {
	public static final String KEY_FID = "KEY_FID";
	private DishNavigateBar navBar;
	private RelativeLayout rlDishInfo, rlComment;
	private String strFid = "";
	private BllGet bllGet = null;
	private BeanFood beanFood = null;
	private LocalVariable lv = null;
	private ScrollView sl = null;
	private DishCommentView dishCommentView = null;
	private DishDetailView dishDetailView = null;
	private DishDetailTips dishTipsView = null;
	private RelativeLayout rlDishDesc;
	private LinearLayout llLoad = null;
	public static Thread loadThread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_dish);
		lv = new LocalVariable(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			strFid = bundle.getString(KEY_FID);
			lv.setTempFId(strFid);
		} else {
			strFid = lv.getTempFId();
		}
		bllGet = new BllGet(this);
		init_ctrl();
		// new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		navBar = (DishNavigateBar) findViewById(R.id.DishNAV);
		navBar.setOnDishNavLeftClickListener(NavLeftClickListener);
		navBar.setOnDishNavRightClickListener(NavRightClickListener);
		rlDishInfo = (RelativeLayout) findViewById(R.id.rlDishInfo);
		rlComment = (RelativeLayout) findViewById(R.id.rlComment);
		rlDishDesc = (RelativeLayout) findViewById(R.id.rlDishDesc);
		sl = (ScrollView) findViewById(R.id.sl);
	}

	private void init_data() {
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		dishDetailView = new DishDetailView(this, null, beanFood,beanFood.getUid());
		rlDishInfo.addView(dishDetailView, lp);
		dishCommentView = new DishCommentView(this, null, strFid,
				beanFood.getUid());
		if (dishCommentView != null) {
			dishCommentView.init_data();
		}
		rlComment.addView(dishCommentView, lp);
		dishTipsView = new DishDetailTips(this, beanFood, null);
		rlDishDesc.addView(dishTipsView, lp);
		sl.smoothScrollTo(0, 0);
		navBar.setTitle(beanFood.getName());
	}

	private OnDishNavLeftClickListener NavLeftClickListener = new OnDishNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActDish.this.finish();
		}
	};

	private OnDishNavRightClickListener NavRightClickListener = new OnDishNavRightClickListener() {
		@Override
		public void onClickListenr() {
			Intent intent = new Intent();
			intent.setClass(ActDish.this, ActMain.class);
			startActivity(intent);
		}
	};

	public Runnable loadRunnable = new Runnable() {

		@Override
		public void run() {
			beanFood = bllGet.getFoodInfo(strFid);
			if (beanFood != null) {
				lv.setTempDId(beanFood.getRid());
			}
			handler.sendEmptyMessage(1);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (beanFood != null) {
					init_data();
				}
				llLoad.setVisibility(View.GONE);
				break;
			}
		}
	};

	// private class myTask extends AsyncTask<Void, Void, Void> {
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// llLoad.setVisibility(View.VISIBLE);
	// }
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	// beanFood = bllGet.getFoodInfo(strFid);
	// if (beanFood != null) {
	// lv.setTempDId(beanFood.getRid());
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// super.onPostExecute(result);
	// if (beanFood != null) {
	// init_data();
	// }
	// llLoad.setVisibility(View.GONE);
	// }
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActDish.this.finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
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
		loadThread = new Thread(loadRunnable);
		loadThread.start();
	}
}
