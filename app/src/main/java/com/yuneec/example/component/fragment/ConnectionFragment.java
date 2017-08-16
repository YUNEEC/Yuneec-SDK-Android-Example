package com.yuneec.example.component.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yuneec.example.R;
import com.yuneec.example.component.utils.Common;

/**
 * Created by sushma on 8/16/17.
 */

public
class ConnectionFragment
				extends Fragment

{

	 private View rootView;

	 TextView connectionStateView;

	 private static final String TAG = ConnectionFragment.class.getCanonicalName ( );


	 @Override
	 public
	 void onCreate ( Bundle savedInstanceState )
	 {

			super.onCreate ( savedInstanceState );
			Log.d ( TAG, "on create" );
	 }

	 @Nullable
	 @Override
	 public
	 View onCreateView ( LayoutInflater inflater,
											 ViewGroup container,
											 Bundle savedInstanceState )
	 {

			setRetainInstance ( true );
			initViews ( inflater, container );
			Log.d ( TAG, "on create view" );
			return rootView;
	 }

	 @Override
	 public
	 void onStart ( )
	 {

			super.onStart ( );
	 }

	 @Override
	 public
	 void onStop ( )
	 {

			super.onStop ( );
	 }

	 @Override
	 public
	 void onPause ( )
	 {

			super.onPause ( );
	 }

	 @Override
	 public
	 void onResume ( )
	 {

			super.onResume ( );
	 }

	 @Override
	 public
	 void onViewStateRestored (
																		@Nullable
																						Bundle savedInstanceState )
	 {

			super.onViewStateRestored ( savedInstanceState );
			Log.d ( TAG, "on restore" );
			connectionStateView.setText ( Common.connectionStatus );

	 }

	 private
	 void initViews ( LayoutInflater inflater,
										ViewGroup container )
	 {

			rootView = inflater.inflate ( R.layout.connection_layout, container, false );
			connectionStateView = ( TextView ) rootView.findViewById ( R.id.connection_state );
	 }


	 public
	 void setConnectionStateView ( String connectionStatus )
	 {

			Common.connectionStatus = connectionStatus;
			connectionStateView.setText ( connectionStatus );
			Log.d ( TAG, "connection view text set" );
	 }

}
