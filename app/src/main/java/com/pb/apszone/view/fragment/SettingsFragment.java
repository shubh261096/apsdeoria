package com.pb.apszone.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pb.apszone.BuildConfig;
import com.pb.apszone.R;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.utils.DialogHelper;
import com.pb.apszone.view.ui.LoginActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsFragment extends BaseFragment {

    Unbinder unbinder;
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
    KeyStorePref keyStorePref;

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
    }

    @Override
    public void getNetworkData(boolean status) {
    }

    @Override
    public void onAttach(Context context) {
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

    @OnClick({R.id.ll_about, R.id.ll_logout, R.id.ll_contact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_about:
                Fragment aboutFragment = AboutFragment.newInstance();
                replaceFragment(aboutFragment);
                break;
            case R.id.ll_logout:
                DialogHelper.build(getActivity(),
                        getString(R.string.title_logout), getString(R.string.msg_logout), getString(R.string.msg_dialog_positive),getString(R.string.msg_dialog_negative), new DialogHelper.SimpleAlertListener() {
                            @Override
                            public void onPositive() {
                                keyStorePref.clearAllPref();
                                startActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.dynamic_settings_frame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
