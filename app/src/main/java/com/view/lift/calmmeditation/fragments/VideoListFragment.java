package com.view.lift.calmmeditation.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.view.lift.calmmeditation.R;
import com.view.lift.calmmeditation.adapter.VideoListAdapter;
import com.view.lift.calmmeditation.dto.Feed;
import com.view.lift.calmmeditation.dto.Item;
import com.view.lift.calmmeditation.interfaces.ItemClick;
import com.view.lift.calmmeditation.util.Utils;
import com.view.lift.calmmeditation.viewmodels.VideoViewModel;

import java.util.ArrayList;

public class VideoListFragment extends Fragment implements ItemClick, ExoPlayer.EventListener{


    RelativeLayout mainLayout;
    private View root;
    private RecyclerView videoListRecyclerView;
    private  GridLayoutManager gridLayoutManager;
    private VideoListAdapter videoListAdapter;

    private VideoViewModel videoViewModel;
    private FrameLayout videoViewFrameLayout;
    private ImageButton closeVideoViewImgBtn;

    private PlayerView playerView;
    private ExoPlayer player;
    private String lastPlaying;
    private int lastPosition = 0;
    private boolean isPreparing;


    private Feed currentFeed;

    private ProgressBar progressBar;


    public static VideoListFragment getInstance(Context context){
        VideoListFragment videoListFragment = new VideoListFragment();
        return videoListFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.fragment_video_list,null,false);
        mainLayout =  root.findViewById(R.id.fragment_video_list);
        progressBar = Utils.createProgressDialog(this.getContext(),mainLayout);

        videoListRecyclerView = root.findViewById(R.id.videoListRecyclerView);
        videoViewFrameLayout = root.findViewById(R.id.videoParentView);
        closeVideoViewImgBtn =  root.findViewById(R.id.closeVideoViewImgBtn);
        playerView = root.findViewById(R.id.video_view);

        closeVideoViewImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayer();
                removeDialog();
            }
        });

        setUpRecyclerView();
        initViewModel();
        return root;
    }

    public void showDialog(){
        if(progressBar!=null) {
            progressBar.setVisibility(View.VISIBLE);
        }
       // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
         //       WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void removeDialog(){
        if(progressBar!=null) {
            progressBar.setVisibility(View.GONE);
        }
       // getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void initViewModel() {

        showDialog();
        videoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        videoViewModel.getAllVideos().observe(this, new Observer<Feed>() {

            @Override
            public void onChanged(@Nullable Feed feed) {

                //Update RecyclerView

                if(feed !=  null) {
                    currentFeed = feed;
                    videoListAdapter.setDataSet(feed.getArticleList());
                    videoListAdapter.notifyDataSetChanged();
                }else{

                    Toast.makeText(getActivity().getApplicationContext(),"Not connected to any network!",Toast.LENGTH_SHORT).show();
                }
                removeDialog();
            }

        });
    }

    public void setUpRecyclerView(){
        int orientation = getResources().getConfiguration().orientation;


        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            if(gridLayoutManager == null){
                gridLayoutManager = new GridLayoutManager(getContext(),2);
                videoListRecyclerView.setLayoutManager(gridLayoutManager);
            }else{
                gridLayoutManager.setSpanCount(2);
            }



        }else{
            if(gridLayoutManager == null){
                gridLayoutManager = new GridLayoutManager(getContext(),1);
                videoListRecyclerView.setLayoutManager(gridLayoutManager);
            }else{
                gridLayoutManager.setSpanCount(1);
            }
        }

        if(videoListAdapter == null) {
            videoListAdapter = new VideoListAdapter(getContext(), this, new ArrayList<Item>(), orientation);
            videoListRecyclerView.setAdapter(videoListAdapter);
        }

        videoListAdapter.setOrientation(orientation);
    }


    @Override
    public void onItemClick(final int position) {
        if(currentFeed !=  null) {

            if(Utils.isNetworkAvailable(getContext())) {
                showDialog();

                videoViewFrameLayout.setVisibility(View.VISIBLE);
                lastPlaying = currentFeed.getArticleList().get(position).getMediaContent().getUrl();
                playVideo(lastPlaying, true, 0);
            }
            else{
                Toast.makeText(this.getContext(),"Not connected to network!",Toast.LENGTH_LONG).show();
            }
        }
    }


    private void initializePlayer() {

        player = ExoPlayerFactory.newSimpleInstance(this.getContext(),new DefaultRenderersFactory(this.getContext()),new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setPlayer(player);
        player.setPlayWhenReady(true);
        player.seekTo(0, 0);
        player.addListener(this);

    }

    public void playVideo(String url, boolean reset, int currentPosition){

            Uri uri = Uri.parse(url);
            MediaSource mediaSource = Utils.buildMediaSource(uri);
            if (player != null) {

                player.seekTo(currentPosition);
                isPreparing = true;
                player.prepare(mediaSource, reset, false);

            }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
            if(lastPlaying !=null) {
                playVideo(lastPlaying, false,lastPosition);
            }
        }else if(player != null){
            if(lastPlaying !=null) {
                playerView.onResume();
                playVideo(lastPlaying, false,lastPosition);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            player.stop();
        }else if(player != null) {
            lastPosition = (int)player.getCurrentPosition();
            playerView.onPause();
            player.stop();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUiFullScreen() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void showSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        playerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int currentOrientation = getResources().getConfiguration().orientation;
        if(currentOrientation == Configuration.ORIENTATION_LANDSCAPE)
        {

            hideSystemUi();//hideSystemUiFullScreen();
        }else{
            hideSystemUi();
        }
        //setupOrientation();
        setUpRecyclerView();

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(isPreparing && playbackState == ExoPlayer.STATE_READY){
            // this is accurate
            isPreparing = false;
            removeDialog();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Toast.makeText(getContext(),"Not connected to any network!",Toast.LENGTH_LONG).show();
        removeDialog();
        stopPlayer();
    }

    public void stopPlayer(){
        if (playerView != null && player != null) {
            player.stop();
        }
        videoViewFrameLayout.setVisibility(View.GONE);
        showSystemUI();

    }
}
