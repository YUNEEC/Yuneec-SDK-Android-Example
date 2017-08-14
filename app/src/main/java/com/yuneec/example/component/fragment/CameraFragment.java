/**
 * CameraFragment.java Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec. All rights reserved.
 */

package com.yuneec.example.component.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.yuneec.example.R;
import com.yuneec.example.component.custom_callback.VideoSurfaceHolderCallBack;
import com.yuneec.example.component.listeners.CameraListener;
import com.yuneec.rtvplayer.RTVPlayer;
import com.yuneec.sdk.Camera;

import java.util.ArrayList;

public
class CameraFragment
				extends Fragment
				implements SwipeRefreshLayout.OnRefreshListener
{

	 private View rootView;

	 private static final String TAG = CameraFragment.class.getCanonicalName ( );

	 private Toast toast = null;

	 Button capturePicture;

	 SurfaceView videoStreamView;

	 Surface videoStreamSurface;

	 SurfaceHolder surfaceHolder;

	 Surface videoSurface;

	 RTVPlayer rtvPlayer;

	 VideoSurfaceHolderCallBack videoSurfaceHolderCallBack;


	 @Override
	 public
	 void onCreate ( Bundle savedInstanceState )
	 {

			super.onCreate ( savedInstanceState );
	 }

	 @Override
	 public
	 View onCreateView ( LayoutInflater inflater,
											 ViewGroup container,
											 Bundle savedInstanceState )
	 {

			super.onCreate ( savedInstanceState );
			setRetainInstance ( true );
			initViews ( inflater, container );
			addOnClickListeners ( );
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
			rtvPlayer.deinit ( );
			if ( videoSurfaceHolderCallBack != null )
			{
				 videoSurfaceHolderCallBack = null;
			}
	 }

	 @Override
	 public
	 void onPause ( )
	 {

			super.onPause ( );
			rtvPlayer.stop ( );
	 }

	 @Override
	 public
	 void onResume ( )
	 {

			super.onResume ( );

	 }

	 private
	 void initViews ( LayoutInflater inflater,
										ViewGroup container )
	 {

			rootView = inflater.inflate ( R.layout.camera_layout, container, false );
			capturePicture = ( Button ) rootView.findViewById ( R.id.capturePicture );
			videoStreamView = ( SurfaceView ) rootView.findViewById ( R.id.video_live_stream_view );
			surfaceHolder = videoStreamView.getHolder ( );
			rtvPlayer = RTVPlayer.getPlayer ( RTVPlayer.PLAYER_FFMPEG );
			rtvPlayer.init ( getActivity ( ), RTVPlayer.IMAGE_FORMAT_YV12, false );
			videoSurfaceHolderCallBack = new VideoSurfaceHolderCallBack ( getActivity ( ), videoSurface, rtvPlayer );
			surfaceHolder.addCallback ( videoSurfaceHolderCallBack );
	 }


	 private
	 void registerListener ( )
	 {

			CameraListener.registerCameraListener ( );
	 }

	 private
	 void unRegisterListener ( )
	 {

			CameraListener.unRegisterCameraListener ( );
	 }


	 private
	 void addOnClickListeners ( )
	 {

			capturePicture.setOnClickListener ( new View.OnClickListener ( )
			{

				 @Override
				 public
				 void onClick ( View v )
				 {

						Camera.asyncTakePhoto ( );
				 }
			} );
	 }

	 public static
	 class MediaInfoEntry
	 {

			public String path;

			public String title;

			public String description;

			public boolean downloaded = false;
	 }

	 public
	 class ListviewMediaInfosAdapter
					 extends BaseAdapter
	 {

			private ArrayList< MediaInfoEntry > entries;

			private LayoutInflater inflater;

			public
			ListviewMediaInfosAdapter ( Context context,
																	ArrayList< MediaInfoEntry > list )
			{

				 entries = list;
				 inflater = LayoutInflater.from ( context );
			}

			public
			MediaInfoEntry entryFromMediaInfo ( Camera.MediaInfo mediaInfo )
			{

				 MediaInfoEntry entry = new MediaInfoEntry ( );
				 entry.path = mediaInfo.path;
				 entry.description = String.format ( "%.1f MiB", mediaInfo.size_mib );
				 // We want to split "100MEDIA/YUN00001.jpg" to "YUN00001.jpg"
				 String[] parts = entry.path.split ( "/" );
				 entry.title = parts[ 1 ];

				 return entry;
			}

			public
			void setEntries ( ArrayList< Camera.MediaInfo > list )
			{

				 entries.clear ( );
				 for ( Camera.MediaInfo item : list )
				 {
						entries.add ( entryFromMediaInfo ( item ) );
				 }
			}

			@Override
			public
			int getCount ( )
			{

				 return entries.size ( );
			}

			@Override
			public
			MediaInfoEntry getItem ( int index )
			{

				 return entries.get ( index );
			}

			@Override
			public
			long getItemId ( int index )
			{

				 return index;
			}

			public
			View getView ( int index,
										 View convertView,
										 ViewGroup parent )
			{

				 ViewHolder holder;
				 if ( convertView == null )
				 {
						convertView = inflater.inflate ( android.R.layout.simple_list_item_2, null );
						holder = new ViewHolder ( );
						holder.text1 = ( TextView ) convertView.findViewById ( android.R.id.text1 );
						holder.text2 = ( TextView ) convertView.findViewById ( android.R.id.text2 );

						convertView.setTag ( holder );
				 }
				 else
				 {
						holder = ( ViewHolder ) convertView.getTag ( );
				 }

				 MediaInfoEntry entry = entries.get ( index );
				 holder.text1.setText ( entry.title );
				 holder.text2.setText ( entry.description );

				 if ( entry.downloaded )
				 {
						convertView.setBackgroundColor ( Color.argb ( 20, 0, 0, 255 ) );
				 }
				 else
				 {
						convertView.setBackgroundColor ( Color.WHITE );
				 }

				 return convertView;
			}

			class ViewHolder
			{

				 TextView text1, text2;
			}
	 }

	 @Override
	 public
	 void onRefresh ( )
	 {

			refreshIndex ( );
	 }

	 public
	 void refreshIndex ( )
	 {

			//Camera.getMediaInfosAsync ( mediaInfoslistener );
	 }

	 private
	 void updateToast ( String text )
	 {

			if ( toast != null )
			{
				 toast.cancel ( );
			}
			toast = Toast.makeText ( rootView.getContext ( ), text, Toast.LENGTH_SHORT );
			toast.show ( );
	 }
}
