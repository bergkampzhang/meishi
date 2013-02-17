package com.meishimeike;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.meishimeike.Adapter.CityAdapter;
import com.meishimeike.Bean.BeanCity;
import com.meishimeike.Bll.BllGet;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-6-18 class desc
 */
public class ActCitySel extends Activity {
	private BllGet bllGet = null;
	private ArrayList<BeanCity> arrList = null;
	private Thread loadThread = null;
	private CityAdapter adapter = null;
	private ListView lvCity = null;
	private String cityId = "", cityName = "";
	private boolean isCity = false;
	private LinearLayout llLoad = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_city_sel);
		bllGet = new BllGet(this);
		init_ctrl();
		preInit();
		loadThread = new Thread(runnable);
		loadThread.start();
	}

	private void init_ctrl() {
		lvCity = (ListView) findViewById(R.id.lvCity);
		lvCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				cityId = arrList.get(pos).getId();
				if (!isCity) {
					cityName = arrList.get(pos).getName();
					preInit();
					loadThread = new Thread(runnable);
					loadThread.start();
				} else {
					cityName += arrList.get(pos).getName();
					Intent i = new Intent();
					Bundle b = new Bundle();
					b.putString("cityId", cityId);
					b.putString("cityName", cityName);
					i.putExtras(b);
					ActCitySel.this.setResult(RESULT_OK, i);
					ActCitySel.this.finish();
				}
				isCity = !isCity;
			}
		});
		llLoad = (LinearLayout) findViewById(R.id.llLoad);
	}

	private void preInit() {
		lvCity.setAdapter(null);
		llLoad.setVisibility(View.VISIBLE);
	}

	private void init_data() {
		bllGet = new BllGet(this);
		arrList = bllGet.getCityList(cityId);
	}

	private void setCtrlData() {
		adapter = new CityAdapter(this, arrList);
		lvCity.setAdapter(adapter);
		llLoad.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isCity) {
				cityId = "";
				isCity = false;
				preInit();
				loadThread = new Thread(runnable);
				loadThread.start();
				return true;
			} else {
				ActCitySel.this.finish();
				return true;
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			init_data();
			handler.sendEmptyMessage(1);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setCtrlData();
				break;
			case 2:
				break;
			}
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
