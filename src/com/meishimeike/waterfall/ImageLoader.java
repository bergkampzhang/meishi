package com.meishimeike.waterfall;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageLoader {

	// 图片软引用
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	public List<Bitmap> loadBitmaps(final String[] paths) {
		final ArrayList<Bitmap> maps = new ArrayList<Bitmap>();

		int len = paths.length;
		for (int i = 0; i < len; i++) {
			Bitmap bitmap = loadImage(paths[i]);
			if (null != bitmap) {
				maps.add(bitmap);
			}
		}

		return maps;
	}

	public Bitmap loadImage(String iamgeUrl) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(iamgeUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {
				throw new Exception("图片文件不存在或路径错误，错误代码：" + responseCode);
			}
			
			InputStream in = conn.getInputStream();
			
			//FileInputStream in = new FileInputStream(iamgeUrl);

			BufferedInputStream bis = new BufferedInputStream(in);
			bitmap = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
