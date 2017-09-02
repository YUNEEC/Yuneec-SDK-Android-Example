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
import android.os.Vibrator;
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
import com.yuneec.example.component.listeners.GimbalListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.rtvplayer.RTVPlayer;
import com.yuneec.sdk.Camera;
import com.yuneec.sdk.Gimbal;


public class CameraFragment
    extends Fragment implements
    View.OnClickListener {

    private View rootView;

    private static final String TAG = CameraFragment.class.getCanonicalName();

    private Toast toast = null;

    Button capturePicture;

    //Button captureVideo;

    Button cameraSettings;

    Button rotateClockwise;

    Button rotateAnticlockwise;

    Button media_download;

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
        rtvPlayer.stop();
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

        rtvPlayer.deinit();
        if (videoSurfaceHolderCallBack != null) {
            videoSurfaceHolderCallBack = null;
        }
    }

    private void initViews(LayoutInflater inflater,
                           ViewGroup container) {

        rootView = inflater.inflate(R.layout.camera_layout, container, false);
        capturePicture = (Button) rootView.findViewById(R.id.capturePicture);
        cameraSettings = (Button) rootView.findViewById(R.id.cameraSettings);
        rotateClockwise = (Button) rootView.findViewById(R.id.gimbal_rotate_clockwise);
        rotateAnticlockwise = (Button) rootView.findViewById(R.id.gimbal_rotate_anticlockwise);
        media_download = (Button) rootView.findViewById(R.id.media_download);
        //captureVideo = (Button) rootView.findViewById(R.id.captureVideo);
        videoStreamView = (SurfaceView) rootView.findViewById(R.id.video_live_stream_view);
        surfaceHolder = videoStreamView.getHolder();
        rtvPlayer = RTVPlayer.getPlayer(RTVPlayer.PLAYER_FFMPEG);
        rtvPlayer.init(getActivity(), RTVPlayer.IMAGE_FORMAT_YV12, false);
        videoSurfaceHolderCallBack = new VideoSurfaceHolderCallBack(getActivity(), videoSurface, rtvPlayer);
        surfaceHolder.addCallback(videoSurfaceHolderCallBack);
    }


    private void registerListener() {

        CameraListener.registerCameraListener(getActivity());
        GimbalListener.registerGimbalListener();
    }

    private void unRegisterListener() {

        CameraListener.unRegisterCameraListener();
        GimbalListener.unRegisterGimbalListener();
    }


    private void addOnClickListeners() {

        capturePicture.setOnClickListener(this);
        cameraSettings.setOnClickListener(this);
        rotateClockwise.setOnClickListener(this);
        rotateAnticlockwise.setOnClickListener(this);
        media_download.setOnClickListener(this);
        //captureVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.capturePicture:
                Camera.asyncTakePhoto();
                vibrate();
                Toast.makeText(getActivity(), "Picture Captured", Toast.LENGTH_LONG).show();
                Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone currentRingtone = RingtoneManager.getRingtone(getActivity(), notificationSound);
                currentRingtone.play();
                break;
            case R.id.cameraSettings:
                vibrate();
                break;
            case R.id.gimbal_rotate_clockwise:
                vibrate();
                if(Common.currentRotation + Common.fixedRotationAngleDeg >= 180) {
                    Common.currentRotation = 0;
                    Gimbal.asyncSetPitchAndYawOfJni(0, Common.currentRotation,
                            GimbalListener.getGimbaListener());
                }
                else {
                    Common.currentRotation += Common.fixedRotationAngleDeg;
                    Gimbal.asyncSetPitchAndYawOfJni(0, Common.currentRotation,
                            GimbalListener.getGimbaListener());
                }
                break;
            case R.id.gimbal_rotate_anticlockwise:
                vibrate();
                if(Common.currentRotation - Common.fixedRotationAngleDeg <= -180) {
                    Common.currentRotation = 0;
                    Gimbal.asyncSetPitchAndYawOfJni(0, Common.currentRotation,
                            GimbalListener.getGimbaListener());
                }
                else {
                    Common.currentRotation -= Common.fixedRotationAngleDeg;
                    Gimbal.asyncSetPitchAndYawOfJni(0, Common.currentRotation,
                            GimbalListener.getGimbaListener());
                }
                break;
            case R.id.media_download:
                vibrate();
                DialogFragment dialogFragment = new MediaDownloadFragment();
                dialogFragment.show(getFragmentManager(), "dialog");
                break;
            /*case R.id.captureVideo:
                if (captureVideo.getText()
                        .equals("Start Video")) {
                    Camera.asyncStartVideo();
                    captureVideo.setText("Stop Video");
                } else {
                    Camera.asyncStopVideo();
                    captureVideo.setText("Start Video");
                }*/
        }

    }

    void vibrate() {
        Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(100);
    }
}
