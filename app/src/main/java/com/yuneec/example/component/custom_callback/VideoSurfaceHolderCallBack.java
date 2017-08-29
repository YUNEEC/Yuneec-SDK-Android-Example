package com.yuneec.example.component.custom_callback;

import android.content.Context;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.yuneec.example.component.utils.Common;
import com.yuneec.rtvplayer.RTVPlayer;

/**
 * Created by sushma on 8/14/17.
 */

public class VideoSurfaceHolderCallBack
        implements SurfaceHolder.Callback {

    private RTVPlayer rtvPlayer;

    private Surface surface;

    private Context context;

    private final static String TAG = VideoSurfaceHolderCallBack.class.getCanonicalName();

    public VideoSurfaceHolderCallBack(Context context,
                                      Surface surface,
                                      RTVPlayer rtvPlayer) {

        this.context = context;
        this.surface = surface;
        this.rtvPlayer = rtvPlayer;
    }

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
        rtvPlayer.setSurface(surface);
        Log.d(TAG, "Surface changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        rtvPlayer.deinit();
    }

    public void preparePlayer() {

        rtvPlayer.play(Common.VideoStreamUrl);
    }


}
