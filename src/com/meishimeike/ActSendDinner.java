package com.meishimeike;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.meishimeike.Adapter.SendDinnerAdapter;
import com.meishimeike.Bean.BeanLoc;
import com.meishimeike.Bean.BeanRimDinner;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.Location.GpsManager;
import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-5-28 class desc
 */
public class ActSendDinner extends ActivityTemplate implements
		GpsManager.GpsListener {
	private MainNavigateBar navBar;
	private EditText txtDinner = null;
	private ImageView imgSearch = null;
	private ListView lvDinner = null;
	private LocalVariable lv = null;
	private ArrayList<BeanRimDinner> arrList = null;
	private Thread searchThread = null;
	private LinearLayout llLoad = null;
	private BllGet bllGet = null;
	private BeanLoc beanLoc = null;
	private SendDinnerAdapter adapter = null;
	private String strSearch = "";
	private double lng = 0.0, lat = 0.0;
	private static long range = 0;
	private GpsManager mGpsManager = null;
	private static Location locNow = null;
	private Commons coms = null;
	private TextView txtAdd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_send_dinner);
		lv = new LocalVariable(this);
		bllGet = new BllGet(this);
		coms = new Commons();
		// 加载GpsManager
		mGpsManager = new GpsManager(this);
		mGpsManager.setGpsListener(this);
		init_ctrl();
		range = 5000;
		strSearch = lv.getTempDinnerName();
		searchThread = new Thread(searchRunnable);
		searchThread.start();
		llLoad.setVisibility(View.VISIBLE);
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);
		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		imgSearch = (ImageView) findViewById(R.id.imgSearch);
		imgSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				strSearch = txtDinner.getText().toString();
				if ("".equals(strSearch)) {
					range = 5000;
				} else {
					range = 30000;
				}
				searchThread = new Thread(searchRunnable);
				searchThread.start();
				llLoad.setVisibility(View.VISIBLE);

			}
		});
		txtDinner = (EditText) findViewById(R.id.txtDinner);
		txtDinner.setText(lv.getTempDinnerName());

		txtAdd = (TextView) findViewById(R.id.txtAdd);
		txtAdd.setVisibility(View.GONE);
		txtAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ActSendDinner.this, ActNewDinner.class);
				intent.putExtra("DinnerName", strSearch);
				startActivity(intent);
				ActSendDinner.this.finish();
			}
		});

		lvDinner = (ListView) findViewById(R.id.lvDinner);
		lvDinner.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				BeanRimDinner bean = arrList.get(pos);
				lv.setTempDinnerId(bean.getRid());
				lv.setTempDinnerName(bean.getName());
				Intent intent = new Intent();
				intent.setClass(ActSendDinner.this, ActSendName.class);
				startActivity(intent);
				ActSendDinner.this.finish();
			}
		});
	}

	private Runnable searchRunnable = new Runnable() {

		@Override
		public void run() {
			if (beanLoc == null) {
				beanLoc = AppMsmk.getBeanLoc();
			}
			if (beanLoc != null) {
				lng = beanLoc.getLng();
				lat = beanLoc.getLat();
				arrList = bllGet.getDinnerByRim(lng, lat, range, strSearch);
			}
			handler.sendEmptyMessage(1);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				lvDinner.setAdapter(null);
				if (arrList == null || arrList.size() < 1) {
					lvDinner.setVisibility(View.GONE);
				} else {
					lvDinner.setVisibility(View.VISIBLE);
					adapter = new SendDinnerAdapter(ActSendDinner.this, arrList);
					lvDinner.setAdapter(adapter);
				}
				if (!"".equals(strSearch)) {
					txtAdd.setText("添加叫'" + strSearch + "'的餐厅");
					txtAdd.setVisibility(View.VISIBLE);
				} else {
					txtAdd.setVisibility(View.GONE);
				}
				break;
			}
			llLoad.setVisibility(View.GONE);
		}
	};

	private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			lv.setTempDinnerId("");
			txtDinner.setText("");
			Intent intent = new Intent();
			intent.setClass(ActSendDinner.this, ActPicView.class);
			startActivity(intent);
			ActSendDinner.this.finish();
			// String str = txtDinner.getText().toString().trim();
			// if ("".equals(str)) {
			// Toast.makeText(ActSendDinner.this, "餐馆名称不能为空",
			// Toast.LENGTH_SHORT).show();
			// } else {
			// lv.setTempDinnerId(str);
			// Intent intent = new Intent();
			// intent.setClass(ActSendDinner.this, ActSendDinner.class);
			// startActivity(intent);
			// ActSendDinner.this.finish();
			// }
		}
	};

	private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
		@Override
		public void onClickListenr() {
			coms.initSendCfg(ActSendDinner.this);
			ActSendDinner.this.finish();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			lv.setTempDinnerId("");
			lv.setTempDinnerName("");
			txtDinner.setText("");
			Intent intent = new Intent();
			intent.setClass(ActSendDinner.this, ActPicView.class);
			startActivity(intent);
			ActSendDinner.this.finish();
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
