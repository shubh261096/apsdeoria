package com.pb.apszone.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AccountsDao {
    // Dao method to get all accounts
    @Query("SELECT * FROM AccountsModel")
    List<AccountsModel> getAllAccounts();

    // Dao method to insert account
    @Insert(onConflict = REPLACE)
    void insertAccount(AccountsModel accountsModel);

    // Dao method to delete account by userId
    @Query("DELETE FROM accountsmodel WHERE userId = :userId")
    void deleteByUserId(String userId);

    // Dao method to get username by userId
    @Query("SELECT userName FROM AccountsModel WHERE userId=:userId ")
    String getUserNameById(String userId);

    // Dao method to get userId limit by 1
    @Query("SELECT userId FROM AccountsModel LIMIT 1")
    String getUserId();

    // Doa to delete the table
    @Query("DELETE FROM AccountsModel")
    void deleteAll();
}