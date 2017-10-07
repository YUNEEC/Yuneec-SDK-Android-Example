/**
 * MainActivity.java Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec. All rights reserved.
 */

package com.yuneec.example.component.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yuneec.example.R;
import com.yuneec.example.component.custom_callback.OnChangeListener;
import com.yuneec.example.component.listeners.ConnectionListener;
import com.yuneec.example.component.listeners.TelemetryListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.example.view.CustomButton;
import com.yuneec.sdk.Camera;
import com.yuneec.sdk.Telemetry;

/**
 * Simple example demonstrating waypoints and mission,  based on the Yuneec SDK for Android
 */
public class MainActivity
        extends FragmentActivity implements View.OnClickListener, GoogleMap.OnMapClickListener, OnMapReadyCallback, OnChangeListener {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private Marker droneMarker = null;

    private GoogleMap googleMap = null;

    private Button locate, add, clear;

    private Button config, upload, start, stop;

    private TextView connectionStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        MultiDex.install(this);
        initUI();
    }

    private void initUI() {
        locate = (Button) findViewById(R.id.locate);
        add = (Button) findViewById(R.id.add);
        clear = (Button) findViewById(R.id.clear);
        config = (Button) findViewById(R.id.config);
        upload = (Button) findViewById(R.id.upload);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        connectionStatusText = (TextView) findViewById(R.id.ConnectStatusTextView);
        locate.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);
        config.setOnClickListener(this);
        upload.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        TelemetryListener.registerPositionListener(this);
    }

    private void unRegisterListeners() {

        ConnectionListener.unRegisterConnectionListener();
        TelemetryListener.unRegisterBatteryListener();
        TelemetryListener.unRegisterPositionListener();
    }

    @Override
    public void publishConnectionStatus(final String connectionStatus) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Log.d(TAG, connectionStatus);
                connectionStatusText.setText(connectionStatus);
            }
        });
    }

    @Override
    public void publishBatteryChangeStatus(final String batteryStatus) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Log.d(TAG, batteryStatus);
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
    public void publishPositionStatus(final Telemetry.Position position) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, position.latitudeDeg +  " " + position.longitudeDeg);
                Common.droneLat = position.latitudeDeg;
                Common.droneLong = position.longitudeDeg;
                updateDroneLocation();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locate: {
                updateDroneLocation();
                break;
            }
            default:
                break;
        }
    }

    public void cameraUpdate() {
        LatLng pos = new LatLng(Common.droneLat, Common.droneLong);
        float zoomlevel = (float) 18.0;
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(pos, zoomlevel);
        googleMap.moveCamera(cu);
    }

    public void updateDroneLocation() {
        LatLng pos = new LatLng(Common.droneLat, Common.droneLong);
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.drone_icon));
        if (droneMarker != null) {
            droneMarker.remove();
        }
        if (checkGpsCoordinates(Common.droneLat, Common.droneLong)) {
            droneMarker = googleMap.addMarker(markerOptions);
        }
        cameraUpdate();
    }

    public static boolean checkGpsCoordinates(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap mGoogleMap) {
        if(googleMap == null) {
            googleMap = mGoogleMap;
            setUpMap();
        }
        LatLng US = new LatLng(40.71, -74.00);
        googleMap.addMarker(new MarkerOptions().position(US));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(US));
    }

    private void setUpMap() {
        googleMap.setOnMapClickListener(this);
    }
}

