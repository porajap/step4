package com.poseintelligence.cssd.model;

public class ModelMaster {
	private String S_Id, S_Code, S_Name;
	private boolean B_IsCancel;
	
	

	public ModelMaster(String s_Id, String s_Code, String s_Name, boolean b_IsCancel) {
		S_Id = s_Id;
		S_Code = s_Code;
		S_Name = s_Name;
		B_IsCancel = b_IsCancel;
	}

	public String getS_Id() {
		return S_Id;
	}

	public void setS_Id(String s_Id) {
		S_Id = s_Id;
	}

	public String getS_Code() {
		return S_Code;
	}

	public void setS_Code(String s_Code) {
		S_Code = s_Code;
	}

	public String getS_Name() {
		return S_Name;
	}

	public void setS_Name(String s_Name) {
		S_Name = s_Name;
	}

	public boolean isB_IsCancel() {
		return B_IsCancel;
	}

	public void setB_IsCancel(boolean b_IsCancel) {
		B_IsCancel = b_IsCancel;
	}

}
