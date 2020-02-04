package com.pb.apszone.service.rest.model;

import com.google.gson.annotations.SerializedName;

public class TimetableRequestModel{

	@SerializedName("class_id")
	private String classId;

	@SerializedName("today")
	private String today;

	@SerializedName("teacher_id")
	private String teacherId;

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

	public void setTeacherId(String teacherId){
		this.teacherId = teacherId;
	}

	public String getTeacherId(){
		return teacherId;
	}

	@Override
 	public String toString(){
		return 
			"TimetableRequestModel{" + 
			"class_id = '" + classId + '\'' + 
			",today = '" + today + '\'' +
			",teacher_id = '" + teacherId + '\'' +
			"}";
		}
}