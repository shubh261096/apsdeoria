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
import com.pb.apszone.service.model.SyllabusItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SyllabusAdapter extends RecyclerView.Adapter<SyllabusAdapter.SyllabusViewHolder> {

    private final List<SyllabusItem> syllabusItemList;
    private Context context;
    private final OnDownloadItemClickListener onDownloadItemClickListener;

    static class SyllabusViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subject_name)
        TextView subjectName;
        @BindView(R.id.subject_description)
        TextView subjectDescription;
        @BindView(R.id.download_syllabus)
        TextView downloadSyllabus;

        SyllabusViewHolder(final View itemView, final OnDownloadItemClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            downloadSyllabus.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public SyllabusAdapter(List<SyllabusItem> syllabusItemList, Context context, OnDownloadItemClickListener onDownloadItemClickListener) {
        this.syllabusItemList = syllabusItemList;
        this.context = context;
        this.onDownloadItemClickListener = onDownloadItemClickListener;
    }

    @NonNull
    @Override
    public SyllabusViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_syllabus, parent, false);
        return new SyllabusViewHolder(view, onDownloadItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull SyllabusViewHolder syllabusViewHolder, final int position) {
        SyllabusItem syllabusItem = getItem(position);
        if (!TextUtils.isEmpty(syllabusItem.getName())) {
            syllabusViewHolder.subjectName.setText(syllabusItem.getName());
        }
        if (!TextUtils.isEmpty(syllabusItem.getDescription())) {
            syllabusViewHolder.subjectDescription.setText(syllabusItem.getDescription());
        } else {
            syllabusViewHolder.subjectDescription.setText("");
        }
        if (!TextUtils.isEmpty(syllabusItem.getSyllabus())) {
            syllabusViewHolder.downloadSyllabus.setVisibility(View.VISIBLE);
        } else {
            syllabusViewHolder.downloadSyllabus.setVisibility(View.GONE);
        }
    }

    private SyllabusItem getItem(int position) {
        return syllabusItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return syllabusItemList.size();
    }

    public interface OnDownloadItemClickListener {
        void onItemClick(int position, View view);
    }

}