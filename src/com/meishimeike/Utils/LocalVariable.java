package com.meishimeike.Utils;

import android.content.Context;

public class LocalVariable {
	public static final String NAME = "msmk_preferences";

	private Context mContext;
	private Commons comm;

	public LocalVariable(Context context) {
		mContext = context;
		comm = new Commons();
	}

	// ---------is not first-------
	public void setIsNotFirst(boolean value) {
		comm.saveBoolSP(mContext, "IsNotFirst", value);
	}

	public boolean getIsNotFirst() {
		return comm.getBoolSP(mContext, "IsNotFirst");
	}

	// ---------------------
	// ---------Temp Food Id---------
	public void setTempFoodId(String value) {
		comm.saveStrSP(mContext, "TempFoodId", value);
	}

	public String getTempFoodId() {
		return comm.getStrSP(mContext, "TempFoodId");
	}

	// ---------------------
	// ---------Temp Msg User Id---------
	public void setTempMsgUId(String value) {
		comm.saveStrSP(mContext, "TempMsgUId", value);
	}

	public String getTempMsgUId() {
		return comm.getStrSP(mContext, "TempMsgUId");
	}

	// ---------------------
	// ---------Temp Msg User Name---------
	public void setTempMsgUName(String value) {
		comm.saveStrSP(mContext, "TempMsgUName", value);
	}

	public String getTempMsgUName() {
		return comm.getStrSP(mContext, "TempMsgUName");
	}

	// ---------------------

	// ---------login-------
	public void setIsLogin(boolean value) {
		comm.saveBoolSP(mContext, "IsLogin", value);
	}

	public boolean getIsLogin() {
		return comm.getBoolSP(mContext, "IsLogin");
	}

	// ---------------------
	// ---------weibo login-------
	public void setIsWLogin(boolean value) {
		comm.saveBoolSP(mContext, "IsWLogin", value);
	}

	public boolean getIsWLogin() {
		return comm.getBoolSP(mContext, "IsWLogin");
	}

	// ---------------------
	// ---------binding-------
	public void setIsBinding(boolean value) {
		comm.saveBoolSP(mContext, "IsBinding", value);
	}

	public boolean getIsBinding() {
		return comm.getBoolSP(mContext, "IsBinding");
	}

	// ---------------------
	// ---------sina weibo friend-------
	public void setIsSinaFriend(boolean value) {
		comm.saveBoolSP(mContext, "IsSinaFriend", value);
	}

	public boolean getIsSinaFriend() {
		return comm.getBoolSP(mContext, "IsSinaFriend");
	}

	// ---------------------
	// ---------is new register-------
	public void setIsNewRegister(boolean value) {
		comm.saveBoolSP(mContext, "IsNewRegister", value);
	}

	public boolean getIsNewRegister() {
		return comm.getBoolSP(mContext, "IsNewRegister");
	}

	// ---------------------
	// ---------Weibo Uid---------
	public void setWUid(String value) {
		comm.saveStrSP(mContext, "WUid", value);
	}

	public String getWUid() {
		return comm.getStrSP(mContext, "WUid");
	}

	// ---------------------

	// ---------Weibo Secret---------
	public void setWSecret(String value) {
		comm.saveStrSP(mContext, "WSecret", value);
	}

	public String getWSecret() {
		return comm.getStrSP(mContext, "WSecret");
	}

	// ---------------------

	// ---------Weibo Token---------
	public void setWToken(String value) {
		comm.saveStrSP(mContext, "WToken", value);
	}

	public String getWToken() {
		return comm.getStrSP(mContext, "WToken");
	}

	// ---------------------

	// ---------Uid---------
	public void setUid(String value) {
		comm.saveStrSP(mContext, "Uid", value);
	}

	public String getUid() {
		return comm.getStrSP(mContext, "Uid");
	}

	// ---------------------

	// ---------Name---------
	public void setName(String value) {
		comm.saveStrSP(mContext, "Name", value);
	}

	public String getName() {
		return comm.getStrSP(mContext, "Name");
	}

	// ---------------------
	// ---------quanzi city id---------
		public void setQuanZiCityId(String value) {
			comm.saveStrSP(mContext, "QuanZiCityId", value);
		}

		public String getQuanZiCityId() {
			return comm.getStrSP(mContext, "QuanZiCityId");
		}

		// ---------------------
	// ---------Head Url---------
	public void setHeadUrl(String value) {
		comm.saveStrSP(mContext, "HeadUrl", value);
	}

	public String getHeadUrl() {
		return comm.getStrSP(mContext, "HeadUrl");
	}

	// ---------------------
	// ---------Login Name---------
	public void setLoginName(String value) {
		comm.saveStrSP(mContext, "LoginName", value);
	}

	public String getLoginName() {
		return comm.getStrSP(mContext, "LoginName");
	}

	// ---------------------
	// ---------TempDinnerId---------
	public void setTempDId(String value) {
		comm.saveStrSP(mContext, "TempDId", value);
	}

	public String getTempDId() {
		return comm.getStrSP(mContext, "TempDId");
	}

	// ---------------------
	// ---------TempFoodId---------
	public void setTempFId(String value) {
		comm.saveStrSP(mContext, "TempFId", value);
	}

	public String getTempFId() {
		return comm.getStrSP(mContext, "TempFId");
	}

	// ---------------------
	// ---------Temp User Id---------
	public void setTempUId(String value) {
		comm.saveStrSP(mContext, "TempUId", value);
	}

	public String getTempUId() {
		return comm.getStrSP(mContext, "TempUId");
	}

	// ---------------------
	// ---------Temp User IsAttention---------
	public void setTempUAttention(boolean value) {
		comm.saveBoolSP(mContext, "TempUAttention", value);
	}

	public boolean getTempUAttention() {
		return comm.getBoolSP(mContext, "TempUAttention");
	}

	// ---------------------
	// ---------TempPic1---------
	public void setTempPic1(String value) {
		comm.saveStrSP(mContext, "TempPic1", value);
	}

	public String getTempPic1() {
		return comm.getStrSP(mContext, "TempPic1");
	}

	// ---------------------
	// ---------TempPic2---------
	public void setTempPic2(String value) {
		comm.saveStrSP(mContext, "TempPic2", value);
	}

	public String getTempPic2() {
		return comm.getStrSP(mContext, "TempPic2");
	}

	// ---------------------
	// ---------TempPic3---------
	public void setTempPic3(String value) {
		comm.saveStrSP(mContext, "TempPic3", value);
	}

	public String getTempPic3() {
		return comm.getStrSP(mContext, "TempPic3");
	}

	// ---------------------
	// ---------TempPicType1---------
	public void setTempPicType1(String value) {
		comm.saveStrSP(mContext, "TempPicType1", value);
	}

	public String getTempPicType1() {
		return comm.getStrSP(mContext, "TempPicType1");
	}

	// ---------------------
	// ---------TempPicType2---------
	public void setTempPicType2(String value) {
		comm.saveStrSP(mContext, "TempPicType2", value);
	}

	public String getTempPicType2() {
		return comm.getStrSP(mContext, "TempPicType2");
	}

	// ---------------------
	// ---------TempPicType3---------
	public void setTempPicType3(String value) {
		comm.saveStrSP(mContext, "TempPicType3", value);
	}

	public String getTempPicType3() {
		return comm.getStrSP(mContext, "TempPicType3");
	}

	// ---------------------
	// ---------TempPicNum---------
	public void setTempPicNum(int value) {
		comm.saveIntSP(mContext, "TempPicNum", value);
	}

	public int getTempPicNum() {
		return comm.getIntSP(mContext, "TempPicNum");
	}

	// ---------------------
	// ---------TempFoodName---------
	public void setTempFoodName(String value) {
		comm.saveStrSP(mContext, "TempFoodName", value);
	}

	public String getTempFoodName() {
		return comm.getStrSP(mContext, "TempFoodName");
	}

	// ---------------------
	// ---------TempDinnerId---------
	public void setTempDinnerId(String value) {
		comm.saveStrSP(mContext, "DinnerId", value);
	}

	public String getTempDinnerId() {
		return comm.getStrSP(mContext, "DinnerId");
	}

	// ---------------------
	// ---------TempDinnerName---------
	public void setTempDinnerName(String value) {
		comm.saveStrSP(mContext, "DinnerName", value);
	}

	public String getTempDinnerName() {
		return comm.getStrSP(mContext, "DinnerName");
	}

	// ---------------------
	// ---------is new Login-------
	public void setIsNewLogin(boolean value) {
		comm.saveBoolSP(mContext, "IsNewLogin", value);
	}

	public boolean getIsNewLogin() {
		return comm.getBoolSP(mContext, "IsNewLogin");
	}

	// ---------------------
	// ---------is new Publish-------
	public void setIsNewPublish(boolean value) {
		comm.saveBoolSP(mContext, "IsNewPublish", value);
	}

	public boolean getIsNewPublish() {
		return comm.getBoolSP(mContext, "IsNewPublish");
	}

	// ---------------------
}
