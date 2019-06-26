package com.pb.apszone.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.InboxItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pb.apszone.utils.CommonUtils.getTime;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private final List<InboxItem> inboxItemList;
    private Context context;

    static class InboxViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.inbox_title)
        TextView inboxTitle;
        @BindView(R.id.inbox_description)
        TextView inboxDescription;
        @BindView(R.id.image_url)
        ImageView imageUrl;
        @BindView(R.id.inbox_time)
        TextView inboxTime;
        @BindView(R.id.inbox_logo)
        ImageView inboxLogo;
        @BindView(R.id.card_image_url)
        CardView cardImageUrl;

        InboxViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public InboxAdapter(List<InboxItem> inboxItemList, Context context) {
        this.inboxItemList = inboxItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_inbox, parent, false);
        return new InboxViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder inboxViewHolder, final int position) {
        InboxItem inboxItem = getItem(position);
        if (!TextUtils.isEmpty(inboxItem.getTitle())) {
            inboxViewHolder.inboxTitle.setText(inboxItem.getTitle());
        }
        if (!TextUtils.isEmpty(inboxItem.getMessage())) {
            inboxViewHolder.inboxDescription.setText(inboxItem.getMessage());
        }
        if (!TextUtils.isEmpty(inboxItem.getImageUrl())) {
            inboxViewHolder.cardImageUrl.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(inboxItem.getImageUrl())
                    .into(inboxViewHolder.imageUrl);
        } else {
            inboxViewHolder.cardImageUrl.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(inboxItem.getDate())) {
            inboxViewHolder.inboxTime.setText(getTime(inboxItem.getDate()));
        }
    }

    private InboxItem getItem(int position) {
        return inboxItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return inboxItemList.size();
    }

    public void clearData() {
        inboxItemList.clear();
        notifyDataSetChanged();
    }

}