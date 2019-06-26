package com.pb.apszone.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pb.apszone.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private final HashMap<String, String> profileValueHashmap;
    private Context context;

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.input_text_key)
        TextView inputTextKey;
        @BindView(R.id.input_text_value)
        TextView inputTextValue;

        ProfileViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ProfileAdapter(HashMap<String, String> profileValueHashmap, Context context) {
        this.profileValueHashmap = profileValueHashmap;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder profileViewHolder, final int position) {
        int i = 0;
        for (Map.Entry<String, String> entry : profileValueHashmap.entrySet()) {
            if (position == i) {
                String key = entry.getKey();
                String value = entry.getValue();
                profileViewHolder.inputTextKey.setText(key);
                profileViewHolder.inputTextValue.setText(value);
                break;
            }
            i++;
        }
    }

    @Override
    public int getItemCount() {
        return profileValueHashmap.size();
    }

    public void clearData() {
        profileValueHashmap.clear();
        notifyDataSetChanged();
    }
}