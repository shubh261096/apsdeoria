package com.pb.apszone.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.SubjectId;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_HOMEWORK;
import static com.pb.apszone.utils.AppConstants.UI_ELEMENT_SYLLABUS;

public class TeacherHomeworkAdapter extends RecyclerView.Adapter<TeacherHomeworkAdapter.HomeworkViewHolder> {

    private final List<SubjectId> subjectIdList;
    private final OnSubjectItemClick onSubjectItemClick;
    private String dashboard_element_name;

    static class HomeworkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subject_name)
        TextView subjectName;
        @BindView(R.id.image_next)
        ImageView imageNext;

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

    public TeacherHomeworkAdapter(List<SubjectId> subjectIdList, OnSubjectItemClick onSubjectItemClick, String dashboard_element_name) {
        this.subjectIdList = subjectIdList;
        this.onSubjectItemClick = onSubjectItemClick;
        this.dashboard_element_name = dashboard_element_name;
    }

    @NonNull
    @Override
    public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_teacher_homework, parent, false);
        return new HomeworkViewHolder(view, onSubjectItemClick);
    }


    @Override
    public void onBindViewHolder(@NonNull HomeworkViewHolder homeworkViewHolder, final int position) {
        SubjectId subjectId = getItem(position);
        homeworkViewHolder.subjectName.setText(subjectId.getName());
        if (TextUtils.equals(dashboard_element_name, UI_ELEMENT_SYLLABUS)) {
            homeworkViewHolder.imageNext.setVisibility(View.GONE);
        } else if (TextUtils.equals(dashboard_element_name, UI_ELEMENT_HOMEWORK)) {
            homeworkViewHolder.imageNext.setVisibility(View.VISIBLE);
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
        void onItemClick(int position, View view);
    }
}