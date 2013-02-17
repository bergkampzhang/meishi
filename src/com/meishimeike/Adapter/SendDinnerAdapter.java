package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanRimDinner;

public class SendDinnerAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeanRimDinner> arrList;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder = null;

	public SendDinnerAdapter(Context context, ArrayList<BeanRimDinner> arr) {
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
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int pos, View cView, ViewGroup parent) {
		BeanRimDinner bean = null;
		if (cView == null) {
			cView = mInflater.inflate(R.layout.send_dinner_item, null);
			viewHolder = new ViewHolder();
			viewHolder.setTxtName((TextView) cView.findViewById(R.id.txtDName));
			viewHolder.setTxtAddr((TextView) cView.findViewById(R.id.txtDAddr));
			cView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) cView.getTag();
		}

		bean = arrList.get(pos);
		viewHolder.getTxtName().setText(bean.getName());
		viewHolder.getTxtAddr().setText(bean.getAddr());
		return cView;
	}

	final static class ViewHolder {
		TextView txtName = null;
		TextView txtAddr = null;

		public TextView getTxtName() {
			return txtName;
		}

		public void setTxtName(TextView txtName) {
			this.txtName = txtName;
		}

		public TextView getTxtAddr() {
			return txtAddr;
		}

		public void setTxtAddr(TextView txtAddr) {
			this.txtAddr = txtAddr;
		}

	}

}
