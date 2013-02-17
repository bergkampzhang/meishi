package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanTagNum;

public class TaTagNumAdapter extends BaseAdapter {
	private Context mContext = null;
	private ArrayList<BeanTagNum> arrList = null;
	private LayoutInflater mInflater;

	public TaTagNumAdapter(Context context, ArrayList<BeanTagNum> _list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		BeanTagNum bean = null;
		TextView txtItem = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ta_tag_num_item,
					null);
			txtItem = (TextView) convertView.findViewById(R.id.txtTagItem);
			convertView.setTag(txtItem);
		} else {
			txtItem = (TextView) convertView.getTag();
		}

		bean = arrList.get(position);

		txtItem.setText(bean.getName() + " " + bean.getNum());
		return convertView;
	}

}
