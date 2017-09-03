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
import com.yuneec.rtvplayer.RTVPlayer;
import com.yuneec.sdk.Camera;


public class CameraFragment
    extends Fragment implements
    View.OnClickListener {

    private View rootView;

    private static final String TAG = CameraFragment.class.getCanonicalName();

    private Toast toast = null;

    Button capturePicture;

    Button captureVideo;

    SurfaceView videoStreamView;

    SurfaceHolder surfaceHolder;

    Surface videoSurface;

    RTVPlayer rtvPlayer;

    VideoSurfaceHolderCallBack videoSurfaceHolderCallBack;

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
        addOnClickListeners();
        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();
        registerListener();
    }

    @Override
    public void onStop() {

        super.onStop();
        unRegisterListener();
    }

    @Override
    public void onPause() {

        super.onPause();
        if (supportsVideoStreamLib()) {
            rtvPlayer.stop();
        }
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        deInitPlayer();
    }

    public void deInitPlayer() {

        if (supportsVideoStreamLib()) {
            rtvPlayer.deinit();
            if (videoSurfaceHolderCallBack != null) {
                videoSurfaceHolderCallBack = null;
            }
        }
    }


    private boolean supportsVideoStreamLib() {
        String arch = System.getProperty("os.arch");
        // The underlying ffmpeg implementation does not support any other architectures as of yet.
        return (arch.equals("x86") || arch.equals("x86_64") || arch.equals("armeabi-v7a"));
    }

    private void initViews(LayoutInflater inflater,
                           ViewGroup container) {

        rootView = inflater.inflate(R.layout.camera_layout, container, false);
        capturePicture = (Button) rootView.findViewById(R.id.capturePicture);
        captureVideo = (Button) rootView.findViewById(R.id.captureVideo);
        if (supportsVideoStreamLib()) {
            videoStreamView = (SurfaceView) rootView.findViewById(R.id.video_live_stream_view);
            surfaceHolder = videoStreamView.getHolder();
            rtvPlayer = RTVPlayer.getPlayer(RTVPlayer.PLAYER_FFMPEG);
            rtvPlayer.init(getActivity(), RTVPlayer.IMAGE_FORMAT_YV12, false);
            videoSurfaceHolderCallBack = new VideoSurfaceHolderCallBack(getActivity(), videoSurface, rtvPlayer);
            surfaceHolder.addCallback(videoSurfaceHolderCallBack);
        } else {
            Log.d(TAG, "Video stream not supported on this architecture.");
        }
    }


    private void registerListener() {

        CameraListener.registerCameraListener(getActivity());
    }

    private void unRegisterListener() {

        CameraListener.unRegisterCameraListener();
    }


    private void addOnClickListeners() {

        capturePicture.setOnClickListener(this);
        captureVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.capturePicture:
                Camera.asyncTakePhoto();
                Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone currentRingtone = RingtoneManager.getRingtone(getActivity(), notificationSound);
                currentRingtone.play();
                break;
            case R.id.captureVideo:
                if (captureVideo.getText()
                        .equals("Start Video")) {
                    Camera.asyncStartVideo();
                    captureVideo.setText("Stop Video");
                } else {
                    Camera.asyncStopVideo();
                    captureVideo.setText("Start Video");
                }
        }

    }
}
