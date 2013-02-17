package com.meishimeike;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.Bean.BeanDinner;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.DishTabWidget.DishNavigateBar;
import com.meishimeike.DishTabWidget.OnDishNavLeftClickListener;
import com.meishimeike.DishTabWidget.OnDishNavRightClickListener;
import com.meishimeike.Template.MapActivityTemplate;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;
import com.meishimeike.View.DinnerDescView;
import com.meishimeike.View.DinnerMapView;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-5-24 class desc
 */
public class ActDinner extends MapActivityTemplate implements OnClickListener {
	public static final String KEY_DID = "KEY_DID";
	private DishNavigateBar navBar;
	private RelativeLayout rlDinnerDesc;
	private BllGet bllGet = null;
	private BeanDinner beanDinner = null;
	private Button btnMap, btnDesc;
	private TextView txtDName, txtDAddress, txtDPhone;
	private ImageView imgDinner;
	private LocalVariable lv = null;
	private Bitmap bitmap = null;
	private String strDid = "", strDAddr = "", strDPhone = "";
	private String strDName = "", strImgUrl = "", strPhoneNum = "";
	private double lng = 0, lat = 0;
	private DinnerDescView descView = null;
	private DinnerMapView mapView = null;
	private ScrollView sl = null;
	private LinearLayout llLoad = null;
	private Commons coms = null;
	private LayoutParams lp = null;
	private LinearLayout ll_Dinner = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_dinner);
		lv = new LocalVariable(this);
		coms = new Commons();
		strDid = lv.getTempDId();
		bllGet = new BllGet(this);
		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		ll_Dinner = (LinearLayout) findViewById(R.id.ll_act_dinner);
		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		navBar = (DishNavigateBar) findViewById(R.id.DishNAV);
		navBar.setOnDishNavLeftClickListener(NavLeftClickListener);
		navBar.setOnDishNavRightClickListener(NavRightClickListener);
		rlDinnerDesc = (RelativeLayout) findViewById(R.id.rlDinnerDesc);
		btnMap = (Button) findViewById(R.id.btnMap);
		btnMap.setOnClickListener(this);
		btnDesc = (Button) findViewById(R.id.btnDesc);
		btnDesc.setOnClickListener(this);
		txtDName = (TextView) findViewById(R.id.txtDName);
		txtDAddress = (TextView) findViewById(R.id.txtDAddress);
		txtDPhone = (TextView) findViewById(R.id.txtDPhone);
		txtDPhone.setOnClickListener(this);
		imgDinner = (ImageView) findViewById(R.id.imgDinner);
		sl = (ScrollView) findViewById(R.id.sl);
	}

	private void init_data() {
		beanDinner = bllGet.getDinnerInfo(strDid);
		if (beanDinner != null) {
			strDName = beanDinner.getName();
			strDAddr = beanDinner.getAddress();
			if (strDAddr.contains("null")) {
				strDAddr = "";
			}
			strPhoneNum = beanDinner.getTel();
			strDPhone = strPhoneNum;
			strImgUrl = "";
			lng = beanDinner.getLng();
			lat = beanDinner.getLat();
		}
	}

	private void setCtrlData() {
		lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);

		navBar.setTitle(strDName);

		txtDName.setText(strDName);
		txtDAddress.setText(strDAddr);
		if ("".equals(strDAddr)) {
			txtDAddress.setText("无");
		}
		txtDPhone.setText(strDPhone);
		if ("".equals(strDPhone)) {
			txtDPhone.setText("无");
		}
		if (!"".equals(strImgUrl)) {
			imgDinner.setTag(strImgUrl);
			ImageDownloadItem item = new ImageDownloadItem();
			item.imageUrl = strImgUrl;
			item.callback = new ImageDownloadCallback() {
				@Override
				public void update(Bitmap bitmap, String imageUrl) {
					ImageView imageViewByTag = (ImageView) ll_Dinner
							.findViewWithTag(imageUrl);
					if (imageViewByTag != null)
						imageViewByTag.setImageBitmap(bitmap);
					bitmap = null;
				}
			};
			ImageDownloadThread imageDownloadThread = ImageDownloadThread
					.getInstance();
			bitmap = imageDownloadThread.downloadWithCache(item);
			if (bitmap != null) {// 从缓存中取到
				imgDinner.setImageBitmap(bitmap);
			}
		}
		bitmap = null;

		descView = new DinnerDescView(this, null, strDid, llLoad);
		descView.setVisibility(View.VISIBLE);
		rlDinnerDesc.addView(descView, lp);

		mapView = new DinnerMapView(this, null, lng, lat);
		mapView.setVisibility(View.GONE);
		rlDinnerDesc.addView(mapView, lp);
	}

	@Override
	public void onClick(View v) {
		int resId = v.getId();
		changeImage(resId);
		switch (resId) {
		case R.id.btnMap:
			descView.setVisibility(View.GONE);
			mapView.setVisibility(View.VISIBLE);
			sl.setEnabled(false);
			rlDinnerDesc.postInvalidate();
			break;
		case R.id.btnDesc:
			descView.setVisibility(View.VISIBLE);
			mapView.setVisibility(View.GONE);
			sl.setEnabled(true);
			rlDinnerDesc.postInvalidate();
			break;
		case R.id.txtDPhone:
			if (!"".equals(strPhoneNum)) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ strPhoneNum));
				startActivity(intent);
			}
			break;
		}

	}

	private OnDishNavLeftClickListener NavLeftClickListener = new OnDishNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActDinner.this.finish();
		}
	};

	private OnDishNavRightClickListener NavRightClickListener = new OnDishNavRightClickListener() {
		@Override
		public void onClickListenr() {
			if (lv.getIsLogin()) {
				lv.setTempDinnerId(strDid);
				lv.setTempDinnerName(strDName);
				Intent intent = new Intent();
				intent.setClass(ActDinner.this, ActGetPic.class);
				startActivity(intent);
				coms.initSendCfgButDinner(ActDinner.this);
			} else {
				Toast.makeText(ActDinner.this, "亲~你还没登录呢", Toast.LENGTH_SHORT)
						.show();
			}
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
			if (beanDinner != null) {
				setCtrlData();
			} else {
				Toast.makeText(ActDinner.this, "餐馆信息有误，请稍候重试 id:" + strDid,
						Toast.LENGTH_SHORT).show();
			}
			llLoad.setVisibility(View.GONE);
		}
	}

	private void changeImage(int resId) {
		switch (resId) {
		case R.id.btnMap:
			btnMap.setBackgroundResource(R.drawable.btn_dinner_map_focus);
			btnDesc.setBackgroundResource(R.drawable.btn_dinner_desc);
			break;
		case R.id.btnDesc:
			btnMap.setBackgroundResource(R.drawable.btn_dinner_map);
			btnDesc.setBackgroundResource(R.drawable.btn_dinner_desc_focus);
			break;
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
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}

}
