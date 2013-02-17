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
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;

/**
 * @author :LiuQiang
 * @version ：2012-7-6 class desc
 */
public class PersonDetailItem extends LinearLayout implements OnClickListener {
	private Context mContext;
	private Bitmap bitmap = null;
	private ImageView imgHead = null;
	private ImageView imgFood = null;
	private TextView txtTitle = null;
	private TextView txtLike = null;
	private TextView txtEat = null;
	private TextView txtDinner = null;
	private TextView txtDesc = null;
	private BeanFood bean = null;
	private String strFoodUrl = "", strHeadUrl = "", strDate = "";
	private String strTitle = "", strLike = "", strType = "";
	private String strDesc = "", strEat = "", strFoodName = "";
	private String strDinnerName = "", strUName = "";
	private String strFid = "", strUid = "";
	private Commons coms = null;
	private LocalVariable lv = null;

	public PersonDetailItem(Context context, AttributeSet attrs, BeanFood _bean) {
		super(context, attrs);
		mContext = context;
		bean = _bean;
		coms = new Commons();
		lv = new LocalVariable(mContext);
		init_ctrl();
		init_data();
		setCtrlData();
	}

	private void init_ctrl() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.person_dish_list_item, this);
		txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		txtLike = (TextView) view.findViewById(R.id.txtLike);
		txtEat = (TextView) view.findViewById(R.id.txtEat);
		txtDinner = (TextView) view.findViewById(R.id.txtDiner);
		txtDesc = (TextView) view.findViewById(R.id.txtDesc);
		imgHead = (ImageView) view.findViewById(R.id.imgHead);
		imgHead.setOnClickListener(this);
		imgFood = (ImageView) view.findViewById(R.id.imgFood);
		imgFood.setOnClickListener(this);
	}

	private void init_data() {
		strFid = bean.getFid();
		strUid = bean.getUid();
		strFoodUrl = bean.getBiggest_pic();
		strHeadUrl = bean.getuHead();
		strLike = bean.getLike_num() + "喜欢";
		strEat = bean.getWant_num() + "收集";
		strFoodName = bean.getName();
		strDinnerName = bean.getrName();
		strType = bean.getTypeName();
		if ("1".equals(bean.getType())) {
			strDate = bean.getC_date();
		} else {
			strDate = bean.getM_date();
		}
		strUName = bean.getuName();
		if ("null".equals(strUName)) {
			strUName = "";
		}
		strTitle = coms.getTimeDiff(strDate) + " " + strType + " "
				+ "<font color='#666666'><b>" + strFoodName + "</b></font> ";
		// strDesc = "<font color='#c83214'><b>" + strUName + "</b></font> "
		// + bean.getIntro();
		strDesc = bean.getIntro();
	}

	private void setCtrlData() {
		txtLike.setText(strLike);
		txtEat.setText(strEat);
		txtDesc.setText(Html.fromHtml(strDesc));
		txtTitle.setText(Html.fromHtml(strTitle));
		txtDinner.setText(strDinnerName);

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
		if (bitmap != null) {// 从缓存中取到
			imgHead.setImageBitmap(bitmap);
		}

		imgFood.setTag(strFoodUrl);
		item = new ImageDownloadItem();
		item.imageUrl = strFoodUrl;
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
			imgFood.setImageBitmap(bitmap);
		}
		bitmap = null;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
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
		case R.id.imgFood:
			intent = new Intent();
			intent.setClass(mContext, ActDish.class);
			intent.putExtra(ActDish.KEY_FID, strFid);
			mContext.startActivity(intent);
			break;
		}

	}

}
