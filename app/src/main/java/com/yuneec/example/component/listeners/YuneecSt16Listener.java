package com.yuneec.example.component.listeners;

import android.content.Context;
import android.util.Log;

import com.yuneec.example.component.custom_callback.OnChangeListener;
import com.yuneec.sdk.YuneecSt16;

/**
 * Created by Julian Oes on 11/29/17.
 *
 * TODO: this listeners have duplicate initialization code. This needs to be cleaned up.
 */

public class YuneecSt16Listener {

    private static YuneecSt16.ResultListener yuneecSt16ResultListener = null;
    private static YuneecSt16.ButtonStateListener yuneecSt16ButtonStateListener = null;
    private static YuneecSt16.SwitchStateListener yuneecSt16SwitchStateListener = null;
    private static YuneecSt16.GpsPositionListener yuneecSt16GpsListener = null;
    private static YuneecSt16.M4VersionListener yuneecSt16VersionListener = null;

    private static final String TAG = YuneecSt16Listener.class.getCanonicalName();

    private static OnChangeListener onChangeListener;

    public static YuneecSt16.ResultListener getYuneecSt16ResultListener() {
        if (yuneecSt16ResultListener == null) {
            registerYuneecSt16ResultListener();
        }
        return yuneecSt16ResultListener;
    }

    public static YuneecSt16.ButtonStateListener getYuneecSt16ButtonListener() {
        if (yuneecSt16ButtonStateListener == null) {
            registerYuneecSt16ButtonStateListener();
        }

        return yuneecSt16ButtonStateListener;
    }

    public static YuneecSt16.SwitchStateListener getYuneecSt16SwitchListener() {
        if (yuneecSt16SwitchStateListener == null) {
            registerYuneecSt16SwitchStateListener();
        }

        return yuneecSt16SwitchStateListener;
    }

    public static YuneecSt16.GpsPositionListener getYuneecSt16GpsPositionListener() {
        if (yuneecSt16GpsListener == null) {
            registerYuneecSt16GpsListener();
        }

        return yuneecSt16GpsListener;
    }

    public static YuneecSt16.M4VersionListener getYuneecSt16VersionListener() {
        if (yuneecSt16VersionListener == null) {
            registerYuneecSt16VersionListener();
        }

        return yuneecSt16VersionListener;
    }

    public static void registerYuneecSt16Listeners(Context context) {
        onChangeListener = (OnChangeListener) context;
        registerYuneecSt16ResultListener();
        registerYuneecSt16ButtonStateListener();
        registerYuneecSt16SwitchStateListener();
        registerYuneecSt16GpsListener();
        registerYuneecSt16VersionListener();
    }

    private static void registerYuneecSt16ResultListener() {
        if (yuneecSt16ResultListener == null) {
            Log.d(TAG, "Initialized yuneecSt16 result listener");
            yuneecSt16ResultListener = new YuneecSt16.ResultListener() {
                @Override
                public void onResultCallback(YuneecSt16.Result result) {
                    Log.d(TAG, result.resultStr);
                    onChangeListener.publishYuneecSt16Result(result.resultStr);
                }
            };
        }
    }

    private static void registerYuneecSt16ButtonStateListener() {
        if (yuneecSt16ButtonStateListener == null) {
            Log.d(TAG, "Initialized yuneecSt16 button state listener");
            yuneecSt16ButtonStateListener = new YuneecSt16.ButtonStateListener() {
                @Override
                public void onChangeCallback(YuneecSt16.ButtonId buttonId, YuneecSt16.ButtonState buttonState) {
                    Log.d(TAG, "Button " + buttonId.toString() + " changed to " + buttonState.toString());
                    onChangeListener.publishYuneecSt16Result("Button " + buttonId.toString() + " changed to " +
                                                             buttonState.toString());
                }
            };
        }
        YuneecSt16.setButtonStateListener(yuneecSt16ButtonStateListener);
    }

    private static void registerYuneecSt16SwitchStateListener() {
        if (yuneecSt16SwitchStateListener == null) {
            Log.d(TAG, "Initialized yuneecSt16 switch state listener");
            yuneecSt16SwitchStateListener = new YuneecSt16.SwitchStateListener() {
                @Override
                public void onChangeCallback(YuneecSt16.SwitchId switchId, YuneecSt16.SwitchState switchState) {
                    Log.d(TAG, "Switch " + switchId.toString() + " changed to " + switchState.toString());
                    onChangeListener.publishYuneecSt16Result("Switch " + switchId.toString() + " changed to " +
                                                             switchState.toString());
                }
            };
        }
        YuneecSt16.setSwitchStateListener(yuneecSt16SwitchStateListener);
    }

    private static void registerYuneecSt16GpsListener() {
        if (yuneecSt16GpsListener == null) {
            Log.d(TAG, "Initialized yuneecSt16 GPS position listener");
            yuneecSt16GpsListener = new YuneecSt16.GpsPositionListener() {
                @Override
                public void onCallback(YuneecSt16.GpsPosition gpsPosition) {
                    Log.d(TAG, "ST16: Latitude: " + gpsPosition.latitudeDeg + ", longitude: " +
                          gpsPosition.longitudeDeg);
                    Log.d(TAG, "ST16: Altitude: " + gpsPosition.absoluteAltitudeM);
                    Log.d(TAG, "ST16: Num satellites: " + gpsPosition.numSatellites);
                    Log.d(TAG, "ST16: PDOP: " + gpsPosition.pdop);
                    Log.d(TAG, "ST16: Speed: " + gpsPosition.speedMs);
                    Log.d(TAG, "ST16: Heading: " + gpsPosition.headingDeg);
                    // TODO: Display required Gps data to the user
                    //onChangeListener.publishYuneecSt16Result("ST16: Latitude: " + gpsPosition.latitudeDeg +
                    //                                         ", longitude: " +
                    //                                         gpsPosition.longitudeDeg);
                }
            };
        }
        YuneecSt16.setGpsPositionListener(yuneecSt16GpsListener);
    }

    private static void registerYuneecSt16VersionListener() {
        if (yuneecSt16VersionListener == null) {
            Log.d(TAG, "Initialized yuneecSt16 GPS position listener");
            yuneecSt16VersionListener = new YuneecSt16.M4VersionListener() {
                @Override
                public void onCallback(YuneecSt16.Result result, YuneecSt16.M4Version version) {
                    if (result.resultID == YuneecSt16.Result.ResultID.SUCCESS) {
                        Log.d(TAG, "ST16: Got version callback");
                        onChangeListener.publishYuneecSt16Result("Version: " + version.major +
                                                                 "." +
                                                                 version.minor +
                                                                 "." +
                                                                 version.patch);
                    } else {
                        Log.d(TAG, "ST16: could not get version");
                    }
                }
            };
        }
        YuneecSt16.setGpsPositionListener(yuneecSt16GpsListener);
    }

    public static void unRegisterYuneecSt16Listeners() {

        if (yuneecSt16ResultListener != null) {
            yuneecSt16ResultListener = null;
        }

        if (yuneecSt16ButtonStateListener != null) {
            yuneecSt16ButtonStateListener = null;
        }

        if (yuneecSt16SwitchStateListener != null) {
            yuneecSt16SwitchStateListener = null;
        }

        if (yuneecSt16GpsListener != null) {
            yuneecSt16GpsListener = null;
        }

        if (yuneecSt16VersionListener != null) {
            yuneecSt16VersionListener = null;
        }
    }
}
