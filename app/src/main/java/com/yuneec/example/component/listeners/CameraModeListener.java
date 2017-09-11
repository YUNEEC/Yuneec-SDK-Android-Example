package com.yuneec.example.component.listeners;

import android.content.Context;
import android.util.Log;

import com.yuneec.example.component.custom_callback.OnChangeListener;
import com.yuneec.sdk.Camera;

/**
 * Created by sushmas on 9/11/17.
 */

public class CameraModeListener {

    private static Camera.ModeListener cameraModeListener = null;

    private static final String TAG = CameraModeListener.class.getCanonicalName();


    public static Camera.ModeListener getCameraModeListener() {
        return cameraModeListener;
    }

    public static void registerCameraModeListener() {
        if (cameraModeListener == null) {
            Log.d(TAG, "Initialized camera result listener");
            cameraModeListener = new Camera.ModeListener() {

                @Override
                public void callback(Camera.Result result, Camera.Mode mode) {
                    Log.d(TAG, mode + "mode set" + result.resultStr);
                }

            };
        }
    }

    public static void unRegisterCameraModeListener() {

        if (cameraModeListener != null) {
            cameraModeListener = null;
        }
    }
}
