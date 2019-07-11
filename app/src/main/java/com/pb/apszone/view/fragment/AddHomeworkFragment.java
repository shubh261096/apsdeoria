package com.pb.apszone.view.fragment;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.rest.HomeworkRequestModel;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.viewModel.HomeworkTeacherFragmentViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.KEY_SUBJECT_ID;
import static com.pb.apszone.utils.AppConstants.KEY_TEACHER_ID;
import static com.pb.apszone.utils.CommonUtils.getTodayDate;

public class AddHomeworkFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_add_homework)
    Toolbar toolbarAddHomework;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    HomeworkTeacherFragmentViewModel homeworkTeacherFragmentViewModel;
    KeyStorePref keyStorePref;
    @BindView(R.id.date_filter)
    TextView dateFilter;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.remarks)
    EditText remarks;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.submit_homework)
    Button submitHomework;
    @BindView(R.id.ll_homework)
    LinearLayout llHomework;
    @BindView(R.id.exist_homework)
    TextView existHomework;
    HomeworkRequestModel homeworkRequestModel;
    private String teacher_id, subject_id, class_id;

    public AddHomeworkFragment() {
        // Required empty public constructor
    }

    public static AddHomeworkFragment newInstance() {
        return new AddHomeworkFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyStorePref = KeyStorePref.getInstance(getContext());
        if (getArguments() != null) {
            teacher_id = getArguments().getString(KEY_TEACHER_ID);
            subject_id = getArguments().getString(KEY_SUBJECT_ID);
            class_id = getArguments().getString(KEY_CLASS_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_homework, container, false);
        unbinder = ButterKnife.bind(this, view);
        dateFilter.setText(getTodayDate());
        llHomework.setVisibility(View.VISIBLE);
        toolbarAddHomework.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }

    private void createRequestModel() {
        homeworkRequestModel = new HomeworkRequestModel();
        homeworkRequestModel.setDate(dateFilter.getText().toString());
        homeworkRequestModel.setClassId(this.class_id);
        homeworkRequestModel.setSubjectId(this.subject_id);
        homeworkRequestModel.setTeacherId(this.teacher_id);
        if (!TextUtils.isEmpty(title.getText().toString())) {
            homeworkRequestModel.setDescription(title.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(description.getText().toString())) {
            homeworkRequestModel.setTitle(description.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(remarks.getText().toString())) {
            homeworkRequestModel.setRemarks(remarks.getText().toString().trim());
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        homeworkTeacherFragmentViewModel = ViewModelProviders.of(this).get(HomeworkTeacherFragmentViewModel.class);
        observeResponse();
    }

    private void observeResponse() {
        homeworkTeacherFragmentViewModel.getSubmitResponse().observe(this, commonResponseModel -> {
            if (commonResponseModel != null) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), commonResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                if (!commonResponseModel.isError()) {
                    Objects.requireNonNull(getActivity()).onBackPressed();
                } else {
                    llHomework.setVisibility(View.GONE);
                    existHomework.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void getNetworkData(boolean status) {
        if (status) {
            createRequestModel();
            subscribe(this.homeworkRequestModel);
        }
    }

    private void subscribe(HomeworkRequestModel homeworkRequestModel) {
        llHomework.setVisibility(View.GONE);
        existHomework.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        homeworkTeacherFragmentViewModel.addHomeworkRequest(homeworkRequestModel);
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

    @OnClick(R.id.date_filter)
    public void onDateFilterClicked() {
        final Calendar c = Calendar.getInstance();
        int mYear, mMonth, mDay;
        String currentDate = dateFilter.getText().toString();

        /* Setting the previous date selected in calendar */
        if (currentDate.length() > 0) {
            String[] data = currentDate.split("-");
            mYear = Integer.parseInt(data[0]);
            mMonth = Integer.parseInt(data[1]);
            mMonth = mMonth - 1;
            mDay = Integer.parseInt(data[2]);
        } else {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                (view1, year, monthOfYear, dayOfMonth) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(0);
                    cal.set(year, monthOfYear, dayOfMonth);
                    Date chosenDate = cal.getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    dateFilter.setText(dateFormat.format(chosenDate));
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    @OnClick(R.id.submit_homework)
    public void onSubmitHomeworkClicked() {
        if (validate()) {
            createRequestModel();
            subscribe(this.homeworkRequestModel);
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(title.getText().toString().trim())) {
            Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(description.getText().toString().trim())) {
            Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
