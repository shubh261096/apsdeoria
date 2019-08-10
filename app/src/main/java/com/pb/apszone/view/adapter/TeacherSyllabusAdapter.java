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

public class TeacherSyllabusAdapter extends RecyclerView.Adapter<TeacherSyllabusAdapter.HomeworkViewHolder> {

    private final List<SubjectId> subjectIdList;
    private final OnSubjectItemClick onSubjectItemClick;

    static class HomeworkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subject_name)
        TextView subjectName;


        HomeworkViewHolder(final View itemView, final OnSubjectItemClick subjectItemClick) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (subjectItemClick != null) {
                    subjectItemClick.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public TeacherSyllabusAdapter(List<SubjectId> subjectIdList, OnSubjectItemClick onSubjectItemClick) {
        this.subjectIdList = subjectIdList;
        this.onSubjectItemClick = onSubjectItemClick;
    }

    @NonNull
    @Override
    public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_teacher_syllabus, parent, false);
        return new HomeworkViewHolder(view, onSubjectItemClick);
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

    public interface OnSubjectItemClick {
        void onItemClick(int position, View view);
    }
}