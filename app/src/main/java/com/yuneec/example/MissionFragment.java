/**
 * MissionFragment.java
 * Yuneec-SDK-Android-Example
 *
 * Copyright @ 2016-2017 Yuneec.
 * All rights reserved.
 */
package com.yuneec.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;

import com.yuneec.sdk.Mission;
import com.yuneec.sdk.MissionItem;


public class MissionFragment extends Fragment implements View.OnClickListener{

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
                                              float relativeAltitudeM) {

        MissionItem newItem = new MissionItem();
        newItem.setPosition(latitudeDeg, longitudeM);
        newItem.setRelativeAltitude(relativeAltitudeM);

        return newItem;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mission_send_button:

                ArrayList<MissionItem> missionItems = new ArrayList<MissionItem>();
                missionItems.add(makeMissionItem(47.398243367, 8.545548536, 10.0f));
                missionItems.add(makeMissionItem(47.398283893, 8.544984959, 10.0f));
                missionItems.add(makeMissionItem(47.398053226, 8.544522415, 10.0f));
                missionItems.add(makeMissionItem(47.397787369, 8.544570259, 10.0f));
                missionItems.add(makeMissionItem(47.397768778, 8.545077389, 10.0f));
                missionItems.add(makeMissionItem(47.397986475, 8.545551966, 10.0f));

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
