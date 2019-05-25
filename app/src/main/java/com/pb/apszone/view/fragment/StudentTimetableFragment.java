package com.pb.apszone.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.pb.apszone.R;
import com.pb.apszone.service.model.TimetableItem;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.StudentTimetableAdapter;
import com.pb.apszone.viewModel.StudentTimetableFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_FILTER_BY_DAY;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_CLASS_ID;
import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.showProgress;

public class StudentTimetableFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_timetable)
    Toolbar toolbarTimetable;
    @BindView(R.id.spinner_day)
    Spinner spinnerDay;
    @BindView(R.id.rvTimetable)
    RecyclerView rvTimetable;
    private List<TimetableItem> timetableItemList;
    StudentTimetableFragmentViewModel studentTimetableFragmentViewModel;
    KeyStorePref keyStorePref;
    StudentTimetableAdapter studentTimetableAdapter;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable_student, container, false);
        unbinder = ButterKnife.bind(this, view);
        timetableItemList = new ArrayList<>();
        rvTimetable.setLayoutManager(new LinearLayoutManager(getActivity()));
        studentTimetableAdapter = new StudentTimetableAdapter(timetableItemList, getActivity());
        rvTimetable.setAdapter(studentTimetableAdapter);
        toolbarTimetable.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        studentTimetableFragmentViewModel = ViewModelProviders.of(this).get(StudentTimetableFragmentViewModel.class);
        if (!TextUtils.isEmpty(keyStorePref.getString(KEY_STUDENT_CLASS_ID))) {
            studentTimetableFragmentViewModel.sendRequest(keyStorePref.getString(KEY_STUDENT_CLASS_ID), "Monday"); // TODO call method#getDayOfWeek()
        }
        showProgress(getActivity(), "Please wait...");
        subscribe();
    }

    private void subscribe() {
        studentTimetableFragmentViewModel.getTimetable(KEY_FILTER_BY_DAY).observe(this, timetableResponseModel -> {
            if (timetableResponseModel != null) {
                hideProgress();
                List<TimetableItem> timetableItems = timetableResponseModel.getTimetable();
                timetableItemList.addAll(timetableItems);
                studentTimetableAdapter.notifyDataSetChanged();
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

}
