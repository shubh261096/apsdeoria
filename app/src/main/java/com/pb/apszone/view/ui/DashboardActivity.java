package com.pb.apszone.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.DashboardItem;
import com.pb.apszone.utils.AutoFitGridLayoutManager;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.DashboardAdapter;
import com.pb.apszone.view.fragment.AttendanceFragment;
import com.pb.apszone.view.fragment.FeesFragment;
import com.pb.apszone.view.fragment.HomeworkFragment;
import com.pb.apszone.view.fragment.InboxFragment;
import com.pb.apszone.view.fragment.ProfileFragment;
import com.pb.apszone.view.fragment.StudentTimetableFragment;
import com.pb.apszone.view.fragment.SyllabusFragment;
import com.pb.apszone.view.listener.OnDashboardItemClickListener;
import com.pb.apszone.view.receiver.NetworkChangeReceiver;
import com.pb.apszone.viewModel.DashboardViewModel;
import com.pb.apszone.viewModel.ProfileFragmentViewModel;
import com.pb.apszone.viewModel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_ATTENDANCE;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_FEES;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_HOMEWORK;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_INBOX;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_SYLLABUS;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_TIMETABLE;
import static com.pb.apszone.utils.AppConstants.USER_GENDER_MALE;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_PARENT;

public class DashboardActivity extends AppCompatActivity implements OnDashboardItemClickListener, ProfileFragment.OnFragmentInteractionListener {

    @BindView(R.id.rvDashboardUI)
    RecyclerView rvDashboardUI;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.more_info)
    TextView moreInfo;
    @BindView(R.id.user_dp)
    ImageView userDp;
    @BindView(R.id.includeNetworkLayout)
    View includeNetworkLayout;
    DashboardViewModel dashboardViewModel;
    ProfileFragmentViewModel profileFragmentViewModel;
    SharedViewModel sharedViewModel;
    DashboardAdapter dashboardAdapter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<DashboardItem> dashboardItemList;
    private OnDashboardItemClickListener onDashboardItemClickListener;
    KeyStorePref keyStorePref;
    private String user_type, user_id;
    NetworkChangeReceiver changeReceiver;

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(changeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        /* Starting observer of Internet change*/
        changeReceiver = new NetworkChangeReceiver(this);
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        observeInternetChange();

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        profileFragmentViewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel.class);
        dashboardItemList = new ArrayList<>();
        onDashboardItemClickListener = this;
        keyStorePref = KeyStorePref.getInstance(this);
        user_type = keyStorePref.getString(KEY_USER_TYPE);
        user_id = keyStorePref.getString(KEY_USER_ID);
        setUpGridView();
    }

    private void observeInternetChange() {
        sharedViewModel.getStatus().observe(this, status -> {
            if (status != null) {
                if (status) {
                    if (dashboardAdapter != null) {
                        dashboardAdapter.clearData();
                    }
                    subscribe();
                    subscribeProfile();
                    includeNetworkLayout.setVisibility(View.GONE);
                } else {
                    includeNetworkLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void subscribeProfile() {
        profileFragmentViewModel.sendRequest(user_id, user_type);
        profileFragmentViewModel.getProfile().observe(this, profileResponseModel -> {
            if (profileResponseModel != null) {
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
        progressBar.setVisibility(View.VISIBLE);
        dashboardViewModel.getDashboardUIElements().observe(this, dashboardUIResponseModel -> {
            if (dashboardUIResponseModel != null) {
                progressBar.setVisibility(View.GONE);
                /* Clearing data since activity is always active */
                if (dashboardAdapter != null) {
                    dashboardAdapter.clearData();
                }
                if (!dashboardUIResponseModel.isError()) {
                    List<DashboardItem> dashboardItems = dashboardUIResponseModel.getDashboard();

                    /* Adding those data to list whose value is enabled */
                    dashboardItemList.addAll(dashboardViewModel.addListData(dashboardItems, user_type));
                    dashboardAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, dashboardUIResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

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
            if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_ATTENDANCE)) {
                Fragment fragment = AttendanceFragment.newInstance();
                replaceFragment(fragment);
            }
            if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_SYLLABUS)) {
                Fragment fragment = SyllabusFragment.newInstance();
                replaceFragment(fragment);
            }
            if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_HOMEWORK)) {
                Fragment fragment = HomeworkFragment.newInstance();
                replaceFragment(fragment);
            }
            if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_FEES)) {
                Fragment fragment = FeesFragment.newInstance();
                replaceFragment(fragment);
            }
        }
        if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_INBOX)) {
            Fragment fragment = InboxFragment.newInstance();
            replaceFragment(fragment);
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


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(changeReceiver);
    }
}
