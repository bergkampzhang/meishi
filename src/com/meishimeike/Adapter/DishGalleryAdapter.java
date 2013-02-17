package com.meishimeike.Adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.meishimeike.R;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;

public class DishGalleryAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<String> imgURL = null;
	private ViewHolder viewHolder = null;
	private Bitmap bitmap = null;
	private LayoutInflater mInflater;
	private View convertView = null;

	public DishGalleryAdapter(Context context, List<String> imgURL) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.imgURL = imgURL;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (imgURL == null) {
			return 0;
		} else {
			return imgURL.size();
		}
		// return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imgURL.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		String strFoodUrl = "";
		convertView = cView;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.dish_detail_gallery_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.setImgFood((ImageView) convertView
					.findViewById(R.id.imgFood));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		try {
			strFoodUrl = imgURL.get(position % imgURL.size());
			viewHolder.getImgFood().setTag(strFoodUrl);
			ImageDownloadItem item = new ImageDownloadItem();
			item.imageUrl = strFoodUrl;
			item.callback = new ImageDownloadCallback() {
				@Override
				public void update(Bitmap bitmap, String imageUrl) {
					ImageView imageViewByTag = (ImageView) convertView
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 设置边界对齐
		viewHolder.getImgFood().setAdjustViewBounds(true);
		return convertView;
	}

	final static class ViewHolder {
		ImageView imgFood = null;

		public ImageView getImgFood() {
			return imgFood;
		}

		public void setImgFood(ImageView imgFood) {
			this.imgFood = imgFood;
		}

	}

}
