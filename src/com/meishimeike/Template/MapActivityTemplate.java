package com.meishimeike.Template;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.mapabc.mapapi.map.MapActivity;

public class MapActivityTemplate extends MapActivity {
	public static final String CLOSE_AC = "CloseAllAct";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(this.broadcastReceiver);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(CLOSE_AC);
		registerReceiver(this.broadcastReceiver, filter);
		super.onResume();
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			((Activity) context).finish();
		}
	};
}
