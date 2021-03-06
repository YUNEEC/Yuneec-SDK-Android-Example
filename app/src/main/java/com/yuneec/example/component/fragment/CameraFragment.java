/**
 * CameraFragment.java Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec. All rights reserved.
 */

package com.yuneec.example.component.fragment;

import android.content.Context;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yuneec.example.R;
import com.yuneec.example.component.custom_callback.VideoSurfaceHolderCallBack;
import com.yuneec.example.component.listeners.CameraListener;
import com.yuneec.example.component.listeners.CameraModeListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.example.component.utils.Media;
import com.yuneec.sdk.Camera;
import com.yuneec.videostreaming.RTSPPlayer;
import com.yuneec.videostreaming.VideoPlayer;
import com.yuneec.videostreaming.VideoPlayerException;

import java.io.IOException;


public class CameraFragment
    extends Fragment implements
    View.OnClickListener {

    private View rootView;

    private static final String TAG = CameraFragment.class.getCanonicalName();

    private Toast toast = null;

    Button capturePicture;

    Button video;

    Button photoInterval;

    private Button cameraSettings;

    private Camera.Mode cameraMode = Camera.Mode.UNKNOWN;

    private boolean isPhotoInterval = false;

    private SurfaceView videoSurfaceView;

    private Surface videoSurface;

    private SurfaceHolder videoSurfaceHolder;

    private RTSPPlayer videoPlayer;


    public Camera.Mode getCameraMode() {
        return cameraMode;
    }

    public void setCameraMode(Camera.Mode cameraMode) {
        this.cameraMode = cameraMode;
    }

    public boolean getIsPhotoInterval() {
        return isPhotoInterval;
    }

    public void setIsPhotoInterval(boolean photoInterval) {
        isPhotoInterval = photoInterval;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initViews(inflater, container);
        videoPlayer = (RTSPPlayer) VideoPlayer.getPlayer(VideoPlayer.PlayerType.LIVE_STREAM);
        addOnClickListeners();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initVideoPlayer();
        registerListeners();
    }

    @Override
    public void onStop() {

        super.onStop();
        deInitVideoPlayer();
        unRegisterListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            videoPlayer.stop();
        } catch (VideoPlayerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (!videoPlayer.isPlaying()) {
                videoPlayer.start();
            }
        } catch (VideoPlayerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initVideoPlayer() {
        if (!Common.isConnected) {
            Common.makeToast(getActivity(), "Not connected to the drone!");
        } else {
            videoPlayer.initializePlayer();
            try {
                videoPlayer.setDataSource(Common.VideoStreamUrl);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            } catch (VideoPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    private void deInitVideoPlayer() {
        try {
            videoPlayer.stop();
            videoPlayer.releasePlayer();
        } catch (VideoPlayerException e) {
            e.printStackTrace();
        }
    }

    private void initViews(LayoutInflater inflater,
                           ViewGroup container) {

        rootView = inflater.inflate(R.layout.camera_layout, container, false);
        capturePicture = (Button) rootView.findViewById(R.id.capturePicture);
        video = (Button) rootView.findViewById(R.id.video);
        photoInterval = (Button) rootView.findViewById(R.id.photo_interval);
        cameraSettings = (Button) rootView.findViewById(R.id.camera_settings);
        videoSurfaceView = (SurfaceView) rootView.findViewById(R.id.video_view);
        videoSurfaceHolder = videoSurfaceView.getHolder();
        videoSurfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                videoSurface = holder.getSurface();
                try {
                    videoPlayer.setSurface(videoSurface);
                } catch (VideoPlayerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                videoSurface = holder.getSurface();
                try {
                    videoPlayer.setSurface(videoSurface);
                    videoPlayer.start();
                } catch (VideoPlayerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                try {
                    videoPlayer.setSurface(null);
                } catch (VideoPlayerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void registerListeners() {
        CameraListener.registerCameraListener(getActivity());
        CameraModeListener.registerCameraModeListener(getActivity());
    }

    private void unRegisterListeners() {
        CameraModeListener.unRegisterCameraModeListener();
        CameraListener.unRegisterCameraListener();
    }


    private void addOnClickListeners() {
        capturePicture.setOnClickListener(this);
        video.setOnClickListener(this);
        photoInterval.setOnClickListener(this);
        cameraSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (Common.isConnected) {
            Media.vibrate(getActivity());
            switch (v.getId()) {
                case R.id.capturePicture:
                    if (!getCameraMode().equals(Camera.Mode.PHOTO)) {
                        Camera.setMode(Camera.Mode.PHOTO, CameraModeListener.getCameraModeListener());
                    } else {
                        Camera.asyncTakePhoto();
                    }
                    break;
                case R.id.video:
                    if (!getCameraMode().equals(Camera.Mode.VIDEO)) {
                        Camera.setMode(Camera.Mode.VIDEO, CameraModeListener.getCameraModeListener());
                    } else {
                        if (video.getText().equals(getString(R.string.video))) {
                            Camera.asyncStartVideo();
                        } else {
                            Camera.asyncStopVideo();
                        }
                    }
                    break;
                case R.id.photo_interval:
                    Media.vibrate(getActivity());
                    setIsPhotoInterval(true);
                    if (!getCameraMode().equals(Camera.Mode.PHOTO)) {
                        Camera.setMode(Camera.Mode.PHOTO, CameraModeListener.getCameraModeListener());
                    } else {
                        if (photoInterval.getText().equals(getString(R.string.photo_interval))) {
                            Camera.asyncStartPhotoInterval(Common.defaultPhotoIntervalInSeconds);
                        } else {
                            Camera.asyncStopPhotoInterval();
                        }
                    }
                    break;
                case R.id.camera_settings:
                    DialogFragment settingsDialogFragment = new CameraSettingsFragment();
                    settingsDialogFragment.show(getFragmentManager(), "settings");
                    Media.vibrate(getActivity());
                    break;
            }
        } else {
            Common.makeToast(getActivity(), "Please Connect To The Drone");
        }


    }
}
