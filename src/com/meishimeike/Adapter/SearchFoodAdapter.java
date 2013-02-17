package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;

/**
 * @author :LiuQiang
 * @version ：2012-6-6 class desc
 */
public class SearchFoodAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeanFood> arrList;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder = null;
	private Bitmap bitmap = null;
	private View cView = null;

	public SearchFoodAdapter(Context context, ArrayList<BeanFood> arr) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		arrList = arr;
	}

	@Override
	public int getCount() {
		if (arrList == null) {
			return 0;
		} else {
			return arrList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrList.get(position).getFid();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		cView = convertView;
		BeanFood bean = null;
		String strFoodUrl = "";
		String strDistance = "";
		if (cView == null) {
			cView = mInflater.inflate(R.layout.search_food_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.setTxtName((TextView) cView.findViewById(R.id.txtName));
			viewHolder.setTxtDinner((TextView) cView
					.findViewById(R.id.txtDinner));
			viewHolder.setTxtDistance((TextView) cView
					.findViewById(R.id.txtDistance));
			viewHolder.setTxtLikeNum((TextView) cView
					.findViewById(R.id.txtLike));
			viewHolder.setTxtWantNum((TextView) cView
					.findViewById(R.id.txtWant));
			viewHolder.setTxtCommentNum((TextView) cView
					.findViewById(R.id.txtComment));
			viewHolder.setImgFood((ImageView) cView.findViewById(R.id.imgFood));
			cView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) cView.getTag();
		}

		bean = arrList.get(position);

		if (bean.getDistance() > 0) {
			float distance = bean.getDistance();
			if (distance > 1000) {
				distance = (float) Math.rint(distance / 1000);
				strDistance = String.valueOf((int) distance) + "km";
			} else {
				strDistance = String.valueOf((int) distance) + "m";
			}
		} else {
			strDistance = "";
		}

		viewHolder.getTxtName().setText(bean.getName());
		viewHolder.getTxtDinner().setText(bean.getrName());
		viewHolder.getTxtDistance().setText(strDistance);
		viewHolder.getTxtCommentNum().setText(bean.getComment_num() + "人评论");
		viewHolder.getTxtLikeNum().setText(bean.getLike_num() + "人喜欢");
		viewHolder.getTxtWantNum().setText(bean.getWant_num() + "人收集");

		strFoodUrl = bean.getMiddle_pic();
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
					notifyDataSetChanged();
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
		bitmap = null;
		return cView;
	}

	final static class ViewHolder {
		TextView txtName = null;
		TextView txtDistance = null;
		TextView txtDinner = null;
		TextView txtCommentNum = null;
		TextView txtLikeNum = null;
		TextView txtWantNum = null;
		ImageView imgFood = null;

		public TextView getTxtName() {
			return txtName;
		}

		public void setTxtName(TextView txtName) {
			this.txtName = txtName;
		}

		public TextView getTxtDistance() {
			return txtDistance;
		}

		public void setTxtDistance(TextView txtDistance) {
			this.txtDistance = txtDistance;
		}

		public TextView getTxtDinner() {
			return txtDinner;
		}

		public void setTxtDinner(TextView txtDinner) {
			this.txtDinner = txtDinner;
		}

		public TextView getTxtCommentNum() {
			return txtCommentNum;
		}

		public void setTxtCommentNum(TextView txtCommentNum) {
			this.txtCommentNum = txtCommentNum;
		}

		public TextView getTxtLikeNum() {
			return txtLikeNum;
		}

		public void setTxtLikeNum(TextView txtLikeNum) {
			this.txtLikeNum = txtLikeNum;
		}

		public TextView getTxtWantNum() {
			return txtWantNum;
		}

		public void setTxtWantNum(TextView txtWantNum) {
			this.txtWantNum = txtWantNum;
		}

		public ImageView getImgFood() {
			return imgFood;
		}

		public void setImgFood(ImageView imgFood) {
			this.imgFood = imgFood;
		}

	}

}
