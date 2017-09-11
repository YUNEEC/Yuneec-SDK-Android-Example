/**
 * MainActivity.java Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec. All rights reserved.
 */

package com.yuneec.example.component.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yuneec.example.R;
import com.yuneec.example.component.custom_callback.OnChangeListener;
import com.yuneec.example.component.fragment.CameraFragment;
import com.yuneec.example.component.fragment.MissionFragment;
import com.yuneec.example.component.listeners.ConnectionListener;
import com.yuneec.example.component.listeners.TelemetryListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.example.component.utils.Sounds;
import com.yuneec.example.view.CustomTextView;

/**
 * Simple example based on the Yuneec SDK for Android
 * <p>
 * The example has 3 tabs (fragments) called Telemetry, Action, Mission.
 */
public class MainActivity
    extends AppCompatActivity
    implements OnChangeListener, View.OnClickListener {

    private FragmentTabHost mTabHost;

    CustomTextView connectionStateText;

    CustomTextView batteryLevel;

    ImageButton batteryIcon;

    ImageButton waypointIcon;

    Context context;

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();
        context = this;
    }

    void initViews() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.custom_action_bar_layout,
                null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(view, layoutParams);
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0,0);
        connectionStateText = (CustomTextView) view.findViewById(R.id.connection_state);
        batteryLevel = (CustomTextView) view.findViewById(R.id.battery_level);
        batteryIcon = (ImageButton) view.findViewById(R.id.battery_status_icon);
        waypointIcon = (ImageButton) view.findViewById(R.id.waypoint_icon);
        setOnClickListeners();
        FragmentManager fragmentManager = getSupportFragmentManager();
        CameraFragment cameraFragment = new CameraFragment();
        fragmentManager.beginTransaction().add(R.id.fragment_view, cameraFragment).commit();
    }

    private void setOnClickListeners() {
        waypointIcon.setOnClickListener(this);
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
        TelemetryListener.registerBatteryListener(this);
        TelemetryListener.registerHealthListener(this);
    }

    private void unRegisterListeners() {

        ConnectionListener.unRegisterConnectionListener();
        TelemetryListener.unRegisterBatteryListener();
        TelemetryListener.unRegisterBatteryListener();
    }

    @Override
    public void publishConnectionStatus(final String connectionStatus) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Log.d(TAG, connectionStatus);
                connectionStateText.setText(connectionStatus);
                Common.connectionStatus = connectionStatus;
                if(Common.isConnected) {
                    SurfaceView view = (SurfaceView) findViewById(R.id.video_live_stream_view);
                    if(view!=null) {
                        view.setBackground(null);
                        Log.d(TAG, "Background null");
                    }
                }
                if(!Common.isConnected) {
                    SurfaceView view = (SurfaceView) findViewById(R.id.video_live_stream_view);
                    if(view!=null) {
                        view.setBackground(ContextCompat.getDrawable(context, R.drawable.background));
                        Log.d(TAG, "Background Added");
                    }
                }
            }
        });
    }

    @Override
    public void publishBatteryChangeStatus(final String batteryStatus) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Log.d(TAG, batteryStatus);
                batteryLevel.setText(batteryStatus);
                try {
                    int batteryPercentage = Integer.parseInt(batteryStatus);
                    if (batteryPercentage <= 35) {
                        batteryIcon.setImageResource(R.drawable.battery1_android);
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void publishHealthChangeStatus(final String healthStatus) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Log.d(TAG, healthStatus);

            }
        });
    }

    @Override
    public void publishCameraResult(final String result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, result);
                if(!result.equals("Success")) {
                    Toast.makeText(context, result + "-" + "Please make sure SD card is inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.waypoint_icon:
                Sounds.vibrate(this);
                DialogFragment dialogFragment = new MissionFragment();
                dialogFragment.show(getSupportFragmentManager(), "Mission");
                break;
        }
    }
}


