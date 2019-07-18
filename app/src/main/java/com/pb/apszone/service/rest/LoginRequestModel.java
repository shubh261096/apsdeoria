package com.pb.apszone.service.rest;

import com.google.gson.annotations.SerializedName;

public class LoginRequestModel{

	@SerializedName("password")
	private String password;

	@SerializedName("id")
	private String id;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"LoginRequestModel{" + 
			"password = '" + password + '\'' + 
			",id = '" + id + '\'' +
			"}";
		}
}