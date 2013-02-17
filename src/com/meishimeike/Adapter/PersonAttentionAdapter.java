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
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;

public class PersonAttentionAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeanUserInfo> arrList = null;
	private LayoutInflater mInflater;
	private static boolean[] isFocused;
	private String strUrl = "";
	private Bitmap bitmap = null;
	private ViewHolder viewHolder = null;
	private View convertView = null;

	public PersonAttentionAdapter(Context context, ArrayList<BeanUserInfo> arr) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		arrList = arr;
		if (arrList != null) {
			isFocused = new boolean[arrList.size()];
			for (int i = 0; i < arrList.size(); i++) {
				isFocused[i] = false;
			}
		}
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
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int pos, View cView, ViewGroup parent) {
		convertView = cView;
		BeanUserInfo bean = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.person_attention_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.setTxtName((TextView) convertView
					.findViewById(R.id.txtName));
			viewHolder.setTxtDesc((TextView) convertView
					.findViewById(R.id.txtDesc));
			viewHolder.setImgHead((ImageView) convertView
					.findViewById(R.id.imgHead));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bean = arrList.get(pos);
		viewHolder.getTxtName().setText(bean.getName());
		viewHolder.getTxtDesc().setText(bean.getIntro());

		strUrl = bean.getPhoto_60_url();

		viewHolder.getImgHead().setTag(strUrl);
		ImageDownloadItem item = new ImageDownloadItem();
		item.imageUrl = strUrl;
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
			viewHolder.getImgHead().setImageBitmap(bitmap);
		}
		bitmap = null;

		return convertView;
	}

	final static class ViewHolder {
		TextView txtName = null;
		TextView txtDesc = null;
		ImageView imgHead = null;

		public ImageView getImgHead() {
			return imgHead;
		}

		public void setImgHead(ImageView imgHead) {
			this.imgHead = imgHead;
		}

		public TextView getTxtName() {
			return txtName;
		}

		public void setTxtName(TextView txtName) {
			this.txtName = txtName;
		}

		public TextView getTxtDesc() {
			return txtDesc;
		}

		public void setTxtDesc(TextView txtDesc) {
			this.txtDesc = txtDesc;
		}

	}

}
