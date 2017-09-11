/**
 * CameraFragment.java Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec. All rights reserved.
 */

package com.yuneec.example.component.fragment;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

import com.yuneec.example.R;
import com.yuneec.example.component.custom_callback.VideoSurfaceHolderCallBack;
import com.yuneec.example.component.listeners.CameraListener;
import com.yuneec.example.component.listeners.CameraModeListener;
import com.yuneec.example.component.listeners.GimbalListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.example.component.utils.Sounds;
import com.yuneec.rtvplayer.RTVPlayer;
import com.yuneec.sdk.Camera;
import com.yuneec.sdk.Gimbal;


public class CameraFragment
    extends Fragment implements
    View.OnClickListener {

    private View rootView;

    private static final String TAG = CameraFragment.class.getCanonicalName();

    private Button captureMedia;

    private Button togglePhotoVideo;

    private Button cameraSettings;

    private Button rotateClockwise;

    private Button rotateAnticlockwise;

    private Button media_download;

    SurfaceView videoStreamView;

    SurfaceHolder surfaceHolder;

    Surface videoSurface;

    RTVPlayer rtvPlayer;

    VideoSurfaceHolderCallBack videoSurfaceHolderCallBack;

    private Camera.Mode cameraMode = Camera.Mode.PHOTO;

    private boolean isRecording = false;

    private boolean initCameraMode = false;

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
        registerListeners();
    }

    @Override
    public void onStop() {

        super.onStop();
        unRegisterListeners();
    }

    @Override
    public void onPause() {

        super.onPause();
        rtvPlayer.stop();
    }

    @Override
    public void onResume() {

        super.onResume();
        if(!rtvPlayer.isPlaying()) {
            rtvPlayer.play();
        }
        initCameraMode();
    }

    private void initCameraMode() {
        Camera.setMode(Camera.Mode.PHOTO, CameraModeListener.getCameraModeListener());
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
        captureMedia = (Button) rootView.findViewById(R.id.captureMedia);
        cameraSettings = (Button) rootView.findViewById(R.id.cameraSettings);
        rotateClockwise = (Button) rootView.findViewById(R.id.gimbal_rotate_clockwise);
        rotateAnticlockwise = (Button) rootView.findViewById(R.id.gimbal_rotate_anticlockwise);
        media_download = (Button) rootView.findViewById(R.id.media_download);
        videoStreamView = (SurfaceView) rootView.findViewById(R.id.video_live_stream_view);
        togglePhotoVideo = (Button) rootView.findViewById(R.id.togglePhotoVideo);
        surfaceHolder = videoStreamView.getHolder();
        rtvPlayer = RTVPlayer.getPlayer(RTVPlayer.PLAYER_FFMPEG);
        rtvPlayer.init(getActivity(), RTVPlayer.IMAGE_FORMAT_YV12, false);
        videoSurfaceHolderCallBack = new VideoSurfaceHolderCallBack(getActivity(), videoSurface, rtvPlayer);
        surfaceHolder.addCallback(videoSurfaceHolderCallBack);
    }


    private void registerListeners() {

        CameraListener.registerCameraListener(getActivity());
        GimbalListener.registerGimbalListener();
        CameraModeListener.registerCameraModeListener();
    }

    private void unRegisterListeners() {

        CameraListener.unRegisterCameraListener();
        GimbalListener.unRegisterGimbalListener();
        CameraModeListener.unRegisterCameraModeListener();
    }


    private void addOnClickListeners() {

        captureMedia.setOnClickListener(this);
        cameraSettings.setOnClickListener(this);
        rotateClockwise.setOnClickListener(this);
        rotateAnticlockwise.setOnClickListener(this);
        media_download.setOnClickListener(this);
        togglePhotoVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.captureMedia:
                Sounds.vibrate(getActivity());
                if(!initCameraMode) {
                    initCameraMode();
                    initCameraMode = true;
                }
                if(Common.isConnected) {
                    if(cameraMode.equals(Camera.Mode.PHOTO)) {
                        Camera.asyncTakePhoto();
                        Toast.makeText(getActivity(), "Picture Captured", Toast.LENGTH_LONG).show();
                        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone currentRingtone = RingtoneManager.getRingtone(getActivity(), notificationSound);
                        currentRingtone.play();
                    }
                    else {
                        if(!isRecording) {
                            Camera.asyncStartVideo();
                            Toast.makeText(getActivity(), "Video Recording Started", Toast.LENGTH_LONG).show();
                            captureMedia.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.video_stop));
                            isRecording = true;
                        }
                        else {
                            Camera.asyncStopVideo();
                            Toast.makeText(getActivity(), "Video Recording Stopped", Toast.LENGTH_LONG).show();
                            captureMedia.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.photo_capture));
                            isRecording=false;
                        }
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Please Connect To The Drone", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cameraSettings:
                DialogFragment settingsDialogFragment = new SettingsFragment();
                settingsDialogFragment.show(getFragmentManager(), "settings");
                Sounds.vibrate(getActivity());
                break;
            case R.id.gimbal_rotate_clockwise:
                Sounds.vibrate(getActivity());
                if(Common.isConnected) {
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
                }
                else {
                    Toast.makeText(getActivity(), "Please Connect To The Drone", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.gimbal_rotate_anticlockwise:
                Sounds.vibrate(getActivity());
                if(Common.isConnected) {
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
                }
                else {
                    Toast.makeText(getActivity(), "Please Connect To The Drone", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.media_download:
                Sounds.vibrate(getActivity());
                DialogFragment mediaDialogFragment = new MediaDownloadFragment();
                mediaDialogFragment.show(getFragmentManager(), "dialog");
                break;
            case R.id.togglePhotoVideo:
                Sounds.vibrate(getActivity());
                if(cameraMode.equals(Camera.Mode.PHOTO)) {
                    Camera.setMode(Camera.Mode.VIDEO, CameraModeListener.getCameraModeListener());
                    togglePhotoVideo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.video_switch));
                    cameraMode = Camera.Mode.VIDEO;
                }
                else {
                    Camera.setMode(Camera.Mode.PHOTO, CameraModeListener.getCameraModeListener());
                    togglePhotoVideo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.photo_switch));
                    cameraMode = Camera.Mode.PHOTO;
                }

        }

    }

}
