/**
 * TelemetryFragment.java
 * Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec.
 * All rights reserved.
 */
package com.yuneec.example.component.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yuneec.example.R;
import com.yuneec.example.component.listeners.TelemetryListener;
import com.yuneec.sdk.Connection;
import com.yuneec.sdk.Telemetry;

import java.util.ArrayList;


public class TelemetryFragment extends Fragment {

    class TelemetryIndices {
        public final static int LATITUDE = 0;
        public final static int LONGITUDE = 1;
        public final static int RELATIVE_ALTITUDE = 2;
        public final static int BATTERY = 3;
        public final static int ROLL = 4;
        public final static int PITCH = 5;
        public final static int YAW = 6;
        public final static int VELOCITY_NORTH = 7;
        public final static int VELOCITY_EAST = 8;
        public final static int VELOCITY_UP = 9;
        public final static int FLIGHT_MODE = 10;
        public final static int HEALTH = 11;
        public final static int GPS_NO_OF_SATELLITES = 12;
        public final static int HOME_POSITION_LATITUDE = 13;
        public final static int HOME_POSITION_LONGITUDE = 14;
        public final static int IS_ARMED = 15;
        public final static int IN_AIR = 16;
        public final static int RC_STATUS = 17;
    }

    ;

    private ListviewTelemetryAdapter adapter;

    public class TelemetryEntry {

        public TelemetryEntry(String new_description, String new_unit) {
            description = new_description;
            unit = new_unit;
            value = "-";
        }

        public String description;
        public String value;
        public String unit;
    }

    public class ListviewTelemetryAdapter extends BaseAdapter {
        private ArrayList<TelemetryEntry> entries;
        private LayoutInflater inflater;

        public ListviewTelemetryAdapter(Context context, ArrayList<TelemetryEntry> list) {
            entries = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return entries.size();
        }

        @Override
        public Object getItem(int index) {
            return entries.get(index);
        }

        public void setItemValue(int index, String new_value) {
            entries.get(index).value = new_value;
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        public View getView(int index, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_2, null);
                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
                holder.text2 = (TextView) convertView.findViewById(android.R.id.text2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text1.setText(entries.get(index).description);
            holder.text2.setText(entries.get(index).value + " " + entries.get(index).unit);

            return convertView;
        }

        class ViewHolder {
            TextView text1, text2;
        }
    }

    public class PositionListener implements Telemetry.PositionListener {

        @Override
        public void onPositionCallback(Telemetry.Position position) {
            adapter.setItemValue(TelemetryIndices.RELATIVE_ALTITUDE,
                                 String.format("%.1f", position.relativeAltitudeM));
            adapter.setItemValue(TelemetryIndices.LATITUDE,
                                 String.format("%.6f", position.latitudeDeg));
            adapter.setItemValue(TelemetryIndices.LONGITUDE,
                                 String.format("%.6f", position.longitudeDeg));

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class AttitudeEulerAngleListener implements Telemetry.AttitudeEulerAngleListener {

        @Override
        public void onAttitudeEulerAngleCallback(Telemetry.AttitudeEulerAngle attitude) {

            adapter.setItemValue(TelemetryIndices.ROLL,
                                 String.format("%d", (int) attitude.rollDeg));
            adapter.setItemValue(TelemetryIndices.PITCH,
                                 String.format("%d", (int) attitude.pitchDeg));
            adapter.setItemValue(TelemetryIndices.YAW,
                                 String.format("%d", (int) attitude.yawDeg));

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class BatteryListener implements Telemetry.BatteryListener {

        @Override
        public void onBatteryCallback(Telemetry.Battery battery) {

            adapter.setItemValue(TelemetryIndices.BATTERY,
                                 String.format("%d", (int)(100 * battery.remainingPercent)));

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class GroundSpeedNEDListener implements Telemetry.GroundSpeedNEDListener {

        @Override
        public void onGroundSpeedNEDCallback(Telemetry.GroundSpeedNED groundSpeedNED) {

            adapter.setItemValue(TelemetryIndices.VELOCITY_NORTH,
                                 String.format("%.1f", groundSpeedNED.velocityNorthMS));
            adapter.setItemValue(TelemetryIndices.VELOCITY_EAST,
                                 String.format("%.1f", groundSpeedNED.velocityEastMS));
            adapter.setItemValue(TelemetryIndices.VELOCITY_UP,
                                 String.format("%.1f", (-1) * groundSpeedNED.velocityDownMS));

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class FlightModeListener implements Telemetry.FlightModeListener {

        @Override
        public void onFlightModeCallback(Telemetry.FlightMode flightMode) {

            adapter.setItemValue(TelemetryIndices.FLIGHT_MODE, flightMode.flightModeStr);

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class HealthListener implements Telemetry.HealthListener {

        @Override
        public void onHealthCallback(Telemetry.Health health) {

            boolean calibrationOk = health.accelerometerCalibrationOk &&
                                    health.gyrometerCalibrationOk &&
                                    health.magnetometerCalibrationOk &&
                                    health.levelCalibrationOk;

            boolean positionOk = health.globalPositionOk &&
                                 health.localPositionOk &&
                                 health.homePositionOk;

            adapter.setItemValue(TelemetryIndices.HEALTH,
                                 String.format("calibration: %s, position: %s",
                                               calibrationOk ? "ok" : "not ok",
                                               positionOk ? "ok" : "not ok"));

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class GPSInfoListener implements Telemetry.GPSInfoListener {

        @Override
        public void onGPSInfoCallback(Telemetry.GPSInfo gpsInfo) {
            adapter.setItemValue(TelemetryIndices.GPS_NO_OF_SATELLITES, String.valueOf(gpsInfo.numSatellites));

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class HomePositionListener implements Telemetry.HomePositionListener {

        @Override
        public void onHomePositionCallback(Telemetry.Position position) {
            adapter.setItemValue(TelemetryIndices.HOME_POSITION_LATITUDE, String.valueOf(position.latitudeDeg));
            adapter.setItemValue(TelemetryIndices.HOME_POSITION_LONGITUDE,
                                 String.valueOf(position.longitudeDeg));

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class ArmedListener implements Telemetry.ArmedListener {

        @Override
        public void onIsArmedCallback(boolean b) {
            adapter.setItemValue(TelemetryIndices.IS_ARMED, String.valueOf(b));

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class InAirListener implements Telemetry.InAirListener {

        @Override
        public void onIsInAirCallback(boolean b) {
            adapter.setItemValue(TelemetryIndices.IN_AIR, String.valueOf(b));

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class RCStatusListener implements Telemetry.RCStatusListener {

        @Override
        public void onRcStatusCallback(Telemetry.RCStatus rcStatus) {
            adapter.setItemValue(TelemetryIndices.RC_STATUS,  String.format("%d",
                                                                            (int)(100 * rcStatus.signalStrengthPercent)));

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        YCListener listener = new YCListener();
        Connection.addListener(listener);

        ArrayList<TelemetryEntry> list = GetInitialTelemetrylist();

        adapter = new ListviewTelemetryAdapter(getActivity(), list);

        subscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.telemetry_example, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.telemetry_list);
        lv.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<TelemetryEntry> GetInitialTelemetrylist() {
        final ArrayList<TelemetryEntry> list = new ArrayList<TelemetryEntry>();
        list.add(TelemetryIndices.LATITUDE, new TelemetryEntry("Latitude", "deg"));
        list.add(TelemetryIndices.LONGITUDE, new TelemetryEntry("Longitude", "deg"));
        list.add(TelemetryIndices.RELATIVE_ALTITUDE, new TelemetryEntry("Altitude", "meters"));
        list.add(TelemetryIndices.BATTERY, new TelemetryEntry("Battery", "%"));
        list.add(TelemetryIndices.ROLL, new TelemetryEntry("Roll", "deg"));
        list.add(TelemetryIndices.PITCH, new TelemetryEntry("Pitch", "deg"));
        list.add(TelemetryIndices.YAW, new TelemetryEntry("Yaw", "deg"));
        list.add(TelemetryIndices.VELOCITY_NORTH, new TelemetryEntry("Velocity North", "m/s"));
        list.add(TelemetryIndices.VELOCITY_EAST, new TelemetryEntry("Velocity East", "m/s"));
        list.add(TelemetryIndices.VELOCITY_UP, new TelemetryEntry("Velocity Up", "m/s"));
        list.add(TelemetryIndices.FLIGHT_MODE, new TelemetryEntry("Flight mode", ""));
        list.add(TelemetryIndices.HEALTH, new TelemetryEntry("Health", ""));
        list.add(TelemetryIndices.GPS_NO_OF_SATELLITES, new TelemetryEntry("GPS Satellites", ""));
        list.add(TelemetryIndices.HOME_POSITION_LATITUDE, new TelemetryEntry("Home Position Latitude",
                                                                             "deg"));
        list.add(TelemetryIndices.HOME_POSITION_LONGITUDE, new TelemetryEntry("Home Position Longitude",
                                                                              "deg"));
        list.add(TelemetryIndices.IS_ARMED, new TelemetryEntry("Vehicle Armed", ""));
        list.add(TelemetryIndices.IN_AIR, new TelemetryEntry("In Air", ""));
        list.add(TelemetryIndices.RC_STATUS, new TelemetryEntry("RC Status", "%"));
        return list;
    }

    private void subscribe() {
        Telemetry.setPositionListener(new PositionListener());
        Telemetry.setGroundSpeedNEDListener(new GroundSpeedNEDListener());
        Telemetry.setAttitudeEulerAngleListener(new AttitudeEulerAngleListener());
        Telemetry.setBatteryListener(new BatteryListener());
        Telemetry.setFlightModeListener(new FlightModeListener());
        Telemetry.setHealthListener(new HealthListener());
        Telemetry.setGPSInfoListener(new GPSInfoListener());
        Telemetry.setHomePostionListener(new HomePositionListener());
        Telemetry.setArmedListener(new ArmedListener());
        Telemetry.setInAirListener(new InAirListener());
        Telemetry.setRCStatusListener(new RCStatusListener());
    }

    class YCListener implements Connection.Listener {

        @Override
        public void onDiscoverCallback() {
            // When a new device has connected, we need to subscribe all listeners again.
            subscribe();
        }

        @Override
        public void onTimeoutCallback() {
            // Do nothing, leave last values.
        }
    }

    ;
}
