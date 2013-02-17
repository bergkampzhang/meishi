package com.meishimeike;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleAdapter;

import com.meishimeike.Utils.LocalVariable;
import com.meishimeike.View.MyGallery;
import com.umeng.analytics.MobclickAgent;

public class ActExplan extends Activity {
	private MyGallery gallery;
	private ArrayList<HashMap<String, Integer>> list;
	private LocalVariable lv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_explan);
		lv = new LocalVariable(this);
	}

	private void init_ctrl() {
		getdata();
		gallery = (MyGallery) findViewById(R.id.gallery);
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.item_explan, new String[] { "path" },
				new int[] { R.id.imageView });
		gallery.setAdapter(adapter);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if (pos > 3) {
					Intent intent = new Intent();
					intent.setClass(ActExplan.this, ActMain.class);
					startActivity(intent);
					ActExplan.this.finish();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void getdata() {
		list = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Integer> map = null;
		map = new HashMap<String, Integer>();
		map.put("path", R.drawable.bg_explan_1);
		list.add(map);
		map = new HashMap<String, Integer>();
		map.put("path", R.drawable.bg_explan_2);
		list.add(map);
		map = new HashMap<String, Integer>();
		map.put("path", R.drawable.bg_explan_3);
		list.add(map);
		map = new HashMap<String, Integer>();
		map.put("path", R.drawable.bg_explan_4);
		list.add(map);
		map = new HashMap<String, Integer>();
		map.put("path", R.drawable.bg_common_pic);
		list.add(map);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(ActExplan.this, ActMain.class);
			startActivity(intent);
			ActExplan.this.finish();
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
		if (lv.getIsNotFirst()) {
			Intent intent = new Intent();
			intent.setClass(ActExplan.this, ActMain.class);
			startActivity(intent);
			ActExplan.this.finish();
		} else {
			init_ctrl();
			lv.setIsNotFirst(true);
		}
	}
}
