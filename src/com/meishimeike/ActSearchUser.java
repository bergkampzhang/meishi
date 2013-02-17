package com.meishimeike;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.Adapter.SearchUserAdapter;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Bll.BllGet;
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
 * @version ：2012-6-7 class desc
 */
public class ActSearchUser extends ActivityTemplate implements OnClickListener {
	private MainNavigateBar navBar;
	private LinearLayout llLoad = null;
	private ListView lvUser;
	private ImageView imgSearch;
	private EditText txtSearch;
	private TextView txtDesc = null;
	private LinearLayout llContacts, llOther, llSina;
	private LinearLayout llBeforeSearch, llAfterSearch;
	private AlertDialog dlgProgress = null;
	private Thread searchThread = null;
	private Thread commitThread = null;
	private ArrayList<BeanUserInfo> arrList = null;
	private SearchUserAdapter adapter = null;
	private BllGet bllGet = null;
	private BllSend bllSend = null;
	private LocalVariable lv = null;
	private String strSearch = "", strMessage = "";
	private String strDesc = "", strUId = "";
	private Button btnAttention = null;
	private BeanUserInfo tempBean = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_search_user);
		init_ctrl();
		bllGet = new BllGet(this);
		bllSend = new BllSend(this);
		lv = new LocalVariable(this);
		searchThread = new Thread(searchRunnable);
		searchThread.start();
		llLoad.setVisibility(View.VISIBLE);
	}

	private void init_ctrl() {
		navBar = (MainNavigateBar) findViewById(R.id.NAV);
		navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
		navBar.setOnMainNavRightClickListener(NavRightClickListener);

		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		llContacts = (LinearLayout) findViewById(R.id.llContacts);
		llContacts.setOnClickListener(this);
		llOther = (LinearLayout) findViewById(R.id.llOther);
		llOther.setOnClickListener(this);
		llSina = (LinearLayout) findViewById(R.id.llSina);
		llSina.setOnClickListener(this);

		llBeforeSearch = (LinearLayout) findViewById(R.id.llBeforeSearch);
		llAfterSearch = (LinearLayout) findViewById(R.id.llAfterSearch);

		txtDesc = (TextView) findViewById(R.id.txtDesc);
		imgSearch = (ImageView) findViewById(R.id.imgSearch);
		imgSearch.setOnClickListener(this);
		txtSearch = (EditText) findViewById(R.id.txtSearch);

		lvUser = (ListView) findViewById(R.id.lvUser);
	}

	private void init_data() {
		strSearch = txtSearch.getText().toString();
		if ("".equals(strSearch)) {
			arrList = bllGet.getRecommentdUser(lv.getUid());
			strDesc = "推荐关注";
		} else {
			arrList = bllGet.searchUser(strSearch);
			strDesc = "\"" + strSearch + "\"的搜索结果";
		}
	}

	private void setCtrlData() {
		if ("".equals(strSearch)) {
			llBeforeSearch.setVisibility(View.VISIBLE);
			llAfterSearch.setVisibility(View.GONE);
		} else {
			llBeforeSearch.setVisibility(View.GONE);
			llAfterSearch.setVisibility(View.VISIBLE);
		}
		adapter = new SearchUserAdapter(this, arrList);
		lvUser.setAdapter(adapter);
		txtDesc.setText(strDesc);
	}

	private Runnable searchRunnable = new Runnable() {

		@Override
		public void run() {
			init_data();
			handler.sendEmptyMessage(1);
		}
	};

	private Runnable commitRunnable = new Runnable() {

		@Override
		public void run() {
			if (dataCheck()) {
				handler.sendEmptyMessage(2);
			}
		}
	};

	@SuppressWarnings("unused")
	private void send() {
		dlgProgress = ProgressDialog.show(this, "请等待...", "正在进行操作.....", true);
		dlgProgress.show();
		commitThread = new Thread(commitRunnable);
		commitThread.start();
	}

	private boolean dataCheck() {
		boolean b = false;
		String str = "";
		str = bllSend.Attention(strUId);
		if (str.split("#")[0].equals("1")) {
			strMessage = str.split("#")[1];
			b = true;
		} else {
			strMessage = str.split("#")[1];
			handler.sendEmptyMessage(3);
		}
		return b;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setCtrlData();
				break;
			case 2:
				// commit success
				Toast.makeText(ActSearchUser.this, strMessage,
						Toast.LENGTH_LONG).show();
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				tempBean.setAttention(true);
				btnAttention.setText("已关注");
				adapter.notifyDataSetChanged();
				break;
			case 3:
				// commit fail
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActSearchUser.this, strMessage,
						Toast.LENGTH_LONG).show();
				break;
			}
			llLoad.setVisibility(View.GONE);
		}
	};

	@Override
	public void onClick(View v) {
		int resId = v.getId();
		Intent i = null;
		switch (resId) {
		case R.id.imgSearch:
			strSearch = txtSearch.getText().toString();
			searchThread = new Thread(searchRunnable);
			searchThread.start();
			llLoad.setVisibility(View.VISIBLE);
			break;
		case R.id.llContacts:
			i = new Intent();
			i.setAction(Intent.ACTION_GET_CONTENT);
			i.setData(Contacts.CONTENT_URI);
			i.setType("vnd.android.cursor.item/phone");
			startActivityForResult(i, 1);
			break;
		case R.id.llOther:
			i = new Intent();
			i.setClass(ActSearchUser.this, ActInvite.class);
			startActivity(i);
			break;
		case R.id.llSina:
			if (lv.getIsWLogin() || lv.getIsBinding()) {
				i = new Intent();
				i.setClass(ActSearchUser.this, ActSinaFriend.class);
			} else {
				lv.setIsSinaFriend(true);
				i = new Intent();
				i.setClass(ActSearchUser.this, ActWeiboGuide.class);
			}
			startActivity(i);
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Cursor c = null;
		String contactId = "";
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			try {
				String name = "", number = "", email = "";
				if (data != null) {
					c = getContentResolver().query(data.getData(), null, null,
							null, null);
				}
				if (c != null) {
					c.moveToFirst();
					contactId = c.getString(c
							.getColumnIndex(ContactsContract.Contacts._ID));
					for (int i = 0; i < c.getColumnCount(); i++) {
						String colName = c.getColumnName(i);
						String result = c.getString(i);
						Log.e("contacts", "col:" + colName + " result:"
								+ result);
						if (colName.equalsIgnoreCase("number")) {
							number = result;
						}
						if (colName.equalsIgnoreCase("name")) {
							name = result;
						}
						if (colName.equalsIgnoreCase("email")) {
							email = result;
						}
					}
					c.close();
				}
				Cursor emails = getContentResolver().query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Email.CONTACT_ID
								+ " = " + contactId, null, null);

				while (emails.moveToNext()) {
					email = emails
							.getString(emails
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				}
				emails.close();
				if (!"".equals(name)) {
					Intent intent = new Intent();
					intent.setClass(ActSearchUser.this, ActInvite.class);
					intent.putExtra("name", name);
					intent.putExtra("number", number);
					intent.putExtra("email", email);
					startActivity(intent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActSearchUser.this.finish();
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
