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
import com.meishimeike.Bean.BeanComment;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;

/**
 * @author :LiuQiang
 * @version ：2012-6-25 class desc
 */
public class PersonCommentAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeanComment> arrList;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder = null;
	private Bitmap bitmap = null;
	private View convertView = null;
	private Commons coms = null;

	public PersonCommentAdapter(Context context, ArrayList<BeanComment> arr) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		arrList = arr;
		coms = new Commons();
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
		BeanComment bean = null;
		String strName = "", strHeadUrl = "";
		String strContent = "", strTime = "";
		String strDesc = "";
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.person_msg_item, null);
			viewHolder = new ViewHolder();
			viewHolder.setTxtName((TextView) convertView
					.findViewById(R.id.txtName));
			viewHolder.setTxtContent((TextView) convertView
					.findViewById(R.id.txtContent));
			viewHolder.setTxtTime((TextView) convertView
					.findViewById(R.id.txtTime));
			viewHolder.setTxtDesc((TextView) convertView
					.findViewById(R.id.txtDesc));
			viewHolder.setImgHead((ImageView) convertView
					.findViewById(R.id.imgHead));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bean = arrList.get(position);

		strName = bean.getName();

		strContent = bean.getContent();
		strTime = coms.getTimeFromStr(bean.getC_time());
		viewHolder.getTxtName().setText(strName);
		viewHolder.getTxtContent().setText(strContent);
		viewHolder.getTxtTime().setText(strTime);
		if (!"".equals(bean.getA_uid())) {
			strDesc = "回复了  " + bean.getFood_name();
		} else {
			strDesc = "评论了  " + bean.getFood_name();
		}
		viewHolder.getTxtDesc().setText(strDesc);

		strHeadUrl = bean.getPhoto_url();
		viewHolder.getImgHead().setTag(strHeadUrl);
		ImageDownloadItem item = new ImageDownloadItem();
		item.imageUrl = strHeadUrl;
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
		TextView txtContent = null;
		TextView txtTime = null;
		TextView txtDesc = null;
		ImageView imgHead = null;

		public TextView getTxtDesc() {
			return txtDesc;
		}

		public void setTxtDesc(TextView txtDesc) {
			this.txtDesc = txtDesc;
		}

		public TextView getTxtName() {
			return txtName;
		}

		public void setTxtName(TextView txtName) {
			this.txtName = txtName;
		}

		public TextView getTxtContent() {
			return txtContent;
		}

		public void setTxtContent(TextView txtContent) {
			this.txtContent = txtContent;
		}

		public TextView getTxtTime() {
			return txtTime;
		}

		public void setTxtTime(TextView txtTime) {
			this.txtTime = txtTime;
		}

		public ImageView getImgHead() {
			return imgHead;
		}

		public void setImgHead(ImageView imgHead) {
			this.imgHead = imgHead;
		}

	}
}
