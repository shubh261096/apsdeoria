package com.pb.apszone.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.FeesItem;
import com.pb.apszone.service.model.FeesResponseModel;
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
import static com.pb.apszone.utils.CommonUtils.getCurrentMonth;
import static com.pb.apszone.utils.CommonUtils.getCurrentYear;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;
import static com.pb.apszone.utils.CommonUtils.showLateFeeAlertDialog;

public class FeesFragment extends BaseFragment implements FeesAdapter.OnFeeDetailItemClick {

    Unbinder unbinder;
    @BindView(R.id.toolbar_fees)
    Toolbar toolbarFees;
    @BindView(R.id.rvFees)
    RecyclerView rvFees;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.feeDeadlineNotice)
    TextView feeDeadlineNotice;
    @BindView(R.id.no_data)
    TextView tvNoData;
    @BindView(R.id.admission_fee)
    TextView admissionFee;
    @BindView(R.id.annual_fee)
    TextView annualFee;
    @BindView(R.id.activity_fee)
    TextView activityFee;
    @BindView(R.id.computer_fee)
    TextView computerFee;
    @BindView(R.id.ll_computer_fee)
    LinearLayout llComputerFee;
    @BindView(R.id.ll_display_fee)
    LinearLayout llDisplayFee;
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
    public void getNetworkData(boolean status) {
        if (status) {
            if (feesAdapter != null) {
                feesAdapter.clearData();
            }
            subscribe();
        }
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
        observeFee();
    }

    private void observeFee() {
        feesFragmentViewModel.getFees().observe(this, responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);
                if (feesAdapter != null) {
                    feesAdapter.clearData();
                }

                if (responseEvent.isSuccess()) {
                    FeesResponseModel feesResponseModel = responseEvent.getFeesResponseModel();
                    if (!feesResponseModel.isError()) {
                        List<FeesItem> feesItems = feesResponseModel.getFees();
                        // Setting up common static data which is same for every list item
                        setupData(feesItems);

                        feesItemList.addAll(feesItems);
                        feesAdapter.notifyDataSetChanged();
                        checkForLateFee();
                        llDisplayFee.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                    } else {
                        llDisplayFee.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    private void subscribe() {
        if (!TextUtils.isEmpty(keyStorePref.getString(KEY_STUDENT_ID))) {
            feesFragmentViewModel.sendRequest(keyStorePref.getString(KEY_STUDENT_CLASS_ID), getCurrentYear(), keyStorePref.getString(KEY_STUDENT_ID));
        }
        tvNoData.setVisibility(View.GONE);
        llDisplayFee.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setupData(List<FeesItem> feesItems) {
        if (!TextUtils.isEmpty(feesItems.get(0).getFeesId().getActivityFee())) {
            activityFee.setText(String.format("%s %s", getString(R.string.rupee_symbol), feesItems.get(0).getFeesId().getActivityFee()));
        }
        if (!TextUtils.isEmpty(feesItems.get(0).getFeesId().getAdmissionFee())) {
            admissionFee.setText(String.format("%s %s", getString(R.string.rupee_symbol), feesItems.get(0).getFeesId().getAdmissionFee()));
        }
        if (!TextUtils.isEmpty(feesItems.get(0).getFeesId().getAnnualFee())) {
            annualFee.setText(String.format("%s %s", getString(R.string.rupee_symbol), feesItems.get(0).getFeesId().getAnnualFee()));
        }
        if (!TextUtils.isEmpty(feesItems.get(0).getFeesId().getComputerFee())) {
            llComputerFee.setVisibility(View.VISIBLE);
            computerFee.setText(String.format("%s %s", getString(R.string.rupee_symbol), feesItems.get(0).getFeesId().getComputerFee()));
        }
    }

    private void checkForLateFee() {
        boolean found = false;
        for (int i = 0; i < feesItemList.size(); i++) {
            found = TextUtils.equals(feesItemList.get(i).getPeriod(), getCurrentMonth());
        }
        if (!found) {
            showLateFeeAlertDialog(Objects.requireNonNull(getView()), getContext());
        }
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

    private void showCustomDialog(View view, int position) {
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

        if (!TextUtils.isEmpty(feesItemList.get(position).getPeriod())) {
            period.setText(feesItemList.get(position).getPeriod());
        }
        if (!TextUtils.isEmpty(feesItemList.get(position).getFeesId().getTotalAmount())) {
            totalAmount.setText(String.format("%s %s", getString(R.string.rupee_symbol), feesItemList.get(position).getFeesId().getTotalAmount()));
        }
        if (!TextUtils.isEmpty(feesItemList.get(position).getDueAmount())) {
            dueAmount.setText(String.format("%s %s", getString(R.string.rupee_symbol), feesItemList.get(position).getDueAmount()));
        }
        if (!TextUtils.isEmpty(feesItemList.get(position).getFeesPaid())) {
            feesPaid.setText(String.format("%s %s", getString(R.string.rupee_symbol), feesItemList.get(position).getFeesPaid()));
        }
        if (!TextUtils.isEmpty(feesItemList.get(position).getDatePaid())) {
            datePaid.setText(feesItemList.get(position).getDatePaid());
        }
        if (!TextUtils.isEmpty(feesItemList.get(position).getStatus())) {
            status.setText(capitalize(feesItemList.get(position).getStatus()));
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
