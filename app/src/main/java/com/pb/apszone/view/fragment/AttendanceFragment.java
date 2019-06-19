package com.pb.apszone.view.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.AttendanceItem;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.AttendanceAdapter;
import com.pb.apszone.viewModel.AttendanceFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_ID;
import static com.pb.apszone.utils.CommonUtils.getCurrentMonth;
import static com.pb.apszone.utils.CommonUtils.getCurrentYear;
import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.showProgress;

public class AttendanceFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.rvAttendanceUI)
    RecyclerView rvAttendanceUI;
    AttendanceAdapter attendanceAdapter;
    List<String> day;
    String currentMonth, currentYear;
    @BindView(R.id.textMonth)
    TextView textMonth;
    @BindView(R.id.textYear)
    TextView textYear;
    @BindView(R.id.rlMonth)
    LinearLayout rlMonth;
    AttendanceFragmentViewModel attendanceFragmentViewModel;
    KeyStorePref keyStorePref;
    @BindView(R.id.toolbar_profile)
    Toolbar toolbarProfile;
    List<AttendanceItem> attendanceItemList = new ArrayList<>();
    @BindView(R.id.card_monthly_attendance)
    CardView cardMonthlyAttendance;
    @BindView(R.id.card_calendar)
    CardView cardCalendar;
    @BindView(R.id.num_present)
    TextView numPresent;
    @BindView(R.id.num_absent)
    TextView numAbsent;
    private int presentCount, absentCount;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    public static AttendanceFragment newInstance() {
        return new AttendanceFragment();
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
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        unbinder = ButterKnife.bind(this, view);
        currentMonth = getCurrentMonth();
        currentYear = getCurrentYear();
        textMonth.setText(currentMonth);
        textYear.setText(currentYear);
        day = new ArrayList<>();
        rvAttendanceUI.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        toolbarProfile.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        attendanceFragmentViewModel = ViewModelProviders.of(this).get(AttendanceFragmentViewModel.class);
        subscribe();
    }

    private void subscribe() {
        if (!TextUtils.isEmpty(keyStorePref.getString(KEY_STUDENT_ID))) {
            attendanceFragmentViewModel.sendRequest(keyStorePref.getString(KEY_STUDENT_ID), currentMonth, currentYear);
            showProgress(getActivity(), "Please wait...");
            attendanceFragmentViewModel.getAttendance().observe(this, attendanceResponseModel -> {
                if (attendanceResponseModel != null) {
                    hideProgress();
                    if (!attendanceResponseModel.isError()) {
                        updateUI(1);
                        day = attendanceFragmentViewModel.setUpList(currentMonth, currentYear);
                        List<AttendanceItem> attendanceItems = attendanceResponseModel.getAttendance();
                        attendanceItemList.addAll(attendanceItems);
                        attendanceAdapter = new AttendanceAdapter(getActivity(), day, attendanceItemList);
                        rvAttendanceUI.setAdapter(attendanceAdapter);
                        attendanceAdapter.notifyDataSetChanged();
                        getPresentAbsentCount();
                        numPresent.setText(String.format(getString(R.string.present), presentCount));
                        numAbsent.setText(String.format(getString(R.string.absent), absentCount));
                    } else {
                        updateUI(0);
                        Toast.makeText(getActivity(), attendanceResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void updateUI(int visibility) {
        presentCount = 0;
        absentCount = 0;
        if (visibility == 0) {
            cardCalendar.setVisibility(View.GONE);
            cardMonthlyAttendance.setVisibility(View.GONE);
        } else {
            cardCalendar.setVisibility(View.VISIBLE);
            cardMonthlyAttendance.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.rlMonth)
    public void onRlMonthClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] months = getResources().getStringArray(R.array.month);
        builder.setItems(months, (dialog, which) -> {
            currentMonth = months[which];
            textMonth.setText(currentMonth);
            updateUI(0);
            if (attendanceAdapter != null) {
                attendanceAdapter.clearData();
            }
            subscribe();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getPresentAbsentCount() {
        for (int i = 0; i < attendanceItemList.size(); i++) {
            if (attendanceItemList.get(i).getStatus() != null) {
                if (TextUtils.equals(attendanceItemList.get(i).getStatus(), "1")) {
                    presentCount++;
                } else {
                    absentCount++;
                }
            }
        }
    }
}
