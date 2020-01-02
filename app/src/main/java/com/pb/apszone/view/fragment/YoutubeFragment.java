package com.pb.apszone.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
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
    private String videoUrl;
    private YouTubePlayer mYouTubePlayer;

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
        YouTubePlayerSupportFragmentX youTubePlayerSupportFragment = (YouTubePlayerSupportFragmentX) getChildFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);
        if (youTubePlayerSupportFragment != null) {
            youTubePlayerSupportFragment.initialize(YOUTUBE_API_KEY, this);
        }
        if (getArguments() != null) {
            videoUrl = getArguments().getString(VIDEO_URL);
            videoItemList = getArguments().getParcelableArrayList(VIDEO_LIST);
            txtViewVideoTitle.setText(getArguments().getString(VIDEO_TITLE));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvVideo.setLayoutManager(layoutManager);
        VideoAdapter videoAdapter = new VideoAdapter(videoItemList, this);
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
            mYouTubePlayer = youTubePlayer;
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
        if (!TextUtils.equals(this.videoUrl, videoItemList.get(position).getVideoUrl())) {
            if (mYouTubePlayer != null) {
                mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                mYouTubePlayer.loadVideo(videoItemList.get(position).getVideoUrl());
                mYouTubePlayer.play();
                this.videoUrl = videoItemList.get(position).getVideoUrl();
                txtViewVideoTitle.setText(videoItemList.get(position).getTitle());
            }
        }
    }
}
