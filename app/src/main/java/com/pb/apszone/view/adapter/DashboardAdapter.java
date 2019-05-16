package com.pb.apszone.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import static com.pb.apszone.utils.AppConstants.KEY_ENABLED;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_PARENT;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_TEACHER;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    private final List<DashboardItem> dashboardItemList;
    private Context context;
    private final OnDashboardItemClickListener onDashboardItemClickListener;
    private String user_type;


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

    public DashboardAdapter(List<DashboardItem> dashboardItemList, OnDashboardItemClickListener clickListener, Context context, String user_type) {
        this.dashboardItemList = dashboardItemList;
        this.onDashboardItemClickListener = clickListener;
        this.context = context;
        this.user_type = user_type;
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
        if (TextUtils.equals(user_type, USER_TYPE_PARENT)) {
            if (TextUtils.equals(dashboardItem.getParent(), KEY_ENABLED)) {
                dashboardViewHolder.name.setText(dashboardItem.getName());
                Picasso.get()
                        .load(dashboardItem.getImageUrl())
                        .into(dashboardViewHolder.imageUi);
            } else {
                dashboardViewHolder.itemView.setVisibility(View.GONE);
            }
        } else if (TextUtils.equals(user_type, USER_TYPE_TEACHER)) {
            if (TextUtils.equals(dashboardItem.getTeacher(), KEY_ENABLED)) {
                dashboardViewHolder.name.setText(dashboardItem.getName());
                Picasso.get()
                        .load(dashboardItem.getImageUrl())
                        .into(dashboardViewHolder.imageUi);
            } else {
                dashboardViewHolder.itemView.setVisibility(View.GONE);
            }
        }
    }

    private DashboardItem getItem(int position) {
        return dashboardItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return dashboardItemList.size();
    }

}