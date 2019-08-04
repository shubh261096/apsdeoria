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
import com.pb.apszone.service.model.HomeworkItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder> {

    private final List<HomeworkItem> homeworkItemList;
    private Context context;
    private final OnDownloadItemClickListener onDownloadItemClickListener;

    static class HomeworkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subject_name)
        TextView subjectName;
        @BindView(R.id.teacher_name)
        TextView teacherName;
        @BindView(R.id.teacher_remarks)
        TextView teacherRemarks;
        @BindView(R.id.download_homework)
        TextView downloadHomework;

        HomeworkViewHolder(final View itemView, final OnDownloadItemClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            downloadHomework.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public HomeworkAdapter(List<HomeworkItem> homeworkItemList, Context context, OnDownloadItemClickListener onDownloadItemClickListener) {
        this.homeworkItemList = homeworkItemList;
        this.context = context;
        this.onDownloadItemClickListener = onDownloadItemClickListener;
    }

    @NonNull
    @Override
    public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_homework, parent, false);
        return new HomeworkViewHolder(view, onDownloadItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull HomeworkViewHolder homeworkViewHolder, final int position) {
        HomeworkItem homeworkItem = getItem(position);
        if (!TextUtils.isEmpty(homeworkItem.getSubjectId().getName())) {
            homeworkViewHolder.subjectName.setText(homeworkItem.getSubjectId().getName());
        }
        if (!TextUtils.isEmpty(homeworkItem.getTeacherId().getFullname())) {
            homeworkViewHolder.teacherName.setText(homeworkItem.getTeacherId().getFullname());
        }
        if (!TextUtils.isEmpty(homeworkItem.getRemarks())) {
            homeworkViewHolder.teacherRemarks.setVisibility(View.VISIBLE);
            homeworkViewHolder.teacherRemarks.setText(homeworkItem.getRemarks());
        } else {
            homeworkViewHolder.teacherRemarks.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(homeworkItem.getData())) {
            homeworkViewHolder.downloadHomework.setVisibility(View.VISIBLE);
        } else {
            homeworkViewHolder.downloadHomework.setVisibility(View.GONE);
        }
    }

    private HomeworkItem getItem(int position) {
        return homeworkItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return homeworkItemList.size();
    }

    public interface OnDownloadItemClickListener {
        void onItemClick(int position, View view);
    }

    public void clearData() {
        homeworkItemList.clear();
        notifyDataSetChanged();
    }

}