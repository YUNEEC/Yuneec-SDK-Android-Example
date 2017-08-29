package com.yuneec.example.component.custom_callback;

/**
 * Created by sushma on 8/18/17.
 */

public interface OnChangeListener {

    void publishConnectionStatus(String connectionStatus);

    void publishBatteryChangeStatus(String batteryStatus);

    void publishHealthChangeStatus(String healthStatus);

    void publishCameraResult(String result);
}
