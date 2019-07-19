package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class DashboardItem{

	@SerializedName("student")
	private String student;

	@SerializedName("teacher")
	private String teacher;

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("name")
	private String name;

	@SerializedName("admin")
	private String admin;

	@SerializedName("id")
	private String id;

	public void setStudent(String student){
		this.student = student;
	}

	public String getStudent(){
		return student;
	}

	public void setTeacher(String teacher){
		this.teacher = teacher;
	}

	public String getTeacher(){
		return teacher;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setAdmin(String admin){
		this.admin = admin;
	}

	public String getAdmin(){
		return admin;
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
			"DashboardItem{" + 
			"student = '" + student + '\'' +
			",teacher = '" + teacher + '\'' + 
			",image_url = '" + imageUrl + '\'' + 
			",name = '" + name + '\'' + 
			",admin = '" + admin + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}