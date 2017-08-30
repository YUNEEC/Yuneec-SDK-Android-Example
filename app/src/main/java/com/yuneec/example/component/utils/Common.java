package com.yuneec.example.component.utils;

/**
 * Created by sushma on 8/14/17.
 */

public class Common {

    public static final String VideoStreamUrl = "rtsp://192.168.42.1/live";

    public static final int fixedRotationAngleDeg = 20;

    public static int currentRotation = 0;

    public static final String connectionStatusDefault = "Not connected to the drone";

    public static final String batteryStatusDefault = "Unknown";

    public static final String healthStatusDefault = "Unknown";

    public static String connectionStatus = connectionStatusDefault;

    public static String healthStatus = healthStatusDefault;

    public static String batteryStatus = batteryStatusDefault;

}
