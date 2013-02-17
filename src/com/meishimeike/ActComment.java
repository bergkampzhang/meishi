package com.meishimeike;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meishimeike.Bll.BllSend;
import com.meishimeike.DishTabWidget.DishNavigateBar;
import com.meishimeike.DishTabWidget.OnDishNavLeftClickListener;
import com.meishimeike.DishTabWidget.OnDishNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.umeng.analytics.MobclickAgent;

public class ActComment extends ActivityTemplate {
	public static final String CUR_FID = "cur_fid";
	public static final String CUR_UID = "cur_uid";
	public static final String CUR_RID = "cur_rid";
	public static final String CUR_AUID = "cur_auid";
	private DishNavigateBar navBar;
	private String strFid = "";
	private String strUid = "";// 琚瘎璁轰汉id锛岄粯璁�=鐧诲綍uid
	private String strRid = "";// 鍥炲鐨勮瘎璁篿d锛岄粯璁�
	private String strAUid = "";
	private String strContent = "";
	private BllSend bllSend = null;
	private EditText txtComment;
	private AlertDialog dlgProgress = null;
	private String strMessage;
	private Thread comitThread = null;
	private Button btnSend = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_comment);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			strFid = bundle.getString(CUR_FID);
			strAUid = bundle.getString(CUR_AUID);
			strUid = bundle.getString(CUR_UID);
			strRid = bundle.getString(CUR_RID);
		}
		bllSend = new BllSend(this);
		init_ctrl();
	}

	private void init_ctrl() {
		navBar = (DishNavigateBar) findViewById(R.id.DishNAV);
		navBar.setOnDishNavLeftClickListener(NavLeftClickListener);
		navBar.setOnDishNavRightClickListener(NavRightClickListener);
		txtComment = (EditText) findViewById(R.id.txtComment);
		btnSend = (Button) findViewById(R.id.btnOk);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();
			}
		});
	}

	private OnDishNavLeftClickListener NavLeftClickListener = new OnDishNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActComment.this.finish();
		}
	};

	private OnDishNavRightClickListener NavRightClickListener = new OnDishNavRightClickListener() {
		@Override
		public void onClickListenr() {
			login();
		}
	};

	private void login() {
		dlgProgress = ProgressDialog.show(this, "璇风瓑寰�..", "姝ｅ湪杩涜鎿嶄綔.....", true);
		dlgProgress.show();
		comitThread = new Thread() {
			public void run() {
				try {
					if (dataCheck()) {
						handler.sendEmptyMessage(1);
					}
				} catch (Exception ex) {
					strMessage = ex.getLocalizedMessage();
					handler.sendEmptyMessage(2);
				}
			};
		};
		comitThread.start();
	}

	private boolean dataCheck() {
		boolean b = false;
		String str = "";
		strContent = txtComment.getText().toString().trim();
		if (!strContent.equals("")) {
			str = bllSend.comment(strFid, strAUid, strUid, strRid, strContent);
			if (str.split("#")[0].equals("1")) {
				strMessage = str.split("#")[1];
				b = true;
			} else {
				strMessage = str.split("#")[1];
				handler.sendEmptyMessage(2);
			}
		} else {
			strMessage = "璇勮鍐呭涓嶈兘涓虹┖锛�";
			handler.sendEmptyMessage(2);
		}
		return b;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(ActComment.this, strMessage, Toast.LENGTH_LONG)
						.show();
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				ActComment.this.finish();
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActComment.this, strMessage, Toast.LENGTH_LONG)
						.show();
				break;
			}
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
