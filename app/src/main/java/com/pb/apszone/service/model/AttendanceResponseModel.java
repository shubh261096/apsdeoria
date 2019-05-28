package com.pb.apszone.service.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AttendanceResponseModel{

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	@SerializedName("attendance")
	private List<AttendanceItem> attendance;

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

	public void setAttendance(List<AttendanceItem> attendance){
		this.attendance = attendance;
	}

	public List<AttendanceItem> getAttendance(){
		return attendance;
	}

	@Override
 	public String toString(){
		return 
			"AttendanceResponseModel{" + 
			"error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			",attendance = '" + attendance + '\'' + 
			"}";
		}
}