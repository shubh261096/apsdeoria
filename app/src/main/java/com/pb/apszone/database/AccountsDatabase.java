package com.pb.apszone.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Room database class
@Database(entities = AccountsModel.class, version = 1, exportSchema = false)
public abstract class AccountsDatabase extends RoomDatabase {
    //define static instance
    private static AccountsDatabase mInstance;

    // method to get room database
    public static AccountsDatabase getDatabase(Context context) {

        if (mInstance == null)
            mInstance = Room.databaseBuilder(context.getApplicationContext(),
                    AccountsDatabase.class, "accounts_db")
                    .build();

        return mInstance;
    }

    // method to remove instance
    public static void closeDatabase() {
        mInstance = null;
    }

    // define accounts dao ( data access object )
    public abstract AccountsDao accountsDao();


}