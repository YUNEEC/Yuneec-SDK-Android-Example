package com.yuneec.example.component.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.yuneec.example.CameraSettingsFragment;
import com.yuneec.example.R;
import com.yuneec.example.component.listeners.CameraSettingsListener;
import com.yuneec.example.component.utils.Sounds;
import com.yuneec.sdk.Camera;
import com.yuneec.sdk.Mission;
import com.yuneec.sdk.MissionItem;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by sushmas on 9/3/17.
 */

public class SettingsFragment extends DialogFragment implements View.OnClickListener{

    View rootView;

    Spinner wb_spinner;

    Spinner color_mode_spinner;

    Spinner exposure_spinner;

    Spinner iso_val_spinner;

    Spinner shutter_speed_spinner;

    Button applySettings;

    Button cancel;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDialog().setTitle("Camera Settings");
        setRetainInstance(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews(inflater, container);
        addItemsOnSpinner();
        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();
        CameraSettingsListener.registerSettingsListener();

    }

    @Override
    public void onStop() {

        super.onStop();
    }

    private void initViews(LayoutInflater inflater,
              ViewGroup container) {
        rootView = inflater.inflate(R.layout.camera_settings_dialogue, container, false);
        wb_spinner = (Spinner) rootView.findViewById(R.id.wb_dropdown);
        color_mode_spinner = (Spinner) rootView.findViewById(R.id.color_mode_dropdown);
        exposure_spinner = (Spinner) rootView.findViewById(R.id.exposure_mode_dropdown);
        iso_val_spinner = (Spinner) rootView.findViewById(R.id.iso_dropdown);
        shutter_speed_spinner = (Spinner) rootView.findViewById(R.id.shutter_speed_dropdown);
        applySettings = (Button) rootView.findViewById(R.id.ok);
        cancel = (Button) rootView.findViewById(R.id.cancel);
        addOnClickListeners();
    }

    private void addOnClickListeners() {
        applySettings.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public void addItemsOnSpinner() {
        List<Camera.WhiteBalance> wbList =
                new ArrayList<>(EnumSet.allOf(Camera.WhiteBalance.class));
        ArrayAdapter<Camera.WhiteBalance> wbAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, wbList);
        wbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wb_spinner.setAdapter(wbAdapter);

        List<Camera.ColorMode> colorModeList =
                new ArrayList<>(EnumSet.allOf(Camera.ColorMode.class));
        ArrayAdapter<Camera.ColorMode> colorModeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, colorModeList);
        colorModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color_mode_spinner.setAdapter(colorModeAdapter);

        List<Camera.ExposureMode> exposureModeList =
                new ArrayList<>(EnumSet.allOf(Camera.ExposureMode.class));
        ArrayAdapter<Camera.ExposureMode> exposureModeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, exposureModeList);
        exposureModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exposure_spinner.setAdapter(exposureModeAdapter);

       /* List<String> isoValList =
        ArrayAdapter<Camera.ExposureMode> exposureModeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, exposureModeList);
        exposureModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color_mode_spinner.setAdapter(exposureModeAdapter);*/

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                Sounds.vibrate(getActivity());
                applySettings();
                makeToast("Camera Settings Applied");
                dismiss();
                break;
            case R.id.cancel:
                Sounds.vibrate(getActivity());
                makeToast("Camera Settings Not Applied");
                CameraSettingsListener.unRegisterCameraSettingsListeners();
                dismiss();
                break;
        }

    }

    private void applySettings() {
           Camera.WhiteBalance wbSelection = (Camera.WhiteBalance) wb_spinner.getSelectedItem();
           Camera.setWhiteBalance(wbSelection, CameraSettingsListener.getWhiteBalanceListener());
           Camera.ColorMode colorModeSelection = (Camera.ColorMode) color_mode_spinner.getSelectedItem();
           Camera.setColorMode(colorModeSelection, CameraSettingsListener.getColorModeListener());
           Camera.ExposureMode exposureModeSelection = (Camera.ExposureMode) exposure_spinner.getSelectedItem();
           Camera.setExposureMode(exposureModeSelection, CameraSettingsListener.getExposureModeListener());
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
