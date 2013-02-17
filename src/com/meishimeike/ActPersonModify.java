package com.meishimeike;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.Adapter.PersonModifyTagAdapter;
import com.meishimeike.Bean.BeanTag;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.Bll.BllSend;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.UserTabWidget.OnUserNavLeftClickListener;
import com.meishimeike.UserTabWidget.OnUserNavRightClickListener;
import com.meishimeike.UserTabWidget.UserNavigateBar;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

public class ActPersonModify extends ActivityTemplate implements
		OnClickListener {
	private UserNavigateBar navBar;
	private LocalVariable lv = null;
	private AlertDialog dlgProgress = null;
	private String strMessage;
	private Thread comitThread = null;
	private BllGet bllGet = null;
	private BllSend bllSend = null;
	private BeanUserInfo beanUser = null;
	private Button btnSave = null, txtCityName = null;
	private EditText txtName, txtIntro;
	private EditText txtOldPwd, txtNewPwd1, txtNewPwd2;
	private RadioButton rbMale, rbFemale;
	private ImageView imgHead;
	private LinearLayout llHead;
	private LinearLayout llOldPwd, llNewPwd, llNewPwd2;
	private GridView lvTag;
	private Bitmap bitmap = null;
	private String strUid = "", strHeadUrl = "", strCityName = "";
	private String strCityId = "", strName = "", strIntro = "";
	private String strGenderId = "", strPath = "", strTagIds = "";
	private String strOldPwd = "", strNewPwd1 = "", strNewPwd2 = "";
	private boolean isPwdModify = false;
	private Uri imgUri = null;
	private PersonModifyTagAdapter adapter = null;
	private ArrayList<BeanTag> arrList = null;
	private List<String> strsId;
	private Commons coms = null;
	private LinearLayout llLoad = null, llPersonModify = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_person_modify);
		coms = new Commons();
		bllGet = new BllGet(this);
		bllSend = new BllSend(this);
		lv = new LocalVariable(this);
		strUid = lv.getUid();
		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		llPersonModify = (LinearLayout) findViewById(R.id.ll_act_person_modify);
		llLoad = (LinearLayout) findViewById(R.id.llLoad);
		navBar = (UserNavigateBar) findViewById(R.id.NAV);
		navBar.setOnUserNavLeftClickListener(NavLeftClickListener);
		navBar.setOnUserNavRightClickListener(NavRightClickListener);

		llOldPwd = (LinearLayout) findViewById(R.id.llOldPwd);
		llNewPwd = (LinearLayout) findViewById(R.id.llNewPwd);
		llNewPwd2 = (LinearLayout) findViewById(R.id.llNewPwd2);

		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		llHead = (LinearLayout) findViewById(R.id.llHeadModify);
		llHead.setOnClickListener(this);
		txtName = (EditText) findViewById(R.id.txtName);
		txtCityName = (Button) findViewById(R.id.txtCityName);
		txtCityName.setOnClickListener(this);
		txtIntro = (EditText) findViewById(R.id.txtIntro);
		txtOldPwd = (EditText) findViewById(R.id.txtOldPwd);
		txtNewPwd1 = (EditText) findViewById(R.id.txtNewPwd1);
		txtNewPwd2 = (EditText) findViewById(R.id.txtNewPwd2);
		rbMale = (RadioButton) findViewById(R.id.rbMale);
		rbFemale = (RadioButton) findViewById(R.id.rbFemale);
		imgHead = (ImageView) findViewById(R.id.imgHead);
		lvTag = (GridView) findViewById(R.id.lvTag);
		lvTag.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {
				TextView txt = (TextView) view.findViewById(R.id.txtTagItem);
				boolean b = Boolean.valueOf(txt.getTag().toString());
				if (b) {
					strsId.remove(arrList.get(pos).getTagId());
					adapter.setFocus(pos, false);
					// isTagModify = true;
				} else {
					strsId.add(arrList.get(pos).getTagId());
					adapter.setFocus(pos, true);
					// isTagModify = true;
				}
			}
		});
	}

	private void init_data() {
		strsId = new ArrayList<String>();
		beanUser = bllGet.getUserInfo(strUid);
		if (beanUser != null) {
			strHeadUrl = beanUser.getPhoto_80_url();
			strCityName = beanUser.getCity();
			strName = beanUser.getName();
			strIntro = beanUser.getIntro();
			strGenderId = beanUser.getGender();
			strTagIds = beanUser.getTags();
			if (!"".equals(strTagIds)) {
				String[] strs = strTagIds.split("\\|");
				for (String s : strs) {
					strsId.add(s);
				}
				// strsId.addAll(Arrays.asList(strTagIds.split("|")));
			}
		}
		arrList = bllGet.getTagList();
	}

	private void setCtrlData() {
		if (strGenderId.contains("F")) {
			rbFemale.setChecked(true);
			rbMale.setChecked(false);
		} else {
			rbFemale.setChecked(false);
			rbMale.setChecked(true);
		}
		txtName.setText(strName);
		txtCityName.setText(strCityName);
		txtIntro.setText(strIntro);
		if (!"".equals(strHeadUrl)) {
			imgHead.setTag(strHeadUrl);
			ImageDownloadItem item = new ImageDownloadItem();
			item.imageUrl = strHeadUrl;
			item.callback = new ImageDownloadCallback() {
				@Override
				public void update(Bitmap bitmap, String imageUrl) {
					ImageView imageViewByTag = (ImageView) llPersonModify
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

		adapter = new PersonModifyTagAdapter(this, arrList, strsId);
		lvTag.setAdapter(adapter);

		int totalHeight = 0;
		int count = adapter.getCount();
		int item = 0;
		for (int i = 0; i < count; i++) {
			View listItem = adapter.getView(i, null, lvTag);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			item = listItem.getMeasuredHeight();
		}
		totalHeight = (totalHeight / 3) + (count % 3 > 0 ? 1 : 0) * item;
		ViewGroup.LayoutParams params = lvTag.getLayoutParams();
		params.height = totalHeight;
		lvTag.setLayoutParams(params);

		if (!lv.getWUid().equals("")) {
			llOldPwd.setVisibility(View.GONE);
			llNewPwd.setVisibility(View.GONE);
			llNewPwd2.setVisibility(View.GONE);
		} else {
			llOldPwd.setVisibility(View.VISIBLE);
			llNewPwd.setVisibility(View.VISIBLE);
			llNewPwd2.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSave:
			comit();
			break;
		case R.id.llHeadModify:
			Intent localIntent = new Intent();
			localIntent.setType("image/*");
			localIntent.setAction("android.intent.action.GET_CONTENT");
			Intent localIntent2 = Intent.createChooser(localIntent, "选择JPG头像");
			startActivityForResult(localIntent2, 1);
			break;
		case R.id.txtCityName:
			Intent intent = new Intent();
			intent.setClass(ActPersonModify.this, ActCitySel.class);
			startActivityForResult(intent, 100);
			break;
		}

	}

	private void comit() {
		dlgProgress = ProgressDialog.show(ActPersonModify.this, "请等待...",
				"正在进行操作.....", true);
		dlgProgress.show();
		comitThread = new Thread(comitRunnable);
		comitThread.start();
	}

	private void netCheck() {
		boolean b = true;
		String str = "";
		strName = txtName.getText().toString().trim();
		strIntro = txtIntro.getText().toString().trim();
		strGenderId = rbMale.isChecked() ? "M" : "F";
		strOldPwd = txtOldPwd.getText().toString().trim();
		strNewPwd1 = txtNewPwd1.getText().toString().trim();
		strNewPwd2 = txtNewPwd2.getText().toString().trim();
		strTagIds = "";
		for (String s : strsId) {
			strTagIds += s + "|";
		}
		if (!"".equals(strTagIds)) {
			strTagIds = strTagIds.substring(0, strTagIds.length() - 1);
		}
		if (!"".equals(strOldPwd) && !"".equals(strNewPwd1)
				&& !"".equals(strNewPwd2)) {
			if (strNewPwd1.equals(strNewPwd2)) {
				isPwdModify = true;
			} else {
				strMessage = "新密码填写有误！";
				handler.sendEmptyMessage(2);
				return;
			}
		} else if ("".equals(strOldPwd) && "".equals(strNewPwd1)
				&& "".equals(strNewPwd2)) {
			isPwdModify = false;
		} else {
			strMessage = "密码填写有误！";
			handler.sendEmptyMessage(2);
			return;
		}

		if (!strName.equals("")) {
			if (isPwdModify) {
				str = bllSend.modifyUserPassword(strOldPwd, strNewPwd1);
				if (str.split("#")[0].equals("1")) {
					strMessage = "密码修改成功！";
					handler.sendEmptyMessage(3);
				} else {
					strMessage = "密码修改失败！";
					handler.sendEmptyMessage(3);
					b = false;
				}
			}

			if (!"".equals(strPath)) {
				str = bllSend.modifyHeadImg(strPath);
				if (str.split("#")[0].equals("1")) {
					strMessage = "头像修改成功！";
					handler.sendEmptyMessage(3);
				} else {
					strMessage = "头像修改失败！";
					handler.sendEmptyMessage(3);
					b = false;
				}
			}

			str = bllSend.modifyUserInfo(strName, strGenderId, strIntro,
					strCityId, strTagIds);
			if (str.split("#")[0].equals("1")) {
				strMessage = "基本信息修改成功！";
				handler.sendEmptyMessage(3);
			} else {
				strMessage = "基本信息修改失败！";
				handler.sendEmptyMessage(3);
				b = false;
			}

			if (b) {
				strMessage = "所有信息修改成功！";
				handler.sendEmptyMessage(1);
			} else {
				strMessage = "部分信息修改失败，请重试！";
				handler.sendEmptyMessage(2);
			}
		} else {
			strMessage = "请填写昵称！";
			handler.sendEmptyMessage(2);
		}
	}

	private Runnable comitRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				netCheck();
			} catch (Exception ex) {
				strMessage = ex.getLocalizedMessage();
				handler.sendEmptyMessage(2);
			}
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(ActPersonModify.this, strMessage,
						Toast.LENGTH_SHORT).show();
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				ActPersonModify.this.finish();
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(ActPersonModify.this, strMessage,
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(ActPersonModify.this, strMessage,
						Toast.LENGTH_SHORT).show();
				break;
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
			if (beanUser != null) {
				setCtrlData();
			}
			llLoad.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK
				&& data != null) {
			imgUri = data.getData();
			if (imgUri != null) {
				strPath = getRealPathFromURI(imgUri, getContentResolver());
				imgHead.setImageBitmap(getPic(strPath));
			} else {
				Toast.makeText(ActPersonModify.this, "获取头像失败，请重新选择！",
						Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == 1 && resultCode == Activity.RESULT_OK
				&& data == null) {
			Toast.makeText(ActPersonModify.this, "获取头像失败，请重试！",
					Toast.LENGTH_SHORT).show();
		} else if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			strCityId = bundle.getString("cityId");
			strCityName = bundle.getString("cityName");
			txtCityName.setText(strCityName);
		}
	}

	private Bitmap getPic(String _path) {
		Bitmap b = null;
		try {
			int be = 1;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			b = BitmapFactory.decodeFile(_path, options);

			options.inJustDecodeBounds = false;
			if (options.outWidth > options.outHeight) {
				if (options.outWidth > (float) 100) {
					be = (int) (options.outWidth / (float) 100);
				}
			} else {
				if (options.outHeight > (float) 100) {
					be = (int) (options.outHeight / (float) 100);
				}
			}
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;

			b = BitmapFactory.decodeFile(_path, options);

			return b;
		} catch (Exception e) {
			return null;
		} finally {
			b = null;
		}
	}

	public static String getRealPathFromURI(Uri uri, ContentResolver resolver) {

		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = resolver.query(uri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String str = cursor.getString(column_index);
		cursor.close();
		return str;
	}

	private OnUserNavLeftClickListener NavLeftClickListener = new OnUserNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			// comit();
			coms.ExitLogin(ActPersonModify.this);
		}
	};

	private OnUserNavRightClickListener NavRightClickListener = new OnUserNavRightClickListener() {
		@Override
		public void onClickListenr() {
			comit();
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
