package com.yuneec.example.component.listeners;

import android.content.Context;
import android.util.Log;
import com.yuneec.example.component.custom_callback.OnConnectionChangeListener;
import com.yuneec.sdk.Connection;

/**
 * Created by sushma on 8/8/17.
 */

public
class ConnectionListener
{

	 private static Connection.Listener connectionListener = null;

	 private static final String TAG = ConnectionListener.class.getCanonicalName ( );

	 private static OnConnectionChangeListener onConnectionChange;

	 public static
	 void registerConnectionListener ( Context context )
	 {

			onConnectionChange = ( OnConnectionChangeListener ) context;
			if ( connectionListener == null )
			{
				 connectionListener = new Connection.Listener ( )
				 {

						@Override
						public
						void onDiscoverCallback ( )
						{

							 onConnectionChange.publishConnectionStatus ( "Discovered" );
							 Log.d ( TAG, "Connected" );

						}

						@Override
						public
						void onTimeoutCallback ( )
						{

							 onConnectionChange.publishConnectionStatus ( "Connection time out" );
							 Log.d ( TAG, " Not Connected" );
						}
				 };

				 Connection.Result result = Connection.addConnection ( );
				 if ( result.resultID != Connection.Result.ResultID.SUCCESS )
				 {
						onConnectionChange.publishConnectionStatus ( result.resultStr );
						Log.d ( TAG, result.resultStr );
				 }

				 Connection.addListener ( connectionListener );
			}
	 }

	 public static
	 void unRegisterConnectionListener ( )
	 {

			Connection.removeConnection ( );
			if ( connectionListener != null )
			{
				 connectionListener = null;
			}
	 }
}
