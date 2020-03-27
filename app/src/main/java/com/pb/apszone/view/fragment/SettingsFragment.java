package com.pb.apszone.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.pb.apszone.BuildConfig;
import com.pb.apszone.R;
import com.pb.apszone.utils.DialogHelper;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.ui.LoginActivity;
import com.pb.apszone.view.ui.SplashActivity;
import com.pb.apszone.viewModel.AccountsViewModel;
import com.pb.apszone.viewModel.DashboardViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_STUDENT;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_TEACHER;

public class SettingsFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.toolbar_setting)
    Toolbar toolbarSettings;
    @BindView(R.id.app_version)
    TextView appVersion;
    @BindView(R.id.ll_about)
    LinearLayout llAbout;
    @BindView(R.id.ll_logout)
    LinearLayout llLogout;
    @BindView(R.id.ll_contact)
    LinearLayout llContact;
    @BindView(R.id.ll_logout_all)
    LinearLayout llLogoutAll;
    @BindView(R.id.txtViewLogOut)
    TextView txtViewLogOut;
    private KeyStorePref keyStorePref;
    private AccountsViewModel accountsViewModel;
    private int accountSize;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarSettings.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        appVersion.setText(BuildConfig.VERSION_NAME);
        if (TextUtils.equals(keyStorePref.getString(KEY_USER_TYPE), USER_TYPE_STUDENT)) {
            accountsViewModel = new ViewModelProvider(this).get(AccountsViewModel.class);
            observeAccounts();
            observeUserName();
        }
    }

    private void observeUserName() {
        accountsViewModel.getUserNameById().observe(getViewLifecycleOwner(), userName -> {
            if (!TextUtils.isEmpty(userName))
                txtViewLogOut.setText(String.format("Log out %s", userName));
        });
    }

    private void observeAccounts() {
        accountsViewModel.getAllAccounts().observe(getViewLifecycleOwner(), accounts -> {
            if (accounts != null) {
                this.accountSize = accounts.size();
                if (accountSize > 1) llLogoutAll.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void getNetworkData(boolean status) {
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

    @OnClick({R.id.ll_about, R.id.ll_logout, R.id.ll_contact, R.id.ll_logout_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_about:
                Fragment aboutFragment = AboutFragment.newInstance();
                replaceFragment(aboutFragment);
                break;
            case R.id.ll_logout:
                DialogHelper.build(getActivity(),
                        getString(R.string.title_logout), getString(R.string.msg_logout), getString(R.string.msg_dialog_positive), getString(R.string.msg_dialog_negative), new DialogHelper.SimpleAlertListener() {
                            @Override
                            public void onPositive() {
                                if (TextUtils.equals(keyStorePref.getString(KEY_USER_TYPE), USER_TYPE_TEACHER)) {
                                    keyStorePref.clearAllPref();
                                    startActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                } else {
                                    if (accountSize == 1) {
                                        accountsViewModel.deleteAccount(keyStorePref.getString(KEY_USER_ID));
                                        keyStorePref.clearAllPref();
                                        startActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    } else {
                                        accountsViewModel.deleteAccount(keyStorePref.getString(KEY_USER_ID));
                                        accountsViewModel.getUserId().observe(getViewLifecycleOwner(), userId -> {
                                            if (!TextUtils.isEmpty(userId))
                                                keyStorePref.putString(KEY_USER_ID, userId);
                                        });
                                        DashboardViewModel.unsubscribeFromAllTopic();
                                        startActivity(new Intent(getActivity(), SplashActivity.class));
                                    }
                                }
                            }

                            @Override
                            public void onNegative() {

                            }
                        }
                ).show();
                break;
            case R.id.ll_contact:
                Fragment contactFragment = ContactFragment.newInstance();
                replaceFragment(contactFragment);
                break;
            case R.id.ll_logout_all:
                DialogHelper.build(getActivity(),
                        getString(R.string.title_logout), getString(R.string.msg_logout_all), getString(R.string.msg_dialog_positive), getString(R.string.msg_dialog_negative), new DialogHelper.SimpleAlertListener() {
                            @Override
                            public void onPositive() {
                                keyStorePref.clearAllPref();
                                accountsViewModel.deleteAllAccount();
                                startActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }

                            @Override
                            public void onNegative() {

                            }
                        }
                ).show();
                break;
        }
    }

    private void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.dynamic_settings_frame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
