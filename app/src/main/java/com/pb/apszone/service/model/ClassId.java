package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassId{

	@SerializedName("name")
	private String name;

	@SerializedName("students")
	private List<StudentsItem> students;

	@SerializedName("id")
	private String id;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setStudents(List<StudentsItem> students){
		this.students = students;
	}

	public List<StudentsItem> getStudents(){
		return students;
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
			"ClassId{" + 
			"name = '" + name + '\'' + 
			",students = '" + students + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}