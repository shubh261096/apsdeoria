package com.pb.apszone.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pb.apszone.database.AccountsModel;
import com.pb.apszone.database.AccountsRepository;
import com.pb.apszone.utils.KeyStorePref;

import java.util.List;

import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;

public class AccountsViewModel extends AndroidViewModel {

    private MutableLiveData<List<AccountsModel>> accountsModelMutableLiveData;
    private MutableLiveData<String> userNameMutableLiveData;
    private MutableLiveData<String> userIdMutableLiveData;
    private AccountsRepository accountsRepository;
    private KeyStorePref keyStorePref;

    public AccountsViewModel(@NonNull Application application) {
        super(application);
        keyStorePref = KeyStorePref.getInstance(application);
        accountsModelMutableLiveData = new MutableLiveData<>();
        userNameMutableLiveData = new MutableLiveData<>();
        userIdMutableLiveData = new MutableLiveData<>();
        accountsRepository = new AccountsRepository(application);
    }

    public LiveData<List<AccountsModel>> getAllAccounts() {
        accountsRepository.getAllAccounts(accountsModelMutableLiveData);
        return accountsModelMutableLiveData;
    }

    public void addAccount(AccountsModel accountsModel) {
        accountsRepository.addAccount(accountsModel);
    }

    public LiveData<String> getUserNameById() {
        accountsRepository.getUserNameById(userNameMutableLiveData, keyStorePref.getString(KEY_USER_ID));
        return userNameMutableLiveData;
    }

    public void deleteAccount(String userId) {
        accountsRepository.deleteAccount(userId);
    }

    public LiveData<String> getUserId() {
        accountsRepository.getUserId(userIdMutableLiveData);
        return userIdMutableLiveData;
    }

    public void deleteAllAccount() {
        accountsRepository.deleteAllAccount();
    }


}
