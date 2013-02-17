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
import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

public class ActInvite extends ActivityTemplate implements OnClickListener {
	private MainNavigateBar navBar;
	private AlertDialog dlgProgress = null;
	private Thread comitThread = null;
	private BllSend bllSend = null;
	private String strMessage = "";
	private Button btnSend = null;
	private EditText txtName, txtEmail;
	private EditText txtPhone, txtSign;
	private String strName = "", strEmail = "";
	private String strPhone = "", strSign = "";
	private LocalVariable lv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_invite);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			strName = bundle.getString("name");
			strPhone = bundle.getString("number");
			strEmail = bundle.getString("email");
		}
		lv = new LocalVariable(this);
		bllSend = new BllSend(this);
		init_ctrl();
		init_data();
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);

		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(this);
		txtName = (EditText) findViewById(R.id.txtName);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtPhone = (EditText) findViewById(R.id.txtPhone);
		txtSign = (EditText) findViewById(R.id.txtSign);
	}

	private void init_data() {
		txtName.setText(strName);
		txtPhone.setText(strPhone);
		txtEmail.setText(strEmail);
		txtSign.setText(lv.getName());
	}

	private void send() {
		dlgProgress = ProgressDialog.show(ActInvite.this, "请等待...",
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
		strEmail = txtEmail.getText().toString().trim();
		strPhone = txtPhone.getText().toString().trim();
		strSign = txtSign.getText().toString().trim();
		if ((!strPhone.equals("") || !strEmail.equals(""))
				&& !strSign.equals("")) {
			str = bllSend.invitePerson(strEmail, strSign, strPhone);
			if (str.split("#")[0].equals("1")) {
				strMessage = str.split("#")[1];
				b = true;
			} else {
				strMessage = str.split("#")[1];
				handler.sendEmptyMessage(2);
			}
		} else {
			strMessage = "请确认信息填写完整！";
			handler.sendEmptyMessage(2);
		}
		return b;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(ActInvite.this, strMessage, Toast.LENGTH_LONG)
						.show();
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				ActInvite.this.finish();
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActInvite.this, strMessage, Toast.LENGTH_LONG)
						.show();
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSend:
			send();
			break;
		}
	}

	private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActInvite.this.finish();
		}
	};

	private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
		@Override
		public void onClickListenr() {

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
