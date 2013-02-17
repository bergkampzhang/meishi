package com.meishimeike.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanTag;

public class PersonModifyTagAdapter extends BaseAdapter {
	private Context mContext = null;
	private ArrayList<BeanTag> arrList = null;
	private LayoutInflater mInflater;
	private static boolean[] isFocused;
	private List<String> arrTags = null;
	private boolean isInit = true;

	public PersonModifyTagAdapter(Context context, ArrayList<BeanTag> _list,
			List<String> _tags) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.arrList = _list;
		if (arrList != null) {
			isFocused = new boolean[arrList.size()];
			for (int i = 0; i < arrList.size(); i++) {
				isFocused[i] = false;
			}
		}
		if (_tags != null && _tags.size() > 0) {
			arrTags = _tags;
		} else {
			isInit = false;
		}
	}

	public void setFocus(int position, boolean focus) {
		isInit = false;
		isFocused[position] = focus;
		notifyDataSetChanged();
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
		BeanTag bean = null;
		TextView txtItem = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.person_modify_tag_item,
					null);
			txtItem = (TextView) convertView.findViewById(R.id.txtTagItem);
			convertView.setTag(txtItem);
		} else {
			txtItem = (TextView) convertView.getTag();
		}

		bean = arrList.get(position);
		if (isInit) {
			if (arrTags.contains(bean.getTagId())) {
				isFocused[position] = true;
				txtItem.setTag(true);
				txtItem.setTextColor(mContext.getResources().getColor(
						R.color.red));
			} else {
				txtItem.setTag(false);
				txtItem.setTextColor(mContext.getResources().getColor(
						R.color.gray));
			}
		} else {
			if (isFocused[position]) {
				txtItem.setTag(true);
				txtItem.setTextColor(mContext.getResources().getColor(
						R.color.red));
			} else {
				txtItem.setTag(false);
				txtItem.setTextColor(mContext.getResources().getColor(
						R.color.gray));
			}
		}

		txtItem.setText(bean.getTagName());
		return convertView;
	}

}
