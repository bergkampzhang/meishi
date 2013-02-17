package com.meishimeike;

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
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-5-28 class desc
 */
public class ActSendDesc extends ActivityTemplate implements OnClickListener {
	private MainNavigateBar navBar;
	private EditText txtSendDesc, txtPrice;
	private Button btnSend;
	private BllSend bllSend = null;
	private AlertDialog dlgProgress = null;
	private String strMessage;
	private Thread comitThread = null;
	private String strDesc = "", strPrice = "";
	private Commons coms = null;
	private LocalVariable lv = null;
	private TextView txtTitle = null;
	private String strTitle = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_send_desc);
		coms = new Commons();
		lv = new LocalVariable(this);
		bllSend = new BllSend(this);
		init_ctrl();
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);

		txtSendDesc = (EditText) findViewById(R.id.txtSendDesc);
		txtPrice = (EditText) findViewById(R.id.txtPrice);
		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(this);

		txtTitle = (TextView) findViewById(R.id.txtSendTitle);
		strTitle = "您分享了  <font color='#000000'>" + lv.getTempFoodName()
				+ "</font> 在 " + "<font color='#000000'>"
				+ lv.getTempDinnerName() + "</font>";
		txtTitle.setText(Html.fromHtml(strTitle));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSend:
			send();
			break;
		}
	}

	private void send() {
		dlgProgress = ProgressDialog.show(ActSendDesc.this, "请等待...",
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
		strDesc = txtSendDesc.getText().toString().trim();
		strPrice = txtPrice.getText().toString().trim();
		if (!strDesc.equals("") && !strPrice.equals("")) {
			str = bllSend.sendFood(strDesc, strPrice);
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
				Toast.makeText(ActSendDesc.this, strMessage, Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent(ActSendDesc.this, ActMain.class);
				startActivity(intent);
				coms.initSendCfg(ActSendDesc.this);
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				lv.setIsNewPublish(true);
				ActSendDesc.this.finish();
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActSendDesc.this, strMessage, Toast.LENGTH_LONG)
						.show();
				break;
			}
		}
	};

	private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			txtSendDesc.setText("");
			txtPrice.setText("");
			Intent intent = new Intent();
			intent.setClass(ActSendDesc.this, ActSendName.class);
			startActivity(intent);
			ActSendDesc.this.finish();
		}
	};

	private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
		@Override
		public void onClickListenr() {
			coms.initSendCfg(ActSendDesc.this);
			ActSendDesc.this.finish();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			txtSendDesc.setText("");
			txtPrice.setText("");
			Intent intent = new Intent();
			intent.setClass(ActSendDesc.this, ActSendName.class);
			startActivity(intent);
			ActSendDesc.this.finish();
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
	}

}
