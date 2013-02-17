package com.meishimeike.View;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mapabc.mapapi.core.GeoPoint;
import com.mapabc.mapapi.core.OverlayItem;
import com.mapabc.mapapi.map.ItemizedOverlay;
import com.mapabc.mapapi.map.MapController;
import com.mapabc.mapapi.map.MapView;
import com.mapabc.mapapi.map.MyLocationOverlay;
import com.mapabc.mapapi.map.Projection;
import com.meishimeike.R;

public class DinnerMapView extends LinearLayout {
	private Context mContext = null;
	private double dLng = 0, dLat = 0;
	MapView mMapView;
	MapController mMapController;
	MyLocationOverlay myloc;
	LinearLayout llMap = null;
	int screenHeight = 0;

	public DinnerMapView(Context context, AttributeSet attrs, double _lng,
			double _lat) {
		super(context, attrs);
		mContext = context;
		if (!"".equals(_lng) && !"".equals(_lng)) {
			dLng = _lng;
			dLat = _lat;
			init_ctrl();
		}
	}

	private void init_ctrl() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dinner_map_view, this);

		llMap = (LinearLayout) view.findViewById(R.id.llMap);
		mMapView = (MapView) view.findViewById(R.id.itemizedoverlayview);
		mMapView.setBuiltInZoomControls(false);
		mMapController = mMapView.getController();
		mMapController.setZoom(12);

		Drawable marker = view.getResources().getDrawable(
				R.drawable.img_map_poi);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		mMapView.getOverlays().add(new OverItemT(marker, mContext));

		myloc = new MyLocationOverlay(mContext, mMapView);
		myloc.enableMyLocation();
		myloc.enableCompass();
		mMapView.getOverlays().add(myloc);
	}

	class OverItemT extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
		private Drawable marker;

		private double mLat1 = dLat; // point1纬度
		private double mLon1 = dLng; // point1经度

		public OverItemT(Drawable marker, Context context) {
			super(boundCenterBottom(marker));

			this.marker = marker;

			GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));

			GeoList.add(new OverlayItem(p1, "", ""));
			populate();

			if (mLat1 != 0 || mLon1 != 0) {
				mMapController.animateTo(p1);
			}
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {

			// Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
			Projection projection = mapView.getProjection();
			for (int index = size() - 1; index >= 0; index--) { // 遍历GeoList
				OverlayItem overLayItem = getItem(index); // 得到给定索引的item

				String title = overLayItem.getTitle();
				// 把经纬度变换到相对于MapView左上角的屏幕像素坐标
				Point point = projection.toPixels(overLayItem.getPoint(), null);

				Paint paintCircle = new Paint();
				paintCircle.setColor(Color.RED);
				canvas.drawCircle(point.x, point.y, 5, paintCircle); // 画圆

				Paint paintText = new Paint();
				paintText.setColor(Color.BLACK);
				paintText.setTextSize(15);
				canvas.drawText(title, point.x, point.y - 25, paintText); // 绘制文本

			}

			super.draw(canvas, mapView, shadow);
			// 调整一个drawable边界，使得（0，0）是这个drawable底部最后一行中心的一个像素
			boundCenterBottom(marker);
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return GeoList.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return GeoList.size();
		}

		@Override
		// 处理当点击事件
		protected boolean onTap(int i) {
			// setFocus(GeoList.get(i));
			// Toast.makeText(this.mContext, GeoList.get(i).getSnippet(),
			// Toast.LENGTH_SHORT).show();
			return true;
		}
	}
}
