package com.pb.apszone.view.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.ClassSubjectItem;
import com.pb.apszone.service.model.SubjectId;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.TeacherHomeworkAdapter;
import com.pb.apszone.viewModel.HomeworkTeacherFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.KEY_SUBJECT_ID;
import static com.pb.apszone.utils.AppConstants.KEY_TEACHER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;

public class HomeworkTeacherFragment extends BaseFragment implements TeacherHomeworkAdapter.OnSubjectItemClick {

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
    KeyStorePref keyStorePref;
    TeacherHomeworkAdapter teacherHomeworkAdapter;
    private String[] class_name;
    private String[] class_id;
    private String classId;
    private int classPos = 0;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_homework, container, false);
        unbinder = ButterKnife.bind(this, view);
        classSubjectItemList = new ArrayList<>();
        subjectIdList = new ArrayList<>();
        rvSubject.setLayoutManager(new LinearLayoutManager(getActivity()));
        teacherHomeworkAdapter = new TeacherHomeworkAdapter(subjectIdList, this);
        rvSubject.setAdapter(teacherHomeworkAdapter);
        toolbarHomework.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        homeworkTeacherFragmentViewModel = ViewModelProviders.of(this).get(HomeworkTeacherFragmentViewModel.class);
        observeHomework();
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
        if (status) {
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
        AddHomeworkFragment nextFrag = new AddHomeworkFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TEACHER_ID, keyStorePref.getString(KEY_USER_ID));
        bundle.putString(KEY_CLASS_ID, this.classId);
        bundle.putString(KEY_SUBJECT_ID, classSubjectItemList.get(this.classPos).getClassId().getSubjectId().get(position).getId());
        nextFrag.setArguments(bundle);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame_layout, nextFrag, AddHomeworkFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }
}