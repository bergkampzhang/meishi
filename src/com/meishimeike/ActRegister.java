package com.meishimeike;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meishimeike.Bll.BllSend;
import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-5-25 class desc
 */
public class ActRegister extends ActivityTemplate implements OnClickListener {
	private MainNavigateBar navBar;
	private Button btnOk;
	private EditText txtName, txtName2;
	private EditText txtPwd, txtPwd2;
	private String strName = "", strName2 = "";
	private String strPwd = "", strPwd2 = "";
	private BllSend bllSend = null;
	private AlertDialog dlgProgress = null;
	private String strMessage;
	private Thread comitThread = null;
	private LocalVariable lv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_register);
		bllSend = new BllSend(this);
		lv = new LocalVariable(this);
		init_ctrl();
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		txtName = (EditText) findViewById(R.id.txtName);
		txtName2 = (EditText) findViewById(R.id.txtName2);
		txtPwd = (EditText) findViewById(R.id.txtPwd);
		txtPwd2 = (EditText) findViewById(R.id.txtPwd2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOk:
			register();
			break;
		}
	}

	private void register() {
		dlgProgress = ProgressDialog.show(ActRegister.this, "请等待...",
				"正在进行操作.....", true);
		dlgProgress.show();
		comitThread = new Thread() {
			public void run() {
				try {
					if (netCheck()) {
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

	private boolean netCheck() {
		boolean b = false;
		String str = "";
		strName = txtName.getText().toString().trim();
		strName2 = txtName2.getText().toString().trim();
		strPwd = txtPwd.getText().toString().trim();
		strPwd2 = txtPwd2.getText().toString().trim();
		if (!strName.equals("") && !strPwd.equals("") && !strName.equals("")
				&& !strName2.equals("")) {
			str = bllSend.register(strName, strName2, strPwd, strPwd2);
			if (str.split("#")[0].equals("1")) {
				strMessage = str.split("#")[1];
				b = true;
			} else {
				strMessage = str.split("#")[1];
				handler.sendEmptyMessage(2);
			}
		} else {
			strMessage = "请确认登录信息填写完整！";
			handler.sendEmptyMessage(2);
		}
		return b;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				lv.setIsNewRegister(true);
				Toast.makeText(ActRegister.this, strMessage, Toast.LENGTH_LONG)
						.show();
				Intent i = new Intent();
				ActRegister.this.setResult(RESULT_OK, i);
				ActRegister.this.finish();
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				ActRegister.this.finish();
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActRegister.this, strMessage, Toast.LENGTH_LONG)
						.show();
				break;
			}
		}
	};

	private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActRegister.this.finish();
		}
	};

	private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
		@Override
		public void onClickListenr() {
			Intent intent = new Intent();
			intent.setClass(ActRegister.this, ActSetting.class);
			startActivity(intent);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

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
