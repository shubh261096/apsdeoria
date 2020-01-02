package com.pb.apszone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pb.apszone.R;
import com.pb.apszone.service.model.VideoItem;
import com.pb.apszone.view.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pb.apszone.utils.AppConstants.VIDEO_LIST;
import static com.pb.apszone.utils.AppConstants.VIDEO_POSITION;
import static com.pb.apszone.utils.AppConstants.VIDEO_TITLE;
import static com.pb.apszone.utils.AppConstants.VIDEO_URL;

public class VideoFragment extends BaseFragment implements VideoAdapter.OnItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.toolbar_video)
    Toolbar toolbarVideo;
    @BindView(R.id.rvVideo)
    RecyclerView rvVideo;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView tvNoData;
    private List<VideoItem> videoItemList = new ArrayList<>();
    private VideoAdapter videoAdapter;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            videoItemList = getArguments().getParcelableArrayList(VIDEO_LIST);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvVideo.setLayoutManager(layoutManager);
        videoAdapter = new VideoAdapter(videoItemList, this);
        rvVideo.setAdapter(videoAdapter);
        toolbarVideo.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }

    @Override
    public void getNetworkData(boolean status) {

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        String videoUrl = videoItemList.get(position).getVideoUrl();
        List<VideoItem> videoItems = new ArrayList<>(videoItemList);
        bundle.putParcelableArrayList(VIDEO_LIST, (ArrayList<? extends Parcelable>) videoItems);
        bundle.putString(VIDEO_URL, videoUrl);
        bundle.putString(VIDEO_TITLE, videoItemList.get(position).getTitle());
        bundle.putString(VIDEO_POSITION, videoItemList.get(position).getId());
        Fragment youtubeFragment = YoutubeFragment.newInstance();
        youtubeFragment.setArguments(bundle);
        replaceFragment(youtubeFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.dynamic_video_frame, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
