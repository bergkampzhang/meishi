package com.meishimeike.UserTabWidget;

import com.meishimeike.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class UserBottomButton extends RelativeLayout {
	private ImageView imgbtn;
	private OnUserBottomButtonClickListener onBottomButtonClickListener;
	
	public UserBottomButton(Context context) {
		super(context);
	}
	
	public UserBottomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.user_bottombutton, this, true);
		initView(context,attrs);
	}
	
	private void initView(Context context,AttributeSet attrs) {
		imgbtn = (ImageView)findViewById(R.id.imgbtn);
		TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.BottomButton);
		Drawable bgDrawable = ta.getDrawable(R.styleable.BottomButton_ButtonSrc);
		if(bgDrawable!=null)imgbtn.setBackgroundDrawable(bgDrawable);
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onBottomButtonClickListener!=null){
					onBottomButtonClickListener.onClickListenr();
				}
			}
		});
	}

	public void setOnBottomButtonClickListener(OnUserBottomButtonClickListener click) {
		this.onBottomButtonClickListener = click;
	}
	
}
