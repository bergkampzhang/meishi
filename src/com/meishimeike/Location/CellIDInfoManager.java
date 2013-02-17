package com.meishimeike.Location;

import java.util.ArrayList;
import java.util.List;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

public class CellIDInfoManager {
	private PhoneStateListener listener;
	private GsmCellLocation gsm;
	private CdmaCellLocation cdma;
	int lac;
	String current_ci, mcc, mnc;

	public CellIDInfoManager() {
	}

	public ArrayList<CellIDInfo> getCellIDInfo(TelephonyManager manager) {
		listener = new PhoneStateListener();
		manager.listen(listener, 0);
		ArrayList<CellIDInfo> CellID = new ArrayList<CellIDInfo>();
		CellIDInfo currentCell = new CellIDInfo();

		int type = manager.getNetworkType();

		if (type == TelephonyManager.NETWORK_TYPE_GPRS
				|| type == TelephonyManager.NETWORK_TYPE_EDGE
				|| type == TelephonyManager.NETWORK_TYPE_HSDPA
				|| type == TelephonyManager.NETWORK_TYPE_UMTS) {
			gsm = ((GsmCellLocation) manager.getCellLocation());
			if (gsm == null)
				return null;
			lac = gsm.getLac();
			mcc = manager.getNetworkOperator().substring(0, 3);
			mnc = manager.getNetworkOperator().substring(3, 5);

			currentCell.cellId = gsm.getCid();
			currentCell.mobileCountryCode = mcc;
			currentCell.mobileNetworkCode = mnc;
			currentCell.locationAreaCode = lac;
			currentCell.radioType = "gsm";
			CellID.add(currentCell);

			List<NeighboringCellInfo> list = manager.getNeighboringCellInfo();
			int size = list.size();
			for (int i = 0; i < size; i++) {
				CellIDInfo info = new CellIDInfo();
				info.cellId = list.get(i).getCid();
				info.mobileCountryCode = mcc;
				info.mobileCountryCode = mnc;
				info.locationAreaCode = list.get(i).getLac();
				info.signal_strength = list.get(i).getRssi();
				CellID.add(info);
			}
			return CellID;

		} else if (type == TelephonyManager.NETWORK_TYPE_CDMA
				|| type == TelephonyManager.NETWORK_TYPE_1xRTT) {
			cdma = ((CdmaCellLocation) manager.getCellLocation());
			if (cdma == null)
				return null;

			if ("460".equals(manager.getSimOperator().substring(0, 3)))
				return null;
		}
		return null;
	}
}