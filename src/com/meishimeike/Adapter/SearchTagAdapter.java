package com.meishimeike.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanTag;

/**
 * @author :LiuQiang
 * @version ：2012-6-6 class desc
 */
public class SearchTagAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeanTag> arrList;
	private LayoutInflater mInflater;

	public SearchTagAdapter(Context context, ArrayList<BeanTag> arr) {
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
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int pos, View cView, ViewGroup arg2) {
		BeanTag bean = null;
		TextView txtItem = null;
		if (cView == null) {
			cView = mInflater.inflate(R.layout.search_tag_list_item, null);
			txtItem = (TextView) cView.findViewById(R.id.txtItem);
			cView.setTag(txtItem);
		} else {
			txtItem = (TextView) cView.getTag();
		}

		bean = arrList.get(pos);
		txtItem.setText(bean.getTagName());
		return cView;
	}

}
