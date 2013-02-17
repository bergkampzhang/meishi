package com.meishimeike.Bean;

public class BeanDinner {
	private String rid = "";
	private String name = "";
	private String city = "";
	private String c_date = "";
	private String city_name = "";
	private String address = "";
	private String tel = "";
	private float distance = 0;
	private double lng = 0.0;
	private double lat = 0.0;

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getC_date() {
		return c_date;
	}

	public void setC_date(String cDate) {
		c_date = cDate;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String cityName) {
		city_name = cityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
