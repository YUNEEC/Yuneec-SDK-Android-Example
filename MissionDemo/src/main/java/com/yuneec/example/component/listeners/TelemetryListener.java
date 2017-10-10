package com.yuneec.example.component.listeners;

import android.content.Context;
import android.util.Log;

import com.yuneec.example.component.custom_callback.OnChangeListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.sdk.Telemetry;

/**
 * Created by sushma on 8/18/17.
 */

public class TelemetryListener {

    private static Telemetry.BatteryListener batteryListener = null;

    private static Telemetry.HealthListener healthListener = null;

    private static Telemetry.PositionListener positionListener = null;

    private static Telemetry.GPSInfoListener gpsInfoListener = null;

    private static final String TAG = TelemetryListener.class.getCanonicalName();

    private static OnChangeListener onChangeListener;

    public static void registerBatteryListener(final Context context) {

        onChangeListener = (OnChangeListener) context;
        if (batteryListener == null) {
            Log.d(TAG, "Initialized battery result listener");
            batteryListener = new Telemetry.BatteryListener() {
                @Override
                public void onBatteryCallback(Telemetry.Battery battery) {

                    ((OnChangeListener) context).publishBatteryChangeStatus(
                        String.format("%d", (int)(100 * battery.remainingPercent)));
                    Log.d(TAG, String.format("%d", (int)(100 * battery.remainingPercent)));
                }
            };
        }
        Telemetry.setBatteryListener(batteryListener);
    }

    public static void unRegisterBatteryListener() {

        Common.batteryStatus = Common.batteryStatusDefault;
        if (batteryListener != null) {
            batteryListener = null;
        }
    }

    public static void registerHealthListener(final Context context) {

        onChangeListener = (OnChangeListener) context;
        if (healthListener == null) {
            Log.d(TAG, "Initialized health result listener");
            healthListener = new Telemetry.HealthListener() {

                @Override
                public void onHealthCallback(Telemetry.Health health) {

                    boolean calibrationOk = health.accelerometerCalibrationOk &&
                                            health.gyrometerCalibrationOk &&
                                            health.magnetometerCalibrationOk &&
                                            health.levelCalibrationOk;

                    boolean positionOk = health.globalPositionOk &&
                                         health.localPositionOk &&
                                         health.homePositionOk;
                    if (calibrationOk && positionOk) {
                        ((OnChangeListener) context).publishHealthChangeStatus("Calibration and position is okay");
                    } else {
                        if (calibrationOk) {
                            ((OnChangeListener) context).publishHealthChangeStatus("Position is not okay");

                        } else {
                            ((OnChangeListener) context).publishHealthChangeStatus("Calibration is not okay");
                        }
                    }
                }
            };
        }

        Telemetry.setHealthListener(healthListener);
    }

    public static void unRegisterHealthListener() {

        Common.healthStatus = Common.healthStatusDefault;
        if (healthListener != null) {
            healthListener = null;
        }
    }

    public static void registerPositionListener(final Context context) {

        onChangeListener = (OnChangeListener) context;
        if (positionListener == null) {
            Log.d(TAG, "Initialized position listener");
            positionListener = new Telemetry.PositionListener() {

                @Override
                public void onPositionCallback(Telemetry.Position position) {
                    Log.d(TAG, position.latitudeDeg + " " + position.longitudeDeg);
                    Common.droneLat = position.latitudeDeg;
                    Common.droneLong = position.longitudeDeg;
                }
            };
        }
        Telemetry.setPositionListener(positionListener);
    }

    public static void unRegisterPositionListener() {

        if (positionListener != null) {
            positionListener = null;
        }
    }

    public static void registerGPSListener(final Context context) {

        onChangeListener = (OnChangeListener) context;
        if (gpsInfoListener == null) {
            Log.d(TAG, "Initialized gps info listener");
            gpsInfoListener = new Telemetry.GPSInfoListener() {
                @Override
                public void onGPSInfoCallback(Telemetry.GPSInfo gpsInfo) {
                    Log.d(TAG, "GPS info - num of satellites" + gpsInfo.numSatellites );
                }
            };
        }
        Telemetry.setGPSInfoListener(gpsInfoListener);
    }

    public static void unRegisterGPSListener() {

        if (gpsInfoListener != null) {
            gpsInfoListener = null;
        }
    }
}
