package com.yuneec.example.component.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.yuneec.example.CameraSettingsFragment;
import com.yuneec.example.R;
import com.yuneec.example.component.listeners.CameraSettingsListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.example.component.utils.Sounds;
import com.yuneec.example.view.CustomTextView;
import com.yuneec.sdk.Camera;
import com.yuneec.sdk.Mission;
import com.yuneec.sdk.MissionItem;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by sushmas on 9/3/17.
 */

public class SettingsFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    View rootView;

    Spinner wb_spinner;

    Spinner color_mode_spinner;

    Spinner exposure_spinner;

    Spinner ex_com_spinner;

    Spinner shutter_speed_spinner;

    Spinner iso_spinner;

    CustomTextView ex_com_view;

    CustomTextView iso_view;

    private static final String TAG = SettingsFragment.class.getCanonicalName();

    //Button applySettings;

    //Button cancel;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDialog().setTitle("Camera Settings");
        setRetainInstance(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();
        CameraSettingsListener.registerSettingsListener();
        addOnClickListeners();
    }

    @Override
    public void onStop() {

        super.onStop();
        CameraSettingsListener.unRegisterCameraSettingsListeners();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("wbSpinner", wb_spinner.getSelectedItemPosition());
        outState.putInt("colorModeSpinner", color_mode_spinner.getSelectedItemPosition());
        outState.putInt("exposureSpinner", exposure_spinner.getSelectedItemPosition());
        if(ex_com_spinner.getVisibility() == View.VISIBLE) {
            outState.putInt("exComSpinner", ex_com_spinner.getSelectedItemPosition());
        }
        if(iso_spinner.getVisibility() == View.VISIBLE) {
            outState.putInt("isoSpinner", iso_spinner.getSelectedItemPosition());
        }
    }

    private void initViews(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.camera_settings_dialogue, container, false);
        wb_spinner = (Spinner) rootView.findViewById(R.id.wb_dropdown);
        color_mode_spinner = (Spinner) rootView.findViewById(R.id.color_mode_dropdown);
        exposure_spinner = (Spinner) rootView.findViewById(R.id.exposure_mode_dropdown);
        ex_com_spinner = (Spinner) rootView.findViewById(R.id.exposure_com_dropdown);
        shutter_speed_spinner = (Spinner) rootView.findViewById(R.id.shutter_speed_dropdown);
        iso_spinner = (Spinner) rootView.findViewById(R.id.iso_dropdown);
        ex_com_view = (CustomTextView) rootView.findViewById(R.id.ex_com_text);
        iso_view = (CustomTextView) rootView.findViewById(R.id.iso_val);
        if(savedInstanceState != null) {
            wb_spinner.setSelection(savedInstanceState.getInt("wbSpinner"));
            color_mode_spinner.setSelection(savedInstanceState.getInt("colorModeSpinner"));
            exposure_spinner.setSelection(savedInstanceState.getInt("exposureSpinner"));
            if(ex_com_spinner.getVisibility() == View.VISIBLE) {
                ex_com_spinner.setSelection(savedInstanceState.getInt("exComSpinner"));            }
            if(iso_spinner.getVisibility() == View.VISIBLE) {
                iso_spinner.setSelection(savedInstanceState.getInt("isoSpinner"));
            }
        }
        //applySettings = (Button) rootView.findViewById(R.id.ok);
        //cancel = (Button) rootView.findViewById(R.id.cancel);
    }

    private void addOnClickListeners() {
        //applySettings.setOnClickListener(this);
        //cancel.setOnClickListener(this);
        wb_spinner.setOnItemSelectedListener(this);
        color_mode_spinner.setOnItemSelectedListener(this);
        exposure_spinner.setOnItemSelectedListener(this);
        ex_com_spinner.setOnItemSelectedListener(this);
        shutter_speed_spinner.setOnItemSelectedListener(this);
        iso_spinner.setOnItemSelectedListener(this);
        addItemsOnSpinner();
    }

    public void addItemsOnSpinner() {
        List<Camera.WhiteBalance> wbList =
                new ArrayList<>(EnumSet.allOf(Camera.WhiteBalance.class));
        ArrayAdapter<Camera.WhiteBalance> wbAdapter = new ArrayAdapter<>(getActivity(),
            R.layout.custom_spinner_item, wbList);
        wbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wb_spinner.setAdapter(wbAdapter);

        List<Camera.ColorMode> colorModeList =
                new ArrayList<>(EnumSet.allOf(Camera.ColorMode.class));
        ArrayAdapter<Camera.ColorMode> colorModeAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.custom_spinner_item, colorModeList);
        colorModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color_mode_spinner.setAdapter(colorModeAdapter);

        List<Camera.ExposureMode> exposureModeList =
                new ArrayList<>(EnumSet.allOf(Camera.ExposureMode.class));
        ArrayAdapter<Camera.ExposureMode> exposureModeAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.custom_spinner_item, exposureModeList);
        exposureModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exposure_spinner.setAdapter(exposureModeAdapter);

        List<Float> exposureComList = new ArrayList<>();
        exposureComList.add(-2.0f);
        exposureComList.add(-1.5f);
        exposureComList.add(-1.0f);
        exposureComList.add(-0.5f);
        exposureComList.add(0.0f);
        exposureComList.add(0.5f);
        exposureComList.add(1.0f);
        exposureComList.add(1.5f);
        exposureComList.add(2.0f);

        ArrayAdapter<Float> exposureComAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.custom_spinner_item, exposureComList);
        exposureModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ex_com_spinner.setAdapter(exposureComAdapter);

        List<Integer> isoValLIst = new ArrayList<>();

        isoValLIst.add(100);
        isoValLIst.add(150);
        isoValLIst.add(200);
        isoValLIst.add(300);
        isoValLIst.add(400);
        isoValLIst.add(600);

        ArrayAdapter<Integer> isoAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.custom_spinner_item, isoValLIst);
        isoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iso_spinner.setAdapter(isoAdapter);

    }


    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {
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
        }*/

    }

    /*private void applySettings() {
           Camera.WhiteBalance wbSelection = (Camera.WhiteBalance) wb_spinner.getSelectedItem();
           Camera.setWhiteBalance(wbSelection, CameraSettingsListener.getWhiteBalanceListener());
           Camera.ColorMode colorModeSelection = (Camera.ColorMode) color_mode_spinner.getSelectedItem();
           Camera.setColorMode(colorModeSelection, CameraSettingsListener.getColorModeListener());
           Camera.ExposureMode exposureModeSelection = (Camera.ExposureMode) exposure_spinner.getSelectedItem();
           Camera.setExposureMode(exposureModeSelection, CameraSettingsListener.getExposureModeListener());
    }*/

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(Common.isConnected) {
            switch (adapterView.getId()) {
                case R.id.wb_dropdown:
                    Log.d(TAG, "wb selected");
                    Camera.WhiteBalance wbSelection = (Camera.WhiteBalance) wb_spinner.getSelectedItem();
                    Camera.setWhiteBalance(wbSelection, CameraSettingsListener.getWhiteBalanceListener());
                    break;
                case R.id.color_mode_dropdown:
                    Log.d(TAG, "color mode selected");
                    Camera.ColorMode colorModeSelection = (Camera.ColorMode) color_mode_spinner.getSelectedItem();
                    Camera.setColorMode(colorModeSelection, CameraSettingsListener.getColorModeListener());
                    break;
                case R.id.exposure_mode_dropdown:
                    Log.d(TAG, "em selected");
                    Camera.ExposureMode exposureModeSelection = (Camera.ExposureMode) exposure_spinner.getSelectedItem();
                    if(exposureModeSelection == Camera.ExposureMode.AUTO || exposureModeSelection == Camera.ExposureMode.UNKNOWN ) {
                        //ex_com_spinner.setVisibility(View.GONE);
                        //ex_com_view.setVisibility(View.GONE);
                        iso_view.setVisibility(View.GONE);
                        iso_spinner.setVisibility(View.GONE);
                        Camera.setExposureMode(exposureModeSelection, CameraSettingsListener.getExposureModeListener());
                    }
                    else {
                        //ex_com_spinner.setVisibility(View.VISIBLE);
                        iso_spinner.setVisibility(View.VISIBLE);
                        //ex_com_view.setVisibility(View.VISIBLE);
                        iso_view.setVisibility(View.VISIBLE);
                        Camera.setExposureMode(exposureModeSelection, CameraSettingsListener.getExposureModeListener());
                    }
                    break;
                case R.id.exposure_com_dropdown:
                    Log.d(TAG, "eval selected");
                    Float exposure_com_selection = (Float) ex_com_spinner.getSelectedItem();
                    Camera.setExposureValue(exposure_com_selection, CameraSettingsListener.getExposureValueListener());
                    break;
                case R.id.iso_dropdown:
                    Log.d(TAG, "iso selected");
                    int iso_selection = (int) iso_spinner.getSelectedItem();
                    Camera.setISOValue(iso_selection, CameraSettingsListener.getIsoValueListener());
                    break;
            }

        }

        else {
            Toast.makeText(getActivity(), "Please Connect To The Drone", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
