package com.pb.apszone.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// Entity class model of room database
@Entity(indices = {@Index(value = {"userId"}, unique = true)})
public class AccountsModel {
    // room database entity primary key
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo(name = "userId")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @ColumnInfo(name = "userName")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @ColumnInfo(name = "userGender")
    private String userGender;

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public AccountsModel(String userId, String userName, String userGender) {
        this.userId = userId;
        this.userName = userName;
        this.userGender = userGender;
    }

}