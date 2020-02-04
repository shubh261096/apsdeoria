package com.pb.apszone.service.rest.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequestModel{

	@SerializedName("password")
	private String password;

	@SerializedName("id")
	private String id;

	@SerializedName("dob")
	private String dob;

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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	@Override
 	public String toString(){
		return 
			"LoginRequestModel{" + 
			"password = '" + password + '\'' + 
			",id = '" + id + '\'' +
			",dob = '" + dob + '\'' +
			"}";
		}
}