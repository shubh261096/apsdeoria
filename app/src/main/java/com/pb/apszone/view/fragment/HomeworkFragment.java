package com.pb.apszone.view.fragment;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.HomeworkItem;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.HomeworkAdapter;
import com.pb.apszone.view.adapter.SyllabusAdapter;
import com.pb.apszone.view.receiver.DownloadBroadcastReceiver;
import com.pb.apszone.viewModel.HomeworkFragmentViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_DOWNLOAD_ID;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_CLASS_ID;
import static com.pb.apszone.utils.CommonUtils.beginDownload;
import static com.pb.apszone.utils.CommonUtils.getNextDate;
import static com.pb.apszone.utils.CommonUtils.getPreviousDate;
import static com.pb.apszone.utils.CommonUtils.getTodayDate;

public class HomeworkFragment extends BaseFragment implements SyllabusAdapter.OnDownloadItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.toolbar_homework)
    Toolbar toolbarHomework;
    @BindView(R.id.rvHomework)
    RecyclerView rvHomework;
    @BindView(R.id.previous)
    TextView previous;
    @BindView(R.id.today_date)
    TextView todayDate;
    @BindView(R.id.next)
    TextView next;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<HomeworkItem> homeworkItemList;
    HomeworkFragmentViewModel homeworkFragmentViewModel;
    KeyStorePref keyStorePref;
    HomeworkAdapter homeworkAdapter;
    DownloadBroadcastReceiver downloadBroadcastReceiver;
    private String today_date;

    public HomeworkFragment() {
        // Required empty public constructor
    }

    public static HomeworkFragment newInstance() {
        return new HomeworkFragment();
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
        View view = inflater.inflate(R.layout.fragment_homework, container, false);
        unbinder = ButterKnife.bind(this, view);
        homeworkItemList = new ArrayList<>();
        rvHomework.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeworkAdapter = new HomeworkAdapter(homeworkItemList, getActivity(), this::onItemClick);
        rvHomework.setAdapter(homeworkAdapter);
        toolbarHomework.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        today_date = getTodayDate();
        todayDate.setText(today_date);
        downloadBroadcastReceiver = new DownloadBroadcastReceiver();
        Objects.requireNonNull(getContext()).registerReceiver(downloadBroadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        homeworkFragmentViewModel = ViewModelProviders.of(this).get(HomeworkFragmentViewModel.class);
        observeHomework();
    }

    private void observeHomework() {
        homeworkFragmentViewModel.getHomework().observe(this, homeworkResponseModel -> {
            if (homeworkResponseModel != null) {
                progressBar.setVisibility(View.GONE);

                if (homeworkAdapter != null) {
                    homeworkAdapter.clearData();
                }

                if (!homeworkResponseModel.isError()) {
                    List<HomeworkItem> homeworkItems = homeworkResponseModel.getHomework();
                    homeworkItemList.addAll(homeworkItems);
                    homeworkAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), homeworkResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void getNetworkData(boolean status) {
        if (status) {
            if (homeworkAdapter != null) {
                homeworkAdapter.clearData();
            }
            subscribe();
        }
    }

    private void subscribe() {
        if (!TextUtils.isEmpty(keyStorePref.getString(KEY_STUDENT_CLASS_ID))) {
            homeworkFragmentViewModel.sendRequest(keyStorePref.getString(KEY_STUDENT_CLASS_ID), today_date);
        }
        progressBar.setVisibility(View.VISIBLE);
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
        Objects.requireNonNull(getContext()).unregisterReceiver(downloadBroadcastReceiver);
    }

    @Override
    public void onItemClick(int position, View view) {
        if (!TextUtils.isEmpty(homeworkItemList.get(position).getData())) {
            if (URLUtil.isValidUrl(homeworkItemList.get(position).getData())) {
                KEY_DOWNLOAD_ID = beginDownload(homeworkItemList.get(position).getData(), getContext());
            } else {
                Toast.makeText(getContext(), "Not a valid URL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick({R.id.previous, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.previous:
                today_date = getPreviousDate(today_date);
                todayDate.setText(today_date);
                if (homeworkAdapter != null) {
                    homeworkAdapter.clearData();
                    subscribe();
                }
                break;
            case R.id.next:
                today_date = getNextDate(today_date);
                todayDate.setText(today_date);
                if (homeworkAdapter != null) {
                    homeworkAdapter.clearData();
                    subscribe();
                }
                break;
        }
    }

    @OnClick(R.id.today_date)
    public void onTodayDateClicked() {
        final Calendar c = Calendar.getInstance();
        int mYear, mMonth, mDay;
        String currentDate = todayDate.getText().toString();

        /* Setting the previous date selected in calendar */
        if (currentDate.length() > 0) {
            String[] data = currentDate.split("-");
            mYear = Integer.parseInt(data[0]);
            mMonth = Integer.parseInt(data[1]);
            mMonth = mMonth - 1;
            mDay = Integer.parseInt(data[2]);
        } else {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                (view1, year, monthOfYear, dayOfMonth) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(0);
                    cal.set(year, monthOfYear, dayOfMonth);
                    Date chosenDate = cal.getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    today_date = dateFormat.format(chosenDate);
                    todayDate.setText(today_date);
                    if (homeworkAdapter != null) {
                        homeworkAdapter.clearData();
                        subscribe();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}