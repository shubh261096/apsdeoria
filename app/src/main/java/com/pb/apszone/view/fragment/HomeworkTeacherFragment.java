package com.pb.apszone.view.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.ClassSubjectItem;
import com.pb.apszone.service.model.SubjectId;
import com.pb.apszone.utils.CommonUtils;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.TeacherHomeworkAdapter;
import com.pb.apszone.viewModel.HomeworkTeacherFragmentViewModel;
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
import static com.pb.apszone.utils.AppConstants.KEY_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.KEY_DASHBOARD_ELEMENT_NAME;
import static com.pb.apszone.utils.AppConstants.KEY_SUBJECT_ID;
import static com.pb.apszone.utils.AppConstants.KEY_SUBJECT_NAME;
import static com.pb.apszone.utils.AppConstants.KEY_TEACHER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.READ_EXTERNAL_STORAGE_CODE;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_HOMEWORK;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_SYLLABUS;
import static com.pb.apszone.utils.CommonUtils.getUriRealPath;
import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.isReadStoragePermissionGranted;
import static com.pb.apszone.utils.CommonUtils.isValidFileType;
import static com.pb.apszone.utils.CommonUtils.showProgress;

public class HomeworkTeacherFragment extends BaseFragment implements TeacherHomeworkAdapter.OnSubjectItemClick {

    private static final int PDF_REQ_CODE = 901;
    private static final String TAG = "HomeworkTeacherFragment";
    Unbinder unbinder;
    @BindView(R.id.toolbar_homework)
    Toolbar toolbarHomework;
    @BindView(R.id.tvClass)
    TextView tvClass;
    @BindView(R.id.rvSubject)
    RecyclerView rvSubject;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<ClassSubjectItem> classSubjectItemList;
    private List<SubjectId> subjectIdList;
    HomeworkTeacherFragmentViewModel homeworkTeacherFragmentViewModel;
    SyllabusTeacherFragmentViewModel syllabusTeacherFragmentViewModel;
    KeyStorePref keyStorePref;
    TeacherHomeworkAdapter teacherHomeworkAdapter;
    private String[] class_name;
    private String[] class_id;
    private String classId, subjectId;
    private int classPos = 0;
    private String dashboard_element_name;
    private boolean isPDFSelected;

    public HomeworkTeacherFragment() {
        // Required empty public constructor
    }

    public static HomeworkTeacherFragment newInstance() {
        return new HomeworkTeacherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyStorePref = KeyStorePref.getInstance(getContext());
        if (getArguments() != null) {
            dashboard_element_name = getArguments().getString(KEY_DASHBOARD_ELEMENT_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_homework, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (TextUtils.equals(dashboard_element_name, UI_ELEMENT_HOMEWORK)) {
            toolbarHomework.setTitle(Objects.requireNonNull(getContext()).getResources().getString(R.string.toolbar_homework));
        } else if (TextUtils.equals(dashboard_element_name, UI_ELEMENT_SYLLABUS)) {
            toolbarHomework.setTitle(Objects.requireNonNull(getContext()).getResources().getString(R.string.toolbar_syllabus));
        }
        classSubjectItemList = new ArrayList<>();
        subjectIdList = new ArrayList<>();
        rvSubject.setLayoutManager(new LinearLayoutManager(getActivity()));
        teacherHomeworkAdapter = new TeacherHomeworkAdapter(subjectIdList, this, dashboard_element_name);
        rvSubject.setAdapter(teacherHomeworkAdapter);
        toolbarHomework.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        homeworkTeacherFragmentViewModel = ViewModelProviders.of(this).get(HomeworkTeacherFragmentViewModel.class);
        observeHomework();
        if (TextUtils.equals(dashboard_element_name, UI_ELEMENT_SYLLABUS)) {
            syllabusTeacherFragmentViewModel = ViewModelProviders.of(this).get(SyllabusTeacherFragmentViewModel.class);
            observeSyllabus();
        }
    }

    private void observeSyllabus() {
        syllabusTeacherFragmentViewModel.checkResponse().observe(this, commonResponseModel -> {
            if (commonResponseModel != null) {
                if (commonResponseModel.isError()) {
                    Toast.makeText(getContext(), commonResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    if (isPDFSelected) {
                        isPDFSelected = false;
                        clearData();
                        subscribe();
                        hideProgress();
                        Toast.makeText(getContext(), commonResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        openPDFIntent();
                    }
                }
            }
        });
    }

    private void observeHomework() {
        homeworkTeacherFragmentViewModel.getClassSubjectDetail().observe(this, classSubjectResponseModel -> {
            if (classSubjectResponseModel != null) {
                progressBar.setVisibility(View.GONE);

                clearData();

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
                    classId = class_id[this.classPos];
                    tvClass.setText(class_name[this.classPos]);
                    teacherHomeworkAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), classSubjectResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
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
            homeworkTeacherFragmentViewModel.sendRequest(keyStorePref.getString(KEY_TEACHER_ID));
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    private void subscribeSyllabus(String subject_id) {
        syllabusTeacherFragmentViewModel.sendRequest(subject_id);
    }

    private void subscribeUpdateSyllabus(File file, String subject_id) {
        syllabusTeacherFragmentViewModel.updateRequest(file, subject_id);
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
                if (teacherHomeworkAdapter != null) {
                    teacherHomeworkAdapter.clearData();
                }
                subjectIdList.addAll(classSubjectItemList.get(this.classPos).getClassId().getSubjectId());
                teacherHomeworkAdapter.notifyDataSetChanged();

            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void clearData() {
        if (classSubjectItemList != null) {
            classSubjectItemList.clear();
        }
        if (teacherHomeworkAdapter != null) {
            teacherHomeworkAdapter.clearData();
        }
    }

    @Override
    public void onItemClick(int position, View view) {
        if (TextUtils.equals(dashboard_element_name, UI_ELEMENT_HOMEWORK)) {
            AddHomeworkFragment nextFrag = new AddHomeworkFragment();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_TEACHER_ID, keyStorePref.getString(KEY_USER_ID));
            bundle.putString(KEY_CLASS_ID, this.classId);
            bundle.putString(KEY_SUBJECT_ID, classSubjectItemList.get(this.classPos).getClassId().getSubjectId().get(position).getId());
            bundle.putString(KEY_SUBJECT_NAME, classSubjectItemList.get(this.classPos).getClassId().getSubjectId().get(position).getName());
            nextFrag.setArguments(bundle);
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_frame_layout, nextFrag, AddHomeworkFragment.class.getSimpleName())
                    .addToBackStack(null)
                    .commit();
        } else if (TextUtils.equals(dashboard_element_name, UI_ELEMENT_SYLLABUS)) {
            this.subjectId = classSubjectItemList.get(this.classPos).getClassId().getSubjectId().get(position).getId();
            if (isReadStoragePermissionGranted(getContext())) {
                subscribeSyllabus(this.subjectId);
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: Permission Granted");
                subscribeSyllabus(this.subjectId);
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
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (uri != null) {
                if (isValidFileType(Objects.requireNonNull(uri.getPath()))) {
                    showProgress(getContext(), "Please wait while we upload the syllabus.");
                    isPDFSelected = true;
                    File file = new File(Objects.requireNonNull(getUriRealPath(getActivity(), uri)));
                    subscribeUpdateSyllabus(file, this.subjectId);
                } else {
                    Toast.makeText(getContext(), "Please select a valid pdf file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}