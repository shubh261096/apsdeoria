package com.pb.apszone.view.fragment;

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
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.SyllabusItem;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.SyllabusAdapter;
import com.pb.apszone.view.receiver.DownloadBroadcastReceiver;
import com.pb.apszone.viewModel.SyllabusFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_DOWNLOAD_ID;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_CLASS_ID;
import static com.pb.apszone.utils.CommonUtils.beginDownload;

public class SyllabusFragment extends BaseFragment implements SyllabusAdapter.OnDownloadItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.toolbar_syllabus)
    Toolbar toolbarSyllabus;
    @BindView(R.id.rvSyllabus)
    RecyclerView rvSyllabus;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<SyllabusItem> syllabusItemList;
    SyllabusFragmentViewModel syllabusFragmentViewModel;
    KeyStorePref keyStorePref;
    SyllabusAdapter syllabusAdapter;
    DownloadBroadcastReceiver downloadBroadcastReceiver;

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
        syllabusAdapter = new SyllabusAdapter(syllabusItemList, getActivity(), this);
        rvSyllabus.setAdapter(syllabusAdapter);
        toolbarSyllabus.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        downloadBroadcastReceiver = new DownloadBroadcastReceiver();
        Objects.requireNonNull(getContext()).registerReceiver(downloadBroadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        syllabusFragmentViewModel = ViewModelProviders.of(this).get(SyllabusFragmentViewModel.class);
        syllabusFragmentViewModel.getSyllabus().observe(this, syllabusResponseModel -> {
            if (syllabusResponseModel != null) {
                progressBar.setVisibility(View.GONE);

                if (syllabusAdapter != null) {
                    syllabusAdapter.clearData();
                }

                if (!syllabusResponseModel.isError()) {
                    List<SyllabusItem> syllabusItems = syllabusResponseModel.getSyllabus();
                    syllabusItemList.addAll(syllabusItems);
                    syllabusAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), syllabusResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onItemClick(int position, View view) {
        if (!TextUtils.isEmpty(syllabusItemList.get(position).getSyllabus())) {
            if (URLUtil.isValidUrl(syllabusItemList.get(position).getSyllabus())) {
                KEY_DOWNLOAD_ID = beginDownload(syllabusItemList.get(position).getSyllabus(), getContext());
            } else {
                Toast.makeText(getContext(), "Not a valid URL", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
