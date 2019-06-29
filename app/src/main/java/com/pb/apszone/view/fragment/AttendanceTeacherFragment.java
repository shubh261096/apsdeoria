package com.pb.apszone.view.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.ClassDetailItem;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.viewModel.AttendanceTeacherFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_TEACHER_ID;

public class AttendanceTeacherFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_attendance)
    Toolbar toolbarAttendance;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvClass)
    TextView tvClass;
    private List<ClassDetailItem> classDetailItemList = new ArrayList<>();
    AttendanceTeacherFragmentViewModel attendanceTeacherFragmentViewModel;
    KeyStorePref keyStorePref;
    String[] class_name;

    public AttendanceTeacherFragment() {
        // Required empty public constructor
    }

    public static AttendanceTeacherFragment newInstance() {
        return new AttendanceTeacherFragment();
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
        View view = inflater.inflate(R.layout.fragment_teacher_attendance, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarAttendance.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        attendanceTeacherFragmentViewModel = ViewModelProviders.of(this).get(AttendanceTeacherFragmentViewModel.class);
    }

    @Override
    public void getNetworkData(boolean status) {
        if (status) {
            if (classDetailItemList != null) {
                classDetailItemList.clear();
            }
            subscribe();
        }
    }

    private void subscribe() {
        if (!TextUtils.isEmpty(keyStorePref.getString(KEY_TEACHER_ID))) {
            attendanceTeacherFragmentViewModel.sendRequest(keyStorePref.getString(KEY_TEACHER_ID));
        }
        progressBar.setVisibility(View.VISIBLE);
        attendanceTeacherFragmentViewModel.getClassDetail().observe(this, classDetailResponseModel -> {
            if (classDetailResponseModel != null) {
                progressBar.setVisibility(View.GONE);

                if (classDetailItemList != null) {
                    classDetailItemList.clear();
                }

                if (!classDetailResponseModel.isError()) {
                    List<ClassDetailItem> classDetailItems = classDetailResponseModel.getClassDetail();
                    classDetailItemList.addAll(classDetailItems);
                    class_name = new String[classDetailItemList.size()];
                    for (int i = 0; i < classDetailItemList.size(); i++) {
                        class_name[i] = classDetailItemList.get(i).getClassId().getName();
                    }
                    tvClass.setText(class_name[0]);
                } else {
                    Toast.makeText(getActivity(), classDetailResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.select_class));
        builder.setItems(class_name, (dialog, which) -> tvClass.setText(class_name[which]));
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
