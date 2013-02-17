package com.meishimeike.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import weibo4andriod.Weibo;
import weibo4andriod.androidexamples.OAuthConstant;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.meishimeike.Template.ActivityTemplate;
import com.meishimeike.weibo.sina.AccessInfoHelper;

public class Commons {

	public Commons() {

	}

	public byte[] DrawableToByte(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		bitmap.recycle();
		bitmap = null;

		return baos.toByteArray();
	}

	public Bitmap loadPicOpt(String _path, Bitmap b) {
		b = null;
		try {
			int be = 1;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			b = BitmapFactory.decodeFile(_path, options);

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
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;
			b = null;
			b = BitmapFactory.decodeFile(_path, options);
			if (b != null) {
				b = imgCrop(b);
			}
			return b;
		} catch (Exception e) {
			return null;
		} catch (OutOfMemoryError oe) {
			System.gc();
			return null;
		} finally {
			b = null;
		}
	}

	public Bitmap imgCrop(Bitmap _b) {
		int bw = 0, bh = 0;
		int w = 0, h = 0, x = 0, y = 0;

		try {
			bw = _b.getWidth();
			bh = _b.getHeight();

			if (bw < 200 || bh < 200) {
				return _b;
			}

			if (bw > bh) {
				// 横图
				if (bw > 1.5 * bh) {
					w = (int) (1.5 * bh);
					h = bh;
					x = (int) ((bw - w) / 2);
					y = 0;
				} else {
					w = bw;
					h = (int) (bw / 1.5);
					x = 0;
					y = 0;
				}
			} else {
				// 竖图
				w = bw;
				h = (int) (bw / 1.5);
				x = 0;
				y = (int) ((bh - h) / 2);
			}
			_b = Bitmap.createBitmap(_b, x, y, w, h, null, false);
			return _b;
		} catch (Exception e) {
			e.printStackTrace();
			_b = null;
			return null;
		} catch (OutOfMemoryError oe) {
			System.gc();
			return null;
		} finally {
			_b = null;
		}
	}

	public void initSendCfg(Context mContext) {
		LocalVariable lv = new LocalVariable(mContext);
		// if (lv.getTempPicType1().equals("camera")) {
		// delTempPic(lv.getTempPic1());
		// }
		// if (lv.getTempPicType2().equals("camera")) {
		// delTempPic(lv.getTempPic2());
		// }
		// if (lv.getTempPicType3().equals("camera")) {
		// delTempPic(lv.getTempPic3());
		// }
		lv.setTempPic1("");
		lv.setTempPicType1("");
		lv.setTempPic2("");
		lv.setTempPicType2("");
		lv.setTempPic3("");
		lv.setTempPicType3("");
		lv.setTempPicNum(0);
		lv.setTempFoodName("");
		lv.setTempDinnerId("");
		lv.setTempDinnerName("");
		// mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
		// .parse("file://" + Environment.getExternalStorageDirectory())));
	}

	public void initSendCfgButDinner(Context mContext) {
		LocalVariable lv = new LocalVariable(mContext);
		// if (lv.getTempPicType1().equals("camera")) {
		// delTempPic(lv.getTempPic1());
		// }
		// if (lv.getTempPicType2().equals("camera")) {
		// delTempPic(lv.getTempPic2());
		// }
		// if (lv.getTempPicType3().equals("camera")) {
		// delTempPic(lv.getTempPic3());
		// }
		lv.setTempPic1("");
		lv.setTempPicType1("");
		lv.setTempPic2("");
		lv.setTempPicType2("");
		lv.setTempPic3("");
		lv.setTempPicType3("");
		lv.setTempPicNum(0);
		lv.setTempFoodName("");
		// mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
		// .parse("file://" + Environment.getExternalStorageDirectory())));
	}

	public void delTempPic(String _path) {
		if ("".equals(_path)) {
			return;
		}
		File file = new File(_path);
		if (file != null) {
			file.delete();
		}
	}

	public String getTimeFromStr(String _time) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date dTime = new Date(Long.valueOf(_time) * 1000);
			String strDate = sdf.format(dTime);
			str = strDate;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	public String getDateFromStr(String _time) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dTime = new Date(Long.valueOf(_time) * 1000);
			String strDate = sdf.format(dTime);
			str = strDate;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	public String getTimeDiff(String _time) {
		String str = "";
		Long timeDiff = 0l, lMinute = 0l;
		Long lHour = 0l, lDay = 0l, lWeek = 0l;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		try {
			Date dTime = new Date(Long.valueOf(_time) * 1000);
			String strDate = sdf.format(dTime);
			Date dNow = new Date();
			timeDiff = dNow.getTime() - dTime.getTime();
			lMinute = timeDiff / (1000 * 60);
			if (lMinute > 60) {
				lHour = lMinute / 60;
				if (lHour > 24) {
					lDay = lHour / 24;
					if (lDay > 7) {
						lWeek = lDay / 7;
						if (lWeek > 4) {
							str = strDate;
						} else {
							str = lWeek + "周前";
						}
					} else {
						str = lDay + "天前";
					}
				} else {
					str = lHour + "小时前";
				}
			} else {
				str = lMinute + "分钟前";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	public String getDeviceIMEI(Context mContext) {
		String IMEI = "";
		TelephonyManager telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = telephonyManager.getDeviceId();
		return IMEI;
	}

	public String getSysVer() {
		String sysver = "";
		sysver = android.os.Build.VERSION.RELEASE;
		sysver = sysver.substring(0, sysver.lastIndexOf("."));
		return sysver;
	}

	public String getScreenSize(Context mContext) {
		String sSize = "";
		Display display = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay();
		sSize = "h:" + display.getHeight();
		sSize += " w:" + display.getWidth();
		return sSize;
	}

	public int getScreenHeight(Context mContext) {
		int height = 0;
		Display display = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay();
		height = display.getHeight();
		return height;
	}

	public String getPhoneNum(Context mContext) {
		String phoneId = "";
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		phoneId = tm.getLine1Number();
		return phoneId;
	}

	/**
	 * 保存String系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @param SPValue
	 */
	public void saveStrSP(Context context, String SPKey, String SPValue) {
		SharedPreferences sp = context.getSharedPreferences(LocalVariable.NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(SPKey, SPValue);
		editor.commit();
	}

	/**
	 * 读取String系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @return
	 */
	public String getStrSP(Context context, String SPKey) {
		SharedPreferences sp = context.getSharedPreferences(LocalVariable.NAME,
				Context.MODE_PRIVATE);
		return sp.getString(SPKey, "");
	}

	/**
	 * 保存int系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @param SPValue
	 */
	public void saveIntSP(Context context, String SPKey, int SPValue) {
		SharedPreferences sp = context.getSharedPreferences(LocalVariable.NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(SPKey, SPValue);
		editor.commit();
	}

	/**
	 * 读取String系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @return
	 */
	public int getIntSP(Context context, String SPKey) {
		SharedPreferences sp = context.getSharedPreferences(LocalVariable.NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(SPKey, 0);
	}

	/**
	 * 保存bool系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @param SPValue
	 */
	public void saveBoolSP(Context context, String SPKey, Boolean SPValue) {
		SharedPreferences sp = context.getSharedPreferences(LocalVariable.NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(SPKey, SPValue);
		editor.commit();
	}

	/**
	 * 读取bool系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @return true/false
	 */
	public Boolean getBoolSP(Context context, String SPKey) {
		SharedPreferences sp = context.getSharedPreferences(LocalVariable.NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(SPKey, false);
	}

	/**
	 * 判断目录是否存在
	 * 
	 * @return
	 */
	public boolean existFolder(String path) {

		if (existSDcard()) { // 判断手机SD卡是否存在
			if (new File("/sdcard").canRead()) {
				File file = new File("/sdcard/Msmk/");
				if (!file.exists()) {
					file.mkdir();
				}
				file = new File(path);
				if (!file.exists()) {
					file.mkdir();
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public String saveNewPic(String oldPath) {
		String path = "";
		Bitmap bitmap = null;
		int picQuality = 85;
		File file = null;
		if (!existFolder(Constants.MSMK_CACHE_PATH)) {
			return path;
		}
		SimpleDateFormat timeStampFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String filename = timeStampFormat.format(new Date());
		path = Constants.MSMK_CACHE_PATH + filename;
		try {
			bitmap = getPic(oldPath);
			if (bitmap != null) {
				file = new File(path);
				Canvas canvas = new Canvas();
				canvas.drawBitmap(bitmap, 0, 0, null);
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(CompressFormat.JPEG, picQuality, fos);
				fos.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			path = "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			path = "";
		} finally {
			bitmap = null;
		}

		return path;
	}

	private Bitmap getPic(String _path) {
		Bitmap b = null;
		int picSize = 600;
		try {
			int be = 1;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			b = BitmapFactory.decodeFile(_path, options);

			options.inJustDecodeBounds = false;
			if (options.outWidth > options.outHeight) {
				if (options.outWidth > (float) picSize) {
					be = (int) (options.outWidth / (float) picSize);
				}
			} else {
				if (options.outHeight > (float) picSize) {
					be = (int) (options.outHeight / (float) picSize);
				}
			}
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;
			b = null;
			b = BitmapFactory.decodeFile(_path, options);

			return b;
		} catch (Exception e) {
			return null;
		} finally {
			b = null;
		}
	}

	/**
	 * 判断存储卡是否存在
	 * 
	 * @return
	 */
	public boolean existSDcard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 判断网络连接
	 * 
	 * @param act
	 * @return
	 */
	public boolean checkNetState(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			Toast.makeText(context, "未发现可用网络", Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 把输入流转换成字符数组
	 * 
	 * @param inputStream
	 *            输入流
	 * @return 字符数组
	 * @throws Exception
	 */
	public byte[] readStream(InputStream inputStream) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			bout.write(buffer, 0, len);
		}
		bout.close();
		inputStream.close();

		return bout.toByteArray();
	}

	/**
	 * 下载文件
	 * 
	 * @param fileUrl
	 * @param fileName
	 *            =getAppPath+filename
	 * @return 成功0，失败1
	 */
	public boolean downloadFile(String fileUrl, String fileName) {
		boolean result = false;
		try {
			if (!existFolder(Constants.MSMK_CACHE_PATH)) {
				return false;
			}
			if ("".equals(fileName)) {
				fileName = Constants.MSMK_CACHE_PATH + urlToFileName(fileUrl);
			}
			File outputFile = new File(fileName);
			if ("".equals(fileUrl)) {
				return false;
			}
			URL url = new URL(Constants.APP_SERVICE + fileUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "charset=utf-8");
			http.connect();
			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = http.getInputStream();
				FileOutputStream fos = new FileOutputStream(outputFile);
				byte[] bt = new byte[1024];
				int i = 0;
				while ((i = is.read(bt)) > 0) {
					fos.write(bt, 0, i);
				}
				fos.flush();
				fos.close();
				is.close();
			} else {
				result = true;
			}
		} catch (FileNotFoundException e) {
			result = false;
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	public String downTempFile(String fileUrl) {
		String fileName = "";
		try {
			if (!existFolder(Constants.MSMK_CACHE_PATH)) {
				return "";
			}
			if ("".equals(fileName)) {
				fileName = Constants.MSMK_CACHE_PATH + urlToFileName(fileUrl);
			}
			File outputFile = new File(fileName);
			if ("".equals(fileUrl)) {
				return "";
			}
			URL url = new URL(fileUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(40000);
			http.setRequestProperty("content-type", "charset=utf-8");
			http.connect();
			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = http.getInputStream();
				FileOutputStream fos = new FileOutputStream(outputFile);
				byte[] bt = new byte[1024];
				int i = 0;
				while ((i = is.read(bt)) > 0) {
					fos.write(bt, 0, i);
				}
				fos.flush();
				fos.close();
				is.close();
			} else {
				fileName = "";
			}
		} catch (FileNotFoundException e) {
			fileName = "";
		} catch (IOException e) {
			fileName = "";
		}
		return fileName;
	}

	public String checkPicPath(String url) {
		try {
			String fileName = "";
			if (!existFolder(Constants.MSMK_CACHE_PATH)) {
				return "";
			}
			if ("".equals(url)) {
				fileName = Constants.MSMK_CACHE_PATH + urlToFileName(url);
			}
			File file = new File(fileName);
			if (file != null && file.exists()) {
				return fileName;
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String urlToFileName(String url) {
		String tmp = "";
		if ("".equals(url)) {
			return tmp;
		}
		// tmp = url.substring(url.lastIndexOf("/") + 1, url.length() - 1);
		tmp = url.replace("/", "-");
		tmp = tmp.replace(":", "#");
		return tmp;
	}

	public void ExitSys(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("系统提示");
		builder.setMessage("确定退出系统？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent iCloseAc = new Intent();
				iCloseAc.setAction(ActivityTemplate.CLOSE_AC);
				context.sendBroadcast(iCloseAc);
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(startMain);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.show();
	}

	public void ExitSysNoAsk(final Context context) {
		Intent iCloseAc = new Intent();
		iCloseAc.setAction(ActivityTemplate.CLOSE_AC);
		context.sendBroadcast(iCloseAc);
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(startMain);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public void ExitLogin(final Context mContext) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("系统提示");
		builder.setMessage("确定退出登录？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				LocalVariable lv = new LocalVariable(mContext);
				try {
					Weibo weibo = OAuthConstant.getInstance().getWeibo();
					weibo.setToken(lv.getWToken(), lv.getWSecret());

					CookieSyncManager.createInstance(mContext
							.getApplicationContext());
					CookieManager.getInstance().removeAllCookie();

					AccessInfoHelper accessDBHelper = new AccessInfoHelper(
							mContext);
					accessDBHelper.open();
					accessDBHelper.delete();
					accessDBHelper.close();

					if (lv.getIsWLogin()) {
						lv.setIsBinding(false);
					}

					lv.setWUid("");
					lv.setWToken("");
					lv.setWSecret("");
					lv.setIsLogin(false);
					// lv.setLoginName("");
					lv.setName("");
					lv.setUid("");
					lv.setHeadUrl("");
					lv.setIsWLogin(false);

					lv.setIsSinaFriend(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((Activity) mContext).finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.show();
	}

	public void clearWeiboLogin(Context mContext) {
		LocalVariable lv = new LocalVariable(mContext);
		try {
			// Weibo weibo = OAuthConstant.getInstance().getWeibo();
			// weibo.setToken(lv.getWToken(), lv.getWSecret());
			// weibo.endSession();

			CookieSyncManager.createInstance(mContext.getApplicationContext());
			CookieManager.getInstance().removeAllCookie();

			AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
			accessDBHelper.open();
			accessDBHelper.delete();
			accessDBHelper.close();

			lv.setWUid("");
			lv.setWToken("");
			lv.setWSecret("");
			lv.setIsWLogin(false);
			lv.setIsBinding(false);
			lv.setIsSinaFriend(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearBrowserLogin(Context mContext) {
		LocalVariable lv = new LocalVariable(mContext);
		try {
			// Weibo weibo = OAuthConstant.getInstance().getWeibo();
			// weibo.setToken(lv.getWToken(), lv.getWSecret());
			// weibo.endSession();

			CookieSyncManager.createInstance(mContext.getApplicationContext());
			CookieManager.getInstance().removeAllCookie();

			AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
			accessDBHelper.open();
			accessDBHelper.delete();
			accessDBHelper.close();

			lv.setWUid("");
			lv.setWToken("");
			lv.setWSecret("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// --------------------md5-------------------
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	// ------------------------------------------

	public byte[] headImg2Bytes(String _path) {
		Bitmap b = null;
		int be = 1;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			b = BitmapFactory.decodeFile(_path, options);

			options.inJustDecodeBounds = false;
			if (options.outWidth > options.outHeight) {
				if (options.outWidth > (float) 300) {
					be = (int) (options.outWidth / (float) 300);
				}
			} else {
				if (options.outHeight > (float) 300) {
					be = (int) (options.outHeight / (float) 300);
				}
			}
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;

			b = BitmapFactory.decodeFile(_path, options);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			b.compress(Bitmap.CompressFormat.JPEG, 70, baos);
			return baos.toByteArray();
		} catch (Exception e) {
			return null;
		} finally {
			b = null;
		}
	}

}
