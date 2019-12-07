package com.pb.apszone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
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
import static com.pb.apszone.utils.AppConstants.YOUTUBE_API_KEY;

public class YoutubeFragment extends BaseFragment implements YouTubePlayer.OnInitializedListener, VideoAdapter.OnItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.toolbar_youtube)
    Toolbar toolbarYoutube;
    @BindView(R.id.rvVideo)
    RecyclerView rvVideo;
    @BindView(R.id.txtViewVideoTitle)
    TextView txtViewVideoTitle;
    private List<VideoItem> videoItemList = new ArrayList<>();
    private List<VideoItem> videoItemTempList = new ArrayList<>();
    VideoAdapter videoAdapter;
    private String videoUrl;

    public YoutubeFragment() {
        // Required empty public constructor
    }

    public static YoutubeFragment newInstance() {
        return new YoutubeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);
        unbinder = ButterKnife.bind(this, view);
        YouTubePlayerSupportFragment youTubePlayerSupportFragment = (YouTubePlayerSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);
        if (youTubePlayerSupportFragment != null) {
            youTubePlayerSupportFragment.initialize(YOUTUBE_API_KEY, this);
        }
        if (getArguments() != null) {
            videoUrl = getArguments().getString(VIDEO_URL);
            videoItemList = getArguments().getParcelableArrayList(VIDEO_LIST);
            if (videoItemList != null) {
                videoItemTempList.addAll(videoItemList);
                for (int i = 0; i < videoItemList.size(); i++) {
                    if (TextUtils.equals(getArguments().getString(VIDEO_POSITION), videoItemList.get(i).getId())) {
                        videoItemList.remove(i);
                        i++;
                    }
                }
            }
            txtViewVideoTitle.setText(getArguments().getString(VIDEO_TITLE));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvVideo.setLayoutManager(layoutManager);
        videoAdapter = new VideoAdapter(videoItemList, this);
        rvVideo.setAdapter(videoAdapter);
        toolbarYoutube.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    }


    @Override
    public void getNetworkData(boolean status) {
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
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

        //Enables automatic control of orientation
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);

        if (!wasRestored) {
            youTubePlayer.setFullscreen(true);
            youTubePlayer.loadVideo(videoUrl);
            youTubePlayer.play();
        } else {
            youTubePlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        String videoUrl = videoItemList.get(position).getVideoUrl();
        List<VideoItem> videoItems = new ArrayList<>(videoItemTempList);
        bundle.putParcelableArrayList(VIDEO_LIST, (ArrayList<? extends Parcelable>) videoItems);
        bundle.putString(VIDEO_URL, videoUrl);
        bundle.putString(VIDEO_TITLE, videoItemList.get(position).getTitle());
        bundle.putString(VIDEO_POSITION, videoItemList.get(position).getId());
        Fragment youtubeFragment = YoutubeFragment.newInstance();
        youtubeFragment.setArguments(bundle);
        replaceFragment(youtubeFragment);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.dynamic_youtube_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
