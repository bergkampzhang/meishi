package com.meishimeike.Location;

import java.util.ArrayList;
import android.net.wifi.WifiManager;

public class WifiInfoManager {

	public WifiInfoManager() {
	}

	public ArrayList<WifiInfo> getWifiInfo(WifiManager wm) {
			ArrayList<WifiInfo> wifi = new ArrayList<WifiInfo>();  		
			WifiInfo info = new WifiInfo();
			info.mac = wm.getConnectionInfo().getBSSID(); 
			info.signal_strength = wm.getConnectionInfo().getRssi();
			wifi.add(info);
			return wifi;
	}
}