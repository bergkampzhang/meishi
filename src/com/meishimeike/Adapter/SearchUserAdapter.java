package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.ActPerson;
import com.meishimeike.ActTa;
import com.meishimeike.R;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Bll.BllSend;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;

/**
 * @author:LiuQiang
 * @version：2012-7-18 desc:
 */
public class SearchUserAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeanUserInfo> arrList = null;
	private LayoutInflater mInflater;
	private static boolean[] isFocused;
	private String strUrl = "";
	private Bitmap bitmap = null;
	private ViewHolder viewHolder = null;
	private View convertView = null;
	private String strUId;
	private BllSend bllSend = null;
	private AlertDialog dlgProgress = null;
	private String strMessage;
	private Thread comitThread = null;
	private Button btn = null;
	private BeanUserInfo tempBean = null;
	private LocalVariable lv = null;

	public SearchUserAdapter(Context context, ArrayList<BeanUserInfo> arr) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		bllSend = new BllSend(mContext);
		lv = new LocalVariable(mContext);
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
	public View getView(final int pos, View cView, ViewGroup parent) {
		convertView = cView;
		BeanUserInfo bean = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.search_user_item, null);
			viewHolder = new ViewHolder();
			viewHolder.setTxtName((TextView) convertView
					.findViewById(R.id.txtName));
			viewHolder.setTxtDesc((TextView) convertView
					.findViewById(R.id.txtDesc));
			viewHolder.setImgHead((ImageView) convertView
					.findViewById(R.id.imgHead));
			viewHolder.setBtnAttention((Button) convertView
					.findViewById(R.id.btnAttention));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bean = arrList.get(pos);
		viewHolder.getTxtName().setText(bean.getName());
		viewHolder.getTxtDesc().setText(bean.getIntro());
		if (bean.getUid().equals(lv.getUid())) {
			viewHolder.getBtnAttention().setVisibility(View.INVISIBLE);
		}else{
			viewHolder.getBtnAttention().setVisibility(View.VISIBLE);
		}
		viewHolder.getBtnAttention().setText(bean.isAttention() ? "已关注" : "关注");
		viewHolder.getBtnAttention().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn = (Button) v.findViewById(R.id.btnAttention);
				tempBean = arrList.get(pos);
				if (tempBean.isAttention()) {
					Toast.makeText(mContext, "该用户已关注", Toast.LENGTH_SHORT)
							.show();
					btn.setText("已关注");
				} else {
					strUId = tempBean.getUid();
					send();
				}

			}
		});

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
		viewHolder.getImgHead().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String strUid = arrList.get(pos).getUid();
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
		return convertView;
	}

	private void send() {
		dlgProgress = ProgressDialog.show(mContext, "请等待...", "正在进行操作.....",
				true);
		dlgProgress.show();
		comitThread = new Thread() {
			public void run() {
				try {
					if (dataCheck()) {
						handler.sendEmptyMessage(1);
					}
				} catch (Exception ex) {
					strMessage = ex.getLocalizedMessage();
					handler.sendEmptyMessage(2);
				}
			};
		};
		comitThread.start();
	}

	private boolean dataCheck() {
		boolean b = false;
		String str = "";
		str = bllSend.Attention(strUId);
		if (str.split("#")[0].equals("1")) {
			strMessage = str.split("#")[1];
			b = true;
		} else {
			strMessage = str.split("#")[1];
			handler.sendEmptyMessage(2);
		}
		return b;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(mContext, strMessage, Toast.LENGTH_LONG).show();
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				tempBean.setAttention(true);
				btn.setText("已关注");
				notifyDataSetChanged();
				break;
			case 2:
				if (dlgProgress != null && dlgProgress.isShowing()) {
					dlgProgress.dismiss();
				}
				Toast.makeText(mContext, strMessage, Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	final static class ViewHolder {
		TextView txtName = null;
		TextView txtDesc = null;
		ImageView imgHead = null;
		Button btnAttention = null;

		public Button getBtnAttention() {
			return btnAttention;
		}

		public void setBtnAttention(Button btnAttention) {
			this.btnAttention = btnAttention;
		}

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
