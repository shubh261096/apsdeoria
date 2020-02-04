package com.pb.apszone.view.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.AttendanceItem;
import com.pb.apszone.service.model.ClassDetailItem;
import com.pb.apszone.service.model.ClassDetailResponseModel;
import com.pb.apszone.service.model.StudentsItem;
import com.pb.apszone.service.model.SubmitAttendanceResponseModel;
import com.pb.apszone.service.rest.model.SubmitAttendanceRequestModel;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.TeacherAttendanceAdapter;
import com.pb.apszone.view.listener.OnCheckBoxCheckedListener;
import com.pb.apszone.viewModel.AttendanceTeacherFragmentViewModel;

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

import static com.pb.apszone.utils.AppConstants.KEY_TEACHER_ID;
import static com.pb.apszone.utils.AppConstants.Numbers.ONE;
import static com.pb.apszone.utils.AppConstants.Numbers.ZERO;
import static com.pb.apszone.utils.CommonUtils.getTodayDate;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;

public class AttendanceTeacherFragment extends BaseFragment implements OnCheckBoxCheckedListener {

    Unbinder unbinder;
    @BindView(R.id.toolbar_attendance)
    Toolbar toolbarAttendance;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvClass)
    TextView tvClass;
    @BindView(R.id.rvAttendance)
    RecyclerView rvAttendance;
    @BindView(R.id.date_filter)
    TextView dateFilter;
    @BindView(R.id.edit_attendance)
    TextView editAttendance;
    @BindView(R.id.submit_attendance)
    Button submitAttendance;
    @BindView(R.id.llAttendance)
    LinearLayout llAttendance;
    @BindView(R.id.no_data)
    TextView tvNoData;
    private List<ClassDetailItem> classDetailItemList = new ArrayList<>();
    private List<StudentsItem> studentsItemList = new ArrayList<>();
    private AttendanceTeacherFragmentViewModel attendanceTeacherFragmentViewModel;
    KeyStorePref keyStorePref;
    private String[] class_name;
    private TeacherAttendanceAdapter teacherAttendanceAdapter;
    private int classPos = 0;
    private String today_date;
    private boolean isEdit;

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
        rvAttendance.setLayoutManager(new LinearLayoutManager(getActivity()));
        teacherAttendanceAdapter = new TeacherAttendanceAdapter(studentsItemList, getActivity(), this);
        rvAttendance.setAdapter(teacherAttendanceAdapter);
        today_date = getTodayDate();
        dateFilter.setText(today_date);
        toolbarAttendance.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        attendanceTeacherFragmentViewModel = ViewModelProviders.of(this).get(AttendanceTeacherFragmentViewModel.class);
        observeAttendance();
        observeSubmitAttendance();
    }

    private void observeAttendance() {
        attendanceTeacherFragmentViewModel.getClassDetail().observe(this, responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                clearData();

                if (responseEvent.isSuccess()) {
                    ClassDetailResponseModel classDetailResponseModel = responseEvent.getClassDetailResponseModel();
                    if (!classDetailResponseModel.isError()) {
                        List<ClassDetailItem> classDetailItems = classDetailResponseModel.getClassDetail();
                        classDetailItemList.addAll(classDetailItems);

                        if (classDetailItems.get(this.classPos).getClassId().getStudents() != null) {

                            // Add attendance item value here if attendance is null
                            for (int i = 0; i < classDetailItems.get(this.classPos).getClassId().getStudents().size(); i++) {
                                if (classDetailItems.get(this.classPos).getClassId().getStudents().get(i).getAttendance() == null) {
                                    AttendanceItem attendanceItem = new AttendanceItem();
                                    attendanceItem.setTimetableId(classDetailItems.get(this.classPos).getTimetableId());
                                    attendanceItem.setStudentId(classDetailItems.get(this.classPos).getClassId().getStudents().get(i).getId());
                                    attendanceItem.setDate(this.today_date);
                                    attendanceItem.setStatus(ZERO);
                                    attendanceItem.setRemarks(getString(R.string.msg_absent));
                                    classDetailItems.get(this.classPos).getClassId().getStudents().get(i).setAttendance(attendanceItem);
                                    updateUI(false, true);
                                    isEdit = false;
                                } else {
                                    updateUI(true, false);
                                    isEdit = true;
                                }
                            }
                            studentsItemList.addAll(classDetailItems.get(this.classPos).getClassId().getStudents());
                        } else {
                            Toast.makeText(getContext(), getString(R.string.msg_no_student_found), Toast.LENGTH_SHORT).show();
                        }
                        teacherAttendanceAdapter.notifyDataSetChanged();

                        /* Class name String array is get with size of response class_detail  */
                        class_name = new String[classDetailItemList.size()];
                        for (int i = 0; i < classDetailItemList.size(); i++) {
                            class_name[i] = classDetailItemList.get(i).getClassId().getName();
                        }
                        tvClass.setText(class_name[this.classPos]);
                    } else {
                        tvClass.setText(null);
                        class_name = null;
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    private void observeSubmitAttendance() {
        attendanceTeacherFragmentViewModel.getSubmitResponse().observe(this, responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                clearData();

                if (responseEvent.isSuccess()) {
                    SubmitAttendanceResponseModel submitAttendanceResponseModel = responseEvent.getSubmitAttendanceResponseModel();
                    Toast.makeText(getContext(), submitAttendanceResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    if (!submitAttendanceResponseModel.isError()) {
                        subscribe();
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
            clearData();
            subscribe();
        }
    }

    private void subscribe() {
        if (!TextUtils.isEmpty(keyStorePref.getString(KEY_TEACHER_ID))) {
            attendanceTeacherFragmentViewModel.sendRequest(keyStorePref.getString(KEY_TEACHER_ID), this.today_date);
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
    }

    @OnClick(R.id.tvClass)
    public void onClassViewClicked() {
        if (class_name != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.select_class));
            builder.setItems(class_name, (dialog, which) -> {
                tvClass.setText(class_name[which]);
                this.classPos = which;
                clearData();
                subscribe();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @OnClick(R.id.date_filter)
    public void onDateFilterClicked() {
        final Calendar c = Calendar.getInstance();
        int mYear, mMonth, mDay;
        String currentDate = dateFilter.getText().toString();

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
                    dateFilter.setText(today_date);
                    clearData();
                    this.classPos = 0;
                    subscribe();
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @OnClick(R.id.edit_attendance)
    public void onEditAttendanceClicked() {
        updateUI(false, true);
    }

    @OnClick(R.id.submit_attendance)
    public void onSubmitAttendanceClicked() {
        List<AttendanceItem> attendanceItems = new ArrayList<>();
        for (int i = 0; i < studentsItemList.size(); i++) {
            attendanceItems.add(i, classDetailItemList.get(classPos).getClassId().getStudents().get(i).getAttendance());
        }
        SubmitAttendanceRequestModel submitAttendanceRequestModel = new SubmitAttendanceRequestModel();
        submitAttendanceRequestModel.setAttendance(attendanceItems);
        progressBar.setVisibility(View.VISIBLE);
        if (isEdit) {
            attendanceTeacherFragmentViewModel.editAttendanceRequest(submitAttendanceRequestModel);
        } else {
            attendanceTeacherFragmentViewModel.addAttendanceRequest(submitAttendanceRequestModel);
        }
    }

    @Override
    public void onItemChecked(int position, boolean isChecked) {
        if (studentsItemList.get(position).getAttendance() != null) {
            if (isChecked) {
                studentsItemList.get(position).getAttendance().setStatus(ONE);
                studentsItemList.get(position).getAttendance().setRemarks(getString(R.string.msg_present));
            } else {
                studentsItemList.get(position).getAttendance().setStatus(ZERO);
                studentsItemList.get(position).getAttendance().setRemarks(getString(R.string.msg_absent));
            }
            teacherAttendanceAdapter.notifyDataSetChanged();
        }
    }


    private void clearData() {
        updateUI(false, false);
        if (classDetailItemList != null) {
            classDetailItemList.clear();
        }
        if (teacherAttendanceAdapter != null) {
            teacherAttendanceAdapter.clearData();
        }
    }

    private void updateUI(boolean llAttendanceVisibility, boolean submitAttendanceVisibility) {
        if (llAttendanceVisibility) {
            llAttendance.setVisibility(View.VISIBLE);
        } else {
            llAttendance.setVisibility(View.GONE);
        }
        if (submitAttendanceVisibility) {
            submitAttendance.setVisibility(View.VISIBLE);
        } else {
            submitAttendance.setVisibility(View.GONE);
        }
    }
}
