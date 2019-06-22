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
import com.pb.apszone.service.model.FeesItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeesAdapter extends RecyclerView.Adapter<FeesAdapter.FeesViewHolder> {

    private final List<FeesItem> feesItemList;
    private Context context;

    static class FeesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.period)
        TextView period;
        @BindView(R.id.fees_paid)
        TextView feesPaid;
        @BindView(R.id.due_amount)
        TextView dueAmount;
        @BindView(R.id.total_amount)
        TextView totalAmount;

        FeesViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public FeesAdapter(List<FeesItem> feesItemList, Context context) {
        this.feesItemList = feesItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_fees, parent, false);
        return new FeesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FeesViewHolder feesViewHolder, final int position) {
        FeesItem feesItem = getItem(position);
        if (!TextUtils.isEmpty(feesItem.getPeriod())) {
            feesViewHolder.period.setText(feesItem.getPeriod());
        }
        if (!TextUtils.isEmpty(feesItem.getFeesId().getTotalAmount())) {
            feesViewHolder.totalAmount.setText(feesItem.getFeesId().getTotalAmount());
        }
        if (!TextUtils.isEmpty(feesItem.getDueAmount())) {
            feesViewHolder.dueAmount.setText(feesItem.getDueAmount());
            if (TextUtils.equals(feesItem.getDueAmount(), "0")) {
                feesViewHolder.dueAmount.setTextColor(context.getResources().getColor(R.color.green));
            } else {
                feesViewHolder.dueAmount.setTextColor(context.getResources().getColor(R.color.red));
            }
        } else {
            feesViewHolder.dueAmount.setTextColor(context.getResources().getColor(R.color.green));
            feesViewHolder.dueAmount.setText("-");
        }
        if (!TextUtils.isEmpty(feesItem.getFeesPaid())) {
            feesViewHolder.feesPaid.setText(feesItem.getFeesPaid());
        } else {
            feesViewHolder.feesPaid.setTextColor(context.getResources().getColor(R.color.red));
            feesViewHolder.feesPaid.setText("-");
        }
    }

    private FeesItem getItem(int position) {
        return feesItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return feesItemList.size();
    }

}