package com.pb.apszone.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pb.apszone.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.BuildConfig.VERSION_NAME;
import static com.pb.apszone.utils.AppConstants.PRIVACY_POLICY_URL;
import static com.pb.apszone.utils.AppConstants.WEBSITE_URL;

public class AboutFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_about)
    Toolbar toolbarAbout;
    @BindView(R.id.ll_privacy_policy)
    LinearLayout llPrivacyPolicy;
    @BindView(R.id.ll_website)
    LinearLayout llWebsite;
    @BindView(R.id.ll_instagram)
    LinearLayout llInstagram;
    @BindView(R.id.ll_facebook)
    LinearLayout llFacebook;
    @BindView(R.id.ll_twitter)
    LinearLayout llTwitter;
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
        appVersion.setText(String.format("Version: %s", VERSION_NAME));
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

    @OnClick({R.id.ll_privacy_policy, R.id.ll_website, R.id.ll_instagram, R.id.ll_facebook, R.id.ll_twitter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_privacy_policy:
                Intent privacyIntent = new Intent(Intent.ACTION_VIEW);
                privacyIntent.setData(Uri.parse(PRIVACY_POLICY_URL));
                startActivity(privacyIntent);
                break;
            case R.id.ll_website:
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(WEBSITE_URL));
                startActivity(webIntent);
                break;
            case R.id.ll_instagram:
                openInstaApp();
                break;
            case R.id.ll_facebook:
                openFacebookApp();
                break;
            case R.id.ll_twitter:
                openTwitterApp();
                break;
        }
    }

    protected void openFacebookApp() {

        String facebookPageID = "apsdeoria";
        String facebookUrl = "https://www.facebook.com/" + facebookPageID;
        String facebookUrlScheme = "fb://page/" + facebookPageID;

        try {
            int versionCode = Objects.requireNonNull(getContext()).getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

            if (versionCode >= 3002850) {
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrlScheme)));
            }
        } catch (PackageManager.NameNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));

        }

    }

    public void openInstaApp() {
        Uri uri = Uri.parse("http://instagram.com/_u/apsdeoria");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/apsdeoria")));
        }
    }

    public void openTwitterApp() {
        Intent intent = null;
        try {
            // get the Twitter app if possible
            Objects.requireNonNull(getContext()).getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=1101801777639419904"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/apsdeoria"));
        }
        this.startActivity(intent);
    }
}
