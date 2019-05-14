package com.pb.apszone.service.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Profile{

	@SerializedName("parent")
	private List<ParentItem> parent;

	@SerializedName("address")
	private String address;

	@SerializedName("gender")
	private String gender;

	@SerializedName("mother_name")
	private String motherName;

	@SerializedName("class_id")
	private ClassId classId;

	@SerializedName("last_login_date")
	private String lastLoginDate;

	@SerializedName("date_of_join")
	private String dateOfJoin;

	@SerializedName("qualification")
	private String qualification;

	@SerializedName("father_name")
	private String fatherName;

	@SerializedName("phone")
	private String phone;

	@SerializedName("dob")
	private String dob;

	@SerializedName("id")
	private String id;

	@SerializedName("fullname")
	private String fullname;

	@SerializedName("husband_name")
	private String husbandName;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private String status;

	public void setParent(List<ParentItem> parent){
		this.parent = parent;
	}

	public List<ParentItem> getParent(){
		return parent;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setMotherName(String motherName){
		this.motherName = motherName;
	}

	public String getMotherName(){
		return motherName;
	}

	public void setClassId(ClassId classId){
		this.classId = classId;
	}

	public ClassId getClassId(){
		return classId;
	}

	public void setLastLoginDate(String lastLoginDate){
		this.lastLoginDate = lastLoginDate;
	}

	public String getLastLoginDate(){
		return lastLoginDate;
	}

	public void setDateOfJoin(String dateOfJoin){
		this.dateOfJoin = dateOfJoin;
	}

	public String getDateOfJoin(){
		return dateOfJoin;
	}

	public void setQualification(String qualification){
		this.qualification = qualification;
	}

	public String getQualification(){
		return qualification;
	}

	public void setFatherName(String fatherName){
		this.fatherName = fatherName;
	}

	public String getFatherName(){
		return fatherName;
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

	public void setHusbandName(String husbandName){
		this.husbandName = husbandName;
	}

	public String getHusbandName(){
		return husbandName;
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
			"Profile{" + 
			"parent = '" + parent + '\'' + 
			",address = '" + address + '\'' + 
			",gender = '" + gender + '\'' + 
			",mother_name = '" + motherName + '\'' + 
			",class_id = '" + classId + '\'' + 
			",last_login_date = '" + lastLoginDate + '\'' + 
			",date_of_join = '" + dateOfJoin + '\'' + 
			",qualification = '" + qualification + '\'' + 
			",father_name = '" + fatherName + '\'' + 
			",phone = '" + phone + '\'' + 
			",dob = '" + dob + '\'' + 
			",id = '" + id + '\'' + 
			",fullname = '" + fullname + '\'' + 
			",husband_name = '" + husbandName + '\'' + 
			",email = '" + email + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}