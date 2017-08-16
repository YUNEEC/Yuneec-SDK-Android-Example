/**
 * MainActivity.java Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec. All rights reserved.
 */

package com.yuneec.example.component.actitivty;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import com.yuneec.example.R;
import com.yuneec.example.component.custom_callback.OnConnectionChangeListener;
import com.yuneec.example.component.fragment.*;
import com.yuneec.example.component.listeners.ConnectionListener;

/**
 * Simple example based on the Yuneec SDK for Android
 * <p>
 * The example has 3 tabs (fragments) called Telemetry, Action, Mission.
 */
public
class MainActivity
				extends FragmentActivity
				implements OnConnectionChangeListener
{

	 private FragmentTabHost mTabHost;

	 //TextView connectionStateText;

	 private static final String TAG = MainActivity.class.getCanonicalName ( );

	 @Override
	 protected
	 void onCreate ( Bundle savedInstanceState )
	 {

			super.onCreate ( savedInstanceState );
			setContentView ( R.layout.main_activity );

			mTabHost = ( FragmentTabHost ) findViewById ( android.R.id.tabhost );
			mTabHost.setup ( this, getSupportFragmentManager ( ), R.id.tabcontent );
			mTabHost.addTab ( mTabHost.newTabSpec ( "connection" )
																.setIndicator ( "Connection Info" ), ConnectionFragment.class, null );
			mTabHost.addTab ( mTabHost.newTabSpec ( "telemetry" )
																.setIndicator ( "Telemetry" ), TelemetryFragment.class, null );
			mTabHost.addTab ( mTabHost.newTabSpec ( "action" )
																.setIndicator ( "Action" ), ActionFragment.class, null );
			mTabHost.addTab ( mTabHost.newTabSpec ( "mission" )
																.setIndicator ( "Mission" ), MissionFragment.class, null );
			mTabHost.addTab ( mTabHost.newTabSpec ( "camera" )
																.setIndicator ( "Camera" ), CameraFragment.class, null );
			mTabHost.addTab ( mTabHost.newTabSpec ( "gimbal" )
																.setIndicator ( "Gimbal" ), GimbalFragment.class, null );

			//connectionStateText = ( TextView ) findViewById ( R.id.connection_state_text );
			//connectionStateText.setText ( "Not connected" );
	 }

	 @Override
	 protected
	 void onStart ( )
	 {

			super.onStart ( );
			registerListeners ( );
	 }

	 @Override
	 protected
	 void onStop ( )
	 {

			super.onStop ( );
			unRegisterListeners ( );

	 }

	 private
	 void registerListeners ( )
	 {

			ConnectionListener.registerConnectionListener ( this );
	 }

	 private
	 void unRegisterListeners ( )
	 {

			ConnectionListener.unRegisterConnectionListener ( );
	 }

	 @Override
	 public
	 void publishConnectionStatus ( final String connectionStatus )
	 {

			runOnUiThread ( new Runnable ( )
			{

				 @Override
				 public
				 void run ( )
				 {

						Log.d ( TAG, connectionStatus );
						ConnectionFragment fragment = ( ConnectionFragment ) getSupportFragmentManager ( ).findFragmentByTag (
										"connection" );
						fragment.setConnectionStateView ( connectionStatus );

				 }
			} );
	 }

}


