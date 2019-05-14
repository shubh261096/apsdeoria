package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class ParentItem{

	@SerializedName("address")
	private String address;

	@SerializedName("phone")
	private String phone;

	@SerializedName("dob")
	private String dob;

	@SerializedName("student_id")
	private String studentId;

	@SerializedName("id")
	private String id;

	@SerializedName("fullname")
	private String fullname;

	@SerializedName("type")
	private String type;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private String status;

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setDob(String dob){
		this.dob = dob;
	}

	public String getDob(){
		return dob;
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

	public void setFullname(String fullname){
		this.fullname = fullname;
	}

	public String getFullname(){
		return fullname;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
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
			"ParentItem{" + 
			"address = '" + address + '\'' + 
			",phone = '" + phone + '\'' + 
			",dob = '" + dob + '\'' + 
			",student_id = '" + studentId + '\'' + 
			",id = '" + id + '\'' + 
			",fullname = '" + fullname + '\'' + 
			",type = '" + type + '\'' + 
			",email = '" + email + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}