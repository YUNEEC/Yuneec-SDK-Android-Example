/**
 * CameraSettingsFragment.java
 * Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec.
 * All rights reserved.
 */
package com.yuneec.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Toast;

import com.yuneec.sdk.Camera;

public class CameraSettingsFragment extends Fragment implements View.OnClickListener {
    /**Create view*/
    View mView;

    Camera.WhiteBalanceListener whiteBalanceListener;
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
                                       Toast.LENGTH_LONG).show();
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
                                       Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        Camera.setResultListener(resultListener);

        mView.findViewById(R.id.wb_get_button).setOnClickListener(this);
        mView.findViewById(R.id.auto_button).setOnClickListener(this);
        mView.findViewById(R.id.lock_button).setOnClickListener(this);
        mView.findViewById(R.id.sunny_button).setOnClickListener(this);
        mView.findViewById(R.id.cloudy_button).setOnClickListener(this);
        mView.findViewById(R.id.picture_button).setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wb_get_button:
                Camera.getWhiteBalance(whiteBalanceListener);
                break;
            case R.id.auto_button:
                Camera.setWhiteBalance(Camera.WhiteBalance.AUTO, whiteBalanceListener);
                break;
            case R.id.lock_button:
                Camera.setWhiteBalance(Camera.WhiteBalance.LOCK, whiteBalanceListener);
                break;
            case R.id.sunny_button:
                Camera.setWhiteBalance(Camera.WhiteBalance.SUNNY, whiteBalanceListener);
                break;
            case R.id.cloudy_button:
                Camera.setWhiteBalance(Camera.WhiteBalance.CLOUDY, whiteBalanceListener);
                break;
            case R.id.picture_button:
                Camera.asyncTakePhoto();
                break;
        }
    }
}
