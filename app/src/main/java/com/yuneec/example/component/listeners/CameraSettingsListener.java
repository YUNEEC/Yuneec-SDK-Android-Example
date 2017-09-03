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


    private static final String TAG = CameraSettingsListener.class.getCanonicalName();

    public static Camera.WhiteBalanceListener getWhiteBalanceListener() {
        return whiteBalanceListener;
    }

    public static Camera.ColorModeListener getColorModeListener() {
        return colorModeListener;
    }

    public static Camera.ExposureModeListener getExposureModeListener() {
        return exposureModeListener;
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

       /* exposureValueListener = new Camera.ExposureValueListener() {
            @Override
            public void callback(final Camera.Result result, final float exposureValue) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mView.getContext(), result.resultStr + ", EV: " + exposureValue,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        shutterSpeedSListener = new Camera.ShutterSpeedListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.ShutterSpeedS shutterSpeedS) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (shutterSpeedS.denominator == 1) {
                            Toast.makeText(mView.getContext(), result.resultStr +
                                            ", Shutter: " + shutterSpeedS.numerator + " s",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mView.getContext(), result.resultStr +
                                            ", Shutter: " + shutterSpeedS.numerator + "/" + shutterSpeedS.denominator + " s",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };*/

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
    }
}
