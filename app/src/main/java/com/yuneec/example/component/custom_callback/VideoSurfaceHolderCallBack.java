package com.yuneec.example.component.custom_callback;

import android.content.Context;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.yuneec.example.component.utils.Common;

/**
 * Created by sushma on 8/14/17.
 */

public class VideoSurfaceHolderCallBack
    implements SurfaceHolder.Callback {

    private Surface surface;

    private Context context;

    private final static String TAG = VideoSurfaceHolderCallBack.class.getCanonicalName();

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        preparePlayer();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format,
                               int width,
                               int height) {

        surface = holder.getSurface();
        Log.d(TAG, "Surface changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO: remove player
    }

    public void preparePlayer() {
        // TODO: add player
    }


}
