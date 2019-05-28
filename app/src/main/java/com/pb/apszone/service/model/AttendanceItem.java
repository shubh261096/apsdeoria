package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class AttendanceItem{

	@SerializedName("date")
	private String date;

	@SerializedName("student_id")
	private String studentId;

	@SerializedName("id")
	private String id;

	@SerializedName("timetable_id")
	private String timetableId;

	@SerializedName("remarks")
	private String remarks;

	@SerializedName("status")
	private String status;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setStudentId(String studentId){
		this.studentId = studentId;
	}

	public String getStudentId(){
		return studentId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTimetableId(String timetableId){
		this.timetableId = timetableId;
	}

	public String getTimetableId(){
		return timetableId;
	}

	public void setRemarks(String remarks){
		this.remarks = remarks;
	}

	public String getRemarks(){
		return remarks;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"AttendanceItem{" + 
			"date = '" + date + '\'' + 
			",student_id = '" + studentId + '\'' + 
			",id = '" + id + '\'' + 
			",timetable_id = '" + timetableId + '\'' + 
			",remarks = '" + remarks + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}