package com.pb.apszone.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.ClassSubjectItem;
import com.pb.apszone.service.model.ClassSubjectResponseModel;
import com.pb.apszone.service.model.CommonResponseModel;
import com.pb.apszone.service.model.SubjectId;
import com.pb.apszone.utils.CommonUtils;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.TeacherSyllabusAdapter;
import com.pb.apszone.view.receiver.DownloadBroadcastReceiver;
import com.pb.apszone.view.ui.RemotePDFActivity;
import com.pb.apszone.viewModel.SyllabusTeacherFragmentViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.pb.apszone.utils.AppConstants.KEY_DOWNLOAD_ID;
import static com.pb.apszone.utils.AppConstants.KEY_TEACHER_ID;
import static com.pb.apszone.utils.AppConstants.READ_EXTERNAL_STORAGE_CODE;
import static com.pb.apszone.utils.CommonUtils.beginDownload;
import static com.pb.apszone.utils.CommonUtils.getUriRealPath;
import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.isReadStoragePermissionGranted;
import static com.pb.apszone.utils.CommonUtils.isValidFileType;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;
import static com.pb.apszone.utils.CommonUtils.showProgress;

public class SyllabusTeacherFragment extends BaseFragment implements TeacherSyllabusAdapter.OnSubjectItemClick {

    private static final int PDF_REQ_CODE = 901;
    private static final String TAG = "SyllabusTeacherFragment";
    Unbinder unbinder;
    @BindView(R.id.toolbar_syllabus)
    Toolbar toolbarSyllabus;
    @BindView(R.id.tvClass)
    TextView tvClass;
    @BindView(R.id.rvSubject)
    RecyclerView rvSubject;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<ClassSubjectItem> classSubjectItemList;
    private List<SubjectId> subjectIdList;
    SyllabusTeacherFragmentViewModel syllabusTeacherFragmentViewModel;
    KeyStorePref keyStorePref;
    TeacherSyllabusAdapter teacherSyllabusAdapter;
    private String[] class_name;
    private String[] class_id;
    private String subjectId, classId;
    private int classPos = 0;
    private boolean isPDFSelected;
    DownloadBroadcastReceiver downloadBroadcastReceiver;

    public SyllabusTeacherFragment() {
        // Required empty public constructor
    }

    public static SyllabusTeacherFragment newInstance() {
        return new SyllabusTeacherFragment();
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
        View view = inflater.inflate(R.layout.fragment_teacher_syllabus, container, false);
        unbinder = ButterKnife.bind(this, view);
        classSubjectItemList = new ArrayList<>();
        subjectIdList = new ArrayList<>();
        rvSubject.setLayoutManager(new LinearLayoutManager(getActivity()));
        teacherSyllabusAdapter = new TeacherSyllabusAdapter(subjectIdList, this);
        rvSubject.setAdapter(teacherSyllabusAdapter);
        toolbarSyllabus.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        // Registering download broadcast receiver
        downloadBroadcastReceiver = new DownloadBroadcastReceiver();
        Objects.requireNonNull(getContext()).registerReceiver(downloadBroadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        syllabusTeacherFragmentViewModel = ViewModelProviders.of(this).get(SyllabusTeacherFragmentViewModel.class);
        observeHomework();
        observeSyllabus();
    }

    private void observeSyllabus() {
        syllabusTeacherFragmentViewModel.checkResponse().observe(this, responseEvent -> {
            hideProgress();
            if (responseEvent != null) {
                if (responseEvent.isSuccess()) {
                    CommonResponseModel commonResponseModel = responseEvent.getCommonResponseModel();
                    Toast.makeText(getContext(), commonResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    clearAndSubscribe();
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    private void observeHomework() {
        syllabusTeacherFragmentViewModel.getClassSubjectDetail().observe(this, responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                clearData();
                if (responseEvent.isSuccess()) {
                    ClassSubjectResponseModel classSubjectResponseModel = responseEvent.getClassSubjectResponseModel();
                    if (!classSubjectResponseModel.isError()) {
                        List<ClassSubjectItem> classSubjectItems = classSubjectResponseModel.getClassSubject();
                        classSubjectItemList.addAll(classSubjectItems);
                        subjectIdList.addAll(classSubjectItems.get(this.classPos).getClassId().getSubjectId());

                        /* Class name String array is get with size of response class_detail  */
                        class_name = new String[classSubjectItemList.size()];
                        class_id = new String[classSubjectItemList.size()];
                        for (int i = 0; i < classSubjectItemList.size(); i++) {
                            class_name[i] = classSubjectItemList.get(i).getClassId().getName();
                            class_id[i] = classSubjectItemList.get(i).getClassId().getId();
                        }
                        this.classId = class_id[this.classPos];
                        tvClass.setText(class_name[this.classPos]);
                        teacherSyllabusAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), classSubjectResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    @Override
    public void getNetworkData(boolean status) {
        if (status && !isPDFSelected) {
            clearData();
            subscribe();
        }
    }

    private void subscribe() {
        if (!TextUtils.isEmpty(keyStorePref.getString(KEY_TEACHER_ID))) {
            syllabusTeacherFragmentViewModel.sendClassDetailRequest(keyStorePref.getString(KEY_TEACHER_ID));
        }
        progressBar.setVisibility(View.VISIBLE);
    }


    private void subscribeUpdateSyllabus(File file, String subject_id, String subject_description) {
        syllabusTeacherFragmentViewModel.updateRequest(file, subject_id, subject_description);
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

    @OnClick(R.id.tvClass)
    public void onClassViewClicked() {
        if (class_name != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.select_class));
            builder.setItems(class_name, (dialog, which) -> {
                tvClass.setText(class_name[which]);
                this.classPos = which;
                this.classId = class_id[this.classPos];

                /* Since all the data is fetch at start so just adding the data and notifying the adapter of selected position */
                if (teacherSyllabusAdapter != null) {
                    teacherSyllabusAdapter.clearData();
                }
                subjectIdList.addAll(classSubjectItemList.get(this.classPos).getClassId().getSubjectId());
                teacherSyllabusAdapter.notifyDataSetChanged();

            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void clearData() {
        if (classSubjectItemList != null) {
            classSubjectItemList.clear();
        }
        if (teacherSyllabusAdapter != null) {
            teacherSyllabusAdapter.clearData();
        }
    }

    @Override
    public void onDownloadClick(int position, View view) {
        if (URLUtil.isValidUrl(classSubjectItemList.get(this.classPos).getClassId().getSubjectId().get(position).getSyllabus())) {
            KEY_DOWNLOAD_ID = beginDownload(classSubjectItemList.get(this.classPos).getClassId().getSubjectId().get(position).getSyllabus(), getContext());
        } else {
            Toast.makeText(getContext(), getString(R.string.msg_invalid_url), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUploadClick(int position, View view) {
        this.subjectId = classSubjectItemList.get(this.classPos).getClassId().getSubjectId().get(position).getId();
        if (isReadStoragePermissionGranted(getContext())) {
            openPDFIntent();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
        }
    }

    @Override
    public void onViewClick(int position, View view) {
        if (URLUtil.isValidUrl(classSubjectItemList.get(this.classPos).getClassId().getSubjectId().get(position).getSyllabus())) {
            RemotePDFActivity.launchForUrl(getContext(), classSubjectItemList.get(this.classPos).getClassId().getSubjectId().get(position).getName(), classSubjectItemList.get(this.classPos).getClassId().getSubjectId().get(position).getSyllabus());
        } else {
            Toast.makeText(getContext(), getString(R.string.msg_invalid_url), Toast.LENGTH_SHORT).show();
        }
    }

    private void clearAndSubscribe() {
        isPDFSelected = false;
        clearData();
        subscribe();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: Permission Granted");
                openPDFIntent();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Log.i(TAG, "onRequestPermissionsResult: Permission Denied");
                if (!ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    CommonUtils.showPermissionDeniedDialog(getActivity());
                }
            }
        }
    }

    private void openPDFIntent() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.msg_select_pdf)), PDF_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (uri != null) {
                if (isValidFileType(Objects.requireNonNull(uri.getPath()))) {
                    isPDFSelected = true;
                    File file = new File(Objects.requireNonNull(getUriRealPath(getActivity(), uri)));
                    showAddSyllabusDescriptionAlertDialog(file, this.subjectId);
                } else {
                    Toast.makeText(getContext(), getString(R.string.msg_invalid_pdf), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showAddSyllabusDescriptionAlertDialog(File file, String subjectId) {
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_add_syllabus_description, null, false);

        TextInputLayout tilDescription = dialogView.findViewById(R.id.til_description);
        EditText description = dialogView.findViewById(R.id.description);
        Button addSyllabus = dialogView.findViewById(R.id.add_syllabus);

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (!TextUtils.isEmpty(text)) {
                    if (tilDescription.isErrorEnabled()) {
                        tilDescription.setErrorEnabled(false);
                        tilDescription.setHelperTextEnabled(true);
                    } else {
                        tilDescription.setHelperTextEnabled(true);
                    }
                } else {
                    tilDescription.setError(getString(R.string.msg_description_required));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog);
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        addSyllabus.setOnClickListener(v -> {
            if (TextUtils.isEmpty(Objects.requireNonNull(tilDescription.getEditText()).getText().toString().trim())) {
                tilDescription.setError(getString(R.string.msg_description_required));
                return;
            }

            String subject_description = tilDescription.getEditText().getText().toString().trim();
            alertDialog.dismiss();
            showProgress(getContext(), getString(R.string.msg_wait_upload_syllabus));
            subscribeUpdateSyllabus(file, subjectId, subject_description);
        });
        alertDialog.show();
    }
}
