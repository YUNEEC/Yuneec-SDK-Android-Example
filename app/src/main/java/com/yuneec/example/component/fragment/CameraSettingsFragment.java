/**
 * CameraSettingsFragment.java
 * Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec.
 * All rights reserved.
 */
package com.yuneec.example.component.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Toast;

import com.yuneec.example.R;
import com.yuneec.sdk.Camera;

public class CameraSettingsFragment extends Fragment implements View.OnClickListener {
    /**Create view*/
    View mView;

    Camera.WhiteBalanceListener whiteBalanceListener;
    Camera.ColorModeListener colorModeListener;
    Camera.ExposureModeListener exposureModeListener;
    Camera.ExposureValueListener exposureValueListener;
    Camera.ShutterSpeedListener shutterSpeedSListener;
    Camera.ShutterSpeedS shutterSpeedS;
    Camera.ISOValueListener isoValueListener;
    Camera.ResultListener resultListener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.camera_settings_example, container, false);

        whiteBalanceListener = new Camera.WhiteBalanceListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.WhiteBalance whiteBalance) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mView.getContext(), result.resultStr + ", WB: " + whiteBalance,
                                       Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        colorModeListener = new Camera.ColorModeListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.ColorMode colorMode) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mView.getContext(), result.resultStr + ", Color Mode: " + colorMode,
                                       Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        exposureModeListener = new Camera.ExposureModeListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.ExposureMode exposureMode) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mView.getContext(), result.resultStr + ", Exposure mode: " + exposureMode,
                                       Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        exposureValueListener = new Camera.ExposureValueListener() {
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
        };

        shutterSpeedS = new Camera.ShutterSpeedS();

        isoValueListener = new Camera.ISOValueListener() {
            @Override
            public void callback(final Camera.Result result, final int isoValue) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mView.getContext(), result.resultStr + ", ISO: " + isoValue,
                                       Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        resultListener = new Camera.ResultListener() {
            @Override
            public void resultCallback(final Camera.Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mView.getContext(), result.resultStr,
                                       Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        Camera.setResultListener(resultListener);

        mView.findViewById(R.id.whitebalance_get_button).setOnClickListener(this);
        mView.findViewById(R.id.whitebalance_auto_button).setOnClickListener(this);
        mView.findViewById(R.id.whitebalance_lock_button).setOnClickListener(this);
        mView.findViewById(R.id.whitebalance_sunny_button).setOnClickListener(this);
        mView.findViewById(R.id.whitebalance_cloudy_button).setOnClickListener(this);
        mView.findViewById(R.id.colormode_get_button).setOnClickListener(this);
        mView.findViewById(R.id.colormode_neutral_button).setOnClickListener(this);
        mView.findViewById(R.id.colormode_enhanced_button).setOnClickListener(this);
        mView.findViewById(R.id.colormode_night_button).setOnClickListener(this);
        mView.findViewById(R.id.colormode_unprocessed_button).setOnClickListener(this);
        mView.findViewById(R.id.exposuremode_get_button).setOnClickListener(this);
        mView.findViewById(R.id.exposuremode_auto_button).setOnClickListener(this);
        mView.findViewById(R.id.exposuremode_manual_button).setOnClickListener(this);
        mView.findViewById(R.id.exposurevalue_get_button).setOnClickListener(this);
        mView.findViewById(R.id.exposurevalue_neutral_button).setOnClickListener(this);
        mView.findViewById(R.id.exposurevalue_positive_button).setOnClickListener(this);
        mView.findViewById(R.id.exposurevalue_negative_button).setOnClickListener(this);
        mView.findViewById(R.id.picture_button).setOnClickListener(this);
        mView.findViewById(R.id.shutterspeed_get_button).setOnClickListener(this);
        mView.findViewById(R.id.shutterspeed_2_button).setOnClickListener(this);
        mView.findViewById(R.id.shutterspeed_1_30_button).setOnClickListener(this);
        mView.findViewById(R.id.shutterspeed_1_1000_button).setOnClickListener(this);
        mView.findViewById(R.id.isovalue_get_button).setOnClickListener(this);
        mView.findViewById(R.id.isovalue_100_button).setOnClickListener(this);
        mView.findViewById(R.id.isovalue_400_button).setOnClickListener(this);
        mView.findViewById(R.id.isovalue_1600_button).setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.whitebalance_get_button:
                Camera.getWhiteBalance(whiteBalanceListener);
                break;
            case R.id.whitebalance_auto_button:
                Camera.setWhiteBalance(Camera.WhiteBalance.AUTO, whiteBalanceListener);
                break;
            case R.id.whitebalance_lock_button:
                Camera.setWhiteBalance(Camera.WhiteBalance.LOCK, whiteBalanceListener);
                break;
            case R.id.whitebalance_sunny_button:
                Camera.setWhiteBalance(Camera.WhiteBalance.SUNNY, whiteBalanceListener);
                break;
            case R.id.whitebalance_cloudy_button:
                Camera.setWhiteBalance(Camera.WhiteBalance.CLOUDY, whiteBalanceListener);
                break;
            case R.id.colormode_get_button:
                Camera.getColorMode(colorModeListener);
                break;
            case R.id.colormode_neutral_button:
                Camera.setColorMode(Camera.ColorMode.NEUTRAL, colorModeListener);
                break;
            case R.id.colormode_enhanced_button:
                Camera.setColorMode(Camera.ColorMode.ENHANCED, colorModeListener);
                break;
            case R.id.colormode_night_button:
                Camera.setColorMode(Camera.ColorMode.NIGHT, colorModeListener);
                break;
            case R.id.colormode_unprocessed_button:
                Camera.setColorMode(Camera.ColorMode.UNPROCESSED, colorModeListener);
                break;
            case R.id.exposuremode_get_button:
                Camera.getExposureMode(exposureModeListener);
                break;
            case R.id.exposuremode_auto_button:
                Camera.setExposureMode(Camera.ExposureMode.AUTO, exposureModeListener);
                break;
            case R.id.exposuremode_manual_button:
                Camera.setExposureMode(Camera.ExposureMode.MANUAL, exposureModeListener);
                break;
            case R.id.exposurevalue_get_button:
                Camera.getExposureValue(exposureValueListener);
                break;
            case R.id.exposurevalue_neutral_button:
                Camera.setExposureValue(0.0f, exposureValueListener);
                break;
            case R.id.exposurevalue_positive_button:
                Camera.setExposureValue(2.0f, exposureValueListener);
                break;
            case R.id.exposurevalue_negative_button:
                Camera.setExposureValue(-2.0f, exposureValueListener);
                break;
            case R.id.picture_button:
                Camera.asyncTakePhoto();
                break;
            case R.id.shutterspeed_get_button:
                Camera.getShutterSpeed(shutterSpeedSListener);
                break;
            case R.id.shutterspeed_2_button:
                shutterSpeedS.numerator = 2;
                shutterSpeedS.denominator = 1;
                Camera.setShutterSpeed(shutterSpeedS, shutterSpeedSListener);
                break;
            case R.id.shutterspeed_1_30_button:
                shutterSpeedS.numerator = 1;
                shutterSpeedS.denominator = 30;
                Camera.setShutterSpeed(shutterSpeedS, shutterSpeedSListener);
                break;
            case R.id.shutterspeed_1_1000_button:
                shutterSpeedS.numerator = 1;
                shutterSpeedS.denominator = 1000;
                Camera.setShutterSpeed(shutterSpeedS, shutterSpeedSListener);
                break;
            case R.id.isovalue_get_button:
                Camera.getISOValue(isoValueListener);
                break;
            case R.id.isovalue_100_button:
                Camera.setISOValue(100, isoValueListener);
                break;
            case R.id.isovalue_400_button:
                Camera.setISOValue(400, isoValueListener);
                break;
            case R.id.isovalue_1600_button:
                Camera.setISOValue(1600, isoValueListener);
                break;
        }
    }
}
