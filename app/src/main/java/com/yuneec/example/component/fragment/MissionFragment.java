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

import com.yuneec.example.R;
import com.yuneec.sdk.Mission;
import com.yuneec.sdk.MissionItem;

import java.util.ArrayList;


public class MissionFragment extends Fragment implements View.OnClickListener {

    View rootView;

    Mission.ResultListener resultListener;
    Mission.ProgressListener progressListener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.mission_example, container, false);

        resultListener = new Mission.ResultListener() {
            @Override
            public void onResultCallback(final Mission.Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(rootView.getContext(), result.resultStr,
                                       Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        progressListener = new Mission.ProgressListener() {
            @Override
            public void onProgressUpdate(final int current, final int total) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        String text = String.format("Reached %d of %d", current, total);
                        Toast.makeText(rootView.getContext(), text, Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        rootView.findViewById(R.id.mission_send_button).setOnClickListener(this);
        rootView.findViewById(R.id.mission_start_button).setOnClickListener(this);
        rootView.findViewById(R.id.mission_pause_button).setOnClickListener(this);

        return rootView;
    }

    static public MissionItem makeMissionItem(double latitudeDeg, double longitudeM,
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
    }
}
