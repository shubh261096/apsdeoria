package com.pb.apszone.view.fragment;

import android.Manifest;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.SyllabusItem;
import com.pb.apszone.service.model.SyllabusResponseModel;
import com.pb.apszone.utils.CommonUtils;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.SyllabusAdapter;
import com.pb.apszone.view.receiver.DownloadBroadcastReceiver;
import com.pb.apszone.view.ui.RemotePDFActivity;
import com.pb.apszone.viewModel.SyllabusFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_DOWNLOAD_ID;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.WRITE_EXTERNAL_STORAGE_CODE;
import static com.pb.apszone.utils.CommonUtils.beginDownload;
import static com.pb.apszone.utils.CommonUtils.isWriteStoragePermissionGranted;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;

public class SyllabusFragment extends BaseFragment implements SyllabusAdapter.OnDownloadItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.toolbar_syllabus)
    Toolbar toolbarSyllabus;
    @BindView(R.id.rvSyllabus)
    RecyclerView rvSyllabus;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<SyllabusItem> syllabusItemList;
    private SyllabusFragmentViewModel syllabusFragmentViewModel;
    KeyStorePref keyStorePref;
    private SyllabusAdapter syllabusAdapter;
    private DownloadBroadcastReceiver downloadBroadcastReceiver;
    private int itemPosition;

    public SyllabusFragment() {
        // Required empty public constructor
    }

    public static SyllabusFragment newInstance() {
        return new SyllabusFragment();
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
        View view = inflater.inflate(R.layout.fragment_syllabus, container, false);
        unbinder = ButterKnife.bind(this, view);
        syllabusItemList = new ArrayList<>();
        rvSyllabus.setLayoutManager(new LinearLayoutManager(getActivity()));
        syllabusAdapter = new SyllabusAdapter(syllabusItemList, this);
        rvSyllabus.setAdapter(syllabusAdapter);
        toolbarSyllabus.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        downloadBroadcastReceiver = new DownloadBroadcastReceiver();
        Objects.requireNonNull(getContext()).registerReceiver(downloadBroadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        syllabusFragmentViewModel = ViewModelProviders.of(this).get(SyllabusFragmentViewModel.class);
        syllabusFragmentViewModel.getSyllabus().observe(this, responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                if (syllabusAdapter != null) {
                    syllabusAdapter.clearData();
                }

                if (responseEvent.isSuccess()) {
                    SyllabusResponseModel syllabusResponseModel = responseEvent.getSyllabusResponseModel();
                    if (!syllabusResponseModel.isError()) {
                        List<SyllabusItem> syllabusItems = syllabusResponseModel.getSyllabus();
                        syllabusItemList.addAll(syllabusItems);
                        syllabusAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), syllabusResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
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
            if (syllabusAdapter != null) {
                syllabusAdapter.clearData();
            }
            subscribe();
        }
    }

    private void subscribe() {
        if (!TextUtils.isEmpty(keyStorePref.getString(KEY_STUDENT_CLASS_ID))) {
            syllabusFragmentViewModel.sendRequest(keyStorePref.getString(KEY_STUDENT_CLASS_ID));
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
    public void onDownloadItemClick(int position, View view) {
        if (isWriteStoragePermissionGranted(getContext())) {
            this.itemPosition = position;
            downloadSyllabus();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
        }
    }

    private void downloadSyllabus() {
        if (!TextUtils.isEmpty(syllabusItemList.get(this.itemPosition).getSyllabus())) {
            if (URLUtil.isValidUrl(syllabusItemList.get(this.itemPosition).getSyllabus())) {
                KEY_DOWNLOAD_ID = beginDownload(syllabusItemList.get(this.itemPosition).getSyllabus(), Objects.requireNonNull(getContext()));
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
        if (!TextUtils.isEmpty(syllabusItemList.get(position).getSyllabus())) {
            if (URLUtil.isValidUrl(syllabusItemList.get(position).getSyllabus())) {
                RemotePDFActivity.launchForUrl(getContext(), syllabusItemList.get(position).getName(), syllabusItemList.get(position).getSyllabus());
            } else {
                Toast.makeText(getContext(), getString(R.string.msg_invalid_url), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
