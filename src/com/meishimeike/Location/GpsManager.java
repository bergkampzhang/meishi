package com.meishimeike.Location;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GpsManager {

	private static boolean GPS_Enabled = false;
	private static boolean NET_Enabled = false;
	private static StationManager stateManage = null;

	// 检查Gps是否开启
	public static boolean isGpsOpen(Context context) {
		boolean b = false;
		LocationManager locationManager = getLocationManager(context);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			b = true;
		}
		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			b = true;
		}
		return b;
	}
	
	// 检查Gps是否开启
	public static boolean isOnlyGpsOpen(Context context) {
		boolean b = false;
		LocationManager locationManager = getLocationManager(context);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			b = true;
		}
		return b;
	}

	// 获取一个LocationManager
	private static LocationManager getLocationManager(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		try {
			GPS_Enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {

		}
		try {
			NET_Enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {

		}
		return lm;
	}

	// 开启系GPS配置面板
	public static void OpenGpsConfig(Context context) {
		Intent callGPSSettingIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		context.startActivity(callGPSSettingIntent);
	}

	// 获取最近位置，五分钟内的loc有效
	public static Location getGPSLoc(Context context) {
		Location loctmp = null;
		stateManage = new StationManager();
		long misTime;
		if (loctmp == null && NET_Enabled) {
			loctmp = getLocationManager(context).getLastKnownLocation(
					LocationManager.NETWORK_PROVIDER);
		}
		if (GPS_Enabled) {
			if (isGpsOpen(context)) {
				loctmp = getLocationManager(context).getLastKnownLocation(
						LocationManager.GPS_PROVIDER);
			} else {
				loctmp = null;
			}

		}
		if (loctmp != null && loctmp.getLatitude() != 0
				&& loctmp.getLongitude() != 0) {
			misTime = new Date().getTime() - loctmp.getTime();
			if ((misTime / (1000 * 60)) > 5) {
				loctmp = null;
			}
		}
		if (loctmp == null) {
			loctmp = stateManage.getMyLocation(context);
		}
		return loctmp;
	}

	// 获取基站坐标
	public static Location getStationLoc(Context context) {
		Location loctmp = null;
		stateManage = new StationManager();
		loctmp = stateManage.getMyLocation(context);
		return loctmp;
	}

	// Gps位置服务接口
	public interface GpsListener {
		// Gps位置发生变化
		public void onLocationChanged(Location location);

		// Gps设备开启
		public void onGpsDeviceOpen();

		// Gps设备关闭
		public void onGpsDeviceClose();

		// Gps设备状态转化
		public void onGpsDeviceStateChanged(int status);
	}

	private GpsListener mGpsListener = null;

	public GpsListener getIGpsManager() {
		return mGpsListener;
	}

	public void setGpsListener(GpsListener mIGpsManager) {
		this.mGpsListener = mIGpsManager;
	}

	// 间隔多少秒进行捕获
	private final int MILLISECOND = 1000;

	// Gps误差（米）进行捕获
	private final int METER = 0;

	public GpsManager(Context context) {
		LocationManager locationManager = getLocationManager(context);

		if (GPS_Enabled) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, MILLISECOND, METER,
					locationListener);
		}

		if (NET_Enabled) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, MILLISECOND, METER,
					locationListener);
		}
	}

	// GpsListenter接口
	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
			// log it when the location changes
			if (location != null) {
				if (GpsManager.this.mGpsListener != null) {
					GpsManager.this.mGpsListener.onLocationChanged(location);
				}
				// Log.i("SuperMap", "Location changed : Lat: "
				// + location.getLatitude() + " Lng: "
				// + location.getLongitude());
			}
		}

		public void onProviderDisabled(String provider) {
			// Provider被disable时触发此函数，比如GPS被关闭
			if (GpsManager.this.mGpsListener != null) {
				GpsManager.this.mGpsListener.onGpsDeviceClose();
			}
		}

		public void onProviderEnabled(String provider) {
			// Provider被enable时触发此函数，比如GPS被打开
			if (GpsManager.this.mGpsListener != null) {
				GpsManager.this.mGpsListener.onGpsDeviceOpen();
			}
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
			if (GpsManager.this.mGpsListener != null) {
				GpsManager.this.mGpsListener.onGpsDeviceStateChanged(status);
			}
		}
	};
}
