package com.meishimeike;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.meishimeike.Adapter.SearchFoodAdapter;
import com.meishimeike.Adapter.SearchTagAdapter;
import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Bean.BeanLoc;
import com.meishimeike.Bean.BeanTag;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.Location.GpsManager;
import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.Commons;
import com.umeng.analytics.MobclickAgent;

public class ActSearch extends ActivityTemplate implements OnClickListener,
		OnCheckedChangeListener, GpsManager.GpsListener {
	private MainNavigateBar navBar;
	private Button btnSort, btnRim;
	private LinearLayout llSort, llRim;
	private ListView lvSort, lvFood;
	private ImageView imgSearch;
	private RadioButton rb1, rb2, rb3, rb4, rb5;
	private EditText txtSearch;
	private BllGet bllGet = null;
	private ArrayList<BeanTag> arrTagList = null;
	private ArrayList<BeanFood> arrSerFoodList = null;
	private ArrayList<BeanFood> arrRimFoodList = null;
	private ArrayList<BeanFood> arrTagFoodList = null;
	private Thread searchThread = null;
	private Thread tagThread = null;
	private Thread rimThread = null;
	private double lng = 0.0, lat = 0.0;
	private long range = 0;
	private String strSearch = "", strTag = "";
	private SearchFoodAdapter foodAdapter = null;
	private SearchTagAdapter tagAdapter = null;
	private LinearLayout llLoad = null;
	private BeanLoc beanLoc = null;
	private Commons coms = null;
	private GpsManager mGpsManager = null;
	private static Location locNow = null;
	private boolean isTagFood = false;
	private TextView txt1, txt2, txt3, txt4, txt5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_search);
		coms = new Commons();
		init_ctrl();
		// 加载GpsManager
		mGpsManager = new GpsManager(this);
		mGpsManager.setGpsListener(this);
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);

		llLoad = (LinearLayout) findViewById(R.id.llLoad);

		btnSort = (Button) findViewById(R.id.btnSort);
		btnSort.setOnClickListener(this);
		btnRim = (Button) findViewById(R.id.btnRim);
		btnRim.setOnClickListener(this);
		imgSearch = (ImageView) findViewById(R.id.imgSearch);
		imgSearch.setOnClickListener(this);
		txtSearch = (EditText) findViewById(R.id.txtSearch);

		rb1 = (RadioButton) findViewById(R.id.rb1);
		rb1.setOnCheckedChangeListener(this);
		rb2 = (RadioButton) findViewById(R.id.rb2);
		rb2.setOnCheckedChangeListener(this);
		rb3 = (RadioButton) findViewById(R.id.rb3);
		rb3.setOnCheckedChangeListener(this);
		rb4 = (RadioButton) findViewById(R.id.rb4);
		rb4.setOnCheckedChangeListener(this);
		rb5 = (RadioButton) findViewById(R.id.rb5);
		rb5.setOnCheckedChangeListener(this);

		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt5 = (TextView) findViewById(R.id.txt5);

		llSort = (LinearLayout) findViewById(R.id.llSearch);
		llRim = (LinearLayout) findViewById(R.id.llRim);

		lvSort = (ListView) findViewById(R.id.lvSort);
		lvSort.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				lvSort.setVisibility(View.GONE);
				lvFood.setVisibility(View.VISIBLE);

				strTag = arrTagList.get(pos).getTagId();
				isTagFood = true;
				tagThread = new Thread(tagRunnable);
				tagThread.start();
				llLoad.setVisibility(View.VISIBLE);
			}
		});
		lvFood = (ListView) findViewById(R.id.lvRim);
		lvFood.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int pos,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(ActSearch.this, ActDish.class);
				intent.putExtra(ActDish.KEY_FID, adapter.getItemAtPosition(pos)
						.toString());
				startActivity(intent);
			}
		});
	}

	private void init_data() {
		bllGet = new BllGet(this);
		arrTagList = bllGet.getTagList();
	}

	private void setCtrlData() {
		tagAdapter = new SearchTagAdapter(this, arrTagList);
		lvSort.setAdapter(tagAdapter);
	}

	@Override
	public void onClick(View v) {
		int resId = v.getId();
		changeImage(resId);
		switch (resId) {
		case R.id.btnSort:
			break;
		case R.id.btnRim:
			changeTxtColor(1);
			if (range == 0) {
				range = 500;
				rimThread = new Thread(rimRunnable);
				rimThread.start();
				llLoad.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.imgSearch:
			strSearch = txtSearch.getText().toString();
			searchThread = new Thread(searchRunnable);
			searchThread.start();
			llLoad.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.rb1:
			range = 500;
			rimThread = new Thread(rimRunnable);
			rimThread.start();
			llLoad.setVisibility(View.VISIBLE);
			changeTxtColor(1);
			break;
		case R.id.rb2:
			range = 1500;
			rimThread = new Thread(rimRunnable);
			rimThread.start();
			llLoad.setVisibility(View.VISIBLE);
			changeTxtColor(2);
			break;
		case R.id.rb3:
			range = 3000;
			rimThread = new Thread(rimRunnable);
			rimThread.start();
			llLoad.setVisibility(View.VISIBLE);
			changeTxtColor(3);
			break;
		case R.id.rb4:
			range = 5000;
			rimThread = new Thread(rimRunnable);
			rimThread.start();
			llLoad.setVisibility(View.VISIBLE);
			changeTxtColor(4);
			break;
		case R.id.rb5:
			range = 30000;
			rimThread = new Thread(rimRunnable);
			rimThread.start();
			llLoad.setVisibility(View.VISIBLE);
			changeTxtColor(5);
			break;
		}
	}

	private void changeTxtColor(int index) {
		switch (index) {
		case 1:
			txt1.setTextColor(R.color.red);
			txt2.setTextColor(R.color.gray_dark);
			txt3.setTextColor(R.color.gray_dark);
			txt4.setTextColor(R.color.gray_dark);
			txt5.setTextColor(R.color.gray_dark);
			break;
		case 2:
			txt1.setTextColor(R.color.gray_dark);
			txt2.setTextColor(R.color.red);
			txt3.setTextColor(R.color.gray_dark);
			txt4.setTextColor(R.color.gray_dark);
			txt5.setTextColor(R.color.gray_dark);
			break;
		case 3:
			txt1.setTextColor(R.color.gray_dark);
			txt2.setTextColor(R.color.gray_dark);
			txt3.setTextColor(R.color.red);
			txt4.setTextColor(R.color.gray_dark);
			txt5.setTextColor(R.color.gray_dark);
			break;
		case 4:
			txt1.setTextColor(R.color.gray_dark);
			txt2.setTextColor(R.color.gray_dark);
			txt3.setTextColor(R.color.gray_dark);
			txt4.setTextColor(R.color.red);
			txt5.setTextColor(R.color.gray_dark);
			break;
		case 5:
			txt1.setTextColor(R.color.gray_dark);
			txt2.setTextColor(R.color.gray_dark);
			txt3.setTextColor(R.color.gray_dark);
			txt4.setTextColor(R.color.gray_dark);
			txt5.setTextColor(R.color.red);
			break;
		}

	}

	private Runnable searchRunnable = new Runnable() {

		@Override
		public void run() {
			arrSerFoodList = bllGet.searchFood(strSearch);
			handler.sendEmptyMessage(1);
		}
	};

	private Runnable tagRunnable = new Runnable() {

		@Override
		public void run() {
			arrTagFoodList = bllGet.getFoodListByTag(strTag);
			handler.sendEmptyMessage(2);
		}
	};

	private Runnable rimRunnable = new Runnable() {

		@Override
		public void run() {
			if (beanLoc == null) {
				beanLoc = AppMsmk.getBeanLoc();
			}
			if (beanLoc != null) {
				lng = beanLoc.getLng();
				lat = beanLoc.getLat();
				arrRimFoodList = bllGet.getFoodListByRim(lng, lat, range, 20);
				handler.sendEmptyMessage(3);
			}
			handler.sendEmptyMessage(3);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				lvFood.setAdapter(null);
				foodAdapter = new SearchFoodAdapter(ActSearch.this,
						arrSerFoodList);
				lvFood.setAdapter(foodAdapter);
				break;
			case 2:
				lvFood.setAdapter(null);
				foodAdapter = new SearchFoodAdapter(ActSearch.this,
						arrTagFoodList);
				lvFood.setAdapter(foodAdapter);
				break;
			case 3:
				lvFood.setAdapter(null);
				foodAdapter = new SearchFoodAdapter(ActSearch.this,
						arrRimFoodList);
				lvFood.setAdapter(foodAdapter);
				break;
			}
			llLoad.setVisibility(View.GONE);
		}
	};

	private class myTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			llLoad.setVisibility(View.VISIBLE);
			// llContent.setVisibility(View.GONE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			init_data();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			setCtrlData();
			llLoad.setVisibility(View.GONE);
		}
	}

	private void changeImage(int resId) {
		switch (resId) {
		case R.id.btnSort:
			btnSort.setBackgroundResource(R.drawable.img_search_sort_focus);
			btnRim.setBackgroundResource(R.drawable.img_search_rim);
			llSort.setVisibility(View.VISIBLE);
			llRim.setVisibility(View.GONE);
			lvSort.setVisibility(View.VISIBLE);
			lvFood.setVisibility(View.GONE);
			break;
		case R.id.btnRim:
			btnSort.setBackgroundResource(R.drawable.img_search_sort);
			btnRim.setBackgroundResource(R.drawable.img_search_rim_focus);
			llSort.setVisibility(View.GONE);
			llRim.setVisibility(View.VISIBLE);
			lvSort.setVisibility(View.GONE);
			lvFood.setVisibility(View.VISIBLE);
		case R.id.imgSearch:
			lvSort.setVisibility(View.GONE);
			lvFood.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isTagFood) {
				isTagFood = false;
				changeImage(R.id.btnSort);
			} else {
				coms.ExitSys(ActSearch.this);
			}
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			Intent intent = new Intent();
			intent.setClass(ActSearch.this, ActSetting.class);
			startActivity(intent);
		}
	};

	private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
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

	@Override
	public void onGpsDeviceClose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsDeviceOpen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsDeviceStateChanged(int status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			locNow = location;
			beanLoc = new BeanLoc();
			beanLoc.setLng(locNow.getLongitude());
			beanLoc.setLat(locNow.getLatitude());
			beanLoc.setLoc(locNow);
			AppMsmk.setBeanLoc(beanLoc);
		}

	}

}
