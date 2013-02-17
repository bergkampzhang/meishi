package com.meishimeike.MainTabWidget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.meishimeike.ActDaren;
import com.meishimeike.ActGetPic;
import com.meishimeike.ActLogin;
import com.meishimeike.ActMain;
import com.meishimeike.ActPerson;
import com.meishimeike.ActSearch;
import com.meishimeike.R;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.LocalVariable;

public class MainBottomBar extends LinearLayout {

	private Context mContext;
	private MainBottomButton[] buttons;
	private int position;
	private AttributeSet attrs = null;
	private LocalVariable lv = null;
	private Commons coms = null;

	public MainBottomBar(Context context) {
		super(context);
		mContext = context;
	}

	public MainBottomBar(Context context, AttributeSet _attrs) {
		super(context, _attrs);
		attrs = _attrs;
		mContext = context;
		coms = new Commons();
		lv = new LocalVariable(mContext);
		LayoutInflater.from(context).inflate(R.layout.main_bottombar, this,
				true);
		initView(mContext, attrs);
	}

	private void initView(Context context, AttributeSet attrs) {
		buttons = new MainBottomButton[5];
		buttons[0] = (MainBottomButton) findViewById(R.id.btnMain);
		buttons[1] = (MainBottomButton) findViewById(R.id.btnSearch);
		buttons[2] = (MainBottomButton) findViewById(R.id.btnCamera);
		buttons[3] = (MainBottomButton) findViewById(R.id.btnTopic);
		buttons[4] = (MainBottomButton) findViewById(R.id.btnPerson);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.BottomBar);
		position = ta.getInt(R.styleable.BottomBar_BBarPosition, 0);
		changeBtn(position);
		buttons[position].setGravity(Gravity.CENTER);
		for (int i = 0; i < buttons.length; i++) {
			final int o = i;
			buttons[i]
					.setOnBottomButtonClickListener(new OnMainBottomButtonClickListener() {
						@Override
						public void onClickListenr() {
							Intent intent = new Intent();
							boolean isForward = false;
							switch (o) {
							case 0:
								if (position != 0) {
									intent.setClass(mContext, ActMain.class);
									isForward = true;
								}
								break;
							case 1:
								if (position != 1) {
									intent.setClass(mContext, ActSearch.class);
									isForward = true;
								}
								break;
							case 2:
								if (position != 2) {
									if (lv.getIsLogin()) {
										System.gc();
										intent.setClass(mContext,
												ActGetPic.class);
										coms.initSendCfg(mContext);
									} else {
										Toast.makeText(mContext, "亲~你还没登录呢",
												Toast.LENGTH_SHORT).show();
										intent.setClass(mContext,
												ActLogin.class);
									}
									isForward = false;
								}
								break;
							case 3:
								if (position != 3) {
									intent.setClass(mContext, ActDaren.class);
									isForward = true;
								}
								break;
							case 4:
								if (position != 4) {
									if (lv.getIsLogin()) {
										intent.setClass(mContext,
												ActPerson.class);
									} else {
										intent.setClass(mContext,
												ActLogin.class);
									}
									isForward = false;
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
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_main_focus);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_search);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_camera);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_super);
			imgBtn = (ImageView) buttons[4].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_person);
			break;
		case 1:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_main);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_search_focus);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_camera);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_super);
			imgBtn = (ImageView) buttons[4].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_person);
			break;
		case 2:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_main);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_search);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_camera_focus);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_super);
			imgBtn = (ImageView) buttons[4].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_person);
			break;
		case 3:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_main);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_search);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_camera);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_super_focus);
			imgBtn = (ImageView) buttons[4].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_person);
			break;
		case 4:
			imgBtn = (ImageView) buttons[0].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_main);
			imgBtn = (ImageView) buttons[1].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_search);
			imgBtn = (ImageView) buttons[2].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_camera);
			imgBtn = (ImageView) buttons[3].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_super);
			imgBtn = (ImageView) buttons[4].findViewById(R.id.imgbtn);
			imgBtn.setBackgroundResource(R.drawable.btn_bottom_person_focus);
			break;
		}
	}

}
