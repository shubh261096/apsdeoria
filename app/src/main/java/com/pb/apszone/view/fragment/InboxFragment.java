package com.pb.apszone.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.InboxItem;
import com.pb.apszone.view.adapter.InboxAdapter;
import com.pb.apszone.viewModel.InboxFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InboxFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_inbox)
    Toolbar toolbarInbox;
    @BindView(R.id.rvInbox)
    RecyclerView rvInbox;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<InboxItem> inboxItemList;
    InboxFragmentViewModel inboxFragmentViewModel;
    InboxAdapter inboxAdapter;

    public InboxFragment() {
        // Required empty public constructor
    }

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        unbinder = ButterKnife.bind(this, view);
        inboxItemList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvInbox.setLayoutManager(layoutManager);
        inboxAdapter = new InboxAdapter(inboxItemList, getActivity());
        rvInbox.setAdapter(inboxAdapter);
        toolbarInbox.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }

    @Override
    public void getNetworkData(boolean status) {
        if (status) {
            if (inboxAdapter != null) {
                inboxAdapter.clearData();
            }
            subscribe();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        inboxFragmentViewModel = ViewModelProviders.of(this).get(InboxFragmentViewModel.class);
        observeInbox();
    }

    private void observeInbox() {
        inboxFragmentViewModel.getInbox().observe(this, inboxResponseModel -> {
            if (inboxResponseModel != null) {
                progressBar.setVisibility(View.GONE);

                if (inboxAdapter != null) {
                    inboxAdapter.clearData();
                }
                if (!inboxResponseModel.isError()) {
                    List<InboxItem> inboxItems = inboxResponseModel.getInbox();
                    inboxItemList.addAll(inboxItems);
                    inboxAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), inboxResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void subscribe() {
        progressBar.setVisibility(View.VISIBLE);
        inboxFragmentViewModel.sendRequest();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
