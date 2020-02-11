package com.pb.apszone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.TimetableItem;
import com.pb.apszone.service.model.TimetableResponseModel;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.TimetableAdapter;
import com.pb.apszone.viewModel.TimetableFragmentViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_FILTER_BY_DAY;
import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.CommonUtils.getDayOfWeek;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;

public class TimetableFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    private Unbinder unbinder;
    @BindView(R.id.toolbar_timetable)
    Toolbar toolbarTimetable;
    @BindView(R.id.rvTimetable)
    RecyclerView rvTimetable;
    @BindView(R.id.spinnerDay)
    Spinner spinnerDay;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView tvNoData;
    private List<TimetableItem> timetableItemList;
    private TimetableFragmentViewModel timetableFragmentViewModel;
    private KeyStorePref keyStorePref;
    private TimetableAdapter timetableAdapter;
    private String day;
    private int checkInit = 0;
    private String user_type;

    public TimetableFragment() {
        // Required empty public constructor
    }

    public static TimetableFragment newInstance() {
        return new TimetableFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyStorePref = KeyStorePref.getInstance(getContext());
        user_type = keyStorePref.getString(KEY_USER_TYPE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        unbinder = ButterKnife.bind(this, view);
        timetableItemList = new ArrayList<>();
        rvTimetable.setLayoutManager(new LinearLayoutManager(getActivity()));
        timetableAdapter = new TimetableAdapter(timetableItemList, user_type);
        rvTimetable.setAdapter(timetableAdapter);
        toolbarTimetable.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        day = getDayOfWeek();
        setUpSpinner();
        return view;
    }

    private void setUpSpinner() {
        String[] days = getResources().getStringArray(R.array.day);
        ArrayAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item, days);
        int pos = new ArrayList<>(Arrays.asList(days)).indexOf(day); // Getting current position by day
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerDay.setAdapter(adapter);
        spinnerDay.setSelection(pos); // Setting current day
        spinnerDay.setOnItemSelectedListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        timetableFragmentViewModel = new ViewModelProvider(this).get(TimetableFragmentViewModel.class);
        observeTimetable();
    }

    private void observeTimetable() {
        timetableFragmentViewModel.getTimetable().observe(getViewLifecycleOwner(), responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                if (timetableAdapter != null) {
                    timetableAdapter.clearData();
                }
                if (responseEvent.isSuccess()) {
                    TimetableResponseModel timetableResponseModel = responseEvent.getTimetableResponseModel();
                    if (!timetableResponseModel.isError()) {
                        List<TimetableItem> timetableItems = timetableResponseModel.getTimetable();
                        timetableItemList.addAll(timetableItems);
                        timetableAdapter.notifyDataSetChanged();
                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    @Override
    public void getNetworkData(boolean status) {
        if (status) {
            if (timetableAdapter != null) {
                timetableAdapter.clearData();
            }
            subscribe();
        }
    }

    private void subscribe() {
        if (!TextUtils.isEmpty(keyStorePref.getString(KEY_USER_ID))) {
            timetableFragmentViewModel.sendRequest(keyStorePref.getString(KEY_USER_ID), day, KEY_FILTER_BY_DAY, user_type);
        }
        tvNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        checkInit++;
        if (checkInit > 1) {
            day = parent.getItemAtPosition(position).toString();
            if (timetableAdapter != null) {
                timetableAdapter.clearData();
                subscribe();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
