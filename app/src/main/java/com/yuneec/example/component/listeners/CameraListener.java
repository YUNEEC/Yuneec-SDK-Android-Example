package com.yuneec.example.component.listeners;

import android.content.Context;
import android.util.Log;

import com.yuneec.example.component.custom_callback.OnChangeListener;
import com.yuneec.sdk.Camera;

/**
 * Created by sushma on 8/8/17.
 */

public class CameraListener {

    private static Camera.ResultListener cameraResultListener = null;

    private static final String TAG = CameraListener.class.getCanonicalName();

    private static OnChangeListener onChangeListener;

    public static void registerCameraListener(Context context) {
        onChangeListener = (OnChangeListener) context;
        if (cameraResultListener == null) {
            Log.d(TAG, "Initialized camera result listener");
            cameraResultListener = new Camera.ResultListener() {

                @Override
                public void resultCallback(Camera.Result result) {
                    onChangeListener.publishCameraResult(result.resultStr);
                    Log.d(TAG, result.resultStr);
                }


            };
            Camera.setResultListener(cameraResultListener);
        }
    }

    public static void unRegisterCameraListener() {

        if (cameraResultListener != null) {
            cameraResultListener = null;
        }
    }
}
