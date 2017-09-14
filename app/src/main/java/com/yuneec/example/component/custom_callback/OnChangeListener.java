package com.yuneec.example.component.custom_callback;

import com.yuneec.sdk.Camera;

/**
 * Created by sushma on 8/18/17.
 */

public interface OnChangeListener {

    void publishConnectionStatus(String connectionStatus);

    void publishBatteryChangeStatus(String batteryStatus);

    void publishHealthChangeStatus(String healthStatus);

    void publishCameraResult(String result);

    void publishCameraModeResult(Camera.Mode mode, String result);
}
