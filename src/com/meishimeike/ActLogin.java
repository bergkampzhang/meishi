package com.meishimeike;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.Bll.BllSend;
import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.LocalVariable;
import com.meishimeike.weibo.sina.ActWeiboGuide;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-5-25 class desc
 */
public class ActLogin extends ActivityTemplate implements OnClickListener {
	private MainNavigateBar navBar;
	private Button btnLogin, btnRegister, btnSina;
	private EditText txtName, txtPwd;
	private Button btnDouban, btnKaixin, btnRenren;
	private TextView txtFindPwd;
	private String strName = "", strPWD = "";
	private BllSend bllSend = null;
	private AlertDialog dlgProgress = null;
	private String strMessage;
	private Thread comitThread = null;
	private LocalVariable lv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_login);
		lv = new LocalVariable(this);
		bllSend = new BllSend(this);
		init_ctrl();
		init_data();
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		btnSina = (Button) findViewById(R.id.btnLoginSina);
		btnSina.setOnClickListener(this);
		btnDouban = (Button) findViewById(R.id.btnLoginDouban);
		btnDouban.setOnClickListener(this);
		btnKaixin = (Button) findViewById(R.id.btnLoginKaixin);
		btnKaixin.setOnClickListener(this);
		btnRenren = (Button) findViewById(R.id.btnLoginRenren);
		btnRenren.setOnClickListener(this);
		txtName = (EditText) findViewById(R.id.txtName);
		txtPwd = (EditText) findViewById(R.id.txtPwd);
		txtFindPwd = (TextView) findViewById(R.id.txtFindPwd);
		txtFindPwd.setOnClickListener(this);
	}

	private void init_data() {
		String str = "";
		String strDesc = "忘记了" + "<font color='red'>" + "密码？" + "</font>";
		txtFindPwd.setText(Html.fromHtml(strDesc));
		str = lv.getLoginName();
		txtName.setText(str.contains("@sinaweibo.com") ? "" : str);
	}

	private void login() {
		dlgProgress = ProgressDialog.show(ActLogin.this, "请等待...",
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
		strPWD = txtPwd.getText().toString().trim();
		if (!strName.equals("") && !strPWD.equals("")) {
			str = bllSend.login(strName, strPWD);
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
				Toast.makeText(ActLogin.this, strMessage, Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent();
				if (lv.getIsNewRegister()) {
					intent.setClass(ActLogin.this, ActSearchUser.class);
					lv.setIsNewRegister(false);
					startActivity(intent);
				}
				// else {
				// intent.setClass(ActLogin.this, ActPerson.class);
				// }
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				lv.setIsNewLogin(true);
				ActLogin.this.finish();
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActLogin.this, strMessage, Toast.LENGTH_LONG)
						.show();
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btnLogin:
			login();
			break;
		case R.id.btnRegister:
			intent = new Intent();
			intent.setClass(ActLogin.this, ActRegister.class);
			startActivityForResult(intent, 100);
			break;
		case R.id.btnLoginSina:
			intent = new Intent();
			intent.setClass(ActLogin.this, ActWeiboGuide.class);
			startActivity(intent);
			lv.setIsWLogin(true);
			ActLogin.this.finish();
			break;
		case R.id.btnLoginDouban:
			break;
		case R.id.btnLoginKaixin:
			break;
		case R.id.btnLoginRenren:
			break;
		case R.id.txtFindPwd:
			intent = new Intent();
			intent.setClass(ActLogin.this, ActFindPwd.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			init_data();
			login();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActLogin.this.finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			Intent intent = new Intent();
			intent.setClass(ActLogin.this, ActSetting.class);
			startActivity(intent);
		}
	};

	private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
		@Override
		public void onClickListenr() {
			ActLogin.this.finish();
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
