package com.pb.apszone.view.fragment;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.TimetableItem;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.StudentTimetableAdapter;
import com.pb.apszone.viewModel.StudentTimetableFragmentViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_FILTER_BY_DAY;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.KEY_TEACHER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_PARENT;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_TEACHER;
import static com.pb.apszone.utils.CommonUtils.getDayOfWeek;

public class StudentTimetableFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    Unbinder unbinder;
    @BindView(R.id.toolbar_timetable)
    Toolbar toolbarTimetable;
    @BindView(R.id.rvTimetable)
    RecyclerView rvTimetable;
    @BindView(R.id.spinnerDay)
    Spinner spinnerDay;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<TimetableItem> timetableItemList;
    StudentTimetableFragmentViewModel studentTimetableFragmentViewModel;
    KeyStorePref keyStorePref;
    StudentTimetableAdapter studentTimetableAdapter;
    private String day;
    private int checkInit = 0;
    private String user_type;

    public StudentTimetableFragment() {
        // Required empty public constructor
    }

    public static StudentTimetableFragment newInstance() {
        return new StudentTimetableFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyStorePref = KeyStorePref.getInstance(getContext());
        user_type = keyStorePref.getString(KEY_USER_TYPE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable_student, container, false);
        unbinder = ButterKnife.bind(this, view);
        timetableItemList = new ArrayList<>();
        rvTimetable.setLayoutManager(new LinearLayoutManager(getActivity()));
        studentTimetableAdapter = new StudentTimetableAdapter(timetableItemList, getActivity(), user_type);
        rvTimetable.setAdapter(studentTimetableAdapter);
        toolbarTimetable.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        day = getDayOfWeek();
        setUpSpinner();
        return view;
    }

    private void setUpSpinner() {
        String[] days = getResources().getStringArray(R.array.day);
        ArrayAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item, days);
        int pos = new ArrayList<>(Arrays.asList(days)).indexOf(day); // Getting current position by day
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerDay.setAdapter(adapter);
        spinnerDay.setSelection(pos); // Setting current day
        spinnerDay.setOnItemSelectedListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        studentTimetableFragmentViewModel = ViewModelProviders.of(this).get(StudentTimetableFragmentViewModel.class);
    }

    @Override
    public void getNetworkData(boolean status) {
        if (status) {
            if (studentTimetableAdapter != null) {
                studentTimetableAdapter.clearData();
            }
            subscribe();
        }
    }

    private void subscribe() {
        if (!TextUtils.isEmpty(user_type)) {
            if (TextUtils.equals(user_type, USER_TYPE_PARENT)) {
                if (!TextUtils.isEmpty(keyStorePref.getString(KEY_STUDENT_CLASS_ID))) {
                    studentTimetableFragmentViewModel.sendRequest(keyStorePref.getString(KEY_STUDENT_CLASS_ID), day);
                }
            } else if (TextUtils.equals(user_type, USER_TYPE_TEACHER)) {
                if (!TextUtils.isEmpty(keyStorePref.getString(KEY_TEACHER_ID))) {
                    studentTimetableFragmentViewModel.sendTeacherRequest(keyStorePref.getString(KEY_TEACHER_ID), day);
                }
            }
            progressBar.setVisibility(View.VISIBLE);
            studentTimetableFragmentViewModel.getTimetable(KEY_FILTER_BY_DAY, user_type).observe(this, timetableResponseModel -> {
                if (timetableResponseModel != null) {
                    progressBar.setVisibility(View.GONE);

                    if (studentTimetableAdapter != null) {
                        studentTimetableAdapter.clearData();
                    }

                    if (!timetableResponseModel.isError()) {
                        List<TimetableItem> timetableItems = timetableResponseModel.getTimetable();
                        timetableItemList.addAll(timetableItems);
                        studentTimetableAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), timetableResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        checkInit++;
        if (checkInit > 1) {
            day = parent.getItemAtPosition(position).toString();
            if (studentTimetableAdapter != null) {
                studentTimetableAdapter.clearData();
                subscribe();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
