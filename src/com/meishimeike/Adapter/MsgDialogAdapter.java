package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanMessage;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;

public class MsgDialogAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<BeanMessage> arrList;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder = null;
	private Bitmap bitmap = null;
	private Commons coms = null;
	private LocalVariable lv = null;
	private View convertView = null;

	public MsgDialogAdapter(Context context, ArrayList<BeanMessage> arr) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		arrList = arr;
		coms = new Commons();
		lv = new LocalVariable(mContext);
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
		BeanMessage bean = null;
		String strHeadUrl = "";
		String strContent = "", strTime = "";
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.msg_dialog_item, null);
			viewHolder = new ViewHolder();
			viewHolder.setTxtContent((TextView) convertView
					.findViewById(R.id.txtContent));
			viewHolder.setTxtTime((TextView) convertView
					.findViewById(R.id.txtTime));
			viewHolder.setImgLeftHead((ImageView) convertView
					.findViewById(R.id.imgLeftHead));
			viewHolder.setImgRightHead((ImageView) convertView
					.findViewById(R.id.imgRightHead));
			viewHolder.setLlLeftHead((LinearLayout) convertView
					.findViewById(R.id.llLeftHead));
			viewHolder.setLlRightHead((LinearLayout) convertView
					.findViewById(R.id.llRightHead));
			viewHolder.setLlContent((LinearLayout) convertView
					.findViewById(R.id.llContent));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bean = arrList.get(position);
		strContent = bean.getContents();
		strTime = coms.getTimeFromStr(bean.getC_date());
		if (lv.getUid().equals(bean.getSuid())) {
			strHeadUrl = bean.getSuHeadUrl();
			viewHolder.getLlLeftHead().setTag(-10101, "no");
			viewHolder.getLlLeftHead().setVisibility(View.GONE);
			viewHolder.getLlRightHead().setVisibility(View.VISIBLE);
			viewHolder.getLlContent().setBackgroundResource(
					R.drawable.bg_message_me);
		} else {
			strHeadUrl = bean.getSuHeadUrl();
			viewHolder.getLlLeftHead().setTag(-10101, "yes");
			viewHolder.getLlLeftHead().setVisibility(View.VISIBLE);
			viewHolder.getLlRightHead().setVisibility(View.GONE);
			viewHolder.getLlContent().setBackgroundResource(
					R.drawable.bg_message_other);
		}

		viewHolder.getTxtContent().setText(strContent);
		viewHolder.getTxtTime().setText(strTime);

		if (viewHolder.getLlLeftHead().getTag(-10101).equals("yes")) {
			viewHolder.getImgLeftHead().setTag(-12121, strHeadUrl);
		} else {
			viewHolder.getImgRightHead().setTag(-12121, strHeadUrl);
		}
		ImageDownloadItem item = new ImageDownloadItem();
		item.imageUrl = strHeadUrl;
		item.callback = new ImageDownloadCallback() {
			@Override
			public void update(Bitmap bitmap, String imageUrl) {
				ImageView imageViewByTag = (ImageView) convertView
						.findViewWithTag(imageUrl);
				if (imageViewByTag == null) {
					return;
				}
				if (imageViewByTag.getTag(-10101).equals("yes")) {
					imageViewByTag.setImageBitmap(bitmap);
					imageViewByTag.setVisibility(View.VISIBLE);
					imageViewByTag.setVisibility(View.GONE);
				} else {
					imageViewByTag.setImageBitmap(bitmap);
					imageViewByTag.setVisibility(View.VISIBLE);
					imageViewByTag.setVisibility(View.GONE);
				}
				notifyDataSetChanged();
				bitmap = null;
			}
		};
		ImageDownloadThread imageDownloadThread = ImageDownloadThread
				.getInstance();
		bitmap = imageDownloadThread.downloadWithCache(item);
		if (bitmap != null) {
			if (viewHolder.getLlLeftHead().getTag(-10101).equals("yes")) {
				viewHolder.getImgLeftHead().setImageBitmap(bitmap);
				viewHolder.getLlLeftHead().setVisibility(View.VISIBLE);
				viewHolder.getLlRightHead().setVisibility(View.GONE);
			} else {
				viewHolder.getImgRightHead().setImageBitmap(bitmap);
				viewHolder.getLlRightHead().setVisibility(View.VISIBLE);
				viewHolder.getLlLeftHead().setVisibility(View.GONE);
			}
			notifyDataSetChanged();
		} else {
			viewHolder.getImgRightHead().setImageBitmap(null);
			viewHolder.getImgLeftHead().setImageBitmap(null);
		}
		bitmap = null;
		return convertView;
	}

	final static class ViewHolder {
		TextView txtContent = null;
		TextView txtTime = null;
		ImageView imgLeftHead = null;
		ImageView imgRightHead = null;
		LinearLayout llLeftHead = null;
		LinearLayout llRightHead = null;
		LinearLayout llContent = null;

		public LinearLayout getLlContent() {
			return llContent;
		}

		public void setLlContent(LinearLayout llContent) {
			this.llContent = llContent;
		}

		public LinearLayout getLlLeftHead() {
			return llLeftHead;
		}

		public void setLlLeftHead(LinearLayout llLeftHead) {
			this.llLeftHead = llLeftHead;
		}

		public LinearLayout getLlRightHead() {
			return llRightHead;
		}

		public void setLlRightHead(LinearLayout llRightHead) {
			this.llRightHead = llRightHead;
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

		public ImageView getImgLeftHead() {
			return imgLeftHead;
		}

		public void setImgLeftHead(ImageView imgLeftHead) {
			this.imgLeftHead = imgLeftHead;
		}

		public ImageView getImgRightHead() {
			return imgRightHead;
		}

		public void setImgRightHead(ImageView imgRightHead) {
			this.imgRightHead = imgRightHead;
		}

	}

}
