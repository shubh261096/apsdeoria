package com.pb.apszone.service.rest.model;

import com.google.gson.annotations.SerializedName;

public class AttendanceRequestModel{

	@SerializedName("month")
	private String month;

	@SerializedName("year")
	private String year;

	@SerializedName("student_id")
	private String studentId;

	public void setMonth(String month){
		this.month = month;
	}

	public String getMonth(){
		return month;
	}

	public void setYear(String year){
		this.year = year;
	}

	public String getYear(){
		return year;
	}

	public void setStudentId(String studentId){
		this.studentId = studentId;
	}

	public String getStudentId(){
		return studentId;
	}

	@Override
 	public String toString(){
		return 
			"AttendanceRequestModel{" + 
			"month = '" + month + '\'' + 
			",year = '" + year + '\'' + 
			",student_id = '" + studentId + '\'' + 
			"}";
		}
}