package com.pb.apszone.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.TimetableItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pb.apszone.utils.CommonUtils.getFormatedDateTime;

public class StudentTimetableAdapter extends RecyclerView.Adapter<StudentTimetableAdapter.StudentTimetableViewHodlder> {

    private final List<TimetableItem> timetableItemList;
    private Context context;

    static class StudentTimetableViewHodlder extends RecyclerView.ViewHolder {
        @BindView(R.id.start_time)
        TextView startTime;
        @BindView(R.id.end_time)
        TextView endTime;
        @BindView(R.id.subject)
        TextView subject;
        @BindView(R.id.teacher)
        TextView teacher;

        StudentTimetableViewHodlder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public StudentTimetableAdapter(List<TimetableItem> timetableItemList, Context context) {
        this.timetableItemList = timetableItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentTimetableViewHodlder onCreateViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_timetable_student, parent, false);
        return new StudentTimetableViewHodlder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull StudentTimetableViewHodlder studentTimetableViewHodlder, final int position) {
        TimetableItem timetableItem = getItem(position);
        studentTimetableViewHodlder.startTime.setText(getFormatedDateTime(timetableItem.getStartTime()));
        studentTimetableViewHodlder.endTime.setText(getFormatedDateTime(timetableItem.getEndTime()));
        if (timetableItem.getSubjectId() == null) {
            studentTimetableViewHodlder.subject.setText(R.string.text_recess);
        } else {
            studentTimetableViewHodlder.subject.setText(timetableItem.getSubjectId().getName());
        }
        if (timetableItem.getTeacherId() == null) {
            studentTimetableViewHodlder.teacher.setVisibility(View.GONE);
        } else {
            studentTimetableViewHodlder.teacher.setVisibility(View.VISIBLE);
            studentTimetableViewHodlder.teacher.setText(timetableItem.getTeacherId().getFullname());
        }
    }

    private TimetableItem getItem(int position) {
        return timetableItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return timetableItemList.size();
    }

}