package com.pb.apszone.view.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import static com.pb.apszone.utils.CommonUtils.capitalize;
import static com.pb.apszone.utils.CommonUtils.getCurrentYear;
import static com.pb.apszone.utils.CommonUtils.hideProgress;
import static com.pb.apszone.utils.CommonUtils.showProgress;

public class FeesFragment extends Fragment implements FeesAdapter.OnFeeDetailItemClick {

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
        feesAdapter = new FeesAdapter(feesItemList, getActivity(), this);
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

    @Override
    public void onItemClick(int position, View view) {
        showCustomDialog(view, position);
    }

    private void showCustomDialog(View view, int postion) {
        //before inflating the custom alert dialog layout, we will get the current activity viewGroup
        ViewGroup viewGroup = view.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fee_detail, viewGroup, false);
        ImageView close = dialogView.findViewById(R.id.close);
        TextView period = dialogView.findViewById(R.id.period);
        TextView totalAmount = dialogView.findViewById(R.id.total_amount);
        TextView dueAmount = dialogView.findViewById(R.id.due_amount);
        TextView feesPaid = dialogView.findViewById(R.id.fees_paid);
        TextView datePaid = dialogView.findViewById(R.id.date_paid);
        TextView status = dialogView.findViewById(R.id.status);

        if (!TextUtils.isEmpty(feesItemList.get(postion).getPeriod())) {
            period.setText(feesItemList.get(postion).getPeriod());
        }
        if (!TextUtils.isEmpty(feesItemList.get(postion).getFeesId().getTotalAmount())) {
            totalAmount.setText(String.format("%s %s", getString(R.string.rupee_symbol), feesItemList.get(postion).getFeesId().getTotalAmount()));
        }
        if (!TextUtils.isEmpty(feesItemList.get(postion).getDueAmount())) {
            dueAmount.setText(String.format("%s %s", getString(R.string.rupee_symbol), feesItemList.get(postion).getDueAmount()));
        }
        if (!TextUtils.isEmpty(feesItemList.get(postion).getFeesPaid())) {
            feesPaid.setText(String.format("%s %s", getString(R.string.rupee_symbol), feesItemList.get(postion).getFeesPaid()));
        }
        if (!TextUtils.isEmpty(feesItemList.get(postion).getDatePaid())) {
            datePaid.setText(feesItemList.get(postion).getDatePaid());
        }
        if (!TextUtils.isEmpty(feesItemList.get(postion).getStatus())) {
            status.setText(capitalize(feesItemList.get(postion).getStatus()));
        }

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();

        close.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }
}
