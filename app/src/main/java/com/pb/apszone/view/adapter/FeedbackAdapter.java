package com.pb.apszone.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.TeacherId;
import com.pb.apszone.view.listener.OnFeebackRatingEditListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private final List<TeacherId> teacherIdList;
    private final OnFeebackRatingEditListener onFeebackRatingEditListener;

    static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.teacher_name)
        TextView teacherName;
        @BindView(R.id.rating)
        MaterialRatingBar ratingBar;
        @BindView(R.id.edt_feedback)
        EditText edtFeedback;

        FeedbackViewHolder(final View itemView, final OnFeebackRatingEditListener onFeebackRatingEditListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ratingBar.setOnRatingChangeListener((ratingBar, rating) -> onFeebackRatingEditListener.onRatingChanged(getAdapterPosition(), (int) rating));

            edtFeedback.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() != 0) {
                        onFeebackRatingEditListener.onFeedbackChanged(getAdapterPosition(), s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }

    public FeedbackAdapter(List<TeacherId> teacherIdList, OnFeebackRatingEditListener onFeebackRatingEditListener) {
        this.teacherIdList = teacherIdList;
        this.onFeebackRatingEditListener = onFeebackRatingEditListener;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_feedback, parent, false);
        return new FeedbackViewHolder(view, onFeebackRatingEditListener);
    }


    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder feedbackViewHolder, final int position) {
        TeacherId teacherId = getItem(position);
        if (teacherId != null) {
            if (!TextUtils.isEmpty(teacherId.getFullname())) {
                feedbackViewHolder.teacherName.setText(teacherId.getFullname());
            }
        }
    }

    private TeacherId getItem(int position) {
        return teacherIdList.get(position);
    }

    @Override
    public int getItemCount() {
        return teacherIdList.size();
    }

    public void clearData() {
        teacherIdList.clear();
        notifyDataSetChanged();
    }

}