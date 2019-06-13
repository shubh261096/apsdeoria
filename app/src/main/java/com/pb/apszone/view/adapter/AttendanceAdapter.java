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
import com.pb.apszone.service.model.AttendanceItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pb.apszone.utils.CommonUtils.getFirstDateValueFromFullDate;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private Context context;
    private List<String> numDay;
    private List<AttendanceItem> attendanceItemList;
    private int i = 0;

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

    public AttendanceAdapter(Context context, List<String> numDay, List<AttendanceItem> attendanceItemList) {
        this.context = context;
        this.numDay = numDay;
        this.attendanceItemList = attendanceItemList;
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
        String day = parseDate(i);
        String status = parseStatus(i);
        if (!numDay.get(position).isEmpty()) {
            attendanceViewHolder.status.setVisibility(View.VISIBLE);
            if (TextUtils.equals(attendanceViewHolder.day.getText(), day)) {
                i++; // Increment the position by 1 if day value set in viewHolder is equal to day. This is a hack
                if (TextUtils.equals(status, "1")) {
                    attendanceViewHolder.status.setBackground(context.getResources().getDrawable(R.drawable.view_bg_round_green));
                } else {
                    attendanceViewHolder.status.setBackground(context.getResources().getDrawable(R.drawable.view_bg_round_red));
                }
            }
        } else {
            attendanceViewHolder.status.setVisibility(View.GONE);
        }
    }

    private String parseDate(int pos) {
        if (pos < attendanceItemList.size()) {
            return getFirstDateValueFromFullDate(attendanceItemList.get(pos).getDate());
        }
        return null;
    }

    private String parseStatus(int pos) {
        if (pos < attendanceItemList.size()) {
            return attendanceItemList.get(pos).getStatus();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return numDay.size();
    }

    public void clearData() {
        numDay.clear();
        attendanceItemList.clear();
        notifyDataSetChanged();
    }

}