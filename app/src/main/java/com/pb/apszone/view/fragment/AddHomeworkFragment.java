package com.pb.apszone.view.fragment;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.KEY_SUBJECT_ID;
import static com.pb.apszone.utils.AppConstants.KEY_SUBJECT_NAME;
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
    @BindView(R.id.ll_exist_homework)
    LinearLayout llExistHomework;
    @BindView(R.id.tvSubjectName)
    TextView tvSubjectName;
    @BindView(R.id.add_homework)
    TextView addHomework;
    HomeworkRequestModel homeworkRequestModel;
    @BindView(R.id.til_title)
    TextInputLayout tilTitle;
    @BindView(R.id.til_description)
    TextInputLayout tilDescription;
    @BindView(R.id.til_remarks)
    TextInputLayout tilRemarks;
    private String teacher_id, subject_id, class_id, subject_name;

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
            subject_name = getArguments().getString(KEY_SUBJECT_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_homework, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvSubjectName.setText(subject_name);
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
        if (!TextUtils.isEmpty(Objects.requireNonNull(tilTitle.getEditText()).getText().toString())) {
            homeworkRequestModel.setDescription(tilTitle.getEditText().getText().toString().trim());
        }
        if (!TextUtils.isEmpty(Objects.requireNonNull(tilDescription.getEditText()).getText().toString())) {
            homeworkRequestModel.setTitle(tilDescription.getEditText().getText().toString().trim());
        }
        if (!TextUtils.isEmpty(Objects.requireNonNull(tilRemarks.getEditText()).getText().toString())) {
            homeworkRequestModel.setRemarks(tilRemarks.getEditText().getText().toString().trim());
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
                if (!commonResponseModel.isError()) {
                    Toast.makeText(getContext(), commonResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity()).onBackPressed();
                } else {
                    if (commonResponseModel.getData() != null) {
                        llHomework.setVisibility(View.VISIBLE);
                        llExistHomework.setVisibility(View.GONE);
                    } else {
                        llHomework.setVisibility(View.GONE);
                        llExistHomework.setVisibility(View.VISIBLE);
                    }
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
        llExistHomework.setVisibility(View.GONE);
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

    @OnClick(R.id.add_homework)
    public void onAddHomeworkClicked() {
        llExistHomework.setVisibility(View.GONE);
        llHomework.setVisibility(View.VISIBLE);
    }

    private boolean validate() {
        if (TextUtils.isEmpty(Objects.requireNonNull(tilTitle.getEditText()).getText().toString().trim())) {
            tilTitle.setError("Title is required");
            return false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(tilDescription.getEditText()).getText().toString().trim())) {
            tilDescription.setError("Description is required");
            return false;
        }
        return true;
    }

    @OnTextChanged(value = R.id.title, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTitleTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            if (tilTitle.isErrorEnabled()) {
                tilTitle.setErrorEnabled(false);
                tilTitle.setHelperTextEnabled(true);
            } else {
                tilTitle.setHelperTextEnabled(true);
            }
        } else {
            tilTitle.setErrorEnabled(true);
            tilTitle.setError("Title is required");
        }
    }

    @OnTextChanged(value = R.id.description, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onDescriptionTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            if (tilDescription.isErrorEnabled()) {
                tilDescription.setErrorEnabled(false);
                tilDescription.setHelperTextEnabled(true);
            } else {
                tilDescription.setHelperTextEnabled(true);
            }
        } else {
            tilDescription.setError("Description is required");
        }
    }
}
