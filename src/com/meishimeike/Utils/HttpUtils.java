package com.meishimeike.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpUtils {
	static Commons coms = new Commons();

	public static Bitmap loadImageFromUrl(String url) {
		Bitmap b = null;
		InputStream i = null;
		try {
			i = getUrlInputStream(url);
			if (i == null) {
				return null;
			}

			int be = 1;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			b = BitmapFactory.decodeStream(i, null, options);
			options.inJustDecodeBounds = false;
			if (options.outWidth > options.outHeight) {
				if (options.outWidth > (float) 800) {
					be = (int) (options.outWidth / (float) 800);
				}
			} else {
				if (options.outHeight > (float) 800) {
					be = (int) (options.outHeight / (float) 800);
				}
			}
			if (be <= 0 || be == 1) {
				be = 1;
			} else {
				if (be < 2) {
					be = 2;
				} else {
					be = be * 2 - 2;
				}
				if (be % 2 != 0) {
					be = be + 1;
				}
			}

			i = getUrlInputStream(url);
			BitmapFactory.Options op_new = new BitmapFactory.Options();
			op_new.inSampleSize = be;
			op_new.inPurgeable = true;
			op_new.inInputShareable = true;
			b = BitmapFactory.decodeStream(i, null, op_new);
			if (b == null) {
				return null;
			}
			b = coms.imgCrop(b);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (i != null) {
					i.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			b = null;
		}
	}

	public static InputStream getUrlInputStream(String url) {
		InputStream i = null;
		try {
			URL m = new URL(url);
			HttpURLConnection http = (HttpURLConnection) m.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "charset=utf-8");
			http.connect();
			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				i = http.getInputStream();
			}
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {

		}
	}

	/**
	 * 检验网络连接
	 * 
	 * @return
	 */
	public static boolean is_Intent(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			// 当前网络不可用
			return false;
		}
		return true;

	}
}
