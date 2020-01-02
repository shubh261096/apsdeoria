package com.pb.apszone.view.adapter;

import android.content.Context;
import android.content.res.ColorStateList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.pb.apszone.R;
import com.pb.apszone.service.model.StudentsItem;
import com.pb.apszone.view.listener.OnCheckBoxCheckedListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pb.apszone.utils.AppConstants.Numbers.ONE;

public class TeacherAttendanceAdapter extends RecyclerView.Adapter<TeacherAttendanceAdapter.AttendanceViewHolder> {

    private final List<StudentsItem> studentsItemList;
    private Context context;
    private final OnCheckBoxCheckedListener checkBoxCheckedListener;

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkbox)
        CheckBox checkbox;

        AttendanceViewHolder(final View itemView, OnCheckBoxCheckedListener checkedListener, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (buttonView.isShown()) {
                    if (checkedListener != null) {
                        if (isChecked) {
                            checkbox.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
                        } else {
                            checkbox.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
                        }
                        checkedListener.onItemChecked(getAdapterPosition(), isChecked);
                    }
                }
            });
        }

    }

    public TeacherAttendanceAdapter(List<StudentsItem> studentsItemList, Context context, OnCheckBoxCheckedListener checkedListener) {
        this.studentsItemList = studentsItemList;
        this.context = context;
        this.checkBoxCheckedListener = checkedListener;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_teacher_attendance, parent, false);
        return new AttendanceViewHolder(view, checkBoxCheckedListener, context);
    }


    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder attendanceViewHolder, final int position) {
        StudentsItem studentsItem = getItem(position);
        if (studentsItem != null) {
            if (!TextUtils.isEmpty(studentsItem.getFullname())) {
                attendanceViewHolder.checkbox.setText(studentsItem.getFullname());
                if (studentsItem.getAttendance() == null) {
                    attendanceViewHolder.checkbox.setChecked(false);
                } else {
                    if (studentsItem.getAttendance().getStatus().equals(ONE)) {
                        attendanceViewHolder.checkbox.setChecked(true);
                        attendanceViewHolder.checkbox.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
                    } else {
                        attendanceViewHolder.checkbox.setChecked(false);
                        attendanceViewHolder.checkbox.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
                    }
                }
            }
        }
    }

    private StudentsItem getItem(int position) {
        return studentsItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return studentsItemList.size();
    }

    public void clearData() {
        studentsItemList.clear();
        notifyDataSetChanged();
    }

}