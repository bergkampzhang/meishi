package com.meishimeike.DishTabWidget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meishimeike.ActDinner;
import com.meishimeike.ActDish;
import com.meishimeike.ActDishAttention;
import com.meishimeike.ActShare;
import com.meishimeike.R;
import com.meishimeike.Utils.LocalVariable;
import com.meishimeike.weibo.sina.ActWeiboGuide;

public class DishBottomBar extends LinearLayout {

	private Context mContext;
	private DishBottomButton[] buttons;
	private int position;
	private AttributeSet attrs = null;
	private LocalVariable lv = null;

	public DishBottomBar(Context context) {
		super(context);
		mContext = context;
		lv = new LocalVariable(mContext);
	}

	public DishBottomBar(Context context, AttributeSet _attrs) {
		super(context, _attrs);
		attrs = _attrs;
		mContext = context;
		lv = new LocalVariable(mContext);
		LayoutInflater.from(context).inflate(R.layout.dish_bottombar, this,
				true);
		initView(context, attrs);
	}

	private void initView(Context context, AttributeSet attrs) {
		buttons = new DishBottomButton[4];
		buttons[0] = (DishBottomButton) findViewById(R.id.btnReport);
		buttons[1] = (DishBottomButton) findViewById(R.id.btnWhoCome);
		buttons[2] = (DishBottomButton) findViewById(R.id.btnGoDinner);
		buttons[3] = (DishBottomButton) findViewById(R.id.btnShare);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.DishBottomBar);
		position = ta.getInt(R.styleable.DishBottomBar_DishBBarPosition, 0);
		changeBtn(position);
		buttons[position].setGravity(Gravity.CENTER);
		for (int i = 0; i < buttons.length; i++) {
			final int o = i;
			buttons[i]
					.setOnBottomButtonClickListener(new OnDishBottomButtonClickListener() {
						@Override
						public void onClickListenr() {
							Intent intent = new Intent();
							boolean isForward = false;
							switch (o) {
							case 0:
								if (position != 0) {
									intent.setClass(mContext, ActDish.class);
									isForward = true;
								}
								break;
							case 1:
								if (position != 1) {
									intent.setClass(mContext,
											ActDishAttention.class);
									isForward = true;
								}
								break;
							case 2:
								if (position != 2) {
									intent.setClass(mContext, ActDinner.class);
									isForward = true;
								}
								break;
							case 3:
								if (position != 3) {
									if (lv.getWUid().equals("")) {
										intent.setClass(mContext,
												ActWeiboGuide.class);
									} else {
										intent.setClass(mContext,
												ActShare.class);
									}
									isForward = false;
								}
								break;
							default:
								break;
							}
							try {
								if (isForward) {
									mContext.startActivity(intent);
									Activity ac = (Activity) mContext;
									ac.finish();
								} else {
									mContext.startActivity(intent);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		}
	}

	private void changeBtn(int i) {
		ImageView imgBtn;
		switch (i) {
		case 0:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_dish_main_focus);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_who_come);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_go_dinner);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_share);
			break;
		case 1:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_dish_main);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_who_come_focus);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_go_dinner);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_share);
			break;
		case 2:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_dish_main);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_who_come);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_go_dinner_focus);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_share);
			break;
		case 3:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_dish_main);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_who_come);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_go_dinner);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgDishBtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_share_focus);
			break;
		}
	}
}
