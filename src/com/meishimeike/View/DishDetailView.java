package com.meishimeike.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.ActComment;
import com.meishimeike.ActPerson;
import com.meishimeike.ActTa;
import com.meishimeike.R;
import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.Bll.BllSend;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.LocalVariable;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;

public class DishDetailView extends FrameLayout implements OnClickListener {
	private Context mContext;
	private TextView txtFoodName, txtFoodType, txtDistance;
	private TextView txtPrice, txtDinner, txtSign;
	private Button btnAttention;
	private LinearLayout llEat, llLike, llComment;
	private TextView txtEat, txtLike, txtComment;
	private ImageView imgHead;
	private BeanFood beanFood = null;
	private LocalVariable lv = null;
	private BllGet bllGet = null;
	private BllSend bllSend = null;
	private String strUid = "", strDName = "", strDistance = "";
	private String strHeadUrl = "", strSign = "", price = "";
	private String strLike = "", strEat = "", strComment = "";
	private boolean isAttention = false;
	private String strUName = "", strFid = "";
	private String strFoodUrl = "", strAUid = "";
	private ImageView imgFood = null;
	private AlertDialog dlgProgress = null;
	private String strMessage;
	private Bitmap bitmap = null;
	
	private class myTask extends AsyncTask<Void, Void, Void> {
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
			setCtrlData();
		}
	}

	public DishDetailView(Context context, AttributeSet attrs, BeanFood _bean,
			String auid) {
		super(context, attrs);
		mContext = context;
		strAUid = auid;
		beanFood = _bean;
		strFid = beanFood.getFid();
		lv = new LocalVariable(mContext);
		bllGet = new BllGet(mContext);
		bllSend = new BllSend(mContext);
		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dish_detail_view, this);
		imgFood = (ImageView) view.findViewById(R.id.imgFood);
		txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
		txtFoodType = (TextView) view.findViewById(R.id.txtFoodType);
		txtDistance = (TextView) view.findViewById(R.id.txtDistance);
		txtPrice = (TextView) view.findViewById(R.id.txtPrice);
		txtDinner = (TextView) view.findViewById(R.id.txtDiner);
		txtSign = (TextView) view.findViewById(R.id.txtSign);
		txtSign.setOnClickListener(this);
		llEat = (LinearLayout) view.findViewById(R.id.llEat);
		llEat.setOnClickListener(this);
		llLike = (LinearLayout) view.findViewById(R.id.llLike);
		llLike.setOnClickListener(this);
		llComment = (LinearLayout) view.findViewById(R.id.llComment);
		llComment.setOnClickListener(this);
		btnAttention = (Button) view.findViewById(R.id.btnAttention);
		btnAttention.setOnClickListener(this);
		imgHead = (ImageView) view.findViewById(R.id.imgHead);
		imgHead.setOnClickListener(this);

		txtEat = (TextView) view.findViewById(R.id.txtEat);
		txtLike = (TextView) view.findViewById(R.id.txtLike);
		txtComment = (TextView) view.findViewById(R.id.txtComment);

		txtFoodName.setText(beanFood.getName());
		txtFoodType.setText(beanFood.getTypeName());
		price = beanFood.getPrice();
		txtPrice.setText(price);
		if (!"".equals(price) && Integer.valueOf(price) > 0) {
			txtPrice.setVisibility(View.VISIBLE);
		} else {
			txtPrice.setVisibility(View.INVISIBLE);
		}
		txtDistance.setText("銆��銆��");

	}

	private void init_data() {
		// get dinner info
		strDName = beanFood.getrName();
		if (beanFood.getDistance() > 0) {
			float distance = beanFood.getDistance();
			if (distance > 1000) {
				distance = (float) Math.rint(distance / 1000);
				strDistance = String.valueOf((int) distance) + "km";
			} else {
				strDistance = String.valueOf((int) distance) + "m";
			}
		} else {
			strDistance = "";
		}
		strLike = beanFood.getLike_num();
		strComment = beanFood.getComment_num();
		strEat = beanFood.getWant_num();
		// get user info
		strUid = beanFood.getUid();
		strHeadUrl = beanFood.getuHead();
		strSign = beanFood.getIntro();
		strUName = beanFood.getuName();
		strFoodUrl = beanFood.getBiggest_pic();
		isAttention = bllGet.isAttention(strUid);
		if (!"".equals(strUName)) {
			strUName = "<font color='#000000'><b>" + strUName + "</b></font>";
		}
	}

	private void setCtrlData() {
		if (!"".equals(strDistance)) {
			txtDistance.setText(strDistance);
			txtDistance.setVisibility(View.VISIBLE);
		} else {
			txtDistance.setVisibility(View.INVISIBLE);
		}
		if (isAttention) {
			btnAttention.setText("宸插叧娉�");
		} else {
			btnAttention.setText("鍏虫敞");
		}
		txtDinner.setText(strDName);
		txtSign.setText(Html.fromHtml(strUName + " : " + strSign));

		txtComment.setText(strComment);
		txtLike.setText(strLike);
		txtEat.setText(strEat);

		if (!"".equals(strHeadUrl)) {
			imgHead.setTag(strHeadUrl);
			ImageDownloadItem item = new ImageDownloadItem();
			item.imageUrl = strHeadUrl;
			item.callback = new ImageDownloadCallback() {
				@Override
				public void update(Bitmap bitmap, String imageUrl) {
					ImageView imageViewByTag = (ImageView) findViewWithTag(imageUrl);
					if (imageViewByTag != null)
						imageViewByTag.setImageBitmap(bitmap);
					bitmap = null;
				}
			};
			ImageDownloadThread imageDownloadThread = ImageDownloadThread
					.getInstance();
			bitmap = imageDownloadThread.downloadWithCache(item);
			if (bitmap != null) {// 浠庣紦瀛樹腑鍙栧埌
				imgHead.setImageBitmap(bitmap);
			}
			bitmap = null;
		}

		if (!"".equals(strFoodUrl)) {
			imgFood.setTag(strFoodUrl);
			ImageDownloadItem item = new ImageDownloadItem();
			item.imageUrl = strFoodUrl;
			item.callback = new ImageDownloadCallback(){
				@Override
				public void update(Bitmap bitmap, String imageUrl) {
					ImageView imageViewByTag = (ImageView) findViewWithTag(imageUrl);
					if (imageViewByTag != null)
						imageViewByTag.setImageBitmap(bitmap);
					bitmap = null;
				}
			};
			ImageDownloadThread imageDownloadThread = ImageDownloadThread
					.getInstance();
			bitmap = imageDownloadThread.downloadWithCache(item);
			if (bitmap != null) {// 浠庣紦瀛樹腑鍙栧埌
				imgFood.setImageBitmap(bitmap);
			}
			//@Override
			/*public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}*/
			bitmap = null;
		}
		}



	@Override
	public void onClick(View v) {
		btnId = v.getId();
		Intent intent = null;
		switch (btnId) {
		case R.id.llEat:
			if (lv.getIsLogin()) {
				comit();
			} else {
				Toast.makeText(mContext, "璇峰厛鐧诲綍", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.llLike:
			if (lv.getIsLogin()) {
				comit();
			} else {
				Toast.makeText(mContext, "璇峰厛鐧诲綍", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.llComment:
			if (lv.getIsLogin()) {
				intent = new Intent();
				intent.setClass(mContext, ActComment.class);
				intent.putExtra(ActComment.CUR_FID, strFid);
				intent.putExtra(ActComment.CUR_AUID, strAUid);
				intent.putExtra(ActComment.CUR_RID, "");
				intent.putExtra(ActComment.CUR_UID, "");
				mContext.startActivity(intent);
			} else {
				Toast.makeText(mContext, "璇峰厛鐧诲綍", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnAttention:
			if (lv.getIsLogin()) {
				if (isAttention) {
					Toast.makeText(mContext, "璇ョ敤鎴峰凡鍏虫敞", Toast.LENGTH_SHORT)
							.show();
				} else {
					comit();
				}
			} else {
				Toast.makeText(mContext, "璇峰厛鐧诲綍", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.txtSign:
			intent = new Intent();
			intent.setClass(mContext, ActTa.class);
			intent.putExtra(ActTa.KEY_UID, strUid);
			mContext.startActivity(intent);
			break;
		case R.id.imgHead:
			intent = new Intent();
			if (strUid.equals(lv.getUid())) {
				intent.setClass(mContext, ActPerson.class);
			} else {
				intent.setClass(mContext, ActTa.class);
				intent.putExtra(ActTa.KEY_UID, strUid);
			}
			mContext.startActivity(intent);
			break;
		}
	}

	private int btnId = -1;
	private Thread comitThread = null;

	private void comit() {
		dlgProgress = ProgressDialog.show(mContext, "璇风瓑寰�..", "姝ｅ湪杩涜鎿嶄綔.....",
				true);
		dlgProgress.show();
		comitThread = new Thread() {
			public void run() {
				try {
					switch (btnId) {
					case R.id.llEat:
						strMessage = bllSend.wantFood(strFid);
						if (strMessage.split("#")[0].equals("1")) {
							strEat = String
									.valueOf(Integer.valueOf(strEat) + 1);
						}
						break;
					case R.id.llLike:
						strMessage = bllSend.likeFood(strFid);
						if (strMessage.split("#")[0].equals("1")) {
							strLike = String
									.valueOf(Integer.valueOf(strLike) + 1);
						}
						break;
					case R.id.llComment:
						break;
					case R.id.btnAttention:
						strMessage = bllSend.Attention(strUid);
						if (strMessage.split("#")[0].equals("1")) {
							isAttention = true;
						}
						break;
					default:
						break;
					}
					if (strMessage.split("#")[0].equals("1")) {
						strMessage = strMessage.split("#")[1];
						handler.sendEmptyMessage(1);
					} else {
						strMessage = strMessage.split("#")[1];
						handler.sendEmptyMessage(2);
					}
				} catch (Exception ex) {
					strMessage = ex.getLocalizedMessage();
					handler.sendEmptyMessage(2);
				}
			};
		};
		comitThread.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (isAttention) {
					btnAttention.setText("宸插叧娉�");
				} else {
					btnAttention.setText("鍏虫敞");
				}
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(mContext, "鎴愬姛!" + strMessage, Toast.LENGTH_LONG)
						.show();
				txtComment.setText(strComment);
				txtLike.setText(strLike);
				txtEat.setText(strEat);
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(mContext, "澶辫触!" + strMessage, Toast.LENGTH_LONG)
						.show();
				break;
			}
		}
	};

}
