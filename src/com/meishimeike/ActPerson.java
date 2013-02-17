package com.meishimeike;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meishimeike.Adapter.TaTagNumAdapter;
import com.meishimeike.Bean.BeanTagNum;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.UserTabWidget.OnUserNavLeftClickListener;
import com.meishimeike.UserTabWidget.OnUserNavRightClickListener;
import com.meishimeike.UserTabWidget.UserNavigateBar;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;
import com.meishimeike.View.PersonDescView;
import com.umeng.analytics.MobclickAgent;

public class ActPerson extends ActivityTemplate {
	private UserNavigateBar navBar;
	private RelativeLayout rlPersonDesc;
	private BllGet bllGet = null;
	private BeanUserInfo beanUser = null;
	private TextView txtName, txtAddress, txtSign;
	private TextView txtNumAttention, txtNumFriend;
	private ImageView imgHead;
	private LocalVariable lv = null;
	private Bitmap bitmap = null;
	private String strUid = "", strAddr = "", strSign = "", strGender = "";
	private String strName = "", strImgUrl = "", strNumFriend = "";
	private String strNumAttention = "";
	private PersonDescView descView = null;
	private ArrayList<BeanTagNum> arrList = null;
	// private LinearLayout llTagNum = null;
	private LinearLayout llLoad = null;
	private TaTagNumAdapter adapter = null;
	private GridView gvTag = null;
	private LinearLayout ll_Person = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_person);
		lv = new LocalVariable(this);
		strUid = lv.getUid();
		bllGet = new BllGet(this);
		init_ctrl();
	}

	private void init_ctrl() {
		ll_Person = (LinearLayout) findViewById(R.id.ll_act_person);
		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		navBar = (UserNavigateBar) findViewById(R.id.NAV);
		navBar.setOnUserNavLeftClickListener(NavLeftClickListener);
		navBar.setOnUserNavRightClickListener(NavRightClickListener);
		rlPersonDesc = (RelativeLayout) findViewById(R.id.rlPersonDesc);
		txtName = (TextView) findViewById(R.id.txtName);
		txtAddress = (TextView) findViewById(R.id.txtAddress);
		txtSign = (TextView) findViewById(R.id.txtSign);
		imgHead = (ImageView) findViewById(R.id.imgHead);
		txtNumAttention = (TextView) findViewById(R.id.txtNumAttention);
		txtNumFriend = (TextView) findViewById(R.id.txtNumFriend);

		// llTagNum = (LinearLayout) findViewById(R.id.llTagNum);

		gvTag = (GridView) findViewById(R.id.gvTag);
	}

	private void init_data() {
		beanUser = bllGet.getUserInfo(strUid);
		if (beanUser != null) {
			strAddr = beanUser.getCity();
			strGender = beanUser.getGender_name();
			if (strGender.contains("保密")) {
				strGender = "";
			}
			strName = beanUser.getName();
			strSign = beanUser.getIntro();
			if ("".equals(strSign) || "null".equals(strSign)) {
				strSign = "";
			}
			strImgUrl = beanUser.getPhoto_180_url();
			strNumAttention = "关注  " + beanUser.getFollow_num();
			strNumFriend = "粉丝  " + beanUser.getFans_num();
		}
		arrList = bllGet.getTagNum(strUid);
	}

	private void setCtrlData() {
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);

		txtName.setText(strName);
		if (!"".equals(strName)) {
			navBar.setTitle(strName);
		}
		txtAddress.setText(strAddr + "　" + strGender);
		if ("".equals(strAddr)) {
			txtAddress.setCompoundDrawables(null, null, null, null);
		} else {
			txtAddress.setCompoundDrawables(
					getResources().getDrawable(R.drawable.img_common_dinner),
					null, null, null);
		}
		txtSign.setText(strSign);
		txtNumAttention.setText(strNumAttention);
		txtNumFriend.setText(strNumFriend);

		if (!"".equals(strImgUrl)) {
			imgHead.setTag(strImgUrl);
			ImageDownloadItem item = new ImageDownloadItem();
			item.imageUrl = strImgUrl;
			item.callback = new ImageDownloadCallback() {
				@Override
				public void update(Bitmap bitmap, String imageUrl) {
					ImageView imageViewByTag = (ImageView) ll_Person
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
				imgHead.setImageBitmap(bitmap);
			}
		}
		bitmap = null;

		descView = new PersonDescView(this, null, strUid, llLoad);
		descView.setVisibility(View.VISIBLE);
		rlPersonDesc.addView(descView, lp);

		adapter = new TaTagNumAdapter(this, arrList);
		gvTag.setAdapter(adapter);
		int totalHeight = 0;
		int count = adapter.getCount();
		int item = 0;
		for (int i = 0; i < count; i++) {
			View listItem = adapter.getView(i, null, gvTag);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			item = listItem.getMeasuredHeight();
		}
		totalHeight = (totalHeight / 2) + (count % 2 > 0 ? 1 : 0) * item;
		ViewGroup.LayoutParams params = gvTag.getLayoutParams();
		params.height = totalHeight;
		gvTag.setLayoutParams(params);
	}

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
			if (beanUser != null) {
				setCtrlData();
			} else {
				llLoad.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// coms.ExitSys(ActPerson.this);
			return super.onKeyDown(keyCode, event);
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private OnUserNavLeftClickListener NavLeftClickListener = new OnUserNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			Intent intent = new Intent();
			intent.setClass(ActPerson.this, ActSetting.class);
			startActivity(intent);
		}
	};

	private OnUserNavRightClickListener NavRightClickListener = new OnUserNavRightClickListener() {
		@Override
		public void onClickListenr() {
			Intent intent = new Intent();
			intent.setClass(ActPerson.this, ActPersonModify.class);
			startActivity(intent);
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
		strUid = lv.getUid();
		if ("".equals(strUid)) {
			Intent intent = new Intent();
			intent.setClass(this, ActLogin.class);
			startActivity(intent);
			ActPerson.this.finish();
		}
		new myTask().execute((Void) null);
	}
}
