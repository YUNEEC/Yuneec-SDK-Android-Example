package com.yuneec.example.component.listeners;

import android.util.Log;

import com.yuneec.sdk.Camera;

/**
 * Created by sushmas on 9/3/17.
 */

public class CameraSettingsListener {

    private static Camera.WhiteBalanceListener whiteBalanceListener = null;

    private static Camera.ColorModeListener colorModeListener = null;

    private static Camera.ExposureModeListener exposureModeListener = null;

    private static Camera.ExposureValueListener exposureValueListener = null;

    private static Camera.ISOValueListener isoValueListener = null;

    private static Camera.ShutterSpeedListener shutterSpeedListener = null;

    private static final String TAG = CameraSettingsListener.class.getCanonicalName();

    public static Camera.WhiteBalanceListener getWhiteBalanceListener() {
        return whiteBalanceListener;
    }

    public static Camera.ExposureValueListener getExposureValueListener() {
        return exposureValueListener;
    }

    public static Camera.ColorModeListener getColorModeListener() {
        return colorModeListener;
    }

    public static Camera.ExposureModeListener getExposureModeListener() {
        return exposureModeListener;
    }

    public static Camera.ISOValueListener getIsoValueListener() {
        return isoValueListener;
    }

    public static Camera.ShutterSpeedListener getShutterSpeedListener() {
        return shutterSpeedListener;
    }

    public static void registerSettingsListener() {
        whiteBalanceListener = new Camera.WhiteBalanceListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.WhiteBalance whiteBalance) {
                        Log.d(TAG, result.resultStr);
            }
        };

        colorModeListener = new Camera.ColorModeListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.ColorMode colorMode) {
                Log.d(TAG, result.resultStr);
            }
        };

        exposureModeListener = new Camera.ExposureModeListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.ExposureMode exposureMode) {
                Log.d(TAG, result.resultStr);
            }
        };

        exposureValueListener = new Camera.ExposureValueListener() {
            @Override
            public void callback(final Camera.Result result, final float exposureValue) {
                Log.d(TAG, result.resultStr);
            }
        };


        isoValueListener = new Camera.ISOValueListener() {
            @Override
            public void callback(Camera.Result result, int i) {
                Log.d(TAG, result.resultStr);
            }
        };

        shutterSpeedListener = new Camera.ShutterSpeedListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.ShutterSpeedS shutterSpeedS) {
                Log.d(TAG, result.resultStr);
            }
        };

    }

    public static void unRegisterCameraSettingsListeners() {

        if (whiteBalanceListener != null) {
            whiteBalanceListener = null;
        }

        if(colorModeListener != null) {
            colorModeListener = null;
        }

        if(exposureModeListener != null) {
            exposureModeListener = null;
        }

        if(exposureValueListener != null) {
            exposureValueListener = null;
        }
        if(isoValueListener != null) {
            isoValueListener = null;
        }
    }
}
