package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;

/**
 * @author :LiuQiang
 * @version ：2012-8-29 class desc
 */
public class MainGalleryAdapter extends BaseAdapter {
	private Context mContext = null;
	private ArrayList<BeanFood> mArr = null;
	private LayoutInflater mInflater;

	public MainGalleryAdapter(Context _context, ArrayList<BeanFood> _arr) {
		mContext = _context;
		mArr = _arr;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if (mArr == null) {
			return 0;
		} else {
			return mArr.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (mArr == null) {
			return null;
		} else {
			return mArr.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		View cView = convertView;
		if (cView == null) {
			cView = mInflater.inflate(R.layout.main_item_view, null);
			viewHolder = new ViewHolder();
			viewHolder.setImgFood((ImageView) cView.findViewById(R.id.imgFood));
			viewHolder.setImgHead((ImageView) cView.findViewById(R.id.imgHead));
			viewHolder.setTxtFoodName((TextView) cView
					.findViewById(R.id.txtFoodName));
			viewHolder.setTxtDistance((TextView) cView
					.findViewById(R.id.txtDistance));
			viewHolder
					.setTxtDiner((TextView) cView.findViewById(R.id.txtDiner));
			viewHolder.setTxtLike((TextView) cView.findViewById(R.id.txtLike));
			viewHolder.setTxtEat((TextView) cView.findViewById(R.id.txtEat));
			viewHolder.setTxtComment((TextView) cView
					.findViewById(R.id.txtComment));
			viewHolder.setTxtSign((TextView) cView.findViewById(R.id.txtSign));
			viewHolder.setLlMainItemview((LinearLayout) cView
					.findViewById(R.id.llMainItemview));
			viewHolder.setLlPerson((LinearLayout) cView
					.findViewById(R.id.llPerson));
			cView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) cView.getTag();
		}
		load_data(mArr.get(position), viewHolder, cView);
		return cView;
	}

	public void load_data(BeanFood _bean, ViewHolder viewHolder,
			final View cView) {
		Bitmap bitmap = null;
		String strDinnerName = "", strComment = "";
		String strFoodName = "", strDistance = "";
		String strLike = "", strEat = "", strFoodUrl = "";
		String strHeadUrl = "", strSign = "", strUserName = "";
		float distance = 0;
		// ========================init data====================================
		BeanFood beanFood = _bean;
		strFoodUrl = beanFood.getBiggest_pic();
		strFoodName = beanFood.getName();
		strLike = beanFood.getLike_num();
		strEat = beanFood.getWant_num();
		strComment = beanFood.getComment_num();
		strDinnerName = beanFood.getrName();
		strHeadUrl = beanFood.getuHead();
		strUserName = beanFood.getuName();
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
		// ========================load data====================================
		viewHolder.getTxtFoodName().setText(strFoodName);
		viewHolder.getTxtLike().setText(strLike + "人喜欢");
		viewHolder.getTxtEat().setText(strEat + "人收集");
		viewHolder.getTxtComment().setText(strComment + "条评论");
		viewHolder.getTxtDistance().setText("　　　　");
		if (!"".equals(strDistance.trim())) {
			viewHolder.getTxtDistance().setVisibility(View.VISIBLE);
			viewHolder.getTxtDistance().setText(strDistance);
		} else {
			viewHolder.getTxtDistance().setVisibility(View.INVISIBLE);
		}
		viewHolder.getTxtDiner().setText(strDinnerName);
		viewHolder.getTxtSign().setText(
				Html.fromHtml(strUserName + " : " + strSign));

		// load food image
		viewHolder.getImgFood().setTag(strFoodUrl);
		ImageDownloadItem item = new ImageDownloadItem();
		item.imageUrl = strFoodUrl;
		item.callback = new ImageDownloadCallback() {
			@Override
			public void update(Bitmap bitmap, String imageUrl) {
				ImageView imageViewByTag = (ImageView) cView
						.findViewWithTag(imageUrl);
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
			viewHolder.getImgFood().setImageBitmap(bitmap);
		}

		// load head image
		viewHolder.getImgHead().setTag(strHeadUrl);
		item = new ImageDownloadItem();
		item.imageUrl = strHeadUrl;
		item.callback = new ImageDownloadCallback() {
			@Override
			public void update(Bitmap bitmap, String imageUrl) {
				ImageView imageViewByTag = (ImageView) cView
						.findViewWithTag(imageUrl);
				if (imageViewByTag != null)
					imageViewByTag.setImageBitmap(bitmap);
				bitmap = null;
			}
		};
		imageDownloadThread = ImageDownloadThread.getInstance();
		bitmap = imageDownloadThread.downloadWithCache(item);
		if (bitmap != null) {// 从缓存中取到
			viewHolder.getImgHead().setImageBitmap(bitmap);
		}

		bitmap = null;
	}

	final static class ViewHolder {
		private TextView txtFoodName, txtDistance, txtDiner;
		private TextView txtLike, txtEat, txtComment;
		private TextView txtSign;
		private ImageView imgFood, imgHead;
		private LinearLayout llMainItemview = null;
		private LinearLayout llPerson = null;

		public LinearLayout getLlMainItemview() {
			return llMainItemview;
		}

		public void setLlMainItemview(LinearLayout llMainItemview) {
			this.llMainItemview = llMainItemview;
		}

		public LinearLayout getLlPerson() {
			return llPerson;
		}

		public void setLlPerson(LinearLayout llPerson) {
			this.llPerson = llPerson;
		}

		public TextView getTxtFoodName() {
			return txtFoodName;
		}

		public void setTxtFoodName(TextView txtFoodName) {
			this.txtFoodName = txtFoodName;
		}

		public TextView getTxtDistance() {
			return txtDistance;
		}

		public void setTxtDistance(TextView txtDistance) {
			this.txtDistance = txtDistance;
		}

		public TextView getTxtDiner() {
			return txtDiner;
		}

		public void setTxtDiner(TextView txtDiner) {
			this.txtDiner = txtDiner;
		}

		public TextView getTxtLike() {
			return txtLike;
		}

		public void setTxtLike(TextView txtLike) {
			this.txtLike = txtLike;
		}

		public TextView getTxtEat() {
			return txtEat;
		}

		public void setTxtEat(TextView txtEat) {
			this.txtEat = txtEat;
		}

		public TextView getTxtComment() {
			return txtComment;
		}

		public void setTxtComment(TextView txtComment) {
			this.txtComment = txtComment;
		}

		public TextView getTxtSign() {
			return txtSign;
		}

		public void setTxtSign(TextView txtSign) {
			this.txtSign = txtSign;
		}

		public ImageView getImgFood() {
			return imgFood;
		}

		public void setImgFood(ImageView imgFood) {
			this.imgFood = imgFood;
		}

		public ImageView getImgHead() {
			return imgHead;
		}

		public void setImgHead(ImageView imgHead) {
			this.imgHead = imgHead;
		}

	}

}
