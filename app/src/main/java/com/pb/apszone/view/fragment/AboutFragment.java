package com.pb.apszone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.pb.apszone.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.BuildConfig.VERSION_NAME;
import static com.pb.apszone.utils.AppConstants.PRIVACY_POLICY_URL;
import static com.pb.apszone.utils.AppConstants.WEBSITE_URL;
import static com.pb.apszone.utils.CommonUtils.openFacebookApp;
import static com.pb.apszone.utils.CommonUtils.openInstaApp;
import static com.pb.apszone.utils.CommonUtils.openTwitterApp;
import static com.pb.apszone.utils.CommonUtils.openWebIntent;
import static com.pb.apszone.utils.CommonUtils.openYoutubeApp;

public class AboutFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_about)
    Toolbar toolbarAbout;
    @BindView(R.id.txtViewPrivacyPolicy)
    TextView txtViewPrivacyPolicy;
    @BindView(R.id.txtViewWebsite)
    TextView txtViewWebsite;
    @BindView(R.id.txtViewInstagram)
    TextView txtViewInstagram;
    @BindView(R.id.txtViewFacebook)
    TextView txtViewFacebook;
    @BindView(R.id.txtViewTwitter)
    TextView txtViewTwitter;
    @BindView(R.id.txtViewYouTube)
    TextView txtViewYouTube;
    @BindView(R.id.app_version)
    TextView appVersion;


    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarAbout.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        getVersionCode();
    }

    private void getVersionCode() {
        appVersion.setText(String.format(getString(R.string.version_dash) + " %s", VERSION_NAME));
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

    @OnClick({R.id.txtViewPrivacyPolicy, R.id.txtViewWebsite, R.id.txtViewInstagram, R.id.txtViewFacebook, R.id.txtViewTwitter, R.id.txtViewYouTube})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtViewPrivacyPolicy:
                openWebIntent(Objects.requireNonNull(getContext()), PRIVACY_POLICY_URL);
                break;
            case R.id.txtViewWebsite:
                openWebIntent(Objects.requireNonNull(getContext()), WEBSITE_URL);
                break;
            case R.id.txtViewInstagram:
                openInstaApp(Objects.requireNonNull(getContext()));
                break;
            case R.id.txtViewFacebook:
                openFacebookApp(Objects.requireNonNull(getContext()));
                break;
            case R.id.txtViewTwitter:
                openTwitterApp(Objects.requireNonNull(getContext()));
                break;
            case R.id.txtViewYouTube:
                openYoutubeApp(Objects.requireNonNull(getContext()));
                break;
        }
    }
}
