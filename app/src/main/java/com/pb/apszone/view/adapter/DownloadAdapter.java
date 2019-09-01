package com.pb.apszone.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.DownloadItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    private final List<DownloadItem> downloadItemList;
    private final OnDownloadItemClickListener onDownloadItemClickListener;

    static class DownloadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.download_content)
        TextView downloadContent;

        DownloadViewHolder(final View itemView, final OnDownloadItemClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            downloadContent.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onDownloadItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public DownloadAdapter(List<DownloadItem> downloadItemList, OnDownloadItemClickListener onDownloadItemClickListener) {
        this.downloadItemList = downloadItemList;
        this.onDownloadItemClickListener = onDownloadItemClickListener;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_download, parent, false);
        return new DownloadViewHolder(view, onDownloadItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder downloadViewHolder, final int position) {
        DownloadItem downloadItem = getItem(position);
        if (!TextUtils.isEmpty(downloadItem.getName())) {
            downloadViewHolder.name.setText(downloadItem.getName());
        }
        if (!TextUtils.isEmpty(downloadItem.getUrl())) {
            downloadViewHolder.downloadContent.setVisibility(View.VISIBLE);
        } else {
            downloadViewHolder.downloadContent.setVisibility(View.GONE);
        }
    }

    private DownloadItem getItem(int position) {
        return downloadItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return downloadItemList.size();
    }

    public interface OnDownloadItemClickListener {
        void onDownloadItemClick(int position, View view);
    }

    public void clearData() {
        downloadItemList.clear();
        notifyDataSetChanged();
    }

}