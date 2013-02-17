package com.meishimeike.View;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meishimeike.ActDish;
import com.meishimeike.R;
import com.meishimeike.Adapter.PersonGridAdapter;
import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Bll.BllGet;

/**
 * @author :LiuQiang
 * @version ：2012-5-28 class desc
 */
public class PersonDescView extends RelativeLayout implements OnClickListener {
	private Context mContext = null;
	private Button btnList, btnGrid;
	private GridView gvDish;
	private String strUId = "";
	private ArrayList<BeanFood> arrList = null;
	private BllGet bllGet = null;
	private LinearLayout llDish = null;
	private PersonGridAdapter gridAdapter = null;
	private LinearLayout llParentLoad = null;
	private TextView txtCtrlMsg = null, txtLoad = null;
	private Thread addThread = null;

	public PersonDescView(Context context, AttributeSet attrs, String _UId,
			LinearLayout _llLoad) {
		super(context, attrs);
		strUId = _UId;
		mContext = context;
		llParentLoad = _llLoad;
		bllGet = new BllGet(mContext);
		init_ctrl();
		new myTask().execute((Void) null);
	}

	private void init_ctrl() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dinner_desc_view, this);
		txtLoad = (TextView) view.findViewById(R.id.txtLoad);
		txtCtrlMsg = (TextView) view.findViewById(R.id.txtCtrlMsg);
		btnList = (Button) view.findViewById(R.id.btnList);
		btnList.setOnClickListener(this);
		btnGrid = (Button) view.findViewById(R.id.btnGrid);
		btnGrid.setOnClickListener(this);
		llDish = (LinearLayout) view.findViewById(R.id.llDish);
		gvDish = (GridView) view.findViewById(R.id.gvDish);
		gvDish.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(mContext, ActDish.class);
				intent.putExtra(ActDish.KEY_FID, arrList.get(pos).getFid());
				mContext.startActivity(intent);

			}
		});
	}

	private void init_data() {
		arrList = bllGet.getUserFood(strUId);

		gridAdapter = new PersonGridAdapter(mContext, arrList);
	}

	private void setCtrlData() {
		if (arrList != null && arrList.size() > 0) {
			txtCtrlMsg.setText("全部" + arrList.size() + "条美食");
			addThread = new Thread(addRunnable);
			addThread.start();
		} else {
			txtCtrlMsg.setText("全部0条美食");
			txtLoad.setVisibility(View.GONE);
		}
		llDish.setVisibility(View.VISIBLE);
		gvDish.setVisibility(View.GONE);
		// load grid view
		gvDish.setAdapter(gridAdapter);
		int totalHeight = 0;
		int count = gridAdapter.getCount();
		int item = 0;
		for (int i = 0; i < gridAdapter.getCount(); i++) {
			View listItem = gridAdapter.getView(i, null, gvDish);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			item = listItem.getMeasuredHeight();
		}
		totalHeight = (totalHeight / 4) + (count % 4 > 0 ? 1 : 0) * item;
		ViewGroup.LayoutParams params = gvDish.getLayoutParams();
		params.height = totalHeight;
		gvDish.setLayoutParams(params);
	}

	private Runnable addRunnable = new Runnable() {

		@Override
		public void run() {
			for (BeanFood bean : arrList) {
				PersonDetailItem pdItem = new PersonDetailItem(mContext, null,
						bean);
				Message msg = new Message();
				msg.obj = pdItem;
				msg.what = 0;
				handler.sendMessage(msg);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				PersonDetailItem view = (PersonDetailItem) msg.obj;
				llDish.addView(view);
				break;
			case 1:
				txtLoad.setVisibility(View.GONE);
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		int resId = v.getId();
		changeBg(resId);
		switch (resId) {
		case R.id.btnList:
			llDish.setVisibility(View.VISIBLE);
			gvDish.setVisibility(View.GONE);
			break;
		case R.id.btnGrid:
			llDish.setVisibility(View.GONE);
			gvDish.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void changeBg(int resId) {
		switch (resId) {
		case R.id.btnList:
			btnList.setBackgroundResource(R.drawable.img_list_focus);
			btnGrid.setBackgroundResource(R.drawable.img_grid);
			break;
		case R.id.btnGrid:
			btnList.setBackgroundResource(R.drawable.img_list);
			btnGrid.setBackgroundResource(R.drawable.img_grid_focus);
			break;
		}
	}

	private class myTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			init_data();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (arrList != null) {
				setCtrlData();
			} else {
				txtCtrlMsg.setText("全部0条美食");
				txtLoad.setVisibility(View.GONE);
			}
			llParentLoad.setVisibility(View.GONE);
		}
	}

}
