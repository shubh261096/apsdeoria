package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"id = '" + id + '\'' + 
			",type = '" + type + '\'' +
			"}";
		}
}