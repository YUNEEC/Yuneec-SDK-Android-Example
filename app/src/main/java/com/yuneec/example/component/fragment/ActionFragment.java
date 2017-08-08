/**
 * ActionFragment.java
 * Yuneec-SDK-Android-Example
 *
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

        mView = inflater.inflate( R.layout.action_example, container, false);

        listener = new Action.ResultListener() {
            @Override
            public void onResultCallback(final Action.Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mView.getContext(), result.resultStr,
                                       Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        mView.findViewById(R.id.arm_button).setOnClickListener(this);
        mView.findViewById(R.id.disarm_button).setOnClickListener(this);
        mView.findViewById(R.id.takeoff_button).setOnClickListener(this);
        mView.findViewById(R.id.land_button).setOnClickListener(this);
        mView.findViewById(R.id.return_to_launch_button).setOnClickListener(this);
        mView.findViewById(R.id.kill_button).setOnClickListener(this);
        mView.findViewById(R.id.set_altitude).setOnClickListener(this);
        mView.findViewById(R.id.get_altitude).setOnClickListener(this);
        setHeight=(EditText) mView.findViewById(R.id.saltitude);
        getHeight=(EditText)mView.findViewById(R.id.galtitude);

        return mView;
    }

    @Override
    public void onClick(View v) {
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
            case R.id.set_altitude:
                try {
                    Action.setAltitudeM(Double.parseDouble(setHeight.getText() + ""));
                    Toast.makeText(mView.getContext(),"Set success",Toast.LENGTH_LONG).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(mView.getContext(),"Input error",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.get_altitude:
                double height= Action.getAltitude();
                getHeight.setText(height+"");
        }
    }
}
