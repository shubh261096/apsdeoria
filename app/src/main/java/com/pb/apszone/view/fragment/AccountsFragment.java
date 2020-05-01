package com.pb.apszone.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.apszone.R;
import com.pb.apszone.database.AccountsModel;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.AccountsAdapter;
import com.pb.apszone.view.listener.OnItemClickListener;
import com.pb.apszone.view.ui.LoginActivity;
import com.pb.apszone.view.ui.SplashActivity;
import com.pb.apszone.viewModel.AccountsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;

public class AccountsFragment extends BaseFragment implements OnItemClickListener {

    private Unbinder unbinder;
    @BindView(R.id.toolbar_accounts)
    Toolbar toolbarAccounts;
    @BindView(R.id.rvAccounts)
    RecyclerView rvAccounts;
    @BindView(R.id.clAddAccount)
    ConstraintLayout clAddAccount;
    private List<AccountsModel> accountsModelList;
    private AccountsViewModel accountsViewModel;
    private AccountsAdapter accountsAdapter;
    private KeyStorePref keyStorePref;

    public AccountsFragment() {
        // Required empty public constructor
    }

    public static AccountsFragment newInstance() {
        return new AccountsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyStorePref = KeyStorePref.getInstance(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);
        unbinder = ButterKnife.bind(this, view);
        accountsModelList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvAccounts.setLayoutManager(layoutManager);
        accountsAdapter = new AccountsAdapter(accountsModelList, this);
        rvAccounts.setAdapter(accountsAdapter);
        toolbarAccounts.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }

    @Override
    public void getNetworkData(boolean status) {

    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        accountsViewModel = new ViewModelProvider(this).get(AccountsViewModel.class);
        observeAccounts();
    }

    private void observeAccounts() {
        accountsViewModel.getAllAccounts().observe(getViewLifecycleOwner(), accounts -> {
            if (accounts != null) {
                if (accountsAdapter != null) {
                    accountsAdapter.clearData();
                }
                accountsModelList.addAll(accounts);
                accountsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.clAddAccount)
    void onClAddAccountViewClicked() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        if (!TextUtils.equals(keyStorePref.getString(KEY_USER_ID), accountsModelList.get(position).getUserId())) {
            keyStorePref.putString(KEY_USER_ID, accountsModelList.get(position).getUserId());
            startActivity(new Intent(getActivity(), SplashActivity.class));
        }
    }
}