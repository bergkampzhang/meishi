package com.meishimeike;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.Adapter.MsgDialogAdapter;
import com.meishimeike.Bean.BeanMessage;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.Bll.BllSend;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.UserTabWidget.OnUserNavLeftClickListener;
import com.meishimeike.UserTabWidget.OnUserNavRightClickListener;
import com.meishimeike.UserTabWidget.UserNavigateBar;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-6-11 class desc
 */
public class ActUserMsgSend extends ActivityTemplate implements OnClickListener {
	private UserNavigateBar navBar;
	private Button btnLoadMore;
	private TextView btnMsgSend;
	private ListView lvMsg;
	private EditText txtInput;
	private LocalVariable lv = null;
	private AlertDialog dlgProgress = null;
	private String strMessage;
	private Thread comitThread = null, loadThread = null;
	private BllGet bllGet = null;
	private BllSend bllSend = null;
	private MsgDialogAdapter adapter = null;
	private ArrayList<BeanMessage> arrList = null;
	private String strUid = "", strUName = "";
	private String strMsgUid = "", strInput = "";
	private LinearLayout llLoad = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_user_msg_send);
		bllGet = new BllGet(this);
		bllSend = new BllSend(this);
		lv = new LocalVariable(this);
		strUid = lv.getUid();
		strMsgUid = lv.getTempMsgUId();
		strUName = lv.getTempMsgUName();
		init_ctrl();
		loadThread = new Thread(loadRunnable);
		loadThread.start();
		llLoad.setVisibility(View.VISIBLE);
	}

	private void init_ctrl() {
		llLoad = (LinearLayout) findViewById(R.id.llLoad);

		navBar = (UserNavigateBar) findViewById(R.id.NAV);
		navBar.setOnUserNavLeftClickListener(NavLeftClickListener);
		navBar.setOnUserNavRightClickListener(NavRightClickListener);
		navBar.setTitle(strUName);

		btnMsgSend = (TextView) findViewById(R.id.btnMsgSend);
		btnMsgSend.setOnClickListener(this);
		btnLoadMore = (Button) findViewById(R.id.btnLoadMore);
		btnLoadMore.setOnClickListener(this);
		txtInput = (EditText) findViewById(R.id.txtInput);
		lvMsg = (ListView) findViewById(R.id.lvMsg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLoadMore:
			break;
		case R.id.btnMsgSend:
			send();
			break;
		}
	}

	private Runnable loadRunnable = new Runnable() {

		@Override
		public void run() {
			arrList = bllGet.getMessageDialog(strMsgUid);
			handler.sendEmptyMessage(3);
		}
	};

	private void send() {
		dlgProgress = ProgressDialog.show(ActUserMsgSend.this, "请等待...",
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
		strInput = txtInput.getText().toString().trim();
		if (!strInput.equals("")) {
			if ("".equals(strUName)) {
				BeanUserInfo bean = bllGet.getUserInfo(strMsgUid);
				if (bean != null) {
					strUName = bean.getName();
				}
			}

			str = bllSend.sendMsg(strUid, strMsgUid, strInput);
			if (str.split("#")[0].equals("1")) {
				strMessage = str.split("#")[1];
				b = true;
			} else {
				strMessage = str.split("#")[1];
				handler.sendEmptyMessage(2);
			}
		} else {
			strMessage = "不能发送空信息！";
			handler.sendEmptyMessage(2);
		}
		return b;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				loadThread = new Thread(loadRunnable);
				loadThread.start();
				llLoad.setVisibility(View.VISIBLE);
				txtInput.setText("");
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActUserMsgSend.this, strMessage,
						Toast.LENGTH_LONG).show();
				break;
			case 3:
				adapter = new MsgDialogAdapter(ActUserMsgSend.this, arrList);
				lvMsg.setAdapter(adapter);
				llLoad.setVisibility(View.GONE);
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private OnUserNavLeftClickListener NavLeftClickListener = new OnUserNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			// ActUserMsgSend.this.finish();
		}
	};

	private OnUserNavRightClickListener NavRightClickListener = new OnUserNavRightClickListener() {
		@Override
		public void onClickListenr() {
			send();
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
		if (lv.getIsLogin()) {
			if (!lv.getTempUAttention()) {
				Toast.makeText(this, "只有关注后才能发送私信", Toast.LENGTH_LONG).show();
				this.finish();
			}
		} else {
			Toast.makeText(this, "亲~你还没有登录哦", Toast.LENGTH_LONG).show();
			this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		lv.setTempMsgUId("");
		super.onDestroy();
	}

}
