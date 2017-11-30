/**
 * ActionFragment.java
 * Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec.
 * All rights reserved.
 */
package com.yuneec.example.component.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuneec.example.R;
import com.yuneec.example.component.listeners.YuneecSt16Listener;
import com.yuneec.example.component.utils.Media;
import com.yuneec.sdk.YuneecSt16;

public class St16Fragment extends Fragment implements View.OnClickListener {
    /**Create view*/
    View mView;

    YuneecSt16.ResultListener listener;
    YuneecSt16.ButtonStateListener buttonStateListener;
    YuneecSt16.SwitchStateListener switchStateListener;
    YuneecSt16.GpsPositionListener gpsPositionListener;

    private static final String TAG = St16Fragment.class.getCanonicalName();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(inflater, container);
        return mView;
    }

    @Override
    public void onStart() {

        super.onStart();
        listener = YuneecSt16Listener.getYuneecSt16ResultListener(getActivity());
        buttonStateListener = YuneecSt16Listener.getYuneecSt16ButtonListener(getActivity());
        switchStateListener = YuneecSt16Listener.getYuneecSt16SwitchListener(getActivity());
        gpsPositionListener = YuneecSt16Listener.getYuneecSt16GpsPositionListener(getActivity());
        registerListener();
    }

    @Override
    public void onStop() {

        super.onStop();
        unRegisterListener();
    }

    private void initView(LayoutInflater inflater,
                          ViewGroup container) {
        mView = inflater.inflate(R.layout.st16_layout, container, false);
        mView.findViewById(R.id.pair_button).setOnClickListener(this);
        mView.findViewById(R.id.unpair_button).setOnClickListener(this);
        mView.findViewById(R.id.check_paired_button).setOnClickListener(this);
    }

    private void registerListener() {
        YuneecSt16Listener.registerYuneecSt16Listener();
    }

    private void unRegisterListener() {
        YuneecSt16Listener.unRegisterYuneecSt16Listener();
    }

    @Override
    public void onClick(View v) {
        Media.vibrate(getActivity());
        switch (v.getId()) {
            case R.id.pair_button:
                YuneecSt16.pairAsync(listener);
                break;
            case R.id.unpair_button:
                YuneecSt16.unpairAsync(listener);
                break;
            case R.id.check_paired_button:
                YuneecSt16.checkPairedAsync(listener);
                break;
        }
    }
}
