package com.pb.apszone.service.rest;

import com.google.gson.annotations.SerializedName;

public class SyllabusRequestModel{

	@SerializedName("class_id")
	private String classId;

	public void setClassId(String classId){
		this.classId = classId;
	}

	public String getClassId(){
		return classId;
	}

	@Override
 	public String toString(){
		return 
			"SyllabusRequestModel{" + 
			"class_id = '" + classId + '\'' + 
			"}";
		}
}