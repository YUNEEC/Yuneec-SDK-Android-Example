package com.yuneec.example.component.listeners;

import android.util.Log;
import com.yuneec.sdk.Camera;

/**
 * Created by sushma on 8/8/17.
 */

public
class CameraListener
{

	 private static Camera.ResultListener cameraResultListener = null;

	 private static final String TAG = "CameraResultListener";

	 public static
	 void initCameraListener ( )
	 {

			if ( cameraResultListener == null )
			{
				 Log.d ( TAG, "Initialized camera result listener" );
				 cameraResultListener = new Camera.ResultListener ( )
				 {

						@Override
						public
						void resultCallback ( Camera.Result result )
						{
							 //Message msg = handler.obtainMessage();
							 //msg.obj = result.resultStr;
							 //msg.sendToTarget();
							 Log.d ( TAG, result.resultStr );
						}


				 };
				 Camera.setResultListener ( cameraResultListener );
			}
	 }
}
