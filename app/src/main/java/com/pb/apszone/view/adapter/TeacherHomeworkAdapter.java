package com.pb.apszone.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.SubjectId;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherHomeworkAdapter extends RecyclerView.Adapter<TeacherHomeworkAdapter.HomeworkViewHolder> {

    private final List<SubjectId> subjectIdList;

    static class HomeworkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subject_name)
        TextView subjectName;

        HomeworkViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public TeacherHomeworkAdapter(List<SubjectId> subjectIdList) {
        this.subjectIdList = subjectIdList;
    }

    @NonNull
    @Override
    public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_teacher_homework, parent, false);
        return new HomeworkViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HomeworkViewHolder homeworkViewHolder, final int position) {
        SubjectId subjectId = getItem(position);
        homeworkViewHolder.subjectName.setText(subjectId.getName());
    }

    private SubjectId getItem(int position) {
        return subjectIdList.get(position);
    }

    @Override
    public int getItemCount() {
        return subjectIdList.size();
    }

    public void clearData() {
        subjectIdList.clear();
        notifyDataSetChanged();
    }

}