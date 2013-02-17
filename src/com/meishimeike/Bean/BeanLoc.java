package com.meishimeike.Bean;

import android.location.Location;

/**
 * @author :LiuQiang
 * @version ：2012-6-6 class desc
 */
public class BeanLoc {
	double lng = 0.0;
	double lat = 0.0;
	long distance = 0;
	Location loc = null;

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

}
