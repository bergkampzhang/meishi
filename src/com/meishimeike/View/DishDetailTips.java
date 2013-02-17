package com.meishimeike.View;

import com.meishimeike.R;
import com.meishimeike.Bean.BeanFood;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author :LiuQiang
 * @version ：2012-6-8 class desc
 */
public class DishDetailTips extends LinearLayout {
	private Context mContext;
	private BeanFood beanFood = null;
	private TextView txtMenu, txtTips;
	private String strMenu = "", strTips = "";
	private LinearLayout llMenu, llTips;

	public DishDetailTips(Context context, BeanFood _bean, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		beanFood = _bean;
		init_ctrl();
		init_data();
	}

	private void init_ctrl() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dish_detail_tips, this);
		txtMenu = (TextView) view.findViewById(R.id.txtMenu);
		txtTips = (TextView) view.findViewById(R.id.txtTips);

		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		llTips = (LinearLayout) findViewById(R.id.llTips);
	}

	private void init_data() {
		String str = "";
		strMenu = beanFood.getMenu();
		strTips = beanFood.getTips();
		if (strMenu.equals("")) {
			llMenu.setVisibility(View.GONE);
		}
		if (strTips.equals("")) {
			llTips.setVisibility(View.GONE);
		}
		txtMenu.setText(strMenu + str);
		txtTips.setText(strTips + str);
	}

}
