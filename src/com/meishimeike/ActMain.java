
package com.meishimeike;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.mapabc.mapapi.core.MapAbcException;
import com.mapabc.mapapi.geocoder.Geocoder;
import com.meishimeike.Adapter.MainGalleryAdapter;
import com.meishimeike.Bean.BeanCity;
import com.meishimeike.Bean.BeanFood;
import com.meishimeike.Bean.BeanLoc;
import com.meishimeike.Bll.BllGet;
import com.meishimeike.Location.GpsManager;
import com.meishimeike.MainTabWidget.MainBottomBar;
import com.meishimeike.MainTabWidget.MainNavigateBar;
import com.meishimeike.MainTabWidget.OnMainNavLeftClickListener;
import com.meishimeike.MainTabWidget.OnMainNavRightClickListener;
import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.Utils.Commons;
import com.meishimeike.Utils.LocalVariable;
import com.meishimeike.View.MainAttentionNoLoginView;
import com.meishimeike.waterfall.WaterfallView;
import com.meishimeike.waterfall.WaterfallView.OnScrollListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

public class ActMain extends ActivityTemplate implements OnClickListener,
        GpsManager.GpsListener, OnScrollListener {
    private MainNavigateBar navBar;
    private Button btnNew, btnRim, btnPopularity, btnAttention;
    private BllGet bll = null;
    private ArrayList<BeanFood> arrList = null;
    private LayoutParams lp = null;
    private AlertDialog dlgProgress = null;
    private int type = 1;
    private Thread loadThread = null;
    private double lng = 0.0, lat = 0.0;
    private long range = 1000;
    private BeanLoc beanLoc = null;
    private LocalVariable lv = null;
    private Commons coms = null;
    private LinearLayout llLoad = null;
    private GpsManager mGpsManager = null;
    private static Location locNow = null;
    private MainAttentionNoLoginView attentionNoLoginView = null;
    private Gallery gallery = null;
    
    private MainGalleryAdapter adapter = null;
    private WaterfallView waterfall = null;
    private LinearLayout llGallery = null;

    private int arrPart = 0;
    private boolean isReLoad = false;
	
    private static int start =0;
    //http://www.apkbus.com/android-54747-1-1.html
    public static final int arrynumber = 15; // all the list are set as this number.
    // use the container to add remove bar
    private LinearLayout topContainer;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.onError(this);
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        setContentView(R.layout.act_main);
        // 加载GpsManager
        mGpsManager = new GpsManager(this);
        mGpsManager.setGpsListener(this);
        lv = new LocalVariable(this);
        coms = new Commons();
        bll = new BllGet(this);
        lp = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        LayoutInflater inflater = LayoutInflater.from((Context)this); 
        View layout = inflater.inflate(R.layout.act_main, null);
        /*AttributeSet att = null;
        Resources res = getResources();
        XmlPullParser parser = res.getXml(R.layout.act_main);
        AttributeSet attributes = Xml.asAttributeSet(parser);
        MainBottomBar btmBar = new MainBottomBar((Context) this, attributes);
        */
        /*LinearLayout topContainer = new LinearLayout((Context)this);
        topContainer.removeView(navBar);
        topContainer.addView(btmBar,lp);
        topContainer.invalidate();*/
        topContainer = (LinearLayout)findViewById(R.id.bottom);
        //topContainer.removeView(navBar);
        //topContainer.addView(btmBar,lp);
        //topContainer.invalidate();
        animation_view();
        
        //MainBottomBar btmBar =(MainBottomBar)findViewById(R.id.bottom);
        //animation_view();
        init_ctrl();
        loadThread = new Thread(loadRunnable);
        loadThread.start();
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //main = getLayoutInflater().from(this).inflate(R.layout.act_main, null);  
        //setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  
        //animation_view(navBar);
      
        
    }
	

	   private void showHead(View head) {
           head.startAnimation(AnimationUtils.loadAnimation(this, R.anim.umeng_xp_push_down_out));
           
   }
    private void init_ctrl() {	
        llLoad = (LinearLayout) findViewById(R.id.llLoad);
        navBar = (MainNavigateBar) findViewById(R.id.NAV);
        navBar.setOnMainNavLeftClickListener(NavLeftClickListener);
        navBar.setOnMainNavRightClickListener(NavRightClickListener);
        // add the bttom bar by dynamic
        
        
        btnNew = (Button) findViewById(R.id.BtnNew);
        btnNew.setOnClickListener(this);
        btnRim = (Button) findViewById(R.id.BtnRim);
        btnRim.setOnClickListener(this);
        btnPopularity = (Button) findViewById(R.id.BtnPopularity);
        btnPopularity.setOnClickListener(this);
        btnAttention = (Button) findViewById(R.id.BtnAttention);
        btnAttention.setOnClickListener(this);

        /*gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setOnItemClickListener(itemClickListener);
        llGallery = (LinearLayout) findViewById(R.id.llGallery);
        attentionNoLoginView = new MainAttentionNoLoginView(this);
        llGallery.addView(attentionNoLoginView, lp);
        attentionNoLoginView.setVisibility(View.GONE);
        */
        attentionNoLoginView = new MainAttentionNoLoginView(this);
        waterfall = (WaterfallView) findViewById(R.id.waterfall);
        View footer = View.inflate((Context) this, R.layout.view_main_footer,null);
        View header = View.inflate((Context) this, R.layout.view_main_header, null);
        waterfall.setFooter(header);
        waterfall.setHeader(footer);
        
        
    }
    private void add_view(){
    	MainBottomBar btmBar = new MainBottomBar((Context) this);
    	
    }
    private void remove_view(View theView){
    	WindowManager mWm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
    	try {
    		mWm.removeView(theView);
    	}catch (Exception e){
    		
    	}
    }
    @SuppressLint("NewApi")
	private void animation_view(){
    Animation ani = AnimationUtils.loadAnimation(this, R.anim.umeng_xp_fade_out);
    
    findViewById(R.id.bottom).startAnimation(ani);
    }
    /**
     * init main item view
     * 
     * @author :LiuQiang
     * @version ：2012-5-21
     * @param _type 1-new,2-hot,3-rim
     */
    
    //1.com.meishimeike.waterfall包下文件为首页瀑布流相关代码，调用方式可查看ActMain.java中注释的代码。
    //2.查看瀑布流效果可将ActMain.java中的gallery、adapter相关代码注释，再将其它注释代码取消注释，然后将act_main.xml中的Gallery控件改为WaterfallView即可。
    
    private void init_data() {
        if (type == 3) {
            if (beanLoc == null) {
                beanLoc = AppMsmk.getBeanLoc();
            }
            if (beanLoc != null) {
                lng = beanLoc.getLng();
                lat = beanLoc.getLat();
                arrList = bll.getFoodListByRim(lng, lat, range, arrynumber);
                
            }
        } else if (type < 0) {
            if (lv.getUid().equals("")) {
                arrList = null;
            } else {
                arrList = bll.getMainFoodByAttention();
            }
        } else {
            arrList = bll.getMainFoodList(type, start, arrynumber);
        }
        
    }

    private OnItemClickListener itemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos,
                long id) {
            String strFid = arrList.get(pos).getFid();
            Intent intent = new Intent();
            intent.setClass(ActMain.this, ActDish.class);
            intent.putExtra(ActDish.KEY_FID, strFid);
            startActivity(intent);

        }
    };

    private void proLoadData() {
        llLoad.setVisibility(View.VISIBLE);
        arrList = null;
    }

    private void setCtrlData() {
        llLoad.setVisibility(View.GONE);
        if (type == 3) {
            if (beanLoc == null) {
                Toast.makeText(this, "尚未成功定位，请稍候再试....", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }
        if (type < 0) {
            if (lv.getUid().equals("")) {
                Toast.makeText(this, "你还没有登录，不能查看关注好友动态哦", Toast.LENGTH_SHORT).show();
                attentionNoLoginView.setVisibility(View.VISIBLE);
                //gallery.setVisibility(View.GONE);
                waterfall.setVisibility(View.GONE);
                return;
            }
            if (arrList == null || arrList.size() < 1) {
                Toast.makeText(this, "没有发现关注好友动态哦，去添加更多好友吧", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }
        if (arrList == null || arrList.size() < 1) {
            String str = "";
            if (type == 1 || type == 2 || type == -1) {
                str = "未能获美食信息,请稍候再试....";
            } else {
                str = "未能在周边发现美食,请稍候再试....";
            }
            Toast.makeText(ActMain.this, str, Toast.LENGTH_LONG).show();
            attentionNoLoginView.setVisibility(View.GONE);
            //gallery.setVisibility(View.GONE);
            waterfall.setVisibility(View.GONE);
            return;
        }
        if (arrList == null) {
            attentionNoLoginView.setVisibility(View.GONE);
            //gallery.setVisibility(View.GONE);
            waterfall.setVisibility(View.GONE);
            return;
        }
        //adapter = new MainGalleryAdapter(this, arrList);
        //gallery.setAdapter(adapter);
        //gallery.setVisibility(View.VISIBLE);

        //waterfall加载数据
        BeanFood[] beans = getArrSplit();
        waterfall.addItemToContainer(beans);
        waterfall.setVisibility(View.VISIBLE);
        waterfall.setOnScrollListener(this);
        /* it is not suitable to do it here
        waterfall.setOnClickListener(new OnScrollListener() {

        public boolean OnBottom(int itemCount) {
        BeanFood[] beans = getArrSplit();
        if (beans == null) {
            return false;
        } else {
        	waterfall.addItems(getArrSplit());
            return true;
        }
        	return true;
        }

        public boolean OnTop() {
        isReLoad = true;
        loadThread = new Thread(loadRunnable);
        loadThread.start();
        arrPart = 0;
        return true;
        }
        });*/


        //attentionNoLoginView.setVisibility(View.GONE);
    }

    private BeanFood[] getArrSplit() {
    BeanFood[] bean = null;
    if (arrList == null || arrList.size() < 1 || arrList.size() < arrynumber) {
       return null;
    }
    bean = new BeanFood[arrynumber];
    // need check the bounds here  zhang peng
    for (int i = 0; i < arrynumber; i++) {
        //bean[i] = arrList.get(i + arrPart);
    	bean[i] = arrList.get(i);//out of bounds
    }
       //arrPart += 1; // seems we need not this parameter
       return bean;
    }

    @Override
    public void onClick(View v) {
    	  showHead(navBar);
          remove_view(navBar);
          LinearLayout topContainer = new LinearLayout((Context)this);
          topContainer.removeViewAt(1);
        System.gc();
        changeImage(v.getId());
        switch (v.getId()) {
            case R.id.BtnNew:
                type = 1;
                break;
            case R.id.BtnRim:
                type = 3;
                break;
            case R.id.BtnPopularity:
                type = 2;
                break;
            case R.id.BtnAttention:
                type = -1;
                break;
        }
        proLoadData();
        loadThread = new Thread(loadRunnable);
        loadThread.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            coms.ExitSys(ActMain.this);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private OnMainNavLeftClickListener NavLeftClickListener = new OnMainNavLeftClickListener() {
        @Override
        public void onClickListenr() {
            Intent intent = new Intent();
            intent.setClass(ActMain.this, ActSetting.class);
            startActivity(intent);
        }
    };

    private OnMainNavRightClickListener NavRightClickListener = new OnMainNavRightClickListener() {
        @Override
        public void onClickListenr() {
            Intent intent = new Intent();
            if (!lv.getIsLogin()) {
                Toast.makeText(ActMain.this, "亲~你还没登录呢", Toast.LENGTH_SHORT)
                        .show();
            } else {
                intent.setClass(ActMain.this, ActSearchUser.class);
                startActivity(intent);
            }
        }
    };

    private void changeImage(int resId) {
        switch (resId) {
            case R.id.BtnNew:
                btnNew.setBackgroundResource(R.drawable.btn_main_new_focus);
                btnRim.setBackgroundResource(R.drawable.btn_main_rim);
                btnPopularity.setBackgroundResource(R.drawable.btn_main_popularity);
                btnAttention.setBackgroundResource(R.drawable.btn_main_attention);
                break;
            case R.id.BtnRim:
                btnNew.setBackgroundResource(R.drawable.btn_main_new);
                btnRim.setBackgroundResource(R.drawable.btn_main_rim_focus);
                btnPopularity.setBackgroundResource(R.drawable.btn_main_popularity);
                btnAttention.setBackgroundResource(R.drawable.btn_main_attention);
                break;
            case R.id.BtnPopularity:
                btnNew.setBackgroundResource(R.drawable.btn_main_new);
                btnRim.setBackgroundResource(R.drawable.btn_main_rim);
                btnPopularity
                        .setBackgroundResource(R.drawable.btn_main_popularity_focus);
                btnAttention.setBackgroundResource(R.drawable.btn_main_attention);
                break;
            case R.id.BtnAttention:
                btnNew.setBackgroundResource(R.drawable.btn_main_new);
                btnRim.setBackgroundResource(R.drawable.btn_main_rim);
                btnPopularity.setBackgroundResource(R.drawable.btn_main_popularity);
                btnAttention
                        .setBackgroundResource(R.drawable.btn_main_attention_focus);
                break;
        }
    }
    private void OnClick(){
    	//remove_view(waterfall);
        //waterfall.setVisibility(View.GONE);
      
        //LinearLayout ll = (LinearLayout)findViewById(R.layout.act_main);
        //ll.removeViewInLayout(navBar);
    }
    private Runnable loadRunnable = new Runnable() {

        @Override
        public void run() {
            init_data();
            handler.sendEmptyMessage(1);
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setCtrlData();
                    if (dlgProgress != null && dlgProgress.isShowing()) {
                        dlgProgress.dismiss();
                    }
                    break;
                case 2:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (lv.getIsNewPublish()) {
            loadThread = new Thread(loadRunnable);
            loadThread.start();
            lv.setIsNewPublish(false);
        }
        if (lv.getIsNewLogin()) {
            if (type == -1) {
                loadThread = new Thread(loadRunnable);
                loadThread.start();
            }
            lv.setIsNewLogin(false);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onGpsDeviceClose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsDeviceOpen() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsDeviceStateChanged(int status) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            locNow = location;
            beanLoc = new BeanLoc();
            beanLoc.setLng(locNow.getLongitude());
            beanLoc.setLat(locNow.getLatitude());
            beanLoc.setLoc(locNow);
            Geocoder gc = new Geocoder(this, getResources().getString(
                    R.string.maps_api_key));
            try {
                double lat = locNow.getLatitude();
                double lng = locNow.getLongitude();
                List<Address> fromLocation = gc.getFromLocation(lat, lng, 2);
                if (fromLocation.size() > 0) {
                    Address address = fromLocation.get(0);
                    String name = address.getAdminArea();
                    ArrayList<BeanCity> quanZiCityList = bll
                            .getQuanZiCityList();
                    for (BeanCity bean : quanZiCityList) {
                        if (name.contains(bean.getName())) {
                            lv.setQuanZiCityId(bean.getId());
                        }
                    }
                }
            } catch (MapAbcException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            AppMsmk.setBeanLoc(beanLoc);
        }

    }

	@Override
	public boolean OnBottom(int itemCount) {
		// TODO Auto-generated method stub
		// down load thread
		// update the scroll view 
		// recycle the picture
		
		arrPart =0;
		
		start ++;
		arrList = bll.getMainFoodList(type, start*5, arrynumber);
		BeanFood[] beans = getArrSplit();
		// exchange the beans
		if (beans == null) {
            return false;
        } else {
        	/*waterfall.addItemToContainer(beans);      
        	loadThread = new Thread(loadRunnable);
            loadThread.start();*/
        	waterfall.addItems(beans);
            return true;
        }
        		
		
		
		
	}

	@Override
	public boolean OnTop() {
		// TODO Auto-generated method stub
		
		System.out.println(" on Top");
		if(start>0)
		start --;
		arrList = bll.getMainFoodList(type, start*5, arrynumber);
		BeanFood[] beans = getArrSplit();
		// exchange the beans
		if (beans == null) {
            return false;
        } else {
        	/*waterfall.addItemToContainer(beans);      
        	loadThread = new Thread(loadRunnable);
            loadThread.start();*/
        	waterfall.addItems(beans);
            return true;
        }
		
	}

	@Override
	public void onScroll() {
		// TODO Auto-generated method stub
		animation_view();
		
	}

}
