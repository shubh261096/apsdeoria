package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class FeesItem{

	@SerializedName("fees_paid")
	private String feesPaid;

	@SerializedName("period")
	private String period;

	@SerializedName("due_amount")
	private String dueAmount;

	@SerializedName("date_paid")
	private String datePaid;

	@SerializedName("fees_id")
	private FeesId feesId;

	@SerializedName("student_id")
	private String studentId;

	@SerializedName("status")
	private String status;

	public void setFeesPaid(String feesPaid){
		this.feesPaid = feesPaid;
	}

	public String getFeesPaid(){
		return feesPaid;
	}

	public void setPeriod(String period){
		this.period = period;
	}

	public String getPeriod(){
		return period;
	}

	public void setDueAmount(String dueAmount){
		this.dueAmount = dueAmount;
	}

	public String getDueAmount(){
		return dueAmount;
	}

	public void setDatePaid(String datePaid){
		this.datePaid = datePaid;
	}

	public String getDatePaid(){
		return datePaid;
	}

	public void setFeesId(FeesId feesId){
		this.feesId = feesId;
	}

	public FeesId getFeesId(){
		return feesId;
	}

	public void setStudentId(String studentId){
		this.studentId = studentId;
	}

	public String getStudentId(){
		return studentId;
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
				"FeesItem{" +
						"fees_paid = '" + feesPaid + '\'' +
						",period = '" + period + '\'' +
						",due_amount = '" + dueAmount + '\'' +
						",date_paid = '" + datePaid + '\'' +
						",fees_id = '" + feesId + '\'' +
						",student_id = '" + studentId + '\'' +
						",status = '" + status + '\'' +
						"}";
	}
}