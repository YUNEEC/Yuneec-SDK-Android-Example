package com.yuneec.example.component.custom_callback;

import com.yuneec.sdk.Camera;
import com.yuneec.sdk.Telemetry;

/**
 * Created by sushma on 8/18/17.
 */

public interface OnChangeListener {

    void publishConnectionStatus(String connectionStatus);

    void publishBatteryChangeStatus(String batteryStatus);

    void publishHealthChangeStatus(String healthStatus);

    void publishPositionStatus(Telemetry.Position position);

    void publishMissionStatus(String missionStatus);
}
