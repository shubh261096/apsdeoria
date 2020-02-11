package com.pb.apszone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.LearnItem;
import com.pb.apszone.service.model.LearnResponseModel;
import com.pb.apszone.view.adapter.LearnAdapter;
import com.pb.apszone.viewModel.LearnFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.VIDEO_LIST;
import static com.pb.apszone.utils.CommonUtils.showInformativeDialog;

public class LearnFragment extends BaseFragment implements LearnAdapter.OnItemClickListener {

    private Unbinder unbinder;
    @BindView(R.id.toolbar_learn)
    Toolbar toolbarLearn;
    @BindView(R.id.rvLearn)
    RecyclerView rvLearn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView tvNoData;
    private List<LearnItem> learnItemList;
    private LearnFragmentViewModel learnFragmentViewModel;
    private LearnAdapter learnAdapter;

    public LearnFragment() {
        // Required empty public constructor
    }

    public static LearnFragment newInstance() {
        return new LearnFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        unbinder = ButterKnife.bind(this, view);
        learnItemList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvLearn.setLayoutManager(layoutManager);
        learnAdapter = new LearnAdapter(learnItemList, this);
        rvLearn.setAdapter(learnAdapter);
        toolbarLearn.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }

    @Override
    public void getNetworkData(boolean status) {
        if (status) {
            if (learnAdapter != null) {
                learnAdapter.clearData();
            }
            subscribe();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        learnFragmentViewModel = new ViewModelProvider(this).get(LearnFragmentViewModel.class);
        observeData();
    }

    private void observeData() {
        learnFragmentViewModel.getLearnVideo().observe(getViewLifecycleOwner(), responseEvent -> {
            if (responseEvent != null) {
                progressBar.setVisibility(View.GONE);

                if (learnAdapter != null) {
                    learnAdapter.clearData();
                }
                if (responseEvent.isSuccess()) {
                    LearnResponseModel learnResponseModel = responseEvent.getLearnResponseModel();
                    if (!learnResponseModel.isError()) {
                        List<LearnItem> learnItems = learnResponseModel.getLearn();
                        for (int i = 0; i < learnItems.size(); i++) {
                            if (!TextUtils.isEmpty(learnItems.get(i).getName()) && learnItems.get(i).getVideo() != null) {
                                learnItemList.add(learnItems.get(i));
                            }
                        }
                        learnAdapter.notifyDataSetChanged();
                        if (learnItemList.size() == 0) {
                            tvNoData.setVisibility(View.VISIBLE);
                        } else {
                            tvNoData.setVisibility(View.GONE);
                        }
                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    showInformativeDialog(getContext(), responseEvent.getErrorModel().getMessage());
                }
            }
        });
    }

    private void subscribe() {
        tvNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        learnFragmentViewModel.sendRequest();
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
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(VIDEO_LIST, (ArrayList<? extends Parcelable>) learnItemList.get(position).getVideo());
        Fragment videoFragment = VideoFragment.newInstance();
        videoFragment.setArguments(bundle);
        replaceFragment(videoFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.dynamic_learn_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
