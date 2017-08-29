package com.yuneec.example.component.listeners;

import android.util.Log;

import com.yuneec.sdk.Gimbal;

/**
 * Created by sushma on 8/15/17.
 */

public class GimbalListener {

    private static Gimbal.ResultListener gimbalResultListener = null;

    private static final String TAG = GimbalListener.class.getCanonicalName();

    public static Gimbal.ResultListener getGimbaListener() {

        if (gimbalResultListener == null) {
            Log.d(TAG, "Initialized gimbal result listener");
            gimbalResultListener = new Gimbal.ResultListener() {


                @Override
                public void onResultCallback(Gimbal.Result result) {

                    Log.d(TAG, result.resultStr);
                }
            };
        }

        return gimbalResultListener;
    }

    public static void registerGimbalListener() {

        if (gimbalResultListener == null) {
            Log.d(TAG, "Initialized gimbal result listener");
            gimbalResultListener = new Gimbal.ResultListener() {


                @Override
                public void onResultCallback(Gimbal.Result result) {

                    Log.d(TAG, result.resultStr);
                }
            };
        }
    }

    public static void unRegisterGimbalListener() {

        if (gimbalResultListener != null) {
            gimbalResultListener = null;
        }
    }
}
