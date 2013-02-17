package com.meishimeike;

import android.app.Application;
import android.graphics.Bitmap;

import com.meishimeike.Bean.BeanLoc;

public class AppMsmk extends Application {
	private static Bitmap picBitmap = null;
	private static BeanLoc beanLoc = null;

	public static BeanLoc getBeanLoc() {
		return beanLoc;
	}

	public static void setBeanLoc(BeanLoc beanLoc) {
		AppMsmk.beanLoc = beanLoc;
	}

	public Bitmap getPicBitmap() {
		return picBitmap;
	}

	public void setPicBitmap(Bitmap picBitmap) {
		AppMsmk.picBitmap = picBitmap;
	}

}
