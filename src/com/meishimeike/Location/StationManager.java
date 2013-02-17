package com.meishimeike.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.util.Log;

public class StationManager {
	// 获取基站
	public Location getMyLocation(Context mContext) {
		WifiManager tmpwm = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		TelephonyManager tmpManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		WifiInfoManager wim = new WifiInfoManager();
		CellIDInfoManager cim = new CellIDInfoManager();
		Location loc;
		loc = callGSMGear(wim.getWifiInfo(tmpwm), cim
				.getCellIDInfo(tmpManager));
		if (loc == null) {
			loc = callCDMAGear(tmpManager);
		}
		return loc;
	}

	// GSM Location
	private Location callGSMGear(ArrayList<WifiInfo> wifi,
			ArrayList<CellIDInfo> cellID) {

		if (cellID == null)
			return null;

		DefaultHttpClient client = new DefaultHttpClient();

		HttpPost post = new HttpPost("http://www.google.com/loc/json");
		JSONObject holder = new JSONObject();

		try {
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("home_mobile_country_code",
					cellID.get(0).mobileCountryCode);
			holder.put("home_mobile_network_code",
					cellID.get(0).mobileNetworkCode);
			holder.put("radio_type", cellID.get(0).radioType);
			holder.put("request_address", true);
			if ("460".equals(cellID.get(0).mobileCountryCode))
				holder.put("address_language", "zh_CN");
			else
				holder.put("address_language", "en_US");

			JSONObject data, current_data;

			JSONArray array = new JSONArray();

			current_data = new JSONObject();
			current_data.put("cell_id", cellID.get(0).cellId);
			current_data.put("location_area_code",
					cellID.get(0).locationAreaCode);
			current_data.put("signal_strength", cellID.get(0).signal_strength);
			current_data.put("mobile_country_code",
					cellID.get(0).mobileCountryCode);
			current_data.put("mobile_network_code",
					cellID.get(0).mobileNetworkCode);
			current_data.put("age", 0);
			array.put(current_data);

			if (cellID.size() > 0) {
				for (int i = 1; i < cellID.size(); i++) {
					data = new JSONObject();
					data.put("cell_id", cellID.get(i).cellId);
					data.put("location_area_code",
							cellID.get(0).locationAreaCode);
					data.put("mobile_country_code",
							cellID.get(0).mobileCountryCode);
					data.put("mobile_network_code",
							cellID.get(0).mobileNetworkCode);
					data.put("signal_strength", cellID.get(0).signal_strength);
					data.put("age", 0);
					array.put(data);
				}
			}
			holder.put("cell_towers", array);

			if (wifi.get(0).mac != null) {
				data = new JSONObject();
				data.put("mac_address", wifi.get(0).mac);
				data.put("signal_strength", wifi.get(0).signal_strength);
				data.put("age", 0);
				array = new JSONArray();
				array.put(data);
				holder.put("wifi_towers", array);
			}

			StringEntity se = new StringEntity(holder.toString());
			Log.e("Location send", holder.toString());
			post.setEntity(se);
			HttpResponse resp = client.execute(post);

			HttpEntity entity = resp.getEntity();

			BufferedReader br = new BufferedReader(new InputStreamReader(entity
					.getContent()));
			StringBuffer sb = new StringBuffer();
			String result = br.readLine();
			while (result != null) {
				Log.e("Locaiton reseive", result);
				sb.append(result);
				result = br.readLine();
			}

			data = new JSONObject(sb.toString());
			data = (JSONObject) data.get("location");

			Location loc = new Location(LocationManager.NETWORK_PROVIDER);
			loc.setLatitude((Double) data.get("latitude"));
			loc.setLongitude((Double) data.get("longitude"));
			loc.setAccuracy(Float.parseFloat(data.get("accuracy").toString()));
			return loc;
		} catch (JSONException e) {
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// CDMA Location
	private Location callCDMAGear(TelephonyManager tmpManager) {
		try {
			CdmaCellLocation location = (CdmaCellLocation) tmpManager
					.getCellLocation();
			if (location == null)
				return null;

			int lat = location.getBaseStationLatitude();
			int lng = location.getBaseStationLongitude();
			double dlat = lat / 14400;
			double dlng = lng / 14400;

			Location loc = new Location(LocationManager.NETWORK_PROVIDER);
			loc.setLatitude(dlat);
			loc.setLongitude(dlng);
			loc.setTime(System.currentTimeMillis());

			return loc;
		} catch (Exception ex) {
			return null;
		}
	}
}
