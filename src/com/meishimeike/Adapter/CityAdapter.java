package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanCity;

/**
 * @author :LiuQiang
 * @version ：2012-6-18 class desc
 */
public class CityAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeanCity> arrList;
	private LayoutInflater mInflater;

	public CityAdapter(Context context, ArrayList<BeanCity> arr) {
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
		BeanCity bean = null;
		TextView txtItem = null;
		if (cView == null) {
			cView = mInflater.inflate(R.layout.city_item, null);
			txtItem = (TextView) cView.findViewById(R.id.txtCity);
			cView.setTag(txtItem);
		} else {
			txtItem = (TextView) cView.getTag();
		}

		bean = arrList.get(pos);
		txtItem.setText(bean.getName());
		return cView;
	}

}
