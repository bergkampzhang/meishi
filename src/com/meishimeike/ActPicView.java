package com.meishimeike;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

public class ActPicView extends ActivityTemplate implements OnClickListener {
	private ImageView imgPic = null;
	private Button btnAgain = null, btnOk = null;
	private Bitmap bitmap = null;
	private ProgressBar pb = null;
	private LinearLayout llPannel = null;
	private LocalVariable lv = null;
	private Commons coms = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_pic_view);
		lv = new LocalVariable(this);
		coms = new Commons();
		init_ctrl();
		new MyTask().execute((Void) null);
	}

	private void init_ctrl() {
		imgPic = (ImageView) findViewById(R.id.imgPic);
		btnAgain = (Button) findViewById(R.id.btnAgain);
		btnAgain.setOnClickListener(this);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		pb = (ProgressBar) findViewById(R.id.pb);
		llPannel = (LinearLayout) findViewById(R.id.llPannel);
	}

	private void init_data() {
		switch (lv.getTempPicNum()) {
		case 1:
			bitmap = getPic(lv.getTempPic1(), lv.getTempPicType1());
			break;
		case 2:
			bitmap = getPic(lv.getTempPic2(), lv.getTempPicType2());
			break;
		case 3:
			bitmap = getPic(lv.getTempPic3(), lv.getTempPicType3());
			break;
		}
	}

	private Bitmap getPic(String _path, String _type) {
		Bitmap b = null;
		try {
			int be = 1;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			b = BitmapFactory.decodeFile(_path, options);

			options.inJustDecodeBounds = false;
			if (options.outWidth > options.outHeight) {
				if (options.outWidth > (float) 800) {
					be = (int) (options.outWidth / (float) 800);
				}
			} else {
				if (options.outHeight > (float) 800) {
					be = (int) (options.outHeight / (float) 800);
				}
			}
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;
			b = null;
			b = BitmapFactory.decodeFile(_path, options);

			return b;
		} catch (Exception e) {
			return null;
		} finally {
			b = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAgain:
			if (lv.getTempPicNum() >= 3) {
				Toast.makeText(ActPicView.this, "最多添加三张照片", Toast.LENGTH_SHORT)
						.show();
			} else {
				Intent intent = new Intent();
				intent.setClass(ActPicView.this, ActGetPic.class);
				startActivity(intent);
				ActPicView.this.finish();
			}
			break;
		case R.id.btnOk:
			Intent intent = new Intent();
			intent.setClass(ActPicView.this, ActSendDinner.class);
			startActivity(intent);
			ActPicView.this.finish();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (lv.getTempPicNum()) {
			case 1:
//				if (lv.getTempPicType1().equals("camera")) {
//					coms.delTempPic(lv.getTempPic1());
//				}
				lv.setTempPic1("");
				lv.setTempPicType1("");
				lv.setTempPicNum(lv.getTempPicNum() - 1);
				break;
			case 2:
//				if (lv.getTempPicType2().equals("camera")) {
//					coms.delTempPic(lv.getTempPic2());
//				}
				lv.setTempPic2("");
				lv.setTempPicType2("");
				lv.setTempPicNum(lv.getTempPicNum() - 1);
				break;
			case 3:
//				if (lv.getTempPicType3().equals("camera")) {
//					coms.delTempPic(lv.getTempPic3());
//				}
				lv.setTempPic3("");
				lv.setTempPicType3("");
				lv.setTempPicNum(lv.getTempPicNum() - 1);
				break;
			}
			Intent intent = new Intent();
			intent.setClass(ActPicView.this, ActGetPic.class);
			startActivity(intent);
			ActPicView.this.finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private class MyTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
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
			imgPic.setImageBitmap(bitmap);
			pb.setVisibility(View.GONE);
			llPannel.setVisibility(View.VISIBLE);
			btnAgain.setEnabled(true);
			btnOk.setEnabled(true);
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
		bitmap = null;
	}

	@Override
	protected void onDestroy() {
		bitmap = null;
		super.onDestroy();
	}

}
