package com.meishimeike;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mapabc.mapapi.core.GeoPoint;
import com.mapabc.mapapi.map.MapController;
import com.mapabc.mapapi.map.MapView;
import com.mapabc.mapapi.map.MyLocationOverlay;
import com.mapabc.mapapi.map.Overlay;
import com.meishimeike.Bean.BeanLoc;
import com.meishimeike.Bll.BllSend;
import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.MapActivityTemplate;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-7-18 class desc
 */
public class ActNewDinner extends MapActivityTemplate {
	private MainNavigateBar navBar;
	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint point;
	private MyLocationOverlay myloc;
	private Overlay overlay = null;
	private boolean isMarkPush = false, isMarkUp = false;
	private boolean isMarkMove = false;
	private int markX = 100, markY = 100;
	private double mLat = 0;
	private double mLon = 0;
	private EditText txtDinnerName = null;
	private Thread comitThread = null;
	private AlertDialog dlgProgress = null;
	private String strMessage = "", strDinnerName = "";
	private BllSend bllSend = null;
	private LocalVariable lv = null;
	private Geocoder myGeocoder = null;
	private TextView txtAddress = null;
	private Address address = null;
	private String strAddress = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_new_dinner);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			strDinnerName = bundle.getString("DinnerName");
		}
		bllSend = new BllSend(this);
		myGeocoder = new Geocoder(this);
		lv = new LocalVariable(this);
		lv.setTempDinnerName(strDinnerName);
		init_ctrl();
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);

		txtAddress = (TextView) findViewById(R.id.txtAddress);

		txtDinnerName = (EditText) findViewById(R.id.txtDinner);
		txtDinnerName.setText(strDinnerName);

		mMapView = (MapView) findViewById(R.id.mapView);
		mMapView.setBuiltInZoomControls(true);
		mMapController = mMapView.getController();
		mMapController.setZoom(12);

		mMapView.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY;

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				if (!isMarkPush) {
					return false;
				}
				int ea = e.getAction();
				switch (ea) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) e.getRawX();
					lastY = (int) e.getRawY();
					markX = lastX;
					markY = lastY;
					break;
				case MotionEvent.ACTION_MOVE:
					isMarkMove = true;
					int dx = (int) e.getRawX() - lastX;
					int dy = (int) e.getRawY() - lastY;

					int left = lastX + dx;
					int top = lastY + dy;

					if (left < 0) {
						left = 0;
					}

					if (top < 0) {
						top = 0;
					}

					markX = left;
					markY = top;
					mMapView.getOverlays().remove(overlay);
					point = mMapView.getProjection().fromPixels(markX, markY);
					overlay = new MyOverlay(point);
					mMapView.getOverlays().add(overlay);
					mMapView.invalidate();

					lastX = (int) e.getRawX();
					lastY = (int) e.getRawY();
					markX = lastX;
					markY = lastY;

					break;
				case MotionEvent.ACTION_UP:
					isMarkUp = true;
					isMarkPush = false;
					isMarkMove = false;
					mMapView.invalidate();
					getLoc();
					break;
				}
				if (isMarkPush) {
					return true;
				} else {
					return false;
				}
			}
		});

		myloc = new MyLocationOverlay(this, mMapView);
		myloc.enableMyLocation();
		myloc.enableCompass();
		GeoPoint gp = myloc.getMyLocation();
		if (gp.getlongLongitudeE6() == 0 && gp.getlongLatitudeE6() == 0) {
			BeanLoc bean = AppMsmk.getBeanLoc();
			gp = new GeoPoint((int) (Double.valueOf(bean.getLat()) * 1E6),
					(int) (Double.valueOf(bean.getLng()) * 1E6));
		}
		mMapController.setCenter(gp); // 设置地图中心点
		mMapView.getOverlays().add(myloc);

		point = mMapView.getProjection().fromPixels(markX, markY);
		overlay = new MyOverlay(point);
		mMapView.getOverlays().add(overlay);

	}

	private void getLoc() {
		point = mMapView.getProjection().fromPixels(markX, markY);
		mLat = point.getLatitudeE6() / 1E6;
		mLon = point.getLongitudeE6() / 1E6;
		Log.i("", "position:" + mLat + ", " + mLon);
		try {
			List<Address> list = myGeocoder.getFromLocation(mLat, mLon, 1);
			if (list != null && list.size() > 0) {
				address = list.get(0);
				strAddress = address.getAddressLine(1);
				strAddress = "null".equals(strAddress) ? "" : strAddress;
				if (!"".equals(strAddress)) {
					strAddress += address.getAddressLine(2);
				} else {
					strAddress = address.getLocality();
					strAddress += address.getThoroughfare();
					strAddress += address.getFeatureName();
				}
				if (!"".equals(strAddress)) {
					txtAddress.setText(strAddress);
				}
				Log.e("new dinner address", strAddress);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Toast.makeText(ActNewDinner.this, "lng:" + mLon + " lat:" + mLat,
		// Toast.LENGTH_LONG).show();
	}

	class MyOverlay extends Overlay {
		GeoPoint point = null;
		String strText = "";

		public MyOverlay(GeoPoint _point) {
			point = _point;
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// TODO Auto-generated method stub
			super.draw(canvas, mapView, shadow);
			Point screenPts = new Point();
			mapView.getProjection().toPixels(point, screenPts);
			// ---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.img_map_loc);
			canvas.drawBitmap(bmp, screenPts.x - 50, screenPts.y - 260, null);

			Paint paintText = new Paint();
			paintText.setTextSize(18);
			paintText.setColor(Color.RED);
			canvas.drawText(strText, screenPts.x, screenPts.y + 40, paintText); // 绘制文本
		}

		@Override
		public boolean onTap(GeoPoint arg0, MapView arg1) {
			if (isMarkUp) {
				isMarkUp = !isMarkUp;
				isMarkPush = false;
			} else {
				isMarkPush = true;
				mMapView.invalidate();
			}
			return super.onTap(arg0, arg1);
		}
	}

	private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
		@Override
		public void onClickListenr() {
			dlgProgress = ProgressDialog.show(ActNewDinner.this, "请等待...",
					"正在进行操作.....", true);
			dlgProgress.show();
			comitThread = new Thread(runnable);
			comitThread.start();
		}
	};

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			try {
				if (netCheck()) {
					handler.sendEmptyMessage(1);
				}
			} catch (Exception ex) {
				strMessage = ex.getLocalizedMessage();
				handler.sendEmptyMessage(2);
			}
		}
	};

	private boolean netCheck() {
		boolean b = false;
		String str = "";
		strDinnerName = txtDinnerName.getText().toString().trim();
		if (!strDinnerName.equals("")) {
			str = bllSend.newDinner(strDinnerName, String.valueOf(mLat),
					String.valueOf(mLon), strAddress);
			if (str.split("#")[0].equals("1")) {
				strMessage = str.split("#")[1];
				b = true;
			} else {
				strMessage = str.split("#")[1];
				handler.sendEmptyMessage(2);
			}
		} else {
			strMessage = "请填写餐馆名称！";
			handler.sendEmptyMessage(2);
		}
		return b;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Intent intent = new Intent();
				intent.setClass(ActNewDinner.this, ActSendName.class);
				startActivity(intent);
				ActNewDinner.this.finish();
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActNewDinner.this, strMessage, Toast.LENGTH_LONG)
						.show();
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(ActNewDinner.this, ActSendDinner.class);
			startActivity(intent);
			ActNewDinner.this.finish();
		}
		return super.onKeyDown(keyCode, event);
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
