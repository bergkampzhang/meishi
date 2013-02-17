package com.meishimeike;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.Adapter.SendNameAdapter;
import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Bll.BllGet;
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
public class ActSendName extends ActivityTemplate {
	private MainNavigateBar navBar;
	private EditText txtSearch = null;
	private ImageView imgSearch = null;
	private LocalVariable lv = null;
	private ListView lvFoodName = null;
	private ArrayList<BeanFood> arrSerFoodList = null;
	private Thread searchThread = null;
	private LinearLayout llLoad = null;
	private BllGet bllGet = null;
	private SendNameAdapter adapter = null;
	private String strSearch = "";
	private TextView txtAdd = null;
	private Commons coms = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_send_name);
		lv = new LocalVariable(this);
		bllGet = new BllGet(this);
		coms = new Commons();
		init_ctrl();
		searchThread = new Thread(searchRunnable);
		searchThread.start();
		llLoad.setVisibility(View.VISIBLE);
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);

		llLoad = (LinearLayout) findViewById(R.id.llLoad);

		txtAdd = (TextView) findViewById(R.id.txtAdd);
		txtAdd.setVisibility(View.GONE);
		txtAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("".equals(strSearch)) {
					Toast.makeText(ActSendName.this, "美食名称不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					lv.setTempFoodName(strSearch);
					Intent intent = new Intent();
					if (!lv.getTempDinnerId().equals("")
							&& !lv.getTempDinnerName().equals("")) {
						intent.setClass(ActSendName.this, ActSendDesc.class);
					} else {
						intent.setClass(ActSendName.this, ActSendDesc.class);
					}
					startActivity(intent);
					ActSendName.this.finish();
				}

			}
		});

		lvFoodName = (ListView) findViewById(R.id.lvFoodName);
		lvFoodName.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int pos,
					long arg3) {
				String str = adapter.getItemAtPosition(pos).toString();
				lv.setTempFoodName(str);
				Intent intent = new Intent();
				if (!lv.getTempDinnerId().equals("")
						&& !lv.getTempDinnerName().equals("")) {
					intent.setClass(ActSendName.this, ActSendDesc.class);
				} else {
					intent.setClass(ActSendName.this, ActSendDesc.class);
				}
				startActivity(intent);
				ActSendName.this.finish();
			}
		});

		imgSearch = (ImageView) findViewById(R.id.imgSearch);
		imgSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				strSearch = txtSearch.getText().toString();
				searchThread = new Thread(searchRunnable);
				searchThread.start();
				llLoad.setVisibility(View.VISIBLE);
			}
		});
		txtSearch = (EditText) findViewById(R.id.txtSearch);
		txtSearch.setText(lv.getTempFoodName());
	}

	private Runnable searchRunnable = new Runnable() {

		@Override
		public void run() {
			if ("".equals(strSearch)) {
				arrSerFoodList = bllGet.getDinnerFood(lv.getTempDinnerId());
				if (arrSerFoodList == null || arrSerFoodList.size() < 1) {
					arrSerFoodList = bllGet.searchFood(strSearch);
				}
			} else {
				arrSerFoodList = bllGet.searchFood(strSearch);
			}
			handler.sendEmptyMessage(1);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				lvFoodName.setAdapter(null);
				if (arrSerFoodList == null || arrSerFoodList.size() < 1) {
					lvFoodName.setVisibility(View.GONE);
					txtAdd.setText("添加叫'" + strSearch + "'的美食");
					txtAdd.setVisibility(View.VISIBLE);
				} else {
					lvFoodName.setVisibility(View.VISIBLE);
					txtAdd.setVisibility(View.GONE);
					adapter = new SendNameAdapter(ActSendName.this,
							arrSerFoodList);
					lvFoodName.setAdapter(adapter);
				}
				break;
			}
			llLoad.setVisibility(View.GONE);
		}
	};

	private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			txtSearch.setText("");
			lv.setTempFoodName("");
			Intent intent = new Intent();
			intent.setClass(ActSendName.this, ActSendDinner.class);
			startActivity(intent);
			ActSendName.this.finish();
		}
	};

	private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
		@Override
		public void onClickListenr() {
			coms.initSendCfg(ActSendName.this);
			ActSendName.this.finish();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			txtSearch.setText("");
			lv.setTempFoodName("");
			Intent intent = new Intent();
			intent.setClass(ActSendName.this, ActSendDinner.class);
			startActivity(intent);
			ActSendName.this.finish();
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
}
