package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanFood;

public class SendNameAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeanFood> arrList;
	private LayoutInflater mInflater;

	public SendNameAdapter(Context context, ArrayList<BeanFood> arr) {
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
		return arrList.get(position).getName();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int pos, View cView, ViewGroup parent) {
		BeanFood bean = null;
		TextView txtItem = null;
		if (cView == null) {
			cView = mInflater.inflate(R.layout.send_name_item, null);
			txtItem = (TextView) cView.findViewById(R.id.txtItem);
			cView.setTag(txtItem);
		} else {
			txtItem = (TextView) cView.getTag();
		}

		bean = arrList.get(pos);
		txtItem.setText(bean.getName());
		return cView;
	}

}
