package com.pb.apszone.view.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.FeesItem;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.FeesAdapter;
import com.pb.apszone.viewModel.FeesFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_ID;
import static com.pb.apszone.utils.CommonUtils.getCurrentYear;
import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.showProgress;

public class FeesFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_fees)
    Toolbar toolbarFees;
    @BindView(R.id.rvFees)
    RecyclerView rvFees;
    private List<FeesItem> feesItemList;
    FeesFragmentViewModel feesFragmentViewModel;
    KeyStorePref keyStorePref;
    FeesAdapter feesAdapter;

    public FeesFragment() {
        // Required empty public constructor
    }

    public static FeesFragment newInstance() {
        return new FeesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyStorePref = KeyStorePref.getInstance(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fees, container, false);
        unbinder = ButterKnife.bind(this, view);
        feesItemList = new ArrayList<>();
        rvFees.setLayoutManager(new LinearLayoutManager(getActivity()));
        feesAdapter = new FeesAdapter(feesItemList, getActivity());
        rvFees.setAdapter(feesAdapter);
        toolbarFees.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        feesFragmentViewModel = ViewModelProviders.of(this).get(FeesFragmentViewModel.class);
        subscribe();
    }

    @SuppressLint("SetTextI18n")
    private void subscribe() {
        if (!TextUtils.isEmpty(keyStorePref.getString(KEY_STUDENT_ID))) {
            feesFragmentViewModel.sendRequest(keyStorePref.getString(KEY_STUDENT_CLASS_ID), getCurrentYear(), keyStorePref.getString(KEY_STUDENT_ID));
        }
        showProgress(getActivity(), "Please wait...");
        feesFragmentViewModel.getFees().observe(this, feesResponseModel -> {
            if (feesResponseModel != null) {
                hideProgress();
                if (!feesResponseModel.isError()) {
                    List<FeesItem> feesItems = feesResponseModel.getFees();
                    feesItemList.addAll(feesItems);
                    feesAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), feesResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
