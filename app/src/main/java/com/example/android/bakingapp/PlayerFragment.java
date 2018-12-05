package com.example.android.bakingapp;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;


// This fragment is called by the RecipeActivity in tablet mode and by RecipeDetailActivity on phones.


public class PlayerFragment extends Fragment {
    private static final String TAG = "PlayerFragment";
    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView mStepTextView;
    private Step mStep;
    private ImageView mImageView;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        mExoPlayerView = rootView.findViewById(R.id.player_view);
        mImageView = rootView.findViewById(R.id.image_view);
        mStepTextView = rootView.findViewById(R.id.step_description_text_view);

        try {
            mStep = getArguments().getParcelable(RecipeActivity.STEP);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (mStep != null) {
            String url1 = mStep.getVideoURL();
            String url2 = mStep.getThumbnailURL();

            if (!url1.isEmpty()) {
                mExoPlayerView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                LoadImageByUri(Uri.parse(url1));
            } else if (!url2.isEmpty()) {
                mExoPlayerView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                LoadImageByUri(Uri.parse(url2));
            } else {
                mExoPlayerView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
            }
        }
        try {
            mStepTextView.setText(mStep.getDescription());
        }catch (Exception e){
            e.printStackTrace();
        }

        setupBack(rootView);

        return rootView;
    }


    // Since we are having the manifest deal with configuration changes so the video does not restart on rotation


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.smallestScreenWidthDp < 600) {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mStepTextView.setVisibility(View.VISIBLE);
                mStepTextView.setText(mStep.getDescription());
                mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            } else {
                if (mExoPlayerView.getVisibility() != View.GONE) {
                    mStepTextView.setVisibility(View.GONE);
                    mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
                }
            }
        }
    }


    private void LoadImageByUri(Uri uri) {
        Context context = getContext();
        Picasso
                .with(context)
                .load(uri)
                .placeholder(R.drawable.cookies)
                .into(mImageView);
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(context, "BakingApp");

            assert context != null;
            MediaSource mediaSource = new ExtractorMediaSource(uri,
                    new DefaultDataSourceFactory(context, userAgent), new DefaultExtractorsFactory(),
                    null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
        }

    }
    private void pausePlayer() {
        if (mExoPlayer != null && mImageView !=null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.getPlaybackState();
        }
    }
    private void startPlayer() {
        if (mExoPlayer != null && mImageView !=null) {
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.getPlaybackState();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            pausePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startPlayer();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void setupBack(View root) {
        ImageButton mBack = (ImageButton) root.findViewById(R.id.back_but);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }


}