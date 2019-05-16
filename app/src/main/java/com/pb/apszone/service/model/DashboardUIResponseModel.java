package com.pb.apszone.service.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DashboardUIResponseModel{

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	@SerializedName("dashboard")
	private List<DashboardItem> dashboard;

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setDashboard(List<DashboardItem> dashboard){
		this.dashboard = dashboard;
	}

	public List<DashboardItem> getDashboard(){
		return dashboard;
	}

	@Override
 	public String toString(){
		return 
			"DashboardUIResponseModel{" + 
			"error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			",dashboard = '" + dashboard + '\'' + 
			"}";
		}
}