/**
 * MainActivity.java
 * Yuneec-SDK-Android-Example
 *
 * Copyright @ 2016-2017 Yuneec.
 * All rights reserved.
 */
package com.yuneec.example;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.yuneec.sdk.Connection;

/**
 * Simple example based on the Yuneec SDK for Android
 *
 * The example has 3 tabs (fragments) called Telemetry, Action, Mission.
 */
public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("telemetry").setIndicator("Telemetry"),
                TelemetryFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("action").setIndicator("Action"),
                ActionFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("mission").setIndicator("Mission"),
                MissionFragment.class, null);

        Connection.Listener listener = new Connection.Listener() {

            @Override
            public void onDiscoverCallback() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView ConnectionStateTV = (TextView) findViewById(
                                R.id.connection_state_text);
                        ConnectionStateTV.setText("Discovered device");
                    }
                });
            }

            @Override
            public void onTimeoutCallback() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView ConnectionStateTV = (TextView) findViewById(
                                R.id.connection_state_text);
                        ConnectionStateTV.setText("Timed out");
                    }
                });
            }
        };

        TextView ConnectionStateTV = (TextView) findViewById(R.id.connection_state_text);
        ConnectionStateTV.setText("Not connected");

        Toast.makeText(this, "connect: " + Connection.connect(), Toast.LENGTH_SHORT).show();

        Connection.addListener(listener);
    }
}


