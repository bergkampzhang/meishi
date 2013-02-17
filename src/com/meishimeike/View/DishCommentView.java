package com.meishimeike.View;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.meishimeike.R;
import com.meishimeike.Adapter.DishCommentAdapter;
import com.meishimeike.Bean.BeanComment;
import com.meishimeike.Bll.BllGet;

/**
 * @author :LiuQiang
 * @version ：2012-5-23 class desc
 */
public class DishCommentView extends FrameLayout {
	private Context mContext;
	private BllGet bllGet = null;
	private ListView lvComment = null;
	private DishCommentAdapter adapter = null;
	private ArrayList<BeanComment> arrList = null;
	private static String strFid = "";
	private static String strAUid = "";
	private Thread comitThread = null;
	private FrameLayout flDishComment = null;

	public DishCommentView(Context context, AttributeSet attrs, String fid,
			String auid) {
		super(context, attrs);
		mContext = context;
		strFid = fid;
		strAUid = auid;
		bllGet = new BllGet(mContext);
		init_ctrl();
		init_data();
	}

	private void init_ctrl() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dish_comment_view, this);
		flDishComment = (FrameLayout) view.findViewById(R.id.flDishComment);
		lvComment = (ListView) view.findViewById(R.id.lvComment);
	}

	public void init_data() {
		comitThread = new Thread() {
			public void run() {
				try {
					arrList = bllGet.getFoodComment(strFid);
					handler.sendEmptyMessage(1);
				} catch (Exception ex) {
					handler.sendEmptyMessage(2);
				}
			};
		};
		comitThread.start();
	}

	private void setCtrlData() {
		if (arrList != null && arrList.size() > 0) {
			flDishComment.setVisibility(View.VISIBLE);
		} else {
			flDishComment.setVisibility(View.GONE);
		}

		adapter = new DishCommentAdapter(mContext, arrList, strFid, strAUid);
		lvComment.setAdapter(adapter);
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, lvComment);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = lvComment.getLayoutParams();
		params.height = totalHeight
				+ (lvComment.getDividerHeight() * (adapter.getCount() - 1));
		lvComment.setLayoutParams(params);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setCtrlData();
				break;
			case 2:
				// 失败
				flDishComment.setVisibility(View.GONE);
				break;
			}
		}
	};

}
