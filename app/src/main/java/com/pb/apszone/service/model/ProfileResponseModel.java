package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class ProfileResponseModel{

	/* Profile Dummy Response
	{
		"error": false,
			"message": "Profile Fetched Successfully",
			"profile": {
				"id": "APS101",
				"fullname": "Apoorva Pandey",
				"father_name": "Father Name",
				"mother_name": "Mother Name",
				"husband_name": "Husband Name",
				"qualification": "Qualification",
				"address": "Address",
				"dob": "2018-11-06",
				"gender": "Female",
				"date_of_join": "2019-03-01",
				"email": "apoorva@gmail.com",
				"phone": "8734791230",
				"last_login_date": "2019-05-04",
				"class_id": {
					"id": "C102",
					"name": "Nursery"
		},
				"status": "1",
				"parent": [
		{
				"id": "P101",
				"type": "Father",
				"student_id": "APS101",
				"fullname": "Ansh Pandey",
				"email": "ansh@gmail.com",
				"dob": "1970-04-01",
				"phone": "9839222123",
				"address": "Deoria",
				"status": "1"
		},
		{
				"id": "P102",
				"type": "Mother",
				"student_id": "APS101",
				"fullname": "Manvi Pandey",
				"email": "manu@gmail.com",
				"dob": "1970-04-01",
				"phone": "9839222112",
				"address": "Deoria",
				"status": "1"
		},
		{
				"id": "P103",
				"type": "Guardian",
				"student_id": "APS101",
				"fullname": "Shreyas Mehra",
				"email": "shryas@gmail.com",
				"dob": "1970-04-01",
				"phone": "9839222112",
				"address": "New Delhi",
				"status": "1"
		}
    ]
	}
	}*/

	@SerializedName("profile")
	private Profile profile;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	public void setProfile(Profile profile){
		this.profile = profile;
	}

	public Profile getProfile(){
		return profile;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ProfileResponseModel{" + 
			"profile = '" + profile + '\'' + 
			",error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}