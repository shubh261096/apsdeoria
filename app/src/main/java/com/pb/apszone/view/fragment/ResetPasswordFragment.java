package com.pb.apszone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pb.apszone.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ResetPasswordFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_reset_password)
    Toolbar toolbarResetPassword;


    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarResetPassword.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }

    @Override
    public void getNetworkData(boolean status) {

    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        inboxFragmentViewModel = ViewModelProviders.of(this).get(InboxFragmentViewModel.class);
//        observeInbox();
    }

    private void observeInbox() {
//        inboxFragmentViewModel.getInbox().observe(this, inboxResponseModel -> {
//            if (inboxResponseModel != null) {
//                progressBar.setVisibility(View.GONE);
//
//                if (inboxAdapter != null) {
//                    inboxAdapter.clearData();
//                }
//                if (!inboxResponseModel.isError()) {
//                    List<InboxItem> inboxItems = inboxResponseModel.getInbox();
//                    inboxItemList.addAll(inboxItems);
//                    inboxAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getActivity(), inboxResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void subscribe() {
//        progressBar.setVisibility(View.VISIBLE);
//        inboxFragmentViewModel.sendRequest();
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
