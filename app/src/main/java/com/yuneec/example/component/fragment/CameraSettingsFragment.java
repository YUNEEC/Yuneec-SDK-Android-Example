/**
 * CameraSettingsFragment.java
 * Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec.
 * All rights reserved.
 */
package com.yuneec.example.component.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.yuneec.example.R;
import com.yuneec.example.component.listeners.CameraModeListener;
import com.yuneec.example.component.listeners.CameraSettingsListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.example.component.utils.Media;
import com.yuneec.example.view.CustomButton;
import com.yuneec.example.view.CustomEditTextView;
import com.yuneec.example.view.CustomTextView;
import com.yuneec.sdk.Camera;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CameraSettingsFragment extends DialogFragment implements View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    View rootView;

    Spinner wb_spinner;

    Spinner color_mode_spinner;

    Spinner exposure_spinner;

    Spinner ex_com_spinner;

    Spinner shutter_speed_spinner;

    Spinner iso_spinner;

    TextView ex_com_view;

    TextView iso_view;

    TextView shutter_speed_view;

    Camera.ShutterSpeedS shutterSpeedS = new Camera.ShutterSpeedS();

    Spinner photo_format_spinner;

    Spinner photo_quality_spinner;

    Spinner video_format_spinner;

    Spinner video_resolution_spinner;

    Spinner metering_spinner;

    CustomTextView photo_format_text;

    CustomTextView photo_quality_text;

    CustomTextView video_format_text;

    CustomTextView video_resolution_text;

    CustomTextView metering_text;

    CustomTextView resolution_text;

    CustomButton photo_format;

    CustomButton photo_quality;

    CustomButton video_format;

    CustomButton video_resolution;

    CustomButton metering;

    CustomButton resolution;

    CustomButton resetSettings;

    private static final String TAG = CameraSettingsListener.class.getCanonicalName();


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getDialog().setTitle("Camera Settings");
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
        outState.putInt("exComSpinner", ex_com_spinner.getSelectedItemPosition());
        if (iso_spinner.getVisibility() == View.VISIBLE) {
            outState.putInt("isoSpinner", iso_spinner.getSelectedItemPosition());
        }
        if (shutter_speed_spinner.getVisibility() == View.VISIBLE) {
            outState.putInt("shutterSpeedSpinner", shutter_speed_spinner.getSelectedItemPosition());
        }
    }

    private void initViews(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.camera_setting_layout, container, false);
        wb_spinner = (Spinner) rootView.findViewById(R.id.wb_dropdown);
        color_mode_spinner = (Spinner) rootView.findViewById(R.id.color_mode_dropdown);
        exposure_spinner = (Spinner) rootView.findViewById(R.id.exposure_mode_dropdown);
        ex_com_spinner = (Spinner) rootView.findViewById(R.id.exposure_com_dropdown);
        shutter_speed_spinner = (Spinner) rootView.findViewById(R.id.shutter_speed_dropdown);
        iso_spinner = (Spinner) rootView.findViewById(R.id.iso_dropdown);
        ex_com_view = (TextView) rootView.findViewById(R.id.ex_com_text);
        iso_view = (TextView) rootView.findViewById(R.id.iso_val);
        shutter_speed_view = (TextView) rootView.findViewById(R.id.shutter_speed);
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
        photo_format_spinner = (Spinner) rootView.findViewById(R.id.photo_format_dropdown);
        photo_quality_spinner = (Spinner) rootView.findViewById(R.id.photo_quality_dropdown);
        video_format_spinner = (Spinner) rootView.findViewById(R.id.video_format_dropdown);
        video_resolution_spinner = (Spinner) rootView.findViewById(R.id.video_resolution_dropdown);
        metering_spinner = (Spinner) rootView.findViewById(R.id.metering_dropdown);

        photo_format_text = (CustomTextView) rootView.findViewById(R.id.photo_format_text);
        photo_quality_text = (CustomTextView) rootView.findViewById(R.id.photo_quality_text);
        video_format_text = (CustomTextView) rootView.findViewById(R.id.video_format_text);
        video_resolution_text = (CustomTextView) rootView.findViewById(R.id.video_resolution_text);
        resolution_text = (CustomTextView) rootView.findViewById(R.id.resolution_text);
        metering_text = (CustomTextView) rootView.findViewById(R.id.metering_text);

        photo_format = (CustomButton) rootView.findViewById(R.id.get_photo_format);
        photo_quality = (CustomButton) rootView.findViewById(R.id.get_photo_quality);
        video_format = (CustomButton) rootView.findViewById(R.id.get_video_format);
        video_resolution = (CustomButton) rootView.findViewById(R.id.get_video_resolution);
        resolution = (CustomButton) rootView.findViewById(R.id.get_resolution);
        metering = (CustomButton) rootView.findViewById(R.id.get_metering);
        resetSettings = (CustomButton) rootView.findViewById(R.id.reset_settings);
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
        photo_format_spinner.setOnItemSelectedListener(this);
        photo_quality_spinner.setOnItemSelectedListener(this);
        video_format_spinner.setOnItemSelectedListener(this);
        video_resolution_spinner.setOnItemSelectedListener(this);
        metering_spinner.setOnItemSelectedListener(this);
        photo_format.setOnClickListener(this);
        photo_quality.setOnClickListener(this);
        video_format.setOnClickListener(this);
        video_resolution.setOnClickListener(this);
        resolution.setOnClickListener(this);
        metering.setOnClickListener(this);
        resetSettings.setOnClickListener(this);
    }

    private void setSelection() {
        wb_spinner.setSelection(0, false);
        color_mode_spinner.setSelection(0, false);
        exposure_spinner.setSelection(0, false);
        ex_com_spinner.setSelection(0, false);
        shutter_speed_spinner.setSelection(0, false);
        iso_spinner.setSelection(0, false);
        photo_format_spinner.setSelection(0, false);
        photo_quality_spinner.setSelection(0, false);
        video_format_spinner.setSelection(0, false);
        video_resolution_spinner.setSelection(0, false);
        metering_spinner.setSelection(0, false);
    }

    public void addItemsOnSpinner() {
        List<Camera.WhiteBalance> wbList =
            new ArrayList<>(EnumSet.allOf(Camera.WhiteBalance.class));
        ArrayAdapter<Camera.WhiteBalance> wbAdapter = new ArrayAdapter<>(getActivity(),
                                                                         R.layout.spinner_item, wbList);
        wb_spinner.setAdapter(wbAdapter);

        List<Camera.ColorMode> colorModeList =
            new ArrayList<>(EnumSet.allOf(Camera.ColorMode.class));
        ArrayAdapter<Camera.ColorMode> colorModeAdapter = new ArrayAdapter<>(getActivity(),
                                                                             R.layout.spinner_item, colorModeList);
        color_mode_spinner.setAdapter(colorModeAdapter);

        List<Camera.ExposureMode> exposureModeList =
            new ArrayList<>(EnumSet.allOf(Camera.ExposureMode.class));
        ArrayAdapter<Camera.ExposureMode> exposureModeAdapter = new ArrayAdapter<>(getActivity(),
                                                                                   R.layout.spinner_item, exposureModeList);
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
                                                                    R.layout.spinner_item, exposureComList);
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
                                                              R.layout.spinner_item, isoValLIst);
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
                                                                      R.layout.spinner_item, shutterSpeedList);
        shutter_speed_spinner.setAdapter(shutterSpeedAdapter);

        List<Camera.PhotoQuality> photoQualityList =
            new ArrayList<>(EnumSet.allOf(Camera.PhotoQuality.class));
        ArrayAdapter<Camera.PhotoQuality> photoQualityArrayAdapter = new ArrayAdapter<>(getActivity(),
                                                                                        R.layout.spinner_item, photoQualityList);
        photo_quality_spinner.setAdapter(photoQualityArrayAdapter);

        List<Camera.PhotoFormat> photoFormatList =
            new ArrayList<>(EnumSet.allOf(Camera.PhotoFormat.class));
        ArrayAdapter<Camera.PhotoFormat> photoFormatArrayAdapter = new ArrayAdapter<>(getActivity(),
                                                                                      R.layout.spinner_item, photoFormatList);
        photo_format_spinner.setAdapter(photoFormatArrayAdapter);

        List<Camera.VideoFormat> videoFormatList =
            new ArrayList<>(EnumSet.allOf(Camera.VideoFormat.class));
        ArrayAdapter<Camera.VideoFormat> videoFormatArrayAdapter = new ArrayAdapter<>(getActivity(),
                                                                                      R.layout.spinner_item, videoFormatList);
        video_format_spinner.setAdapter(videoFormatArrayAdapter);

        List<Camera.VideoResolution> videoResolutionList =
            new ArrayList<>(EnumSet.allOf(Camera.VideoResolution.class));
        ArrayAdapter<Camera.VideoResolution> videoResolutionArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, videoResolutionList);
        video_resolution_spinner.setAdapter(videoResolutionArrayAdapter);

        List<Camera.Metering.Mode> meteringList =
            new ArrayList<>(EnumSet.allOf(Camera.Metering.Mode.class));
        ArrayAdapter<Camera.Metering.Mode> meteringArrayAdapter = new ArrayAdapter<>(getActivity(),
                                                                                     R.layout.spinner_item, meteringList);
        metering_spinner.setAdapter(meteringArrayAdapter);
    }


    @Override
    public void onClick(View v) {
        if (Common.isConnected) {
            Media.vibrate(getActivity());
            switch (v.getId()) {
                case R.id.get_resolution:
                    Log.d(TAG, "resolution button clicked");
                    Camera.getResolution(CameraSettingsListener.getResolutionListener());
                    break;

                case R.id.get_photo_format:
                    Log.d(TAG, "photo format button clicked");
                    Camera.getPhotoFormat(CameraSettingsListener.getPhotoFormatListener());
                    break;

                case R.id.get_photo_quality:
                    Log.d(TAG, "photo quality button clicked");
                    Camera.getPhotoQuality(CameraSettingsListener.getPhotoQualityListener());
                    break;

                case R.id.get_video_format:
                    Log.d(TAG, "video format button clicked");
                    Camera.getVideoFormat(CameraSettingsListener.getVideoFormatListener());
                    break;

                case R.id.get_video_resolution:
                    Log.d(TAG, "video resolution button clicked");
                    Camera.getVideoResolution(CameraSettingsListener.getVideoResolutionListener());
                    break;

                case R.id.get_metering:
                    Log.d(TAG, "metering button clicked");
                    Camera.getMetering(CameraSettingsListener.getMeteringListener());
                    break;
                case R.id.reset_settings:
                    Log.d(TAG, "reset settings clicked");
                    Camera.asyncReset();
            }
        } else {
            Common.makeToast(getActivity(), "Please Connect To The Drone");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Media.vibrate(getActivity());
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
                Camera.ExposureMode exposureModeSelection = (Camera.ExposureMode)
                                                            exposure_spinner.getSelectedItem();
                if (exposureModeSelection == Camera.ExposureMode.AUTO
                        || exposureModeSelection == Camera.ExposureMode.UNKNOWN) {
                    ex_com_spinner.setVisibility(View.VISIBLE);
                    ex_com_view.setVisibility(View.VISIBLE);
                    iso_view.setVisibility(View.GONE);
                    iso_spinner.setVisibility(View.GONE);
                    shutter_speed_spinner.setVisibility(View.GONE);
                    shutter_speed_view.setVisibility(View.GONE);
                    Camera.setExposureMode(exposureModeSelection, CameraSettingsListener.getExposureModeListener());
                } else {
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
            case R.id.photo_format_dropdown:
                Log.d(TAG, "photo format selected");
                Camera.PhotoFormat photoFormatSelection = (Camera.PhotoFormat)
                                                          photo_format_spinner.getSelectedItem();
                Camera.setPhotoFormat(photoFormatSelection, CameraSettingsListener.getPhotoFormatListener());
                break;

            case R.id.photo_quality_dropdown:
                Log.d(TAG, "photo quality selected");
                Camera.PhotoQuality photoQualitySelection = (Camera.PhotoQuality)
                                                            photo_quality_spinner.getSelectedItem();
                Camera.setPhotoQuality(photoQualitySelection, CameraSettingsListener.getPhotoQualityListener());
                break;

            case R.id.video_format_dropdown:
                Log.d(TAG, "video format selected");
                Camera.VideoFormat videoFormatSelection = (Camera.VideoFormat)
                                                          video_format_spinner.getSelectedItem();
                Camera.setVideoFormat(videoFormatSelection, CameraSettingsListener.getVideoFormatListener());
                break;

            case R.id.video_resolution_dropdown:
                Log.d(TAG, "video resolution selected");
                Camera.VideoResolution videoResolutionSelection = (Camera.VideoResolution)
                                                                  video_resolution_spinner.getSelectedItem();
                Camera.setVideoResolution(videoResolutionSelection,
                                          CameraSettingsListener.getVideoResolutionListener());
                break;
            case R.id.metering_dropdown:
                Log.d(TAG, "metering selected");
                Camera.Metering metering = new Camera.Metering();
                metering.mode = (Camera.Metering.Mode)metering_spinner.getSelectedItem();
                Camera.setMetering(metering, CameraSettingsListener.getMeteringListener());
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}