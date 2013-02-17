package com.meishimeike.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meishimeike.ActDish;
import com.meishimeike.ActPerson;
import com.meishimeike.ActTa;
import com.meishimeike.R;
import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;

public class MainItemView extends LinearLayout implements OnClickListener {
	private Context mContext;
	private TextView txtFoodName, txtDistance, txtDiner;
	private TextView txtLike, txtEat, txtComment;
	private TextView txtSign;
	private ImageView imgFood, imgHead;
	private String strDinnerName = "", strComment = "";
	private String strFoodName = "", strDistance = "";
	private String strLike = "", strEat = "", strFid = "";
	private String strFoodUrl = "";
	private String strHeadUrl = "", strSign = "", strUserName = "";
	private float distance = 0;
	private LinearLayout llMainItemview = null;
	private LinearLayout llPerson = null;
	private BeanFood beanFood = null;
	private String strUid = "";
	private Bitmap bitmap = null;
	private LocalVariable lv = null;

	public MainItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		lv = new LocalVariable(mContext);
		init_ctrl();
	}

	private void init_ctrl() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.main_item_view, this);
		imgFood = (ImageView) view.findViewById(R.id.imgFood);
		imgHead = (ImageView) view.findViewById(R.id.imgHead);
		txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
		txtDistance = (TextView) view.findViewById(R.id.txtDistance);
		txtDiner = (TextView) view.findViewById(R.id.txtDiner);
		txtLike = (TextView) view.findViewById(R.id.txtLike);
		txtEat = (TextView) view.findViewById(R.id.txtEat);
		txtComment = (TextView) view.findViewById(R.id.txtComment);
		txtSign = (TextView) view.findViewById(R.id.txtSign);
		llMainItemview = (LinearLayout) view.findViewById(R.id.llMainItemview);
		llMainItemview.setOnClickListener(this);
		llPerson = (LinearLayout) view.findViewById(R.id.llPerson);
		llPerson.setOnClickListener(this);
	}

	public void init_data(BeanFood _bean) {
		beanFood = _bean;
		strFid = beanFood.getFid();
		strFoodUrl = beanFood.getBiggest_pic();
		strFoodName = beanFood.getName();
		strLike = beanFood.getLike_num();
		strEat = beanFood.getWant_num();
		strComment = beanFood.getComment_num();
		strDinnerName = beanFood.getrName();
		strHeadUrl = beanFood.getuHead();
		strUserName = beanFood.getuName();
		strUid = beanFood.getUid();
		strSign = beanFood.getIntro();
		distance = beanFood.getDistance();
		if (distance == 0) {
			strDistance = "";
		} else {
			if (distance > 1000) {
				distance = (float) Math.rint(distance / 1000);
				strDistance = String.valueOf((int) distance) + "km";
			} else {
				strDistance = String.valueOf((int) distance) + "m";
			}
		}
		if (!"".equals(strUserName)) {
			strUserName = "<font color='#000000'><b>" + strUserName
					+ "</b></font>";
		}

		loadData();
	}

	private void loadData() {
		txtFoodName.setText(strFoodName);
		txtLike.setText(strLike + "人喜欢");
		txtEat.setText(strEat + "人收集");
		txtComment.setText(strComment + "条评论");
		txtDistance.setText("　　　　");
		if (!"".equals(strDistance.trim())) {
			txtDistance.setVisibility(View.VISIBLE);
			txtDistance.setText(strDistance);
		} else {
			txtDistance.setVisibility(View.INVISIBLE);
		}
		txtDiner.setText(strDinnerName);
		txtSign.setText(Html.fromHtml(strUserName + " : " + strSign));

		// load food image
		imgFood.setTag(strFoodUrl);
		ImageDownloadItem item = new ImageDownloadItem();
		item.imageUrl = strFoodUrl;
		item.callback = new ImageDownloadCallback() {
			@Override
			public void update(Bitmap bitmap, String imageUrl) {
				ImageView imageViewByTag = (ImageView) findViewWithTag(imageUrl);
				if (imageViewByTag != null) {
					imageViewByTag.setImageBitmap(bitmap);
					imageViewByTag.postInvalidate();
				}
				bitmap = null;
			}
		};
		ImageDownloadThread imageDownloadThread = ImageDownloadThread
				.getInstance();
		bitmap = imageDownloadThread.downloadWithCache(item);
		if (bitmap != null) {// 从缓存中取到
			imgFood.setImageBitmap(bitmap);
		}

		// load head image
		imgHead.setTag(strHeadUrl);
		item = new ImageDownloadItem();
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
		imageDownloadThread = ImageDownloadThread.getInstance();
		bitmap = imageDownloadThread.downloadWithCache(item);
		if (bitmap != null) {// 从缓存中取到
			imgHead.setImageBitmap(bitmap);
		}

		bitmap = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llMainItemview:
			Intent intent = new Intent();
			intent.setClass(mContext, ActDish.class);
			intent.putExtra(ActDish.KEY_FID, strFid);
			mContext.startActivity(intent);
			break;
		case R.id.llPerson:
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

}
