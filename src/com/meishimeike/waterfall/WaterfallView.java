
package com.meishimeike.waterfall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.meishimeike.ActDish;
import com.meishimeike.ActMain;
import com.meishimeike.R;
import com.meishimeike.Bean.BeanFood;



import java.util.ArrayList;

public class WaterfallView extends ScrollView {

    public WaterfallView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}




	public final static int COLUMN_COUNT = 2; // 显示列数

    public final static int HANDLER_WHAT = 1;

    public final static int MESSAGE_DELAY = 200;

    public final static int SCROLL_SLOP = 0;

    public final static int SHOW_REFRESH_INDICATOR = 8;

    public final static int HIDE_REFRESH_INDICATOR = SHOW_REFRESH_INDICATOR + 1;

    public final static int SHOW_TOP = HIDE_REFRESH_INDICATOR + 1;

    public final static int HIDE_TOP = SHOW_TOP + 1;

    public final static int REFRESH_SLOP = 400;

    private int oriY = -1;

    public int columnWidth = -1;

    private boolean isRefreshing;

    private boolean hasDataOnRemote = true;

    private Context mContext;

    private LinearLayout container;
    private LayoutInflater mInflater;
    private LoaderModule mLoader;
    private int[] column_height = new int[COLUMN_COUNT];
    // private int oriIndex = 20;

    private static Object sLock = new Object();
    private LinearLayout topContainer;
    private View mFooter;
    private View mHeader;

    private OnScrollListener mOnScrollListener;
    

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

        	switch (msg.what) {
    		case 1:
    			if (getMeasuredHeight() - 20 <= 
    					getScrollY() + getHeight()) {
    				/*if(BuildConfig.DEBUG){
    					Log.d(WaterFallView.TAG,"onBottom"+ "childViewHeight->"+childView.getMeasuredHeight() +"waterFallView->"
    							+(waterFallView
    							.getScrollY() + waterFallView.getHeight()));*/
    				
    				if (mOnScrollListener != null) {
    					mOnScrollListener.OnBottom(getItemCount());
    				}
        		}
    			else if (getScrollY() <= 0) {
    				if (mOnScrollListener != null) {
    					mOnScrollListener.OnTop();
    				}
    			} 
    			else {
    				if (mOnScrollListener != null) {
    					
    					mOnScrollListener.onScroll();
    				}
    			}
    			break;
    			
    		case SHOW_REFRESH_INDICATOR:

                if (mFooter != null) {
                    mFooter.setVisibility(View.VISIBLE);
                }
                break;
    		case HIDE_REFRESH_INDICATOR:

                if (mFooter != null) {
                    if (hasDataOnRemote) {
                        mFooter.setVisibility(View.INVISIBLE);
                    } else {
                        mFooter.setVisibility(View.GONE);
                    }
                }
                break;
                
    		case SHOW_TOP:
                if (mHeader != null) 
                    mHeader.setVisibility(View.VISIBLE);
                break;
                    
    		case HIDE_TOP:
                if (mHeader != null) {
                    mHeader.setVisibility(View.GONE);
                }
                break;
             default:
            	 break;
            

        }
        }

    };

    private int oriIndex;

    private static HandlerThread workerThread = new HandlerThread(
            "workerThread");
    static {
        workerThread.start();
    }
    private static Handler workHandler = new Handler(workerThread.getLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };

    public WaterfallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mLoader = new LoaderModule(workHandler, mHandler);
        init();
    }

    private void init() {
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int displayWidth = metrics.widthPixels;
        columnWidth = displayWidth / COLUMN_COUNT;

        LinearLayout container = new LinearLayout(mContext);
        container.setTag("container");
        LinearLayout.LayoutParams containerParam = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        container.setLayoutParams(containerParam);

        topContainer = new LinearLayout(mContext);
        topContainer.setLayoutParams(containerParam);
        topContainer.setTag("topContainer");
        topContainer.setOrientation(LinearLayout.VERTICAL);
        topContainer.addView(container);

        addView(topContainer);
        addColumns(COLUMN_COUNT);

    }

    public void setFooter(View footer) {
        mFooter = footer;
        if (mFooter != null && topContainer != null) {
            mFooter.setVisibility(View.INVISIBLE);
            topContainer.addView(mFooter);
        }
    }

    public void setHeader(View header) {
        mHeader = header;
        if (mHeader != null && topContainer != null) {
            header.setVisibility(View.GONE);
            topContainer.addView(header, 0);
        }
    }

    private void addColumns(int count) {
        container = (LinearLayout) findViewWithTag("container");

        for (int i = 0; i < count; i++) {
            LinearLayout itemLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
                    columnWidth, LayoutParams.WRAP_CONTENT);

            itemLayout.setPadding(4, 2, 4, 2);
            itemLayout.setOrientation(LinearLayout.VERTICAL);

            itemLayout.setLayoutParams(itemParam);
            container.addView(itemLayout);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                oriY = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (ev.getY() - oriY >= REFRESH_SLOP && getScrollY() == 0) {
                    if (mOnScrollListener != null) {
                        if (mOnScrollListener.OnTop()) {
                            mHandler.sendEmptyMessage(SHOW_TOP);
                        }
                    }
                }
                break;

        }

        return super.onTouchEvent(ev);
    }
    
    OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d("waterfall","ACTION_DOWN" + "Y->" + event.getY() + "X->"
								+ event.getX());
				break;
			case MotionEvent.ACTION_UP:
				if (v != null && mOnScrollListener != null) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(1),
							200);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				// Log.d(TAG,"ACTION_MOVE"+"Y->"+
				// event.getY()+"X->"+event.getX());
				break;
			default:
				break;
			}
			return false;
		}

	};
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        trackScroll(l, t, oldl, oldt);
    }

    private void trackScroll(int l, int t, int oldl, int oldt) {
        // System.out.println("trackScroll t = " + t + " oldt = " + oldt
        // + " contaier measureHeight = " + container.getMeasuredHeight() +
        // " waterfallViewmeasureHeight = " + getMeasuredHeight());

        synchronized (sLock) {
            if (!isRefreshing) {
                // 向下滑动 down
                if (t > oldt) {
                	if(t> getMeasuredHeight()){
                		//reload   data
                		//refresh view
                		//recycle the unused picture
                	}
                    //recycle(false, t);
                    // 回收滑出上边界的图片
                    // 重载滑入下边界的图片
                }
                // 向上滑动 up
                else {
                    //recycle(true, t);
                    // 回收滑出下边界的图片

                    // 重载滑入上边界的图片
                }

                if (t + getMeasuredHeight() >= container.getMeasuredHeight()
                        && !isRefreshing) {
                    isRefreshing = true;

                    if (mOnScrollListener != null) {
                        hasDataOnRemote = mOnScrollListener.OnBottom(getItemCount());
                    }

                    System.out.println("bottom");
                    /*
                    ArrayList<ItemBean> beanList = new ArrayList<ItemBean>();
                    
                    int cycleMax = oriIndex + 20;
                     for (; oriIndex < cycleMax; oriIndex++) {
                    String path = "http://192.168.1.70" + File.separator
                     + "images" + File.separator + oriIndex + ".jpg";
                    ItemBean bean = new ItemBean();
                    bean.setImgFood(path);
                    bean.setImgUser("http://192.168.1.70" + File.separator +
                    "images" + File.separator + "user.png");
                    beanList.add(bean);
                    }
                    ItemBean[] beans = beanList.toArray(new ItemBean[20]);
                    
                    addItems(beans);*/
                }
            }
        }
    }

    private int getItemCount() {
        int amount = 0;
        int count = container.getChildCount();
        for (int i = 0; i < count; i++) {
            ViewGroup column = (ViewGroup) container.getChildAt(i);
            amount += column.getChildCount();
        }
        return amount;
    }

    private void recycle(boolean up, int top) {

        int columnCount = container.getChildCount();
        for (int i = 0; i < columnCount; i++) {
            ViewGroup column = (ViewGroup) container.getChildAt(i);
            View preRecycleView = null;
            View reloadView = null;
            for (int j = 0; j < column.getChildCount(); j++) {
                View item = column.getChildAt(j);
                 System.out.println("column=" + i + "    row=" + j +
                 "   item.getBottom() = " + item.getBottom() + " top = " +
                 top);

                 System.out.println("trackScroll t = " + top + " item.getTop() = " + item.getTop() +
                 " waterfallViewmeasureHeight = " + getMeasuredHeight() +
                 " preRecycleView i = " + i + " j = " );

                if (up) {
                    // 划出屏幕上边界了 找最后一个符合条件的
                    if (item.getBottom() < -2*getMeasuredHeight()){
                        preRecycleView = item;
                    }
                    if (item.getTop() <= top + 2*getMeasuredHeight()
                            + SCROLL_SLOP) {
                        reloadView = item;
                    }
                } else {
                    // 划出屏幕下边界了 找第一个符合条件的
                    if (preRecycleView == null) {
                        if (item.getTop() > top + 2*getMeasuredHeight()
                                + SCROLL_SLOP) {
                            preRecycleView = item;
                        }
                    }
                    if (reloadView == null) {
                        if (item.getBottom() >= top - SCROLL_SLOP) {
                            reloadView = item;
                        }
                    }
                }
            
            if (preRecycleView != null) {
                // System.out.println("preRecycleView i = " + i + " j = "
                // + getIndexInParent(column, preRecycleView) + " up " + up);
                ItemTag tag = (ItemTag) preRecycleView.getTag();
                if (tag.isFill) {
                    ImageView icon_food = (ImageView) preRecycleView.findViewById(R.id.imgFood);
                    ImageView icon_user = (ImageView) preRecycleView.findViewById(R.id.imgUser);
                    icon_food.setImageBitmap(null);
                    icon_user.setImageBitmap(null);

                    mLoader.recycle(preRecycleView);
                    tag.isFill = false;
                    preRecycleView = null;
                }
            }

            if (reloadView != null) {
                ItemTag tag = (ItemTag) reloadView.getTag();
                if (!tag.isFill) {
                    tag.isFill = true;
                    mLoader.reload(reloadView);
                }
                reloadView = null;
            }
            }
            }
    }

    public void addItems(BeanFood[] beans) {
        isRefreshing = true;
        mHandler.sendEmptyMessage(SHOW_REFRESH_INDICATOR);
        AddItemToContainer(beans);
    }
    
// clean all the view and reload, the speed will be slow.
    public void addItemToContainer(BeanFood[] beans) {
        int count = container.getChildCount();
        for (int i = 0; i < count; i++) {
            ViewGroup column = (ViewGroup) container.getChildAt(i);
            column.removeAllViews();
        }

        mLoader.cleanImageCache();

        AddItemToContainer(beans);
    }

    private void AddItemToContainer(final BeanFood[] beans) {

        final ArrayList<ItemInfo> mapList = new ArrayList<ItemInfo>();

        workHandler.post(new Runnable() {

            private String fid;
            private String imgFoodUrl;
            private String imgUserUrl;
            private String txtDinnerName;
            private String txtEatNum;
            private String txtFavNum;
            private String txtFoodDesc;
            private String txtFoodName;
            private String txtReviewNum;
            private String txtShareType;
            private String txtUserName;

            @Override
            public void run() {

                if (beans == null) {
                    return;
                }
                int len = beans.length;
                for (int i = 0; i < len&&beans[i]!=null; i++) {
                	
                    imgFoodUrl = beans[i].getBiggest_pic();
                    imgUserUrl = beans[i].getuHead();
                    txtDinnerName = beans[i].getrName();
                    txtEatNum = beans[i].getWant_num();
                    txtFavNum = beans[i].getLike_num();
                    txtFoodDesc = beans[i].getIntro();
                    txtFoodName = beans[i].getName();
                    txtReviewNum = beans[i].getComment_num();
                    txtShareType = beans[i].getSource_name();
                    txtUserName = beans[i].getuName();
                    fid = beans[i].getFid();

                    Bitmap imgFood = mLoader.loadImage(imgFoodUrl);
                    Bitmap imgUser = mLoader.loadImage(imgUserUrl);

                    if (imgFood != null && imgUser != null) {
                        ItemInfo itemInfo = new ItemInfo();
                        itemInfo.bitmapFood = imgFood;
                        itemInfo.bitmapUser = imgUser;
                        itemInfo.foodUrl = imgFoodUrl;
                        itemInfo.userUrl = imgUserUrl;
                        itemInfo.txtDinnerName = txtDinnerName;
                        itemInfo.txtEatNum = txtEatNum;
                        itemInfo.txtFavNum = txtFavNum;
                        itemInfo.txtFoodDesc = txtFoodDesc;
                        itemInfo.txtFoodName = txtFoodName;
                        itemInfo.txtReviewNum = txtReviewNum;
                        itemInfo.txtShareType = txtShareType;
                        itemInfo.txtUserName = txtUserName;
                        itemInfo.fid = fid;
                        mapList.add(itemInfo);
                    }

                }

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        synchronized (sLock) {

                            for (int i = 0; i < mapList.size(); i++) {
                                final ItemInfo info = mapList.get(i);
                                Bitmap bitmapFood = info.bitmapFood;
                                Bitmap bitmapUser = info.bitmapUser;
                                String foodUrl = info.foodUrl;
                                String userUrl = info.userUrl;
                                final int columnIndexOfMinHeight = GetMinValue(column_height);
                                 System.out.println("begin columnIndexOfMinHeight "
                                 + columnIndexOfMinHeight + " i = " + 
                                 " " + Thread.currentThread().toString());
                                LinearLayout column = (LinearLayout) container
                                        .getChildAt(columnIndexOfMinHeight);
                                View water_item = mInflater.inflate(R.layout.water_item_view,
                                        null, false);

                                water_item.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        Intent intent = new Intent();
                                        intent.setClass(mContext, ActDish.class);
                                        intent.putExtra(ActDish.KEY_FID, info.fid);
                                        mContext.startActivity(intent);
                                    }
                                });

                                // ItemTag itemTag = new ItemTag();
                                // itemTag.setUri(uri);
                                ImageView icon_food = (ImageView) water_item
                                        .findViewById(R.id.imgFood);
                                ImageView icon_user = (ImageView) water_item
                                        .findViewById(R.id.imgUser);
                                TextView foodName = (TextView) water_item
                                        .findViewById(R.id.txtFoodName);
                                TextView foodDesc = (TextView) water_item
                                        .findViewById(R.id.txtFoodDesc);
                                TextView dinnerName = (TextView) water_item
                                        .findViewById(R.id.txtDinnerName);
                                TextView favNum = (TextView) water_item
                                        .findViewById(R.id.txtFavNum);
                                TextView eatNum = (TextView) water_item
                                        .findViewById(R.id.txtEatNum);
                                TextView reviewNum = (TextView) water_item
                                        .findViewById(R.id.txtReviewNum);
                                TextView userName = (TextView) water_item
                                        .findViewById(R.id.txtUserName);
                                TextView shareType = (TextView) water_item
                                        .findViewById(R.id.txtShareType);

                                if (info.txtFoodName != null && !"".equals(info.txtFoodName)) {
                                    foodName.setText(info.txtFoodName);
                                }
                                if (info.txtFoodDesc != null && !"".equals(info.txtFoodDesc)) {
                                    foodDesc.setText(info.txtFoodDesc);
                                }
                                if (info.txtDinnerName != null && !"".equals(info.txtDinnerName)) {
                                    dinnerName.setText(info.txtDinnerName);
                                }
                                if (info.txtFavNum != null && !"".equals(info.txtFavNum)) {
                                    favNum.setText(info.txtFavNum);
                                }
                                if (info.txtEatNum != null && !"".equals(info.txtEatNum)) {
                                    eatNum.setText(info.txtEatNum);
                                }
                                if (info.txtReviewNum != null && !"".equals(info.txtReviewNum)) {
                                    reviewNum.setText(info.txtReviewNum);
                                }
                                if (info.txtUserName != null && !"".equals(info.txtUserName)) {
                                    userName.setText(info.txtUserName);
                                }
                                if (info.txtShareType != null && !"".equals(info.txtShareType)) {
                                    shareType.setText("通过" + info.txtShareType + "分享");
                                }
                                
                                int foodWidth = bitmapFood.getWidth();
                                // int userWidth = bitmapUser.getWidth();
                                int foodheight = bitmapFood.getHeight();
                                // int userheight = bitmapUser.getHeight();
                               
                                android.view.ViewGroup.LayoutParams foodLp = icon_food
                                        .getLayoutParams();
                                // android.view.ViewGroup.LayoutParams userLp =
                                // icon_user
                                // .getLayoutParams();

                                int layoutHeight = (foodheight * columnWidth) / foodWidth;
                                foodLp.height = layoutHeight;
                                foodLp.width = columnWidth;

                                // userLp.height = userheight;
                                // userLp.width = userWidth;

                                icon_food.setImageBitmap(bitmapFood);
                                icon_user.setImageBitmap(bitmapUser);

                                ItemTag tag = new ItemTag();
                                tag.foodUrl = foodUrl;
                                tag.userUrl = userUrl;
                                tag.isFill = true;
                                water_item.setTag(tag);

                                column.addView(water_item);

                                float textSize = foodDesc.getTextSize();

                                column_height[columnIndexOfMinHeight] += layoutHeight
                                        + (float) info.txtFoodDesc.length()
                                        * textSize / columnWidth;
                                 System.out.println("end columnIndexOfMinHeight "
                                 + columnIndexOfMinHeight + " i = " + 
                                 " " + Thread.currentThread().toString());
                            }

                        }
                        isRefreshing = false;
                        System.gc();
                        mHandler.sendEmptyMessage(HIDE_REFRESH_INDICATOR);
                        mHandler.sendEmptyMessage(HIDE_TOP);
                    }
                });

            }
        });
    }

    public class ItemTag {
        String foodUrl;
        String userUrl;
        boolean isFill;
    }

    private class ItemInfo {
        Bitmap bitmapFood;
        Bitmap bitmapUser;
        String foodUrl;
        String userUrl;
        String txtDinnerName;
        String txtEatNum;
        String txtFavNum;
        String txtFoodDesc;
        String txtFoodName;
        String txtReviewNum;
        String txtShareType;
        String txtUserName;
        String fid;
    }

    private int GetMinValue(int[] columnHeight) {
        int count = columnHeight.length;
        int m = 0;
        for (int i = 0; i < count; i++) {
            if (columnHeight[i] < columnHeight[m]) {
                m = i;
            }
        }
        return m;
    }

    public interface OnScrollListener {
        /**
         * @param itemCount 已经添加的item数量
         * @return 服务器端是否还有数据库可以加载 如果服务器已经没数据库了 返回false 如果服务器至少还有一条数据没有加载过
         *         就返回ture
         */
        public boolean OnBottom(int itemCount);

        /**
         * @return 是否需要刷新 刷新就返回ture 就会显示header
         */
        public boolean OnTop();
        
        public void onScroll();
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mOnScrollListener = listener;
    }
    
	

}
