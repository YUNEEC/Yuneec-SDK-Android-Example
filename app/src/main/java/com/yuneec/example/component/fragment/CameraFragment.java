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
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yuneec.example.R;
import com.yuneec.example.component.custom_callback.VideoSurfaceHolderCallBack;
import com.yuneec.example.component.listeners.CameraListener;
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
        // TODO: stop player
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

        // TODO: deinit player
        if (videoSurfaceHolderCallBack != null) {
            videoSurfaceHolderCallBack = null;
        }
    }

    private void initViews(LayoutInflater inflater,
                           ViewGroup container) {

        rootView = inflater.inflate(R.layout.camera_layout, container, false);
        capturePicture = (Button) rootView.findViewById(R.id.capturePicture);
        captureVideo = (Button) rootView.findViewById(R.id.captureVideo);
        videoStreamView = (SurfaceView) rootView.findViewById(R.id.video_live_stream_view);
        surfaceHolder = videoStreamView.getHolder();
        // TODO: initiate player
        surfaceHolder.addCallback(videoSurfaceHolderCallBack);
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
