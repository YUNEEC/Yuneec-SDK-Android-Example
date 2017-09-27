package com.yuneec.example.component.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.yuneec.example.R;

/**
 * Created by sushma on 8/14/17.
 */

public class Common {

    public static final String VideoStreamUrl = "rtsp://192.168.42.1/live";

    public static final int fixedRotationAngleDeg = 20;

    public static final String connectionStatusDefault = "Not connected to the drone";

    public static final String batteryStatusDefault = "Unknown";

    public static final String healthStatusDefault = "Unknown";

    public static String connectionStatus = connectionStatusDefault;

    public static String healthStatus = healthStatusDefault;

    public static String batteryStatus = batteryStatusDefault;

    public static boolean isConnected = false;

    public static int defaultPhotoIntervalInSeconds = 4;

    public static void makeToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setBackgroundColor(ContextCompat.getColor(context, R.color.greyDark));
        v.setTextColor(ContextCompat.getColor(context, R.color.white));
        toast.show();
    }

    public static float currentYaw = 0.0f;
    public static float currentPitch = 0.0f;

}
