/**
 * MainActivity.java Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec. All rights reserved.
 */

package com.yuneec.example.component.actitivty;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yuneec.example.R;
import com.yuneec.example.component.custom_callback.OnChangeListener;
import com.yuneec.example.component.fragment.ActionFragment;
import com.yuneec.example.component.fragment.CameraSettingsFragment;
import com.yuneec.example.component.fragment.ConnectionFragment;
import com.yuneec.example.component.fragment.GimbalFragment;
import com.yuneec.example.component.fragment.MediaDownloadFragment;
import com.yuneec.example.component.listeners.ConnectionListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.sdk.Camera;

/**
 * Simple example based on the Yuneec SDK for Android
 * <p>
 * The example has 3 tabs (fragments) called Telemetry, Action, Mission.
 */
public class MainActivity
    extends FragmentActivity
    implements OnChangeListener {

    private FragmentTabHost mTabHost;

    private Context context;

    private Button mediaCapture;

    private static final String TAG = MainActivity.class.getCanonicalName();

    private CameraSettingsFragment cameraSettingsFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        context = this;
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("connection")
                        .setIndicator("Home"), ConnectionFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("camera_settings")
                        .setIndicator("Camera"), CameraSettingsFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("actions")
                        .setIndicator("Actions"), ActionFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("gimbal")
                        .setIndicator("Gimbal"), GimbalFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("media-download")
                        .setIndicator("Media Download"), MediaDownloadFragment.class, null);

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) v.findViewById(android.R.id.title);
            tv.setTextColor(ContextCompat.getColor(this, R.color.orangeDark));
            tv.setTextSize(16);
        }

    }

    @Override
    protected void onStart() {

        super.onStart();
        registerListeners();
    }

    @Override
    protected void onStop() {

        super.onStop();
        unRegisterListeners();

    }

    private void registerListeners() {

        ConnectionListener.registerConnectionListener(this);
    }

    private void unRegisterListeners() {

        ConnectionListener.unRegisterConnectionListener();
    }

    @Override
    public void publishConnectionStatus(final String connectionStatus) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Log.d(TAG, connectionStatus);
                ConnectionFragment fragment = (ConnectionFragment) getSupportFragmentManager().findFragmentByTag(
                                                  "connection");
                fragment.setConnectionStateView(connectionStatus);

            }
        });
    }

    @Override
    public void publishBatteryChangeStatus(final String batteryStatus) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Log.d(TAG, batteryStatus);
                ConnectionFragment fragment = (ConnectionFragment) getSupportFragmentManager().findFragmentByTag(
                                                  "connection");
                fragment.setBatterStateView(batteryStatus);

            }
        });
    }

    @Override
    public void publishHealthChangeStatus(final String healthStatus) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Log.d(TAG, healthStatus);
                ConnectionFragment fragment = (ConnectionFragment) getSupportFragmentManager().findFragmentByTag(
                                                  "connection");
                fragment.setDroneHealthView(healthStatus);

            }
        });
    }

    @Override
    public void publishCameraResult(final String result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, result);
                if(cameraSettingsFragment == null) {
                    cameraSettingsFragment = (CameraSettingsFragment) getSupportFragmentManager().findFragmentByTag("camera_settings");
                }
                if (!result.equals("Success")) {
                    Common.makeToast(context, "Error! Please make sure SD card is inserted and try again");
                    if(cameraSettingsFragment.getCameraMode().equals(Camera.Mode.PHOTO) && cameraSettingsFragment.getIsPhotoInterval()) {
                        mediaCapture = (Button) findViewById(R.id.photo_interval);
                        if (mediaCapture.getText().equals(getString(R.string.stop_photo_interval))) {
                            mediaCapture.setText(getString(R.string.photo_interval));
                        }
                        cameraSettingsFragment.setIsPhotoInterval(false);
                    }
                    if(cameraSettingsFragment.getCameraMode().equals(Camera.Mode.VIDEO)) {
                        mediaCapture = (Button) findViewById(R.id.video);
                        if (mediaCapture.getText().equals(getString(R.string.stop_video))) {
                            mediaCapture.setText(getString(R.string.video));
                        }
                    }
                }
                else {
                    if(cameraSettingsFragment.getCameraMode().equals(Camera.Mode.VIDEO)) {
                        mediaCapture = (Button) findViewById(R.id.video);
                        if (mediaCapture.getText().equals(getString(R.string.video))) {
                            mediaCapture.setText(getString(R.string.stop_video));
                        }
                        else {
                            mediaCapture.setText(getString(R.string.video));
                        }

                    }
                    else if(cameraSettingsFragment.getCameraMode().equals(Camera.Mode.PHOTO) && cameraSettingsFragment.getIsPhotoInterval()) {
                        mediaCapture = (Button) findViewById(R.id.photo_interval);
                        if (mediaCapture.getText().equals(getString(R.string.photo_interval))) {
                            mediaCapture.setText(getString(R.string.stop_photo_interval));
                        }
                        else {
                            mediaCapture.setText(getString(R.string.photo_interval));
                            cameraSettingsFragment.setIsPhotoInterval(false);
                        }

                    }
                    else {
                        Common.makeToast(context, "Picture Capture Successful");
                    }
                }
            }
        });
    }

    @Override
    public void publishActionResult(final String result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Common.makeToast(context, result);
            }
        });
    }

    @Override
    public void publishCameraModeResult(final Camera.Mode mode, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!result.equals("Success")) {
                    Common.makeToast(context, "Please make sure SD card is inserted and try again");
                } else {
                    if(cameraSettingsFragment == null) {
                        cameraSettingsFragment = (CameraSettingsFragment) getSupportFragmentManager().findFragmentByTag("camera_settings");
                    }
                    cameraSettingsFragment.setCameraMode(mode);
                    if (mode.equals(Camera.Mode.PHOTO) && !cameraSettingsFragment.getIsPhotoInterval()) {
                        Camera.asyncTakePhoto();
                    } else if (mode.equals(Camera.Mode.VIDEO)) {
                        mediaCapture = (Button) findViewById(R.id.video);
                        if (mediaCapture.getText().equals(getString(R.string.video))) {
                            Camera.asyncStartVideo();
                        } else {
                            Camera.asyncStopVideo();
                        }
                    } else {
                        mediaCapture = (Button) findViewById(R.id.photo_interval);
                        if (mediaCapture.getText().equals(getString(R.string.photo_interval))) {
                            Camera.asyncStartPhotoInterval(Common.defaultPhotoIntervalInSeconds);
                        } else {
                            Camera.asyncStopPhotoInterval();
                        }
                    }
                }
            }
        });
    }
}

