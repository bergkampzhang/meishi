package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;

/**
 * @author :LiuQiang
 * @version ：2012-5-29 class desc
 */
public class PersonGridAdapter extends BaseAdapter {
	private Context mContext = null;
	private ArrayList<BeanFood> arrList = null;
	private ViewHolder viewHolder = null;
	private Bitmap bitmap = null;
	private LayoutInflater mInflater;
	private View convertView = null;

	public PersonGridAdapter(Context context, ArrayList<BeanFood> _list) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.arrList = _list;
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
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		convertView = cView;
		BeanFood bean = null;
		String strFoodUrl = "";
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.person_dish_grid_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.setImgFood((ImageView) convertView
					.findViewById(R.id.imgFood));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bean = arrList.get(position);
		strFoodUrl = bean.getMiddle_pic();
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
