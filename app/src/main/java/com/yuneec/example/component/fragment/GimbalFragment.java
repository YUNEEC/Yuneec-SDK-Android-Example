package com.yuneec.example.component.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yuneec.example.R;
import com.yuneec.example.component.listeners.GimbalListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.example.component.utils.Media;
import com.yuneec.sdk.Gimbal;

/**
 * Created by sushma on 8/15/17.
 */

public class GimbalFragment
    extends Fragment
    implements View.OnClickListener {

    private View rootView;

    private Button rotateClockwise;

    private Button rotateToInitial;

    private EditText yawVal;

    private EditText pitchVal;

    private Button setPitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        initViews(inflater, container);
        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();
        registerListener();
    }

    @Override
    public void onStop() {

        super.onStop();
        unRegisterListener();
    }


    private void initViews(LayoutInflater inflater,
                           ViewGroup container) {

        rootView = inflater.inflate(R.layout.gimbal_layout, container, false);
        rotateClockwise = (Button) rootView.findViewById(R.id.rotate_camera_clockwise);
        rotateClockwise.setOnClickListener(this);
        yawVal = (EditText) rootView.findViewById(R.id.yaw_degree);
        pitchVal = (EditText) rootView.findViewById(R.id.pitch_degree);
        rotateToInitial = (Button) rootView.findViewById(R.id.rotate_to_initial);
        rotateToInitial.setOnClickListener(this);
        setPitch = (Button) rootView.findViewById(R.id.set_pitch);
        setPitch.setOnClickListener(this);
    }

    private void registerListener() {

        GimbalListener.registerGimbalListener();
    }

    private void unRegisterListener() {

        GimbalListener.unRegisterGimbalListener();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rotate_camera_clockwise:
                Media.vibrate(getActivity());
                if (yawVal.getText().toString().isEmpty()) {
                    Common.makeToast(getActivity(), "Please enter yaw degree, before clicking rotate button");
                } else {
                    try {
                        float yaw_degree = Float.parseFloat(yawVal.getText().toString());
                        Gimbal.asyncSetPitchAndYawOfJni(Common.currentPitch, yaw_degree,
                                                        GimbalListener.getGimbaListener());
                        Common.currentYaw = yaw_degree;
                    } catch (Exception e) {
                        Common.makeToast(getActivity(), "Please enter a valid value for yaw degree");
                    }
                }

                break;
            case R.id.rotate_to_initial:
                Media.vibrate(getActivity());
                Gimbal.asyncSetPitchAndYawOfJni(0, 0, GimbalListener.getGimbaListener());
                Common.currentYaw = 0;
                Common.currentPitch = 0;
                break;
            case R.id.set_pitch:
                Media.vibrate(getActivity());
                if (pitchVal.getText().toString().isEmpty()) {
                    Common.makeToast(getActivity(), "Please enter pitch degree, before clicking rotate button");
                } else {
                    try {
                        float pitch_degree = Float.parseFloat(pitchVal.getText().toString());
                        Gimbal.asyncSetPitchAndYawOfJni(pitch_degree, Common.currentYaw,
                                                        GimbalListener.getGimbaListener());
                        Common.currentPitch = pitch_degree;
                    } catch (Exception e) {
                        Common.makeToast(getActivity(), "Please enter a valid value for pitch degree");
                    }
                }

                break;
        }
    }
}
