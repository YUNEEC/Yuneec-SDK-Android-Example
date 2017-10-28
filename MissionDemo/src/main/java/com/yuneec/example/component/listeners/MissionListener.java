package com.yuneec.example.component.listeners;

import android.content.Context;
import android.util.Log;

import com.yuneec.example.component.custom_callback.OnChangeListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.sdk.Connection;
import com.yuneec.sdk.Mission;
import com.yuneec.sdk.Telemetry;

/**
 * Created by sushmas on 10/6/17.
 */

public class MissionListener {

    private Mission.ResultListener missionResultListener = null;

    private Mission.ProgressListener missionProgressListener = null;

    private final String TAG = MissionListener.class.getCanonicalName();

    private OnChangeListener onChangeListener;

    public void registerMissionResultListener(final Context context) {

        onChangeListener = (OnChangeListener) context;
        if (missionResultListener == null) {
            Log.d(TAG, "Initialized Mission result listener");
            missionResultListener = new Mission.ResultListener() {
                @Override
                public void onResultCallback(Mission.Result result) {
                    Log.d(TAG, result.resultStr);
                    onChangeListener.publishMissionStatus(result.resultStr);
                }
            };
        }
    }

    public void unRegisterMissionResultListener() {

        if (missionResultListener != null) {
            missionResultListener = null;
        }
    }

    public void registerMissionProgressListener(final Context context) {

        onChangeListener = (OnChangeListener) context;
        if (missionProgressListener == null) {
            Log.d(TAG, "Initialized Mission Progress listener");
            missionProgressListener = new Mission.ProgressListener() {
                @Override
                public void onProgressUpdate(int current, int total) {
                    Log.d(TAG, "Current mission item number is " + current);
                    onChangeListener.publishMissionStatus("Current mission item number is " + current);
                }
            };
        }
    }

    public void unRegisterMissionProgressListener() {

        if (missionProgressListener != null) {
            missionProgressListener = null;
        }
    }

    public Mission.ResultListener getMissionResultListener() {
        return missionResultListener;
    }

    public Mission.ProgressListener getMissionProgressListener() {
        return missionProgressListener;
    }
}

