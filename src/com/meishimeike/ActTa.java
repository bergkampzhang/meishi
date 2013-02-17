package com.meishimeike;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.Adapter.TaTagNumAdapter;
import com.meishimeike.Bean.BeanTagNum;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.Bll.BllSend;
import com.meishimeike.TaTabWidget.OnTaNavLeftClickListener;
import com.meishimeike.TaTabWidget.OnTaNavRightClickListener;
import com.meishimeike.TaTabWidget.TaNavigateBar;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;
import com.meishimeike.View.PersonDescView;
import com.umeng.analytics.MobclickAgent;

public class ActTa extends ActivityTemplate implements OnClickListener {
	public static final String KEY_UID = "KEY_UID";
	private TaNavigateBar navBar;
	private RelativeLayout rlPersonDesc;
	private BllGet bllGet = null;
	private BeanUserInfo beanUser = null;
	private TextView txtName, txtAddress, txtSign;
	private TextView txtNumAttention, txtNumFriend;
	private ImageView imgHead;
	private Button btnAttention;
	private LocalVariable lv = null;
	private Bitmap bitmap = null;
	private String strUid = "", strAddr = "", strSign = "", strGender = "";
	private String strName = "", strImgUrl = "", strNumFriend = "";
	private String strNumAttention = "";
	private PersonDescView descView = null;
	private ArrayList<BeanTagNum> arrList = null;
	// private LinearLayout llTagNum = null;
	private AlertDialog dlgProgress = null;
	private String strMessage;
	private boolean isAttention = false;
	private BllSend bllSend = null;
	private LinearLayout llLoad = null, llTa = null;
	private TaTagNumAdapter adapter = null;
	private GridView gvTag = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_ta);
		lv = new LocalVariable(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			strUid = bundle.getString(KEY_UID);
			lv.setTempUId(strUid);
		} else {
			strUid = lv.getTempUId();
		}
		lv.setTempMsgUId(strUid);
		bllGet = new BllGet(this);
		bllSend = new BllSend(this);
		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		llTa = (LinearLayout) findViewById(R.id.ll_act_ta);
		llLoad = (LinearLayout) findViewById(R.id.llLoad);

		navBar = (TaNavigateBar) findViewById(R.id.NAV);
		navBar.setOnTaNavLeftClickListener(NavLeftClickListener);
		navBar.setOnTaNavRightClickListener(NavRightClickListener);
		rlPersonDesc = (RelativeLayout) findViewById(R.id.rlPersonDesc);
		txtName = (TextView) findViewById(R.id.txtName);
		txtAddress = (TextView) findViewById(R.id.txtAddress);
		txtSign = (TextView) findViewById(R.id.txtSign);
		imgHead = (ImageView) findViewById(R.id.imgHead);
		txtNumAttention = (TextView) findViewById(R.id.txtNumAttention);
		txtNumFriend = (TextView) findViewById(R.id.txtNumFriend);

		// llTagNum = (LinearLayout) findViewById(R.id.llTagNum);

		btnAttention = (Button) findViewById(R.id.btnAttention);
		btnAttention.setOnClickListener(this);
		btnAttention.setVisibility(View.VISIBLE);

		gvTag = (GridView) findViewById(R.id.gvTag);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAttention:
			if (lv.getIsLogin()) {
				if (isAttention) {
					Toast.makeText(ActTa.this, "该用户已关注", Toast.LENGTH_SHORT)
							.show();
				} else {
					comit();
				}
			} else {
				Toast.makeText(ActTa.this, "请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		}

	}

	private void init_data() {
		beanUser = bllGet.getUserInfo(strUid);
		if (lv.getIsLogin()) {
			lv.setTempUAttention(beanUser.isAttention());
		}
		if (beanUser != null) {
			strAddr = beanUser.getCity();
			strGender = beanUser.getGender_name();
			if (strGender.contains("保密")) {
				strGender = "";
			}
			strName = beanUser.getName();
			lv.setTempMsgUName(strName);
			strSign = beanUser.getIntro();
			if ("".equals(strSign) || "null".equals(strSign)) {
				strSign = "";
			} else {
				strSign = "<b>" + strSign + "</b>";
			}
			strImgUrl = beanUser.getPhoto_180_url();
			isAttention = beanUser.isAttention();
			strNumAttention = "关注  " + beanUser.getFollow_num();
			strNumFriend = "粉丝  " + beanUser.getFans_num();
		}
		arrList = bllGet.getTagNum(strUid);
	}

	private void setCtrlData() {
		if (isAttention) {
			btnAttention.setText("已关注");
		} else {
			btnAttention.setText("关注");
		}

		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);

		txtName.setText(strName);
		if (!"".equals(strName)) {
			navBar.setTitle(strName);
		}
		txtAddress.setText(strGender + "　" + strAddr);
		if ("".equals(strAddr)) {
			txtAddress.setCompoundDrawables(null, null, null, null);
		} else {
			txtAddress.setCompoundDrawables(getResources().getDrawable(
					R.drawable.img_common_dinner), null, null, null);
		}
		txtSign.setText(Html.fromHtml(strSign));
		txtNumAttention.setText(strNumAttention);
		txtNumFriend.setText(strNumFriend);

		if (!"".equals(strImgUrl)) {
			imgHead.setTag(strImgUrl);
			ImageDownloadItem item = new ImageDownloadItem();
			item.imageUrl = strImgUrl;
			item.callback = new ImageDownloadCallback() {
				@Override
				public void update(Bitmap bitmap, String imageUrl) {
					ImageView imageViewByTag = (ImageView) llTa
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

			bitmap = null;
		}

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
			llLoad.setVisibility(View.VISIBLE);
			super.onPreExecute();
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

	private Thread comitThread = null;

	private void comit() {
		dlgProgress = ProgressDialog.show(ActTa.this, "请等待...", "正在进行操作.....",
				true);
		dlgProgress.show();
		comitThread = new Thread() {
			public void run() {
				try {
					strMessage = bllSend.Attention(strUid);
					if (strMessage.split("#")[0].equals("1")) {
						isAttention = true;
					}
					if (strMessage.split("#")[0].equals("1")) {
						strMessage = strMessage.split("#")[1];
						handler.sendEmptyMessage(1);
					} else {
						strMessage = strMessage.split("#")[1];
						handler.sendEmptyMessage(2);
					}
				} catch (Exception ex) {
					strMessage = ex.getLocalizedMessage();
					handler.sendEmptyMessage(2);
				}
			};
		};
		comitThread.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (isAttention) {
					btnAttention.setText("已关注");
				} else {
					btnAttention.setText("关注");
				}
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActTa.this, "成功!" + strMessage,
						Toast.LENGTH_LONG).show();
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActTa.this, "失败!" + strMessage,
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	private OnTaNavLeftClickListener NavLeftClickListener = new OnTaNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActTa.this.finish();
		}
	};

	private OnTaNavRightClickListener NavRightClickListener = new OnTaNavRightClickListener() {
		@Override
		public void onClickListenr() {
			Intent intent = new Intent();
			intent.setClass(ActTa.this, ActMain.class);
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
	}

}
