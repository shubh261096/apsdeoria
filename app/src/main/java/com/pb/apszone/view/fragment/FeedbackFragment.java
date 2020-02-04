package com.pb.apszone.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.CommonResponseModel;
import com.pb.apszone.service.model.TeacherId;
import com.pb.apszone.service.model.TimetableItem;
import com.pb.apszone.service.model.TimetableResponseModel;
import com.pb.apszone.service.rest.model.FeedbackItem;
import com.pb.apszone.service.rest.model.FeedbackRequestModel;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.view.adapter.FeedbackAdapter;
import com.pb.apszone.view.listener.OnFeebackRatingEditListener;
import com.pb.apszone.viewModel.FeedbackFragmentViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.KEY_FILTER_BY_WEEK;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_STUDENT;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;

public class FeedbackFragment extends BaseFragment implements OnFeebackRatingEditListener {

    Unbinder unbinder;
    @BindView(R.id.toolbar_feedback)
    Toolbar toolbarFeedback;
    @BindView(R.id.rvFeedback)
    RecyclerView rvFeedback;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView tvNoData;
    @BindView(R.id.submit_feedback)
    Button submitFeedback;

    private FeedbackFragmentViewModel feedbackFragmentViewModel;
    private List<TeacherId> teacherIdList;
    private FeedbackAdapter feedbackAdapter;
    KeyStorePref keyStorePref;
    private boolean isFeedbackSubmit;


    public FeedbackFragment() {
        // Required empty public constructor
    }

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        keyStorePref = KeyStorePref.getInstance(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        unbinder = ButterKnife.bind(this, view);
        teacherIdList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvFeedback.setLayoutManager(layoutManager);
        feedbackAdapter = new FeedbackAdapter(teacherIdList, this);
        rvFeedback.setAdapter(feedbackAdapter);
        toolbarFeedback.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }

    @Override
    public void getNetworkData(boolean status) {
        if (status) {
            if (feedbackAdapter != null) {
                feedbackAdapter.clearData();
            }
            subscribeFeedback();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        feedbackFragmentViewModel = ViewModelProviders.of(this).get(FeedbackFragmentViewModel.class);
        observeFeedback();
        observeTimetable(); // This is done to get list of teacher's name
    }

    private void observeFeedback() {
        feedbackFragmentViewModel.checkFeedback().observe(this, responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                if (feedbackAdapter != null) {
                    feedbackAdapter.clearData();
                }

                if (responseEvent.isSuccess()) {
                    CommonResponseModel commonResponseModel = responseEvent.getCommonResponseModel();
                    if (!commonResponseModel.isError()) {
                        if (!isFeedbackSubmit) {
                            subscribeTimetable();
                        } else {
                            Toast.makeText(getContext(), commonResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            subscribeFeedback();
                        }
                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                        tvNoData.setText(commonResponseModel.getMessage());
                    }
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    private void observeTimetable() {
        feedbackFragmentViewModel.getTimetable().observe(this, responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                if (feedbackAdapter != null) {
                    feedbackAdapter.clearData();
                }

                if (responseEvent.isSuccess()) {
                    TimetableResponseModel timetableResponseModel = responseEvent.getTimetableResponseModel();
                    if (!timetableResponseModel.isError()) {
                        submitFeedback.setVisibility(View.VISIBLE);
                        List<TimetableItem> timetableItems = timetableResponseModel.getTimetable();

                        final Set<String> set = new HashSet<>();
                        for (int i = 0; i < timetableItems.size(); i++) {
                            if (timetableItems.get(i).getTeacherId() != null) {
                                if (set.add(timetableItems.get(i).getTeacherId().getId())) {
                                    TeacherId teacherId = new TeacherId();
                                    teacherId.setFullname(timetableItems.get(i).getTeacherId().getFullname());
                                    teacherId.setFeedback("");
                                    teacherId.setRating(0);
                                    teacherId.setId(timetableItems.get(i).getTeacherId().getId());
                                    teacherIdList.add(teacherId);
                                }
                            }
                        }
                        feedbackAdapter.notifyDataSetChanged();
                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    private void subscribeFeedback() {
        submitFeedback.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        feedbackFragmentViewModel.sendRequest();
    }

    private void subscribeTimetable() {
        if (TextUtils.equals(keyStorePref.getString(KEY_USER_TYPE), USER_TYPE_STUDENT)) {
            if (!TextUtils.isEmpty(keyStorePref.getString(KEY_STUDENT_CLASS_ID))) {
                feedbackFragmentViewModel.sendTimetableRequest(keyStorePref.getString(KEY_STUDENT_CLASS_ID), KEY_FILTER_BY_WEEK, USER_TYPE_STUDENT);
            }
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

    @OnClick(R.id.submit_feedback)
    public void onSubmitFeedbackButtonClicked() {
        List<FeedbackItem> feedbackItemList = new ArrayList<>();
        for (int i = 0; i < teacherIdList.size(); i++) {
            if (teacherIdList.get(i).getRating() == 0) {
                Toast.makeText(getContext(), R.string.msg_rating_required, Toast.LENGTH_SHORT).show();
                return;
            }
            FeedbackItem feedbackItem = new FeedbackItem();
            feedbackItem.setFeedback(teacherIdList.get(i).getFeedback());
            feedbackItem.setRating(teacherIdList.get(i).getRating());
            feedbackItem.setStudentId(keyStorePref.getString(KEY_STUDENT_ID));
            feedbackItem.setTeacherId(teacherIdList.get(i).getId());
            feedbackItemList.add(i, feedbackItem);
        }

        FeedbackRequestModel feedbackRequestModel = new FeedbackRequestModel();
        feedbackRequestModel.setFeedback(feedbackItemList);

        submitFeedback.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        feedbackFragmentViewModel.addFeedbackRequest(feedbackRequestModel);

        isFeedbackSubmit = true;

        feedbackAdapter.clearData();
    }

    @Override
    public void onRatingChanged(int position, int rating) {
        teacherIdList.get(position).setRating(rating);
    }

    @Override
    public void onFeedbackChanged(int position, String text) {
        teacherIdList.get(position).setFeedback(text);
    }
}
