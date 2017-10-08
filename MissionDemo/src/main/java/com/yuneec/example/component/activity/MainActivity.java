/**
 * MainActivity.java Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec. All rights reserved.
 */

package com.yuneec.example.component.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.yuneec.example.component.listeners.MissionListener;
import com.yuneec.example.component.listeners.TelemetryListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.sdk.Camera;
import com.yuneec.sdk.Mission;
import com.yuneec.sdk.MissionItem;
import com.yuneec.sdk.Telemetry;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple example demonstrating waypoints and mission,  based on the Yuneec SDK for Android
 */
public class MainActivity
    extends FragmentActivity implements View.OnClickListener, GoogleMap.OnMapClickListener,
    OnMapReadyCallback, OnChangeListener {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private Marker droneMarker = null;

    private GoogleMap googleMap = null;

    private Button locate, add, clear;

    private Button upload, start, pause;

    private TextView connectionStatusText;

    private boolean isAdd = false;

    private final Map<Integer, Marker> mapMarkers = new ConcurrentHashMap<Integer, Marker>();

    ArrayList<MissionItem> missionItems = new ArrayList<MissionItem>();

    MissionListener missionListener = null;

    private float altitude = 100.0f;

    private float pitchDeg = 90f;

    private float yawDeg = 0f;

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
        upload = (Button) findViewById(R.id.upload);
        start = (Button) findViewById(R.id.start);
        pause = (Button) findViewById(R.id.pause);
        connectionStatusText = (TextView) findViewById(R.id.ConnectStatusTextView);
        locate.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);
        upload.setOnClickListener(this);
        start.setOnClickListener(this);
        pause.setOnClickListener(this);
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

    @Override
    public void onMapClick(LatLng point) {
        if (isAdd == true){
            showSettingsDialog(point);
        }
    }

    private void showSettingsDialog(final LatLng point){
        LinearLayout wayPointSettings = (LinearLayout)getLayoutInflater().inflate(R.layout.waypointconfig_layout, null);

        final TextView altitudeText = (TextView) wayPointSettings.findViewById(R.id.altitude);
        final TextView pitchDegText = (TextView) wayPointSettings.findViewById(R.id.pitch_deg);
        final TextView yawDegText = (TextView) wayPointSettings.findViewById(R.id.yaw_deg);

        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(wayPointSettings)
                .setPositiveButton("Okay",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {

                        String altitudeString = altitudeText.getText().toString();
                        altitude = Integer.parseInt(altitudeString);
                        String pitchDegString = pitchDegText.getText().toString();
                        pitchDeg = Integer.parseInt(pitchDegString);
                        String yawDegString = yawDegText.getText().toString();
                        yawDeg = Integer.parseInt(yawDegString);
                        markWaypoint(point);
                        MissionItem missionItem = makeMissionItem(point.latitude, point.longitude, altitude, MissionItem.CameraAction.TAKE_PHOTO , pitchDeg, yawDeg );
                        missionItems.add(missionItem);
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                })
                .create()
                .show();
    }

    private void setMissionItem () {

    }

    private void markWaypoint(LatLng point){
        //Create MarkerOptions object
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker = googleMap.addMarker(markerOptions);
        mapMarkers.put(mapMarkers.size(), marker);
    }

    private MissionItem makeMissionItem(double latitudeDeg, double longitudeM,
                                              float relativeAltitudeM,
                                              MissionItem.CameraAction cameraAction,
                                              float gimbalPitchDeg, float gimbalYawDeg) {

        MissionItem newItem = new MissionItem();
        newItem.setPosition(latitudeDeg, longitudeM);
        newItem.setRelativeAltitude(relativeAltitudeM);
        newItem.setCameraAction(cameraAction);
        newItem.setGimbalPitchAndYaw(gimbalPitchDeg, gimbalYawDeg);
        return newItem;
    }


    private void registerListeners() {
        missionListener = new MissionListener();
        ConnectionListener.registerConnectionListener(this);
        TelemetryListener.registerBatteryListener(this);
        TelemetryListener.registerPositionListener(this);
        missionListener.registerMissionResultListener(this);
        missionListener.registerMissionProgressListener(this);
    }

    private void unRegisterListeners() {

        ConnectionListener.unRegisterConnectionListener();
        TelemetryListener.unRegisterBatteryListener();
        TelemetryListener.unRegisterPositionListener();
        missionListener.unRegisterMissionProgresListener();
        missionListener.unRegisterMissionResultListener();
        missionListener = null;
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
            case R.id.locate:
                updateDroneLocation();
                break;
            case R.id.upload:
                Mission.sendMissionAsync(missionItems, missionListener.getMissionResultListener());
                Mission.subscribeProgress(missionListener.getMissionProgressListener());
                break;
            case R.id.start:
                Mission.startMissionAsync(missionListener.getMissionResultListener());
                break;
            case R.id.pause:
                Mission.pauseMissionAsync(missionListener.getMissionResultListener());
                break;
            case R.id.clear:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        googleMap.clear();
                    }
                });
                missionItems.clear();
                updateDroneLocation();
                break;
            case R.id.add:{
                enableDisableAdd();
                break;
            }
            default:
                break;
        }
    }

    private void enableDisableAdd(){
        if (isAdd == false) {
            isAdd = true;
            add.setText("Done");
        }else{
            isAdd = false;
            add.setText("Add");
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
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f
                && longitude != 0f);
    }

    @Override
    public void onMapReady(GoogleMap mGoogleMap) {
        if (googleMap == null) {
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

