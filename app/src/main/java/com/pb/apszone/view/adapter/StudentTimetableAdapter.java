package com.pb.apszone.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.TimetableItem;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pb.apszone.utils.AppConstants.USER_TYPE_PARENT;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_TEACHER;
import static com.pb.apszone.utils.CommonUtils.getFormattedDateTime;
import static com.pb.apszone.utils.CommonUtils.isTimeBetweenTwoTime;

public class StudentTimetableAdapter extends RecyclerView.Adapter<StudentTimetableAdapter.StudentTimetableViewHolder> {

    private final List<TimetableItem> timetableItemList;
    private Context context;
    private String user_type;

    static class StudentTimetableViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.start_time)
        TextView startTime;
        @BindView(R.id.end_time)
        TextView endTime;
        @BindView(R.id.subject)
        TextView subject;
        @BindView(R.id.teacher)
        TextView teacher;
        @BindView(R.id.list_position)
        TextView listPosition;
        @BindView(R.id.active_class)
        View activeClass;
        @BindView(R.id.teachingClass)
        TextView teachingClass;

        StudentTimetableViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public StudentTimetableAdapter(List<TimetableItem> timetableItemList, Context context, String user_type) {
        this.timetableItemList = timetableItemList;
        this.context = context;
        this.user_type = user_type;
    }

    @NonNull
    @Override
    public StudentTimetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_timetable_student, parent, false);
        return new StudentTimetableViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull StudentTimetableViewHolder studentTimetableViewHolder, final int position) {
        TimetableItem timetableItem = getItem(position);
        int pos = position + 1;
        studentTimetableViewHolder.listPosition.setText(String.valueOf(pos));
        studentTimetableViewHolder.startTime.setText(getFormattedDateTime(timetableItem.getStartTime()));
        studentTimetableViewHolder.endTime.setText(getFormattedDateTime(timetableItem.getEndTime()));
        try {
            if (isTimeBetweenTwoTime(timetableItem.getStartTime(), timetableItem.getEndTime())) {
                studentTimetableViewHolder.activeClass.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (timetableItem.getSubjectId() == null) {
            studentTimetableViewHolder.subject.setText(R.string.text_recess);
        } else {
            studentTimetableViewHolder.subject.setText(timetableItem.getSubjectId().getName());
        }

        /* Checking the user type */
        if (TextUtils.equals(user_type, USER_TYPE_PARENT)) {
            if (timetableItem.getTeacherId() == null) {
                studentTimetableViewHolder.teacher.setVisibility(View.GONE);
            } else {
                studentTimetableViewHolder.teacher.setVisibility(View.VISIBLE);
                studentTimetableViewHolder.teacher.setText(timetableItem.getTeacherId().getFullname());
            }
        } else if (TextUtils.equals(user_type, USER_TYPE_TEACHER)) {
            if (timetableItem.getClassId() == null) {
                studentTimetableViewHolder.teachingClass.setVisibility(View.GONE);
            } else {
                studentTimetableViewHolder.teachingClass.setVisibility(View.VISIBLE);
                studentTimetableViewHolder.teachingClass.setText(timetableItem.getClassId().getName());
            }
        }
    }

    private TimetableItem getItem(int position) {
        return timetableItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return timetableItemList.size();
    }

    public void clearData() {
        timetableItemList.clear();
        notifyDataSetChanged();
    }

}