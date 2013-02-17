package com.meishimeike;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.meishimeike.Adapter.PersonCommentAdapter;
import com.meishimeike.Adapter.PersonMsgAdapter;
import com.meishimeike.Bean.BeanComment;
import com.meishimeike.Bean.BeanMessage;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.UserTabWidget.OnUserNavLeftClickListener;
import com.meishimeike.UserTabWidget.OnUserNavRightClickListener;
import com.meishimeike.UserTabWidget.UserNavigateBar;
import com.meishimeike.Utils.LocalVariable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author :LiuQiang
 * @version ：2012-5-28 class desc
 */
public class ActPersonMsg extends ActivityTemplate implements OnClickListener {
	private UserNavigateBar navBar;
	private Button btnMsg, btnCommend;
	private BllGet bllGet = null;
	private ListView lvMsg = null;
	private ArrayList<BeanComment> arrCommentList = null;
	private ArrayList<BeanMessage> arrMsgList = null;
	private PersonCommentAdapter comAdapter = null;
	private PersonMsgAdapter msgAdapter = null;
	private Thread comitThread = null;
	private LocalVariable lv = null;
	private int loadType = 2;
	private static final int TYPE_COMMENT = 2, TYPE_MSG = 1;
	private LinearLayout llLoad = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.act_person_msg);
		bllGet = new BllGet(this);
		lv = new LocalVariable(this);
		init_ctrl();
		init_data();
	}

	private void init_ctrl() {
		llLoad = (LinearLayout) findViewById(R.id.llLoad);

		navBar = (UserNavigateBar) findViewById(R.id.NAV);
		navBar.setOnUserNavLeftClickListener(NavLeftClickListener);
		navBar.setOnUserNavRightClickListener(NavRightClickListener);

		btnMsg = (Button) findViewById(R.id.btnMsg);
		btnMsg.setOnClickListener(this);
		btnCommend = (Button) findViewById(R.id.btnCommend);
		btnCommend.setOnClickListener(this);

		lvMsg = (ListView) findViewById(R.id.lvMsg);
		lvMsg.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Intent intent = new Intent();
				if (loadType == TYPE_MSG) {
					BeanMessage bean = arrMsgList.get(pos);
					if (lv.getUid().equals(bean.getRuid())) {
						lv.setTempMsgUId(bean.getSuid());
					} else {
						lv.setTempMsgUId(bean.getRuid());
					}
					lv.setTempMsgUName(bean.getuName());
					lv.setTempUAttention(true);
					intent.setClass(ActPersonMsg.this, ActUserMsgSend.class);
				} else {
					BeanComment beanComment = arrCommentList.get(pos);
					lv.setTempMsgUId(beanComment.getUid());
					lv.setTempMsgUName(beanComment.getName());
					lv.setTempFoodId(beanComment.getTid());
					intent
							.setClass(ActPersonMsg.this,
									ActUserCommentSend.class);
				}
				startActivity(intent);
			}
		});
	}

	private void changeImage(int resId) {
		switch (resId) {
		case R.id.btnMsg:
			btnMsg.setTextColor(getResources().getColor(R.color.red));
			btnCommend.setTextColor(getResources().getColor(R.color.gray));
			break;
		case R.id.btnCommend:
			btnMsg.setTextColor(getResources().getColor(R.color.gray));
			btnCommend.setTextColor(getResources().getColor(R.color.red));
			break;
		}
	}

	private void init_data() {
		llLoad.setVisibility(View.VISIBLE);
		comitThread = new Thread(runnable);
		comitThread.start();
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			try {
				switch (loadType) {
				case TYPE_MSG:
					arrMsgList = bllGet.getUserMessage(lv.getUid());
					break;
				case TYPE_COMMENT:
					arrCommentList = bllGet.getUserCommentReply(lv.getUid());
					break;
				}
				handler.sendEmptyMessage(1);
			} catch (Exception ex) {
				handler.sendEmptyMessage(2);
			}
		}
	};

	private void setCtrlData() {
		lvMsg.setAdapter(null);
		switch (loadType) {
		case TYPE_MSG:
			msgAdapter = new PersonMsgAdapter(this, arrMsgList);
			lvMsg.setAdapter(msgAdapter);
			break;
		case TYPE_COMMENT:
			comAdapter = new PersonCommentAdapter(this, arrCommentList);
			lvMsg.setAdapter(comAdapter);
			break;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setCtrlData();
				break;
			case 2:
				// 失败
				lvMsg.setAdapter(null);
				break;
			}
			llLoad.setVisibility(View.GONE);
		}
	};

	@Override
	public void onClick(View v) {
		int resId = v.getId();
		changeImage(resId);
		switch (resId) {
		case R.id.btnMsg:
			loadType = TYPE_MSG;
			break;
		case R.id.btnCommend:
			loadType = TYPE_COMMENT;
			break;
		}
		init_data();
	}

	private OnUserNavLeftClickListener NavLeftClickListener = new OnUserNavLeftClickListener() {
		@Override
		public void onClickListenr() {
			ActPersonMsg.this.finish();
		}
	};

	private OnUserNavRightClickListener NavRightClickListener = new OnUserNavRightClickListener() {
		@Override
		public void onClickListenr() {

		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
