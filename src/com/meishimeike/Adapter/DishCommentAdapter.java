package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.ActComment;
import com.meishimeike.ActPerson;
import com.meishimeike.ActTa;
import com.meishimeike.R;
import com.meishimeike.Bean.BeanComment;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;

/**
 * @author :LiuQiang
 * @version ：2012-5-23 class desc
 */
public class DishCommentAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeanComment> arrList;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder = null;
	private Bitmap bitmap = null;
	private View convertView = null;
	private LocalVariable lv = null;
	private String strFid = "", strAUid = "";

	public DishCommentAdapter(Context context, ArrayList<BeanComment> arr,
			String fid, String auid) {
		strAUid = auid;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		lv = new LocalVariable(mContext);
		arrList = arr;
		strFid = fid;
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
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View cView, ViewGroup parent) {
		convertView = cView;
		BeanComment bean = null;
		String strComment = "", strHeadUrl = "";
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.dish_comment_item, null);
			viewHolder = new ViewHolder();
			viewHolder.setTxtComment((TextView) convertView
					.findViewById(R.id.txtComment));
			viewHolder.setImgHead((ImageView) convertView
					.findViewById(R.id.imgHead));
			viewHolder.setBtnComment((Button) convertView
					.findViewById(R.id.btnComment));
			viewHolder.setRlBtn((RelativeLayout) convertView
					.findViewById(R.id.rlBtn));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bean = arrList.get(position);
		strComment = bean.getName();
		if (!"".equals(bean.getA_uName())) {
			strComment += " 回复  " + bean.getA_uName() + " : ";
		} else {
			strComment += " : ";
		}
		strComment += bean.getContent();
		viewHolder.getTxtComment().setText(Html.fromHtml(strComment));

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
		viewHolder.getImgHead().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String strUid = arrList.get(position).getUid();
				Intent intent = new Intent();
				if (strUid.equals(lv.getUid())) {
					intent.setClass(mContext, ActPerson.class);
				} else {
					intent.setClass(mContext, ActTa.class);
					intent.putExtra(ActTa.KEY_UID, strUid);
				}
				mContext.startActivity(intent);
			}
		});
		viewHolder.getBtnComment().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lv.getIsLogin()) {
					BeanComment bean = arrList.get(position);
					Intent intent = new Intent();
					intent.setClass(mContext, ActComment.class);
					intent.putExtra(ActComment.CUR_FID, strFid);
					intent.putExtra(ActComment.CUR_AUID, strAUid);
					intent.putExtra(ActComment.CUR_RID, bean.getId());
					intent.putExtra(ActComment.CUR_UID, bean.getUid());
					mContext.startActivity(intent);
				} else {
					Toast.makeText(mContext, "亲~你还没登录呐", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		return convertView;
	}

	final static class ViewHolder {
		TextView txtComment = null;
		ImageView imgHead = null;
		Button btnComment = null;
		RelativeLayout rlBtn = null;

		public RelativeLayout getRlBtn() {
			return rlBtn;
		}

		public void setRlBtn(RelativeLayout rlBtn) {
			this.rlBtn = rlBtn;
		}

		public Button getBtnComment() {
			return btnComment;
		}

		public void setBtnComment(Button btnComment) {
			this.btnComment = btnComment;
		}

		public TextView getTxtComment() {
			return txtComment;
		}

		public void setTxtComment(TextView txtComment) {
			this.txtComment = txtComment;
		}

		public ImageView getImgHead() {
			return imgHead;
		}

		public void setImgHead(ImageView imgHead) {
			this.imgHead = imgHead;
		}

	}

}
