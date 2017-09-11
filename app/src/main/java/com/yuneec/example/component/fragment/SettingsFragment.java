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

    CustomTextView shutter_speed_view;

    Camera.ShutterSpeedS shutterSpeedS = new Camera.ShutterSpeedS();

    private static final String TAG = SettingsFragment.class.getCanonicalName();

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
        if (iso_spinner.getVisibility() == View.VISIBLE) {
            outState.putInt("isoSpinner", iso_spinner.getSelectedItemPosition());
        }
        if (shutter_speed_spinner.getVisibility() == View.VISIBLE) {
            outState.putInt("shutterSpeedSpinner", shutter_speed_spinner.getSelectedItemPosition());
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
        shutter_speed_view = (CustomTextView) rootView.findViewById(R.id.shutter_speed);
        if (savedInstanceState != null) {
            wb_spinner.setSelection(savedInstanceState.getInt("wbSpinner"));
            color_mode_spinner.setSelection(savedInstanceState.getInt("colorModeSpinner"));
            exposure_spinner.setSelection(savedInstanceState.getInt("exposureSpinner"));
            ex_com_spinner.setSelection(savedInstanceState.getInt("exComSpinner"));
            if (iso_spinner.getVisibility() == View.VISIBLE) {
                iso_spinner.setSelection(savedInstanceState.getInt("isoSpinner"));
            }
            if (shutter_speed_spinner.getVisibility() == View.VISIBLE) {
                shutter_speed_spinner.setSelection(savedInstanceState.getInt("shutterSpeedSpinner"));
            }
        }
        addOnClickListeners();
    }

    private void addOnClickListeners() {
        addItemsOnSpinner();
        /* work around for https://stackoverflow.com/questions/2562248/how-to-keep-onitemselected-from-firing-off-on-a-newly-instantiated-spinner*/
        setSelection();
        wb_spinner.setOnItemSelectedListener(this);
        color_mode_spinner.setOnItemSelectedListener(this);
        exposure_spinner.setOnItemSelectedListener(this);
        ex_com_spinner.setOnItemSelectedListener(this);
        shutter_speed_spinner.setOnItemSelectedListener(this);
        iso_spinner.setOnItemSelectedListener(this);
    }

    private void setSelection() {
        wb_spinner.setSelection(0, false);
        color_mode_spinner.setSelection(0, false);
        exposure_spinner.setSelection(0, false);
        ex_com_spinner.setSelection(0, false);
        shutter_speed_spinner.setSelection(0, false);
        iso_spinner.setSelection(0, false);
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
        exposureComAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ex_com_spinner.setAdapter(exposureComAdapter);

        List<Integer> isoValLIst = new ArrayList<>();

        isoValLIst.add(100);
        isoValLIst.add(150);
        isoValLIst.add(200);
        isoValLIst.add(300);
        isoValLIst.add(400);
        isoValLIst.add(600);
        isoValLIst.add(800);
        isoValLIst.add(1600);
        isoValLIst.add(3200);

        ArrayAdapter<Integer> isoAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.custom_spinner_item, isoValLIst);
        isoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iso_spinner.setAdapter(isoAdapter);

        List<String> shutterSpeedList = new ArrayList<>();
        shutterSpeedList.add("4");
        shutterSpeedList.add("3");
        shutterSpeedList.add("2");
        shutterSpeedList.add("1");
        shutterSpeedList.add("1/30");
        shutterSpeedList.add("1/60");
        shutterSpeedList.add("1/125");
        shutterSpeedList.add("1/250");
        shutterSpeedList.add("1/500");
        shutterSpeedList.add("1/1000");
        shutterSpeedList.add("1/2000");
        shutterSpeedList.add("1/4000");
        shutterSpeedList.add("1/8000");



        ArrayAdapter<String> shutterSpeedAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.custom_spinner_item, shutterSpeedList);
        shutterSpeedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shutter_speed_spinner.setAdapter(shutterSpeedAdapter);

    }


    @Override
    public void onClick(View view) {

    }

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
                        ex_com_spinner.setVisibility(View.VISIBLE);
                        ex_com_view.setVisibility(View.VISIBLE);
                        iso_view.setVisibility(View.GONE);
                        iso_spinner.setVisibility(View.GONE);
                        shutter_speed_spinner.setVisibility(View.GONE);
                        shutter_speed_view.setVisibility(View.GONE);
                        Camera.setExposureMode(exposureModeSelection, CameraSettingsListener.getExposureModeListener());
                    }
                    else {
                        ex_com_spinner.setVisibility(View.GONE);
                        ex_com_view.setVisibility(View.GONE);
                        iso_spinner.setVisibility(View.VISIBLE);
                        iso_view.setVisibility(View.VISIBLE);
                        shutter_speed_spinner.setVisibility(View.VISIBLE);
                        shutter_speed_view.setVisibility(View.VISIBLE);
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
                case R.id.shutter_speed_dropdown:
                    Log.d(TAG, "shutter speed selected");
                    switch (shutter_speed_spinner.getSelectedItemPosition() + 1) {
                        case 1:
                            shutterSpeedS.numerator = 4;
                            shutterSpeedS.denominator = 1;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 2:
                            shutterSpeedS.numerator = 3;
                            shutterSpeedS.denominator = 1;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 3:
                            shutterSpeedS.numerator = 2;
                            shutterSpeedS.denominator = 1;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 4:
                            shutterSpeedS.numerator = 1;
                            shutterSpeedS.denominator = 1;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 5:
                            shutterSpeedS.numerator = 1;
                            shutterSpeedS.denominator = 30;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 6:
                            shutterSpeedS.numerator = 1;
                            shutterSpeedS.denominator = 60;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 7:
                            shutterSpeedS.numerator = 1;
                            shutterSpeedS.denominator = 125;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 8:
                            shutterSpeedS.numerator = 1;
                            shutterSpeedS.denominator = 250;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 9:
                            shutterSpeedS.numerator = 1;
                            shutterSpeedS.denominator = 500;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 10:
                            shutterSpeedS.numerator = 1;
                            shutterSpeedS.denominator = 1000;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 11:
                            shutterSpeedS.numerator = 1;
                            shutterSpeedS.denominator = 2000;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 12:
                            shutterSpeedS.numerator = 1;
                            shutterSpeedS.denominator = 4000;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;
                        case 13:
                            shutterSpeedS.numerator = 1;
                            shutterSpeedS.denominator = 8000;
                            Log.d(TAG, shutterSpeedS.denominator + "");
                            Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                            break;

                    }
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
