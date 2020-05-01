package com.pb.apszone.database;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class AccountsRepository {

    // Define Accounts Dao
    private AccountsDao accountsDao;

    public AccountsRepository(@NonNull Application application) {
        AccountsDatabase accountsDatabase = AccountsDatabase.getDatabase(application);
        // init Accounts Dao
        accountsDao = accountsDatabase.accountsDao();
    }

    // method to get all accounts
    public void getAllAccounts(MutableLiveData<List<AccountsModel>> accountsModelMutableLiveData) {
        new GetAccounts(accountsModelMutableLiveData).execute();
    }

    // method to get username by userId
    public void getUserNameById(MutableLiveData<String> userNameMutableLiveData, String userId) {
        new GetUserNameById(userNameMutableLiveData).execute(userId);
    }

    // method to add accounts
    public void addAccount(AccountsModel accountsModel) {
        new AddAccount().execute(accountsModel);
    }

    // method to delete account by userId
    public void deleteAccount(String userId) {
        new DeleteAccount().execute(userId);
    }

    // method to get userId
    public void getUserId(MutableLiveData<String> userIdMutableLiveData) {
        new GetUserId(userIdMutableLiveData).execute();
    }

    // method to delete all account
    public void deleteAllAccount() {
        HandlerThread handlerThread = new HandlerThread("newHandlerThread");
        handlerThread.start();
        Handler myHandler = new Handler(handlerThread.getLooper());
        myHandler.post(() -> accountsDao.deleteAll());
    }

    //Async task to add note
    @SuppressLint("StaticFieldLeak")
    public class AddAccount extends AsyncTask<AccountsModel, Void, Void> {
        @Override
        protected Void doInBackground(AccountsModel... accountsModels) {
            accountsDao.insertAccount(accountsModels[0]);
            return null;
        }
    }

    //Async task to get account
    @SuppressLint("StaticFieldLeak")
    public class GetAccounts extends AsyncTask<Void, Void, Void> {

        MutableLiveData<List<AccountsModel>> accountsModelMutableLiveData;

        GetAccounts(MutableLiveData<List<AccountsModel>> accountsModelMutableLiveData) {
            this.accountsModelMutableLiveData = accountsModelMutableLiveData;
        }

        @Override
        protected final Void doInBackground(Void... params) {
            accountsModelMutableLiveData.postValue(accountsDao.getAllAccounts());
            return null;
        }
    }

    //Async task to get username by userId
    @SuppressLint("StaticFieldLeak")
    public class GetUserNameById extends AsyncTask<String, Void, Void> {
        MutableLiveData<String> userNameMutableLiveData;

        GetUserNameById(MutableLiveData<String> userNameMutableLiveData) {
            this.userNameMutableLiveData = userNameMutableLiveData;
        }

        @Override
        protected final Void doInBackground(String... strings) {
            userNameMutableLiveData.postValue(accountsDao.getUserNameById(strings[0]));
            return null;
        }
    }

    //Async task to add note
    @SuppressLint("StaticFieldLeak")
    public class DeleteAccount extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            accountsDao.deleteByUserId(strings[0]);
            return null;
        }
    }

    //Async task to get userId
    @SuppressLint("StaticFieldLeak")
    public class GetUserId extends AsyncTask<Void, Void, Void> {

        private MutableLiveData<String> userIdMutableLiveData;

        GetUserId(MutableLiveData<String> userIdMutableLiveData) {
            this.userIdMutableLiveData = userIdMutableLiveData;
        }

        @Override
        protected final Void doInBackground(Void... strings) {
            userIdMutableLiveData.postValue(accountsDao.getUserId());
            return null;
        }
    }
}
