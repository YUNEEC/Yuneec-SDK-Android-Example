package com.yuneec.example.component.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.yuneec.example.R;
import com.yuneec.example.component.listeners.GimbalListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.sdk.Gimbal;

/**
 * Created by sushma on 8/15/17.
 */

public
class GimbalFragment
				extends Fragment
				implements View.OnClickListener
{

	 private View rootView;

	 private Button rotateClockwise;

	 private Button rotateAnticlockwise;

	 private Button rotateToInitial;


	 @Override
	 public
	 void onCreate ( Bundle savedInstanceState )
	 {

			super.onCreate ( savedInstanceState );
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
			return rootView;
	 }

	 @Override
	 public
	 void onStart ( )
	 {

			super.onStart ( );
			registerListener ( );
	 }

	 @Override
	 public
	 void onStop ( )
	 {

			super.onStop ( );
			unRegisterListener ( );
	 }


	 private
	 void initViews ( LayoutInflater inflater,
										ViewGroup container )
	 {

			rootView = inflater.inflate ( R.layout.gimbal_layout, container, false );
			rotateClockwise = ( Button ) rootView.findViewById ( R.id.rotate_camera_clockwise );
			rotateClockwise.setOnClickListener ( this );
			rotateAnticlockwise = ( Button ) rootView.findViewById ( R.id.rotate_camera_anticlockwise );
			rotateAnticlockwise.setOnClickListener ( this );
			rotateToInitial = ( Button ) rootView.findViewById ( R.id.rotate_to_initial );
			rotateToInitial.setOnClickListener ( this );
	 }

	 private
	 void registerListener ( )
	 {

			GimbalListener.registerGimbalListener ( );
	 }

	 private
	 void unRegisterListener ( )
	 {

			GimbalListener.unRegisterGimbalListener ( );
	 }

	 @Override
	 public
	 void onClick ( View v )
	 {

			switch ( v.getId ( ) )
			{
				 case R.id.rotate_camera_clockwise:
						Gimbal.asyncSetPitchAndYawOfJni ( 0, Common.currentRotation + Common.fixedRotationAngle,
																							GimbalListener.getGimbaListener ( ) );
						Common.currentRotation += Common.fixedRotationAngle;
						break;
				 case R.id.rotate_camera_anticlockwise:
						Gimbal.asyncSetPitchAndYawOfJni ( 0, Common.currentRotation - Common.fixedRotationAngle,
																							GimbalListener.getGimbaListener ( ) );
						Common.currentRotation -= Common.fixedRotationAngle;
						break;
				 case R.id.rotate_to_initial:
						Gimbal.asyncSetPitchAndYawOfJni ( 0, 0, GimbalListener.getGimbaListener ( ) );
						Common.currentRotation = 0;
						break;

			}
	 }
}
