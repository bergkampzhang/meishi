package com.meishimeike;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

public class ActGetPic extends ActivityTemplate implements OnClickListener {
	private Button btnCamera = null, btnPic = null;
	private String CameraPath = "";
	private Uri imgUri = null;
	private LocalVariable lv = null;
	private Commons coms = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_camera);
		lv = new LocalVariable(this);
		coms = new Commons();
		init_ctrl();
		init_data();
	}

	private void init_ctrl() {
		btnCamera = (Button) findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(this);
		btnPic = (Button) findViewById(R.id.btnPic);
		btnPic.setOnClickListener(this);
	}

	private void init_data() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCamera:
			SimpleDateFormat timeStampFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String filename = timeStampFormat.format(new Date());
			ContentValues values = new ContentValues();
			values.put(Media.TITLE, filename);
			Uri photoUri = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			CameraPath = getRealPathFromURI(photoUri, getContentResolver());
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, 0);
			break;
		case R.id.btnPic:
			Intent localIntent = new Intent();
			localIntent.setType("image/*");
			localIntent.setAction("android.intent.action.GET_CONTENT");
			Intent localIntent2 = Intent.createChooser(localIntent, "选择图片");
			startActivityForResult(localIntent2, 1);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Intent intent = null;
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			// camera
			imgUri = null;
			lv.setTempPicNum(lv.getTempPicNum() + 1);
			switch (lv.getTempPicNum()) {
			case 1:
				lv.setTempPic1(CameraPath);
				lv.setTempPicType1("camera");
				break;
			case 2:
				lv.setTempPic2(CameraPath);
				lv.setTempPicType2("camera");
				break;
			case 3:
				lv.setTempPic3(CameraPath);
				lv.setTempPicType3("camera");
				break;
			}
			intent = new Intent();
			intent.setClass(ActGetPic.this, ActPicView.class);
			startActivity(intent);
			ActGetPic.this.finish();
		} else if (requestCode == 1 && resultCode == Activity.RESULT_OK
				&& data != null) {
			// photo
			imgUri = data.getData();
			if (imgUri != null) {
				CameraPath = getRealPathFromURI(imgUri, getContentResolver());
				lv.setTempPicNum(lv.getTempPicNum() + 1);
				switch (lv.getTempPicNum()) {
				case 1:
					lv.setTempPic1(CameraPath);
					lv.setTempPicType1("photo");
					break;
				case 2:
					lv.setTempPic2(CameraPath);
					lv.setTempPicType2("photo");
					break;
				case 3:
					lv.setTempPic3(CameraPath);
					lv.setTempPicType3("photo");
					break;
				}
				intent = new Intent();
				intent.setClass(ActGetPic.this, ActPicView.class);
				startActivity(intent);
				ActGetPic.this.finish();
			} else {
				Toast.makeText(ActGetPic.this, "获取图片失败，请重新选择！",
						Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == 1 && resultCode == Activity.RESULT_OK
				&& data == null) {
			Toast.makeText(ActGetPic.this, "获取图片失败，请重试！", Toast.LENGTH_SHORT)
					.show();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			coms.initSendCfg(ActGetPic.this);
			ActGetPic.this.finish();
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
		CameraPath = "";
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
