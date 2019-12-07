package com.pb.apszone.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.VideoItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private final List<VideoItem> videoItemList;
    private final OnItemClickListener onItemClickListener;

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtViewVideoTitle)
        TextView txtViewVideoTitle;

        VideoViewHolder(final View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public VideoAdapter(List<VideoItem> videoItemList, OnItemClickListener onItemClickListener) {
        this.videoItemList = videoItemList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_learn_content, parent, false);
        return new VideoViewHolder(view, onItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, final int position) {
        VideoItem videoItem = getItem(position);
        if (!TextUtils.isEmpty(videoItem.getTitle())) {
            videoViewHolder.txtViewVideoTitle.setText(videoItem.getTitle());
        }
    }

    private VideoItem getItem(int position) {
        return videoItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return videoItemList.size();
    }

    public void clearData() {
        videoItemList.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}