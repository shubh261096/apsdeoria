package com.pb.apszone.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
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
        @BindView(R.id.upload_syllabus)
        TextView uploadSyllabus;
        @BindView(R.id.download_syllabus)
        TextView downloadSyllabus;
        @BindView(R.id.view_syllabus)
        TextView viewSyllabus;

        HomeworkViewHolder(final View itemView, final OnSubjectItemClick subjectItemClick) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            downloadSyllabus.setOnClickListener(v -> {
                if (subjectItemClick != null) {
                    subjectItemClick.onDownloadClick(getAdapterPosition(), v);
                }
            });
            uploadSyllabus.setOnClickListener(v -> {
                if (subjectItemClick != null) {
                    subjectItemClick.onUploadClick(getAdapterPosition(), v);
                }
            });
            viewSyllabus.setOnClickListener(v -> {
                if (subjectItemClick != null) {
                    subjectItemClick.onViewClick(getAdapterPosition(), v);
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
        if (!TextUtils.isEmpty(subjectId.getSyllabus())) {
            homeworkViewHolder.uploadSyllabus.setVisibility(View.GONE);
            homeworkViewHolder.downloadSyllabus.setVisibility(View.VISIBLE);
            homeworkViewHolder.viewSyllabus.setVisibility(View.VISIBLE);
        } else {
            homeworkViewHolder.uploadSyllabus.setVisibility(View.VISIBLE);
            homeworkViewHolder.downloadSyllabus.setVisibility(View.GONE);
            homeworkViewHolder.viewSyllabus.setVisibility(View.GONE);
        }
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
        void onDownloadClick(int position, View view);

        void onUploadClick(int position, View view);

        void onViewClick(int position, View view);
    }
}