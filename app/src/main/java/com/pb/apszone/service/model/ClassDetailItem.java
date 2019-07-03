package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class ClassDetailItem{

	@SerializedName("class_id")
	private ClassId classId;

	@SerializedName("timetable_id")
	private String timetableId;

	public void setClassId(ClassId classId){
		this.classId = classId;
	}

	public ClassId getClassId(){
		return classId;
	}

	public void setTimetableId(String timetableId){
		this.timetableId = timetableId;
	}

	public String getTimetableId(){
		return timetableId;
	}

	@Override
 	public String toString(){
		return
			"ClassDetailItem{" +
			"class_id = '" + classId + '\'' +
			",timetable_id = '" + timetableId + '\'' +
			"}";
		}
}