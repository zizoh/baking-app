package com.zizohanto.bakingapp.ui.recipedetail;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.step.Step;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link ActDetailMaster}
 * in two-pane mode (on tablets) or a {@link ActDetailDetail}
 * on handsets.
 */
@SuppressWarnings("RedundantCast")
public class FragRecipeDetail extends Fragment implements Player.EventListener {

    public static final String ARG_STEP = "com.zizohanto.bakingapp.ui.recipedetail.ARG_STEP";

    private Step mStep;

    private TextView mStepShortDescription;
    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private ImageView mThumbnail;
    private Context mContext;
    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragRecipeDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        if (getArguments().containsKey(ARG_STEP)) {
            mStep = getArguments().getParcelable(ARG_STEP);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null && mStep != null) {
                appBarLayout.setTitle(String.valueOf(mStep.getId()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        mStepShortDescription = (TextView) rootView.findViewById(R.id.recipe_step_detail);
        mPlayerView = (PlayerView) rootView.findViewById(R.id.playerView);
        mThumbnail = (ImageView) rootView.findViewById(R.id.thumbnail);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stepStepData(mStep);
    }

    private void stepStepData(Step step) {
        mStepShortDescription.setText(String.valueOf(step.getShortDescription()));
    }

    /**
     * Initialize ExoPlayer.
     */
    private void initializePlayer() {
        String videoUrl = mStep.getVideoURL();
        String thumbnail = mStep.getThumbnailURL();
        if (videoUrl.isEmpty()) {
            if (!thumbnail.isEmpty()) {
                Picasso.with(mContext)
                        .load(thumbnail)
                        .error(mContext.getResources().getDrawable(R.drawable.dummy_recipe_preview))
                        .placeholder(mContext.getResources().getDrawable(R.drawable.dummy_recipe_preview))
                        .into(mThumbnail);
            }

            mThumbnail.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
            return;
        }
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            // Produces DataSource instances through which media data is loaded.
            String userAgent = Util.getUserAgent(mContext, "BakingApplication");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent);

            // MediaSource representing the media to be played.
            Uri mediaUri = Uri.parse(videoUrl);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaUri);

            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
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
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
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
        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
