package com.pb.apszone.view.ui;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.pb.apszone.R;
import com.pb.apszone.service.model.DashboardItem;
import com.pb.apszone.service.model.DashboardUIResponseModel;
import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.utils.AutoFitGridLayoutManager;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.DashboardAdapter;
import com.pb.apszone.view.fragment.AttendanceFragment;
import com.pb.apszone.view.fragment.AttendanceTeacherFragment;
import com.pb.apszone.view.fragment.DownloadFragment;
import com.pb.apszone.view.fragment.FeedbackFragment;
import com.pb.apszone.view.fragment.FeesFragment;
import com.pb.apszone.view.fragment.HomeworkFragment;
import com.pb.apszone.view.fragment.HomeworkTeacherFragment;
import com.pb.apszone.view.fragment.InboxFragment;
import com.pb.apszone.view.fragment.LearnFragment;
import com.pb.apszone.view.fragment.ProfileFragment;
import com.pb.apszone.view.fragment.SettingsFragment;
import com.pb.apszone.view.fragment.SyllabusFragment;
import com.pb.apszone.view.fragment.SyllabusTeacherFragment;
import com.pb.apszone.view.fragment.TimetableFragment;
import com.pb.apszone.view.listener.OnDashboardItemClickListener;
import com.pb.apszone.view.receiver.NetworkChangeReceiver;
import com.pb.apszone.viewModel.DashboardViewModel;
import com.pb.apszone.viewModel.ProfileFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_ATTENDANCE;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_DOWNLOAD;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_FEES;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_HOMEWORK;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_INBOX;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_LEARN;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_SYLLABUS;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_TEACHER_FEEDBACK;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_TIMETABLE;
import static com.pb.apszone.utils.AppConstants.USER_GENDER_MALE;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_STUDENT;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_TEACHER;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;

public class DashboardActivity extends AppCompatActivity implements OnDashboardItemClickListener {

    @BindView(R.id.rvDashboardUI)
    RecyclerView rvDashboardUI;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.more_info)
    TextView moreInfo;
    @BindView(R.id.user_dp)
    ImageView userDp;
    @BindView(R.id.includeNetworkLayout)
    ConstraintLayout includeNetworkLayout;
    private DashboardViewModel dashboardViewModel;
    private ProfileFragmentViewModel profileFragmentViewModel;
    private DashboardAdapter dashboardAdapter;
    private NetworkChangeReceiver networkChangeReceiver;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar_dashboard)
    Toolbar toolbarDashboard;
    @BindView(R.id.myCoordinatorLayout)
    CoordinatorLayout myCoordinatorLayout;
    private List<DashboardItem> dashboardItemList;
    private OnDashboardItemClickListener onDashboardItemClickListener;
    private String user_type, user_id;
    private boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarDashboard);

        /* Starting observer of Internet change*/
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        observeInternetChange();
        /* ViewModel initialization */
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        profileFragmentViewModel = new ViewModelProvider(this).get(ProfileFragmentViewModel.class);
        /* Observe LiveData*/
        observeDashboardUIElements();
        observeProfile();

        dashboardItemList = new ArrayList<>();
        onDashboardItemClickListener = this;
        KeyStorePref keyStorePref = KeyStorePref.getInstance(this);
        user_type = keyStorePref.getString(KEY_USER_TYPE);
        user_id = keyStorePref.getString(KEY_USER_ID);
        setUpGridView();
    }

    private void observeProfile() {
        profileFragmentViewModel.getProfile().observe(this, responseEvent -> {
            if (responseEvent != null) {
                if (responseEvent.isSuccess()) {
                    ProfileResponseModel profileResponseModel = responseEvent.getProfileResponseModel();
                    dashboardViewModel.subscribeToTopic(profileResponseModel);
                    if (!TextUtils.isEmpty(profileResponseModel.getProfile().getGender())) {
                        int drawable = TextUtils.equals(profileResponseModel.getProfile().getGender(), USER_GENDER_MALE) ? R.drawable.profile_boy : R.drawable.profile_girl;
                        userDp.setImageResource(drawable);
                    }
                    userName.setText(profileResponseModel.getProfile().getFullname());
                } else {
                    showInformativeDialog(this, responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    private void observeDashboardUIElements() {
        dashboardViewModel.getDashboardUIElements().observe(this, responseEvent -> {
            progressBar.setVisibility(View.GONE);
            if (responseEvent != null) {
                /* Clearing data since activity is always active */
                if (dashboardAdapter != null) {
                    dashboardAdapter.clearData();
                }
                if (responseEvent.isSuccess()) {
                    DashboardUIResponseModel dashboardUIResponseModel = responseEvent.getDashboardUIResponseModel();
                    if (!dashboardUIResponseModel.isError()) {
                        List<DashboardItem> dashboardItems = dashboardUIResponseModel.getDashboard();

                        /* Adding those data to list whose value is enabled */
                        dashboardItemList.addAll(dashboardViewModel.addListData(dashboardItems, user_type));
                        dashboardAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, dashboardUIResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showInformativeDialog(this, responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    private void observeInternetChange() {
        NetworkChangeReceiver.getStatus().observe(this, status -> {
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
        profileFragmentViewModel.sendRequest(user_id);
    }

    private void subscribe() {
        progressBar.setVisibility(View.VISIBLE);
        dashboardViewModel.sendRequest();
    }

    private void setUpGridView() {
        AutoFitGridLayoutManager manager = new AutoFitGridLayoutManager(this, 500);
        if (dashboardAdapter == null) {
            dashboardAdapter = new DashboardAdapter(dashboardItemList, onDashboardItemClickListener);
            rvDashboardUI.setLayoutManager(manager);
            rvDashboardUI.setAdapter(dashboardAdapter);
        } else {
            dashboardAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position, View view) {
        if (TextUtils.equals(user_type, USER_TYPE_STUDENT)) {
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
            if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_TEACHER_FEEDBACK)) {
                Fragment fragment = FeedbackFragment.newInstance();
                replaceFragment(fragment);
            }
            if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_LEARN)) {
                Fragment fragment = LearnFragment.newInstance();
                replaceFragment(fragment);
            }
        } else if (TextUtils.equals(user_type, USER_TYPE_TEACHER)) {
            if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_ATTENDANCE)) {
                Fragment fragment = AttendanceTeacherFragment.newInstance();
                replaceFragment(fragment);
            }
            if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_HOMEWORK)) {
                Fragment fragment = HomeworkTeacherFragment.newInstance();
                replaceFragment(fragment);
            }
            if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_SYLLABUS)) {
                Fragment fragment = SyllabusTeacherFragment.newInstance();
                replaceFragment(fragment);
            }
        }
        if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_TIMETABLE)) {
            Fragment fragment = TimetableFragment.newInstance();
            replaceFragment(fragment);
        }
        if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_INBOX)) {
            Fragment fragment = InboxFragment.newInstance();
            replaceFragment(fragment);
        }
        if (TextUtils.equals(dashboardItemList.get(position).getName(), UI_ELEMENT_DOWNLOAD)) {
            Fragment fragment = DownloadFragment.newInstance();
            replaceFragment(fragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (this.doubleBackToExitPressedOnce) {
                finishAffinity();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Snackbar snackbar = Snackbar.make(myCoordinatorLayout, getString(R.string.exit_application), Snackbar.LENGTH_LONG);
            snackbar.show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
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

    private void replaceFragment(Fragment destFragment) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Fragment fragment = SettingsFragment.newInstance();
            replaceFragment(fragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
}
