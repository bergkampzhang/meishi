package com.meishimeike.DishTabWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meishimeike.R;

public class DishNavigateBar extends LinearLayout {
	private Button btnLeft;
	private Button btnRight;
	private TextView tvBarText;
	private OnDishNavRightClickListener onRightClickListener;
	private OnDishNavLeftClickListener onLeftClickListener;

	public DishNavigateBar(Context context) {
		this(context, null);
	}

	public DishNavigateBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.dish_navigatebar, this,
				true);
		initView(context, attrs);
	}
	
	public void setTitle(String _title) {
		tvBarText.setText(_title);
	}

	private void initView(Context context, AttributeSet attrs) {
		btnLeft = (Button) findViewById(R.id.DishNAVBUTTONLEFT);
		btnRight = (Button) findViewById(R.id.DishNAVBUTTONRIGHT);
		tvBarText = (TextView) findViewById(R.id.DishNAVTITLE);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.DishNavBar);
		CharSequence barText = ta.getText(R.styleable.DishNavBar_DishBarText);
		tvBarText.setText(barText);
		boolean showRightButton = ta.getBoolean(
				R.styleable.DishNavBar_DishHaveRightButton, true);
		if (showRightButton)
			btnRight.setVisibility(View.VISIBLE);
		else
			btnRight.setVisibility(View.INVISIBLE);
		CharSequence navText = ta
				.getText(R.styleable.DishNavBar_DishNavRightText);
		btnRight.setText(navText);
		Drawable d = ta
				.getDrawable(R.styleable.DishNavBar_DishNavRightBackground);
		if (d != null) {
			btnRight.setBackgroundDrawable(d);
		}

		btnRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (onRightClickListener != null) {
					onRightClickListener.onClickListenr();
				}
			}
		});

		boolean showLeftButton = ta.getBoolean(
				R.styleable.DishNavBar_DishHaveLeftButton, false);
		if (showLeftButton)
			btnLeft.setVisibility(View.VISIBLE);
		else
			btnLeft.setVisibility(View.INVISIBLE);
		navText = ta.getText(R.styleable.DishNavBar_DishNavLeftText);
		btnLeft.setText(navText);
		d = ta.getDrawable(R.styleable.DishNavBar_DishNavLeftBackground);
		if (d != null) {
			btnLeft.setBackgroundDrawable(d);
		}

		btnLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (onLeftClickListener != null) {
					onLeftClickListener.onClickListenr();
				}
			}
		});

		Drawable bgDrawable = null;
		if (showLeftButton && showRightButton) {
			bgDrawable = context.getResources().getDrawable(
					R.drawable.navigate_both_bg);
		} else if (showLeftButton) {
			bgDrawable = context.getResources().getDrawable(
					R.drawable.navigate_left_bg);
		} else if (showRightButton) {
			bgDrawable = context.getResources().getDrawable(
					R.drawable.naviage_right_bg);
		} else {
			bgDrawable = context.getResources().getDrawable(
					R.drawable.navigate_no_bg);
		}
		// Drawable bgDrawable =
		// ta.getDrawable(R.styleable.NavBar_BarBackground);
		if (bgDrawable != null) {
			this.setBackgroundDrawable(bgDrawable);
		}
		ta.recycle();
	}

	public void setOnDishNavRightClickListener(OnDishNavRightClickListener click) {
		this.onRightClickListener = click;
	}

	public void setOnDishNavLeftClickListener(OnDishNavLeftClickListener click) {
		this.onLeftClickListener = click;
	}
}
