package com.meishimeike.Template;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.mapabc.mapapi.map.MapView;

/**
 * @author :LiuQiang
 * @version ：2012-7-9 class desc
 */
public class ABCMapView extends MapView {

	public ABCMapView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// Disallow ScrollView to intercept touch events.
			this.getParent().requestDisallowInterceptTouchEvent(true);
			break;

		case MotionEvent.ACTION_UP:
			// Allow ScrollView to intercept touch events.
			this.getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}

		// Handle MapView's touch events.
		super.onTouchEvent(ev);
		return true;
	}

}
