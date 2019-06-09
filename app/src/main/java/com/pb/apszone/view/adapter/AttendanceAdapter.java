package com.pb.apszone.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pb.apszone.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private Context context;
    private List<String> numDay;

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.status)
        View status;
        @BindView(R.id.day)
        TextView day;

        AttendanceViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public AttendanceAdapter(Context context, List<String> numDay) {
        this.context = context;
        this.numDay = numDay;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder attendanceViewHolder, final int position) {
        attendanceViewHolder.day.setText(numDay.get(position));
        if (!numDay.get(position).isEmpty()){
            attendanceViewHolder.status.setVisibility(View.VISIBLE);
            attendanceViewHolder.status.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            attendanceViewHolder.status.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return numDay.size();
    }

    public void clearData() {
        numDay.clear();
        notifyDataSetChanged();
    }

}