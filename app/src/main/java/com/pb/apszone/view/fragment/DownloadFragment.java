package com.pb.apszone.view.fragment;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.DownloadItem;
import com.pb.apszone.service.model.DownloadResponseModel;
import com.pb.apszone.utils.CommonUtils;
import com.pb.apszone.view.adapter.DownloadAdapter;
import com.pb.apszone.view.receiver.DownloadBroadcastReceiver;
import com.pb.apszone.viewModel.DownloadFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_DOWNLOAD_ID;
import static com.pb.apszone.utils.AppConstants.WRITE_EXTERNAL_STORAGE_CODE;
import static com.pb.apszone.utils.CommonUtils.beginDownload;
import static com.pb.apszone.utils.CommonUtils.isWriteStoragePermissionGranted;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;

public class DownloadFragment extends BaseFragment implements DownloadAdapter.OnDownloadItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.toolbar_download)
    Toolbar toolbarDownload;
    @BindView(R.id.rvDownload)
    RecyclerView rvDownload;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView tvNoData;
    private List<DownloadItem> downloadItemList;
    private DownloadFragmentViewModel downloadFragmentViewModel;
    private DownloadAdapter downloadAdapter;
    private DownloadBroadcastReceiver downloadBroadcastReceiver;
    private int itemPosition;

    public DownloadFragment() {
        // Required empty public constructor
    }

    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        unbinder = ButterKnife.bind(this, view);
        downloadItemList = new ArrayList<>();
        rvDownload.setLayoutManager(new LinearLayoutManager(getActivity()));
        downloadAdapter = new DownloadAdapter(downloadItemList, this);
        rvDownload.setAdapter(downloadAdapter);
        toolbarDownload.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        downloadBroadcastReceiver = new DownloadBroadcastReceiver();
        Objects.requireNonNull(getContext()).registerReceiver(downloadBroadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        downloadFragmentViewModel = ViewModelProviders.of(this).get(DownloadFragmentViewModel.class);
        observerDownloads();
    }

    private void observerDownloads() {
        downloadFragmentViewModel.getDownloads().observe(this, responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                if (downloadAdapter != null) {
                    downloadAdapter.clearData();
                }
                if (responseEvent.isSuccess()) {
                    DownloadResponseModel downloadResponseModel = responseEvent.getDownloadResponseModel();
                    if (!downloadResponseModel.isError()) {
                        List<DownloadItem> downloadItems = downloadResponseModel.getDownload();
                        downloadItemList.addAll(downloadItems);
                        downloadAdapter.notifyDataSetChanged();
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
            if (downloadAdapter != null) {
                downloadAdapter.clearData();
            }
            subscribe();
        }
    }

    private void subscribe() {
        downloadFragmentViewModel.sendRequest();
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
        if (!TextUtils.isEmpty(downloadItemList.get(this.itemPosition).getUrl())) {
            if (URLUtil.isValidUrl(downloadItemList.get(this.itemPosition).getUrl())) {
                KEY_DOWNLOAD_ID = beginDownload(downloadItemList.get(this.itemPosition).getUrl(), Objects.requireNonNull(getContext()));
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
}
