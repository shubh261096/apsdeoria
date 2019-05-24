package com.pb.apszone.service.rest;

import com.google.gson.annotations.SerializedName;

public class TimetableRequestModel{

	@SerializedName("class_id")
	private String classId;

	@SerializedName("today")
	private String today;

	public void setClassId(String classId){
		this.classId = classId;
	}

	public String getClassId(){
		return classId;
	}

	public void setToday(String today){
		this.today = today;
	}

	public String getToday(){
		return today;
	}

	@Override
 	public String toString(){
		return 
			"TimetableRequestModel{" + 
			"class_id = '" + classId + '\'' + 
			",today = '" + today + '\'' + 
			"}";
		}
}