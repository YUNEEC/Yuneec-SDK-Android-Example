/**
 * MainActivity.java Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec. All rights reserved.
 */

package com.yuneec.example.component.actitivty;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yuneec.example.R;
import com.yuneec.example.component.custom_callback.OnChangeListener;
import com.yuneec.example.component.fragment.CameraFragment;
import com.yuneec.example.component.fragment.CameraSettingsFragment;
import com.yuneec.example.component.fragment.ConnectionFragment;
import com.yuneec.example.component.fragment.GimbalFragment;
import com.yuneec.example.component.fragment.MediaDownloadFragment;
import com.yuneec.example.component.listeners.ConnectionListener;

/**
 * Simple example based on the Yuneec SDK for Android
 * <p>
 * The example has 3 tabs (fragments) called Telemetry, Action, Mission.
 */
public class MainActivity
    extends FragmentActivity
    implements OnChangeListener {

    private FragmentTabHost mTabHost;

    //TextView connectionStateText;

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("connection")
                        .setIndicator("Connection Info"), ConnectionFragment.class, null);
        //mTabHost.addTab(mTabHost.newTabSpec("camera")
        //              .setIndicator("Camera"), CameraFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("camera_settings")
                        .setIndicator("Camera Settings"), CameraSettingsFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("gimbal")
                        .setIndicator("Gimbal"), GimbalFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("media-download")
                        .setIndicator("Media Download"), MediaDownloadFragment.class, null);

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) v.findViewById(android.R.id.title);
            tv.setTextColor(ContextCompat.getColor(this, R.color.orange));
            tv.setTextSize(16);
        }

        //connectionStateText = ( TextView ) findViewById ( R.id.connection_state_text );
        //connectionStateText.setText ( "Not connected" );
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
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }

}


