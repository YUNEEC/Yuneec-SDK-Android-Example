package com.yuneec.example.component.listeners;

import android.util.Log;
import com.yuneec.sdk.Connection;

/**
 * Created by sushma on 8/8/17.
 */

public
class ConnectionListener
{

	 private static Connection.Listener connectionListener = null;

	 private static final String TAG = "ConnectionListener";


	 public static
	 void registerConnectionListener ( )
	 {

			if ( connectionListener == null )
			{
				 connectionListener = new Connection.Listener ( )
				 {

						@Override
						public
						void onDiscoverCallback ( )
						{

							/*runOnUiThread ( new Runnable ( )
							 {

									@Override
									public
									void run ( )
									{

										 TextView ConnectionStateTV = ( TextView ) findViewById ( R.id.connection_state_text );
										 ConnectionStateTV.setText ( "Discovered device" );
									}
							 } );*/

							 Log.d ( TAG, "Connected" );

						}

						@Override
						public
						void onTimeoutCallback ( )
						{

							 /*runOnUiThread ( new Runnable ( )
							 {

									@Override
									public
									void run ( )
									{

										 TextView ConnectionStateTV = ( TextView ) findViewById ( R.id.connection_state_text );
										 ConnectionStateTV.setText ( "Timed out" );
									}
							 } );*/
							 Log.d ( TAG, " Not Connected" );
						}
				 };

				 Connection.Result result = Connection.addConnection ( );
				 if ( result.resultID != Connection.Result.ResultID.SUCCESS )
				 {
						//Toast.makeText ( this, "addConnection failed: " + result.resultStr, Toast.LENGTH_SHORT )
						//	 .show ( );
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
