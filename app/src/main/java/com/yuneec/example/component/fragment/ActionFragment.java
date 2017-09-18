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
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yuneec.example.R;
import com.yuneec.example.component.listeners.ActionListener;
import com.yuneec.example.component.utils.Media;
import com.yuneec.sdk.Action;

public class ActionFragment extends Fragment implements View.OnClickListener {
    /**Create view*/
    View mView;
    /** Input for the takeoff altitude  */
    EditText setHeight;
    /** Get the fly height  */
    EditText getHeight;

    Action.ResultListener listener;

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
        registerListener();
        listener = ActionListener.getActionResultListener();
    }

    @Override
    public void onStop() {

        super.onStop();
        unRegisterListener();
    }

    private void initView(LayoutInflater inflater,
                          ViewGroup container) {
        mView = inflater.inflate(R.layout.action_layout, container, false);
        mView.findViewById(R.id.arm_button).setOnClickListener(this);
        mView.findViewById(R.id.disarm_button).setOnClickListener(this);
        mView.findViewById(R.id.takeoff_button).setOnClickListener(this);
        mView.findViewById(R.id.land_button).setOnClickListener(this);
        mView.findViewById(R.id.return_to_launch_button).setOnClickListener(this);
        mView.findViewById(R.id.kill_button).setOnClickListener(this);
    }

    private void registerListener() {
        ActionListener.registerActionListener();
    }

    private void unRegisterListener() {
        ActionListener.unRegisterActionistener();
    }

    @Override
    public void onClick(View v) {
        Media.vibrate(getActivity());
        switch (v.getId()) {
            case R.id.arm_button:
                Action.armAsync(listener);
                break;
            case R.id.disarm_button:
                Action.disarmAsync(listener);
                break;
            case R.id.takeoff_button:
                Action.takeoffAsync(listener);
                break;
            case R.id.land_button:
                Action.landAsync(listener);
                break;
            case R.id.return_to_launch_button:
                Action.returnToLaunchAsync(listener);
                break;
            case R.id.kill_button:
                Action.killAsync(listener);
                break;
        }
    }
}
