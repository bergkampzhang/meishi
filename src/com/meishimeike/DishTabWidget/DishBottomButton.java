package com.meishimeike.DishTabWidget;

import com.meishimeike.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DishBottomButton extends RelativeLayout {
	private ImageView imgbtn;
	private OnDishBottomButtonClickListener onBottomButtonClickListener;

	public DishBottomButton(Context context) {
		super(context);
	}

	public DishBottomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.dish_bottombutton, this,
				true);
		initView(context, attrs);
	}

	private void initView(Context context, AttributeSet attrs) {
		imgbtn = (ImageView) findViewById(R.id.imgDishBtn);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.DishBottomButton);
		Drawable bgDrawable = ta
				.getDrawable(R.styleable.DishBottomButton_DishButtonSrc);
		if (bgDrawable != null)
			imgbtn.setBackgroundDrawable(bgDrawable);
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onBottomButtonClickListener != null) {
					onBottomButtonClickListener.onClickListenr();
				}
			}
		});
	}

	public void setOnBottomButtonClickListener(
			OnDishBottomButtonClickListener click) {
		this.onBottomButtonClickListener = click;
	}

}
