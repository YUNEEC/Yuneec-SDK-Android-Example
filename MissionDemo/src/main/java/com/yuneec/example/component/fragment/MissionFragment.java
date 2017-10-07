/**
 * MissionFragment.java
 * Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec.
 * All rights reserved.
 */
package com.yuneec.example.component.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yuneec.example.R;
import com.yuneec.example.component.utils.Common;
import com.yuneec.sdk.Mission;
import com.yuneec.sdk.MissionItem;

import java.util.ArrayList;


public class MissionFragment extends Fragment implements View.OnClickListener, GoogleMap.OnMapClickListener, OnMapReadyCallback{

    View rootView;

    Mission.ResultListener resultListener;
    Mission.ProgressListener progressListener;

    Marker droneMarker = null;

    GoogleMap googleMap = null;

    MapView mapView = null;




    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.mission_layout, container, false);

        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        return rootView;
    }

    /*static public MissionItem makeMissionItem(double latitudeDeg, double longitudeM,
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mission_send_button:

                ArrayList<MissionItem> missionItems = new ArrayList<MissionItem>();
                missionItems.add(makeMissionItem(47.40328702, 8.45186958, 10.0f,
                                                 MissionItem.CameraAction.START_VIDEO, 0.0f, 0.0f));
                missionItems.add(makeMissionItem(47.40321712, 8.45205203, 10.0f, MissionItem.CameraAction.NONE,
                                                 -30.0f, 30.0f));
                missionItems.add(makeMissionItem(47.40309596, 8.45195392, 10.0f, MissionItem.CameraAction.NONE,
                                                 -60.0f, -30.0f));
                missionItems.add(makeMissionItem(47.40302956, 8.45213636, 10.0f, MissionItem.CameraAction.NONE,
                                                 -90.0f, 0.0f));
                missionItems.add(makeMissionItem(47.40314839, 8.45223619, 10.0f,
                                                 MissionItem.CameraAction.STOP_VIDEO, 0.0f, 0.0f));
                missionItems.add(makeMissionItem(47.40309014, 8.45241003, 10.0f,
                                                 MissionItem.CameraAction.TAKE_PHOTO, -90.0f, 0.0f));
                missionItems.add(makeMissionItem(47.40285248, 8.45218627, 10.0f,
                                                 MissionItem.CameraAction.TAKE_PHOTO, -90.0f, 0.0f));
                missionItems.add(makeMissionItem(47.40289092, 8.45208473, 10.0f,
                                                 MissionItem.CameraAction.TAKE_PHOTO, -45.0f, 0.0f));
                missionItems.add(makeMissionItem(47.40292812, 8.45200039, 10.0f,
                                                 MissionItem.CameraAction.TAKE_PHOTO, -45.0f, 90.0f));
                missionItems.add(makeMissionItem(47.40296548, 8.45189884, 10.0f,
                                                 MissionItem.CameraAction.TAKE_PHOTO, 0.0f, -90.0f));
                missionItems.add(makeMissionItem(47.40301907, 8.45175942, 10.0f,
                                                 MissionItem.CameraAction.TAKE_PHOTO, 0.0f, 90.0f));

                Mission.subscribeProgress(progressListener);
                Mission.sendMissionAsync(missionItems, resultListener);
                break;

            case R.id.mission_start_button:
                Mission.startMissionAsync(resultListener);
                break;

            case R.id.mission_pause_button:
                Mission.pauseMissionAsync(resultListener);
                break;
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locate:{
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
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap == null) {
            this.googleMap = googleMap;
            setUpMap();
        }
        LatLng shenzhen = new LatLng(22.5362, 113.9454);
        googleMap.addMarker(new MarkerOptions().position(shenzhen).title("Marker in Shenzhen"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(shenzhen));
    }

    private void setUpMap() {
        googleMap.setOnMapClickListener(this);
    }
}
