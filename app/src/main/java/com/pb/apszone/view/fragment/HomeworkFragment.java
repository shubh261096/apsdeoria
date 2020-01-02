package com.pb.apszone.view.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.HomeworkItem;
import com.pb.apszone.service.model.HomeworkResponseModel;
import com.pb.apszone.utils.CommonUtils;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.HomeworkAdapter;
import com.pb.apszone.view.receiver.DownloadBroadcastReceiver;
import com.pb.apszone.view.ui.RemotePDFActivity;
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
import static com.pb.apszone.utils.AppConstants.WRITE_EXTERNAL_STORAGE_CODE;
import static com.pb.apszone.utils.CommonUtils.beginDownload;
import static com.pb.apszone.utils.CommonUtils.getNextDate;
import static com.pb.apszone.utils.CommonUtils.getPreviousDate;
import static com.pb.apszone.utils.CommonUtils.getTodayDate;
import static com.pb.apszone.utils.CommonUtils.isWriteStoragePermissionGranted;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;

public class HomeworkFragment extends BaseFragment implements HomeworkAdapter.OnDownloadItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.toolbar_homework)
    Toolbar toolbarHomework;
    @BindView(R.id.rvHomework)
    RecyclerView rvHomework;
    @BindView(R.id.previous)
    ImageView previous;
    @BindView(R.id.today_date)
    TextView todayDate;
    @BindView(R.id.next)
    ImageView next;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView tvNoData;
    private List<HomeworkItem> homeworkItemList;
    private HomeworkFragmentViewModel homeworkFragmentViewModel;
    KeyStorePref keyStorePref;
    private HomeworkAdapter homeworkAdapter;
    private DownloadBroadcastReceiver downloadBroadcastReceiver;
    private String today_date;
    private int itemPosition;

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
        homeworkAdapter = new HomeworkAdapter(homeworkItemList, this);
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
        homeworkFragmentViewModel.getHomework().observe(this, responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                if (homeworkAdapter != null) {
                    homeworkAdapter.clearData();
                }

                if (responseEvent.isSuccess()) {
                    HomeworkResponseModel homeworkResponseModel = responseEvent.getHomeworkResponseModel();
                    if (!homeworkResponseModel.isError()) {
                        List<HomeworkItem> homeworkItems = homeworkResponseModel.getHomework();
                        homeworkItemList.addAll(homeworkItems);
                        homeworkAdapter.notifyDataSetChanged();
                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
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
        tvNoData.setVisibility(View.GONE);
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
    public void onDownloadItemClick(int position, View view) {
        if (isWriteStoragePermissionGranted(getContext())) {
            this.itemPosition = position;
            downloadSyllabus();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
        }
    }

    private void downloadSyllabus() {
        if (!TextUtils.isEmpty(homeworkItemList.get(this.itemPosition).getData())) {
            if (URLUtil.isValidUrl(homeworkItemList.get(this.itemPosition).getData())) {
                KEY_DOWNLOAD_ID = beginDownload(homeworkItemList.get(this.itemPosition).getData(), Objects.requireNonNull(getContext()));
            } else {
                Toast.makeText(getContext(), getString(R.string.msg_invalid_url), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadSyllabus();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    CommonUtils.showPermissionDeniedDialog(getActivity());
                }
            }
        }
    }


    @Override
    public void onViewItemClick(int position, View view) {
        if (!TextUtils.isEmpty(homeworkItemList.get(position).getData())) {
            if (URLUtil.isValidUrl(homeworkItemList.get(position).getData())) {
                RemotePDFActivity.launchForUrl(getContext(), homeworkItemList.get(position).getSubjectId().getName(), homeworkItemList.get(position).getData());
            } else {
                Toast.makeText(getContext(), getString(R.string.msg_invalid_url), Toast.LENGTH_SHORT).show();
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
    void onTodayDateClicked() {
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
