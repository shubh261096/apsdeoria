package com.pb.apszone.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.DashboardItem;
import com.pb.apszone.utils.AutoFitGridLayoutManager;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.DashboardAdapter;
import com.pb.apszone.view.fragment.ProfileFragment;
import com.pb.apszone.view.fragment.StudentTimetableFragment;
import com.pb.apszone.view.listener.OnDashboardItemClickListener;
import com.pb.apszone.viewModel.DashboardViewModel;
import com.pb.apszone.viewModel.ProfileFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_TIMETABLE;
import static com.pb.apszone.utils.AppConstants.USER_GENDER_MALE;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_PARENT;
import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.showProgress;

public class DashboardActivity extends AppCompatActivity implements OnDashboardItemClickListener, ProfileFragment.OnFragmentInteractionListener {

    @BindView(R.id.rvDashboardUI)
    RecyclerView rvDashboardUI;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.more_info)
    TextView moreInfo;
    @BindView(R.id.user_dp)
    ImageView userDp;
    DashboardViewModel dashboardViewModel;
    ProfileFragmentViewModel profileFragmentViewModel;
    DashboardAdapter dashboardAdapter;
    private List<DashboardItem> dashboardItemList;
    private OnDashboardItemClickListener onDashboardItemClickListener;
    KeyStorePref keyStorePref;
    private String user_type, user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        profileFragmentViewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel.class);
        dashboardItemList = new ArrayList<>();
        onDashboardItemClickListener = this;
        keyStorePref = KeyStorePref.getInstance(this);
        user_type = keyStorePref.getString(KEY_USER_TYPE);
        user_id = keyStorePref.getString(KEY_USER_ID);
        setUpGridView();
        showProgress(this, "Please wait...");
        subscribe();

        profileFragmentViewModel.sendRequest(user_id, user_type);
        subscribeProfile();
    }

    private void subscribeProfile() {
        profileFragmentViewModel.getProfile().observe(this, profileResponseModel -> {
            if (profileResponseModel != null) {
                hideProgress();
                dashboardViewModel.putSharedPrefData(profileResponseModel); // Adding SharedPref in case student/parent
                if (!TextUtils.isEmpty(profileResponseModel.getProfile().getGender())) {
                    int drawable = TextUtils.equals(profileResponseModel.getProfile().getGender(), USER_GENDER_MALE) ? R.drawable.profile_boy : R.drawable.profile_girl;
                    userDp.setImageResource(drawable);
                }
                userName.setText(profileResponseModel.getProfile().getFullname());
            }
        });
    }

    private void subscribe() {
        dashboardViewModel.getDashboardUIElements().observe(this, dashboardUIResponseModel -> {
            if (dashboardUIResponseModel != null) {
                List<DashboardItem> dashboardItems = dashboardUIResponseModel.getDashboard();
                dashboardItemList.addAll(dashboardItems);
                dashboardAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpGridView() {
        AutoFitGridLayoutManager manager = new AutoFitGridLayoutManager(this, 500);
        if (dashboardAdapter == null) {
            dashboardAdapter = new DashboardAdapter(dashboardItemList, onDashboardItemClickListener, DashboardActivity.this, user_type);
            rvDashboardUI.setLayoutManager(manager);
            rvDashboardUI.setAdapter(dashboardAdapter);
        } else {
            dashboardAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position, View view) {
        if (TextUtils.equals(user_type, USER_TYPE_PARENT)) {
            if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_TIMETABLE)) {
                Fragment fragment = StudentTimetableFragment.newInstance();
                replaceFragment(fragment);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finishAffinity();
        }
    }

    @Override
    public void onFragmentInteraction() {

    }

    @OnClick(R.id.more_info)
    public void onMoreInfoClick() {
        Fragment fragment = ProfileFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_USER_ID, user_id);
        bundle.putString(KEY_USER_TYPE, user_type);
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment).addToBackStack(destFragment.getClass().getSimpleName());
        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }
}
