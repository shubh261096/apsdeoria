package com.pb.apszone.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.DashboardItem;
import com.pb.apszone.view.listener.OnDashboardItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    private final List<DashboardItem> dashboardItemList;
    private final OnDashboardItemClickListener onDashboardItemClickListener;


    static class DashboardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_ui)
        ImageView imageUi;
        @BindView(R.id.name)
        TextView name;

        DashboardViewHolder(final View itemView, final OnDashboardItemClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition(), itemView);
                }
            });
        }
    }

    public DashboardAdapter(List<DashboardItem> dashboardItemList, OnDashboardItemClickListener clickListener) {
        this.dashboardItemList = dashboardItemList;
        this.onDashboardItemClickListener = clickListener;
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dashboard, parent, false);
        return new DashboardViewHolder(view, onDashboardItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder dashboardViewHolder, final int position) {
        DashboardItem dashboardItem = getItem(position);
        dashboardViewHolder.name.setText(dashboardItem.getName());
        Picasso.get()
                .load(dashboardItem.getImageUrl())
                .into(dashboardViewHolder.imageUi);
    }

    private DashboardItem getItem(int position) {
        return dashboardItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return dashboardItemList.size();
    }

    public void clearData() {
        dashboardItemList.clear();
        notifyDataSetChanged();
    }
}