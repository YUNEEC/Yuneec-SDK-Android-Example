package com.yuneec.example.component.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yuneec.example.R;
import com.yuneec.example.component.listeners.GimbalListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.sdk.Gimbal;

/**
 * Created by sushma on 8/15/17.
 */

public class GimbalFragment
    extends Fragment
    implements View.OnClickListener {

    private View rootView;

    private Button rotateClockwise;

    //private Button rotateAnticlockwise;

    private Button rotateToInitial;

    private EditText editText;


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
        editText = (EditText) rootView.findViewById(R.id.yaw_degree);
        //rotateAnticlockwise = (Button) rootView.findViewById(R.id.rotate_camera_anticlockwise);
        //rotateAnticlockwise.setOnClickListener(this);
        rotateToInitial = (Button) rootView.findViewById(R.id.rotate_to_initial);
        rotateToInitial.setOnClickListener(this);
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

                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), R.string.no_text, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        float yaw_degree = Float.parseFloat(editText.getText().toString());
                        Gimbal.asyncSetPitchAndYawOfJni(0, yaw_degree,
                                                        GimbalListener.getGimbaListener());
                        Common.currentRotation += Common.fixedRotationAngleDeg;
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Please enter a valid value for yaw degree",
                                       Toast.LENGTH_LONG).show();
                    }
                }

                break;
            /*case R.id.rotate_camera_anticlockwise:
                Gimbal.asyncSetPitchAndYawOfJni(0, Common.currentRotation - Common.fixedRotationAngleDeg,
                        GimbalListener.getGimbaListener());
                Common.currentRotation -= Common.fixedRotationAngleDeg;
                break;*/
            case R.id.rotate_to_initial:
                Gimbal.asyncSetPitchAndYawOfJni(0, 0, GimbalListener.getGimbaListener());
                Common.currentRotation = 0;
                break;

        }
    }
}
