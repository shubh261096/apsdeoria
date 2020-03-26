package com.pb.apszone.view.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.apszone.R;
import com.pb.apszone.database.AccountsModel;
import com.pb.apszone.view.listener.OnItemClickListener;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pb.apszone.utils.AppConstants.USER_GENDER_MALE;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder> {

    private final List<AccountsModel> accountsModelList;
    private final OnItemClickListener onItemClickListener;

    static class AccountsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.imgViewAccount)
        ImageView imgViewAccount;

        AccountsViewHolder(final View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public AccountsAdapter(List<AccountsModel> accountsModelList, OnItemClickListener onItemClickListener) {
        this.accountsModelList = accountsModelList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_accounts, parent, false);
        return new AccountsViewHolder(view, onItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder accountsViewHolder, final int position) {
        AccountsModel accounts = getItem(position);
        if (!TextUtils.isEmpty(accounts.getUserName())) {
            accountsViewHolder.userName.setText(accounts.getUserName());
        }
        if (!TextUtils.isEmpty(accounts.getUserGender())) {
            int drawable = TextUtils.equals(accounts.getUserGender(), USER_GENDER_MALE) ? R.drawable.profile_boy : R.drawable.profile_girl;
            accountsViewHolder.imgViewAccount.setImageResource(drawable);
        }
    }

    private AccountsModel getItem(int position) {
        return accountsModelList.get(position);
    }

    @Override
    public int getItemCount() {
        return accountsModelList.size();
    }

    public void clearData() {
        accountsModelList.clear();
        notifyDataSetChanged();
    }

}