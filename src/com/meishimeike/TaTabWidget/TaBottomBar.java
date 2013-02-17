package com.meishimeike.TaTabWidget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meishimeike.ActTa;
import com.meishimeike.ActTaAttention;
import com.meishimeike.ActTaFans;
import com.meishimeike.ActUserMsgSend;
import com.meishimeike.R;

public class TaBottomBar extends LinearLayout {

	private Context mContext;
	private TaBottomButton[] buttons;
	private int position;
	private AttributeSet attrs = null;

	public TaBottomBar(Context context) {
		super(context);
		mContext = context;
	}

	public TaBottomBar(Context context, AttributeSet _attrs) {
		super(context, _attrs);
		attrs = _attrs;
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.ta_bottombar, this, true);
		initView(mContext, attrs);
	}

	private void initView(Context context, AttributeSet attrs) {
		buttons = new TaBottomButton[4];
		buttons[0] = (TaBottomButton) findViewById(R.id.homeBtn);
		buttons[1] = (TaBottomButton) findViewById(R.id.typeBtn);
		buttons[2] = (TaBottomButton) findViewById(R.id.infoBtn);
		buttons[3] = (TaBottomButton) findViewById(R.id.buyBtn);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.BottomBar);
		position = ta.getInt(R.styleable.BottomBar_BBarPosition, 0);
		changeBtn(position);
		buttons[position].setGravity(Gravity.CENTER);
		for (int i = 0; i < buttons.length; i++) {
			final int o = i;
			buttons[i]
					.setOnBottomButtonClickListener(new OnTaBottomButtonClickListener() {
						@Override
						public void onClickListenr() {
							Intent intent = new Intent();
							boolean isForward = false;
							switch (o) {
							case 0:
								if (position != 0) {
									intent.setClass(mContext, ActTa.class);
									isForward = true;
								}
								break;
							case 1:
								if (position != 1) {
									intent.setClass(mContext,
											ActUserMsgSend.class);
									isForward = false;
								}
								break;
							case 2:
								if (position != 2) {
									intent.setClass(mContext,
											ActTaAttention.class);
									isForward = true;
								}
								break;
							case 3:
								if (position != 3) {
									intent.setClass(mContext, ActTaFans.class);
									isForward = true;
								}
								break;
							default:
								break;
							}
							if (isForward) {
								mContext.startActivity(intent);
								Activity ac = (Activity) mContext;
								ac.finish();
							} else {
								 try {
									mContext.startActivity(intent);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
		}
	}

	private void changeBtn(int i) {
		ImageView imgBtn;
		switch (i) {
		case 0:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_ta_focus);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_msgta);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_ta_attention);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_tafans);
			break;
		case 1:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_ta);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_msgta_focus);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_ta_attention);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_tafans);
			break;
		case 2:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_ta);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_msgta);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgbtn);
			imgBtn
					.setBackgroundResource(R.drawable.btn_bottom_ta_attention_focus);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_tafans);
			break;
		case 3:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_ta);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_msgta);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_ta_attention);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_tafans_focus);
			break;
		}
	}

}
