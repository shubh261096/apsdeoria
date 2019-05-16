package com.pb.apszone.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pb.apszone.R;
import com.pb.apszone.service.model.DashboardItem;
import com.pb.apszone.utils.AutoFitGridLayoutManager;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.DashboardAdapter;
import com.pb.apszone.view.listener.OnDashboardItemClickListener;
import com.pb.apszone.viewModel.DashboardViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;

public class DashboardActivity extends AppCompatActivity implements OnDashboardItemClickListener {

    @BindView(R.id.rvDashboardUI)
    RecyclerView rvDashboardUI;
    DashboardViewModel dashboardViewModel;
    DashboardAdapter dashboardAdapter;
    private List<DashboardItem> dashboardItemList;
    private OnDashboardItemClickListener onDashboardItemClickListener;
    KeyStorePref keyStorePref;
    String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardItemList = new ArrayList<>();
        onDashboardItemClickListener = this;
        keyStorePref = KeyStorePref.getInstance(this);
        user_type = keyStorePref.getString(KEY_USER_TYPE);
        setUpGridView();
        subscribe();
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

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
