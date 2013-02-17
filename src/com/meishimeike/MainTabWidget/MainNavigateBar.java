package com.meishimeike.MainTabWidget;

import com.meishimeike.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainNavigateBar extends LinearLayout {
	private Button btnLeft;
	private Button btnRight;
	private TextView tvBarText;
	private OnMainNavRightClickListener onRightClickListener;
	private OnMainNavLeftClickListener onLeftClickListener;

	public MainNavigateBar(Context context) {
		this(context, null);
	}

	public MainNavigateBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.main_navigatebar, this,
				true);
		initView(context, attrs);
	}

	public void setTitle(String _title) {
		tvBarText.setText(_title);
	}

	private void initView(Context context, AttributeSet attrs) {
		btnLeft = (Button) findViewById(R.id.NAVBUTTONLEFT);
		btnRight = (Button) findViewById(R.id.NAVBUTTONRIGHT);
		tvBarText = (TextView) findViewById(R.id.NAVTITLE);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.NavBar);
		CharSequence barText = ta.getText(R.styleable.NavBar_BarText);
		tvBarText.setText(barText);

		Drawable bgDrawable = null;
		bgDrawable = ta.getDrawable(R.styleable.NavBar_BarBackground);
		if (bgDrawable != null) {
			tvBarText.setBackgroundDrawable(bgDrawable);
		}

		boolean showRightButton = ta.getBoolean(
				R.styleable.NavBar_HaveRightButton, true);
		if (showRightButton)
			btnRight.setVisibility(View.VISIBLE);
		else
			btnRight.setVisibility(View.INVISIBLE);
		CharSequence navText = ta.getText(R.styleable.NavBar_NavRightText);
		btnRight.setText(navText);
		Drawable d = ta.getDrawable(R.styleable.NavBar_NavRightBackground);
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
				R.styleable.NavBar_HaveLeftButton, false);
		if (showLeftButton)
			btnLeft.setVisibility(View.VISIBLE);
		else
			btnLeft.setVisibility(View.INVISIBLE);
		navText = ta.getText(R.styleable.NavBar_NavLeftText);
		btnLeft.setText(navText);
		d = ta.getDrawable(R.styleable.NavBar_NavLeftBackground);
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

	public void setOnMainNavRightClickListener(OnMainNavRightClickListener click) {
		this.onRightClickListener = click;
	}

	public void setOnMainNavLeftClickListener(OnMainNavLeftClickListener click) {
		this.onLeftClickListener = click;
	}
}
