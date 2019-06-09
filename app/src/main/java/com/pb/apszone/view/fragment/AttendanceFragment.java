package com.pb.apszone.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.view.adapter.AttendanceAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.utils.CommonUtils.getCurrentMonth;
import static com.pb.apszone.utils.CommonUtils.getCurrentYear;
import static com.pb.apszone.utils.CommonUtils.getFirstDayOfMonth;
import static com.pb.apszone.utils.CommonUtils.getNumOfDaysInMonth;

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
    CardView rlMonth;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    public static AttendanceFragment newInstance() {
        return new AttendanceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setUpList(currentMonth, currentYear);
        rvAttendanceUI.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        attendanceAdapter = new AttendanceAdapter(getActivity(), day);
        rvAttendanceUI.setAdapter(attendanceAdapter);
        return view;
    }

    private void setUpList(String currentMonth, String currentYear){
        int numDay = getNumOfDaysInMonth(currentYear, currentMonth);
        int skipPosition = getFirstDayOfMonth(currentYear, currentMonth);
        for (int i = 0; i < skipPosition - 1; i++) {
            day.add(i, "");
        }
        for (int i = 1; i <= numDay; i++) {
            day.add(String.valueOf(i));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

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
            if (attendanceAdapter != null) {
                attendanceAdapter.clearData();
                setUpList(currentMonth, currentYear);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
