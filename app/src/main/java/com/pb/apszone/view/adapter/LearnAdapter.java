package com.pb.apszone.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.LearnItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LearnAdapter extends RecyclerView.Adapter<LearnAdapter.LearnViewHolder> {

    private final List<LearnItem> learnItemList;
    private OnItemClickListener onItemClickListener;

    static class LearnViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subject_name)
        TextView subjectName;

        LearnViewHolder(final View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public LearnAdapter(List<LearnItem> learnItemList, OnItemClickListener clickListener) {
        this.learnItemList = learnItemList;
        this.onItemClickListener = clickListener;
    }

    @NonNull
    @Override
    public LearnViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_teacher_homework, parent, false);
        return new LearnViewHolder(view, onItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull LearnViewHolder learnViewHolder, final int position) {
        LearnItem learnItem = getItem(position);
        if (!TextUtils.isEmpty(learnItem.getName()) && learnItem.getVideo() != null) {
            learnViewHolder.subjectName.setText(learnItem.getName());
        }
    }

    private LearnItem getItem(int position) {
        return learnItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return learnItemList.size();
    }

    public void clearData() {
        learnItemList.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}